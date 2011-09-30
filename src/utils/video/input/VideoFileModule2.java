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

/**
 * 
 */
package utils.video.input;

import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

import javax.media.Processor;

import org.eclipse.swt.widgets.Widget;

import utils.video.FrameIntArray;
import utils.video.ImageManipulator;

import com.sun.media.protocol.avi.DataSource;

import de.humatic.dsj.DSFiltergraph;
import de.humatic.dsj.DSMovie;

/**
 * @author Creative
 *
 */
public class VideoFileModule2 implements VidInputter
{

	private Processor proc;
	private DataSource data_src;
	private JMFGrabber ana_eff = null;
	private boolean stop_stream;
	private FrameIntArray fia;
	private int status;
	int[] data;
	private class RunnableAGCamLib implements Runnable
	{
		boolean sett;

		@Override
		public void run()
		{
			fia.frame_data = new int[640*480];
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			movie.play();
			//movie.pause();
			while (!stop_stream)
			{
				//movie.play();
				try
				{

					Thread.sleep(30);
				} catch (final InterruptedException e)
				{
					e.printStackTrace();
				}
				//movie.step(1);
				long l1 = System.currentTimeMillis();
				//if(!sett)
				{
					//data= ImageManipulator.byteRGB2IntRGB(((DataBufferByte)movie.getImage().getRaster().getDataBuffer()).getData());
					fia.frame_data= ImageManipulator.byteRGB2IntRGB(movie.getData());
					sett=true;
				}
/*				else
				{
					System.arraycopy(data, 0, fia.frame_data , 0, data.length);
				}*/
				//getRGB(0, 0, 640, 480, null, 0, 640);
				long l2 = System.currentTimeMillis();
				status = 1;
				System.out.println(l2-l1 + "\n");
			}
		}

	}
	/**
	 * 
	 */
	public VideoFileModule2()
	{}

	/* (non-Javadoc)
	 * @see utils.video.input.VidInputter#displayMoreSettings()
	 */
	@Override
	public int displayMoreSettings()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see utils.video.input.VidInputter#getName()
	 */
	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return "VideoFileReader";
	}

	/* (non-Javadoc)
	 * @see utils.video.input.VidInputter#getNumCams()
	 */
	@Override
	public int getNumCams()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see utils.video.input.VidInputter#getStatus()
	 */
	@Override
	public int getStatus()
	{
		// TODO Auto-generated method stub
		return status;
	}
	DSMovie movie;
	private Thread th_update_image;
	/* (non-Javadoc)
	 * @see utils.video.input.VidInputter#initialize(utils.video.FrameIntArray, int, int, int)
	 */
	@Override
	public boolean initialize(FrameIntArray frameData, int width, int height, int camIndex)
	{
		fia = frameData;
		//movie = new DSMovie("video.avi", DSFiltergraph.DD7, null);
		movie = new DSMovie("video.avi", DSFiltergraph.DD7, null);



		return true;
		/*

		data_src = new DataSource();
		try
		{
			width=640;
			height=272;
			data_src.setLocator(new MediaLocator(new URL("file:\\E:\\jmj-g4ce.avi")));
			data_src.connect();
			proc = Manager.createProcessor(data_src);

			proc.configure();
			while (proc.getState() != Processor.Configured)
				Thread.sleep(10);
			final TrackControl[] tracks = proc.getTrackControls();

			ana_eff = new JMFGrabber(
					width,
					height,
					frameData);
			ana_eff.setInputFormat(tracks[0].getFormat());
			ana_eff.setOutputFormat(new RGBFormat(new Dimension(width, height), width
		 * height
		 * 3, Format.byteArray, 30.0f, 12, 3, 2, 1, 3, width * 3, 0, 0));

			final Codec[] codecs = { ana_eff };
			try
			{
				tracks[0].setCodecChain(codecs);
			} catch (final UnsupportedPlugInException e)
			{
				e.printStackTrace();
			}



			proc.realize();
			while (proc.getState() != Controller.Realized)
				Thread.sleep(10);


			proc.start();
			while (proc.getState() != Controller.Started)
				Thread.sleep(10);


		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (NoProcessorException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
		 */}

	/* (non-Javadoc)
	 * @see utils.video.input.VidInputter#setFormat(java.lang.String)
	 */
	@Override
	public void setFormat(String format)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see utils.video.input.VidInputter#startStream()
	 */
	@Override
	public boolean startStream()
	{

		th_update_image = new Thread(new RunnableAGCamLib());
		th_update_image.start();
		stop_stream=false;
		return true;
	}

	/* (non-Javadoc)
	 * @see utils.video.input.VidInputter#stopModule()
	 */
	@Override
	public void stopModule()
	{
		stop_stream=true;
		movie.stop();

	}

}
