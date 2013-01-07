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

import java.util.List;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.FrameIntArray;
import utils.video.ImageManipulator;
import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.ImageFormat;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public class V4L2Module extends VidInputter<VidSourceConfigs> implements
		CaptureCallback {

	private VideoDevice		vdevice;
	private FrameGrabber	vfg;

	@Override
	public int displayMoreSettings() {
		return 0;
	}

	@Override
	public void exceptionReceived(final V4L4JException arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return "V4L2";
	}

	@Override
	public int getNumCams() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public boolean initialize(final FrameIntArray frame_data,
			final VidSourceConfigs configs) {
		this.configs = configs;
		if (vdevice == null)
			try {
				vdevice = new VideoDevice("/dev/video" + configs.camIndex);
			} catch (final V4L4JException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		try {
			final List<ImageFormat> formats = vdevice.getDeviceInfo()
					.getFormatList().getRGBEncodableFormats();
			vfg = vdevice.getRGBFrameGrabber(configs.width, configs.height, 0,
					0, formats.get(0));
			vfg.setCaptureCallback(this);
			vfg.setFrameInterval(1, 30);
		} catch (final V4L4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (vfg != null)
			return true;
		else {
			PManager.log.print("Error initializing the Webcam!", this,
					StatusSeverity.ERROR);
			return false;
		}

	}

	@Override
	public void nextFrame(final VideoFrame vframe) {
		/*
		 * if (!stop_stream && th_update_image != null) {
		 */
		/*
		 * try { Thread.sleep(30); } catch (final InterruptedException e) {
		 * e.printStackTrace(); }
		 */
		fia.frame_data = ImageManipulator.byteBGR2IntRGB(vframe.getBytes());

		status = 1;
		vframe.recycle();
		/* } */
	}

	@Override
	public void setFormat(final String s) {
	}

	@Override
	public boolean startStream() {

		try {
			vfg.startCapture();
		} catch (final V4L4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void stopModule() {
		try {
			Thread.sleep(15);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		vfg.stopCapture();
		vdevice.releaseFrameGrabber();
	}

}