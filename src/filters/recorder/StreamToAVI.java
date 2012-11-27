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

package filters.recorder;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import lib_avi.AVIOutputStream;
import lib_avi.AVIOutputStream.VideoFormat;

/**
 * Video Stream to AVI Utility.
 * 
 * @author Creative
 */
public class StreamToAVI {
	/**
	 * Specification of the state of the object.
	 * 
	 * @author Creative
	 */
	private enum State {
		INITIALIZED
	}

	private AVIOutputStream	avi_op;
	private int[]			data;
	private BufferedImage	image;

	private State			state;

	/**
	 * Closes the AVI session.
	 */
	public void close() {
		try {
			avi_op.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialization.
	 * 
	 * @param filename
	 *            filename to save the video frames to
	 * @param format
	 *            video format
	 * @param framerate
	 *            frame rate of the video file
	 * @param width
	 *            video frame width
	 * @param height
	 *            video frame height
	 */
	public void initialize(final String filename, final VideoFormat format,
			final int framerate, final int width, final int height) {
		try {
			avi_op = new AVIOutputStream(new File(filename), format);
			avi_op.setFrameRate(framerate);
			avi_op.setVideoDimension(width, height);
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			data = ((DataBufferInt) (image.getRaster().getDataBuffer()))
					.getData();
			state = State.INITIALIZED;
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes video frame to the video file.
	 * 
	 * @param frame_data
	 *            video frame image
	 */
	public void writeFrame(final int[] frame_data) {
		if (state == State.INITIALIZED) {
			System.arraycopy(frame_data, 0, data, 0, frame_data.length);
			try {
				avi_op.writeFrame(image);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sets timescale of the video library, such that:</br></br>
	 * samples per second = framerate/timescale
	 * @param val
	 */
	public void setTimeScale(int val){
		avi_op.setTimeScale(val);
	}

}
