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

import gfx_panel.GfxPanel;

import java.util.ArrayList;

import modules.ModulesManager;
import modules.ModulesSetup;
import modules.experiment.ExcelEngine;
import modules.zones.ShapeController;

import org.eclipse.swt.widgets.Display;

import utils.Logger.Details;
import utils.StatusManager.StatusSeverity;
import utils.video.VideoManager;
import utils.video.filters.CommonFilterConfigs;
import control.ui.CtrlAbout;
import control.ui.CtrlCamOptions;
import control.ui.CtrlDrawZones;
import control.ui.CtrlExperimentForm;
import control.ui.CtrlGroupsForm;
import control.ui.CtrlMainGUI;
import control.ui.CtrlOptionsWindow;
import control.ui.CtrlRatInfoForm;

/**
 * Program Manager, contains the main function, creates GUI and Controllers.
 * 
 * @author Creative
 */
public class PManager
{

	private final ArrayList<StateListener> arrStateListsners = new ArrayList<StateListener>();

	/**
	 * Defines program states.
	 * 
	 * @author Creative
	 */
	public enum ProgramState
	{
		/**
		 * IDLE: doing nothing, STREAMING: displaying video frames on the
		 * screen, TRACKING: tracking the object: STREAMING + TRACKING, video:
		 * STREAMING + TRACKING.
		 */
		LAUNCHING, IDLE, STREAMING, TRACKING;
	}

	private static PManager default_me;
	/**
	 * MainGUI static instance, Main GUI screen of the program.
	 */
	public static CtrlMainGUI main_gui;
	/**
	 * CamOptions GUI instance, used to change camera options.
	 */
	public CtrlCamOptions cam_options;
	/**
	 * DrawZones GUI instance.
	 */
	public CtrlDrawZones drw_zns;
	/**
	 * Excel engine instance, used to write data to excel sheets.
	 */
	public ExcelEngine excel_engine;
	/**
	 * Experiment form, used to Add/Edit experiment information.
	 */
	public CtrlExperimentForm frm_exp;
	/**
	 * Groups form, used to Add/Edit groups of rats.
	 */
	public CtrlGroupsForm frm_grps;
	/**
	 * Rat form, used to enter next rat number/group.
	 */
	public CtrlRatInfoForm frm_rat;
	/**
	 * Logger instance, used to log messages to the console.
	 */
	public static Logger log;
	/**
	 * Options window, used to alter filters/modules options.
	 */
	public CtrlOptionsWindow options_window;
	/**
	 * ShapeController instance, used to handle the graphical representation of
	 * zones.
	 */
	public final ShapeController shape_controller;
	/**
	 * Program's state, check the documentation of ProgramState enumeration.
	 */
	public ProgramState state;
	/**
	 * Status manager instance, manages the status label at the bottom of the
	 * MainGUI.
	 */
	public StatusManager statusMgr;
	private final VideoManager vidMgr;
	/**
	 * About Dialog box instance, displays credits of this software.
	 */
	public CtrlAbout about;

	/**
	 * Gets the Singleton object.
	 * 
	 * @return Static PManager instance
	 */
	public static PManager getDefault()
	{
		return default_me;
	}

	public static boolean testingMode;

	/**
	 * @param args
	 *            Main arguments
	 */
	public static void main(final String[] args)
	{
		new PManager();
		final Display display = Display.getDefault();

		if (!testingMode)
			while (!main_gui.isShellDisposed())
			{
				if (!display.readAndDispatch())
					display.sleep();
			}

		// display.dispose();
	}

	/**
	 * Initializes GUI controllers, Model Controllers and Video Controller.
	 */
	public PManager()
	{
		state = ProgramState.IDLE;
		excel_engine = new ExcelEngine();
		default_me = this;
		statusMgr = new StatusManager();

		shape_controller = ShapeController.getDefault();
		drw_zns = new CtrlDrawZones();
		frm_exp = new CtrlExperimentForm();
		frm_grps = new CtrlGroupsForm();
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
			ProgramState old_state = ProgramState.LAUNCHING;

			@Override
			public void run()
			{
				while (main_gui.isUIOpened())
				{
					if (old_state != state)
					{
						notifyStateListeners();
						old_state = state;
					}
					try
					{
						Thread.sleep(30);
					} catch (final InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		});
		thStateChangedNotifier.start();
	}

	/**
	 * Gets the VideoManager instance.
	 * 
	 * @return VideoManager instance
	 */
	public VideoManager getVideoManager()
	{
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
			final CommonFilterConfigs common_configs,
			final String vidFile)
	{
		vidMgr.initialize(common_configs, vidFile);
	}

	public void startStreaming()
	{
		if (state == ProgramState.IDLE && vidMgr.isInitialized())
		{
			//ModulesManager.getDefault().setupModules(forcedSwimmingModulesSetup);
			vidMgr.startStreaming();
		}
		else
			statusMgr.setStatus(
					"State is not idle or no video source selected, not able to start streaming",
					StatusSeverity.ERROR);
	}

	/**
	 * Links Gfx_panel with ShapeController, gives the gfx_panel instance to the
	 * shape_controller instance.
	 * 
	 * @param gfx_panel
	 *            GfxPanel object to send to the shape controller
	 */
	public void linkGFXPanelWithShapeCtrlr(final GfxPanel gfx_panel)
	{
		shape_controller.linkWithGFXPanel(gfx_panel);
	}

	/**
	 * Unloads the Video Processor, used when switching video libraries..
	 */
	public void stopStreaming()
	{
		if (vidMgr != null & state != ProgramState.IDLE)
			vidMgr.unloadLibrary();
		else
			statusMgr.setStatus(
					"incorrect state, unable to unload video library",
					StatusSeverity.ERROR);
	}

	public void addStateListener(final StateListener sListener)
	{
		arrStateListsners.add(sListener);
	}

	public void removeStateListener(final StateListener sListener)
	{
		arrStateListsners.remove(sListener);
	}

	private void notifyStateListeners()
	{
		for (final StateListener sl : arrStateListsners)
			sl.updateProgramState(state);
	}

	public boolean startTracking()
	{
		if (state == ProgramState.STREAMING)
		{
			ModulesManager.getDefault().initialize();
			vidMgr.startProcessing();
			ModulesManager.getDefault().runModules(true);
			return true;
		}
		else
		{
			statusMgr.setStatus(
					"Please start the camera first.",
					StatusSeverity.ERROR);
			return false;
		}
	}

	public void stopTracking()
	{
		if (state == ProgramState.TRACKING /*|state == ProgramState.RECORDING*/)
		{
			ModulesManager.getDefault().runModules(false);
			vidMgr.stopProcessing();
		}
		else
			statusMgr.setStatus("Tracking is not running.", StatusSeverity.ERROR);
	}

	public void unloadGUI()
	{
		drw_zns.unloadGUI();
		frm_exp.unloadGUI();
		frm_grps.unloadGUI();
		frm_rat.unloadGUI();
		cam_options.unloadGUI();
		options_window.unloadGUI();
	}

}
