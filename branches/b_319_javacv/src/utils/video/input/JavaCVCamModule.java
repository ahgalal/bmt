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

import java.awt.Point;
import java.awt.image.BufferedImage;

import sun.awt.image.IntegerInterleavedRaster;
import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.Utils;
import utils.video.FrameIntArray;
import cam_lib.ReturnValue;

import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

/**
 * AGCamLib video library.
 * 
 * @author Creative
 */
public class JavaCVCamModule extends VidInputter<VidSourceConfigs> {

	public static final String	JAVA_CV_CAM	= "JavaCVCam";

	/**
	 * Runnable to update the image stream from the webcam.
	 * 
	 * @author Creative
	 */
	private class RunnableJavaCV implements Runnable {
		@Override
		public void run() {
			final Point dims = new Point();
			dims.x=grabber.getImageWidth();
			dims.y=grabber.getImageHeight();
			BufferedImage bufferedImage = new BufferedImage(dims.x, dims.y,BufferedImage.TYPE_INT_RGB);
			IntegerInterleavedRaster integerInterleavedRaster = (IntegerInterleavedRaster)bufferedImage.getRaster();
			int[] im = integerInterleavedRaster.getDataStorage();
			int[] intBuffer = null;
			while (!stopStream && (thUpdateImage != null)) {
				Utils.sleep(30);
				if(paused==false){
					try {
						IplImage iplImage = grabber.grab();
						JavaCVUtils.updateImageData(im,iplImage,intBuffer);
						fia.setFrameData(im);
					} catch (Exception e) {
						e.printStackTrace();
					}
					updateStatus();
				}
			}
		}
	}

	private OpenCVFrameGrabber grabber;
	private boolean		stopStream;

	private Thread		thUpdateImage;

	@Override
	public int displayMoreSettings() {
		return 0;
	}

	@Override
	public String getName() {
		return JAVA_CV_CAM;
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
		if (grabber == null)
			grabber = new OpenCVFrameGrabber(configs.getCamIndex());
		this.configs = configs;
		fia = frameData;
		
		grabber.setImageHeight(configs.getHeight());
		grabber.setImageWidth(configs.getWidth());
		
		
		/*if(initialize==null || initialize!=ReturnValue.SUCCESS)
			initialize = javaCV.initialize(configs.getWidth(), configs.getHeight(), configs.getCamIndex());*/
		initialize=ReturnValue.SUCCESS;
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
	}

	@Override
	public boolean startStream() {
		try {
			grabber.start();
			
			thUpdateImage = new Thread(new RunnableJavaCV(), JAVA_CV_CAM);
			thUpdateImage.start();

			return true;
		} catch (Exception e) {
			PManager.log.print("Error Starting the Webcam!", this,
					StatusSeverity.ERROR);
		}
		return false;
	}

	@Override
	public void stopModule() {
		stopStream = true;
		try {
			Thread.sleep(15);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		try {
			grabber.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initialize=ReturnValue.ERR_CAM;
		thUpdateImage = null;
	}

	@Override
	public SourceType getType() {
		return SourceType.CAM;
	}

}
