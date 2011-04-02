package model.business;

import java.util.ArrayList;

import utils.saveengines.Constants;

/**
 * Handles all the experiments info.
 * 
 * @author ShaQ
 */
public class Experiment implements Exp2GUI
{

	private final ArrayList<model.business.Group> groups;
	private String name;
	private String user;
	private String date;
	private String notes;

	private String[] exp_params;

	/**
	 * Clears the experiment info(data) This functions loops on all the groups
	 * in the experiment and for each group it calls getAllRats() method and
	 * clear their data then clears the group's data by calling clear() method.
	 */
	public void clearExperimentData()
	{
		for (final Group g : groups)
			g.getAllRats().clear();
		groups.clear();
	}

	/**
	 * @param id
	 *            the id of the group to return its instance
	 * @return instance of the group having the given id This function loops on
	 *         all the groups in the experiment and checks the ID of each group
	 *         if it is the required group we return a reference to that group
	 *         else return null
	 */
	public Group getGroupByID(final int id)
	{
		for (final Group tmp_g : groups)
		{
			if (tmp_g.getId() == id)
				return tmp_g;
		}
		return null;
	}

	/**
	 * Initializes the experiment.
	 */
	public Experiment()
	{
		groups = new ArrayList<Group>();
	}

	/**
	 * Sets the info of an experiment.
	 * 
	 * @param name
	 *            name of the experiment
	 * @param user
	 *            user name who makes the experiment
	 * @param date2
	 *            date of the experiment
	 * @param notes
	 *            any additional notes about the experiment
	 */
	public void setExperimentInfo(
			final String name,
			final String user,
			final String date2,
			final String notes)
	{
		this.name = name;
		this.user = user;
		this.date = date2;
		this.notes = notes;
	}

	/**
	 * Adds a new group to the experiment groups by calling the add() method.
	 * 
	 * @param g
	 *            reference to a group to add to the experiment
	 */
	public void addGroup(final Group g)
	{
		groups.add(g);
	}

	/* (non-Javadoc)
	 * @see model.business.Exp2GUI#getName()
	 */
	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see model.business.Exp2GUI#getUser()
	 */
	public String getUser()
	{
		return user;
	}

	public void setUser(final String user)
	{
		this.user = user;
	}

	/* (non-Javadoc)
	 * @see model.business.Exp2GUI#getDate()
	 */
	public String getDate()
	{
		return date;
	}

	/* (non-Javadoc)
	 * @see model.business.Exp2GUI#getNotes()
	 */
	public String getNotes()
	{
		return notes;
	}

	public ArrayList<model.business.Group> getGroups()
	{
		return groups;
	}

	public Group getGroupByName(final String name)
	{
		for (final Group tmp_g : groups)
		{
			if (tmp_g.getName().equals(name))
				return tmp_g;
		}
		return null;
	}

	/**
	 * @return the number of groups in the experiment
	 */
	public int getNoGroups()
	{
		return groups.size();
	}

	public void setMeasurementsList(final String[] exp_parameters)
	{
		exp_params = exp_parameters;
	}

	public String[] getExpParametersList()
	{
		return exp_params;
	}

	public String expInfo2String()
	{
		final StringBuffer exp_info_buf = new StringBuffer();

		try
		{ // TODO: replace System.getProperty("line.separator") by an endl
			// String
			exp_info_buf.append(Constants.h_exp
					+ System.getProperty("line.separator")
					+ Constants.h_exp_name
					+ getName()
					+ System.getProperty("line.separator")
					+ Constants.h_exp_user
					+ getUser()
					+ System.getProperty("line.separator")
					+ Constants.h_exp_date
					+ getDate()
					+ " "
					+ System.getProperty("line.separator")
					+ Constants.h_exp_notes
					+ getNotes()
					+ System.getProperty("line.separator"));

			for (final Group grp_tmp : getGroups())
				exp_info_buf.append(grp_tmp.grp2String(getExpParametersList()));

		} catch (final Exception e)
		{
			e.printStackTrace();
		}

		return exp_info_buf.toString();
	}

}
