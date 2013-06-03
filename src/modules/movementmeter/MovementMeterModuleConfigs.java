package modules.movementmeter;

import modules.ModuleConfigs;

public class MovementMeterModuleConfigs extends ModuleConfigs {

	private double[]					energyLevelsRatio;
	public MovementMeterModuleConfigs(final String moduleName) {
		super(moduleName);
		
		// default value of levels' thresholds
		energyLevelsRatio = new double[]{1,0.2};
	}

	@Override
	protected void initializeModuleID() {
		moduleID = MovementMeterModule.moduleID;
	}

	@Override
	public void mergeConfigs(final ModuleConfigs config) {
		super.mergeConfigs(config);
		
		if(config instanceof MovementMeterModuleConfigs){
			MovementMeterModuleConfigs movementMeterModuleConfigs = (MovementMeterModuleConfigs) config;
			if(movementMeterModuleConfigs.getEnergyLevelsRatio() !=null){
				for(int i=0;i<movementMeterModuleConfigs.getEnergyLevelsRatio().length;i++){
					// check if the level value is valid
					if(movementMeterModuleConfigs.getEnergyLevelsRatio()[i]!=-1 &&
							movementMeterModuleConfigs.getEnergyLevelsRatio()[i]!=this.getEnergyLevelsRatio()[i]){
						System.out.println("Merging config value: "+ movementMeterModuleConfigs.getEnergyLevelsRatio()[i] );
						this.getEnergyLevelsRatio()[i]=movementMeterModuleConfigs.getEnergyLevelsRatio()[i];
					}
				}
			}
		}
	}

	@Override
	public ModuleConfigs newInstance(final String moduleName) {
		return new MovementMeterModuleConfigs(moduleName);
	}

	public void setEnergyLevelsRatio(double[] energyLevelsRatio) {
		this.energyLevelsRatio = energyLevelsRatio;
	}

	public double[] getEnergyLevelsRatio() {
		return energyLevelsRatio;
	}

}
