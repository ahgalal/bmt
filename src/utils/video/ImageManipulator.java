/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package utils.video;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Collection of static methods to handle images and convert between different
 * representations (int rgb, byte rgb, int grayscale ..).
 * 
 * @author Creative
 */
public class ImageManipulator {

	public static class Blob {
		private Point	centroid	= new Point();
		private int		maxX		= -1;
		private int		maxY		= -1;
		private int		minX		= 10000;
		private int		minY		= 10000;

		// private final ArrayList<Point> points = new ArrayList<Point>();

		public void addPoint(final Point p) {
			// points.add(p);
			if (p.x < minX)
				minX = p.x;
			else if (p.x > maxX)
				maxX = p.x;
			if (p.y < minY)
				minY = p.y;
			else if (p.y > maxY)
				maxY = p.y;
			centroid.x = (maxX + minX) / 2;
			centroid.y = (maxY + minY) / 2;
		}

		public Point getCentroid() {
			return centroid;
		}

		/*
		 * public ArrayList<Point> getPoints() { return points; }
		 */

		public void setCentroid(final Point centroid) {
			this.centroid = centroid;
		}
	}

	public static class BlobFinder {
		public static Collection<Blob> getBlobs(final int[] image) {
			final HashMap<Integer, Blob> blobs = new HashMap<Integer, Blob>();
			final int[] labels = new int[image.length];
			int labelCounter = 1;

			for (int i = 0; i < image.length; i++) {
				final int pixel = image[i];
				final int xPixel = i % 640;
				final int yPixel = i / 640;
				if (pixel == 0x00FFFFFF) { // pixel is foreground
					// check neighboring pixels' label
					y_loop:
					for (int yTmp = yPixel - 1; yTmp <= (yPixel + 1); yTmp++) {
						for (int xTmp = xPixel - 1; xTmp <= (xPixel + 1); xTmp++) {
							final int label = labels[xTmp + (yTmp * 640)];
							if (label > 0) {
								labels[i] = label;
								break y_loop;
							}
						}
					}
					Blob blob = null;
					if (labels[i] == 0) { // if label is not set yet for the
											// current
											// foreground pixel, set it
						labels[i] = labelCounter;
						blob = blobs.get(labelCounter);
						if (blob == null) {
							blob = new Blob();
							blobs.put(labelCounter, blob);
						}
						labelCounter++;
					} else {
						blob = blobs.get(labels[i]);
					}
					blob.addPoint(new Point(xPixel, yPixel));
				}
			}

			return blobs.values();
		}

		private final CentroidFinder	centroidFinder;

		public BlobFinder() {
			centroidFinder = new CentroidFinder();
		}

		public void initialize(final int width, final int height) {
			centroidFinder.initialize(width, height);
		}

		public void updateBlobsCentroids(final int[] image,
				final int[] filteredImage, final RGB[] colors,
				final Blob[] blobs, final RGB[] thresholds) {
			for (int i = 0; i < image.length; i++) {
				final int pixel = image[i];
				final int[] pixelRGB = ImageManipulator.intToRGB(pixel);

				for (int iColor = 0; iColor < colors.length; iColor++) {
					if ((Math.abs(pixelRGB[0] - colors[iColor].getR()) < thresholds[iColor]
							.getR())
							&& (Math.abs(pixelRGB[1] - colors[iColor].getG()) < thresholds[iColor]
									.getG())
							&& (Math.abs(pixelRGB[2] - colors[iColor].getB()) < thresholds[iColor]
									.getB())) {
						filteredImage[i] = iColor + 1;
					}
				}
			}

			for (int iColor = 0; iColor < colors.length; iColor++) {
				centroidFinder.updateCentroid(filteredImage, blobs[iColor].getCentroid(),
						iColor + 1);
			}
		}
	}

	public static class CentroidFinder {
		private static final int	CENTROID_HISTORY_SIZE	= 3;
		private static final int	SMALLEST_WHITE_AREA		= 10;
		private final Point[]		centroidHistory			= new Point[CENTROID_HISTORY_SIZE];
		private int					framesRemainingReliableCentroid;
		private int					height;
		private int[]				horiSum;
		private int					searchSideLength		= 600;
		private int					tmpMax;
		private int[]				vertSum;
		private int					width;

