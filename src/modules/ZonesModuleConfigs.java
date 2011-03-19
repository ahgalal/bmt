package modules;


public class ZonesModuleConfigs extends ModuleConfigs {

	public int hyst_value=50;
	
	public ZonesModuleConfigs(String module_name,int hyst_val) {
		super(module_name);
		hyst_value=hyst_val;
	}

	@Override
	protected void mergeConfigs(ModuleConfigs configs) {
		ZonesModuleConfigs tmp_zonConfigs = (ZonesModuleConfigs) configs;
		if(tmp_zonConfigs.hyst_value!=-1)
			hyst_value=tmp_zonConfigs.hyst_value;
	}
}
