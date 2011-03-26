package modules;

import utils.video.filters.Data;

public class SessionModule extends Module
{

	private long session_start_time;
	private long session_end_time;
	private boolean session_is_running;

	private final SessionModuleConfigs session_configs;

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
		gui_cargo.setDataByIndex(0, Float.toString(getSessionTimeTillNow()));
	}

	@Override
	public void updateFileCargoData()
	{
		file_cargo.setDataByIndex(0, Float.toString(getSessionTimeTillNow()));
	}

	@Override
	public void updateConfigs(final ModuleConfigs config)
	{
		session_configs.mergeConfigs(config);
	}

	@Override
	public void updateDataObject(final Data data)
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

	public long getTotalSessionTime()
	{
		final long totalTime = (session_end_time - session_start_time) / (1000);// total
		// time
		// in
		// seconds
		return totalTime;
	}

	private float getSessionTimeTillNow()
	{
		final long time = (System.currentTimeMillis() - session_start_time) / (1000);// time
		// in
		// seconds
		return time;
	}

	@Override
	public void initialize()
	{
		session_start_time = 0;
		session_end_time = 0;

		gui_cargo = new Cargo(new String[] { "Session Time" });

		file_cargo = new Cargo(new String[] { "ST" });
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

}
