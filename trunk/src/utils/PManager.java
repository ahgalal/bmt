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

import modules.ModulesManager;
import modules.experiment.ExcelEngine;
import modules.zones.ShapeController;

import org.eclipse.swt.widgets.Display;

import utils.Logger.Details;
import utils.PManager.ProgramState.GeneralState;
import utils.PManager.ProgramState.StreamState;
import utils.StatusManager.StatusSeverity;
import utils.video.VideoManager;
import control.ui.CtrlAbout;
import control.ui.CtrlCamOptions;
import control.ui.CtrlDrawZones;
import control.ui.CtrlMainGUI;
import control.ui.CtrlOptionsWindow;
import control.ui.CtrlRatInfoForm;
import filters.CommonFilterConfigs;
import gfx_panel.GfxPanel;

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
			//log.print("General state: " + general, this);
			this.general = general;
		}

		public void setStream(final StreamState stream) {
			//log.print("Stream state: " + stream, this);
			this.stream = stream;
		}

		@Override
		public String toString() {
			return "General: " + general.name() + ", Stream: " + stream.name();
		}
	}

	private static PManager		default_me;

	/**
	 * Logger instance, used to log messages to the console.
	 */
	public static Logger		log;
	/**
	 * MainGUI static instance, Main GUI screen of the program.
	 */
	public static CtrlMainGUI	main_gui;
	public static boolean		testingMode;

	/**
	 * Gets the Singleton object.
	 * 
	 * @return Static PManager instance
	 */
	public static PManager getDefault() {
		return default_me;
	}

	/**
	 * @param args
	 *            Main arguments
	 */
	public static void main(final String[] args) {
		new PManager();
		final Display display = Display.getDefault();

		while (!main_gui.isShellDisposed())
			if (!display.readAndDispatch())
				display.sleep();

		// display.dispose();
	}

	/**
	 * About Dialog box instance, displays credits of this software.
	 */
	public CtrlAbout						about;
	private final ArrayList<StateListener>	arrStateListsners	= new ArrayList<StateListener>();
	/**
	 * CamOptions GUI instance, used to change camera options.
	 */
	public CtrlCamOptions					cam_options;
	/**
	 * DrawZones GUI instance.
	 */
	public CtrlDrawZones					drw_zns;
	/**
	 * Excel engine instance, used to write data to excel sheets.
	 */
	public ExcelEngine						excel_engine;
	/**
	 * Rat form, used to enter next rat number/group.
	 */
	public CtrlRatInfoForm					frm_rat;
	/**
	 * Options window, used to alter filters/modules options.
	 */
	public CtrlOptionsWindow				options_window;
	/**
	 * ShapeController instance, used to handle the graphical representation of
	 * zones.
	 */
	public final ShapeController			shape_controller;

	/**
	 * Program's state, check the documentation of ProgramState enumeration.
	 */
	private final ProgramState				state;

	/**
	 * Status manager instance, manages the status label at the bottom of the
	 * MainGUI.
	 */
	public StatusManager					statusMgr;

	private final VideoManager				vidMgr;

	/**
	 * Initializes GUI controllers, Model Controllers and Video Controller.
	 */
	public PManager() {
		state = new ProgramState(StreamState.IDLE, GeneralState.IDLE);
		excel_engine = new ExcelEngine();
		default_me = this;
		statusMgr = new StatusManager();

		shape_controller = ShapeController.getDefault();
		drw_zns = new CtrlDrawZones();
		frm_rat = new CtrlRatInfoForm();
		cam_options = new CtrlCamOptions();
		options_window = new CtrlOptionsWindow();
		log = new Logger(Details.VERBOSE);

		main_gui = new CtrlMainGUI();
		new ModulesManager();
		addStateListener(main_gui);

		main_gui.show(true);

		vidMgr = new VideoManager();

		// State watcher, when state changes, it notified all StateListsners
		final Thread thStateChangedNotifier = new Thread(new Runnable() {
			ProgramState	old_state	= new ProgramState(StreamState.IDLE,
												GeneralState.LAUNCHING);

			@Override
			public void run() {
				while (main_gui.isUIOpened()) {
					if ((old_state.getGeneral() != getState().getGeneral())
							|| (old_state.getStream() != getState().getStream())) {
						notifyStateListeners();
						old_state.setGeneral(getState().getGeneral());
						old_state.setStream(getState().getStream());
						log.print("State is: " + getState(), this);
					}
					try {
						Thread.sleep(100);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thStateChangedNotifier.start();

		main_gui.setActive();
	}

	public void addStateListener(final StateListener sListener) {
		for(StateListener sl:arrStateListsners)
			if(sl==sListener){
				log.print("Tried to add State Listener "+sl+" more than once", this, StatusSeverity.WARNING);
				return;
			}
		arrStateListsners.add(sListener);
	}

	public ProgramState getState() {
		return state;
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
	 * @param common_configs
	 *            CommonFilterConfigs object needed by most filters
	 * @param vidFile
	 *            video file to load (if not using webcam as the streaming
	 *            source)
	 */
	public void initializeVideoManager(
			final CommonFilterConfigs common_configs, final String vidFile) {
		vidMgr.initialize(common_configs, vidFile);
	}

	/**
	 * Links Gfx_panel with ShapeController, gives the gfx_panel instance to the
	 * shape_controller instance.
	 * 
	 * @param gfx_panel
	 *            GfxPanel object to send to the shape controller
	 */
	public void linkGFXPanelWithShapeCtrlr(final GfxPanel gfx_panel) {
		shape_controller.linkWithGFXPanel(gfx_panel);
	}

	private void notifyStateListeners() {
		for (final StateListener sl : arrStateListsners)
			sl.updateProgramState(getState());
	}

	public void pauseResume() {
		switch (getState().getStream()) {
			case STREAMING:
				ModulesManager.getDefault().pauseModules();
				getVideoManager().pauseStream();
				getState().setStream(StreamState.PAUSED);
				break;
			case PAUSED:
				ModulesManager.getDefault().resumeModules();
				getVideoManager().resumeStream();
				getState().setStream(StreamState.STREAMING);
				break;
			default:
				break;
		}
	}

	public void removeStateListener(final StateListener sListener) {
		arrStateListsners.remove(sListener);
	}

	public void signalProgramStateUpdate() {
		notifyStateListeners();
	}

	public void startStreaming() {
		if ((getState().getGeneral() == GeneralState.IDLE)
				&& vidMgr.isInitialized())
			// ModulesManager.getDefault().setupModules(forcedSwimmingModulesSetup);
			vidMgr.startStreaming();
		else
			statusMgr
					.setStatus(
							"State is not idle or no video source selected, not able to start streaming",
							StatusSeverity.ERROR);
	}

	public boolean startTracking() {
		if (getState().getStream() == StreamState.STREAMING ||
				getState().getStream() == StreamState.PAUSED) {
			ModulesManager.getDefault().initialize();
			vidMgr.startProcessing();
			ModulesManager.getDefault().runModules(true);
			return true;
		} else {
			statusMgr.setStatus("Please start the camera first.",
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
			if (vidMgr != null){
				if(getState().getGeneral() != GeneralState.TRACKING) {
					vidMgr.unloadLibrary();
					getState().setStream(StreamState.IDLE);
					statusMgr.setStatus("Streaming is Stopped!", StatusSeverity.WARNING);
				} else
					statusMgr.setStatus(
							"Streaming Cannot be stopped while Tracking is running.",
							StatusSeverity.ERROR);
			}else
				statusMgr.setStatus(
						"incorrect state, unable to unload video library",
						StatusSeverity.ERROR);
		}else
			statusMgr.setStatus(
					"incorrect state, was not in streaming/paused state",
					StatusSeverity.ERROR);
	}

	public void stopTracking() {
		if (getState().getGeneral() == GeneralState.TRACKING) {
			ModulesManager.getDefault().runModules(false);
			vidMgr.stopProcessing();
			getState().setGeneral(GeneralState.IDLE);
		} else
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					statusMgr.setStatus("Tracking is not running.",
							StatusSeverity.ERROR);
				}
			});
	}

	public void unloadGUI() {
		drw_zns.unloadGUI();
		frm_rat.unloadGUI();
		cam_options.unloadGUI();
		options_window.unloadGUI();
	}
}
