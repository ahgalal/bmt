package modules;

public abstract class ModuleConfigs {

	protected boolean enable;
	protected String module_name;
	
	public String getModuleName()
	{
		return module_name;
	}
	
	public ModuleConfigs(String module_name)
	{
		this.module_name=module_name;
	}
	
	protected abstract void mergeConfigs(ModuleConfigs config);
}
