/***************************************************************************
 *  Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly,
 *  Sarah Hamid and Mohammed Ahmed Ramadan
 *  contact: ceng.ahmedgalal@gmail.com
 *
 *  This file is part of Behavioral Monitoring Tool.
 *
 *  Behavioral Monitoring Tool is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, version 3 of the
 *  License.
 *
 *  Behavioral Monitoring Tool is distributed in the hope that it
 *  will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Behavioral Monitoring Tool.
 *  If not, see <http://www.gnu.org/licenses/>.
 *   
 **************************************************************************/

package utils.video.input;

import utils.video.FrameIntArray;
import JMyron.JMyron;

/**
 * JMyron video library.
 * 
 * @author Creative
 */
public class JMyronModule implements VidInputter
{

	private JMyron jmyron;
	@SuppressWarnings("unused")
	private int width, height, cam_index, status;
	private FrameIntArray fia;
	private Thread th_jmyron_update_image;
	private boolean stop_stream;

	@Override
	public boolean startStream()
	{
		jmyron.start(width, height);

		th_jmyron_update_image = new Thread(new RunnableJMyron());
		th_jmyron_update_image.start();

		return true;
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

		jmyron.stop();
		jmyron = null;
		th_jmyron_update_image = null;

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
		if (jmyron == null)
			jmyron = new JMyron();
		this.width = width;
		this.height = height;
		fia = frameData;
		this.cam_index = camIndex;

		return true;
	}

	@Override
	public void setFormat(final String s)
	{
		/**
		 *Empty ... JMyron encapsulates the video format .. and returns an RGB
		 * integer array to the VideoManager .. just like OpenCV!
		 */
	}

	/**
	 * Runnable for updating the image stream from the webcam.
	 * 
	 * @author Creative
	 */
	private class RunnableJMyron implements Runnable
	{
		@Override
		public void run()
		{
			while (!stop_stream && th_jmyron_update_image != null)
			{
				try
				{
					Thread.sleep(20);
				} catch (final InterruptedException e)
				{
					e.printStackTrace();
				}

				jmyron.update();
				fia.frame_data = jmyron.image();

				status = 1;
			}
		}

	}

	@Override
	public int displayMoreSettings()
	{
		if (jmyron == null)
			jmyron = new JMyron();
		jmyron.settings();
		return 0;
	}

	@Override
	public String getName()
	{
		return "JMyron";
	}

}
