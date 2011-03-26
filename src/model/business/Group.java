package model.business;

import java.util.ArrayList;

import utils.saveengines.Constants;

/**
 * @author ShaQ Handles all groups and their info
 */
public class Group implements Grp2GUI
{

	private String name;
	private int id;
	private String rats_numbering;
	private String notes;
	private ArrayList<Rat> arr_rats;

	public Group()
	{

	}

	public Group(
			final int id,
			final String name,
			final String rats_numbering,
			final String notes)
	{
		this.name = name;
		this.rats_numbering = rats_numbering;
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
			if (Integer.parseInt(r_tmp.getValueByMeasurementName("Number")) == num)
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

	public ArrayList<Rat> getAllRats()
	{
		return arr_rats;
	}

	/*
	 * public void saveData(PrintStream p){ try { // Create a new file output
	 * stream // connected to "myfile.txt" p.println("Group Info :");
	 * p.println("Group Name" + '\t' + "Number of Rats" + '\t' +
	 * "Rats Numbering" + '\t' + "Notes"); p.println(name + '\t' + no_rats +
	 * '\t' + rats_numbering + '\t' + notes); p.println("Group Name : " + name);
	 * p.println("Number of Rats : " + no_rats); p.println("Rats Numbering : " +
	 * rats_numbering); p.println("Notes : " + notes); p.close(); } catch
	 * (Exception e) { e.printStackTrace(); } }
	 */

	/*
	 * (non-Javadoc)
	 * @see model.If_Grp2GUI#getName()
	 */
	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see model.If_Grp2GUI#getNo_rats()
	 */
	public int getNoRats()
	{
		return arr_rats.size();
	}

	/*
	 * (non-Javadoc)
	 * @see model.If_Grp2GUI#getRats_numbering()
	 */
	public String getRatsNumbering()
	{
		return rats_numbering;
	}

	public void setRatsNumbering(final String ratsNumbering)
	{
		rats_numbering = ratsNumbering;
	}

	/*
	 * (non-Javadoc)
	 * @see model.If_Grp2GUI#getNotes()
	 */
	public String getNotes()
	{
		return notes;
	}

	public void setNotes(final String notes)
	{
		this.notes = notes;
	}

	public void setId(final int id)
	{
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see model.If_Grp2GUI#getId()
	 */
	public int getId()
	{
		return id;
	}

	public String grp2String(final String[] measurements_list)
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
		// String tags = "";
		final StringBuffer tags_buf = new StringBuffer();
		for (final String s : measurements_list)
			// tags += s + '\t'; // TODO:tab after the last item ??!!
			tags_buf.append(s + '\t');
		str_ret_buf.append(tags_buf.toString() + System.getProperty("line.separator"));

		for (final Rat rat_tmp : getAllRats())
		{
			/*
			 * String values=" "; for(String s:rat_tmp.getValues())
			 * values+=s+'\t'; //TODO:tab after the last item ??!!
			 * str_ret+=values + System.getProperty("line.separator");
			 */
			str_ret_buf.append(rat_tmp.rat2String());
		}
		return str_ret_buf.toString();
	}

}
