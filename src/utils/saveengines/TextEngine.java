package utils.saveengines;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import utils.PManager;
import utils.StatusManager.StatusSeverity;

import model.business.Experiment;
import model.business.Group;
import model.business.Rat;

/**
 * Responsible for saving/loading Experiment's data to/from Text files.
 * 
 * @author Creative
 */
public class TextEngine
{
	private FileOutputStream out;
	private PrintStream p;

	private FileInputStream fis = null;
	private BufferedInputStream bis = null;

	/**
	 * Loads Experiment's data from a Text file to an Experiment object.
	 * 
	 * @param file_name
	 *            file path to the text file containing the experiment's
	 *            information
	 * @param exp
	 *            Experiment object to load the information to
	 */
	public boolean readExpInfoFromTXTFile(final String file_name, final Experiment exp)
	{
		try
		{
			String tmp_line = null;
			String[] line_data;
			fis = new FileInputStream(file_name);
			bis = new BufferedInputStream(fis);

			final BufferedReader buf_rdr = new BufferedReader(new InputStreamReader(bis));
			boolean group_rats_are_loaded = false;
			Group grp_tmp = null;
			while (buf_rdr.ready())
			{
				if (!group_rats_are_loaded)
					tmp_line = buf_rdr.readLine();
				group_rats_are_loaded = false;
				if (tmp_line.equals(Constants.h_exp))
				{ // load exp. info
					final String tmp_name = buf_rdr.readLine().substring(
							Constants.h_exp_name.length());
					final String tmp_user = buf_rdr.readLine().substring(
							Constants.h_exp_user.length());
					final String tmp_date = buf_rdr.readLine().substring(
							Constants.h_exp_date.length());
					final String tmp_notes = buf_rdr.readLine().substring(
							Constants.h_exp_notes.length());
					exp.setExperimentInfo(tmp_name, tmp_user, tmp_date, tmp_notes);
				} else if (tmp_line.equals(Constants.h_grp))
				{ // load grp. info
					final int tmp_id = Integer.parseInt(buf_rdr.readLine().substring(
							Constants.h_grp_id.length()));
					final String tmp_name = buf_rdr.readLine().substring(
							Constants.h_grp_name.length());
					// int tmp_no_rats =
					Integer.parseInt(buf_rdr.readLine().substring(
							Constants.h_grp_no_rats.length()));
					final String tmp_rats_numbers = buf_rdr.readLine().substring(
							Constants.h_grp_rats_numbers.length());
					final String tmp_notes = buf_rdr.readLine().substring(
							Constants.h_grp_notes.length());
					grp_tmp = new Group(tmp_id, tmp_name, tmp_rats_numbers, tmp_notes);
					exp.addGroup(grp_tmp);
				} else if (tmp_line.equals(Constants.h_rat))
				{
					tmp_line = buf_rdr.readLine();
					line_data = readLineData(tmp_line);
					exp.setMeasurementsList(line_data);
					while (buf_rdr.ready())
					{
						if (!(tmp_line = buf_rdr.readLine()).substring(0, 1).equals("["))
						{
							line_data = readLineData(tmp_line);
							for (int i = 0; i < line_data.length; i++)
								line_data[i] = line_data[i].trim();
							final Rat tmp_rat = new Rat(
									exp.getExpParametersList(),
									line_data);
							grp_tmp.addRat(tmp_rat);
						} else
							break;
					}
					group_rats_are_loaded = true;
				}
			}
			fis.close();
			bis.close();
			return true;
		} catch (final FileNotFoundException e)
		{
			e.printStackTrace();
			PManager.log.print(
					"Error Loading Experiment from file! (File is not found)",
					this,
					StatusSeverity.ERROR);
			return false;
		} catch (final IOException e)
		{
			e.printStackTrace();
			PManager.log.print(
					"Error Loading Experiment from file! (File read error)",
					this,
					StatusSeverity.ERROR);
			return false;
		}
	}

	/**
	 * Parses a line of data into an array of Strings (split char is \t).
	 * 
	 * @param line
	 *            line to parse
	 * @return array of Strings containing line data after splitting
	 */
	private String[] readLineData(final String line)
	{
		final String[] res = line.split("\t");
		return res;
	}

	/**
	 * Saves the Experiment's information to a Text file.
	 * 
	 * @param filename
	 *            file path to save the experiment information to
	 * @param exp
	 *            the Experiment to save its information
	 */
	public void writeExpInfoToTXTFile(final String filename, final Experiment exp)
	{
		try
		{
			out = new FileOutputStream(filename);
		} catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		}
		p = new PrintStream(out);
		try
		{
			p.print(exp.expInfo2String());
		} catch (final Exception e)
		{
			e.printStackTrace();
		}
		p.close();

	}

}
