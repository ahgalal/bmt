/**
 * 
 */
package modules;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Creative
 *
 */
public class ModulesNamesRequirements {
	private ArrayList<ModuleRequirement> moduleRequirements=new ArrayList<ModulesNamesRequirements.ModuleRequirement>();
	
	public static class ModuleRequirement{
		private String ID;
		public String getID() {
			return ID;
		}
		public void setID(String iD) {
			ID = iD;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		private String name;
		public ModuleRequirement(String iD, String name) {
			super();
			ID = iD;
			this.name = name;
		}
	}

	public void addModule(String name,String Id){
		ModuleRequirement moduleRequirement=new ModuleRequirement(Id, name);
		moduleRequirements.add(moduleRequirement);
	}
	
	public void remove(String name){
		ModuleRequirement toBeRemoved=null;
		for(ModuleRequirement moduleRequirement:moduleRequirements)
			if(moduleRequirement.getName().equals(name)){
				toBeRemoved=moduleRequirement;
				break;
			}
				
		moduleRequirements.remove(toBeRemoved);
	}
	
	public Iterator<ModuleRequirement> getModuleRequirements(){
		return moduleRequirements.iterator();
	}
}
