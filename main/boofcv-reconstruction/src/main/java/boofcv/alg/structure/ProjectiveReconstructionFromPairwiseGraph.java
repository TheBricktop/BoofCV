/*
 * Copyright (c) 2021, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.alg.structure;

import boofcv.misc.BoofMiscOps;
import lombok.Getter;
import org.ddogleg.struct.DogArray_I32;
import org.ddogleg.struct.FastArray;
import org.ejml.data.DMatrixRMaj;

import java.util.Collections;
import java.util.Map;

/**
 * Given a {@link PairwiseImageGraph} that describes how a set of images are related to each other based on point
 * features, compute a projective reconstruction of the camera matrices for each view. The reconstructed location
 * of scene points are not saved.
 *
 * Summary of approach:
 * <ol>
 *     <li>Input: {@link PairwiseImageGraph}</li>
 *     <li>Select images/views to act as seeds</li>
 *     <li>Pick the first seed and perform initial reconstruction from its neighbors and common features</li>
 *     <li>For each remaining unknown view with a 3D relationship to a known view, find its camera matrix</li>
 *     <li>Stop when no more valid views can be found</li>
 * </ol>
 *
 * In the future multiple seeds will be used to reduce the amount of error which accumulates as the scene spreads out
 * from its initial location
 *
 * <p>Output: {@link #getWorkGraph()}</p>
 *
 * <p>WARNING: There are serious issues with N-view projective scenes. Even with "perfect" simulated data this
 * approach is unstable.</p>
 * <p>NOTE: One possible way (not tested) to mitigate those issues would be to scale pixels using a 3x3 matrix
 * that essentially resembles the inverse of an intrinsic matrix. At that point you might as well do a metric
 * reconstruction.</p>
 *
 * @author Peter Abeles
 * @see ProjectiveInitializeAllCommon
 * @see ProjectiveExpandByOneView
 * @see PairwiseGraphUtils
 */
public class ProjectiveReconstructionFromPairwiseGraph extends ReconstructionFromPairwiseGraph {

	/** Contains the found projective scene */
	public final @Getter SceneWorkingGraph workGraph = new SceneWorkingGraph();

	/** Computes the initial scene from the seed and some of it's neighbors */
	private final @Getter ProjectiveInitializeAllCommon initProjective;

	/** Adds a new view to an existing projective scene */
	private final @Getter ProjectiveExpandByOneView expandProjective;

	public ProjectiveReconstructionFromPairwiseGraph( PairwiseGraphUtils utils ) {
		super(utils);
		initProjective = new ProjectiveInitializeAllCommon();
		initProjective.utils = utils;
		expandProjective = new ProjectiveExpandByOneView();
		expandProjective.utils = utils;
	}

	public ProjectiveReconstructionFromPairwiseGraph( ConfigProjectiveReconstruction config ) {
		this(new PairwiseGraphUtils(config));
	}

	public ProjectiveReconstructionFromPairwiseGraph() {
		this(new ConfigProjectiveReconstruction());
	}

	/**
	 * Performs a projective reconstruction of the scene from the views contained in the graph
	 *
	 * @param db (input) Contains information on each image
	 * @param graph (input) Relationship between the images
	 * @return true if successful or false if it failed and results can't be used
	 */
	public boolean process( LookUpSimilarImages db, PairwiseImageGraph graph ) {
		workGraph.reset();

		// Score nodes for their ability to be seeds
		Map<String, SeedInfo> mapScores = scoreNodesAsSeeds(graph, 4);

		// Doesn't support multiple seeds here. So remove all but the highest score
		Collections.sort(seedScores.toList());
		while (seedScores.size > 1)
			seedScores.remove(seedScores.size-2);

		selectAndSpawnSeeds(db, graph, seedScores, mapScores);

		// TODO redo every component to use shifted pixels
		// TODO redo every component to use scaled pixels

		// NOTE: Computing H to scale camera matrices didn't prevent them from vanishing

		expandScene(db);

		// TODO compute features across all views for SBA
		// NOTE: Could do one last bundle adjustment on the entire scene. not doing that here since it would
		//       be a pain to code up since features need to be tracked across all the images and triangulated
		// TODO Note that the scene should be properly scale first if this is done.

		if (verbose != null) verbose.println("Done");
		return true;
	}

	/**
	 * Initializes the scene at the seed view
	 */
	@Override
	protected boolean spawnSceneFromSeed( LookUpSimilarImages db, PairwiseImageGraph pairwise, SeedInfo info ) {
		// Find the common features
		var commonPairwise = new DogArray_I32();
		utils.findAllConnectedSeed(info.seed, info.motions, commonPairwise);
		if (commonPairwise.size < 6) // if less than the minimum it will fail
			return false;

		// initialize projective scene using common tracks
		if (!initProjective.projectiveSceneN(db, info.seed, commonPairwise, info.motions)) {
			if (verbose != null) verbose.println("Failed initialize seed");
			return false;
		}

		// Save found camera matrices for each view it was estimated in
		if (verbose != null) verbose.println("Saving initial seed camera matrices");
		for (int structViewIdx = 0; structViewIdx < initProjective.utils.structure.views.size; structViewIdx++) {
			PairwiseImageGraph.View view = initProjective.getPairwiseGraphViewByStructureIndex(structViewIdx);
			if (verbose != null) verbose.println("  view.id=`" + view.id + "`");
			DMatrixRMaj cameraMatrix = initProjective.utils.structure.views.get(structViewIdx).worldToView;
			workGraph.addView(view).projective.setTo(cameraMatrix);
			workGraph.exploredViews.add(view.id);
		}

		// save which features were used for later use in metric reconstruction
		utils.saveRansacInliers(workGraph.lookupView(utils.seed.id));

		return true;
	}

	/**
	 * Adds all the remaining views to the scene
	 */
	private void expandScene( LookUpSimilarImages db ) {
		if (verbose != null) verbose.println("ENTER Expanding Scene:");
		// Create a list of views that can be added the work graph
		findAllOpenViews(workGraph);
		FastArray<PairwiseImageGraph.View> open = workGraph.open;

		// Grow the projective scene until there are no more views to process
		DMatrixRMaj cameraMatrix = new DMatrixRMaj(3, 4);
		Expansion score = new Expansion();
		while (open.size > 0) {
			if (!selectNextToProcess(workGraph, score)) {
				if (verbose != null) verbose.println("  No valid views left. open.size=" + open.size);
				break;
			}

			PairwiseImageGraph.View selected = score.scene.open.removeSwap(score.openIdx);

			if (!expandProjective.process(db, workGraph, selected, cameraMatrix)) {
				if (verbose != null) verbose.println("  Failed to expand/add view=" + selected.id + ". Discarding.");
				continue;
			}
			if (verbose != null) {
				verbose.println("  Success Expanding: view=" + selected.id + "  inliers="
						+ utils.inliersThreeView.size() + " / " + utils.matchesTriple.size);
			}

			// save the results
			SceneWorkingGraph.View wview = workGraph.addView(selected);
			wview.projective.setTo(cameraMatrix);

			// save which features were used for later use in metric reconstruction
			BoofMiscOps.checkTrue(utils.seed == wview.pview);// just being paranoid
			utils.saveRansacInliers(wview);

			// Add views which are neighbors
			addOpenForView(workGraph, wview.pview);
		}
		if (verbose != null) verbose.println("EXIT Expanding Scene");
	}
}
