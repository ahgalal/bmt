/**
 * 
 */
package filters;

import java.util.ArrayList;

import utils.Configurable;
import utils.ConfigurationManager;

/**
 * @author Creative
 *
 */
public class FiltersConfigurationManager extends
		ConfigurationManager<FilterConfigs> {

	public FiltersConfigurationManager(ArrayList<? extends Configurable> configurables) {
		super(configurables);
	}

	public FilterConfigs createDefaultConfigs(String filterName,String filterId,CommonFilterConfigs commonConfigs) {
		for(FilterConfigs config: configs){
			if(config.getID().equals(filterId)){
				return config.newInstance(filterName, commonConfigs);
			}
		}
		return null;
	}
	
	@Override
	public FilterConfigs createDefaultConfigs(String name, String moduleId) {
		throw new RuntimeException("Illegal method call, should have called the overload");
	}

	
}
