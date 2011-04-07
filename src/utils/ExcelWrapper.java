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
public class ExcelWrapper
{
	private int nxt_row_idx;
	private final PManager pm;
	private Sheet sheet;
	private Workbook wb;

	/**
	 * Initialization.
	 */
	public ExcelWrapper()
	{
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
	public void fillRow(final int idx, final Object[] data)
	{
		Row row;
		String tmp_str = null;
		Float tmp_float;
		if (idx == -1)
			row = sheet.createRow(getNextRowNumber());
		else
			row = sheet.getRow(idx);
		if (row != null)
		{
			for (int i = 0; i < data.length; i++)
			{
				final Cell tmp_cell = row.createCell(i);
				if (data[i] instanceof String)
				{
					tmp_str = (String) data[i];
					tmp_cell.setCellValue(tmp_str);
				}
				else if (data[i] instanceof Float | data[i] instanceof Integer)
				{
					tmp_float = (Float.valueOf(data[i].toString()));
					tmp_cell.setCellValue(tmp_float);
				}
			}
		}
		else if (sheet.getPhysicalNumberOfRows() == 0)
			pm.status_mgr.setStatus("Excel sheet is Empty!", StatusSeverity.ERROR);
		else
			pm.status_mgr.setStatus("Row is not found!!", StatusSeverity.ERROR);
	}

	/**
	 * Generates the index of the next row.
	 * 
	 * @return an integer representing the next row index
	 */
	private int getNextRowNumber()
	{
		nxt_row_idx++;
		return nxt_row_idx - 1;
	}

	/**
	 * Initializes the excel objects.
	 */
	private void init()
	{
		wb = new XSSFWorkbook();
		sheet = wb.createSheet();
	}

	/**
	 * Reinitialization.
	 */
	public void reset()
	{
		wb = null;
		sheet = null;
		nxt_row_idx = 0;
		init();
	}

	/**
	 * Saves the worksheet to an .xlsx file.
	 * 
	 * @param filename
	 *            path of the file to save the data to
	 */
	public void saveToFile(final String filename)
	{
		try
		{
			final FileOutputStream fos = new FileOutputStream(filename);
			wb.write(fos);
			fos.close();
		} catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
}
