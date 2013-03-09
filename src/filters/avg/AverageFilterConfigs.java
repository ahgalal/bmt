/**
 * 
 */
package filters.avg;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import filters.CommonFilterConfigs;
import filters.FilterConfigs;

/**
 * @author Creative
 *
 */
public class AverageFilterConfigs extends FilterConfigs {

	public AverageFilterConfigs(String name, CommonFilterConfigs commonConfigs) {
		super(name, commonConfigs);
	}

	/* (non-Javadoc)
	 * @see filters.FilterConfigs#mergeConfigs(filters.FilterConfigs)
	 */
	@Override
	public void mergeConfigs(FilterConfigs configs) {
		if (configs.getCommonConfigs() != null)
			setCommonConfigs(configs.getCommonConfigs());
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

}
