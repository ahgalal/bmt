/**
 * 
 */
package modules.experiment.openfield;

import modules.Module;
import modules.experiment.Constants;
import modules.experiment.ExperimentModule;
import modules.experiment.ExperimentModuleConfigs;
import modules.experiment.ExperimentModuleData;

import org.eclipse.swt.widgets.Shell;

import filters.Data;

/**
 * @author Creative
 */
public class OpenFieldExperimentModule extends ExperimentModule {
	public final static String	moduleID	= Constants.EXPERIMENT_ID
													+ ".openfield";
	private boolean				bgSet;

	public OpenFieldExperimentModule(final String name,
			final ExperimentModuleConfigs config) {
		super(name, config);
		gui = new OpenFieldExperimentModuleGUI(this);

		data = new ExperimentModuleData();
		addDefaultModuleDataParams();
	}

	@Override
	public boolean allowTracking() {
		if (isBgSet())
			return true;
		else
			return false;
	}

	@Override
	public boolean amIReady(final Shell shell) {
		if (bgSet)
			return super.amIReady(shell);
		else
			return false;
	}

	@Override
	public String getID() {
		return moduleID;
	}

	/**
	 * Checks if the Background (Subtraction filter) has been set.
	 * 
	 * @return true/false
	 */
	public boolean isBgSet() {
		return bgSet;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Module newInstance(final String name) {
		return null; // ExperimentModules are instantiated explicitly
	}

	@Override
	public void registerFilterDataObject(final Data data) {

	}

	/**
	 * Updates the RGB background of the subtraction filter with the current
	 * image, and returns the current image.
	 * 
	 * @return integer array representing the RGB background image
	 */
	public void setBG() {
		bgSet = true;
	}

}
