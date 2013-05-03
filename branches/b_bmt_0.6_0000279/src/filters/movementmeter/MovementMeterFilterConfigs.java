/**
 * 
 */
package filters.movementmeter;

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

	@Override
	public FilterConfigs newInstance(String filterName,
			CommonFilterConfigs commonConfigs) {
		return new MovementMeterFilterConfigs(filterName, commonConfigs);
	}

}
