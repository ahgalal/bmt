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
import modules.experiment.Constants;

import org.eclipse.swt.widgets.Shell;

import ui.PluggedGUI;
import utils.video.filters.Data;

/**
 * Manages session's start/end time, etc..
 * 
 * @author Creative
 */
public class SessionModule extends Module<PluggedGUI, SessionModuleConfigs,SessionModuleData>
{
	/**
	 * Initializations of the module.
	 * 
	 * @param name
	 *            module instance's name
	 * @param config
	 *            SessionModuleConfigs object to configure the module
	 */
	public SessionModule(final String name, final SessionModuleConfigs config)
	{
		super(name, config);
		data = new SessionModuleData("Session Module Data");
		initialize();
	}

	@Override
	public void process()
	{
		if (!data.session_is_running)
		{
			startSession();
			data.session_is_running = true;
		}
	}

	@Override
	public void updateGUICargoData()
	{
		guiCargo.setDataByTag(
				Constants.GUI_SESSION_TIME,
				Float.toString(getSessionTimeTillNow()));
	}

	@Override
	public void updateFileCargoData()
	{
		fileCargo.setDataByTag(
				Constants.FILE_SESSION_TIME,
				Float.toString(getSessionTimeTillNow()));
	}

	@Override
	public void updateConfigs(final ModuleConfigs config)
	{
		configs.mergeConfigs(config);
	}

	@Override
	public void registerFilterDataObject(final Data data)
	{
		// we don't need any data from any filter here!
	}

	/**
	 * Saves session's start time.
	 */
	private void startSessionTime()
	{
		data.session_start_time = System.currentTimeMillis();
	}

	/**
	 * Saves session's end time.
	 */
	private void stopSessionTimer()
	{
		data.session_end_time = System.currentTimeMillis();
	}

	/**
	 * Gets the total time of the session.
	 * 
	 * @return total time of the session
	 */
	public long getTotalSessionTime()
	{
		final long totalTime = (data.session_end_time - data.session_start_time) / (1000);
		return totalTime;
	}

	/**
	 * Gets session's elapsed time till now.
	 * 
	 * @return session's elapsed time till now
	 */
	private float getSessionTimeTillNow()
	{
		final long time = (System.currentTimeMillis() - data.session_start_time) / (1000);
		return time;
	}

	@Override
	public void initialize()
	{
		data.session_start_time = 0;
		data.session_end_time = 0;

		guiCargo = new Cargo(new String[] { Constants.GUI_SESSION_TIME });

		fileCargo = new Cargo(new String[] { Constants.FILE_SESSION_TIME });
	}

	/**
	 * initializes a new session (reset counters) and starts the timer for the
	 * new session.
	 */
	private void startSession()
	{

		startSessionTime();
	}

	/**
	 * stops session timer.
	 */
	private void endSession()
	{
		stopSessionTimer();
	}

	@Override
	public void deInitialize()
	{
		endSession();
		data.session_is_running = false;
	}

	@Override
	public void deRegisterDataObject(final Data data)
	{

	}

	@Override
	public boolean amIReady(final Shell shell)
	{
		return true;
	}

	@Override
	public void registerModuleDataObject(final Data data)
	{
		// TODO Auto-generated method stub
	}

}
