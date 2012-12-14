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

package modules.session;

import modules.Cargo;
import modules.Module;
import modules.ModuleConfigs;
import modules.ModuleData;
import modules.experiment.Constants;
import modules.experiment.ExperimentType;

import org.eclipse.swt.widgets.Shell;

import ui.PluggedGUI;
import utils.Logger.Details;
import utils.PManager;
import filters.Data;

/**
 * Manages session's start/end time, etc..
 * 
 * @author Creative
 */
public class SessionModule extends
Module<PluggedGUI, SessionModuleConfigs, SessionModuleData> {
	private final String[]	expParams	= new String[] { Constants.FILE_SESSION_TIME };
	private boolean	paused;

	/**
	 * Initializations of the module.
	 * 
	 * @param name
	 *            module instance's name
	 * @param config
	 *            SessionModuleConfigs object to configure the module
	 */
	public SessionModule(final String name, final SessionModuleConfigs config) {
		super(name, config);
		data = new SessionModuleData("Session Module Data");
		initialize();
	}

	@Override
	public boolean amIReady(final Shell shell) {
		return true;
	}

	@Override
	public void deInitialize() {
		endSession();
		data.setSessionRunning(false);
	}

	@Override
	public void deRegisterDataObject(final Data data) {

	}

	@Override
	public void pause() {
		paused=true;
	}

	@Override
	public void resume() {
		startSession();
		paused=false;
	}

	/**
	 * stops session timer.
	 */
	private void endSession() {
		stopSessionTimer();
	}

	/**
	 * Gets session's elapsed time till now.
	 * 
	 * @return session's elapsed time till now
	 */
	private long getSessionTimeTillNow() {
		return data.getAccumulatedSessionTime()/(1000);
	}

	@Override
	public void initialize() {
		PManager.log.print("initializing..", this, Details.VERBOSE);
		data.setSessionStartTime(0);
		data.setSessionEndTime(0);
		data.setAccumulatedSessionTime(0);
		
		guiCargo = new Cargo(new String[] { Constants.GUI_SESSION_TIME });

		fileCargo = new Cargo(expParams);

		for(String param:expParams)
			data.addParameter(param);
		expType = new ExperimentType[] { ExperimentType.OPEN_FIELD,ExperimentType.FORCED_SWIMMING };
	}

	@Override
	public void process() {
		if (!data.isSessionRunning()) {
			startSession();
			data.setSessionRunning(true);
		}
		
		if(paused==false){
			data.setSessionEndTime(System.currentTimeMillis());
			data.setAccumulatedSessionTime((int) (data
					.getAccumulatedSessionTime() + (data.getSessionEndTime() - data.getSessionStartTime())));
			if(data.getSessionStartTime()==0)
				data.setAccumulatedSessionTime(0);
			data.setSessionStartTime(System.currentTimeMillis());
		}
	}

	@Override
	public void registerFilterDataObject(final Data data) {
		// we don't need any data from any filter here!
	}

	@Override
	public void registerModuleDataObject(final ModuleData data) {
		// TODO Auto-generated method stub
	}

	/**
	 * initializes a new session (reset counters) and starts the timer for the
	 * new session.
	 */
	private void startSession() {
		startSessionTime();
	}

	/**
	 * Saves session's start time.
	 */
	private void startSessionTime() {
		data.setSessionStartTime(System.currentTimeMillis());
	}

	/**
	 * Saves session's end time.
	 */
	private void stopSessionTimer() {
		data.setSessionEndTime(System.currentTimeMillis());
	}

	@Override
	public void updateConfigs(final ModuleConfigs config) {
		configs.mergeConfigs(config);
	}

	@Override
	public void updateFileCargoData() {
		fileCargo.setDataByTag(Constants.FILE_SESSION_TIME,
				Float.toString(getSessionTimeTillNow()));
	}

	@Override
	public void updateGUICargoData() {
		guiCargo.setDataByTag(Constants.GUI_SESSION_TIME,
				Float.toString(getSessionTimeTillNow()));
	}

}
