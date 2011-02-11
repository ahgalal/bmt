package control.ui;

import control.ZonesController;
import ui.CamOptions;
import utils.PManager;

/**
 * Controller of the CamOptions GUI window
 * @author Creative
 *
 */
public class Ctrl_CamOptions extends ControllerUI{
	private String library = "JMF";  //  @jve:decl-index=0:
	private String prev_library="";
	private String format = "YUV";  //  @jve:decl-index=0:
	private int width = 640, height = 480;
	private int frame_rate = 30;
	private int cam_num=0;
	private CamOptions ui;
	private boolean lib_is_already_created;

	/**
	 * Initializes class attributes (CamOptions and PManager)
	 */
	public Ctrl_CamOptions()
	{
		pm=PManager.getDefault();
		ui = new CamOptions();
		ui.setController(this);
	}
	/**
	 * Sets the video format of the VideoProcessor
	 * @param format Video format (either RGB or YUV)
	 */
	public void setFormat(String format)
	{
		if(library.equals("JMF"))
			pm.setFormat(format);
	}



	/**
	 * Unloads then Loads the video library
	 */
	public void btn_OK_Action()
	{
		try {
			/**
			 * this piece of code will be executed in case of 1 or 2:
			 * 1. we haven't chosen JMyron
			 * 2. we have chosen JMyron, but haven't set it's
			 *    advanced settings
			 */
			unloadAndLoadLibProcedures();
			show(false);
		} catch (NumberFormatException e1) {
			System.out.print("Error in user input ... aborting !\n");
		}
	}


	/**
	 * Unloads VideoProcessor and then Initializes it
	 */
	public void unloadAndLoadLibProcedures()
	{
		if(lib_is_already_created==false | !library.equals(prev_library))
		{
			pm.unloadVideoProcessor();
			setFormat(format);
			pm.initializeVideoProcessor(library,width,height,frame_rate,cam_num);
			ZonesController.getDefault().setWidthandHeight(width, height);
			lib_is_already_created=true;
			pm.getVideoProcessor().enableSecondaryScreen(true);
			prev_library = library.substring(0);
		}
	}

	/**
	 * Unloads then Loads the video library (JMyron Only, because this
	 * action is only available if the chosen library is JMyron)
	 * note: changing cam properties (brightness, etc ..) in JMyron , will
	 * be still effective if JMF or OpenCV are used after that.
	 */
	public void btn_jmyron_settings_Action()
	{
		unloadAndLoadLibProcedures();
		pm.displayMoreVideoLibSettings();
	}


	@Override
	public void show(boolean visibility) {
		ui.show(visibility);
	}
	@Override
	public void setVars(String[] strs) {
		width=Integer.parseInt(strs[0]);
		height=Integer.parseInt(strs[1]);
		this.frame_rate=Integer.parseInt(strs[2]);
		this.library= strs[3];
		this.format= strs[4];
		this.cam_num=Integer.parseInt(strs[5]);
	}
}
