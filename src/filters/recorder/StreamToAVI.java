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
	private long			prevSampleTime			= 0;
	private long			noFrames				= 0;
	private long			accumulativeRecordTime	= 0;
	private AVIOutputStream	aviOp;
	private int[]			data;
	private BufferedImage	image;

	private State			state;

	/**
	 * Closes the AVI session.
	 */
	public void close() {
		try {
			double framesPerSecond = calculateFramesPerSecond();
			//aviSaver.setTimeScale(timescale);
			setFrameRate(framesPerSecond);
			aviOp.close();
			
			System.out.println("accRecordTime: "
					+ accumulativeRecordTime + " FPS: "+ framesPerSecond);
			accumulativeRecordTime = 0;

			noFrames = 0;
			prevSampleTime = 0;
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private double calculateFramesPerSecond() {
		double framesPerSecond = 1000 * noFrames / (double) accumulativeRecordTime;
		return framesPerSecond;
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
			aviOp = new AVIOutputStream(new File(filename), format);
			aviOp.setFrameRate(framerate);
			aviOp.setVideoDimension(width, height);
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
	 * @param frameData
	 *            video frame image
	 */
	public void writeFrame(final int[] frameData) {
		if (state == State.INITIALIZED) {
			
			// calculate video time and number of frames
			final long currentSampleTime = System.currentTimeMillis();
			if (prevSampleTime != 0) {
				long deltaSamples = currentSampleTime - prevSampleTime;
				if(deltaSamples>333){ // more than third of a second (stream is paused)
					if(noFrames==0)
						deltaSamples=30;
					else
						deltaSamples=accumulativeRecordTime/noFrames;
					System.out.println("setting deltaSamples to avg: "+ deltaSamples);
				}
				accumulativeRecordTime += deltaSamples;
				noFrames++;
			}
			prevSampleTime = currentSampleTime;
			
			System.arraycopy(frameData, 0, data, 0, frameData.length);
			try {
				aviOp.writeFrame(image);
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
		aviOp.setTimeScale(val);
	}

	public void setFrameRate(double d) {
		aviOp.setFrameRate(d);
	}

}