		public void initialize(final int width, final int height) {
			this.width = width;
			this.height = height;
			horiSum = new int[height];
			vertSum = new int[width];

			for (int i = 0; i < centroidHistory.length; i++)
				centroidHistory[i] = new Point(-1, -1);
			searchSideLength = width / 4;
			framesRemainingReliableCentroid = 10;

		}

		public boolean isStableCentroid() {
			return framesRemainingReliableCentroid == 0;
		}

		private void lowPassFilterCentroidPosition(final Point centerPoint) {

			// history remains disabled till ex:10 frames elapse, this is to
			// ensure
			// the reliability of the centroid position (after ex:10 frames)
			if (framesRemainingReliableCentroid == 0) {
				if (centroidHistory[0].x == -1) { // history is not initialized
													// yet
					for (int i = 0; i < (centroidHistory.length - 1); i++) {
						centroidHistory[i].x = centerPoint.x;
						centroidHistory[i].y = centerPoint.y;
					}
				} else {
					int sumX = 0, sumY = 0;
					for (final Point p : centroidHistory) {
						sumX += p.x;
						sumY += p.y;
					}
					final int factor = 5;
					centerPoint.x = ((centerPoint.x * factor) + sumX)
							/ (centroidHistory.length + factor);
					centerPoint.y = ((centerPoint.y * factor) + sumY)
							/ (centroidHistory.length + factor);

					// update history
					for (int i = 0; i < (centroidHistory.length - 1); i++) {
						centroidHistory[i].x = centroidHistory[i + 1].x;
						centroidHistory[i].y = centroidHistory[i + 1].y;
					}
					centroidHistory[centroidHistory.length - 1].x = centerPoint.x;
					centroidHistory[centroidHistory.length - 1].y = centerPoint.y;
				}
			} else
				framesRemainingReliableCentroid--;
		}

		public void updateCentroid(final int[] tmpProcessedData,
				final Point centroid) {
			updateCentroid(tmpProcessedData, centroid, -1);
		}

		/**
		 * Updates the center point (ie: finds the location of the moving
		 * object).
		 * 
		 * @param binaryImage
		 *            input image
		 */
		public void updateCentroid(final int[] binaryImage,
				final Point centerPoint, final int label) {

			tmpMax = SMALLEST_WHITE_AREA;

			int y1, y2;

			if (centerPoint.y == 0) {
				y1 = 0;
				y2 = height;
			} else {
				y1 = (centerPoint.y - searchSideLength) < 0 ? 0 : centerPoint.y
						- searchSideLength;
				y2 = (centerPoint.y + searchSideLength) > height ? height
						: centerPoint.y + searchSideLength;
			}

			for (int y = y1; y < y2; y++) { // Horizontal Sum
				horiSum[y] = 0;
				for (int x = 0; x < width; x++)
					if (label != -1) {
						if (binaryImage[(y * width) + x] == label)
							horiSum[y] += 1;
					} else
						horiSum[y] += binaryImage[(y * width) + x] & 0xFF;
				if (horiSum[y] > tmpMax) {
					centerPoint.y = y;
					tmpMax = horiSum[y];
				}
			}

			tmpMax = SMALLEST_WHITE_AREA;

			int x1, x2;
			if (centerPoint.x == 0) {
				x1 = 0;
				x2 = width;
			} else {
				x1 = (centerPoint.x - searchSideLength) < 0 ? 0 : centerPoint.x
						- searchSideLength;
				x2 = (centerPoint.x + searchSideLength) > width ? width
						: centerPoint.x + searchSideLength;
			}

			for (int x = x1; x < x2; x++) { // Vertical Sum
				vertSum[x] = 0;
				for (int y = 0; y < height; y++)
					if (label != -1) {
						if (binaryImage[(y * width) + x] == label)
							vertSum[x] += 1;
					} else
						vertSum[x] += binaryImage[(y * width) + x] & 0xFF;

				if (vertSum[x] > tmpMax) {
					centerPoint.x = x;
					tmpMax = vertSum[x];
				}
			}

			// low pass filter on position
			lowPassFilterCentroidPosition(centerPoint);
		}
	}

	public static class RGB {

		private int	r, g, b;

		public RGB(final int r, final int g, final int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}

		public int getB() {
			return b;
		}

		public int getG() {
			return g;
		}

		public int getR() {
			return r;
		}

		public void setB(final int b) {
			this.b = b;
		}

		public void setG(final int g) {
			this.g = g;
		}

		public void setR(final int r) {
			this.r = r;
		}

