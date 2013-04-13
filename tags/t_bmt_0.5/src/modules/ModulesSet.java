package modules;

import java.util.ArrayList;

public class ModulesSet {
	private final ArrayList<String>	modulesNames;

	public ModulesSet(final String[] modulesNames) {
		this.modulesNames = new ArrayList<String>();
		for (final String str : modulesNames)
			this.modulesNames.add(str);
	}

	public void addModule(final String moduleName) {
		modulesNames.add(moduleName);
	}

	public String[] getModulesNames() {
		return modulesNames.toArray(new String[0]);
	}

	public void removeModule(final String moduleName) {
		modulesNames.remove(moduleName);
	}

}
