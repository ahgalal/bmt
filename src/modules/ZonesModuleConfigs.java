package modules;

import javax.annotation.Resource;

/**
 * Configuration class for the zones module.
 * 
 * @author Creative
 */
public class ZonesModuleConfigs extends ModuleConfigs
{
	/**
	 * Hello there :D, the style checker recommended adding a Javadoc comment
	 * here, so, here we go!
	 */
	@Resource
	/**
	 * Zone hysteresis value
	 * 
	 *   ___________________________________________________                                                     |
	 *   |                  #     |     #                   |
	 *   |                  #     |     #   <=== O          |
	 *   |                  #     |     #                   |
	 *   |                  #     |     #                   |
	 *   |         Z1       #  A  |  B  #     Z2            |
	 *   |                  #     |     #                   |
	 *   |       X ===>     #     |     #                   |
	 *   |                  #     |     #                   |
	 *   |__________________#_____|_____#___________________|
	 *                          
	 *                            ^
	 *                            |
	 *                         Boundary
	 *                           
	 *   
	 *   rat "O" coming from Z2 to Z1 will not be considered in Z1 till it passes 
	 *   the A region.
	 *   rat "X" coming from Z1 to Z2 will not be considered in Z2 till it passes 
	 *   the B region.
	 *   
	 *   - 	hysteresis region is region (A+B)
	 *   - 	hysteresis value is the width of region A (or B, they are equal)
	 *   
	 *   - 	hysteresis idea is applied to increase the robustness of the zone 
	 *   	detection mechanism, as sometimes the object marker (detected position) 
	 *   	vibrates around the boundary between two zones, so that causes errors in
	 *   	calculating number of entering/leaving of zones. 
	 *   
	 */
	public int hyst_value = 50;

	/**
	 * Initializations for the configurations.
	 * 
	 * @param module_name
	 *            name of the module
	 * @param hyst_val
	 *            initial hysteresis value
	 */
	public ZonesModuleConfigs(final String module_name, final int hyst_val)
	{
		super(module_name);
		hyst_value = hyst_val;
	}

	@Override
	protected void mergeConfigs(final ModuleConfigs configs)
	{
		final ZonesModuleConfigs tmp_zonConfigs = (ZonesModuleConfigs) configs;
		if (tmp_zonConfigs.hyst_value != -1)
			hyst_value = tmp_zonConfigs.hyst_value;
	}
}
