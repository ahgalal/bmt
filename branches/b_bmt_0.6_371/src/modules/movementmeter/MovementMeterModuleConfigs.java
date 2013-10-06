package modules.movementmeter;

import modules.ModuleConfigs;

public class MovementMeterModuleConfigs extends ModuleConfigs {

	public MovementMeterModuleConfigs(final String moduleName) {
		super(moduleName);
	}

	@Override
	protected void initializeModuleID() {
		moduleID = MovementMeterModule.moduleID;
	}

	@Override
	public void mergeConfigs(final ModuleConfigs config) {
		// TODO
	}

	@Override
	public ModuleConfigs newInstance(final String moduleName) {
		return new MovementMeterModuleConfigs(moduleName);
	}

}
