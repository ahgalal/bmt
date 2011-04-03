package modules.session;

import modules.Cargo;
import modules.Module;
import modules.ModuleConfigs;
import utils.saveengines.Constants;
import utils.video.filters.Data;

/**
 * Manages session's start/end time, etc..
 * 
 * @author Creative
 */
public class SessionModule extends Module
{
	private long session_start_time;
	private long session_end_time;
	private boolean session_is_running;

	private final SessionModuleConfigs session_configs;

	/**
	 * Initializations of the module.
	 * 
	 * @param name
	 *            module instance's name
	 * @param config
	 *            SessionModuleConfigs object to configure the module
	 */
	public SessionModule(final String name, final ModuleConfigs config)
	{
		super(name, config);
		session_configs = (SessionModuleConfigs) config;

		initialize();
	}

	@Override
	public void process()
	{
		if (!session_is_running)
		{
			startSession();
			session_is_running = true;
		}
	}

	@Override
	public void updateGUICargoData()
	{
		gui_cargo.setDataByTag(
				Constants.GUI_SESSION_TIME,
				Float.toString(getSessionTimeTillNow()));
	}

	@Override
	public void updateFileCargoData()
	{
		file_cargo.setDataByTag(
				Constants.FILE_SESSION_TIME,
				Float.toString(getSessionTimeTillNow()));
	}

	@Override
	public void updateConfigs(final ModuleConfigs config)
	{
		session_configs.mergeConfigs(config);
	}

	@Override
	public void registerDataObject(final Data data)
	{
		// we don't need any data from any filter here!
	}

	/**
	 * Saves session's start time.
	 */
	private void startSessionTime()
	{
		session_start_time = System.currentTimeMillis();
	}

	/**
	 * Saves session's end time.
	 */
	private void stopSessionTimer()
	{
		session_end_time = System.currentTimeMillis();
	}

	/**
	 * Gets the total time of the session.
	 * 
	 * @return total time of the session
	 */
	public long getTotalSessionTime()
	{
		final long totalTime = (session_end_time - session_start_time) / (1000);
		return totalTime;
	}

	/**
	 * Gets session's elapsed time till now.
	 * 
	 * @return session's elapsed time till now
	 */
	private float getSessionTimeTillNow()
	{
		final long time = (System.currentTimeMillis() - session_start_time) / (1000);
		return time;
	}

	@Override
	public void initialize()
	{
		session_start_time = 0;
		session_end_time = 0;

		gui_cargo = new Cargo(new String[] { Constants.GUI_SESSION_TIME });

		file_cargo = new Cargo(new String[] { Constants.FILE_SESSION_TIME });
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
		session_is_running = false;
	}

	@Override
	public void deRegisterDataObject(final Data data)
	{

	}

}
