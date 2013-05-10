/**
 * 
 */
package utils;


/**
 * @author Creative
 *
 */
@SuppressWarnings("rawtypes")
public interface Configurable<ConfigsType extends Configuration> {
	public void updateConfigs(ConfigsType config);
	public String getName();
	public String getID();
}
