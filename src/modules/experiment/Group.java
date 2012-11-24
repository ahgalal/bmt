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
import java.util.ArrayList;

/**
 * Handles all groups and their info.
 * 
 * @author ShaQ
 */
public class Group implements Grp2GUI, Serializable {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 9105205552189986153L;
	private final ArrayList<Rat>	arr_rats;
	private int						id;
	private String					name;
	private String					notes;

	/**
	 * Initializes the group.
	 * 
	 * @param id
	 *            group's unique id
	 * @param name
	 *            group's name
	 * @param notes
	 *            group's notes
	 */
	public Group(final int id, final String name, final String notes) {
		this.name = name;
		this.notes = notes;
		this.id = id;
		arr_rats = new ArrayList<Rat>();
	}

	/**
	 * Adds new rat to a group.
	 * 
	 * @param new_rat
	 *            reference to a new rat object
	 */
	public void addRat(final Rat new_rat) {
		arr_rats.add(new_rat);
	}

	/**
	 * Gets all rats belonging to the group.
	 * 
	 * @return ArrayList of rats
	 */
	public ArrayList<Rat> getAllRats() {
		return arr_rats;
	}

	/*
	 * (non-Javadoc)
	 * @see model.business.Grp2GUI#getId()
	 */
	@Override
	public int getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see model.business.Grp2GUI#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see model.business.Grp2GUI#getNoRats()
	 */
	@Override
	public int getNoRats() {
		return arr_rats.size();
	}

	/*
	 * (non-Javadoc)
	 * @see model.business.Grp2GUI#getNotes()
	 */
	@Override
	public String getNotes() {
		return notes;
	}

	/**
	 * @param num
	 *            the number of the rat to return its instance
	 * @return instance of the rat having the given number This function loops
	 *         on all the rats in the group and checks the num of each rat if it
	 *         is the required rat we return a reference to that group else
	 *         return null
	 */
	public Rat getRatByNumber(final int num) {
		for (final Rat r_tmp : arr_rats)
			if (Integer.parseInt(r_tmp.getValueByParameterName(Constants.FILE_RAT_NUMBER)) == num)
				return r_tmp;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see model.business.Grp2GUI#getRatsNumbering()
	 */
	@Override
	public String getRatsNumbering() {
		final StringBuffer str_buf = new StringBuffer();
		for (final Rat rat : arr_rats)
			str_buf.append(rat
					.getValueByParameterName(Constants.FILE_RAT_NUMBER) + ", ");
		if (str_buf.length() > 0)
			return str_buf.substring(0, str_buf.length() - 2);
		else
			return "";
	}

	/**
	 * Gets the group's info in the form of a string.
	 * 
	 * @param parameters_list
	 *            list of parameters for rats
	 * @return String containing all the group's information
	 */
	public String grp2String(final String[] parameters_list) {
		// String str_ret = "";
		final StringBuffer str_ret_buf = new StringBuffer();
		str_ret_buf.append(Constants.h_grp
				+ System.getProperty("line.separator"));
		str_ret_buf.append(Constants.h_grp_id + getId()
				+ System.getProperty("line.separator"));
		str_ret_buf.append(Constants.h_grp_name + getName()
				+ System.getProperty("line.separator"));
		str_ret_buf.append(Constants.h_grp_no_rats + getNoRats()
				+ System.getProperty("line.separator"));
		str_ret_buf.append(Constants.h_grp_rats_numbers + getRatsNumbering()
				+ System.getProperty("line.separator"));
		str_ret_buf.append(Constants.h_grp_notes + getNotes()
				+ System.getProperty("line.separator"));
		str_ret_buf.append("" + System.getProperty("line.separator"));
		if (getNoRats() > 0) {
			str_ret_buf.append(Constants.h_rat
					+ System.getProperty("line.separator"));
			final StringBuffer tags_buf = new StringBuffer();
			for (final String s : parameters_list)
				tags_buf.append(s + '\t'); // TODO:tab after the last item ??!!
			str_ret_buf.append(tags_buf.toString()
					+ System.getProperty("line.separator"));

			for (final Rat rat_tmp : getAllRats())
				str_ret_buf.append(rat_tmp.rat2String());
		}
		return str_ret_buf.toString();
	}

	/**
	 * Sets the unique identifier for the group.
	 * 
	 * @param id
	 *            unique identifier
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * Sets the group's name.
	 * 
	 * @param name
	 *            new name of the group
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Sets the notes of the group.
	 * 
	 * @param notes
	 *            new notes of the group
	 */
	public void setNotes(final String notes) {
		this.notes = notes;
	}

}
