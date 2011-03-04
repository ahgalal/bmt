package control.ui;

import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import ui.MainGUI;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;
import utils.video.processors.CommonConfigs;
import utils.video.processors.FilterConfigs;
import utils.video.processors.screendrawer.ScreenDrawerConfigs;
import control.InfoController;
import control.StatsController;

/**
 * Controller of the MainGUI window
 * @author Creative
 *
 */
public class Ctrl_MainGUI extends ControllerUI {
	private MainGUI ui;
	private StatsController stats_controller;  //  @jve:decl-index=0:
	private InfoController info_controller;  //  @jve:decl-index=0:
	private PManager pm;  //  @jve:decl-index=0:
	public boolean stop_tracking=false;
	private Thread th_update_gui;  //  @jve:decl-index=0:
	private Shell ui_shell;
	private boolean ui_is_opened;
	private Ctrl_About ctrl_about_box;

	/**
	 * Initializes class attributes (MainGUI,StatsController,
	 * PManager and InfoController)
	 */
	public Ctrl_MainGUI()
	{
		pm=PManager.getDefault();
		ui= new MainGUI();
		ui_shell=ui.getShell();
		ui.setController(this);
		th_update_gui = new Thread(new runnableUpdateGUI());

		stats_controller=StatsController.getDefault();
		info_controller= InfoController.getDefault();
		pm.status_mgr.initialize(ui.getStatusLabel());
		ctrl_about_box=new Ctrl_About();
	}

	public Frame getMainAWTFrame(){
		return ui.awt_video_main;
	}

	public Frame getSecAWTFrame(){
		return ui.awt_video_sec;
	}

	public boolean isShellDisposed()
	{
		return ui_shell.isDisposed();
	}

	@Override
	public boolean setVars(String[] strs) {
		return true;
	}

	/* (non-Javadoc)
	 * @see control.ui.ControllerUI#show(boolean)
	 * 
	 * Shows the GUI window and starts the thread "RunnableKeepMainGUIStateUpdated"
	 */
	@Override
	public void show(boolean visibility) {
		ui.show(visibility);
		Thread th_ui_state = new Thread(new RunnableKeepMainGUIStateUpdated());
		ui_is_opened=true;
		th_ui_state.start();
	}

	/**
	 * Starts Tracking:
	 * starts StartsController session , the thread "runnableUpdateGUI" and
	 * the VideoProcessor Processing session.
	 */
	public void startTracking()
	{
		if(pm.state==ProgramState.STREAMING)
		{
			stats_controller.startSession();
			stop_tracking=false;
			if(th_update_gui==null)
				th_update_gui = new Thread(new runnableUpdateGUI());
			th_update_gui.start();
			pm.getVideoProcessor().startProcessing();
		}
		else
			pm.status_mgr.setStatus("Please start the camera first.", StatusSeverity.ERROR);
	}

