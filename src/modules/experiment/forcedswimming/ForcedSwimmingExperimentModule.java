package modules.experiment.forcedswimming;

import modules.experiment.ExperimentModule;
import modules.experiment.ExperimentModuleConfigs;
import modules.experiment.ExperimentModuleData;

/**
 * @author Creative
 * 
 */
public class ForcedSwimmingExperimentModule extends ExperimentModule {

    public ForcedSwimmingExperimentModule(final String name,
	    final ExperimentModuleConfigs config) {
	super(name, config);
	// gui = new ExperimentModuleGUI(this);
	data = new ExperimentModuleData(name);
	// data.exp=new Experiment();
	// data.exp.type=ExperimentType.FORCED_SWIMMING;
    }

    @Override
    public boolean allowTracking() {
	// TODO Auto-generated method stub
	return true;
    }

}
