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