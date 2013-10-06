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

import java.io.Serializable;

import modules.Cargo;
import utils.PManager;
import utils.StatusManager.StatusSeverity;

/**
 * Handles rat's info.
 * 
 * @author ShaQ
 */
public class Rat implements Serializable {
	/**
     * 
     */
	private static final long	serialVersionUID	= -6143229970194595747L;
	private final Cargo			info;

	/**
	 * Initializes the rat object.
	 * 
	 * @param paramatersList
	 *            list of parameters measured in the experiment for each rat
	 */
	public Rat(final String[] paramatersList) {
		info = new Cargo(paramatersList);
	}

	/**
	 * Initializes the rat object.
	 * 
	 * @param paramatersList
	 *            list of parameters measured in the experiment for each rat
	 * @param values
	 *            values of the parameters in the list
	 */
	public Rat(final String[] paramatersList, final String[] values) {
		this(paramatersList);
		info.setData(values);
	}

	/**
	 * Gets the value of the parameter, using parameter's name.
	 * 
	 * @param parameterName
	 *            name of the parameter to get the value corresponding to it
	 * @return String containing the value corresponding to the parameter
	 *         specified
	 */
	public String getValueByParameterName(final String parameterName) {
		return info.getDataByTag(parameterName);
	}

	/**
	 * Gets the values of parameters for this rat.
	 * 
	 * @return parameters' values
	 */
	public String[] getValues() {
		return info.getData();
	}

	/**
	 * Converts the rat object, with all the info it holds into a printable
	 * string.
	 * 
	 * @return String containing the rat's info
	 */
	public String rat2String() {
		// String values = " ";
		final StringBuffer valuesBuf = new StringBuffer();
		for (final String s : getValues())
			valuesBuf.append(s + '\t'); // TODO:tab after the last item ??!!
		valuesBuf.append(System.getProperty("line.separator"));
		return valuesBuf.toString();
	}

	/**
	 * Sets the value of a parameter using the parameter name.
	 * 
	 * @param parameterName
	 *            name of the parameter to set its value
	 * @param value
	 *            new value of the parameter
	 * @return success: true
	 */
	public boolean setValueByParameterName(final String parameterName,
			final String value) {
		try {
			info.setDataByTag(parameterName, value);
		} catch (final Exception e) {
			PManager.log.print("Error in parameter name!", this,
					StatusSeverity.ERROR);
			return false;
		}
		return true;
	}
}
