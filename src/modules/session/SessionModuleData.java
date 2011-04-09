package modules.session;

import utils.video.filters.Data;

/**
 * Data of the Session Module.
 * 
 * @author Creative
 */
public class SessionModuleData extends Data
{
	public long session_start_time;
	public long session_end_time;
	public boolean session_is_running;

	/**
	 * Initializes the Data.
	 * 
	 * @param name
	 *            name of the data instance
	 */
	public SessionModuleData(final String name)
	{
		super(name);
	}

}
