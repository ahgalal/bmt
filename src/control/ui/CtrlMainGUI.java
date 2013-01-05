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

package control.ui;

import java.io.File;
import java.util.ArrayList;

import modules.ExperimentManager;
import modules.Module;
import modules.ModulesManager;
import modules.experiment.Constants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import ui.MainGUI;
import ui.PluggedGUI;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.PManager.ProgramState.GeneralState;
import utils.PManager.ProgramState.StreamState;
import utils.StateListener;
import utils.StatusManager.StatusSeverity;
import utils.Utils;
import filters.CommonFilterConfigs;
import filters.screendrawer.ScreenDrawerConfigs;

/**
 * Controller of the MainGUI window.
 * 
 * @author Creative
 */
public class CtrlMainGUI extends ControllerUI<MainGUI> implements StateListener {
	/**
	 * Updates the MainGUI with the latest counters' values got from
	 * StatsController.
	 * 
	 * @author Creative
	 */
	private class RunnableUpdateGUI implements Runnable {
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			final int streamLength = pm.getVideoManager().getStreamLength();
			setTableNamesColumn();
			while (pm.getState().getGeneral() == GeneralState.TRACKING) {
				Utils.sleep(200);
				if (pm.getState().getStream() == StreamState.STREAMING)
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							if (!ui.getShell().isDisposed()) {
								ui.fillDataTable(null, ModulesManager
										.getDefault().getGUIData());

								// update stream position
								int streamPosition = pm.getVideoManager()
										.getStreamPosition();
								ui.setStreamProgress(streamPosition,
										streamLength);
							}
						}
					});
			}
			thUpdateGui = null;
		}
	}

	private final CtrlAbout					ctrlAboutBox;
	private final CtrlNewExperimentWizard	ctrlNewExpWizard;
	private String							fileName	= "";
	private final PManager					pm;				// @jve:decl-index=0:
	private Thread							thUpdateGui;		// @jve:decl-index=0:
	private final MainGUI					ui;
	private boolean							uiOpened;

	private final Shell						uiShell;

	/**
	 * Initializes class attributes
	 */
	public CtrlMainGUI() {
		pm = PManager.getDefault();
		ui = new MainGUI();
		uiShell = ui.getShell();
		ui.setController(this);
		thUpdateGui = new Thread(new RunnableUpdateGUI(),"Update GUI");

		pm.getStatusMgr().initialize(ui.getConsoleText());
		ctrlAboutBox = new CtrlAbout();
		ctrlNewExpWizard = new CtrlNewExperimentWizard();
	}

	/**
	 * Clears the GUI data.
	 */
	public void clearForm() {
		ui.clearForm();
	}

	/**
	 * Stops all Program Activity and closes the GUI window.
	 */
	public void closeProgram() {
		if (pm.getState().getGeneral() == GeneralState.TRACKING)
			pm.stopTracking();
		if (pm.getState().getStream() == StreamState.STREAMING)
			pm.stopStreaming();
		uiOpened = false;
		try {
			Thread.sleep(510);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		ui.closeWindow();
		pm.unloadGUI();
		// System.exit(0);
	}

	/**
	 * Configures the ScreenDrawer filter and sets its two screens to the AWT
	 * screens in this form.
	 * 
	 * @param name
	 *            name of the ScreenDrawer filter
	 * @param configs
	 *            common configurations instance
	 * @param enableSecScreen
	 *            whether to enable secondary screen
	 */
	public void configureScreenDrawerFilter(final String name,
			final CommonFilterConfigs configs, final boolean enableSecScreen) {
		pm.getVideoManager()
				.getFilterManager()
				.applyConfigsToFilter(
						new ScreenDrawerConfigs(name, ui.getAwtVideoMain()
								.getGraphics(), ui.getAwtVideoSec()
								.getGraphics(), null, true, pm
								.getShapeController()));
	}

	/**
	 * is the Shell disposed/unloaded?.
	 * 
	 * @return true/false
	 */
	public boolean isShellDisposed() {
		return uiShell.isDisposed();
	}

	/**
	 * @return the uiOpened
	 */
	public boolean isUIOpened() {
		return uiOpened;
	}

	/**
	 * Handles all key events of the GUI.
	 * 
	 * @param key
	 *            the pressed key on the keyboard
	 */
	public void keyPressedAction(final char key) {
		/*
		 * // manual rearing: if(pm.state==ProgramState.TRACKING |
		 * pm.state==ProgramState.RECORDING)
		 * if(key==java.awt.event.KeyEvent.VK_R)
		 * stats_controller.incrementRearingCounter(); else
		 * if(key==java.awt.event.KeyEvent.VK_C)
		 * stats_controller.decrementRearingCounter();
		 */
	}

	/**
	 * Loads GUI instances for the available video filters.
	 * 
	 * @param filters
	 *            ArrayList of available filters
	 */
	@SuppressWarnings("rawtypes")
	public void loadPluggedGUI(final PluggedGUI[] pGUI) {
		ui.loadPluggedGUI(pGUI);
		for (final PluggedGUI pgui : pGUI)
			pm.addStateListener(pgui);
	}

	/**
	 * Handles the "Edit Experiment" menu item click action.
	 */
	public void mnuitmEditExpAction() {
		ctrlNewExpWizard.show(uiShell, false);
	}

	/**
	 * Handles the camera options menu item click action.
	 */
	public void mnutmCameraOptionsAction() {
		pm.getCamOptions().show(true);
	}

	/**
	 * Starts the Streaming process, by initializing the VideoManager.
	 */
	public void mnutmCameraStartAction() {
		if (pm.getState().getGeneral() == GeneralState.IDLE) {
			final CommonFilterConfigs commonConfigs = new CommonFilterConfigs(
					-1, -1, -1, -1, "Cam", null);
			pm.initializeVideoManager(commonConfigs, null);
			configureScreenDrawerFilter("ScreenDrawer", null, true);
			pm.getStatusMgr().setStatus("Camera is Starting..",
					StatusSeverity.WARNING);
		} else
			pm.getStatusMgr().setStatus("Camera is already started.",
					StatusSeverity.ERROR);

	}

	/**
	 * Handles the "Edit options" menu item click action.
	 */
	public void mnutmEditOptionsAction() {
		pm.getOptionsWindow().show(true);
	}

	/**
	 * Shows a save file dialog to save the exported Excel data to that file.
	 */
	public void mnutmExperimentExportToExcelAction() {
		final FileDialog fileDialog = new FileDialog(ui.getShell(), SWT.SAVE);
		fileDialog.setFilterExtensions(new String[] { "*.xlsx" });
		final String fileName = fileDialog.open();
		if (fileName != null)
			ExperimentManager.getDefault().writeToExcelFile(fileName);
	}

	/**
	 * Loads an experiment from file: Shows Open Dialog box, Unloads the
	 * previous experiment and loads the new experiment from the selected file.
	 * 
	 * @param sShell
	 *            parent shell for the open dialogbox
	 */
	public void mnutmExperimentLoadexpAction(final Shell sShell) {
		final FileDialog fileDialog = new FileDialog(sShell, SWT.OPEN);
		final String fileName = fileDialog.open();
		if (fileName != null) {
			PManager.mainGUI.clearForm();
			ExperimentManager.getDefault().unloadExperiment();
			ExperimentManager.getDefault().loadExperiment(
					ExperimentManager.readExperimentFromFile(fileName));
			PManager.getDefault()
					.getStatusMgr()
					.setStatus(
							"Experiment is Loaded Successfully from file: "
									+ fileName, StatusSeverity.WARNING);
		}
	}

	/**
	 * Shows the new ExperimentForm and unloads the previous experiment.
	 */
	public void mnutmExperimentNewExpAction() {
		PManager.mainGUI.clearForm();
		ExperimentManager.getDefault().unloadExperiment();
		ctrlNewExpWizard.show(uiShell, true);
	}

	/**
	 * Handles the "About" menu item click action.
	 */
	public void mnutmHelpAboutAction() {
		ctrlAboutBox.show(true);
	}

	public void pauseResumeAction() {
		pm.pauseResume();
	}

	public void setActive() {
		ui.setActive();
	}

	public void setExperimantLoaded(final boolean b) {
		ui.setExperimantLoaded(b);
	}

	/**
	 * Fills the column of displaying parameters in the table.
	 */
	private void setTableNamesColumn() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				ui.fillDataTable(ModulesManager.getDefault().getGUINames(),
						null);
			}
		});
	}

	@Override
	public boolean setVars(final String[] strs) {
		return true;
	}

	public void setVideoFileMode() {
		final FileDialog fileDialog = new FileDialog(ui.getShell(), SWT.OPEN);
		if ((fileName == null) || (new File(fileName).exists() == false))
			fileName = fileDialog.open();
		if (fileName != null)
			if (pm.getState().getGeneral() == GeneralState.IDLE) {
				final CommonFilterConfigs commonConfigs = new CommonFilterConfigs(
						-1, -1, -1, -1, "VideoFile", null);
				pm.initializeVideoManager(commonConfigs, fileName);
				configureScreenDrawerFilter("ScreenDrawer", commonConfigs, true);
				pm.getStatusMgr().setStatus(
						"Stream is started from file: " + fileName,
						StatusSeverity.WARNING);
			} else
				pm.getStatusMgr().setStatus("Stream is already started.",
						StatusSeverity.ERROR);
		fileName = null;
	}

	@Override
	public void show(final boolean visibility) {
		ui.show(visibility);
		uiOpened = true;

		final Thread thUpdateControlsEnable = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!uiShell.isDisposed()) {
					if (ExperimentManager.getDefault().isExperimentPresent()
							&& (pm.getState().getStream() == StreamState.IDLE))
						ui.btnStartStreamingEnable(true);
					else
						ui.btnStartStreamingEnable(false);
					if (ModulesManager.getDefault().allowTracking()
							&& ((pm.getState().getStream() == StreamState.STREAMING) || (pm
									.getState().getStream() == StreamState.PAUSED))
							&& (pm.getState().getGeneral() != GeneralState.TRACKING))
						ui.btnStartTrackingEnable(true);
					else
						ui.btnStartTrackingEnable(false);
					try {
						Thread.sleep(100);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		thUpdateControlsEnable.start();

	}

	public void startStreamingAction() {
		if (ui.getSelectedInputMode().equals("CAM"))
			mnutmCameraStartAction();
		else if (ui.getSelectedInputMode().equals("VIDEOFILE"))
			setVideoFileMode();
		pm.startStreaming();
	}

	/**
	 * Starts Tracking: starts StartsController session , the thread
	 * "runnableUpdateGUI" and the VideoManager Processing session.
	 */
	private void startTracking() {
		if (pm.startTracking()) {
			clearForm();
			if (thUpdateGui == null)
				thUpdateGui = new Thread(new RunnableUpdateGUI());
			thUpdateGui.start();
		}
	}

	/**
	 * Shows the rat information window to enter the next rat number & group.
	 */
	public void startTrackingAction() {
		final Thread thStartGUIProcedures = new Thread(new Runnable() {

			private void handleNoExperimentModule(final String msg) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						final MessageBox mbox = new MessageBox(ui.getShell(),
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						mbox.setMessage(msg + ", do you want to continue?");
						mbox.setText("Continue?");
						final int res = mbox.open();
						if (res == SWT.YES)
							startTracking();
					}
				});
			}

			@Override
			public void run() {
				if ((pm.getState().getStream() == StreamState.STREAMING)
						|| (pm.getState().getStream() == StreamState.PAUSED)) {
					if (ModulesManager.getDefault().areModulesReady(
							ui.getShell())) {
						final ArrayList<Module<?,?,?>> experimentModulesAvailable = ModulesManager
								.getDefault().getModulesUnderID(
										Constants.EXPERIMENT_ID);
						if (experimentModulesAvailable.size() == 0)
							handleNoExperimentModule("No Experiment Module found");
						else if (experimentModulesAvailable.size() > 1)
							handleNoExperimentModule("More than one experiment module found, there should be only one loaded");
						else
							startTracking();
					} else
						pm.getStatusMgr().setStatus("Tracking is cancelled",
								StatusSeverity.WARNING);

				} else
					pm.getStatusMgr()
							.setStatus(
									"Please make sure the camera is running, you have set the background.",
									StatusSeverity.ERROR);
			}
		});
		thStartGUIProcedures.start();
	}

	public void stopStreamingAction() {
		pm.stopStreaming();
	}

	/**
	 * Stops Tracking: Ends the session of StatsController and saves the rat
	 * information to file(through the InfoController). if Recording,it stops
	 * and saves the video file.
	 */
	public void stopTrackingAction() {
		pm.stopTracking();
	}

	@Override
	public void updateProgramState(final ProgramState state) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				switch (state.getGeneral()) {
					case IDLE:
						ui.btnStopTrackingEnable(false);
						// ui.btnStartStreamingEnable(true);
						// ui.btnStopStreamingEnable(false);
						ui.btnStartTrackingEnable(false);
						// ui.btnPauseStreamingEnable(false);
						break;

					case TRACKING:
						ui.btnStartTrackingEnable(false);
						ui.btnStopTrackingEnable(true);
						// ui.btnPauseStreamingEnable(true);
						// ui.btnStartStreamingEnable(false);
						ui.btnStopStreamingEnable(false);
						break;
				}
				switch (state.getStream()) {
					case IDLE:
						ui.btnStartStreamingEnable(true);
						ui.btnStopStreamingEnable(false);
						ui.btnPauseStreamingEnable(false);
						break;
					case STREAMING:
						// ui.btnStopTrackingEnable(false);
						ui.btnStartStreamingEnable(false);
						ui.btnStopStreamingEnable(true);
						ui.btnPauseStreamingEnable(true);
						break;
					case PAUSED:
						// ui.btnStopTrackingEnable(false);
						ui.btnStartStreamingEnable(false);
						ui.btnStopStreamingEnable(true);
						ui.btnPauseStreamingEnable(true);
						break;
					default:
						break;
				}
			}
		});
	}

}
