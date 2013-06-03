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
	private final ArrayList<Rat>	rats;
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
		rats = new ArrayList<Rat>();
	}

	/**
	 * Adds new rat to a group.
	 * 
	 * @param newRat
	 *            reference to a new rat object
	 */
	public void addRat(final Rat newRat) {
		rats.add(newRat);
	}

	/**
	 * Gets all rats belonging to the group.
	 * 
	 * @return ArrayList of rats
	 */
	public ArrayList<Rat> getAllRats() {
		return rats;
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
		return rats.size();
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
		for (final Rat ratTmp : rats)
			if (Integer.parseInt(ratTmp.getValueByParameterName(Constants.FILE_RAT_NUMBER)) == num)
				return ratTmp;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see model.business.Grp2GUI#getRatsNumbering()
	 */
	@Override
	public String getRatsNumbering() {
		final StringBuffer strBuf = new StringBuffer();
		for (final Rat rat : rats)
			strBuf.append(rat
					.getValueByParameterName(Constants.FILE_RAT_NUMBER) + ", ");
		if (strBuf.length() > 0)
			return strBuf.substring(0, strBuf.length() - 2);
		else
			return "";
	}

	/**
	 * Gets the group's info in the form of a string.
	 * 
	 * @param parametersList
	 *            list of parameters for rats
	 * @return String containing all the group's information
	 */
	public String grp2String(final String[] parametersList) {
		final StringBuffer strRetBuf = new StringBuffer();
		strRetBuf.append(Constants.H_GRP
				+ System.getProperty("line.separator"));
		strRetBuf.append(Constants.H_GRP_ID + getId()
				+ System.getProperty("line.separator"));
		strRetBuf.append(Constants.H_GRP_NAME + getName()
				+ System.getProperty("line.separator"));
		strRetBuf.append(Constants.H_GRP_NO_RATS + getNoRats()
				+ System.getProperty("line.separator"));
		strRetBuf.append(Constants.H_GRP_RATS_NUMBERS + getRatsNumbering()
				+ System.getProperty("line.separator"));
		strRetBuf.append(Constants.H_GRP_NOTES + getNotes()
				+ System.getProperty("line.separator"));
		strRetBuf.append("" + System.getProperty("line.separator"));
		if (getNoRats() > 0) {
			strRetBuf.append(Constants.H_RAT
					+ System.getProperty("line.separator"));
			final StringBuffer tagsBuf = new StringBuffer();
			for (final String s : parametersList)
				tagsBuf.append(s + '\t'); // TODO:tab after the last item ??!!
			strRetBuf.append(tagsBuf.toString()
					+ System.getProperty("line.separator"));

			for (final Rat ratTmp : getAllRats())
				strRetBuf.append(ratTmp.rat2String());
		}
		return strRetBuf.toString();
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
