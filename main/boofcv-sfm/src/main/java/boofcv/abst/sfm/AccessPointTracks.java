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

package boofcv.abst.sfm;

import georegression.struct.point.Point2D_F64;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Provides access to the location of point tracks.  Location of tracks are provided in terms
 * of pixel coordinates.
 *
 * @author Peter Abeles
 */
public interface AccessPointTracks {

	/**
	 * Returns the total number of tracks
	 */
	int getTotal();

	/**
	 * Used to get the track ID of an active Track
	 *
	 * @param index which track
	 * @return The track's ID
	 */
	long getTrackId( int index );

	/**
	 * All the points being actively tracked in pixel coordinates.
	 *
	 * @return all active tracks in pixel coordinates
	 */
	List<Point2D_F64> getAllTracks( @Nullable List<Point2D_F64> storage );

	/**
	 * True if the specified track is an inlier used in motion estimation
	 *
	 * @param index The index in all
	 * @return if it is an inlier or not
	 */
	boolean isInlier( int index );

	/**
	 * True if the specified track was just spawned
	 *
	 * @param index The index in all
	 * @return if it is new or not
	 */
	boolean isNew( int index );

}
