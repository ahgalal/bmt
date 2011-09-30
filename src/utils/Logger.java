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
