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

import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Codec;
import javax.media.Controller;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoDataSourceException;
import javax.media.NoPlayerException;
import javax.media.NoProcessorException;
import javax.media.NotRealizedError;
import javax.media.Player;
import javax.media.Processor;
import javax.media.UnsupportedPlugInException;
import javax.media.control.FormatControl;
import javax.media.control.TrackControl;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;

import utils.video.FrameIntArray;

/**
 * JMF video library.
 * 
 * @author Creative
 */
public class JMFModule extends VidInputter<VidSourceConfigs>
{
	private String format;
	VideoFormat video_format = null;
	private Processor proc_1;
	private Player pl;
	private JMFGrabber ana_eff = null;

	/**
	 * Creates the MediaLocator for the webcam device.
	 * 
	 * @param format_to_use
	 *            VideoFormat, either RGB or YUV
	 * @return MediaLocator object corresponding to the webcam
	 */
	@SuppressWarnings("unchecked")
	private MediaLocator obtainMediaLocator(final VideoFormat format_to_use)
	{
		MediaLocator ml = null;
		final Vector devicelist = CaptureDeviceManager.getDeviceList(new VideoFormat(null));

		if (devicelist.size() == 0)
		{
			System.err.print("No Cams Available using normal way ... will try finding a CAM using hardcoded way! :)\n");
			try
			{
				ml = new MediaLocator("vfw://" + configs.camIndex);
			} catch (final Exception e)
			{
				e.printStackTrace();
				System.out.print("media locator!!!!!!!!!!!!?");
			}
		}
		else
		{
			if (devicelist.size() == 1) // Only 1 CAM
			{
				ml = ((CaptureDeviceInfo) devicelist.get(0)).getLocator();
			}
			else
			// More than 1 CAM
			{
				boolean breaker = false;
				CaptureDeviceInfo tmp_device;
				Format[] tmp_formats;
				for (int i = 0; i < devicelist.size(); i++) // Loop on CAMs
				{
					tmp_device = (CaptureDeviceInfo) devicelist.get(i);
					tmp_formats = tmp_device.getFormats();
					// Loop on Formats, searching for rgb640x480
					for (int j = 0; j < tmp_formats.length; j++)
					{
						// if found ... locate/use the device
						if (tmp_formats[j].matches(format_to_use))
						{
							ml = tmp_device.getLocator();
							breaker = true;
							break;
						}
					}
					if (breaker)
						break;
				}
			}
		}
		return ml;
	}

	/**
	 * Initializes the MediaLocator,VideoFormat,Processor and JMFGrabber Effect.
	 * 
	 * @param fia
	 *            image data container
	 * @return true for success
	 */
	private boolean initializeJMF(final FrameIntArray fia)
	{
		try
		{
			MediaLocator ml = null;

			final RGBFormat rgb640x480 = new RGBFormat(
					new Dimension(configs.width, configs.height),
					configs.width * configs.height * 3,
					Format.byteArray,
					30.0f,
					24,
					-1,
					-1,
					-1,
					-1,
					-1,
					-1,
					-1);

			final YUVFormat yuv640x480_device = new YUVFormat(
					new Dimension(configs.width, configs.height),
					configs.width * configs.height * 2,
					Format.byteArray,
					30.0f,
					32,
					1280,
					1280,
					0,
					1,
					3);
			// YUVFormat yuv640x480 = new YUVFormat(new Dimension(352, 288),-1,
			// RGBFormat.byteArray, -1, 2, -1 , -1 , -1,-1,-1);

			// RGBFormat rgb320x240 = new RGBFormat(new Dimension(320, 240),
			// RGBFormat.NOT_SPECIFIED, RGBFormat.byteArray, -1, 24, 3, 2,
			// 1,-1,-1,-1,-1);
			VideoFormat format_to_use;
			if (video_format == null)
				format_to_use = yuv640x480_device;
			else
				format_to_use = video_format;

			if (format == "YUV")
				format_to_use = yuv640x480_device;
			else if (format == "RGB")
				format_to_use = rgb640x480;

			ml = obtainMediaLocator(format_to_use);

			DataSource ds = null;
			try
			{

				ds = Manager.createDataSource(ml);
				// ds=Manager.createCloneableDataSource(ds);
			} catch (final NoDataSourceException e1)
			{
				System.out.print("Error using the media locator! .. Aborting!\n");
				System.out.print("ml: " + ml.toString());
				return false;
				// MsgBox.show(MainGUI.getDefault().getsShell(),
				// "Error with CAM\n" + e1.getMessage(), "Error", SWT.ERROR);
			}

			/*
			 * formatcontrol is an array of FormatControl ... this array
			 * contains all FormatControls of the specified
			 * DataSource"Casted as CaptureDeviceInfo" ex: if we have a single
			 * channel source (video no audio) .. so, we will have only one
			 * element inside the formatcontrol array , this element represents
			 * the FormatControl of this Video Channel. we can then change the
			 * Format of this Video Channel by using:
			 * formatcontrol[0].set.setFormat(video_format); also, we can get a
			 * list of supported Formats by this Channel using:
			 * formatcontrol[0].getSupportedFormats()
			 */
			System.err.print("0");
			final FormatControl[] formatcontrol = ((CaptureDevice) ds).getFormatControls();
			final Format[] supported_formats = formatcontrol[0].getSupportedFormats();

			System.err.print("1");
			for (int i = 0; i < supported_formats.length; i++)
			{
				if (supported_formats[i].matches(format_to_use))// yuv640x480_device))
				{
					System.err.print("1");
					video_format = (VideoFormat) supported_formats[i];
					break;
				}
			}

			if (video_format == null)
			{
				System.err.print("The CAM does not support "
						+ format_to_use.toString()
						+ "video!");
				// MsgBox.show(MainGUI.getDefault().getsShell(),
				// "The CAM does not support RGB640*480 video!", "Error",
				// SWT.ERROR);
				ds.disconnect();
				return false;
			}

			System.out.print(video_format.toString());

			formatcontrol[0].setFormat(video_format);

			proc_1 = Manager.createProcessor(ds);

			proc_1.configure();
			while (proc_1.getState() != Processor.Configured)
				Thread.sleep(10);
			final TrackControl[] tracks = proc_1.getTrackControls();
			// proc_1.setContentDescriptor(null);
			System.err.print("2");

			ana_eff = new JMFGrabber(
					video_format.getSize().width,
					video_format.getSize().height,
					fia);
			ana_eff.setInputFormat(tracks[0].getFormat());
			ana_eff.setOutputFormat(new RGBFormat(new Dimension(configs.width, configs.height), configs.width
					* configs.height
					* 3, Format.byteArray, 30.0f, 24, 3, 2, 1, 3, configs.width * 3, 0, 0));

			System.err.print("3");

			final Codec[] codecs = { ana_eff };
			try
			{
				tracks[0].setCodecChain(codecs);
			} catch (final UnsupportedPlugInException e)
			{
				e.printStackTrace();
			}

			// proc_1.setContentDescriptor(null);

			System.err.print("4");

			proc_1.realize();
			while (proc_1.getState() != Controller.Realized)
				Thread.sleep(10);

			// vis_comp=pl.getVisualComponent();
			// MainGUI.getDefault().getAwt_video_frame().add(vis_comp);

		} catch (final NoProcessorException e)
		{
			e.printStackTrace();
		} catch (final MalformedURLException e)
		{
			e.printStackTrace();
		} catch (final IOException e)
		{
			e.printStackTrace();
		} catch (final InterruptedException e)
		{
			e.printStackTrace();
		}

		System.err.print("9");

		return true;
	}

