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

package utils;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;

import sys.utils.Utils;
import utils.Logger.Details;
import utils.PManager.ProgramState.GeneralState;
import utils.PManager.ProgramState.StreamState;
import utils.StatusManager.StatusSeverity;
import utils.video.VideoManager;
import control.ui.CtrlAbout;
import control.ui.CtrlCamOptions;
import control.ui.CtrlMainGUI;
import control.ui.CtrlOptionsWindow;
import control.ui.CtrlRatInfoForm;
import filters.CommonFilterConfigs;

/**
 * Program Manager, contains the main function, creates GUI and Controllers.
 * 
 * @author Creative
 */
public class PManager {
	public static class ProgramState {
		/**
		 * Defines program states.
		 * 
		 * @author Creative
		 */
		public enum GeneralState {
			IDLE, /**
			 * IDLE: doing nothing, STREAMING: displaying video frames on
			 * the screen, TRACKING: tracking the object: STREAMING + TRACKING,
			 * video: STREAMING + TRACKING.
			 */
			LAUNCHING, TRACKING;
		}

		public enum StreamState {
			DISABLED, IDLE, PAUSED, STREAMING;
		}

		private GeneralState	general;

		private StreamState		stream;

		public ProgramState(final StreamState stream, final GeneralState general) {
			super();
			this.stream = stream;
			this.general = general;
		}

		public GeneralState getGeneral() {
			return general;
		}

		public StreamState getStream() {
			return stream;
		}

		public void setGeneral(final GeneralState general) {
			// log.print("General state: " + general, this);
			this.general = general;
		}

		public void setStream(final StreamState stream) {
			// log.print("Stream state: " + stream, this);
			this.stream = stream;
		}

		@Override
		public String toString() {
			return "General: " + general.name() + ", Stream: " + stream.name();
		}
	}

	private static PManager		defaultInstance;

	/**
	 * Logger instance, used to log messages to the console.
	 */
	public static Logger		log;
	/**
	 * MainGUI static instance, Main GUI screen of the program.
	 */
	public static CtrlMainGUI	mainGUI;
	public static boolean		testingMode;

	/**
	 * Gets the Singleton object.
	 * 
	 * @return Static PManager instance
	 */
	public static PManager getDefault() {
		return defaultInstance;
	}

	public static String getOS() {
		final String os = System.getProperty("os.name");
		if (os.contains("Linux"))
			return "Linux";
		else if (os.contains("Windows"))
			return "Windows";

		System.out.print("Unknown OS\n");
		return null;
	}

	/**
	 * @param args
	 *            Main arguments
	 */
	public static void main(final String[] args) {
		if (mainGUI == null) { // for handling successive tests in a suite
			// (to prevent duplicating GUI + Invalid thread access)
			new PManager();
			final Display display = Display.getDefault();

			while (!mainGUI.isShellDisposed())
				if (!display.readAndDispatch())
					display.sleep();
		}
	}

	/**
	 * About Dialog box instance, displays credits of this software.
	 */
	private CtrlAbout						about;
	private boolean							arrStateListenersLock	= false;
	private final ArrayList<StateListener>	arrStateListsners		= new ArrayList<StateListener>();

	/**
	 * CamOptions GUI instance, used to change camera options.
	 */
	private final CtrlCamOptions			camOptions;
	private int								displayExecRequests		= 0;

	/**
	 * Excel engine instance, used to write data to excel sheets.
	 */

	/**
	 * Rat form, used to enter next rat number/group.
	 */
	private final CtrlRatInfoForm			frmRat;

	/**
	 * Options window, used to alter filters/modules options.
	 */
	private final CtrlOptionsWindow			optionsWindow;

	/**
	 * Program's state, check the documentation of ProgramState enumeration.
	 */
	private final ProgramState				state;

	/**
	 * Status manager instance, manages the status label at the bottom of the
	 * MainGUI.
	 */
	private final StatusManager				statusMgr;

	private final VideoManager				vidMgr;