		public void setRGB(final int r, final int g, final int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}

	private static int[]	subResult;

	public static int addRGBInt(final int img1, final int img2) {
		byte r1, r2, g1, g2, b1, b2;
		r1 = (byte) (img1 & 0x00FF0000);
		r2 = (byte) (img2 & 0x00FF0000);
		g1 = (byte) (img1 & 0x0000FF00);
		g2 = (byte) (img2 & 0x0000FF00);
		b1 = (byte) (img1 & 0x000000FF);
		b2 = (byte) (img2 & 0x000000FF);

		final int res = (((r1 + r2) > 0xFF ? 0xFF : r1 + r2) << 16)
				| (((g1 + g2) > 0xFF ? 0xFF : g1 + g2) << 8)
				| ((b1 + b2) > 0xFF ? 0xFF : b1 + b2);
		return res;
	}

	public static int[] bgrIntArray2rgbIntArray(final int[] in) {
		int r, g, b;
		final int[] res = new int[in.length];
		for (int i = 0; i < in.length; i++) {
			/*
			 * r = in[i] & (0x000000FF); g = (in[i] & (0x0000FF00)) >> 8; b =
			 * (in[i] & (0x00FF0000)) >> 16; res[i] = (int) (0.2989 * r + 0.5870
			 * * g + 0.1140 * b);
			 */
			r = ((in[i] & (0x000000FF)));
			g = (((in[i] & (0x0000FF00)) >> 8));
			b = (((in[i] & (0x00FF0000)) >> 16));
			res[i] = b | (g << 8) | (r << 16);
		}
		return res;
	}

	/**
	 * Converts a byte[] BGR image to int[] RGB image.
	 * 
	 * @param byteArr
	 *            input byte[] BGR image
	 * @return integer array RGB image
	 */
	public static int[] byteBGR2IntRGB(final byte[] byteArr) {
		final int[] iarr = new int[byteArr.length / 3];
		int r, g, b;
		for (int i = 0; i < byteArr.length; i += 3) {
			r = byteArr[i + 2] & 255;
			g = byteArr[i + 1] & 255;
			b = byteArr[i] & 255;
			iarr[i / 3] = r | (g << 8) | (b << 16);
		}
		return iarr;
	}

	/**
	 * Converts a byte[] RGB image to int[] RGB image.
	 * 
	 * @param byteArr
	 *            input byte[] RGB image
	 * @return integer array RGB image
	 */
	public static int[] byteRGB2IntRGB(final byte[] byteArr) {
		final int[] iarr = new int[byteArr.length / 3];
		int r, g, b;
		for (int i = 0; i < byteArr.length; i += 3) {
			r = byteArr[i + 2] & 255;
			g = byteArr[i + 1] & 255;
			b = byteArr[i] & 255;
			iarr[i / 3] = b | (g << 8) | (r << 16);
		}
		return iarr;
	}

	public static int divideRGBByNumber(final int img1, final int number) {
		byte r1, g1, b1;
		r1 = (byte) (img1 & 0x00FF0000);
		g1 = (byte) (img1 & 0x0000FF00);
		b1 = (byte) (img1 & 0x000000FF);

		final int res = ((r1 / number) << 16) | ((g1 / number) << 8)
				| (b1 / number);
		return res;
	}

	public static void filterImageRGB(final int[] origImg,
			final int[] filteredImage, final int r, final int rThreshold,
			final int g, final int gThreshold, final int b, final int bThreshold) {
		for (int i = 0; i < origImg.length; i++) {
			final int pixel = origImg[i];
			if ((Math.abs(ImageManipulator.intToRGB(pixel)[0] - r) < rThreshold)
					&& (Math.abs(ImageManipulator.intToRGB(pixel)[1] - g) < gThreshold)
					&& (Math.abs(ImageManipulator.intToRGB(pixel)[2] - b) < bThreshold)) {
				filteredImage[i] = 0x00FFFFFF;
			} else {
				filteredImage[i] = 0x00;
			}
		}
	}

	/**
	 * Flips the input image vertically.
	 * 
	 * @param img
	 *            input image
	 * @param width
	 *            image's width
	 * @param height
	 *            image's height
	 * @return flipped int[] image
	 */
	public static int[] flipImage(final int[] img, final int width,
			final int height) {
		final int[] tmpImg = new int[width * height];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				tmpImg[(y * width) + x] = img[((height - y - 1) * width) + x];
		return tmpImg;
	}

