/***************************************************************************
 *  Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly,
 *  Sarah Hamid and Mohammed Ahmed Ramadan
 *  contact: ceng.ahmedgalal@gmail.com
 *
 *  This file is part of Behavioral Monitoring Tool.
 *
 *  Behavioral Monitoring Tool is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, version 3 of the
 *  License.
 *
 *  Behavioral Monitoring Tool is distributed in the hope that it
 *  will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Behavioral Monitoring Tool.
 *  If not, see <http://www.gnu.org/licenses/>.
 *   
 **************************************************************************/

package modules.experiment;

import utils.ExcelWrapper;

/**
 * Responsible for saving Experiment's data to an Excel file.
 * 
 * @author Creative
 */
public class ExcelEngine
{

	private final ExcelWrapper excel_wrapper; // Excel wrapper utility for

	// creating sheets/cells

	/**
	 * Used to ecport Experiment's data to an excel document.
	 */
	public ExcelEngine()
	{
		excel_wrapper = new ExcelWrapper();
	}

	/**
	 * Saves the Experiment's information to an Excel file.
	 * 
	 * @param filename
	 *            file path to save the experiment information to
	 * @param exp
	 *            the Experiment to save its information
	 */
	public void writeExpInfoToExcelFile(final String filename, final Experiment exp)
	{
		try
		{
			excel_wrapper.fillRow(
					-1,
					new String[] { Constants.h_exp_name, exp.getName() });
			excel_wrapper.fillRow(
					-1,
					new String[] { Constants.h_exp_user, exp.getUser() });
			excel_wrapper.fillRow(
					-1,
					new String[] { Constants.h_exp_date, exp.getDate() });
			excel_wrapper.fillRow(-1, new String[] { Constants.h_exp_notes,
					exp.getNotes() });

			excel_wrapper.fillRow(-1, new String[] { "" });

			for (final Group grp_tmp : exp.getGroups())
			{
				excel_wrapper.fillRow(-1, new Object[] { Constants.h_grp_id,
						(grp_tmp.getId()) });
				excel_wrapper.fillRow(-1, new Object[] { Constants.h_grp_name,
						grp_tmp.getName() });
				excel_wrapper.fillRow(-1, new Object[] { Constants.h_grp_no_rats,
						(grp_tmp.getNoRats()) });
				excel_wrapper.fillRow(-1, new Object[] { Constants.h_grp_rats_numbers,
						grp_tmp.getRatsNumbering() });
				excel_wrapper.fillRow(-1, new Object[] { Constants.h_grp_notes,
						grp_tmp.getNotes() });

				excel_wrapper.fillRow(-1, new Object[] { "" });

				excel_wrapper.fillRow(-1, exp.getExpParametersList());
				for (final Rat rat_tmp : grp_tmp.getAllRats())
				{
					excel_wrapper.fillRow(-1, rat_tmp.getValues());
				}
			}
			excel_wrapper.saveToFile(filename);
		} catch (final Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Resets the Excel wrapper.
	 */
	public void reset()
	{
		excel_wrapper.reset();
	}

}
