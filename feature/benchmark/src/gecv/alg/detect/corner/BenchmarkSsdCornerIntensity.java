/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package gecv.alg.detect.corner;

import gecv.PerformerBase;
import gecv.ProfileOperation;
import gecv.alg.detect.corner.impl.*;
import gecv.alg.misc.ImageTestingOps;
import gecv.struct.image.ImageFloat32;
import gecv.struct.image.ImageSInt16;

import java.util.Random;

/**
 * Benchmark for different convolution operations.
 *
 * @author Peter Abeles
 */
public class BenchmarkSsdCornerIntensity {
	static int imgWidth = 640;
	static int imgHeight = 480;
	static int windowRadius = 2;
	static long TEST_TIME = 1000;

	static ImageFloat32 intensity = new ImageFloat32(imgWidth,imgHeight);

	static ImageFloat32 derivX_F32;
	static ImageFloat32 derivY_F32;
	static ImageFloat32 derivXX_F32;
	static ImageFloat32 derivYY_F32;
	static ImageFloat32 derivXY_F32;
	static ImageSInt16 derivX_I16;
	static ImageSInt16 derivY_I16;
	static ImageSInt16 derivXX_I16;
	static ImageSInt16 derivYY_I16;
	static ImageSInt16 derivXY_I16;

	static Random rand = new Random(234);

	public static class KLT_F32 extends PerformerBase {
		KltCorner_F32 corner = new KltCorner_F32(windowRadius);

		@Override
		public void process() {
			corner.process(derivX_F32, derivY_F32);
		}
	}

	public static class KLT_I16 extends PerformerBase {
		KltCorner_S16 corner = new KltCorner_S16(windowRadius);

		@Override
		public void process() {
			corner.process(derivX_I16, derivY_I16);
		}
	}

	public static class KLT_Naive_I16 extends PerformerBase {
		SsdCornerNaive_S16 corner = new SsdCornerNaive_S16(imgWidth, imgHeight, windowRadius);

		@Override
		public void process() {
			corner.process(derivX_I16, derivY_I16);
		}
	}

	public static class Harris_F32 extends PerformerBase {
		HarrisCorner_F32 corner = new HarrisCorner_F32(windowRadius, 0.04f);

		@Override
		public void process() {
			corner.process(derivX_F32, derivY_F32);
		}
	}

	public static class Harris_I16 extends PerformerBase {
		HarrisCorner_S16 corner = new HarrisCorner_S16( windowRadius, 0.04f);

		@Override
		public void process() {
			corner.process(derivX_I16, derivY_I16);
		}
	}

	public static class KitRos_F32 extends PerformerBase {

		@Override
		public void process() {
			KitRosCornerIntensity.process(intensity,derivX_F32, derivY_F32,derivXX_F32,derivYY_F32,derivXY_F32);
		}
	}

	public static class KitRos_I16 extends PerformerBase {
		@Override
		public void process() {
			KitRosCornerIntensity.process(intensity,derivX_I16, derivY_I16, derivXX_I16,derivYY_I16, derivXY_I16);
		}
	}


	public static void main(String args[]) {
		derivX_F32 = new ImageFloat32(imgWidth, imgHeight);
		derivY_F32 = new ImageFloat32(imgWidth, imgHeight);
		derivXX_F32 = new ImageFloat32(imgWidth, imgHeight);
		derivYY_F32 = new ImageFloat32(imgWidth, imgHeight);
		derivXY_F32 = new ImageFloat32(imgWidth, imgHeight);
		derivX_I16 = new ImageSInt16(imgWidth, imgHeight);
		derivY_I16 = new ImageSInt16(imgWidth, imgHeight);
		derivXX_I16 = new ImageSInt16(imgWidth, imgHeight);
		derivYY_I16 = new ImageSInt16(imgWidth, imgHeight);
		derivXY_I16 = new ImageSInt16(imgWidth, imgHeight);

		ImageTestingOps.randomize(derivX_F32, rand, 0, 255);
		ImageTestingOps.randomize(derivY_F32, rand, 0, 255);
		ImageTestingOps.randomize(derivXX_F32, rand, 0, 255);
		ImageTestingOps.randomize(derivYY_F32, rand, 0, 255);
		ImageTestingOps.randomize(derivXY_F32, rand, 0, 255);
		ImageTestingOps.randomize(derivX_I16, rand, 0, 255);
		ImageTestingOps.randomize(derivY_I16, rand, 0, 255);
		ImageTestingOps.randomize(derivXX_I16, rand, 0, 255);
		ImageTestingOps.randomize(derivYY_I16, rand, 0, 255);
		ImageTestingOps.randomize(derivXY_I16, rand, 0, 255);

		System.out.println("=========  Profile Image Size " + imgWidth + " x " + imgHeight + " ==========");
		System.out.println();

		ProfileOperation.printOpsPerSec(new KLT_F32(), TEST_TIME);
		ProfileOperation.printOpsPerSec(new Harris_F32(), TEST_TIME);
		ProfileOperation.printOpsPerSec(new KitRos_F32(), TEST_TIME);
		ProfileOperation.printOpsPerSec(new KLT_I16(), TEST_TIME);
		ProfileOperation.printOpsPerSec(new Harris_I16(), TEST_TIME);
		ProfileOperation.printOpsPerSec(new KitRos_I16(), TEST_TIME);
		ProfileOperation.printOpsPerSec(new KLT_Naive_I16(), TEST_TIME);

	}
}
