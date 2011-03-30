package model.business;

/**
 * Gives the GUI what it want to see of an Experiment Object.
 * 
 * @author Creative
 */
public interface Exp2GUI
{

	/**
	 * gets experiment's name.
	 * @return String containing the experiment's name.
	 */
	String getName();
	
	/**
	 * gets experiment's user name.
	 * @return String containing the experiment's user name.
	 */
	String getUser();

	/**
	 * gets experiment's creation date.
	 * @return String containing the experiment's creation date.
	 */
	String getDate();

	/**
	 * gets experiment's notes.
	 * @return String containing the experiment's notes.
	 */
	String getNotes();

}