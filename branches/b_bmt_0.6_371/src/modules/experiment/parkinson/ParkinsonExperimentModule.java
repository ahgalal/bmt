/**
 * 
 */
package modules.experiment.parkinson;

import modules.Module;
import modules.experiment.Constants;
import modules.experiment.ExperimentModule;
import modules.experiment.ExperimentModuleConfigs;
import modules.experiment.ExperimentModuleData;

/**
 * @author Creative
 *
 */
public class ParkinsonExperimentModule extends ExperimentModule {
	public final static String			moduleID=Constants.EXPERIMENT_ID+".parkinson";
	public ParkinsonExperimentModule(String name, ExperimentModuleConfigs config) {
		super(name, config);
		
		data = new ExperimentModuleData();
		addDefaultModuleDataParams();
	}

	/* (non-Javadoc)
	 * @see modules.Module#getID()
	 */
	@Override
	public String getID() {
		return moduleID;
	}

	/* (non-Javadoc)
	 * @see modules.Module#newInstance(java.lang.String)
	 */
	@Override
	public Module newInstance(String name) {
		return null; // ExperimentModules are instantiated explicitly
	}
}
