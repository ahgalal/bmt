package utils.saveengines;

/**
 * Class containing All String constants used by the program.
 * 
 * @author Creative
 */
public class Constants
{

	/**
	 * Text File Header, used when saving/loading experiment to/from text file.
	 */

	/**
	 * Experiment Headers:.
	 */
	public static final String h_exp = "[Experiment Info]";
	public static final String h_exp_name = "Name: ";
	public static final String h_exp_user = "User Name: ";
	public static final String h_exp_date = "Date: ";
	public static final String h_exp_notes = "Notes: ";

	/**
	 * Group Headers:.
	 */
	public static final String h_grp = "[Group Info]";
	public static final String h_grp_id = "ID: ";
	public static final String h_grp_name = "Name: ";
	public static final String h_grp_no_rats = "Number of Rats: ";
	public static final String h_grp_rats_numbers = "Rats' Numbers: ";
	public static final String h_grp_notes = "Notes: ";

	/**
	 * Rat Header:.
	 */
	public static final String h_rat = "[Rat Info]";
	public static final String FILE_RAT_NUMBER = "Number";
	public static final String FILE_GROUP_NAME = "Group";
	public static final String FILE_REARING_COUNTER = "RRNG";
	public static final String FILE_SESSION_TIME = "ST";
	public static final String FILE_ALL_ENTRANCE = "AZE";
	public static final String FILE_CENTRAL_ENTRANCE = "CZE";
	public static final String FILE_CENTRAL_TIME = "CT";
	public static final String FILE_TOTAL_DISTANCE = "Distance";

	
	/**
	 * GUI Headers, used to Load data to GUI.
	 */
	public static final String GUI_EXP_NAME = "Experiment's Name";
	public static final String GUI_GROUP_NAME = "Group's Name";
	public static final String GUI_RAT_NUMBER = "Rat Number";
	public static final String GUI_REARING_COUNTER = "Rearing Counter";
	public static final String GUI_SESSION_TIME = "Session Time";
	public static final String GUI_CURRENT_ZONE = "Current Zone";
	public static final String GUI_ALL_ENTRANCE = "All Entrance";
	public static final String GUI_CENTRAL_ENTRANCE = "Central Entrance";
	public static final String GUI_CENTRAL_TIME = "Central Time";
	public static final String GUI_TOTAL_DISTANCE = "Total Distance";

}
