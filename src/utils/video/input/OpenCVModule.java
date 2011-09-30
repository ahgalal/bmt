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

import hypermedia.video.OpenCV;
import utils.video.FrameIntArray;

/**
 * OpenCV video library.
 * 
 * @author Creative
 */
public class OpenCVModule implements VidInputter
{
	private static final long serialVersionUID = 1L;
	private int status; // cam status: 1 ready
	private int cam_index = 0;
	private FrameIntArray fia;
	private int width, height;
	OpenCV cv = null; // OpenCV Object
	Thread th_update_image = null; // the sample thread
	private boolean stop_stream;

	/**
	 * Runnable for updating the image stream from the webcam.
	 * 
	 * @author Creative
	 */
	private class RunnableOpenCV implements Runnable
	{
		@Override
		public void run()
		{
			cv.capture(width, height, cam_index);
			while (!stop_stream & th_update_image != null)
			{
				try
				{
					Thread.sleep(30);
				} catch (final InterruptedException e)
				{
					e.printStackTrace();
				}
				cv.read();
				fia.frame_data = cv.pixels();
				if (fia.frame_data != null)
					status = 1;
			}
		}

	}

	@Override
	public boolean startStream()
	{
		if (th_update_image == null)
			th_update_image = new Thread(new RunnableOpenCV());
		th_update_image.start();
		return true;
	}

	@Override
	public void stopModule()
	{
		stop_stream = true;
		try
		{
			Thread.sleep(30);
		} catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
		th_update_image = null;
		cv.stop();
		cv.dispose();
		cv = null;
	}

	@Override
	public boolean initialize(
			final FrameIntArray fia,
			final int width,
			final int height,
			final int cam_index)
	{
		this.fia = fia;
		this.width = width;
		this.height = height;
		this.cam_index = cam_index;
		cv = new OpenCV();
		// cv.capture( width, height,cam_index);
		return true;
	}

	@Override
	public void setFormat(final String s)
	{
		// Empty because OpenCV encapsulates the format in itself and gives us
		// an int[] array of RGB
	}

	@Override
	public int getStatus()
	{
		return status;
	}

	@Override
	public int getNumCams()
	{
		return 0;
	}

	@Override
	public int displayMoreSettings()
	{
		// not supported for OpenCV
		return 0;
	}

	@Override
	public String getName()
	{
		return "OpenCV";
	}

}