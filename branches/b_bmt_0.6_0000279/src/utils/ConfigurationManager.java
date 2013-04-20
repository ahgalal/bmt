/**
 * 
 */
package utils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Creative
 *
 */
@SuppressWarnings("rawtypes")
public class ConfigurationManager<ConfigurationType extends Configuration>  {

	protected ArrayList<ConfigurationType> configs;
	private ArrayList<? extends Configurable> configurables;
	
	public ConfigurationManager(ArrayList<? extends Configurable> configurables) {
		configs=new ArrayList<ConfigurationType>();
		this.configurables = configurables;
	}
	
	public ConfigurationType addConfiguration(ConfigurationType cfgNew,
			boolean updateExisting) {
		ConfigurationType existing = null;
		for (final ConfigurationType cfg : configs)
			if (cfg.getName().equals(
					cfgNew.getName())) {
				existing = cfg;
				break;
			}
		if (existing != null) {
			if (updateExisting) {
				existing.mergeConfigs(cfgNew);
			} /*else
				
				throw new RuntimeException("Error adding an already existing filter configuration, try updating the existing configuration instead.");*/
			return existing;
		} else {
			configs.add(cfgNew);
			return cfgNew;
		}

	}

	public ConfigurationType getConfigByName(String configName) {
		for (final ConfigurationType cfg: configs)
			if (cfg.getName().equals(configName))
				return cfg;
		return null;
	}
	
	private Configurable getConfigurableByName(String name) {
		for (final Configurable configurable : configurables)
			if (configurable.getName().equals(name))
				return configurable;
		return null;
	}
	/**
	 * Applies a configuration object to a configurable instance, using the name of the configurable instance
	 * specified in the configuration object.</br>Also adds the 
	 * configuration instance to the list if it doesn't exist.
	 * 
	 * @param cfgs
	 *            configurations object
	 */
	public void applyConfigs(ConfigurationType cfgs) {
		final Configurable module = getConfigurableByName(cfgs.getName());
		if (module != null) {
			try {
				// try to add the config to the list, or get the existing one
				// after it is updated by the incoming config
				cfgs = addConfiguration(cfgs, true);
			} catch (final RuntimeException e) {
			} finally {
				module.updateConfigs(cfgs);
			}
		}else{
			throw new RuntimeException("Could not find module to be configured: " + cfgs.getName()+":"+cfgs.getID());
		}
	}
	
	/**
	 * Applies a configuration object to configurable instance(s), using the ID of the configurable instance(s)
	 * specified in the configuration object.
	 * 
	 * @param cfgs
	 *            configurations object
	 */
	public void applyConfigsByID(ConfigurationType cfgs) {
		final ArrayList<Configurable> configurables = getConfigurablesByID(cfgs.getID());
		if (configurables.size()>0) {
				for(Configurable configurable:configurables)
					configurable.updateConfigs(cfgs);
		}else{
			throw new RuntimeException("Could not find module to be configured: " + cfgs.getName()+":"+cfgs.getID());
		}
	}

	private ArrayList<Configurable> getConfigurablesByID(String id) {
		ArrayList<Configurable> ret=new ArrayList<Configurable>();
		
		for(Configurable<?> configurable:configurables)
			if(configurable.getID().equals(id))
				ret.add(configurable);
		return ret;
	}

	public void reset() {
		configs.clear();
	}
	
	public ConfigurationType createDefaultConfigs(String name,String moduleId) {
		for(ConfigurationType config: configs){
			if(config.getID().equals(moduleId)){
				return (ConfigurationType) config.newInstance(name);
			}
		}
		return null;
	}
	
	public void printConfiguration(){
		System.out.println("Configurations available: =====");
		for (final ConfigurationType cfg: configs)
			System.out.println(cfg.getName() + " : " + cfg.getID());
		System.out.println("===============================");
	}

	public Iterator<ConfigurationType> getConfigurations() {
		return configs.iterator();
	}
}
