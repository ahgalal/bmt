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
import sys.utils.Utils;
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

	private StreamToAVI		aviSaver;
	private boolean			isRecording	= false;

	private final PManager	pm;

	private Thread			frameWriterThread;
	private Object			frameWriterLock;

	private class FrameWriterRunnable implements Runnable {

		@Override
		public void run() {
			while (frameWriterLock != null) {
				synchronized (frameWriterLock) {
					try {
						frameWriterLock.wait();
					} catch (InterruptedException e) {
						// do nothing when interrupted
					}
				}
				if (frameAvailable) { // there is a new frame to save
					frameAvailable = false;

					// write frame
					final int[] imageData = linkIn.getData();
					aviSaver.writeFrame(imageData);
				}

			}
		}
	};

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

	private Boolean	frameAvailable	= false;

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
				aviSaver.initialize("video.avi", VideoFormat.JPG, 1, configs
						.getCommonConfigs().getWidth(), configs
						.getCommonConfigs().getHeight());
				isRecording = true;

				// start frame writer
				frameWriterThread = new Thread(new FrameWriterRunnable(),
						"Record Frame Writer");
				frameWriterLock = new Integer(0);
				frameWriterThread.start();

				configs.setEnabled(true);
				return true;
			} else
				pm.getStatusMgr().setStatus("Please start tracking first",
						StatusSeverity.ERROR);
			return false;
		} else {
			if (isRecording == true) {
				isRecording = false;

				// stop frame writer thread
				frameWriterLock = null; // break the while loop
				frameWriterThread.interrupt(); // wake up from wait
				
				final Thread thStopRecording = new Thread(new Runnable() {
					@Override
					public void run() {
						configs.setEnabled(false);
						Utils.sleep(100);

						aviSaver.close();
						aviSaver = null;
					}
				}, "Stop Recording");
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
			if (frameWriterLock != null)
				synchronized (frameWriterLock) {
					frameAvailable = true;
					// notify the waiting "frame writer thread"
					frameWriterLock.notify();
				}
		}
	}

	/**
	 * Saves the current video with the given file name.
	 * 
	 * @param fileName
	 *            name of the file to save data to
	 */
	public void saveVideoFile(final String fileName) {
		final File tmpFile = new File("video.avi");
		try {
			File dest = new File(fileName);
			if (dest.exists())
				dest.delete();
			if (!tmpFile.renameTo(dest))
				throw new Exception();
		} catch (final Exception e) {
			String newFileName = fileName + "_" + Math.random();
			PManager.log.print("Couldn't rename video file, saving to: "
					+ newFileName, this, StatusSeverity.ERROR);
			try {
				tmpFile.renameTo(new File(newFileName));
			} catch (Exception e1) {
				PManager.log.print("Save Error!", this);
			}
		}
	}

	@Override
	public void updateProgramState(final ProgramState state) {

	}
}
