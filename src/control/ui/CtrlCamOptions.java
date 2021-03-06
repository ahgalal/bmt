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

import ui.CamOptions;
import utils.PManager;
import utils.PManager.ProgramState.StreamState;
import filters.CommonFilterConfigs;

/**
 * Controller of the CamOptions GUI window.
 * 
 * @author Creative
 */
public class CtrlCamOptions extends ControllerUI<CamOptions> {
	private int		camNum		= 0;
	private String	format		= "YUV";	// @jve:decl-index=0:
	private int		frameRate	= 30;
	private String	library		= "";	// @jve:decl-index=0:
	private int		width		= 640, height = 480;

	/**
	 * Initializes class attributes (CamOptions and PManager).
	 */
	public CtrlCamOptions() {
		pm = PManager.getDefault();
		ui = new CamOptions();
		ui.setController(this);
	}

	/**
	 * Unloads then Loads the video library (JMyron Only, because this action is
	 * only available if the chosen library is JMyron) note: changing cam
	 * properties (brightness, etc ..) in JMyron , will be still effective if
	 * JMF or OpenCV are used after that.
	 */
	public void btnJmyronSettingsAction() {
		unloadAndLoadLibProcedures();
		pm.getVideoManager().displayMoreSettings();
	}

	/**
	 * Unloads then Loads the video library.
	 */
	public void btnOkAction() {
		try {
			/**
			 * this piece of code will be executed in case of 1 or 2: 1. we
			 * haven't chosen JMyron 2. we have chosen JMyron, but haven't set
			 * it's advanced settings
			 */
			unloadAndLoadLibProcedures();
			show(false);
		} catch (final NumberFormatException e1) {
			System.out.print("Error in user input ... aborting !\n");
		}
	}

	@Override
	public boolean setVars(final String[] strs) {
		width = Integer.parseInt(strs[0]);
		height = Integer.parseInt(strs[1]);
		this.frameRate = Integer.parseInt(strs[2]);
		this.library = strs[3];
		this.format = strs[4]; // TODO: remove, it was used by JMF only
		this.camNum = Integer.parseInt(strs[5]);
		return true;
	}

	@Override
	public void show(final boolean visibility) {
		ui.show(visibility);
		(ui).setVidLibs(pm.getVideoManager().getAvailableCamLibs());
	}

	/**
	 * Unloads VideoManager and then Initializes it.
	 */
	public void unloadAndLoadLibProcedures() {
		if(pm.getState().getStream()==StreamState.STREAMING)
			pm.stopStreaming();
		final CommonFilterConfigs commonConfigs = new CommonFilterConfigs(
				width, height, frameRate, camNum, library, format);
		pm.updateCommonConfigs(commonConfigs);
	}
}
