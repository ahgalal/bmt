package modules.experiment;

import java.util.ArrayList;

import utils.saveengines.Constants;

/**
 * Handles all groups and their info
 * 
 * @author ShaQ
 */
public class Group implements Grp2GUI
{

	private String name;
	private int id;
	private String notes;
	private final ArrayList<Rat> arr_rats;

	/**
	 * Initializes the group.
	 * @param id group's unique id
	 * @param name group's name
	 * @param notes group's notes
	 */
	public Group(
			final int id,
			final String name,
			final String notes)
	{
		this.name = name;
		this.notes = notes;
		this.id = id;
		arr_rats = new ArrayList<Rat>();
	}

	/**
	 * @param num
	 *            the number of the rat to return its instance
	 * @return instance of the rat having the given number This function loops
	 *         on all the rats in the group and checks the num of each rat if it
	 *         is the required rat we return a reference to that group else
	 *         return null
	 */
	public Rat getRatByNumber(final int num)
	{
		for (final Rat r_tmp : arr_rats)
		{
			if (Integer.parseInt(r_tmp.getValueByParameterName("Number")) == num)
				return r_tmp;
		}
		return null;
	}

	/**
	 * Adds new rat to a group.
	 * 
	 * @param new_rat
	 *            reference to a new rat object
	 */
	public void addRat(final Rat new_rat)
	{
		arr_rats.add(new_rat);
	}

	/**
	 * Gets all rats belonging to the group
	 * @return ArrayList of rats
	 */
	public ArrayList<Rat> getAllRats()
	{
		return arr_rats;
	}

	/* (non-Javadoc)
	 * @see model.business.Grp2GUI#getName()
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the group's name.
	 * @param name new name of the group
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see model.business.Grp2GUI#getNoRats()
	 */
	public int getNoRats()
	{
		return arr_rats.size();
	}

	/* (non-Javadoc)
	 * @see model.business.Grp2GUI#getRatsNumbering()
	 */
	public String getRatsNumbering()
	{
		StringBuffer str_buf = new StringBuffer();
		for(Rat rat:arr_rats)
			str_buf.append(rat.getValueByParameterName(ExperimentModule.FILE_RAT_NUMBER)+ ", ") ;
		return str_buf.substring(0, str_buf.length()-2);
	}

	/* (non-Javadoc)
	 * @see model.business.Grp2GUI#getNotes()
	 */
	public String getNotes()
	{
		return notes;
	}

	/**
	 * Sets the notes of the group.
	 * @param notes new notes of the group
	 */
	public void setNotes(final String notes)
	{
		this.notes = notes;
	}

	/**
	 * Sets the unique identifier for the group.
	 * @param id unique identifier
	 */
	public void setId(final int id)
	{
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see model.business.Grp2GUI#getId()
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Gets the group's info in the form of a string.
	 * @param parameters_list list of parameters for rats
	 * @return
	 */
	public String grp2String(final String[] parameters_list)
	{
		// String str_ret = "";
		final StringBuffer str_ret_buf = new StringBuffer();
		str_ret_buf.append(Constants.h_grp + System.getProperty("line.separator"));
		str_ret_buf.append(Constants.h_grp_id
				+ getId()
				+ System.getProperty("line.separator"));
		str_ret_buf.append(Constants.h_grp_name
				+ getName()
				+ System.getProperty("line.separator"));
		str_ret_buf.append(Constants.h_grp_no_rats
				+ getNoRats()
				+ System.getProperty("line.separator"));
		str_ret_buf.append(Constants.h_grp_rats_numbers
				+ getRatsNumbering()
				+ System.getProperty("line.separator"));
		str_ret_buf.append(Constants.h_grp_notes
				+ getNotes()
				+ System.getProperty("line.separator"));
		str_ret_buf.append("" + System.getProperty("line.separator"));
		str_ret_buf.append(Constants.h_rat + System.getProperty("line.separator"));
		final StringBuffer tags_buf = new StringBuffer();
		for (final String s : parameters_list)
			tags_buf.append(s + '\t');	// TODO:tab after the last item ??!!
		str_ret_buf.append(tags_buf.toString() + System.getProperty("line.separator"));

		for (final Rat rat_tmp : getAllRats())
			str_ret_buf.append(rat_tmp.rat2String());
		return str_ret_buf.toString();
	}

}
