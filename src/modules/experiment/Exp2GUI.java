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
 * Gives the GUI what it want to see of an Experiment Object.
 * 
 * @author Creative
 */
public interface Exp2GUI {

	/**
	 * gets experiment's creation date.
	 * 
	 * @return String containing the experiment's creation date.
	 */
	String getDate();

	/**
	 * gets experiment's name.
	 * 
	 * @return String containing the experiment's name.
	 */
	String getName();

	/**
	 * gets experiment's notes.
	 * 
	 * @return String containing the experiment's notes.
	 */
	String getNotes();

	/**
	 * gets experiment's user name.
	 * 
	 * @return String containing the experiment's user name.
	 */
	String getUser();
	
	ExperimentType getType();

}