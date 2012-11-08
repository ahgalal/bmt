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

import hypermedia.video.OpenCV;
import utils.video.FrameIntArray;

/**
 * OpenCV video library.
 * 
 * @author Creative
 */
public class OpenCVModule extends VidInputter<OpenCVConfigs> {
	/**
	 * Runnable for updating the image stream from the webcam.
	 * 
	 * @author Creative
	 */
	private class RunnableOpenCV implements Runnable {
		@Override
		public void run() {
			if (configs.fileName == null)
				cv.capture(configs.width, configs.height, configs.camIndex);
			else
				cv.movie("C:\\vid.avi");
			while (!stop_stream & (th_update_image != null)) {
				try {
					Thread.sleep(30);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
				cv.read();
				fia.frame_data = cv.pixels();
				if (fia.frame_data != null)
					status = SourceStatus.STREAMING;
			}
		}

	}

	private static final long	serialVersionUID	= 1L;
	OpenCV						cv					= null; // OpenCV Object
	private boolean				stop_stream;

	Thread						th_update_image		= null; // the sample thread

	@Override
	public int displayMoreSettings() {
		// not supported for OpenCV
		return 0;
	}

	@Override
	public String getName() {
		return "OpenCV";
	}

	@Override
	public int getNumCams() {
		return 0;
	}

	@Override
	public SourceStatus getStatus() {
		return status;
	}

	@Override
	public boolean initialize(final FrameIntArray frame_data,
			final OpenCVConfigs configs) {
		this.fia = frame_data;
		this.configs = configs;
		cv = new OpenCV();
		return true;
	}

	@Override
	public void setFormat(final String s) {
		// Empty because OpenCV encapsulates the format in itself and gives us
		// an int[] array of RGB
	}

	@Override
	public boolean startStream() {
		if (th_update_image == null)
			th_update_image = new Thread(new RunnableOpenCV());
		th_update_image.start();
		return true;
	}

	@Override
	public void stopModule() {
		stop_stream = true;
		try {
			Thread.sleep(30);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		th_update_image = null;
		cv.stop();
		cv.dispose();
		cv = null;
	}
	
	@Override
	public void pauseStream() {
		// this ia used as a cam module, no pause is supported for cams currently.
	}

	@Override
	public void resumeStream() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public SourceType getType() {
		return SourceType.CAM;
	}

}