package model.business;

import modules.Cargo;
import utils.PManager;
import utils.StatusManager.StatusSeverity;

/**
 * @author ShaQ
 * Handles rat's info
 */
public class Rat {

	private Cargo info;

	public Rat(String[] measurements_list,String[] values){
		this(measurements_list);
		info.setData(values);
	}

	public Rat(String[] measurements_list){
		info=new Cargo(measurements_list);
	}

	public String[] getValues()
	{
		return info.getData();
	}

	public String getValueByMeasurementName(String measurement_name)
	{
		return info.getDataByTag(measurement_name);
	}

/*	private int getIndexByMeasurementName(String measurement_name)
	{
		for(int i=0;i<measurements_list.length;i++)
		{
			if(measurements_list[i].equals(measurement_name))
				return i;
		}
		return -1;
	}*/

	public boolean setValueByMeasurementName(String measurement_name,String value)
	{
		try{
			info.setDataByTag(measurement_name, value);
		}catch(Exception e){
			PManager.log.print("Error in measurement name!", this, StatusSeverity.ERROR);
			return false;
		};
		return true;
	}
}
