package modules.movementmeter;

import java.util.ArrayList;

import modules.Cargo;
import modules.Module;
import modules.ModuleConfigs;
import modules.ModuleData;
import modules.experiment.ExperimentType;

import org.eclipse.swt.widgets.Shell;

import utils.Logger.Details;
import utils.PManager;
import filters.Data;
import filters.movementmeter.MovementMeterData;

public class MovementMeterModule extends
		Module<MovementMeterModuleGUI, ModuleConfigs, ModuleData> {
	private final ArrayList<Integer>	energyData;
	private String[]					expParams;
	private MovementMeterData			movementMeterFilterData;
	private final int					noEnergyLevels	= 3;
	private int[]						sectorsData;
	private int							time			= 0;

	public MovementMeterModule(final String name, final ModuleConfigs config) {
		super(name, config);
		filters_data = new Data[1];
		energyData = new ArrayList<Integer>();
		gui = new MovementMeterModuleGUI(this);
		data = new ModuleData("MovementMeter Module Data");
		initialize();
	}

	@Override
	public boolean amIReady(final Shell shell) {
		if (movementMeterFilterData != null)
			return true;
		else
			return false;
	}

	@Override
	public void deInitialize() {
		sectorizeEnergy();
	}

	@Override
	public void deRegisterDataObject(final Data data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
		PManager.log.print("initializing..", this, Details.VERBOSE);
		guiCargo = new Cargo(new String[] { "Energy" });
		energyData.clear();
		expParams = new String[noEnergyLevels];
		for (int i = 0; i < noEnergyLevels; i++)
			expParams[i] = "eLevel_" + i;
		fileCargo = new Cargo(expParams);

		for (final String param : expParams)
			data.addParameter(param);

		expType = new ExperimentType[] { ExperimentType.FORCED_SWIMMING };
	}

	@Override
	public void process() {
		int newVal = movementMeterFilterData.getWhiteSummation();
		if (energyData.size() > 40) {
			final int oldVal1 = energyData.get(energyData.size() - 10);
			final int oldVal2 = energyData.get(energyData.size() - 20);
			final int oldVal3 = energyData.get(energyData.size() - 30);
			if (Math.abs(newVal - oldVal2) > 1000000)
				newVal = oldVal2;
			else
				newVal = (movementMeterFilterData.getWhiteSummation() + oldVal1 + oldVal2) / 3;
		}

		energyData.add(newVal);
	}

	@Override
	public void registerFilterDataObject(final Data data) {
		if (data instanceof MovementMeterData) {
			movementMeterFilterData = (MovementMeterData) data;
			this.filters_data[0] = movementMeterFilterData;
		}
	}

	@Override
	public void registerModuleDataObject(final ModuleData data) {
		// TODO Auto-generated method stub

	}

	private void sectorizeEnergy() {
		int max = 0, min = 100000000;

		sectorsData = new int[noEnergyLevels];
		for (final int i : energyData)
			if (i > max)
				max = i;
			else if (i < min)
				min = i;

		System.out.println("Sectors: min: " + min + " max: " + max);

		final int[] levels = new int[noEnergyLevels];
		final int levelHeight = (max - min) / noEnergyLevels;

		for (int k = 0; k < noEnergyLevels; k++) {
			levels[k] = levelHeight * k;
			System.out.println("Level-" + k + " has min value: " + levels[k]);
		}

		for (final int i : energyData)
			for (int j = 1; j < noEnergyLevels; j++)
				if ((i < levels[j]) && (i > levels[j - 1]))
					sectorsData[j - 1]++;

		System.out.println("[[[Sectors data:]]]");
		for (int i = 0; i < sectorsData.length; i++)
			System.out.println("Level-" + i + " " + sectorsData[i]);

	}

	@Override
	public void updateConfigs(final ModuleConfigs config) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFileCargoData() {
		final String[] sectorsDataStr = new String[sectorsData.length];
		int k = 0;
		for (final int i : sectorsData) {
			sectorsDataStr[k] = Integer.toString(i);
			k++;
		}
		fileCargo.setData(sectorsDataStr);

		/*
		 * fileCargo.setDataByIndex(0, "" +
		 * movementMeterFilterData.getWhiteSummation());
		 */
	}

	@Override
	public void updateGUICargoData() {
		if (energyData.size() > 0) {
			final int newData = energyData.get(energyData.size() - 1);
			guiCargo.setDataByIndex(0, "" + newData);
			gui.addPoint(time++, newData/ 2000);
		}
	}

}