	@Override
	public boolean startStream()
	{
		try
		{
			proc_1.start();
			while (proc_1.getState() != Controller.Started)
				Thread.sleep(10);

			System.err.print("5");

			try
			{
				pl = Manager.createPlayer(proc_1.getDataOutput());

				pl.realize();
				while (pl.getState() != Controller.Realized)
					Thread.sleep(10);
			} catch (final NoPlayerException e)
			{
				e.printStackTrace();
			} catch (final NotRealizedError e)
			{
				e.printStackTrace();
			} catch (final IOException e)
			{
				e.printStackTrace();
			}

			System.err.print("6");

			/*
			 * Component vis_comp=pl.getVisualComponent();
			 * vis_comp.setVisible(true);
			 * MainGUI.getDefault().getAwt_video_frame().add(vis_comp);
			 */

			System.err.print("7");

			/*
			 * DataSource ds2 = ((SourceCloneable)ds).createClone(); try {
			 * Player pl2 = Manager.createRealizedPlayer(ds2); pl2.realize();
			 * while(pl2.getState()!=Controller.Realized);
			 * MainGUI.getDefault().awt2.add(pl2.getVisualComponent());
			 * pl2.start(); } catch (NoPlayerException e) { e.printStackTrace();
			 * } catch (CannotRealizeException e) { e.printStackTrace(); }
			 */

			try
			{
				pl.start();
				while (pl.getState() != Controller.Started)
					Thread.sleep(10);
				System.err.print("8");
			} catch (final Exception e)
			{
				e.printStackTrace();
			}
			return true;
		} catch (final InterruptedException e1)
		{
			e1.printStackTrace();
		}
		return false;
		// frameGrabber =
		// (FrameGrabbingControl)proc_1.getControl("javax.media.control.FrameGrabbingControl");
	}

	@Override
	public void stopModule()
	{
		try
		{
			format = null;
			video_format = null;
			if (proc_1 != null)
			{
				proc_1.stop();
				proc_1.close();
				while ((proc_1.getState() == Controller.Started))

					Thread.sleep(10);

				proc_1 = null;
			}
			if (pl != null)
			{
				pl.stop();
				pl.close();
				while ((pl.getState() == Controller.Started))
					Thread.sleep(10);
				pl = null;
			}
			ana_eff = null;
		} catch (final InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setFormat(final String s)
	{
		format = s;
	}

	@Override
	public int getStatus()
	{
		return ana_eff.getStatus();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getNumCams()
	{
		final Vector devicelist = CaptureDeviceManager.getDeviceList(new VideoFormat(null));
		return devicelist.size();
	}

	@Override
	public int displayMoreSettings()
	{
		// not supported for JMF
		return 0;
	}

	@Override
	public String getName()
	{
		return "JMF";
	}

	@Override
	public boolean initialize(FrameIntArray frame_data, VidSourceConfigs configs)
	{
		this.configs=configs;
		return initializeJMF(frame_data);
	}

}
