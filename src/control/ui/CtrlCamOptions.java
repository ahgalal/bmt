/***************************************************************************
 *  Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly,
 *  Sarah Hamid and Mohammed Ahmed Ramadan
 *  contact: ceng.ahmedgalal@gmail.com
 *
 *  This file is part of Behavioral Monitoring Tool.
 *
 *  Behavioral Monitoring Tool is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, version 3 of the
 *  License.
 *
 *  Behavioral Monitoring Tool is distributed in the hope that it
 *  will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Behavioral Monitoring Tool.
 *  If not, see <http://www.gnu.org/licenses/>.
 *   
 **************************************************************************/

package control.ui;

import modules.ModuleConfigs;
import modules.ModulesManager;
import modules.zones.ZonesModuleConfigs;
import ui.CamOptions;
import utils.PManager;
import utils.video.VideoManager;
import utils.video.filters.CommonFilterConfigs;

/**
 * Controller of the CamOptions GUI window.
 * 
 * @author Creative
 */
public class CtrlCamOptions extends ControllerUI
{
	private String library = "JMF"; // @jve:decl-index=0:
	// private String prev_library="";
	private String format = "YUV"; // @jve:decl-index=0:
	private int width = 640, height = 480;
	private int frame_rate = 30;
	private int cam_num = 0;
	private final CamOptions ui;

	// private boolean lib_is_already_created;

	/**
	 * Initializes class attributes (CamOptions and PManager).
	 */
	public CtrlCamOptions()
	{
		pm = PManager.getDefault();
		ui = new CamOptions();
		ui.setController(this);
	}

	/**
	 * Unloads then Loads the video library.
	 */
	public void btnOkAction()
	{
		try
		{
			/**
			 * this piece of code will be executed in case of 1 or 2: 1. we
			 * haven't chosen JMyron 2. we have chosen JMyron, but haven't set
			 * it's advanced settings
			 */
			unloadAndLoadLibProcedures();
			show(false);
		} catch (final NumberFormatException e1)
		{
			System.out.print("Error in user input ... aborting !\n");
		}
	}

	/**
	 * Unloads VideoManager and then Initializes it.
	 */
	public void unloadAndLoadLibProcedures()
	{
		pm.stopStreaming();
		final CommonFilterConfigs common_configs = new CommonFilterConfigs(
				width,
				height,
				frame_rate,
				cam_num,
				library,
				format);
		pm.initializeVideoManager(common_configs);
		pm.startStreaming();
		PManager.main_gui.configureScreenDrawerFilter("ScreenDrawer", null, true);
		ModulesManager.getDefault().updateModuleConfigs(
				new ModuleConfigs[] { new ZonesModuleConfigs(
						"Zones Module",
						-1,
						width,
						height) });
	}

	/**
	 * Unloads then Loads the video library (JMyron Only, because this action is
	 * only available if the chosen library is JMyron) note: changing cam
	 * properties (brightness, etc ..) in JMyron , will be still effective if
	 * JMF or OpenCV are used after that.
	 */
	public void btnJmyronSettingsAction()
	{
		unloadAndLoadLibProcedures();
		pm.getVideoManager().displayMoreSettings();
	}

	@Override
	public void show(final boolean visibility)
	{
		ui.show(visibility);
		((CamOptions)ui).setVidLibs(pm.getVideoManager().getAvailableVidLibs());
	}

	@Override
	public boolean setVars(final String[] strs)
	{
		width = Integer.parseInt(strs[0]);
		height = Integer.parseInt(strs[1]);
		this.frame_rate = Integer.parseInt(strs[2]);
		this.library = strs[3];
		this.format = strs[4];
		this.cam_num = Integer.parseInt(strs[5]);
		return true;
	}
}
