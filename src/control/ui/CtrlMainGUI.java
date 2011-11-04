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

import modules.ModulesManager;
import modules.experiment.ExperimentModule;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import ui.MainGUI;
import ui.PluggedGUI;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.StateListener;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;
import utils.video.filters.screendrawer.ScreenDrawerConfigs;
import utils.video.filters.subtractionfilter.SubtractorFilter;
import utils.video.input.VideoFileModule;

/**
 * Controller of the MainGUI window.
 * 
 * @author Creative
 */
public class CtrlMainGUI extends ControllerUI implements StateListener
{
	private final MainGUI ui;
	private final PManager pm; // @jve:decl-index=0:
	private Thread th_update_gui; // @jve:decl-index=0:
	private final Shell ui_shell;
	private boolean ui_is_opened;
	private final CtrlAbout ctrl_about_box;

	/**
	 * @return the ui_is_opened
	 */
	public boolean isUIOpened()
	{
		return ui_is_opened;
	}

	/**
	 * Initializes class attributes (MainGUI,StatsController, PManager and
	 * InfoController).
	 */
	public CtrlMainGUI()
	{
		pm = PManager.getDefault();
		ui = new MainGUI();
		ui_shell = ui.getShell();
		ui.setController(this);
		th_update_gui = new Thread(new RunnableUpdateGUI());

		pm.status_mgr.initialize(ui.getStatusLabel());
		ctrl_about_box = new CtrlAbout();

		// ui.loadModulesGUI(ModulesManager.getDefault().getModulesNames());
	}

	/**
	 * is the Shell disposed/unloaded?.
	 * 
	 * @return true/false
	 */
	public boolean isShellDisposed()
	{
		return ui_shell.isDisposed();
	}

	@Override
	public boolean setVars(final String[] strs)
	{
		return true;
	}

	@Override
	public void show(final boolean visibility)
	{
		//ui.getShell().open();
		ui.show(visibility);
		///final Thread th_ui_state = new Thread(new RunnableKeepMainGUIStateUpdated());
		ui_is_opened = true;
		//th_ui_state.start();
	}

	/**
	 * Starts Tracking: starts StartsController session , the thread
	 * "runnableUpdateGUI" and the VideoManager Processing session.
	 */
	private void startTracking()
	{
		if (pm.startTracking())
		{
			clearForm();
			if (th_update_gui == null)
				th_update_gui = new Thread(new RunnableUpdateGUI());
			th_update_gui.start();
		}
	}

	/*	*//**
	 * Keeps MainGUI's controls in consistency with the current program state.
	 * ex: which buttons should be enabled at each state.
	 * 
	 * @author Creative
	 *//*
	private class RunnableKeepMainGUIStateUpdated implements Runnable
	{
		@Override
		public void run()
		{
			final ExperimentModule local_exp_module = (ExperimentModule) ModulesManager.getDefault()
					.getModuleByName("Experiment Module");

			while (ui_is_opened)
			{
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run()
					{
						if (!ui.getShell().isDisposed())
						{
							if (local_exp_module != null)
							{
																if (!local_exp_module.isExperimentPresent())
																{
																	ui.experiment_module_gui.editExpMenuItemEnable(false);
																	ui.experiment_module_gui.exportExpToExcelMenuItemEnable(false);
																}
																else
																{
																	ui.experiment_module_gui.editExpMenuItemEnable(true);
																	ui.experiment_module_gui.exportExpToExcelMenuItemEnable(true);
																}
							}
						}
					}
				});
				try
				{
					Thread.sleep(500);
				} catch (final InterruptedException e)
				{
				}
			}
		}

	}*/

