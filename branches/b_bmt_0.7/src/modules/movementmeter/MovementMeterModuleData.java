/**
 * 
 */
package modules.movementmeter;

import modules.ModuleData;
import modules.experiment.Constants;

/**
 * @author Creative
 *
 */
public class MovementMeterModuleData extends ModuleData {
	public final static String dataID=Constants.MODULE_ID+".movement.data";
	/* (non-Javadoc)
	 * @see filters.Data#initializeID()
	 */
	@Override
	public String getId() {
		return dataID;
	}
}