	/**
	 * Initializes GUI controllers, Model Controllers and Video Controller.
	 */
	public PManager() {
		state = new ProgramState(StreamState.IDLE, GeneralState.IDLE);

		defaultInstance = this;
		this.statusMgr = new StatusManager();

		this.frmRat = new CtrlRatInfoForm();
		this.camOptions = new CtrlCamOptions();
		this.optionsWindow = new CtrlOptionsWindow();
		log = new Logger(Details.VERBOSE);

		mainGUI = new CtrlMainGUI();
		
		addStateListener(mainGUI);

		mainGUI.show(true);

		vidMgr = new VideoManager();

		// State watcher, when state changes, it notified all StateListsners
		final Thread thStateChangedNotifier = new Thread(new Runnable() {
			ProgramState	oldState	= new ProgramState(StreamState.IDLE,
												GeneralState.LAUNCHING);

			@Override
			public void run() {
				while (mainGUI.isUIOpened()) {
					if ((oldState.getGeneral() != getState().getGeneral())
							|| (oldState.getStream() != getState().getStream())) {
						notifyStateListeners();
						oldState.setGeneral(getState().getGeneral());
						oldState.setStream(getState().getStream());
						log.print("State is: " + getState(), this);
					}
					Utils.sleep(100);
				}
			}
		}, "StateChangeNotifier");
		thStateChangedNotifier.start();

		mainGUI.setActive();
	}

	private void acquireLockArrStateListeners() {
		while (arrStateListenersLock) {
			/*
			 * we read and dispatch to allow Display.(a)syncExec() calls from
			 * other threads to be executed in case this method
			 * "acquireLockArrStateListeners" is called form SWT thread
			 */
			Display.getDefault().readAndDispatch();
		}
		arrStateListenersLock = true;
	}

	public void addStateListener(final StateListener sListener) {
		for (final StateListener sl : arrStateListsners)
			if (sl == sListener) {
				log.print("Tried to add State Listener " + sl
						+ " more than once", this, StatusSeverity.WARNING);
				return;
			}
		arrStateListsners.add(sListener);
	}

	public void closeProgram() {
		if (getState().getGeneral() == GeneralState.TRACKING)
			stopTracking();
		if ((getState().getStream() == StreamState.STREAMING)
				|| (getState().getStream() == StreamState.PAUSED))
			stopStreaming();

		waitForDisplayExec();

		mainGUI.setUiOpened(false);

		mainGUI.closeWindow();
		unloadGUI();
	}

	/**
	 * It is recommended to use this method instead of Display.asyncExec() to
	 * assure correct thread termination.
	 * 
	 * @param runnable
	 */
	public void displayAsyncExec(final Runnable runnable) {
		displayExecRequests++;
		Display.getDefault().asyncExec(runnable);
		displayExecRequests--;
	}

	/**
	 * It is recommended to use this method instead of Display.syncExec() to
	 * assure correct thread termination.
	 * 
	 * @param runnable
	 */
	public void displaySyncExec(final Runnable runnable) {
		displayExecRequests++;
		Display.getDefault().syncExec(runnable);
		displayExecRequests--;
	}

	public CtrlAbout getAbout() {
		return about;
	}

	public CtrlCamOptions getCamOptions() {
		return camOptions;
	}

	public CtrlRatInfoForm getFrmRat() {
		return frmRat;
	}

	public CtrlOptionsWindow getOptionsWindow() {
		return optionsWindow;
	}

	public ProgramState getState() {
		return state;
	}

	public StatusManager getStatusMgr() {
		return statusMgr;
	}

	/**
	 * Gets the VideoManager instance.
	 * 
	 * @return VideoManager instance
	 */
	public VideoManager getVideoManager() {
		return vidMgr;
	}

	/**
	 * Initializes the Video Processor.
	 * 
	 * @param commonConfigs
	 *            CommonFilterConfigs object needed by most filters
	 * @param vidFile
	 *            video file to load (if not using webcam as the streaming
	 *            source)
	 */
	public void initializeVideoManager(final CommonFilterConfigs commonConfigs,
			final String vidFile) {
		vidMgr.initialize(commonConfigs, vidFile);
	}

