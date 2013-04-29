/**
 * 
 */
package filters;

import java.util.ArrayList;

import utils.Configurable;
import utils.ConfigurationManager;

/**
 * @author Creative
 */
public class FiltersConfigurationManager extends
		ConfigurationManager<FilterConfigs> {

	@SuppressWarnings("rawtypes")
	public FiltersConfigurationManager(
			final ArrayList<? extends Configurable> configurables) {
		super(configurables);
	}

	@Override
	public FilterConfigs createDefaultConfigs(final String name,
			final String moduleId) {
		throw new RuntimeException(
				"Illegal method call, should have called the overload");
	}

	public FilterConfigs createDefaultConfigs(final String filterName,
			final String filterId, final CommonFilterConfigs commonConfigs) {
		for (final FilterConfigs config : configs) {
			if (config.getID().equals(filterId)) {
				return config.newInstance(filterName, commonConfigs);
			}
		}
		return null;
	}

}
