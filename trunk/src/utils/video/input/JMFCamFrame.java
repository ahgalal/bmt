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

package utils.video.input;

import javax.media.Buffer;

import utils.video.ImageManipulator;

/**
 * Image frame for JMF video library.
 * 
 * @author Creative
 */
public class JMFCamFrame {
	private Buffer		buffer	= null;
	private final int	height;
	private final int	width;

	/**
	 * Initializes the frame data and parameters.
	 * 
	 * @param data
	 *            image data to initialize the frame with
	 * @param w
	 *            image's width
	 * @param h
	 *            image's height
	 */
	public JMFCamFrame(final byte[] data, final int w, final int h) {
		width = w;
		height = h;
		buffer = new Buffer();
		updateBufferData(data);
	}

	/**
	 * Converts YUV frame to RGB byte frame.
	 * 
	 * @param yuvData1
	 *            YUV data byte array
	 * @return byte array representing the RGB image
	 */
	public byte[] convertYUV2RGB() {
		final byte[] yuvData = (byte[]) buffer.getData();
		final byte[] rgbData = new byte[width * height * 3];

		int rgbRow = height - 1;
		int rgbPtr = width * rgbRow * 3;

		for (int i = 0; i < yuvData.length; i += 4) {
			final int y1 = yuvData[i + 0] & 0xff;
			final int u = yuvData[i + 1] & 0xff;
			final int y2 = yuvData[i + 2] & 0xff;
			final int v = yuvData[i + 3] & 0xff;

			int b1, r1, g1, r2, g2, b2;

			b1 = (int) ((y1 + 1.722 * (u - 128)));
			g1 = (int) ((y1 - 0.714 * (v - 128) - 0.344 * (u - 128)));
			r1 = (int) ((y1 + 1.402 * (v - 128)));

			b2 = (int) ((y2 + 1.722 * (u - 128)));
			g2 = (int) ((y2 - 0.714 * (v - 128) - 0.344 * (u - 128)));
			r2 = (int) ((y2 + 1.402 * (v - 128)));

			if (b1 > 255)
				b1 = 255;
			if (g1 > 255)
				g1 = 255;
			if (r1 > 255)
				r1 = 255;
			if (b2 > 255)
				b2 = 255;
			if (g2 > 255)
				g2 = 255;
			if (r2 > 255)
				r2 = 255;

			if (b1 < 0)
				b1 = 0;
			if (g1 < 0)
				g1 = 0;
			if (r1 < 0)
				r1 = 0;
			if (b2 < 0)
				b2 = 0;
			if (g2 < 0)
				g2 = 0;
			if (r2 < 0)
				r2 = 0;

			rgbData[rgbPtr] = (byte) b1;
			rgbData[rgbPtr + 1] = (byte) g1;
			rgbData[rgbPtr + 2] = (byte) r1;
			rgbData[rgbPtr + 3] = (byte) b2;
			rgbData[rgbPtr + 4] = (byte) g2;
			rgbData[rgbPtr + 5] = (byte) r2;

			rgbPtr += 6;
			if (rgbPtr % (width * 3) == 0) {
				rgbRow--;
				rgbPtr = rgbRow * width * 3;
			}

		}
		return rgbData;
	}

	/**
	 * Gets the image as a 2D integer array.
	 * 
	 * @return 2D integer array representing the RGB image
	 */
	public int[][] get2DIntArray() {
		return ImageManipulator.intArrayTo2DIntArray(getRGBIntArray(), width,
				height);
	}

	/**
	 * Gets the data of the image in Grayscale format.
	 * 
	 * @return byte array representing the image's data
	 */
	public byte[] getGrayByteArray() {
		return ImageManipulator.rgbByteArray2GrayByteArray((byte[]) buffer
				.getData());
	}

	/**
	 * Gets the image's height.
	 * 
	 * @return integer with the value of the image's height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the image's RGB data as an array of bytes.
	 * 
	 * @return byte arry representing the image's RGB data
	 */
	public byte[] getRGBByteArray() {
		return (byte[]) buffer.getData();
	}

	/**
	 * Gets RGB integer array for the image.
	 * 
	 * @return RGB integer array representing the image's data
	 */
	public int[] getRGBIntArray() {
		return ImageManipulator.byteRGB2IntRGB((byte[]) buffer.getData());
	}

	/**
	 * Gets the image's width.
	 * 
	 * @return integer with the value of the image's width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Updates buffer's data.
	 * 
	 * @param newdata
	 *            new buffer's data
	 */
	public void updateBufferData(final byte[] newdata) {
		buffer.setData(newdata);
	}

}
