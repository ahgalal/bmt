package utils;

import gfx_panel.GfxPanel;

import org.eclipse.swt.widgets.Display;

import utils.saveengines.ExcelEngine;
import utils.video.VideoProcessor;
import control.ShapeController;
import control.StatsController;
import control.ZonesController;
import control.ui.Ctrl_CamOptions;
import control.ui.Ctrl_DrawZones;
import control.ui.Ctrl_ExperimentForm;
import control.ui.Ctrl_GroupsForm;
import control.ui.Ctrl_MainGUI;
import control.ui.Ctrl_OptionsWindow;
import control.ui.Ctrl_RatInfoForm;

/**
 * Program Manager, contains the main function, creates GUI and Controllers.
 * @author Creative
 */
public class PManager {

	public enum ProgramState {
		IDLE , RECORDING , STREAMING ,TRACKING;
	}
	
	private static PManager default_me;
	public static Ctrl_MainGUI main_gui;
	
	public static PManager getDefault()
	{
		return default_me;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PManager();
		Display display = Display.getDefault();

		while (!main_gui.isShellDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
	public Ctrl_CamOptions cam_options;
	public Ctrl_DrawZones drw_zns;
	public ExcelEngine excel_engine;
	public Ctrl_ExperimentForm frm_exp;
	public Ctrl_GroupsForm frm_grps;
	public Ctrl_RatInfoForm frm_rat;
	private GfxPanel gfx_panel;
	public Logger log;
	public Ctrl_OptionsWindow options_window;
	private ShapeController shape_controller;
	public ProgramState state;
	private StatsController stats_controller;
	public StatusManager status_mgr;
	String tmp_format="YUV";
	private VideoProcessor vp ;
	private ZonesController zone_controller;

	/**
	 * Initializes GUI controllers, Model Controllers and Video Controller.
	 */
	public PManager() {
		state = ProgramState.IDLE;
		excel_engine=new ExcelEngine();
		default_me=this;
		status_mgr = new StatusManager();
		stats_controller=StatsController.getDefault();
		zone_controller =ZonesController.getDefault();
		shape_controller =ShapeController.getDefault();
		drw_zns= new Ctrl_DrawZones();
		frm_exp = new Ctrl_ExperimentForm();
		frm_grps = new Ctrl_GroupsForm();
		frm_rat = new Ctrl_RatInfoForm();
		cam_options = new Ctrl_CamOptions();
		options_window = new Ctrl_OptionsWindow();
		log= new Logger();
		main_gui = new Ctrl_MainGUI();
		main_gui.show(true);

		stats_controller.init();
		zone_controller.init();
		shape_controller.init();
		vp = new VideoProcessor(main_gui.getMainAWTFrame(),main_gui.getSecAWTFrame());
	}
	/**
	 * Displays advanced cam. options, can be accessible through JMyron lib. only,
	 * but it affects JMF & OpenCV also.
	 */
	public void displayMoreVideoLibSettings()
	{
		vp.displayMoreSettings();
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
	 * @param vid_lib video library to use
	 * @param w width
	 * @param h height
	 * @param frame_rate frame rate
	 * @param cam_index camera index
	 */
	public void initializeVideoProcessor(String vid_lib,int w,int h,int frame_rate,int cam_index)
	{
		vp.setFormat(tmp_format);

		if(vp.initialize(vid_lib,frame_rate,cam_index, w, h, 40))
			vp.startStreaming();
	}

	/**
	 * Linkes Gfx_panel with ShapeController, gives the gfx_panel instance to
	 * the shape_controller instance.
	 * @param gfx_panel GfxPanel object to send to the shape controller
	 */
	public void linkGFXPanelWithShapeCtrlr(GfxPanel gfx_panel)
	{
		this.gfx_panel=gfx_panel;
		shape_controller.linkWithGFXPanel(gfx_panel);
	}

	/**
	 * Sets the video format, useful in case of JMF only.
	 * @param format video format (RGB/YUV)
	 */
	public void setFormat(String format)
	{
		tmp_format=format;
	}

	/**
	 * Sets the subtraction threshold of the video processor's subtraction filter,
	 * and the rearing threshold of the rearing detector.
	 * @param thresh subtraction threshold 
	 * @param rearingThresh rearing threshold
	 */
	public void setThreshold(int thresh, int rearingThresh)
	{
		vp.setThreshold(thresh,rearingThresh);
	}


	/**
	 * Starts the tracking process.
	 */
	public void startTracking()
	{
		main_gui.startTracking();
	}

	/**
	 * Unloads the Video Processor, used when switching video libraries..
	 */
	public void unloadVideoProcessor()
	{
		if(vp!=null & state!=ProgramState.IDLE)
			vp.unloadLibrary();
	}
	
	/**
	 * Updates the video processor's background image.
	 */
	public void updateVideoProcBG()
	{
		vp.setBg_image();
	}
}


