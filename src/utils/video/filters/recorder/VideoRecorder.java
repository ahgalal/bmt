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

package utils.video.filters.recorder;

import java.io.File;

import lib_avi.AVIOutputStream.VideoFormat;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.FilterConfigs;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;

/**
 * Records video frames from the incoming data stream.
 * 
 * @author Creative
 */
public class VideoRecorder extends VideoFilter
{
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
	public VideoRecorder(final String name, final Link linkIn, final Link linkOut)
	{
		super(name, linkIn, linkOut);
		gui = new VideoRecorderGUI();
		pm = PManager.getDefault();
	}

	private final PManager pm;
	private StreamToAVI avi_saver;
	private RecorderConfigs recorder_configs;
	private boolean isRecording = false;

	@Override
	public boolean configure(final FilterConfigs configs)
	{
		recorder_configs = (RecorderConfigs) configs;
		return super.configure(configs);
	}

	/**
	 * Renames the current video file to the given name.
	 * 
	 * @param file_name
	 *            new name of the video file
	 */
	public void renameVideoFile(final String file_name)
	{
		final File tmp_file = new File("video.avi");
		if (!tmp_file.renameTo(new File(file_name)))
			PManager.log.print("Couldn't rename video file", this, StatusSeverity.ERROR);
	}

	/**
	 * Saves the current video with the given file name.
	 * 
	 * @param fileName
	 *            name of the file to save data to
	 */
	public void saveVideoFile(final String fileName)
	{
		renameVideoFile(fileName);
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public void process()
	{
		if (configs.enabled)
		{
			final int[] imageData = link_in.getData();
			avi_saver.writeFrame(imageData);
		}
	}

	@Override
	public boolean enable(final boolean enable)
	{
		if (enable)
		{
			if (pm.state == ProgramState.TRACKING)
			{
				avi_saver = new StreamToAVI();
				avi_saver.initialize(
						"video.avi",
						VideoFormat.JPG,
						10,
						recorder_configs.common_configs.width,
						recorder_configs.common_configs.height);
				isRecording = true;
				configs.enabled = true;
				return true;
			}
			else
				pm.status_mgr.setStatus(
						"Please start tracking first",
						StatusSeverity.ERROR);
			return false;
		}
		else
		{
			final Thread th_stop_recording = new Thread(new Runnable() {
				@Override
				public void run()
				{
					configs.enabled = false;
					try
					{
						Thread.sleep(100);
					} catch (final InterruptedException e)
					{
						e.printStackTrace();
					}
					if (isRecording == true)
					{
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

	@Override
	public void updateProgramState(final ProgramState state)
	{
		// TODO Auto-generated method stub

	}

}
