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
	private static final int			ENERGY_DIVISION_FACTOR	= 2000;
	private static final int			MIN_SAMPLES				= 31;
	private ArrayList<Integer>			energyBins;
	private final ArrayList<Integer>	energyData;
	private int[]						energyLevels;
	private String[]					expParams;
	private int							maxEnergy;
	private int							minEnergy;
	private MovementMeterData			movementMeterFilterData;
	private final int					noEnergyLevels			= 3;
	private int	sectorizeFlag;

	public MovementMeterModule(final String name, final ModuleConfigs config) {
		super(name, config);
		filtersData = new Data[1];
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
		//sectorizeEnergy();
	}

	@Override
	public void deRegisterDataObject(final Data data) {
	}

	private int getMaxEnergy() {
		return maxEnergy;
	}

	private int getMinEnergy() {
		return minEnergy;
	}

	@Override
	public void initialize() {
		PManager.log.print("initializing..", this, Details.VERBOSE);
		energyData.clear();
		expParams = new String[noEnergyLevels];
		energyBins = new ArrayList<Integer>();
		energyLevels = new int[noEnergyLevels];
		for (int i = 0; i < noEnergyLevels; i++) {
			expParams[i] = "eLevel_" + i;
			energyBins.add(0);
		}
		
		//////// in case of 3 levels:
		expParams[0]="Climbing";
		expParams[1]="Swimming";
		expParams[2]="Floating";
		////////
		
		fileCargo = new Cargo(expParams);
		guiCargo = new Cargo(expParams);
		
		for (final String param : expParams)
			data.addParameter(param);

		expType = new ExperimentType[] { ExperimentType.FORCED_SWIMMING };
		maxEnergy = 0;
		minEnergy = 100000000;
	}

	@Override
	public void process() {
		int newVal = movementMeterFilterData.getWhiteSummation()
				/ ENERGY_DIVISION_FACTOR;
		if (energyData.size() > MIN_SAMPLES) {
			final int oldVal1 = energyData.get(energyData.size() - 5);
			final int oldVal2 = energyData.get(energyData.size() - 10);
			final int oldVal3 = energyData.get(energyData.size() - 15);
			final int oldVal4 = energyData.get(energyData.size() - 20);
			final int oldVal5 = energyData.get(energyData.size() - 25);
			final int oldVal6 = energyData.get(energyData.size() - 30);

			// filter out abrupt changes
			if (Math.abs(newVal - oldVal1) > getMaxEnergy() / 2)
				newVal = oldVal1;// +(newVal - oldVal1)/5;

			// smooth curve
			newVal = ((newVal + oldVal1 + oldVal2 + oldVal3 + oldVal4 + oldVal5 + oldVal6) / 7);

			if (newVal > getMaxEnergy())
				setMaxEnergy(newVal);

			if (newVal < getMinEnergy())
				setMinEnergy(newVal);
		}
		energyData.add(newVal);
		
		sectorizeFlag++;
		if(sectorizeFlag%30==0)
			sectorizeEnergy();
	}

	@Override
	public void registerFilterDataObject(final Data data) {
		if (data instanceof MovementMeterData) {
			movementMeterFilterData = (MovementMeterData) data;
			this.filtersData[0] = movementMeterFilterData;
		}
	}

	@Override
	public void registerModuleDataObject(final ModuleData data) {
	}

	private void sectorizeEnergy() {
		// clear bins
		for (int i = 0; i < noEnergyLevels; i++)
			energyBins.set(i, 0);

		// sectorize
		for (final int energy : energyData) {
			boolean addedToBin = false;
			for (int j = 1; j < noEnergyLevels; j++)
				if ((energy < energyLevels[j - 1])
						&& (energy > energyLevels[j])) {
					energyBins.set(j - 1, energyBins.get(j - 1) + 1);
					addedToBin = true;
					break;
				}
			if (!addedToBin) {
				// add it to the smallest value bin
				final int smallestValueBinIndex = noEnergyLevels - 1;
				final Integer smallestValueBinValue = energyBins
						.get(smallestValueBinIndex);
				energyBins
						.set(smallestValueBinIndex, smallestValueBinValue + 1);
			}
		}
		/*
		 * int max = 0, min = 100000000; sectorsData = new int[noEnergyLevels];
		 * for (final int i : energyData) if (i > max) max = i; else if (i <
		 * min) min = i; System.out.println("Sectors: min: " + min + " max: " +
		 * max); final int[] levels = new int[noEnergyLevels]; final int
		 * levelHeight = (max - min) / noEnergyLevels; for (int k = 0; k <
		 * noEnergyLevels; k++) { levels[k] = levelHeight * k;
		 * System.out.println("Level-" + k + " has min value: " + levels[k]); }
		 * for (final int i : energyData) for (int j = 1; j < noEnergyLevels;
		 * j++) if ((i < levels[j]) && (i > levels[j - 1])) sectorsData[j -
		 * 1]++; System.out.println("[[[Sectors data:]]]"); for (int i = 0; i <
		 * sectorsData.length; i++) System.out.println("Level-" + i + " " +
		 * sectorsData[i]);
		 */

	}

	private void setMaxEnergy(final int maxEnergy) {
		this.maxEnergy = maxEnergy;
		updateEnergyLevels();
	}

	private void setMinEnergy(final int minEnergy) {
		this.minEnergy = minEnergy;
		updateEnergyLevels();
	}

	@Override
	public void updateConfigs(final ModuleConfigs config) {
	}

	private void updateEnergyLevels() {
		for (int i = 1; i <= energyLevels.length; i++)
			energyLevels[i - 1] = (int) (getMinEnergy() + ((getMaxEnergy() - getMinEnergy()) * ((energyLevels.length + 1 - i) / (double) energyLevels.length)));
	}

	@Override
	public void updateFileCargoData() {
		final String[] sectorsDataStr = new String[energyBins.size()];
		int k = 0;
		
		// TODO: consider using HashMap to map bin name to each bin, to avoid
		// order index dependency!
		for (final int i : energyBins) {
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
		if (energyData.size() > MIN_SAMPLES) {
			final int newData = energyData.get(energyData.size() - 1);
			guiCargo.setDataByIndex(0, "" + newData);
			final double climbingPercent = 100 * energyBins.get(0)
					/ (double) energyData.size();
			guiCargo.setDataByTag(expParams[0], "" + climbingPercent + "%");
			final double swimmingPercent = 100 * energyBins.get(1)
					/ (double) energyData.size();
			guiCargo.setDataByTag(expParams[1], "" + swimmingPercent + "%");
			final double floatingPercent = 100 * energyBins.get(2)
					/ (double) energyData.size();
			guiCargo.setDataByTag(expParams[2], "" + floatingPercent + "%");
			gui.addPoint(newData);
			
			gui.setEnergyLevels(energyLevels);
		}
	}

}
