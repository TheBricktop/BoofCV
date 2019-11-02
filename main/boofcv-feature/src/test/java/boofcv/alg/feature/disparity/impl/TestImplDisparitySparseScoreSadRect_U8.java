/*
 * Copyright (c) 2011-2019, Peter Abeles. All Rights Reserved.
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

package boofcv.alg.feature.disparity.impl;

import boofcv.alg.feature.disparity.DisparityBlockMatch;
import boofcv.alg.feature.disparity.DisparitySparseScoreSadRect;
import boofcv.struct.image.GrayU8;

/**
 * @author Peter Abeles
 */
public class TestImplDisparitySparseScoreSadRect_U8 extends ChecksImplDisparitySparseScoreSadRect<GrayU8,int[]>{

	public TestImplDisparitySparseScoreSadRect_U8() {
		super(GrayU8.class);
	}

	@Override
	public DisparityBlockMatch<GrayU8, GrayU8> createDense(int minDisparity, int maxDisparity,
														   int radiusX, int radiusY, BlockRowScore scoreRow) {
		return new ImplDisparityScoreBM_S32<>(minDisparity,maxDisparity,radiusX,radiusY,scoreRow,
				new ImplSelectRectBasicWta_S32_U8());
	}

	@Override
	public DisparitySparseScoreSadRect<int[], GrayU8> createSparse(int minDisparity, int maxDisparity,
																   int radiusX, int radiusY) {
		return new ImplDisparitySparseScoreBM_SAD_U8(minDisparity,maxDisparity,radiusX,radiusY);
	}
}
