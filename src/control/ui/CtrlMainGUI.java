package control.ui;

import java.awt.Frame;

import modules.ExperimentModule;
import modules.ModulesManager;
import modules.RearingModule;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import ui.MainGUI;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;
import utils.video.processors.CommonFilterConfigs;
import utils.video.processors.FilterConfigs;
import utils.video.processors.rearingdetection.RearingDetector;
import utils.video.processors.recorder.VideoRecorder;
import utils.video.processors.screendrawer.ScreenDrawerConfigs;
import utils.video.processors.subtractionfilter.SubtractorFilter;

/**
 * Controller of the MainGUI window.
 * 
 * @author Creative
 */
public class CtrlMainGUI extends ControllerUI
{
	private final MainGUI ui;
	private final PManager pm; // @jve:decl-index=0:
	public boolean stop_tracking = false;
	private Thread th_update_gui; // @jve:decl-index=0:
	private final Shell ui_shell;
	private boolean ui_is_opened;
	private final CtrlAbout ctrl_about_box;

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
	}

	public Frame getMainAWTFrame()
	{
		return ui.getAwtVideoMain();
	}

	public Frame getSecAWTFrame()
	{
		return ui.getAwtVideoSec();
	}

	public boolean isShellDisposed()
	{
		return ui_shell.isDisposed();
	}

	@Override
	public boolean setVars(final String[] strs)
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see control.ui.ControllerUI#show(boolean) Shows the GUI window and
	 * starts the thread "RunnableKeepMainGUIStateUpdated"
	 */
	@Override
	public void show(final boolean visibility)
	{
		ui.show(visibility);
		final Thread th_ui_state = new Thread(new RunnableKeepMainGUIStateUpdated());
		ui_is_opened = true;
		th_ui_state.start();
	}

	/**
	 * Starts Tracking: starts StartsController session , the thread
	 * "runnableUpdateGUI" and the VideoProcessor Processing session.
	 */
	public void startTracking()
	{
		if (pm.state == ProgramState.STREAMING)
		{
			clearForm();
			stop_tracking = false;
			if (th_update_gui == null)
				th_update_gui = new Thread(new RunnableUpdateGUI());
			th_update_gui.start();

			pm.getVideoProcessor().startProcessing();
			ModulesManager.getDefault().runAnalyzers(true);
		} else
			pm.status_mgr.setStatus(
					"Please start the camera first.",
					StatusSeverity.ERROR);
	}

	/**
	 * Keeps MainGUI's controls in consistency with the current program state.
	 * ex: which buttons should be enabled at each state.
	 * 
	 * @author Creative
	 */
	private class RunnableKeepMainGUIStateUpdated implements Runnable
	{
		@Override
		public void run()
		{
			while (ui_is_opened)
			{

				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run()
					{
						if (!ui.getShell().isDisposed())
							switch (pm.state)
							{
							case IDLE:
								ui.btnNotRearingEnable(false);
								ui.btnRearingNowEnable(false);
								ui.btnStartRecordEnable(false);
								ui.btnStopRecordEnable(false);
								break;
							case RECORDING:
								ui.btnNotRearingEnable(true);
								ui.btnRearingNowEnable(true);
								ui.btnStartRecordEnable(false);
								ui.btnStopRecordEnable(true);
								break;
							case STREAMING:
								ui.btnNotRearingEnable(false);
								ui.btnRearingNowEnable(false);
								ui.btnStartRecordEnable(false);
								ui.btnStopRecordEnable(false);
								break;
							case TRACKING:
								ui.btnNotRearingEnable(true);
								ui.btnRearingNowEnable(true);
								ui.btnStartRecordEnable(true);
								ui.btnStopRecordEnable(false);
								break;
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

	}

	/**
	 * Updates the MainGUI with the latest counters' values got from
	 * StatsController.
	 * 
	 * @author Creative
	 */
	private class RunnableUpdateGUI implements Runnable
	{
		public void run()
		{

			setTableNamesColumn();
			while (!stop_tracking)
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
						ui.fillDataTable(null, ModulesManager.getDefault().getGUIData());
					}
				});
			}
		}
	}

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
	 * Sets the background of DrawZones window and the VideoProcessor to the
	 * current cam. image at that instant.
	 */
	public void btnSetbgAction()
	{
		if (pm.state == ProgramState.STREAMING)
		{
			pm.getGfxPanel().setBackground(pm.getVideoProcessor().getRGBBackground());
			((SubtractorFilter) pm.getVideoProcessor()
					.getFilterManager()
					.getFilterByName("SubtractionFilter")).setBgImage(pm.getVideoProcessor()
					.getRGBBackground());
		} else if (pm.state == ProgramState.TRACKING)
			pm.status_mgr.setStatus(
					"Background can't be taken while tracking.",
					StatusSeverity.ERROR);
		else
			pm.status_mgr.setStatus(
					"Please start the camera first.",
					StatusSeverity.ERROR);
	}

	/**
	 * Starts the Streaming process, by initializing the VideoProcessor.
	 */
	public void mnutmCameraStartAction()
	{
		if (pm.state == ProgramState.IDLE)
		{
			// CommonConfigs commonConfigs = new CommonConfigs(640, 480, 30, 0,
			// "JMyron", null);
			final CommonFilterConfigs commonConfigs = new CommonFilterConfigs(
					640,
					480,
					30,
					0,
					"AGCamLib",
					null);
			final ScreenDrawerConfigs scrn_drwr_cfgs = new ScreenDrawerConfigs(
					null,
					null,
					null,
					null,
					null,
					null,
					true);
			pm.initializeVideoProcessor(commonConfigs);
			pm.getVideoProcessor().updateFiltersConfigs(
					new FilterConfigs[] { scrn_drwr_cfgs });
			pm.status_mgr.setStatus("Camera is Starting..", StatusSeverity.WARNING);
		} else
			pm.status_mgr.setStatus("Camera is already started.", StatusSeverity.ERROR);

	}

	/**
	 * Stops the camrea stream, by unloading the VideoProcessor.
	 */
	public void mnuitmStopCameraAction()
	{
		if (pm.state == ProgramState.STREAMING)
		{
			pm.unloadVideoProcessor();
			pm.status_mgr.setStatus("Camera is Stopped!", StatusSeverity.WARNING);
		} else if (pm.state == ProgramState.TRACKING)
			pm.status_mgr.setStatus(
					"Camera Cannot be stopped while Tracking is running.",
					StatusSeverity.ERROR);
	}

	public void mnutmCameraOptionsAction()
	{
		pm.cam_options.show(true);
	}

	public void mnutmEditOpenZoneEditorAction()
	{
		pm.drw_zns.show(true);
	}

	public void mnutmEditOptionsAction()
	{
		pm.options_window.show(true);
	}

	public void mnuitmEditExpAction()
	{
		pm.frm_exp.show(true);
	}

	/**
	 * Shows the new ExperimentForm and unloads the previous experiment.
	 */
	public void mnutmExperimentNewExpAction()
	{
		pm.frm_exp.clearForm();
		pm.frm_grps.clearForm();
		PManager.main_gui.clearForm();
		pm.frm_exp.show(true);
		((ExperimentModule) ModulesManager.getDefault().getModuleByName(
				"Experiment Module")).unloadExperiment();
	}

	/**
	 * Loads an experiment from file: Shows Open Dialog box, Unloads the
	 * previous experiment and loads the new experiment from the selected file.
	 * 
	 * @param sShell
	 */
	public void mnutmExperimentLoadexpAction(final Shell sShell)
	{
		final FileDialog fileDialog = new FileDialog(sShell, SWT.OPEN);
		final String file_name = fileDialog.open();
		if (file_name != null)
		{
			pm.frm_exp.clearForm();
			pm.frm_grps.clearForm();
			clearForm();
			((ExperimentModule) ModulesManager.getDefault().getModuleByName(
					"Experiment Module")).unloadExperiment();
			((ExperimentModule) ModulesManager.getDefault().getModuleByName(
					"Experiment Module")).loadInfoFromTXTFile(file_name);
			((ExperimentModule) ModulesManager.getDefault().getModuleByName(
					"Experiment Module")).setExpFileName(file_name);
			pm.status_mgr.setStatus(
					"Experiment Loaded Successfully!",
					StatusSeverity.WARNING);
		}
	}

	/**
	 * Stops all Program Activity and closes the GUI window.
	 */
	public void closeProgram()
	{
		stop_tracking = true;
		if (pm.state != ProgramState.RECORDING)
			pm.unloadVideoProcessor();
		ui_is_opened = false;
		try
		{
			Thread.sleep(510);
		} catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
		ui.closeWindow();
	}

	/**
	 * Starts video recording.
	 */
	public void btnStartRecordAction()
	{
		pm.getVideoProcessor().getFilterManager().enableFilter("Recorder", true);
	}

	/**
	 * Stops video recording, and asks for a location to save the video file.
	 */
	public void stoprecordAction()
	{
		pm.getVideoProcessor().getFilterManager().enableFilter("Recorder", false);
		final FileDialog fileDialog = new FileDialog(ui.getShell(), SWT.SAVE);
		final String file_name = fileDialog.open();
		((VideoRecorder) pm.getVideoProcessor().getFilterManager().getFilterByName(
				"Recorder")).saveVideoFile(file_name);
	}

	/**
	 * Stops Tracking: Ends the session of StatsController and saves the rat
	 * information to file(through the InfoController). if Recording,it stops
	 * and saves the video file.
	 */
	public void btnStopTrackingAction()
	{
		if (pm.state == ProgramState.TRACKING | pm.state == ProgramState.RECORDING)
		{

			ModulesManager.getDefault().runAnalyzers(false);
			if (pm.state == ProgramState.RECORDING)
				stoprecordAction();

			pm.getVideoProcessor().stopProcessing();
			stop_tracking = true;
			// stats_controller.endSession();
			th_update_gui = null;
		} else
			pm.status_mgr.setStatus("Tracking is not running.", StatusSeverity.ERROR);
	}

	/**
	 * Shows the rat information window to enter the next rat number & group.
	 */
	public void btnStartTrackingAction()
	{
		if (pm.state == ProgramState.STREAMING
				&& pm.getVideoProcessor().isBgSet()
				&& ((ExperimentModule) ModulesManager.getDefault().getModuleByName(
						"Experiment Module")).isExperimentPresent())
			pm.frm_rat.show(true);
		else
			pm.status_mgr.setStatus(
					"Please make sure the camera is running, you have set the background and you have selected an experiment to work on.",
					StatusSeverity.ERROR);
	}

	public void clearForm()
	{
		ui.clearForm();
	}

	/**
	 * Shows a save file dialog to save the exported Excel data to that file.
	 */
	public void mnutmExperimentExportToExcelAction()
	{
		final FileDialog fileDialog = new FileDialog(ui.getShell(), SWT.SAVE);
		fileDialog.setFilterExtensions(new String[] { "*.xlsx" });
		final String file_name = fileDialog.open();
		if (file_name != null)
			((ExperimentModule) ModulesManager.getDefault().getModuleByName(
					"Experiment Module")).writeToExcelFile(file_name);
	}

	public void btnNotRearingAction()
	{
		rearingNow(false);
	}

	public void btnRearingNowAction()
	{
		rearingNow(true);
	}

	/**
	 * Notifies the VideoProcessor that the rat is (rearing/not rearing) in
	 * reality, so that the VideoProcessor can start learning the rat's size
	 * when (rearing/not rearing).
	 * 
	 * @param rearing
	 *            is the rat rearing now?
	 */
	public void rearingNow(final boolean rearing)
	{
		if (pm.state == ProgramState.TRACKING)
			((RearingDetector) pm.getVideoProcessor().getFilterManager().getFilterByName(
					"RearingDetector")).rearingNow(rearing);
		else
			pm.status_mgr.setStatus("Tracking is not running!", StatusSeverity.ERROR);
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

	public void btnAddRearingAction()
	{
		if (pm.state == ProgramState.TRACKING | pm.state == ProgramState.RECORDING)
			((RearingModule) ModulesManager.getDefault()
					.getModuleByName("Rearing Module")).incrementRearingCounter();
	}

	public void btnSubRearingAction()
	{
		if (pm.state == ProgramState.TRACKING | pm.state == ProgramState.RECORDING)
			((RearingModule) ModulesManager.getDefault()
					.getModuleByName("Rearing Module")).decrementRearingCounter();
	}

	public void mnutmHelpAboutAction()
	{
		ctrl_about_box.show(true);
	}

}