	public static int[] formGrayImageFromGrayMap(final byte[] grayMap) {
		int r, g, b, grey;
		final int[] res = new int[grayMap.length];
		for (int i = 0; i < grayMap.length; i++) {
			r = (int) (0.2989 * (grayMap[i]));
			g = (int) (0.5870 * ((grayMap[i]) >> 8));
			b = (int) (0.1140 * ((grayMap[i]) >> 16));
			grey = r + g + b;
			res[i] = ((grey + (grey << 8)) | (grey << 16));
		}
		return res;
	}

	public static short[] formGrayMapFromGrayImage(final int[] grayImage) {
		int r, g, b, grey;
		final short[] res = new short[grayImage.length];
		for (int i = 0; i < grayImage.length; i++) {
			r = (int) (0.2989 * (grayImage[i] & 0x000000FF));
			g = (int) (0.5870 * ((grayImage[i] & 0x0000FF00) >> 8));
			b = (int) (0.1140 * ((grayImage[i] & 0x00FF0000) >> 16));
			grey = (r + g + b);
			res[i] = (short) grey;
		}
		return res;
	}

	public static int formGrayValueFromGrayIntensity(final short grayValue) {
		int res = 0;
		res = ((grayValue + (grayValue << 8)) | (grayValue << 16));
		return res;
	}

	public static Point[] getLinePoints(final Point pt1, final Point pt2) {
		final int deltaY = pt1.y - pt2.y;
		final int deltaX = pt1.x - pt2.x;
		;

		// get line equation
		final float slope = deltaY / (float) deltaX;
		final float c = pt1.y - (slope * pt1.x);

		final int numPts = Math.abs(deltaY) > Math.abs(deltaX) ? Math
				.abs(deltaY) : Math.abs(deltaX);
		final Point[] pts = new Point[Math.abs(numPts) + 1];
		int i = 0;
		if (Math.abs(deltaY) > Math.abs(deltaX)) {
			final int yInitial = pt1.y > pt2.y ? pt1.y : pt2.y;
			final int yFinal = pt1.y > pt2.y ? pt2.y : pt1.y;
			for (int y = yInitial; y >= yFinal; y--) {
				int x = -1;
				if (deltaX != 0)
					x = (int) ((y - c) / slope);
				else
					x = pt1.x;
				pts[i] = new Point(x, y);
				i++;
			}
		} else {
			final int xInitial = pt1.x > pt2.x ? pt1.x : pt2.x;
			final int xFinal = pt1.x > pt2.x ? pt2.x : pt1.x;
			for (int x = xInitial; x >= xFinal; x--) {
				int y = -1;
				if (deltaY != 0)
					y = (int) ((slope * x) + c);
				else
					y = pt1.y;
				pts[i] = new Point(x, y);
				i++;
			}
		}
		return pts;
	}

	/**
	 * Converts a byte[] Grayscale image to int[] RGB image (representation
	 * changes, but the image remains Gray colors).
	 * 
	 * @param grayArr
	 *            input Grayscale image
	 * @return integer array of the RGB image
	 */
	public static int[] gray2RGBInt(final byte[] grayArr) {
		final int[] rgbarr = new int[grayArr.length];
		int valgray;
		for (int i = 0; i < grayArr.length; i++) {
			valgray = grayArr[i] & 0xFF;
			rgbarr[i] = valgray | (valgray << 8) | (valgray << 16);
		}
		return rgbarr;
	}

	/**
	 * Converts single dimensional integer array image to a two dimensional
	 * integer array image.
	 * 
	 * @param arr
	 *            input single dimensional array
	 * @param width
	 *            image's width
	 * @param height
	 *            image's height
	 * @return two dimensional integer array image
	 */
	public static int[][] intArrayTo2DIntArray(final int[] arr,
			final int width, final int height) {
		final int[][] res = new int[width][height];
		for (int i = 0; i < arr.length; i++)
			res[i % width][i / width] = (byte) arr[i];
		return res;
	}

	/**
	 * Converts an int[] RGB image to byte[] RGB image.
	 * 
	 * @param intArr
	 *            input image
	 * @return converted image as an array of bytes
	 */
	public static byte[] intRGB2ByteRGB(final int[] intArr) { // 4ms

		final byte[] res = new byte[intArr.length * 3];
		for (int i = 0; i < res.length; i += 3) {
			res[i] = (byte) ((byte) intArr[i / 3] & (255)); // B
			res[i + 1] = (byte) ((intArr[i / 3] >> 8) & (255)); // G
			res[i + 2] = (byte) ((byte) (intArr[i / 3] >> 16) & (255)); // R

		}

		return res;
	}

