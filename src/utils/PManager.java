package utils;

import gfx_panel.GfxPanel;
import modules.ModulesManager;
import modules.experiment.ExcelEngine;

import org.eclipse.swt.widgets.Display;

import utils.video.VideoManager;
import utils.video.filters.CommonFilterConfigs;
import control.ShapeController;
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

	/**
	 * Defines program states.
	 * 
	 * @author Creative
	 */
	public enum ProgramState
	{
		/**
		 * IDLE: doing nothing, STREAMING: displaying video frames on the
		 * screen, TRACKING: tracking the object: STREAMING + TRACKING,
		 * RECORDING: recording video: STREAMING + TRACKING + RECORDING.
		 */
		IDLE, RECORDING, STREAMING, TRACKING;
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
	public StatusManager status_mgr;
	private final VideoManager vp;
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

	/**
	 * @param args
	 *            Main arguments
	 */
	public static void main(final String[] args)
	{
		new PManager();
		final Display display = Display.getDefault();

		while (!main_gui.isShellDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * Initializes GUI controllers, Model Controllers and Video Controller.
	 */
	public PManager()
	{
		state = ProgramState.IDLE;
		excel_engine = new ExcelEngine();
		default_me = this;
		status_mgr = new StatusManager();

		shape_controller = ShapeController.getDefault();
		drw_zns = new CtrlDrawZones();
		frm_exp = new CtrlExperimentForm();
		frm_grps = new CtrlGroupsForm();
		frm_rat = new CtrlRatInfoForm();
		cam_options = new CtrlCamOptions();
		options_window = new CtrlOptionsWindow();
		log = new Logger();
		new ModulesManager();
		main_gui = new CtrlMainGUI();
		main_gui.show(true);

		vp = new VideoManager();
	}

	/**
	 * Gets the VideoManager instance.
	 * 
	 * @return VideoManager instance
	 */
	public VideoManager getVideoManager()
	{
		return vp;
	}

	/**
	 * Initializes the Video Processor.
	 * 
	 * @param common_configs
	 *            CommonFilterConfigs object needed by most filters
	 */
	public void initializeVideoManager(final CommonFilterConfigs common_configs)
	{
		ModulesManager.getDefault().setWidthandHeight(
				common_configs.width,
				common_configs.height);
		if (vp.initialize(common_configs))
			vp.startStreaming();
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
	public void unloadVideoManager()
	{
		if (vp != null & state != ProgramState.IDLE)
			vp.unloadLibrary();
	}

}