	private void notifyStateListeners() {
		acquireLockArrStateListeners();
		for (final StateListener sl : arrStateListsners)
			sl.updateProgramState(getState());
		releaseLockArrStateListeners();
	}

	public void pauseResume() {
		switch (getState().getStream()) {
			case STREAMING:
				getVideoManager().pauseStream();
				getState().setStream(StreamState.PAUSED);
				getStatusMgr().setStatus("PAUSED", StatusSeverity.WARNING);
				break;
			case PAUSED:
				getVideoManager().resumeStream();
				getState().setStream(StreamState.STREAMING);
				getStatusMgr().setStatus("RESUMED", StatusSeverity.WARNING);
				break;
			default:
				break;
		}
	}

	private void releaseLockArrStateListeners() {
		arrStateListenersLock = false;
	}

	public void removeStateListener(final StateListener sListener) {
		acquireLockArrStateListeners();
		arrStateListsners.remove(sListener);
		releaseLockArrStateListeners();
	}

	public void signalProgramStateUpdate() {
		notifyStateListeners();
	}

	public boolean startStreaming() {
		if ((getState().getGeneral() == GeneralState.IDLE)
				&& vidMgr.isInitialized())
			return vidMgr.startStreaming();
		else
			getStatusMgr()
					.setStatus(
							"State is not idle or no video source selected, not able to start streaming",
							StatusSeverity.ERROR);
		return false;
	}

	public boolean startTracking() {
		if ((getState().getStream() == StreamState.STREAMING)
				|| (getState().getStream() == StreamState.PAUSED)) {
			vidMgr.startProcessing();
			getStatusMgr().setStatus("Tracking is started",
					StatusSeverity.WARNING);
			return true;
		} else {
			getStatusMgr().setStatus("Please start the camera first.",
					StatusSeverity.ERROR);
			return false;
		}
	}

	/**
	 * Unloads the Video Processor, used when switching video libraries..
	 */
	public void stopStreaming() {
		if ((getState().getStream() == StreamState.STREAMING)
				|| (getState().getStream() == StreamState.PAUSED)) {
			if (vidMgr != null) {
				if (getState().getGeneral() != GeneralState.TRACKING) {
					vidMgr.unloadLibrary();
					getState().setStream(StreamState.IDLE);
					getStatusMgr().setStatus("Streaming is stopped!",
							StatusSeverity.WARNING);
				} else
					getStatusMgr()
							.setStatus(
									"Streaming Cannot be stopped while Tracking is running.",
									StatusSeverity.ERROR);
			} else
				getStatusMgr().setStatus(
						"incorrect state, unable to unload video library",
						StatusSeverity.ERROR);
		} else
			getStatusMgr().setStatus(
					"incorrect state, was not in streaming/paused state",
					StatusSeverity.ERROR);
	}

	public void stopTracking() {
		if (getState().getGeneral() == GeneralState.TRACKING) {
			vidMgr.stopProcessing();
			getState().setGeneral(GeneralState.IDLE);
			getStatusMgr().setStatus("Tracking is stopped",
					StatusSeverity.WARNING);
		} else
			getStatusMgr().setStatus("Tracking is not running.",
					StatusSeverity.ERROR);
	}

	private void unloadGUI() {
		getFrmRat().unloadGUI();
		getCamOptions().unloadGUI();
		getOptionsWindow().unloadGUI();
	}

	/**
	 * Waits till all Display Exec calls are executed, so that we are sure none
	 * of the program threads is stuck waiting for the Display Exec while the
	 * display is disposed (we are exiting the program).
	 */
	private void waitForDisplayExec() {
		boolean confirmNoRequests = false;
		while ((displayExecRequests > 0) || (confirmNoRequests == false)) {
			Display.getDefault().readAndDispatch();
			if ((confirmNoRequests == false) && (displayExecRequests == 0)) {
				Utils.sleep(100);
				confirmNoRequests = true;
			} else if ((confirmNoRequests == true) && (displayExecRequests > 0)) {
				confirmNoRequests = false;
			}
		}
	}

	public void updateCommonConfigs(CommonFilterConfigs commonConfigs) {
		vidMgr.updateCommonConfigs(commonConfigs);
	}
}