	/**
	 * Converts a int[] RGB buffer to int[] RGB image.</br> i.e. [BRGB | GBRG |
	 * RGBR] to [ 0RGB | 0RGB | 0RGB | 0RGB].
	 * 
	 * @param buf
	 *            input int[] RGB buffer
	 * @param iarr
	 *            the output int[] RGB array
	 * @return integer array RGB image
	 */
	public static int[] intRGBBuf2IntRGB(final int[] buf, final int[] iarr) {
		final float factor = 1.3333333f;
		final int bytes[] = new int[12];
		for (int i = 0; i < buf.length; i += 3) {
			bytes[0] = ((buf[i] & 0xFF000000) >> 24) & 0x000000FF;
			bytes[1] = (buf[i] & 0x00FF0000) >> 16;
			bytes[2] = (buf[i] & 0x0000FF00) >> 8;
			bytes[3] = buf[i] & 0x000000FF;

			bytes[4] = ((buf[i + 1] & 0xFF000000) >> 24) & 0x000000FF;
			bytes[5] = (buf[i + 1] & 0x00FF0000) >> 16;
			bytes[6] = (buf[i + 1] & 0x0000FF00) >> 8;
			bytes[7] = buf[i + 1] & 0x000000FF;

			bytes[8] = ((buf[i + 2] & 0xFF000000) >> 24) & 0x000000FF;
			bytes[9] = (buf[i + 2] & 0x00FF0000) >> 16;
			bytes[10] = (buf[i + 2] & 0x0000FF00) >> 8;
			bytes[11] = buf[i + 2] & 0x000000FF;

			final int dstIndex = Math.round(i * factor);
			int r;
			int g;
			int b;

			b = bytes[0];
			r = bytes[1];
			g = bytes[2];
			iarr[dstIndex] = b | (g << 8) | (r << 16);

			b = bytes[3];
			g = bytes[4];
			r = bytes[6];
			iarr[dstIndex + 1] = b | (g << 8) | (r << 16);

			b = bytes[5];
			g = bytes[7];
			r = bytes[8];
			iarr[dstIndex + 2] = b | (g << 8) | (r << 16);

			g = bytes[9];
			b = bytes[10];
			r = bytes[11];
			iarr[dstIndex + 3] = b | (g << 8) | (r << 16);
		}
		return iarr;
	}

	public static int[] intToRGB(final int pixel) {
		final int[] res = new int[3];
		res[0] = ((pixel >> 16) & (255)); // R
		res[1] = ((pixel >> 8) & (255)); // G
		res[2] = (pixel & (255)); // B
		return res;
	}

	/**
	 * Loads an image file into a buffered image.
	 * 
	 * @param filePath
	 *            path to the image file
	 * @return BufferedImage containing the loaded image's data
	 */
	public static BufferedImage loadImage(final String filePath) {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(filePath));
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return bimg;
	}

	/**
	 * Gets the Grayscale value for an RGB value.
	 * 
	 * @param r
	 *            Red
	 * @param g
	 *            Green
	 * @param b
	 *            Blue
	 * @return Grayscale value
	 */
	public static byte rgb2GrayValue(final int r, final int g, final int b) {
		return (byte) ((0.2989 * r) + (0.5870 * g) + (0.1140 * b));
	}

	/**
	 * Converts an RGB byte[] image to Grayscale byte[] image.
	 * 
	 * @param rgbByteArr
	 *            RGB image byte array
	 * @return Grayscale image as an array of bytes
	 */
	public static byte[] rgbByteArray2GrayByteArray(final byte[] rgbByteArr) {
		final byte[] grayArr = new byte[rgbByteArr.length / 3];
		int r, g, b;
		for (int i = 0; i < rgbByteArr.length; i += 3) {
			r = rgbByteArr[i + 2] & 0xff;
			g = rgbByteArr[i + 1] & 0xff;
			b = rgbByteArr[i] & 0xff;
			grayArr[i / 3] = (byte) ((0.2989 * r) + (0.5870 * g) + (0.1140 * b));
		}
		return grayArr;
	}

