package utils;

import utils.StatusManager.StatusSeverity;

/**
 * Manages System.out.print, for making it easier to print messages o console.
 * @author Creative
 */
public class Logger {

	private boolean enabled=true;		//is it enabled?

	public void print(String str,Object o)
	{
		if(enabled)
			System.out.print(o.getClass().toString().substring(6) +" | "+str+"\n");
	}

	/**
	 * Prints message to console.
	 * @param str message string
	 * @param o object that sends the message
	 * @param type message type
	 */
	public void print(String str,Object o,StatusSeverity type)
	{
		if(enabled)
			if(type==StatusSeverity.WARNING)
				System.out.print(o.getClass().toString().substring(6) +" | "+str+"\n");
			else if(type==StatusSeverity.ERROR)
				System.err.print(o.getClass().toString().substring(6) +" | "+str+"\n");

	}

	/**
	 * Enables logger
	 * @param enabled enable
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
