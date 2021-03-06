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

package boofcv.alg.filter.convolve.noborder;

import boofcv.struct.convolve.Kernel1D_F32;
import boofcv.struct.convolve.Kernel2D_F32;
import boofcv.struct.image.GrayF32;

import javax.annotation.Generated;

//CONCURRENT_CLASS_NAME ConvolveImageUnrolled_SB_MT_F32_F32
//CONCURRENT_INLINE import boofcv.concurrency.BoofConcurrency;

/**
 * <p>
 * Unrolls the convolution kernel to reduce array accessing and save often used variables to the stack.
 * </p>
 *
 * <p>
 * Unrolling the image being convolved resulting in an additional 10% performance boost on a Core i7 processor,
 * see commented out code below. Due to the added complexity it was decided that this performance boost was
 * not worth it. By comparison, unrolling the kernel causes a performance boost between 2 and 3 times.
 * </p>
 *
 * <p>DO NOT MODIFY. Automatically generated code created by GenerateConvolvedUnrolled_SB</p>
 *
 * @author Peter Abeles
 */
@Generated("boofcv.alg.filter.convolve.noborder.GenerateConvolvedUnrolled_SB")
@SuppressWarnings({"ForLoopReplaceableByForEach","Duplicates"})
public class ConvolveImageUnrolled_SB_F32_F32 {
	public static boolean horizontal( Kernel1D_F32 kernel,
								   GrayF32 image, GrayF32 dest) {

		// Unrolled functions only exist for symmetric kernels with an odd width
		if( kernel.offset != kernel.width/2 || kernel.width%2 == 0 )
			return false;

		switch (kernel.width) {
			case 3: horizontal3(kernel, image, dest); break;
			case 5: horizontal5(kernel, image, dest); break;
			case 7: horizontal7(kernel, image, dest); break;
			case 9: horizontal9(kernel, image, dest); break;
			case 11: horizontal11(kernel, image, dest); break;
			default: return false;
		}
		return true;
	}

	public static boolean vertical( Kernel1D_F32 kernel,
								   GrayF32 image, GrayF32 dest) {

		// Unrolled functions only exist for symmetric kernels with an odd width
		if( kernel.offset != kernel.width/2 || kernel.width%2 == 0 )
			return false;

		switch (kernel.width) {
			case 3: vertical3(kernel, image, dest); break;
			case 5: vertical5(kernel, image, dest); break;
			case 7: vertical7(kernel, image, dest); break;
			case 9: vertical9(kernel, image, dest); break;
			case 11: vertical11(kernel, image, dest); break;
			default: return false;
		}
		return true;
	}

	public static boolean convolve( Kernel2D_F32 kernel,
								   GrayF32 image, GrayF32 dest) {

		// Unrolled functions only exist for symmetric kernels with an odd width
		if( kernel.offset != kernel.width/2 || kernel.width%2 == 0 )
			return false;

		switch (kernel.width) {
			case 3: convolve3(kernel, image, dest); break;
			case 5: convolve5(kernel, image, dest); break;
			case 7: convolve7(kernel, image, dest); break;
			case 9: convolve9(kernel, image, dest); break;
			case 11: convolve11(kernel, image, dest); break;
			default: return false;
		}
		return true;
	}

