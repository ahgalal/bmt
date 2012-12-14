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

import utils.ExcelWrapper;

/**
 * Responsible for saving Experiment's data to an Excel file.
 * 
 * @author Creative
 */
public class ExcelEngine {

	private final ExcelWrapper	excelWrapper;	// Excel wrapper utility for

	// creating sheets/cells

	/**
	 * Used to ecport Experiment's data to an excel document.
	 */
	public ExcelEngine() {
		excelWrapper = new ExcelWrapper();
	}

	/**
	 * Resets the Excel wrapper.
	 */
	public void reset() {
		excelWrapper.reset();
	}

	/**
	 * Saves the Experiment's information to an Excel file.
	 * 
	 * @param filename
	 *            file path to save the experiment information to
	 * @param exp
	 *            the Experiment to save its information
	 */
	public void writeExpInfoToExcelFile(final String filename,
			final Experiment exp) {
		try {
			excelWrapper.fillRow(-1,
					new String[] { Constants.H_EXP_NAME, exp.getName() });
			excelWrapper.fillRow(-1,
					new String[] { Constants.H_EXP_USER, exp.getUser() });
			excelWrapper.fillRow(-1,
					new String[] { Constants.H_EXP_DATE, exp.getDate() });
			excelWrapper.fillRow(-1,
					new String[] { Constants.H_EXP_NOTES, exp.getNotes() });

			excelWrapper.fillRow(-1, new String[] { "" });

			for (final Group grpTmp : exp.getGroups()) {
				excelWrapper.fillRow(-1, new Object[] { Constants.H_GRP_ID,
						(grpTmp.getId()) });
				excelWrapper.fillRow(-1, new Object[] { Constants.H_GRP_NAME,
						grpTmp.getName() });
				excelWrapper.fillRow(-1, new Object[] {
						Constants.H_GRP_NO_RATS, (grpTmp.getNoRats()) });
				excelWrapper.fillRow(
						-1,
						new Object[] { Constants.H_GRP_RATS_NUMBERS,
								grpTmp.getRatsNumbering() });
				excelWrapper.fillRow(-1, new Object[] { Constants.H_GRP_NOTES,
						grpTmp.getNotes() });

				excelWrapper.fillRow(-1, new Object[] { "" });

				excelWrapper.fillRow(-1, exp.getExpParametersList());
				for (final Rat ratTmp : grpTmp.getAllRats())
					excelWrapper.fillRow(-1, ratTmp.getValues());
			}
			excelWrapper.saveToFile(filename);
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

}
