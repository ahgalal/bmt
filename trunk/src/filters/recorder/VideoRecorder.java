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

package filters.recorder;

import java.io.File;

import lib_avi.AVIOutputStream.VideoFormat;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.PManager.ProgramState.GeneralState;
import utils.StatusManager.StatusSeverity;
import utils.Utils;
import filters.FilterConfigs;
import filters.FilterData;
import filters.Link;
import filters.VideoFilter;

/**
 * Records video frames from the incoming data stream.
 * 
 * @author Creative
 */
public class VideoRecorder extends VideoFilter<RecorderConfigs, FilterData> {
	private long			accumulativeRecordTime	= 0;

	private StreamToAVI		aviSaver;
	private boolean			isRecording				= false;
	private long			noFrames				= 0;
	private final PManager	pm;
	private long			prevSampleTime			= 0;

	/**
	 * Initializes the filter.
	 * 
	 * @param name
	 *            filter's name
	 * @param linkIn
	 *            input Link for the filter
	 * @param linkOut
	 *            output Link from the filter
	 */
	public VideoRecorder(final String name, final Link linkIn,
			final Link linkOut) {
		super(name, linkIn, linkOut);
		gui = new VideoRecorderGUI(this);
		pm = PManager.getDefault();
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		this.configs = (RecorderConfigs) configs;
		return super.configure(configs);
	}

	@Override
	public boolean enable(final boolean enable) {
		if (enable) {
			if (pm.getState().getGeneral() == GeneralState.TRACKING) {
				aviSaver = new StreamToAVI();
				aviSaver.initialize("video.avi", VideoFormat.JPG, 10,
						configs.getCommonConfigs().getWidth(),
						configs.getCommonConfigs().getHeight());
				isRecording = true;
				configs.setEnabled(true);
				return true;
			} else
				pm.getStatusMgr().setStatus("Please start tracking first",
						StatusSeverity.ERROR);
			return false;
		} else {
			if (isRecording == true) {
				isRecording = false;
				final Thread thStopRecording = new Thread(new Runnable() {
					@Override
					public void run() {
						configs.setEnabled(false);
						Utils.sleep(100);

						final int factor = 3;
						final int timescale = (int) (Math
								.round((30 / factor)
										/ (1000 * noFrames / (double) accumulativeRecordTime)));
						aviSaver.setTimeScale(timescale);
						aviSaver.close();
						aviSaver = null;

						System.out.println("accRecordTime: "
								+ accumulativeRecordTime + " timescale: "
								+ timescale);
						accumulativeRecordTime = 0;
						
						noFrames = 0;
						prevSampleTime = 0;

					}
				},"Stop Recording");
				thStopRecording.start();
			}
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public void process() {
		if (configs.isEnabled()) {

			// calculate video time and number of frames
			final long currentSampleTime = System.currentTimeMillis();
			if (prevSampleTime != 0) {
				final long deltaSamples = currentSampleTime - prevSampleTime;
				accumulativeRecordTime += deltaSamples;
				noFrames++;
			}
			prevSampleTime = currentSampleTime;

			final int[] imageData = linkIn.getData();
			aviSaver.writeFrame(imageData);
		}
	}

	/**
	 * Saves the current video with the given file name.
	 * 
	 * @param fileName
	 *            name of the file to save data to
	 */
	public void saveVideoFile(final String fileName) {
		try {

			final File tmpFile = new File("video.avi");
			if (!tmpFile.renameTo(new File(fileName)))
				throw new Exception();
		} catch (final Exception e) {
			PManager.log.print("Couldn't rename video file", this,
					StatusSeverity.ERROR);
		}
	}

	@Override
	public void updateProgramState(final ProgramState state) {

	}

}