	/**
	 * Keeps MainGUI's controls in consistency with the current program state.
	 * ex: which buttons should be enabled at each state.
	 * @author Creative
	 *
	 */
	private class RunnableKeepMainGUIStateUpdated implements Runnable
	{
		@Override
		public void run() {
			while(ui_is_opened)
			{
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						switch (pm.state)
						{
						case IDLE:
							ui.btn_not_rearing_enable(false);
							ui.btn_rearing_now_enable(false);
							ui.btn_start_record_enable(false);
							ui.btn_stop_record_enable(false);
							break;
						case RECORDING:
							ui.btn_not_rearing_enable(true);
							ui.btn_rearing_now_enable(true);
							ui.btn_start_record_enable(false);
							ui.btn_stop_record_enable(true);
							break;
						case STREAMING:
							ui.btn_not_rearing_enable(false);
							ui.btn_rearing_now_enable(false);
							ui.btn_start_record_enable(false);
							ui.btn_stop_record_enable(false);
							break;
						case TRACKING:
							ui.btn_not_rearing_enable(true);
							ui.btn_rearing_now_enable(true);
							ui.btn_start_record_enable(true);
							ui.btn_stop_record_enable(false);
							break;
						}
					}
				});
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
			}
		}

	}
	/**
	 * Updates the MainGUI with the latest counters' values got from
	 * StatsController.
	 * @author Creative
	 *
	 */
	private class runnableUpdateGUI implements Runnable {
		public void run() {

			while(!stop_tracking)
			{
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(stats_controller.require_update_gui)
				{
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							ui.updateStats(stats_controller.getTotalDistance(),
									stats_controller.getCurrentZoneNumber(),
									stats_controller.getSessionTimeTillNow(),
									stats_controller.getCentral_entrance(),
									stats_controller.getAll_entrance(), 
									stats_controller.getCentralTime(),
									stats_controller.getRearingCtr());

							stats_controller.require_update_gui=false;
						}
					});
				}
			}
		}
	}

	/**
	 * Sets the background of DrawZones window and the VideoProcessor to the
	 * current cam. image at that instant.
	 */
	public void btn_setbg_Action()
	{
		if(pm.state==ProgramState.STREAMING)
		{
			pm.getGfxPanel().setBackground(pm.getVideoProcessor().getRGBBackground());
			pm.getVideoProcessor().setBg_image();
		}
		else if(pm.state==ProgramState.TRACKING)
			pm.status_mgr.setStatus("Background can't be taken while tracking.", StatusSeverity.ERROR);
		else
			pm.status_mgr.setStatus("Please start the camera first.", StatusSeverity.ERROR);
	}

	/**
	 * Starts the Streaming process, by initializing the VideoProcessor.
	 */
	public void mnutm_camera_start_Action()
	{
		if(pm.state==ProgramState.IDLE)
		{
			CommonConfigs commonConfigs = new CommonConfigs(640, 480, 30, 0, "JMyron", null);
			ScreenDrawerConfigs scrn_drwr_cfgs = new ScreenDrawerConfigs(null, null, null, null, null, true);
			pm.initializeVideoProcessor(commonConfigs);
			pm.getVideoProcessor().updateFiltersConfigs(new FilterConfigs[] {scrn_drwr_cfgs});
			pm.status_mgr.setStatus("Camera is Starting..", StatusSeverity.WARNING);
		}
		else
			pm.status_mgr.setStatus("Camera is already started.", StatusSeverity.ERROR);

	}

	/**
	 * Stops the camrea stream, by unloading the VideoProcessor.
	 */
	public void mnuitm_stop_camera_Action() {
		if(pm.state==ProgramState.STREAMING)
		{
			pm.unloadVideoProcessor();
			pm.status_mgr.setStatus("Camera is Stopped!", StatusSeverity.WARNING);
		}
		else if(pm.state==ProgramState.TRACKING)
			pm.status_mgr.setStatus("Camera Cannot be stopped while Tracking is running.", StatusSeverity.ERROR);
	}

	public void mnutm_camera_options_Action() {
		pm.cam_options.show(true);
	}

	public void mnutm_edit_openzoneeditor_Action() {
		pm.drw_zns.show(true);		
	}

	public void mnutm_edit_options_Action() {
		pm.options_window.show(true);		
	}

	public void mnuitm_edt_exp_Action() {
		pm.frm_exp.show(true);		
	}

	/**
	 * Shows the new ExperimentForm and unloads the previous experiment.
	 */
	public void mnutm_experiment_newexp_Action() {
		pm.frm_exp.show(true);
		info_controller.unloadExperiment();		
	}

	/**
	 * Loads an experiment from file:
	 * Shows Open Dialog box, Unloads the previous experiment and loads the
	 * new experiment from the selected file.
	 * @param sShell
	 */
	public void mnutm_experiment_loadexp_Action(Shell sShell) {
		FileDialog fileDialog = new FileDialog(sShell, SWT.OPEN);
		String file_name=fileDialog.open();
		if(file_name!=null)
		{
			info_controller.unloadExperiment();
			info_controller.loadInfoFromTXTFile(file_name);
			info_controller.setExpFileName(file_name);
			pm.status_mgr.setStatus("Experiment Loaded Successfully!", StatusSeverity.WARNING);
		}
	}

	/**
	 * Stops all Program Activity and closes the GUI window
	 */
	public void closeProgram()
	{
		stop_tracking=true;
		if(pm.state!=ProgramState.RECORDING)
			pm.unloadVideoProcessor();
		ui_is_opened=false;
		ui.closeWindow();
	}

	/**
	 * Starts video recording.
	 */
	public void btn_start_record_Action() {
		pm.getVideoProcessor().startRecordingVideo();		
	}

	/**
	 * Stops video recording, and asks for a location to save the video file.
	 */
	public void stoprecordAction() {
		pm.getVideoProcessor().stopRecordingVideo();
		FileDialog fileDialog = new FileDialog(ui.getShell(), SWT.SAVE);
		String file_name=fileDialog.open();
		pm.getVideoProcessor().saveVideoFile(file_name);
	}

	/**
	 * Stops Tracking:
	 * Ends the session of StatsController and saves the rat information to
	 * file(through the InfoController).
	 * if Recording,it stops and saves the video file.
	 */
	public void btn_stop_tracking_Action() {
		if(pm.state==ProgramState.TRACKING | pm.state==ProgramState.RECORDING)
		{
			if(pm.state==ProgramState.RECORDING)
				stoprecordAction();
			
			pm.getVideoProcessor().stopProcessing();
			stop_tracking=true;
			stats_controller.endSession();
			th_update_gui=null;
			InfoController.getDefault().saveRatInfo();
		}
		else
			pm.status_mgr.setStatus("Tracking is not running.", StatusSeverity.ERROR);
	}

	/**
	 * Shows the rat information window to enter the next rat number & group.
	 */
	public void btn_start_tracking_Action() {
		if(pm.state==ProgramState.STREAMING & pm.getVideoProcessor().isBg_set())
			pm.frm_rat.show(true);
		else
			pm.status_mgr.setStatus("Please make sure the camera is running and you have set the background.", StatusSeverity.ERROR);
	}

	public void clearForm() {
		ui.clearForm();		
	}

	/**
	 * Shows a save file dialog to save the exported Excel data to that file.
	 */
	public void mnutm_experiment_exporttoexcel_Action() {
		FileDialog fileDialog = new FileDialog(ui.getShell(), SWT.SAVE);
		fileDialog.setFilterExtensions(new String[] {"*.xlsx"});
		String file_name=fileDialog.open();
		if(file_name!=null)
			info_controller.writeToExcelFile(file_name);
	}

	public void btn_not_rearing_Action() {
		rearingNow(false);
	}

	public void btn_rearing_now_Action() {
		rearingNow(true);
	}

	/**
	 * Notifies the VideoProcessor that the rat is (rearing/not rearing) in
	 * reality, so that the VideoProcessor can start learning the rat's
	 * size when (rearing/not rearing).
	 * @param rearing is the rat rearing now?
	 */
	public void rearingNow(boolean rearing) {
		if(pm.state==ProgramState.TRACKING)
			pm.getVideoProcessor().rearingNow(rearing);
		else
			pm.status_mgr.setStatus("Tracking is not running!", StatusSeverity.ERROR);
	}

	/**
	 * Handles all key events of the GUI.
	 * @param key the pressed key on the keyboard
	 */
	public void keyPressed_Action(char key) {
		if(pm.state==ProgramState.TRACKING | pm.state==ProgramState.RECORDING)
			if(key==java.awt.event.KeyEvent.VK_R)
				stats_controller.incrementRearingCounter();
			else if(key==java.awt.event.KeyEvent.VK_C)
				stats_controller.decrementRearingCounter();
	}

	public void btn_add_rearing_Action() {
		if(pm.state==ProgramState.TRACKING | pm.state==ProgramState.RECORDING)	
			stats_controller.incrementRearingCounter();
	}

	public void btn_sub_rearing_Action() {
		if(pm.state==ProgramState.TRACKING | pm.state==ProgramState.RECORDING)	
			stats_controller.decrementRearingCounter();
	}
	public void mnutm_help_about_Action() {
		ctrl_about_box.show(true);
	}



}
