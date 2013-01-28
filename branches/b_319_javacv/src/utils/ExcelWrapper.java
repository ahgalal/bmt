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

package utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utils.StatusManager.StatusSeverity;

/**
 * Responsible for dealing with Excel files (Writing only).
 * 
 * @author Creative
 */
public class ExcelWrapper {
	private int				nxtRowIdx;
	private final PManager	pm;
	private Sheet			sheet;
	private Workbook		wb;

	/**
	 * Initialization.
	 */
	public ExcelWrapper() {
		pm = PManager.getDefault();
		init();
	}

	/**
	 * Fills a row with data objects (strings/integers/Floats).
	 * 
	 * @param idx
	 *            index of the row
	 * @param data
	 *            data array of objects
	 */
	public void fillRow(final int idx, final Object[] data) {
		Row row;
		String tmpStr = null;
		Float tmpFloat;
		if (idx == -1)
			row = sheet.createRow(getNextRowNumber());
		else
			row = sheet.getRow(idx);
		if (row != null)
			for (int i = 0; i < data.length; i++) {
				final Cell tmpCell = row.createCell(i);
				if (data[i] instanceof String) {
					tmpStr = (String) data[i];
					tmpCell.setCellValue(tmpStr);
				} else if ((data[i] instanceof Float)
						| (data[i] instanceof Integer)) {
					tmpFloat = (Float.valueOf(data[i].toString()));
					tmpCell.setCellValue(tmpFloat);
				}
			}
		else if (sheet.getPhysicalNumberOfRows() == 0)
			pm.getStatusMgr().setStatus("Excel sheet is Empty!",
					StatusSeverity.ERROR);
		else
			pm.getStatusMgr().setStatus("Row is not found!!", StatusSeverity.ERROR);
	}

	/**
	 * Generates the index of the next row.
	 * 
	 * @return an integer representing the next row index
	 */
	private int getNextRowNumber() {
		nxtRowIdx++;
		return nxtRowIdx - 1;
	}

	/**
	 * Initializes the excel objects.
	 */
	private void init() {
		wb = new XSSFWorkbook();
		sheet = wb.createSheet();
	}

	/**
	 * Reinitialization.
	 */
	public void reset() {
		wb = null;
		sheet = null;
		nxtRowIdx = 0;
		init();
	}

	/**
	 * Saves the worksheet to an .xlsx file.
	 * 
	 * @param filename
	 *            path of the file to save the data to
	 */
	public void saveToFile(final String filename) {
		try {
			final FileOutputStream fos = new FileOutputStream(filename);
			wb.write(fos);
			fos.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
