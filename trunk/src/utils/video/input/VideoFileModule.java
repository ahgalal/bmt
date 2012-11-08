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

/**
 * 
 */
package utils.video.input;

import jagvidlib.JAGVidLib;

import java.awt.Point;

import utils.video.FrameIntArray;

/**
 * @author Creative
 */
public class VideoFileModule extends VidInputter<AGVidLibConfigs> {
	private class RunnableAGCamLib implements Runnable {
		@Override
		public void run() {
			final Point dims = vidLib.getVideoDimensions();
			fia.frame_data = new int[dims.x * dims.y];
/*			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e1) {
				e1.printStackTrace();
			}*/
			vidLib.play();
			while (!stop_stream) {
				synchronized (this) {
					while (paused) {
						try {
							this.wait();
						} catch (InterruptedException e) {
						}
					}	
				}

				// long l1 = System.currentTimeMillis();
				fia.frame_data = vidLib.getCurrentFrameInt();
				// long l2 = System.currentTimeMillis();
				if (fia.frame_data != null)
					status = SourceStatus.STREAMING;
				else{
					if(paused)
						status = SourceStatus.PAUSED;
					else
						status=SourceStatus.ERROR;
				}
				
				try {
					Thread.sleep(30);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
				// System.out.println(l2-l1 + "\n");
			}
		}

	}

	private boolean			stop_stream;

	private Thread			th_update_image;

	private final JAGVidLib	vidLib;

	/**
	 * 
	 */
	public VideoFileModule() {
		vidLib = new JAGVidLib();
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#displayMoreSettings()
	 */
	@Override
	public int displayMoreSettings() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#getName()
	 */
	@Override
	public String getName() {
		return "VideoFileReader";
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#getNumCams()
	 */
	@Override
	public int getNumCams() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#getStatus()
	 */
	@Override
	public SourceStatus getStatus() {
		if(paused)
			status=SourceStatus.PAUSED;
		return status;
	}

	@Override
	public boolean initialize(final FrameIntArray frame_data,
			final AGVidLibConfigs configs) {
		this.configs = configs;
		fia = frame_data;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#setFormat(java.lang.String)
	 */
	@Override
	public void setFormat(final String format) {

	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#startStream()
	 */
	@Override
	public boolean startStream() {
		vidLib.initialize(configs.vidFile);
		th_update_image = new Thread(new RunnableAGCamLib());
		th_update_image.start();
		stop_stream = false;
		return true;
	}
	@Override
	public utils.video.input.VidInputter.SourceType getType() {
		return SourceType.FILE;
	}
	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#stopModule()
	 */
	@Override
	public void stopModule() {
		stop_stream = true;
		vidLib.stop();
	}
	
	@Override
	public void pauseStream() {
		paused=true;
		vidLib.pause();
	}

	@Override
	public void resumeStream() {
		paused=false;
		vidLib.play();
		th_update_image.interrupt();		
	}

}