	/**
	 * Converts an int[] RGB image to byte[] Grayscale image.
	 * 
	 * @param in
	 *            input RGB image as array of integers
	 * @return Grayscale image as an array of bytes
	 */
	public static byte[] rgbIntArray2GrayByteArray(final int[] in) {
		int r, g, b;
		final byte[] res = new byte[in.length];
		for (int i = 0; i < in.length; i++) {
			r = in[i] & (0x000000FF);
			g = (in[i] & (0x0000FF00)) >> 8;
			b = (in[i] & (0x00FF0000)) >> 16;
			res[i] = (byte) ((0.2989 * r) + (0.5870 * g) + (0.1140 * b));
		}
		return res;
	}

	/**
	 * Converts an RGB int[] image to Grayscale int[] image.
	 * 
	 * @param in
	 *            RGB image integer array
	 * @return Grayscale image integer array
	 */
	public static int[] rgbIntArray2GrayIntArray(final int[] in, final int[] res) {
		int r, g, b, grey;
		for (int i = 0; i < in.length; i++) {
			/*
			 * r = in[i] & (0x000000FF); g = (in[i] & (0x0000FF00)) >> 8; b =
			 * (in[i] & (0x00FF0000)) >> 16; res[i] = (int) (0.2989 * r + 0.5870
			 * * g + 0.1140 * b);
			 */
			r = (int) (0.2989 * (in[i] & (0x000000FF)));
			g = (int) (0.5870 * ((in[i] & (0x0000FF00)) >> 8));
			b = (int) (0.1140 * ((in[i] & (0x00FF0000)) >> 16));
			grey = r + g + b;
			res[i] = ((grey + (grey << 8)) | (grey << 16));
		}
		return res;
	}

	/**
	 * Converts single dimensional byte array image to a two dimensional byte
	 * array image.
	 * 
	 * @param imgin
	 *            input single dimensional array
	 * @param width
	 *            image's width
	 * @param height
	 *            image's height
	 * @return two dimensional byte array image
	 */
	public static byte[][] singleDim2DoubleDim(final byte[] imgin,
			final int width, final int height) {
		final byte[][] res = new byte[width][height];
		for (int i = 0; i < imgin.length; i++)
			res[i % width][i / width] = imgin[i];
		return res;
	}

	public static int[] subtractGreyImage(final int[] img1, final int[] img2) {
		return subtractGreyImage(img1, img2, 0);
	}

	public synchronized static int[] subtractGreyImage(final int[] img1,
			final int[] img2, final int threshold) {
		// if(subResult==null || subResult.length!=img1.length)
		subResult = new int[img1.length];
		assert img1.length == img2.length : "different image size!";
		int sub;
		for (int i = 0; i < img1.length; i++) {
			sub = (img1[i] & 0x000000FF) - (img2[i] & 0x000000FF);
			sub = sub < 0 ? sub * -1 : sub;
			sub = sub | (sub << 8) | (sub << 16);
			if ((sub & 0x000000FF) > threshold)
				subResult[i] = sub;
			else
				subResult[i] = 0;
		}
		return subResult;
	}

	public static int[] subtractImage(final int[] img1, final int[] img2,
			final int x1, final int x2, final int y1, final int y2,
			final int width) {
		int subWidth, subHeight, sub, i;
		subWidth = x2 - x1;
		subHeight = y2 - y1;
		final int[] subData = new int[subWidth * subHeight];
		for (int x = x1; x < x2; x++)
			for (int y = y1; y < y2; y++) {
				i = x + (y * width);
				sub = img1[i] - img2[i];
				subData[(x - x1) + ((y - y1) * subWidth)] = sub < 0 ? sub * -1
						: sub;
			}
		return null;
	}

	/**
	 * Converts a byte[] Grayscale image to a byte[] RGB image. notice that the
	 * size of the output RGB image is triple that of the input Grayscale image.
	 * 
	 * @param grayArr
	 *            input grayscale byte array
	 * @return RGB byte array image
	 */
	public byte[] grayByteArray2RGBByteArray(final byte[] grayArr) {
		final byte[] rgbarr = new byte[grayArr.length * 3];
		int valgray;
		for (int i = 0; i < (grayArr.length * 3); i += 3) {
			valgray = grayArr[i / 3];
			rgbarr[i] = rgbarr[i + 1] = rgbarr[i + 2] = (byte) valgray;
		}
		return rgbarr;
	}

}
