package modules.experiment.forcedswimming;

import modules.experiment.Experiment;
import modules.experiment.ExperimentModule;
import modules.experiment.ExperimentModuleConfigs;
import modules.experiment.ExperimentModuleData;
import modules.experiment.Experiment.ExperimentType;
import modules.experiment.ExperimentModuleGUI;
import modules.experiment.openfield.OpenFieldExperimentModuleGUI;

/**
 * @author Creative
 *
 */
public class ForcedSwimmingExperimentModule extends ExperimentModule
{

	public ForcedSwimmingExperimentModule(String name, ExperimentModuleConfigs config)
	{
		super(name, config);
		//gui = new ExperimentModuleGUI(this);
		data = new ExperimentModuleData(name);
		//data.exp=new Experiment();
		//data.exp.type=ExperimentType.FORCED_SWIMMING;
	}

}
