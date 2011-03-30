package utils;

import gfx_panel.GfxPanel;
import modules.ModulesManager;

import org.eclipse.swt.widgets.Display;

import utils.saveengines.ExcelEngine;
import utils.video.VideoProcessor;
import utils.video.filters.CommonFilterConfigs;
import control.ShapeController;
import control.ZonesController;
import control.ui.CtrlAbout;
import control.ui.CtrlCamOptions;
import control.ui.CtrlExperimentForm;
import control.ui.CtrlGroupsForm;
import control.ui.CtrlMainGUI;
import control.ui.CtrlOptionsWindow;
import control.ui.CtrlRatInfoForm;
import control.ui.CtrlDrawZones;

/**
 * Program Manager, contains the main function, creates GUI and Controllers.
 * 
 * @author Creative
 */
public class PManager
{

	public enum ProgramState
	{
		IDLE, RECORDING, STREAMING, TRACKING;
	}

	private static PManager default_me;
	public static CtrlMainGUI main_gui;

	public static PManager getDefault()
	{
		return default_me;
	}

	/**
	 * @param args
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

	public CtrlCamOptions cam_options;
	public CtrlDrawZones drw_zns;
	public ExcelEngine excel_engine;
	public CtrlExperimentForm frm_exp;
	public CtrlGroupsForm frm_grps;
	public CtrlRatInfoForm frm_rat;
	private GfxPanel gfx_panel;
	public static Logger log;
	public CtrlOptionsWindow options_window;
	public final ShapeController shape_controller;
	public ProgramState state;
	public StatusManager status_mgr;
	private final VideoProcessor vp;
	private final ZonesController zone_controller;
	public CtrlAbout about;

	/**
	 * Initializes GUI controllers, Model Controllers and Video Controller.
	 */
	public PManager()
	{
		state = ProgramState.IDLE;
		excel_engine = new ExcelEngine();
		default_me = this;
		status_mgr = new StatusManager();
		zone_controller = ZonesController.getDefault();
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
		

		zone_controller.init();
		shape_controller.init();
		vp = new VideoProcessor();
	}

	public GfxPanel getGfxPanel()
	{
		return gfx_panel;
	}

	public VideoProcessor getVideoProcessor()
	{
		return vp;
	}

	/**
	 * Initializes the Video Processor.
	 * 
	 * @param common_configs
	 *            CommonFilterConfigs object needed by most filters
	 */
	public void initializeVideoProcessor(final CommonFilterConfigs common_configs)
	{
		zone_controller.setWidthandHeight(common_configs.width, common_configs.height);
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
		this.gfx_panel = gfx_panel;
		shape_controller.linkWithGFXPanel(gfx_panel);
	}

	/**
	 * Unloads the Video Processor, used when switching video libraries..
	 */
	public void unloadVideoProcessor()
	{
		if (vp != null & state != ProgramState.IDLE)
			vp.unloadLibrary();
	}

}
