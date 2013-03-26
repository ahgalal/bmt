/**
 * 
 */
package utils;

/**
 * @author Creative
 *
 */
public interface Configuration<Type> {
	public String getName();
	public void setName(String name);
	public String getID();
	public void mergeConfigs(Type cfg);
	public Type newInstance(String name);
}
