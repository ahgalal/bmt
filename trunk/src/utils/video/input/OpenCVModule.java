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
import utils.PManager;
import utils.Utils;
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
			if (configs.getVideoFilePath()== null)
				cv.capture(configs.getWidth(), configs.getHeight(), configs.getCamIndex());
			else
				cv.movie(configs.getVideoFilePath(),configs.getWidth(),configs.getHeight());
			while (!stopStream & (thUpdateImage != null)) {
				Utils.sleep(30);
				if(paused==false){
					System.out.println("OpenCV, reading frame..");
					cv.read();
					fia.setFrameData(cv.pixels());
					if (fia.getFrameData() != null)
						status = SourceStatus.STREAMING;
				}
			}
		}

	}

	private static final long	serialVersionUID	= 1L;
	private OpenCV						cv					= null; // OpenCV Object
	private boolean				stopStream;

	private Thread						thUpdateImage		= null; // the sample thread

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
	public boolean initialize(final FrameIntArray frameData,
			final OpenCVConfigs configs) {
		PManager.log.print("initializing..", this);
		this.fia = frameData;
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
		if (thUpdateImage == null)
			thUpdateImage = new Thread(new RunnableOpenCV(),"OpenCV");
		thUpdateImage.start();
		return true;
	}

	@Override
	public void stopModule() {
		stopStream = true;
		Utils.sleep(30);
		thUpdateImage = null;
		cv.stop();
		cv.dispose();
		cv = null;
	}
	
	@Override
	public SourceType getType() {
		return SourceType.CAM;
	}

}