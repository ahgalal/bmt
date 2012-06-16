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
    private Buffer buffer = null;
    private final int width;
    private final int height;

    /**
     * Converts YUV frame to RGB byte frame.
     * 
     * @param yuv_data1
     *            YUV data byte array
     * @return byte array representing the RGB image
     */
    public byte[] convertYUV2RGB(final byte[] yuv_data1) {
	final byte[] yuv_data = (byte[]) buffer.getData();
	final byte[] rgb_data = new byte[width * height * 3];

	int rgb_row = height - 1;
	int rgb_ptr = width * rgb_row * 3;

	for (int i = 0; i < yuv_data.length; i += 4) {
	    final int y1 = yuv_data[i + 0] & 0xff;
	    final int u = yuv_data[i + 1] & 0xff;
	    final int y2 = yuv_data[i + 2] & 0xff;
	    final int v = yuv_data[i + 3] & 0xff;

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

	    rgb_data[rgb_ptr] = (byte) b1;
	    rgb_data[rgb_ptr + 1] = (byte) g1;
	    rgb_data[rgb_ptr + 2] = (byte) r1;
	    rgb_data[rgb_ptr + 3] = (byte) b2;
	    rgb_data[rgb_ptr + 4] = (byte) g2;
	    rgb_data[rgb_ptr + 5] = (byte) r2;

	    rgb_ptr += 6;
	    if (rgb_ptr % (width * 3) == 0) {
		rgb_row--;
		rgb_ptr = rgb_row * width * 3;
	    }

	}
	return rgb_data;
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

    /**
     * Gets RGB integer array for the image.
     * 
     * @return RGB integer array representing the image's data
     */
    public int[] getRGBIntArray() {
	return ImageManipulator.byteRGB2IntRGB((byte[]) buffer.getData());
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
     * Gets the image's RGB data as an array of bytes.
     * 
     * @return byte arry representing the image's RGB data
     */
    public byte[] getRGBByteArray() {
	return (byte[]) buffer.getData();
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
     * Gets the image's width.
     * 
     * @return integer with the value of the image's width
     */
    public int getWidth() {
	return width;
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

}
