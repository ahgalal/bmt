package modules.experiment.forcedswimming;

import modules.Module;
import modules.experiment.Constants;
import modules.experiment.ExperimentModule;
import modules.experiment.ExperimentModuleConfigs;
import modules.experiment.ExperimentModuleData;

/**
 * @author Creative
 */
public class ForcedSwimmingExperimentModule extends ExperimentModule {

	public ForcedSwimmingExperimentModule(String name,
			final ExperimentModuleConfigs config) {
		super(name, config);
		// gui = new ExperimentModuleGUI(this);
		data = new ExperimentModuleData();
		addDefaultModuleDataParams();
		// data.exp=new Experiment();
		// data.exp.type=ExperimentType.FORCED_SWIMMING;
	}
	@Override
	public String getID() {
		return moduleID;
	}
	public final static String			moduleID=Constants.EXPERIMENT_ID+".forcedswimming";
	@Override
	public Module newInstance(String name) {
		return null; // ExperimentModules are instantiated explicitly
	}
}
