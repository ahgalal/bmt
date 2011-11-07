package modules;

import java.util.ArrayList;

public class ModulesSetup
{
	private ArrayList<String> modulesNames;

	public ModulesSetup(String[] modulesNames)
	{
		this.modulesNames=new ArrayList<String>();
		for(String str:modulesNames)
			this.modulesNames.add(str);
	}

	public void addModule(String moduleName)
	{
		modulesNames.add(moduleName);
	}
	
	public void removeModule(String moduleName)
	{
		modulesNames.remove(moduleName);
	}
	
	public String[] getModulesNames()
	{
		return modulesNames.toArray(new String[0]);
	}

}
