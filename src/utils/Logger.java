package utils;

import utils.StatusManager.StatusSeverity;

/**
 * Manages System.out.print, for making it easier to print messages o console.
 * 
 * @author Creative
 */
public class Logger
{

	private boolean enabled = true; // is it enabled?

	/**
	 * Prints messages on the screen, along with the caller object name.
	 * 
	 * @param str
	 *            String to print to the console
	 * @param o
	 *            caller object (caller object should send "this")
	 */
	public void print(final String str, final Object o)
	{
		if (enabled)
			System.out.print(o.getClass().toString().substring(6) + " | " + str + "\n");
	}

	/**
	 * Prints messages on the screen, along with the caller object name.
	 * 
	 * @param str
	 *            String to print to the console
	 * @param o
	 *            caller object (caller object should send "this")
	 * @param type
	 *            message type
	 */
	public void print(final String str, final Object o, final StatusSeverity type)
	{
		if (enabled)
			if (type == StatusSeverity.WARNING)
				System.out.print(o.getClass().toString().substring(6)
						+ " | "
						+ str
						+ "\n");
			else if (type == StatusSeverity.ERROR)
				System.err.print(o.getClass().toString().substring(6)
						+ " | "
						+ str
						+ "\n");

	}

	/**
	 * Enables logger.
	 * 
	 * @param enabled
	 *            enable
	 */
	public void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}

}
