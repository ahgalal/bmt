/**
 * 
 */
package filters.avg;

import filters.CommonFilterConfigs;
import filters.FilterConfigs;

/**
 * @author Creative
 *
 */
public class AverageFilterConfigs extends FilterConfigs {

	public AverageFilterConfigs(String name, CommonFilterConfigs commonConfigs) {
		super(name,AverageFilter.ID, commonConfigs);
	}

	@Override
	public FilterConfigs newInstance(String filterName,
			CommonFilterConfigs commonConfigs) {
		return new AverageFilterConfigs(filterName, commonConfigs);
	}

}
