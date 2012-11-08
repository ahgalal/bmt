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
	private StreamToAVI		avi_saver;

	private boolean			isRecording	= false;
	private final PManager	pm;

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
				avi_saver = new StreamToAVI();
				avi_saver.initialize("video.avi", VideoFormat.JPG, 10,
						configs.common_configs.width,
						configs.common_configs.height);
				isRecording = true;
				configs.enabled = true;
				return true;
			} else
				pm.statusMgr.setStatus("Please start tracking first",
						StatusSeverity.ERROR);
			return false;
		} else {
			final Thread th_stop_recording = new Thread(new Runnable() {
				@Override
				public void run() {
					configs.enabled = false;
					try {
						Thread.sleep(100);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
					if (isRecording == true) {
						avi_saver.close();
						avi_saver = null;
						isRecording = false;
					}
				}
			});
			th_stop_recording.start();
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public void process() {
		if (configs.enabled) {
			final int[] imageData = link_in.getData();
			avi_saver.writeFrame(imageData);
		}
	}

	/**
	 * Renames the current video file to the given name.
	 * 
	 * @param file_name
	 *            new name of the video file
	 */
	public void renameVideoFile(final String file_name) {
		try {
			final File tmp_file = new File("video.avi");
			if (!tmp_file.renameTo(new File(file_name)))
				throw new Exception();
		} catch (final Exception e) {
			PManager.log.print("Couldn't rename video file", this,
					StatusSeverity.ERROR);
		}
	}

	/**
	 * Saves the current video with the given file name.
	 * 
	 * @param fileName
	 *            name of the file to save data to
	 */
	public void saveVideoFile(final String fileName) {
		renameVideoFile(fileName);
	}

	@Override
	public void updateProgramState(final ProgramState state) {
			
	}

}
