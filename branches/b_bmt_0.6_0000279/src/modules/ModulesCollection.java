package modules;

import java.util.ArrayList;
import java.util.Iterator;

public class ModulesCollection {
	private ArrayList<Module<?, ?, ?>> modules = new ArrayList<Module<?,?,?>>();

	public ModulesCollection() {
	}

	public void addModule(final Module<?, ?, ?> module) {
		modules.add(module);
	}

	@SuppressWarnings("rawtypes")
	public String[] getModulesNames() {
		String[] ret = new String[modules.size()];
		int i=0;
		for(Module module:modules){
			ret[i] = module.getName();
			i++;
		}
		return ret;
	}
	
	// TODO: getModuleByName

	@SuppressWarnings("rawtypes")
	public void removeModule(Module module) {
		modules.remove(module);
	}

	public Iterator<Module<?, ?, ?>> getModules() {
		return modules.iterator();
	}

}
