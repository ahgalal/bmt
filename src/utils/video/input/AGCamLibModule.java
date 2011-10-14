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
import utils.video.FrameIntArray;
import cam_lib.JAGCamLib;
import cam_lib.ReturnValue;

/**
 * AGCamLib video library.
 * 
 * @author Creative
 */
public class AGCamLibModule implements VidInputter
{

	private JAGCamLib ag_cam;
	@SuppressWarnings("unused")
	private int width, height, cam_index, status;
	private FrameIntArray fia;
	private Thread th_update_image;
	private boolean stop_stream;

	@Override
	public boolean startStream()
	{
		if (ag_cam.start() == ReturnValue.SUCCESS)
		{
			th_update_image = new Thread(new RunnableAGCamLib());
			th_update_image.start();

			return true;
		}
		else
		{
			PManager.log.print("Error Starting the Webcam!", this, StatusSeverity.ERROR);
			return false;
		}
	}

	@Override
	public void stopModule()
	{
		stop_stream = true;
		try
		{
			Thread.sleep(15);
		} catch (final InterruptedException e)
		{
			e.printStackTrace();
		}

		ag_cam.stop();
		ag_cam.deInitialize();
		ag_cam = null;
		th_update_image = null;

	}

	@Override
	public int getNumCams()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatus()
	{
		return status;
	}

	@Override
	public boolean initialize(
			final FrameIntArray frameData,
			final int width,
			final int height,
			final int camIndex)
	{
		if (ag_cam == null)
			ag_cam = new JAGCamLib();
		this.width = width;
		this.height = height;
		fia = frameData;
		this.cam_index = camIndex;

		if (ag_cam.initialize(width, height, camIndex) == ReturnValue.SUCCESS)
			return true;
		else
		{
			PManager.log.print(
					"Error initializing the Webcam!",
					this,
					StatusSeverity.ERROR);
			return false;
		}
	}

	@Override
	public void setFormat(final String s)
	{
		/**
		 * Empty ... AGCamLib encapsulates the video format .. and returns an
		 * RGB integer array to the VideoManager .. just like OpenCV!
		 */
	}

	/**
	 * Runnable to update the image stream from the webcam.
	 * 
	 * @author Creative
	 */
	private class RunnableAGCamLib implements Runnable
	{
		@Override
		public void run()
		{
			while (!stop_stream && th_update_image != null)
			{
				try
				{
					Thread.sleep(30);
				} catch (final InterruptedException e)
				{
					e.printStackTrace();
				}

				fia.frame_data = ag_cam.grabIntRGBFrame();

				status = 1;
			}
		}

	}

	@Override
	public int displayMoreSettings()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return "AGCamLib";
	}

}
