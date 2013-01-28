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

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.Utils;
import utils.video.FrameIntArray;
import cam_lib.JAGCamLib;
import cam_lib.ReturnValue;

/**
 * AGCamLib video library.
 * 
 * @author Creative
 */
public class AGCamLibModule extends VidInputter<VidSourceConfigs> {

	/**
	 * Runnable to update the image stream from the webcam.
	 * 
	 * @author Creative
	 */
	private class RunnableAGCamLib implements Runnable {
		@Override
		public void run() {
			while (!stopStream && (thUpdateImage != null)) {
				Utils.sleep(30);
				if(paused==false){
					fia.setFrameData(agCam.grabIntRGBFrame());
					
					updateStatus();
				}
			}
		}

	}

	private JAGCamLib	agCam;
	private boolean		stopStream;

	private Thread		thUpdateImage;

	@Override
	public int displayMoreSettings() {
		return 0;
	}

	@Override
	public String getName() {
		return "AGCamLib";
	}

	@Override
	public int getNumCams() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SourceStatus getStatus() {
		return status;
	}
	private static ReturnValue initialize;
	@Override
	public boolean initialize(final FrameIntArray frameData,
			final VidSourceConfigs configs) {
		if (agCam == null)
			agCam = new JAGCamLib();
		this.configs = configs;
		fia = frameData;
		
		if(initialize==null || initialize!=ReturnValue.SUCCESS)
			initialize = agCam.initialize(configs.getWidth(), configs.getHeight(), configs.getCamIndex());
		if (initialize == ReturnValue.SUCCESS)
			return true;
		else {
			PManager.log.print("Error initializing the Webcam!", this,
					StatusSeverity.ERROR);
			return false;
		}
	}

	@Override
	public void setFormat(final String s) {
		/**
		 * Empty ... AGCamLib encapsulates the video format .. and returns an
		 * RGB integer array to the VideoManager .. just like OpenCV!
		 */
	}

	@Override
	public boolean startStream() {
		if (agCam.start() == ReturnValue.SUCCESS) {
			thUpdateImage = new Thread(new RunnableAGCamLib(),"AGCamLib");
			thUpdateImage.start();

			return true;
		} else {
			PManager.log.print("Error Starting the Webcam!", this,
					StatusSeverity.ERROR);
			return false;
		}
	}

	@Override
	public void stopModule() {
		stopStream = true;
		try {
			Thread.sleep(15);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		agCam.stop();
		agCam.deInitialize();
		initialize=ReturnValue.ERR_CAM;
		agCam = null;
		thUpdateImage = null;

	}

	@Override
	public SourceType getType() {
		return SourceType.CAM;
	}

}
