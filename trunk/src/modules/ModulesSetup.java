/**
 * 
 */
package modules;

/**
 * @author Creative
 *
 */
public class ModulesSetup {
	private ModulesCollection modulesCollection;
	private ModulesNamesRequirements modulesRequirements;
	public ModulesSetup(ModulesNamesRequirements modulesRequirements) {
		this.setModulesRequirements(modulesRequirements);
	}
	public void setModulesCollection(ModulesCollection modulesCollection) {
		this.modulesCollection = modulesCollection;
	}
	public ModulesCollection getModulesCollection() {
		return modulesCollection;
	}
	public void setModulesRequirements(ModulesNamesRequirements modulesRequirements) {
		this.modulesRequirements = modulesRequirements;
	}
	public ModulesNamesRequirements getModulesRequirements() {
		return modulesRequirements;
	}
	
	
	

}
