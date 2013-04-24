/**
 * 
 */
package filters.movementmeter;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import filters.CommonFilterConfigs;
import filters.FilterConfigs;

/**
 * @author Creative
 *
 */
public class MovementMeterFilterConfigs extends FilterConfigs {

	public MovementMeterFilterConfigs(String name, CommonFilterConfigs commonConfigs) {
		super(name,MovementMeter.ID, commonConfigs);
	}

	/* (non-Javadoc)
	 * @see filters.FilterConfigs#validate()
	 */
	@Override
	public boolean validate() {
		if (getCommonConfigs() == null) {
			PManager.log.print("Configs are not completely configured!", this,
					StatusSeverity.ERROR);
			return false;
		}
		return true;
	}

	@Override
	public FilterConfigs newInstance(String filterName,
			CommonFilterConfigs commonConfigs) {
		return new MovementMeterFilterConfigs(filterName, commonConfigs);
	}

}
