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

package modules;

import java.io.Serializable;

/**
 * Data carrier, contains a set of tags and data corresponding to those tags.
 * 
 * @author Creative
 */
public class Cargo implements Serializable {
	/**
     * 
     */
	private static final long	serialVersionUID	= 271285755448822703L;
	private String[]			data;
	private String[]			tags;

	/**
	 * Initializes the cargo.
	 * 
	 * @param tags
	 *            a.k.a parameters/keys
	 */
	public Cargo(final String[] tags) {
		data = new String[tags.length];
		this.tags = tags;
	}

	/**
	 * Gets the data in the form of an array.
	 * 
	 * @return array containing the cargo's data
	 */
	public String[] getData() {
		return data;
	}

	/**
	 * Gets the data from the cargo using its index in the array of data.
	 * 
	 * @param index
	 *            index to get the data of
	 * @return data at the index from the data array
	 */
	public String getDataByIndex(final int index) {
		return data[index];
	}

	/**
	 * Gets the data from the cargo using the tag's name.
	 * 
	 * @param tag
	 *            name of the tag to get the data corresponding to
	 * @return data corresponding to the tag
	 */
	public String getDataByTag(final String tag) {
		final int index = getIndexByTag(tag);
		return data[index];
	}

	/**
	 * Gets the index of a tag using the tag's name.
	 * 
	 * @param tag
	 *            name of the tag
	 * @return index of the tag's name in the tags arrray
	 */
	private int getIndexByTag(final String tag) {
		for (int i = 0; i < tags.length; i++)
			if (tags[i].equals(tag))
				return i;
		return -1;
	}

	/**
	 * Gets the cargo's tags' names.
	 * 
	 * @return array of Strings containing tags' names
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * Sets the data of the Cargo.
	 * 
	 * @param data
	 *            data as an array of strings (order should be taken into
	 *            consideration with respect to the order of the tags)
	 */
	public void setData(final String[] data) {
		this.data = data;
	}

	/**
	 * Sets the data inside the cargo using the index of the data in the data
	 * array.
	 * 
	 * @param index
	 *            data index inside the data's array
	 * @param data
	 *            new data value
	 */
	public void setDataByIndex(final int index, final String data) {
		this.data[index] = data;
	}

	/**
	 * Sets the data inside the cargo using the tag's name.
	 * 
	 * @param tag
	 *            tag's name to set the data corresponding to it
	 * @param data
	 *            new data value
	 */
	public void setDataByTag(final String tag, final String data) {
		this.data[getIndexByTag(tag)] = data;
	}

	/**
	 * Sets the tags of the Cargo.
	 * 
	 * @param tags
	 *            Tags as an array of strings
	 */
	public void setTags(final String[] tags) {
		this.tags = tags;
	}

}
