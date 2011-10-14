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

package modules.experiment;

import modules.Cargo;
import utils.PManager;
import utils.StatusManager.StatusSeverity;

/**
 * Handles rat's info.
 * 
 * @author ShaQ
 */
public class Rat
{
	private final Cargo info;

	/**
	 * Initializes the rat object.
	 * 
	 * @param paramaters_list
	 *            list of parameters measured in the experiment for each rat
	 * @param values
	 *            values of the parameters in the list
	 */
	public Rat(final String[] paramaters_list, final String[] values)
	{
		this(paramaters_list);
		info.setData(values);
	}

	/**
	 * Initializes the rat object.
	 * 
	 * @param paramaters_list
	 *            list of parameters measured in the experiment for each rat
	 */
	public Rat(final String[] paramaters_list)
	{
		info = new Cargo(paramaters_list);
	}

	/**
	 * Gets the values of parameters for this rat.
	 * 
	 * @return parameters' values
	 */
	public String[] getValues()
	{
		return info.getData();
	}

	/**
	 * Gets the value of the parameter, using parameter's name.
	 * 
	 * @param parameter_name
	 *            name of the parameter to get the value corresponding to it
	 * @return String containing the value corresponding to the parameter
	 *         specified
	 */
	public String getValueByParameterName(final String parameter_name)
	{
		return info.getDataByTag(parameter_name);
	}

	/**
	 * Sets the value of a parameter using the parameter name.
	 * 
	 * @param parameter_name
	 *            name of the parameter to set its value
	 * @param value
	 *            new value of the parameter
	 * @return success: true
	 */
	public boolean setValueByParameterName(final String parameter_name, final String value)
	{
		try
		{
			info.setDataByTag(parameter_name, value);
		} catch (final Exception e)
		{
			PManager.log.print("Error in parameter name!", this, StatusSeverity.ERROR);
			return false;
		}
		return true;
	}

	/**
	 * Converts the rat object, with all the info it holds into a printable
	 * string.
	 * 
	 * @return String containing the rat's info
	 */
	public String rat2String()
	{
		// String values = " ";
		final StringBuffer values_buf = new StringBuffer();
		for (final String s : getValues())
			values_buf.append(s + '\t'); // TODO:tab after the last item ??!!
		values_buf.append(System.getProperty("line.separator"));
		return values_buf.toString();
	}
}
