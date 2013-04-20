/**
 * 
 */
package utils;


/**
 * @author Creative
 *
 */
public interface Configurable<ConfigsType extends Configuration<?>> {
	public void updateConfigs(ConfigsType config);
	public String getName();
	public String getID();
}
