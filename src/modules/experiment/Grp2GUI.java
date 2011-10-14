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

/**
 * Gives the GUI what it want to see of an Group Object.
 * 
 * @author Creative
 */
public interface Grp2GUI
{
	/**
	 * Gets the name of the group.
	 * 
	 * @return String containing the group's name
	 */
	String getName();

	/**
	 * Gets the number of rats in the group.
	 * 
	 * @return integer representing the number of rats in the group
	 */
	int getNoRats();

	/**
	 * Gets number of rats already exprimented in that group.
	 * 
	 * @return String containing rats numbers, separated by a separator
	 *         character
	 */
	String getRatsNumbering();

	/**
	 * Gets the group's notes.
	 * 
	 * @return String containing the notes on the group
	 */
	String getNotes();

	/**
	 * Gets the group unique id.
	 * 
	 * @return integer representing the unique group id
	 */
	int getId();

}