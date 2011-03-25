package model.business;

import modules.Cargo;
import utils.PManager;
import utils.StatusManager.StatusSeverity;

/**
 * @author ShaQ Handles rat's info
 */
public class Rat
{

	private final Cargo info;

	public Rat(final String[] measurements_list, final String[] values)
	{
		this(measurements_list);
		info.setData(values);
	}

	public Rat(final String[] measurements_list)
	{
		info = new Cargo(measurements_list);
	}

	public String[] getValues()
	{
		return info.getData();
	}

	public String getValueByMeasurementName(final String measurement_name)
	{
		return info.getDataByTag(measurement_name);
	}

	public boolean setValueByMeasurementName(
			final String measurement_name,
			final String value)
	{
		try
		{
			info.setDataByTag(measurement_name, value);
		} catch (final Exception e)
		{
			PManager.log.print("Error in measurement name!", this, StatusSeverity.ERROR);
			return false;
		}
		return true;
	}

	public String rat2String()
	{
		//String values = " ";
		StringBuffer values_buf=new StringBuffer();
		for (final String s : getValues())
			values_buf.append(s + '\t'); // TODO:tab after the last item ??!!
		values_buf.append(System.getProperty("line.separator"));
		return values_buf.toString();
	}
}
