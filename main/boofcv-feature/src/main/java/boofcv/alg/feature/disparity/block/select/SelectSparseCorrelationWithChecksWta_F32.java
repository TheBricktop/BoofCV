/*
 * Copyright (c) 2011-2020, Peter Abeles. All Rights Reserved.
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

package boofcv.alg.feature.disparity.block.select;

import boofcv.alg.feature.disparity.block.SelectSparseStandardWta;

/**
 * Selects the best correlation score with sanity checks.
 *
 * @author Peter Abeles
 */
public class SelectSparseCorrelationWithChecksWta_F32 extends SelectSparseStandardWta<float[]> {

	// texture threshold
	protected float textureThreshold;

	public SelectSparseCorrelationWithChecksWta_F32( double texture) {
		super(0,texture);
	}

	@Override
	protected void setTexture( double texture ) {
		this.textureThreshold = (float)texture;
	}

	@Override
	public boolean select(float[] scores, int disparityRange) {
		int bestDisparity = 0;
		float scoreBest = scores[0];
		float scoreWorst = scoreBest;

		for(int i = 1; i < disparityRange; i++ ) {
			float s = scores[i];
			if( s > scoreBest ) {
				scoreBest = scores[i];
				bestDisparity = i;
			} else if( s < scoreWorst ) {
				scoreWorst = s;
			}
		}

		// test to see if the region lacks sufficient texture if:
		// 1) not already eliminated 2) sufficient disparities to check, 3) it's activated
		if( textureThreshold > 0 && disparityRange >= 3 ) {
			// find the second best disparity value and exclude its neighbors
			float secondBest = scoreWorst;
			for( int i = 0; i < bestDisparity-1; i++ ) {
				if( scores[i] > secondBest )
					secondBest = scores[i];
			}
			for(int i = bestDisparity+2; i < disparityRange; i++ ) {
				if( scores[i] > secondBest )
					secondBest = scores[i];
			}

			// Make the score relative to the worst score
			scoreBest -= scoreWorst;
			secondBest -= scoreWorst;

			// similar scores indicate lack of texture
			// C = (C2-C1)/C1
			if( scoreBest-secondBest >= (1.0f-textureThreshold)*secondBest )
				return false;
		}

		this.disparity = bestDisparity;
		return true;
	}

}
