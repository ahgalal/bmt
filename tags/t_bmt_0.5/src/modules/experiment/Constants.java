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
 * Class containing All String constants used by the program.
 * 
 * @author Creative
 */
public interface Constants {

	/**
	 * Text File Header, used when saving/loading experiment to/from text file.
	 */

	String	FILE_ALL_ENTRANCE		= "AZE";
	String	FILE_CENTRAL_ENTRANCE	= "CZE";
	String	FILE_CENTRAL_TIME		= "CT";
	String	FILE_GROUP_NAME			= "Group";
	String	FILE_RAT_NUMBER			= "Number";
	String	FILE_REARING_COUNTER	= "RRNG";

	String	FILE_SESSION_TIME		= "ST";
	String	FILE_TOTAL_DISTANCE		= "Distance";
	String	GUI_ALL_ENTRANCE		= "All Entrance";
	String	GUI_CENTRAL_ENTRANCE	= "Central Entrance";
	String	GUI_CENTRAL_TIME		= "Central Time";
	String	GUI_CURRENT_ZONE		= "Current Zone";

	String	FLOATING	= "Floating";
	String	SWIMMING	= "Swimming";
	String	CLIMBING	= "Climbing";
	/**
	 * GUI Headers, used to Load data to GUI.
	 */
	String	GUI_EXP_NAME			= "Experiment's Name";
	String	GUI_EXP_TYPE			= "Type";
	String	GUI_GROUP_NAME			= "Group's Name";
	String	GUI_RAT_NUMBER			= "Rat Number";
	String	GUI_REARING_COUNTER		= "Rearing Counter";
	String	GUI_SESSION_TIME		= "Session Time";
	String	GUI_TOTAL_DISTANCE		= "Total Distance";
	/**
	 * Experiment Headers:.
	 */
	String	H_EXP					= "[Experiment Info]";
	String	H_EXP_DATE				= "Date: ";

	String	H_EXP_NAME				= "Name: ";
	String	H_EXP_NOTES				= "Notes: ";
	String	H_EXP_TYPES				= "Type: ";
	String	H_EXP_USER				= "User Name: ";
	/**
	 * Group Headers:.
	 */
	String	H_GRP					= "[Group Info]";
	String	H_GRP_ID				= "ID: ";
	String	H_GRP_NAME				= "Name: ";
	String	H_GRP_NO_RATS			= "Number of Rats: ";
	String	H_GRP_NOTES				= "Notes: ";
	String	H_GRP_RATS_NUMBERS		= "Rats' Numbers: ";
	/**
	 * Rat Header:.
	 */
	String	H_RAT					= "[Rat Info]";
	
	/**
	 * ID constants
	 */
	String BMT_ID="bmt";
	String MODULE_ID=BMT_ID+".module";
	String FILTER_ID=BMT_ID+".filter";
	String DATA_ID="data";
	String EXPERIMENT_ID =MODULE_ID+ ".experiment";

}
