/**
 * 
 */
package modules.experiment.openfield;

import modules.experiment.ExperimentModule;
import modules.experiment.ExperimentModuleConfigs;
import modules.experiment.ExperimentModuleData;

import org.eclipse.swt.widgets.Shell;

import utils.Utils;
import filters.Data;
import filters.source.SourceFilterData;


/**
 * @author Creative
 */
public class OpenFieldExperimentModule extends ExperimentModule {
	private int[]				bgImageRGB;
	private boolean				bgSet;
	private SourceFilterData	rgbData;

	public OpenFieldExperimentModule(final String name,
			final ExperimentModuleConfigs config) {
		super(name, config);
		gui = new OpenFieldExperimentModuleGUI(this);
		data = new ExperimentModuleData(name);
		addDefaultModuleDataParams();
	}

	@Override
	public boolean allowTracking() {
		if (bgSet)
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

	/**
	 * Checks if the Background (Subtraction filter) has been set.
	 * 
	 * @return true/false
	 */
	public boolean isBgSet() {
		return bgSet;
	}

	@Override
	public void registerFilterDataObject(final Data data) {
		if (data instanceof SourceFilterData)
			rgbData = (SourceFilterData) data;

	}

	/**
	 * Updates the RGB background of the subtraction filter with the current
	 * image, and returns the current image.
	 * 
	 * @return integer array representing the RGB background image
	 */
	public int[] updateRGBBackground() {
		bgImageRGB = rgbData.getData();
		if(bgImageRGB==null){
			Utils.sleep(100);
			bgImageRGB = rgbData.getData();
		}
			
		bgSet = true;
		return bgImageRGB;
	}

}
