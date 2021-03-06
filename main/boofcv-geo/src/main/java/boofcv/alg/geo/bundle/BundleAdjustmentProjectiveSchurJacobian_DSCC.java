/*
 * Copyright (c) 2020, Peter Abeles. All Rights Reserved.
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

package boofcv.alg.geo.bundle;

import boofcv.abst.geo.bundle.BundleAdjustmentSchur_DSCC;
import org.ejml.data.DMatrix;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.data.DMatrixSparseTriplet;
import org.ejml.ops.DConvertMatrixStruct;

/**
 * Computes the Jacobian for {@link BundleAdjustmentSchur_DSCC} using sparse matrices
 * in EJML. Parameterization is done using the format in {@link CodecSceneStructureProjective}.
 *
 * @author Peter Abeles
 */
public class BundleAdjustmentProjectiveSchurJacobian_DSCC
		extends BundleAdjustmentProjectiveSchurJacobian<DMatrixSparseCSC> {
	// reference to output Jacobian matrix
	private final DMatrixSparseTriplet leftTriplet = new DMatrixSparseTriplet();
	private final DMatrixSparseTriplet rightTriplet = new DMatrixSparseTriplet();

	@Override
	public void process( double[] input, DMatrixSparseCSC left, DMatrixSparseCSC right ) {
		processInternal(input, leftTriplet, rightTriplet);

		DConvertMatrixStruct.convert(leftTriplet, left);
		DConvertMatrixStruct.convert(rightTriplet, right);
	}

	@Override
	protected void set( DMatrix matrix, int row, int col, double value ) {
		((DMatrixSparseTriplet)matrix).addItem(row, col, value);
	}
}