	public static void horizontal3( Kernel1D_F32 kernel , GrayF32 image, GrayF32 dest )
	{
		final float[] dataSrc = image.data;
		final float[] dataDst = dest.data;

		final float k1 = kernel.data[0];
		final float k2 = kernel.data[1];
		final float k3 = kernel.data[2];

		final int radius = kernel.getRadius();

		final int width = image.getWidth();

		//CONCURRENT_BELOW BoofConcurrency.loopFor(0, image.height, i -> {
		for (int i = 0; i < image.height; i++) {
			int indexDst = dest.startIndex + i*dest.stride+radius;
			int j = image.startIndex + i*image.stride - radius;
			final int jEnd = j+width-radius;

			for( j += radius; j < jEnd; j++ ) {
				int indexSrc = j;
				float total = (dataSrc[indexSrc++])*k1;
				total += (dataSrc[indexSrc++])*k2;
				total += (dataSrc[indexSrc])*k3;

				dataDst[indexDst++] = total;
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void horizontal5( Kernel1D_F32 kernel , GrayF32 image, GrayF32 dest )
	{
		final float[] dataSrc = image.data;
		final float[] dataDst = dest.data;

		final float k1 = kernel.data[0];
		final float k2 = kernel.data[1];
		final float k3 = kernel.data[2];
		final float k4 = kernel.data[3];
		final float k5 = kernel.data[4];

		final int radius = kernel.getRadius();

		final int width = image.getWidth();

		//CONCURRENT_BELOW BoofConcurrency.loopFor(0, image.height, i -> {
		for (int i = 0; i < image.height; i++) {
			int indexDst = dest.startIndex + i*dest.stride+radius;
			int j = image.startIndex + i*image.stride - radius;
			final int jEnd = j+width-radius;

			for( j += radius; j < jEnd; j++ ) {
				int indexSrc = j;
				float total = (dataSrc[indexSrc++])*k1;
				total += (dataSrc[indexSrc++])*k2;
				total += (dataSrc[indexSrc++])*k3;
				total += (dataSrc[indexSrc++])*k4;
				total += (dataSrc[indexSrc])*k5;

				dataDst[indexDst++] = total;
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void horizontal7( Kernel1D_F32 kernel , GrayF32 image, GrayF32 dest )
	{
		final float[] dataSrc = image.data;
		final float[] dataDst = dest.data;

		final float k1 = kernel.data[0];
		final float k2 = kernel.data[1];
		final float k3 = kernel.data[2];
		final float k4 = kernel.data[3];
		final float k5 = kernel.data[4];
		final float k6 = kernel.data[5];
		final float k7 = kernel.data[6];

		final int radius = kernel.getRadius();

		final int width = image.getWidth();

		//CONCURRENT_BELOW BoofConcurrency.loopFor(0, image.height, i -> {
		for (int i = 0; i < image.height; i++) {
			int indexDst = dest.startIndex + i*dest.stride+radius;
			int j = image.startIndex + i*image.stride - radius;
			final int jEnd = j+width-radius;

			for( j += radius; j < jEnd; j++ ) {
				int indexSrc = j;
				float total = (dataSrc[indexSrc++])*k1;
				total += (dataSrc[indexSrc++])*k2;
				total += (dataSrc[indexSrc++])*k3;
				total += (dataSrc[indexSrc++])*k4;
				total += (dataSrc[indexSrc++])*k5;
				total += (dataSrc[indexSrc++])*k6;
				total += (dataSrc[indexSrc])*k7;

				dataDst[indexDst++] = total;
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void horizontal9( Kernel1D_F32 kernel , GrayF32 image, GrayF32 dest )
	{
		final float[] dataSrc = image.data;
		final float[] dataDst = dest.data;

		final float k1 = kernel.data[0];
		final float k2 = kernel.data[1];
		final float k3 = kernel.data[2];
		final float k4 = kernel.data[3];
		final float k5 = kernel.data[4];
		final float k6 = kernel.data[5];
		final float k7 = kernel.data[6];
		final float k8 = kernel.data[7];
		final float k9 = kernel.data[8];

		final int radius = kernel.getRadius();

		final int width = image.getWidth();

		//CONCURRENT_BELOW BoofConcurrency.loopFor(0, image.height, i -> {
		for (int i = 0; i < image.height; i++) {
			int indexDst = dest.startIndex + i*dest.stride+radius;
			int j = image.startIndex + i*image.stride - radius;
			final int jEnd = j+width-radius;

			for( j += radius; j < jEnd; j++ ) {
				int indexSrc = j;
				float total = (dataSrc[indexSrc++])*k1;
				total += (dataSrc[indexSrc++])*k2;
				total += (dataSrc[indexSrc++])*k3;
				total += (dataSrc[indexSrc++])*k4;
				total += (dataSrc[indexSrc++])*k5;
				total += (dataSrc[indexSrc++])*k6;
				total += (dataSrc[indexSrc++])*k7;
				total += (dataSrc[indexSrc++])*k8;
				total += (dataSrc[indexSrc])*k9;

				dataDst[indexDst++] = total;
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void horizontal11( Kernel1D_F32 kernel , GrayF32 image, GrayF32 dest )
	{
		final float[] dataSrc = image.data;
		final float[] dataDst = dest.data;

		final float k1 = kernel.data[0];
		final float k2 = kernel.data[1];
		final float k3 = kernel.data[2];
		final float k4 = kernel.data[3];
		final float k5 = kernel.data[4];
		final float k6 = kernel.data[5];
		final float k7 = kernel.data[6];
		final float k8 = kernel.data[7];
		final float k9 = kernel.data[8];
		final float k10 = kernel.data[9];
		final float k11 = kernel.data[10];

		final int radius = kernel.getRadius();

		final int width = image.getWidth();

		//CONCURRENT_BELOW BoofConcurrency.loopFor(0, image.height, i -> {
		for (int i = 0; i < image.height; i++) {
			int indexDst = dest.startIndex + i*dest.stride+radius;
			int j = image.startIndex + i*image.stride - radius;
			final int jEnd = j+width-radius;

			for( j += radius; j < jEnd; j++ ) {
				int indexSrc = j;
				float total = (dataSrc[indexSrc++])*k1;
				total += (dataSrc[indexSrc++])*k2;
				total += (dataSrc[indexSrc++])*k3;
				total += (dataSrc[indexSrc++])*k4;
				total += (dataSrc[indexSrc++])*k5;
				total += (dataSrc[indexSrc++])*k6;
				total += (dataSrc[indexSrc++])*k7;
				total += (dataSrc[indexSrc++])*k8;
				total += (dataSrc[indexSrc++])*k9;
				total += (dataSrc[indexSrc++])*k10;
				total += (dataSrc[indexSrc])*k11;

				dataDst[indexDst++] = total;
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void vertical3( Kernel1D_F32 kernel, GrayF32 src, GrayF32 dst )
	{
		final float[] dataSrc = src.data;
		final float[] dataDst = dst.data;

		final float k1 = kernel.data[0];
		final float k2 = kernel.data[1];
		final float k3 = kernel.data[2];

		final int radius = kernel.getRadius();

		final int imgWidth = dst.getWidth();
		final int imgHeight = dst.getHeight();

		final int yEnd = imgHeight - radius;

		//CONCURRENT_BELOW BoofConcurrency.loopFor(radius, yEnd, y -> {
		for (int y = radius; y < yEnd; y++) {
			int indexDst = dst.startIndex + y*dst.stride;
			int i = src.startIndex + (y - radius)*src.stride;
			final int iEnd = i + imgWidth;

			for (; i < iEnd; i++) {
				int indexSrc = i;

				float total = (dataSrc[indexSrc]) * k1;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k2;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k3;

				dataDst[indexDst++] = total;
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void vertical5( Kernel1D_F32 kernel, GrayF32 src, GrayF32 dst )
	{
		final float[] dataSrc = src.data;
		final float[] dataDst = dst.data;

		final float k1 = kernel.data[0];
		final float k2 = kernel.data[1];
		final float k3 = kernel.data[2];
		final float k4 = kernel.data[3];
		final float k5 = kernel.data[4];

		final int radius = kernel.getRadius();

		final int imgWidth = dst.getWidth();
		final int imgHeight = dst.getHeight();

		final int yEnd = imgHeight - radius;

		//CONCURRENT_BELOW BoofConcurrency.loopFor(radius, yEnd, y -> {
		for (int y = radius; y < yEnd; y++) {
			int indexDst = dst.startIndex + y*dst.stride;
			int i = src.startIndex + (y - radius)*src.stride;
			final int iEnd = i + imgWidth;

			for (; i < iEnd; i++) {
				int indexSrc = i;

				float total = (dataSrc[indexSrc]) * k1;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k2;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k3;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k4;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k5;

				dataDst[indexDst++] = total;
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void vertical7( Kernel1D_F32 kernel, GrayF32 src, GrayF32 dst )
	{
		final float[] dataSrc = src.data;
		final float[] dataDst = dst.data;

		final float k1 = kernel.data[0];
		final float k2 = kernel.data[1];
		final float k3 = kernel.data[2];
		final float k4 = kernel.data[3];
		final float k5 = kernel.data[4];
		final float k6 = kernel.data[5];
		final float k7 = kernel.data[6];

		final int radius = kernel.getRadius();

		final int imgWidth = dst.getWidth();
		final int imgHeight = dst.getHeight();

		final int yEnd = imgHeight - radius;

		//CONCURRENT_BELOW BoofConcurrency.loopFor(radius, yEnd, y -> {
		for (int y = radius; y < yEnd; y++) {
			int indexDst = dst.startIndex + y*dst.stride;
			int i = src.startIndex + (y - radius)*src.stride;
			final int iEnd = i + imgWidth;

			for (; i < iEnd; i++) {
				int indexSrc = i;

				float total = (dataSrc[indexSrc]) * k1;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k2;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k3;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k4;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k5;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k6;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k7;

				dataDst[indexDst++] = total;
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void vertical9( Kernel1D_F32 kernel, GrayF32 src, GrayF32 dst )
	{
		final float[] dataSrc = src.data;
		final float[] dataDst = dst.data;

		final float k1 = kernel.data[0];
		final float k2 = kernel.data[1];
		final float k3 = kernel.data[2];
		final float k4 = kernel.data[3];
		final float k5 = kernel.data[4];
		final float k6 = kernel.data[5];
		final float k7 = kernel.data[6];
		final float k8 = kernel.data[7];
		final float k9 = kernel.data[8];

		final int radius = kernel.getRadius();

		final int imgWidth = dst.getWidth();
		final int imgHeight = dst.getHeight();

		final int yEnd = imgHeight - radius;

		//CONCURRENT_BELOW BoofConcurrency.loopFor(radius, yEnd, y -> {
		for (int y = radius; y < yEnd; y++) {
			int indexDst = dst.startIndex + y*dst.stride;
			int i = src.startIndex + (y - radius)*src.stride;
			final int iEnd = i + imgWidth;

			for (; i < iEnd; i++) {
				int indexSrc = i;

				float total = (dataSrc[indexSrc]) * k1;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k2;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k3;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k4;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k5;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k6;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k7;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k8;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k9;

				dataDst[indexDst++] = total;
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void vertical11( Kernel1D_F32 kernel, GrayF32 src, GrayF32 dst )
	{
		final float[] dataSrc = src.data;
		final float[] dataDst = dst.data;

		final float k1 = kernel.data[0];
		final float k2 = kernel.data[1];
		final float k3 = kernel.data[2];
		final float k4 = kernel.data[3];
		final float k5 = kernel.data[4];
		final float k6 = kernel.data[5];
		final float k7 = kernel.data[6];
		final float k8 = kernel.data[7];
		final float k9 = kernel.data[8];
		final float k10 = kernel.data[9];
		final float k11 = kernel.data[10];

		final int radius = kernel.getRadius();

		final int imgWidth = dst.getWidth();
		final int imgHeight = dst.getHeight();

		final int yEnd = imgHeight - radius;

		//CONCURRENT_BELOW BoofConcurrency.loopFor(radius, yEnd, y -> {
		for (int y = radius; y < yEnd; y++) {
			int indexDst = dst.startIndex + y*dst.stride;
			int i = src.startIndex + (y - radius)*src.stride;
			final int iEnd = i + imgWidth;

			for (; i < iEnd; i++) {
				int indexSrc = i;

				float total = (dataSrc[indexSrc]) * k1;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k2;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k3;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k4;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k5;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k6;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k7;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k8;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k9;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k10;
				indexSrc += src.stride;
				total += (dataSrc[indexSrc])*k11;

				dataDst[indexDst++] = total;
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void convolve3( Kernel2D_F32 kernel, GrayF32 src, GrayF32 dest)
	{
		final float[] dataSrc = src.data;
		final float[] dataDst = dest.data;

		final int width = src.getWidth();
		final int height = src.getHeight();

		final int kernelRadius = kernel.getRadius();

		//CONCURRENT_BELOW BoofConcurrency.loopFor(kernelRadius, height-kernelRadius, y -> {
		for (int y = kernelRadius; y < height-kernelRadius; y++) {

			// first time through the value needs to be set
			float k1 = kernel.data[0];
			float k2 = kernel.data[1];
			float k3 = kernel.data[2];

			int indexDst = dest.startIndex + y*dest.stride+kernelRadius;
			int indexSrcRow = src.startIndex+(y-kernelRadius)*src.stride-kernelRadius;
			for( int x = kernelRadius; x < width-kernelRadius; x++ ) {
				int indexSrc = indexSrcRow + x;

				float total = 0;
				total += (dataSrc[indexSrc++] )* k1;
				total += (dataSrc[indexSrc++] )* k2;
				total += (dataSrc[indexSrc] )* k3;

				dataDst[indexDst++] = total;
			}

			// rest of the convolution rows are an addition
			for( int i = 1; i < 3; i++ ) {
				indexDst = dest.startIndex + y*dest.stride+kernelRadius;
				indexSrcRow = src.startIndex+(y+i-kernelRadius)*src.stride-kernelRadius;
				
				k1 = kernel.data[i*3 + 0];
				k2 = kernel.data[i*3 + 1];
				k3 = kernel.data[i*3 + 2];

				for( int x = kernelRadius; x < width-kernelRadius; x++ ) {
					int indexSrc = indexSrcRow+x;

					float total = 0;
					total += (dataSrc[indexSrc++] )* k1;
					total += (dataSrc[indexSrc++] )* k2;
					total += (dataSrc[indexSrc] )* k3;

					dataDst[indexDst++] += total;
				}
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void convolve5( Kernel2D_F32 kernel, GrayF32 src, GrayF32 dest)
	{
		final float[] dataSrc = src.data;
		final float[] dataDst = dest.data;

		final int width = src.getWidth();
		final int height = src.getHeight();

		final int kernelRadius = kernel.getRadius();

		//CONCURRENT_BELOW BoofConcurrency.loopFor(kernelRadius, height-kernelRadius, y -> {
		for (int y = kernelRadius; y < height-kernelRadius; y++) {

			// first time through the value needs to be set
			float k1 = kernel.data[0];
			float k2 = kernel.data[1];
			float k3 = kernel.data[2];
			float k4 = kernel.data[3];
			float k5 = kernel.data[4];

			int indexDst = dest.startIndex + y*dest.stride+kernelRadius;
			int indexSrcRow = src.startIndex+(y-kernelRadius)*src.stride-kernelRadius;
			for( int x = kernelRadius; x < width-kernelRadius; x++ ) {
				int indexSrc = indexSrcRow + x;

				float total = 0;
				total += (dataSrc[indexSrc++] )* k1;
				total += (dataSrc[indexSrc++] )* k2;
				total += (dataSrc[indexSrc++] )* k3;
				total += (dataSrc[indexSrc++] )* k4;
				total += (dataSrc[indexSrc] )* k5;

				dataDst[indexDst++] = total;
			}

			// rest of the convolution rows are an addition
			for( int i = 1; i < 5; i++ ) {
				indexDst = dest.startIndex + y*dest.stride+kernelRadius;
				indexSrcRow = src.startIndex+(y+i-kernelRadius)*src.stride-kernelRadius;
				
				k1 = kernel.data[i*5 + 0];
				k2 = kernel.data[i*5 + 1];
				k3 = kernel.data[i*5 + 2];
				k4 = kernel.data[i*5 + 3];
				k5 = kernel.data[i*5 + 4];

				for( int x = kernelRadius; x < width-kernelRadius; x++ ) {
					int indexSrc = indexSrcRow+x;

					float total = 0;
					total += (dataSrc[indexSrc++] )* k1;
					total += (dataSrc[indexSrc++] )* k2;
					total += (dataSrc[indexSrc++] )* k3;
					total += (dataSrc[indexSrc++] )* k4;
					total += (dataSrc[indexSrc] )* k5;

					dataDst[indexDst++] += total;
				}
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void convolve7( Kernel2D_F32 kernel, GrayF32 src, GrayF32 dest)
	{
		final float[] dataSrc = src.data;
		final float[] dataDst = dest.data;

		final int width = src.getWidth();
		final int height = src.getHeight();

		final int kernelRadius = kernel.getRadius();

		//CONCURRENT_BELOW BoofConcurrency.loopFor(kernelRadius, height-kernelRadius, y -> {
		for (int y = kernelRadius; y < height-kernelRadius; y++) {

			// first time through the value needs to be set
			float k1 = kernel.data[0];
			float k2 = kernel.data[1];
			float k3 = kernel.data[2];
			float k4 = kernel.data[3];
			float k5 = kernel.data[4];
			float k6 = kernel.data[5];
			float k7 = kernel.data[6];

			int indexDst = dest.startIndex + y*dest.stride+kernelRadius;
			int indexSrcRow = src.startIndex+(y-kernelRadius)*src.stride-kernelRadius;
			for( int x = kernelRadius; x < width-kernelRadius; x++ ) {
				int indexSrc = indexSrcRow + x;

				float total = 0;
				total += (dataSrc[indexSrc++] )* k1;
				total += (dataSrc[indexSrc++] )* k2;
				total += (dataSrc[indexSrc++] )* k3;
				total += (dataSrc[indexSrc++] )* k4;
				total += (dataSrc[indexSrc++] )* k5;
				total += (dataSrc[indexSrc++] )* k6;
				total += (dataSrc[indexSrc] )* k7;

				dataDst[indexDst++] = total;
			}

			// rest of the convolution rows are an addition
			for( int i = 1; i < 7; i++ ) {
				indexDst = dest.startIndex + y*dest.stride+kernelRadius;
				indexSrcRow = src.startIndex+(y+i-kernelRadius)*src.stride-kernelRadius;
				
				k1 = kernel.data[i*7 + 0];
				k2 = kernel.data[i*7 + 1];
				k3 = kernel.data[i*7 + 2];
				k4 = kernel.data[i*7 + 3];
				k5 = kernel.data[i*7 + 4];
				k6 = kernel.data[i*7 + 5];
				k7 = kernel.data[i*7 + 6];

				for( int x = kernelRadius; x < width-kernelRadius; x++ ) {
					int indexSrc = indexSrcRow+x;

					float total = 0;
					total += (dataSrc[indexSrc++] )* k1;
					total += (dataSrc[indexSrc++] )* k2;
					total += (dataSrc[indexSrc++] )* k3;
					total += (dataSrc[indexSrc++] )* k4;
					total += (dataSrc[indexSrc++] )* k5;
					total += (dataSrc[indexSrc++] )* k6;
					total += (dataSrc[indexSrc] )* k7;

					dataDst[indexDst++] += total;
				}
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void convolve9( Kernel2D_F32 kernel, GrayF32 src, GrayF32 dest)
	{
		final float[] dataSrc = src.data;
		final float[] dataDst = dest.data;

		final int width = src.getWidth();
		final int height = src.getHeight();

		final int kernelRadius = kernel.getRadius();

		//CONCURRENT_BELOW BoofConcurrency.loopFor(kernelRadius, height-kernelRadius, y -> {
		for (int y = kernelRadius; y < height-kernelRadius; y++) {

			// first time through the value needs to be set
			float k1 = kernel.data[0];
			float k2 = kernel.data[1];
			float k3 = kernel.data[2];
			float k4 = kernel.data[3];
			float k5 = kernel.data[4];
			float k6 = kernel.data[5];
			float k7 = kernel.data[6];
			float k8 = kernel.data[7];
			float k9 = kernel.data[8];

			int indexDst = dest.startIndex + y*dest.stride+kernelRadius;
			int indexSrcRow = src.startIndex+(y-kernelRadius)*src.stride-kernelRadius;
			for( int x = kernelRadius; x < width-kernelRadius; x++ ) {
				int indexSrc = indexSrcRow + x;

				float total = 0;
				total += (dataSrc[indexSrc++] )* k1;
				total += (dataSrc[indexSrc++] )* k2;
				total += (dataSrc[indexSrc++] )* k3;
				total += (dataSrc[indexSrc++] )* k4;
				total += (dataSrc[indexSrc++] )* k5;
				total += (dataSrc[indexSrc++] )* k6;
				total += (dataSrc[indexSrc++] )* k7;
				total += (dataSrc[indexSrc++] )* k8;
				total += (dataSrc[indexSrc] )* k9;

				dataDst[indexDst++] = total;
			}

			// rest of the convolution rows are an addition
			for( int i = 1; i < 9; i++ ) {
				indexDst = dest.startIndex + y*dest.stride+kernelRadius;
				indexSrcRow = src.startIndex+(y+i-kernelRadius)*src.stride-kernelRadius;
				
				k1 = kernel.data[i*9 + 0];
				k2 = kernel.data[i*9 + 1];
				k3 = kernel.data[i*9 + 2];
				k4 = kernel.data[i*9 + 3];
				k5 = kernel.data[i*9 + 4];
				k6 = kernel.data[i*9 + 5];
				k7 = kernel.data[i*9 + 6];
				k8 = kernel.data[i*9 + 7];
				k9 = kernel.data[i*9 + 8];

				for( int x = kernelRadius; x < width-kernelRadius; x++ ) {
					int indexSrc = indexSrcRow+x;

					float total = 0;
					total += (dataSrc[indexSrc++] )* k1;
					total += (dataSrc[indexSrc++] )* k2;
					total += (dataSrc[indexSrc++] )* k3;
					total += (dataSrc[indexSrc++] )* k4;
					total += (dataSrc[indexSrc++] )* k5;
					total += (dataSrc[indexSrc++] )* k6;
					total += (dataSrc[indexSrc++] )* k7;
					total += (dataSrc[indexSrc++] )* k8;
					total += (dataSrc[indexSrc] )* k9;

					dataDst[indexDst++] += total;
				}
			}
		}
		//CONCURRENT_ABOVE });
	}

	public static void convolve11( Kernel2D_F32 kernel, GrayF32 src, GrayF32 dest)
	{
		final float[] dataSrc = src.data;
		final float[] dataDst = dest.data;

		final int width = src.getWidth();
		final int height = src.getHeight();

		final int kernelRadius = kernel.getRadius();

		//CONCURRENT_BELOW BoofConcurrency.loopFor(kernelRadius, height-kernelRadius, y -> {
		for (int y = kernelRadius; y < height-kernelRadius; y++) {

			// first time through the value needs to be set
			float k1 = kernel.data[0];
			float k2 = kernel.data[1];
			float k3 = kernel.data[2];
			float k4 = kernel.data[3];
			float k5 = kernel.data[4];
			float k6 = kernel.data[5];
			float k7 = kernel.data[6];
			float k8 = kernel.data[7];
			float k9 = kernel.data[8];
			float k10 = kernel.data[9];
			float k11 = kernel.data[10];

			int indexDst = dest.startIndex + y*dest.stride+kernelRadius;
			int indexSrcRow = src.startIndex+(y-kernelRadius)*src.stride-kernelRadius;
			for( int x = kernelRadius; x < width-kernelRadius; x++ ) {
				int indexSrc = indexSrcRow + x;

				float total = 0;
				total += (dataSrc[indexSrc++] )* k1;
				total += (dataSrc[indexSrc++] )* k2;
				total += (dataSrc[indexSrc++] )* k3;
				total += (dataSrc[indexSrc++] )* k4;
				total += (dataSrc[indexSrc++] )* k5;
				total += (dataSrc[indexSrc++] )* k6;
				total += (dataSrc[indexSrc++] )* k7;
				total += (dataSrc[indexSrc++] )* k8;
				total += (dataSrc[indexSrc++] )* k9;
				total += (dataSrc[indexSrc++] )* k10;
				total += (dataSrc[indexSrc] )* k11;

				dataDst[indexDst++] = total;
			}

			// rest of the convolution rows are an addition
			for( int i = 1; i < 11; i++ ) {
				indexDst = dest.startIndex + y*dest.stride+kernelRadius;
				indexSrcRow = src.startIndex+(y+i-kernelRadius)*src.stride-kernelRadius;
				
				k1 = kernel.data[i*11 + 0];
				k2 = kernel.data[i*11 + 1];
				k3 = kernel.data[i*11 + 2];
				k4 = kernel.data[i*11 + 3];
				k5 = kernel.data[i*11 + 4];
				k6 = kernel.data[i*11 + 5];
				k7 = kernel.data[i*11 + 6];
				k8 = kernel.data[i*11 + 7];
				k9 = kernel.data[i*11 + 8];
				k10 = kernel.data[i*11 + 9];
				k11 = kernel.data[i*11 + 10];

				for( int x = kernelRadius; x < width-kernelRadius; x++ ) {
					int indexSrc = indexSrcRow+x;

					float total = 0;
					total += (dataSrc[indexSrc++] )* k1;
					total += (dataSrc[indexSrc++] )* k2;
					total += (dataSrc[indexSrc++] )* k3;
					total += (dataSrc[indexSrc++] )* k4;
					total += (dataSrc[indexSrc++] )* k5;
					total += (dataSrc[indexSrc++] )* k6;
					total += (dataSrc[indexSrc++] )* k7;
					total += (dataSrc[indexSrc++] )* k8;
					total += (dataSrc[indexSrc++] )* k9;
					total += (dataSrc[indexSrc++] )* k10;
					total += (dataSrc[indexSrc] )* k11;

					dataDst[indexDst++] += total;
				}
			}
		}
		//CONCURRENT_ABOVE });
	}

}
