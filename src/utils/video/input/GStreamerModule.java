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
import java.io.File;
import java.nio.IntBuffer;

import org.gstreamer.Gst;
import org.gstreamer.PlayBin;
import org.gstreamer.State;
import org.gstreamer.elements.RGBDataSink;
import org.gstreamer.elements.RGBDataSink.Listener;

import sys.utils.Utils;
import utils.PManager;
import utils.video.FrameIntArray;
import utils.video.ImageManipulator;

public class GStreamerModule extends VidInputter<VidSourceConfigs> {

	private Point		actualFrameSize;
	private RGBDataSink	dataSink;
	private PlayBin		playBin;

	@Override
	public int displayMoreSettings() {
		return 0;
	}

	@Override
	public Point getFrameSize() {
		return actualFrameSize;
	}

	@Override
	public String getName() {
		return "GStreamer";
	}

	@Override
	public int getNumCams() {
		return 0;
	}

	@Override
	public SourceStatus getStatus() {
		if (dataSink != null) {
			// System.out.println("Duration: "+dataSink.getDuration().longValue()+" Position: "+dataSink.getPosition().longValue());
			if (((playBin.getState() == State.PLAYING) || (playBin.getState() == State.PAUSED))
					&& (dataSink.getDuration().longValue() <= dataSink
							.getPosition().longValue()))
				status = SourceStatus.ERROR;
		}
		return status;
	}

	@Override
	public int getStreamLength() {
		// TODO: test this
		return (int) (dataSink.getDuration().longValue() / 10000);
	}

	@Override
	public int getStreamPosition() {
		// TODO: test this
		return (int) (dataSink.getPosition().longValue() / 10000);
	}

	@Override
	public SourceType getType() {
		return SourceType.FILE;
	}

	@Override
	public boolean initialize(final FrameIntArray frameData,
			final VidSourceConfigs configs) {
		this.configs = configs;
		actualFrameSize = null; // setting it to null, to be explicitly
								// initialized in the sink callback method
								// below
		Gst.init("GSMovie", new String[0]);
		playBin = new PlayBin("PlayerBin");
		PManager.log.print(
				"initializing video file: " + configs.getVideoFilePath(), this);
		playBin.setInputFile(new File(configs.getVideoFilePath()));
		fia = frameData;

		dataSink = new RGBDataSink("rgb", new Listener() {

			@Override
			public void rgbFrame(final int width, final int height,
					final IntBuffer buf) {
				if (paused == false) {
					if (actualFrameSize == null)
						actualFrameSize = new Point();

					actualFrameSize.x = width;
					actualFrameSize.y = height;
					fia.setFrameData(ImageManipulator
							.bgrIntArray2rgbIntArray(buf.array())); // ImageManipulator.byteBGR2IntRGB(buf.getBytes());
					updateStatus();
				}
			}
		});
		playBin.setVideoSink(dataSink);
		return true;
	}

	@Override
	public VidSourceConfigs newConfigurationInstance() {
		return new VidSourceConfigs();
	}

	@Override
	public VidInputter<VidSourceConfigs> newInstance() {
		return new GStreamerModule();
	}

	@Override
	public void pauseStream() {
		super.pauseStream();
		playBin.pause();
	}

	@Override
	public void resumeStream() {
		super.resumeStream();
		playBin.play();
	}

	@Override
	public void setFormat(final String s) {
	}

	@Override
	public boolean startStream() {

		playBin.play();
		while(getStatus()!=SourceStatus.STREAMING)
			Utils.sleep(20);
		return true;
	}

	@Override
	public void stopModule() {
		Utils.sleep(15);
		playBin.stop();
		playBin.dispose();
	}
}