	/**
	 * Updates the MainGUI with the latest counters' values got from
	 * StatsController.
	 * 
	 * @author Creative
	 */
	private class RunnableUpdateGUI implements Runnable
	{
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			setTableNamesColumn();
			while (pm.state == ProgramState.TRACKING)
			{
				try
				{
					Thread.sleep(200);
				} catch (final InterruptedException e)
				{
					e.printStackTrace();
				}
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run()
					{
						if (!ui.getShell().isDisposed())
							ui.fillDataTable(null, ModulesManager.getDefault()
									.getGUIData());
					}
				});
			}
		}
	}

	/**
	 * Fills the column of displaying parameters in the table.
	 */
	private void setTableNamesColumn()
	{
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run()
			{
				ui.fillDataTable(ModulesManager.getDefault().getGUINames(), null);
			}
		});
	}

	/**
	 * Sets the background of DrawZones window and the VideoManager to the
	 * current cam. image at that instant.
	 */
	public void btnSetbgAction()
	{
		if (pm.state == ProgramState.STREAMING)
		{
			pm.drw_zns.setBackground(pm.getVideoManager().updateRGBBackground());
			((SubtractorFilter) pm.getVideoManager()
					.getFilterManager()
					.getFilterByName("SubtractionFilter")).updateBG();
		}
		else if (pm.state == ProgramState.TRACKING)
			pm.status_mgr.setStatus(
					"Background can't be taken while tracking.",
					StatusSeverity.ERROR);
		else
			pm.status_mgr.setStatus(
					"Please start the camera first.",
					StatusSeverity.ERROR);
	}

	/**
	 * Starts the Streaming process, by initializing the VideoManager.
	 */
	public void mnutmCameraStartAction()
	{
		if (pm.state == ProgramState.IDLE)
		{
			final CommonFilterConfigs commonConfigs = new CommonFilterConfigs(
					640,
					480,
					30,
					0,
					"default",
					null);
			ModulesManager.getDefault().setWidthandHeight(
					commonConfigs.width,
					commonConfigs.height);
			pm.initializeVideoManager(commonConfigs,null);
			configureScreenDrawerFilter("ScreenDrawer", commonConfigs, true);
			pm.status_mgr.setStatus("Camera is Starting..", StatusSeverity.WARNING);
		}
		else
			pm.status_mgr.setStatus("Camera is already started.", StatusSeverity.ERROR);

	}

	/**
	 * Configures the ScreenDrawer filter and sets its two screens to the AWT
	 * screens in this form.
	 * 
	 * @param name
	 *            name of the ScreenDrawer filter
	 * @param configs
	 *            common configurations instance
	 * @param enable_sec_screen
	 *            whether to enable secondary screen
	 */
	public void configureScreenDrawerFilter(
			final String name,
			final CommonFilterConfigs configs,
			final boolean enable_sec_screen)
	{
		pm.getVideoManager().updateFiltersConfigs(
				new FilterConfigs[] { new ScreenDrawerConfigs(
						name,
						ui.getAwtVideoMain().getGraphics(),
						ui.getAwtVideoSec().getGraphics(),
						configs,
						true,
						pm.shape_controller) });
	}

	/**
	 * Handles the camera options menu item click action.
	 */
	public void mnutmCameraOptionsAction()
	{
		pm.cam_options.show(true);
	}

	/**
	 * Handles the "Edit options" menu item click action.
	 */
	public void mnutmEditOptionsAction()
	{
		pm.options_window.show(true);
	}

	/**
	 * Stops all Program Activity and closes the GUI window.
	 */
	public void closeProgram()
	{
		if(pm.state == ProgramState.TRACKING)
			pm.stopTracking();
		if (pm.state == ProgramState.STREAMING)
			pm.stopStreaming();
		ui_is_opened = false;
		try
		{
			Thread.sleep(510);
		} catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
		ui.closeWindow();
		pm.unloadGUI();
		//System.exit(0);
	}

	/**
	 * Stops Tracking: Ends the session of StatsController and saves the rat
	 * information to file(through the InfoController). if Recording,it stops
	 * and saves the video file.
	 */
	public void stopTrackingAction()
	{
		th_update_gui = null;
		pm.stopTracking();

	}

	/**
	 * Shows the rat information window to enter the next rat number & group.
	 */
	public void startTrackingAction()
	{
		final Thread th_start_gui_procedures = new Thread(new Runnable() {

			@Override
			public void run()
			{
				if (pm.state == ProgramState.STREAMING
						&& pm.getVideoManager().isBgSet())
				{
					if (ModulesManager.getDefault().areModulesReady(ui.getShell()))
					{
						final ExperimentModule tmp_exp_module = (ExperimentModule) ModulesManager.getDefault()
						.getModuleByName(
								"Experiment Module");
						if (tmp_exp_module != null)
						{
							startTracking();
						}
						else
						{
							Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run()
								{
									final MessageBox mbox = new MessageBox(
											ui.getShell(),
											SWT.ICON_QUESTION
											| SWT.YES
											| SWT.NO);
									mbox.setMessage("No experiment module is found! continue?");
									mbox.setText("Continue?");
									final int res = mbox.open();
									if (res == SWT.YES)
									{
										startTracking();
									}
								}
							});
						}
					}
					else
						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run()
							{
								pm.status_mgr.setStatus(
										"Some Modules have problems.",
										StatusSeverity.ERROR);
							}
						});

				}
				else
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run()
						{
							pm.status_mgr.setStatus(
									"Please make sure the camera is running, you have set the background.",
									StatusSeverity.ERROR);
						}
					});
			}
		});
		th_start_gui_procedures.start();
	}

	/**
	 * Clears the GUI data.
	 */
	public void clearForm()
	{
		ui.clearForm();
	}

	/**
	 * Handles all key events of the GUI.
	 * 
	 * @param key
	 *            the pressed key on the keyboard
	 */
	public void keyPressedAction(final char key)
	{
		/*
		 * if(pm.state==ProgramState.TRACKING |
		 * pm.state==ProgramState.RECORDING)
		 * if(key==java.awt.event.KeyEvent.VK_R)
		 * stats_controller.incrementRearingCounter(); else
		 * if(key==java.awt.event.KeyEvent.VK_C)
		 * stats_controller.decrementRearingCounter();
		 */
	}

	/**
	 * Handles the "About" menu item click action.
	 */
	public void mnutmHelpAboutAction()
	{
		ctrl_about_box.show(true);
	}

	/**
	 * Loads GUI instances for the available video filters.
	 * 
	 * @param filters
	 *            ArrayList of available filters
	 */
	public void loadPluggedGUI(final PluggedGUI[] pGUI)
	{
		ui.loadPluggedGUI(pGUI);
		for (final PluggedGUI pgui : pGUI)
			pm.addStateListener(pgui);
	}
	private String file_name ="F:\\FST.avi";
	public void setVideoFileMode()
	{
		final FileDialog fileDialog = new FileDialog(ui.getShell(), SWT.OPEN);
		if(file_name==null)
			file_name= fileDialog.open();
		if (file_name != null)
		{
			if (pm.state == ProgramState.IDLE)
			{
				final CommonFilterConfigs commonConfigs = new CommonFilterConfigs(
						640,
						480,
						30,
						0,
						"VideoFile",
						null);
				ModulesManager.getDefault().setWidthandHeight(
						commonConfigs.width,
						commonConfigs.height);
				pm.initializeVideoManager(commonConfigs,file_name);

				configureScreenDrawerFilter("ScreenDrawer", commonConfigs, true);
				pm.status_mgr.setStatus("Camera is Starting..", StatusSeverity.WARNING);
			}
			else
				pm.status_mgr.setStatus(
						"Camera is already started.",
						StatusSeverity.ERROR);
		}
		file_name=null;
	}

	public void startStreamingAction()
	{
		if (ui.getSelectedInputMode().equals("CAM"))
			mnutmCameraStartAction();
		else if(ui.getSelectedInputMode().equals("VIDEOFILE"))
			setVideoFileMode();
		pm.startStreaming();
	}

	public void stopStreamingAction()
	{
		if (pm.state == ProgramState.STREAMING)
		{
			pm.stopStreaming();
			pm.status_mgr.setStatus("Streaming is Stopped!", StatusSeverity.WARNING);
		}
		else if (pm.state == ProgramState.TRACKING)
			pm.status_mgr.setStatus(
					"Streaming Cannot be stopped while Tracking is running.",
					StatusSeverity.ERROR);
	}

	@Override
	public void updateProgramState(final ProgramState state)
	{
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run()
			{
				switch (state)
				{
				case IDLE:
					ui.btnStartTrackingEnable(false);
					ui.btnStopTrackingEnable(false);
					ui.btnStartStreamingEnable(true);
					ui.btnStopStreamingEnable(false);
					ui.btnSetBackgroundEnable(false);
					break;
				case STREAMING:
					ui.btnStartTrackingEnable(true);
					ui.btnStopTrackingEnable(false);
					ui.btnStartStreamingEnable(false);
					ui.btnStopStreamingEnable(true);
					ui.btnSetBackgroundEnable(true);
					break;
				case TRACKING:
					ui.btnStartTrackingEnable(false);
					ui.btnStopTrackingEnable(true);
					ui.btnStartStreamingEnable(false);
					ui.btnStopStreamingEnable(false);
					ui.btnSetBackgroundEnable(false);
					break;
				}
			}
		});

	}
}
