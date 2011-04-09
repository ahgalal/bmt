package modules.experiment;

import utils.video.filters.Data;

/**
 * Data of the Experiment Module.
 * 
 * @author Creative
 */
public class ExperimentModuleData extends Data
{
	public int curr_rat_number;
	public String curr_grp_name;
	public Experiment exp;
	public String exp_file_name;

	/**
	 * Initializes the Data.
	 * 
	 * @param name
	 *            name of the data instance
	 */
	public ExperimentModuleData(final String name)
	{
		super(name);
	}

}
