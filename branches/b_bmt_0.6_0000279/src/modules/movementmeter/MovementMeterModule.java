package modules.movementmeter;

import java.util.ArrayList;

import modules.Cargo;
import modules.Module;
import modules.ModuleConfigs;
import modules.ModuleData;
import modules.experiment.Constants;
import modules.experiment.ExperimentType;
import modules.session.SessionModuleData;

import org.eclipse.swt.widgets.Shell;

import utils.Logger.Details;
import utils.PManager;
import filters.Data;
import filters.movementmeter.MovementMeterData;

public class MovementMeterModule extends
		Module<MovementMeterModuleGUI, ModuleConfigs, MovementMeterModuleData> {
	private static final int			ENERGY_DIVISION_FACTOR	= 200;
	private static final int			IGNORED_FRAMES			= 30;
	private static final int			MIN_SAMPLES				= 31;
	public final static String			moduleID				= Constants.MODULE_ID
																		+ ".movementmeter";
	private static final int			OLD_VAL_COUNT			= 20;
	int									accumulatedSessionTime;
	private ArrayList<Integer>			energyBins;
	private final ArrayList<Integer>	energyData;
	private int[]						energyLevels;
	private String[]					expParams;
	private boolean						ignoredFramesNormalized;
	private int							maxEnergy;
	private int							minEnergy;
	private MovementMeterData			movementMeterFilterData;
	private final int					noEnergyLevels			= 3;
	private final int[]					oldValues				= new int[OLD_VAL_COUNT];
	private int							sectorizeFlag;
	private SessionModuleData			sessionModuleData;

	public MovementMeterModule(final ModuleConfigs config) {
		super(config);
		filtersData = new Data[1];
		energyData = new ArrayList<Integer>();
		gui = new MovementMeterModuleGUI(this);
		data = new MovementMeterModuleData();
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
		// sectorizeEnergy();
	}

	@Override
	public void deRegisterDataObject(final Data data) {
	}

	private String formatTime(final double time) {
		return String.format("%.1f", time);
	}

	//@formatter:off	
	/**
	 * .............A
	 * .............|....................___
	 * Climbing (0).|-------------------/---\---------
	 * .............|..........________/.....\
	 * .............|........./...............\
	 * Swimming (1).|--------/-----------------\------
	 * .............|.....__/...................\___
	 * .............|..../
	 * Floating (2).|---/-----------------------------
	 * .............|../
	 * .............|./
	 * .............|/_________________________________
	 * ...............................................
	 * @formatter:on
	 */

	@Override
	public String getID() {
		return moduleID;
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

		// ////// in case of 3 levels:
		expParams[0] = Constants.CLIMBING;
		expParams[1] = Constants.SWIMMING;
		expParams[2] = Constants.FLOATING;
		// //////

		fileCargo = new Cargo(expParams);
		guiCargo = new Cargo(expParams);

		for (final String param : expParams)
			data.addParameter(param);

		expType = new ExperimentType[] { ExperimentType.FORCED_SWIMMING };
		maxEnergy = 0;
		minEnergy = 100000000;
		ignoredFramesNormalized = false;
		sectorizeFlag=0;
	}

	@Override
	public void process() {
		int newVal = movementMeterFilterData.getWhiteSummation()
				/ ENERGY_DIVISION_FACTOR;
		final int size = energyData.size();
		if (size > MIN_SAMPLES + IGNORED_FRAMES) {
			for (int i = 0; i < OLD_VAL_COUNT; i++) {
				oldValues[i] = energyData.get(size - i - 1);
			}

			// filter out abrupt changes (spikes)
			if (Math.abs(newVal - oldValues[0]) > getMaxEnergy() / 2)
				newVal = oldValues[0];// +(newVal - oldVal1)/5;

			newVal = smoothCurve(newVal);

			// smooth curve
			// newVal = (int) (0.1*newVal+0.9*oldVal1);

			if (newVal > getMaxEnergy())
				setMaxEnergy(newVal);

			if (newVal < getMinEnergy())
				setMinEnergy(newVal);
		}
		energyData.add(newVal);

		// normalize ignored frames
		if ((size > IGNORED_FRAMES) && !ignoredFramesNormalized) {
			for (int i = 0; i < IGNORED_FRAMES; i++)
				energyData.add(i, newVal);
			ignoredFramesNormalized = true;
		}

		sectorizeFlag++;
		if (sectorizeFlag % 30 == 0)
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
		if (data instanceof SessionModuleData)
			sessionModuleData = (SessionModuleData) data;
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

		accumulatedSessionTime = sessionModuleData.getAccumulatedSessionTime() / 1000;
		final double climbingTime = accumulatedSessionTime * energyBins.get(0)
				/ (double) energyData.size();
		guiCargo.setDataByTag(expParams[0], formatTime(climbingTime) + " s");
		final double swimmingTime = accumulatedSessionTime * energyBins.get(1)
				/ (double) energyData.size();
		guiCargo.setDataByTag(expParams[1], formatTime(swimmingTime) + " s");
		final double floatingTime = accumulatedSessionTime * energyBins.get(2)
				/ (double) energyData.size();
		guiCargo.setDataByTag(expParams[2], formatTime(floatingTime) + " s");
	}

	private void setMaxEnergy(final int maxEnergy) {
		this.maxEnergy = maxEnergy;
		updateEnergyLevels();
	}

	private void setMinEnergy(final int minEnergy) {
		this.minEnergy = minEnergy;
		updateEnergyLevels();
	}

	private int smoothCurve(final int newVal) {
		int sum = 0;
		for (int i = 0; i < OLD_VAL_COUNT; i++) {
			sum += oldValues[i];
		}
		final int avg = sum / OLD_VAL_COUNT;
		return (int) (0.3 * newVal + 0.7 * avg);
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
		/*
		 * final String[] sectorsDataStr = new String[energyBins.size()]; int k
		 * = 0; // TODO: consider using HashMap to map bin name to each bin, to
		 * avoid // order index dependency! for (final int i : energyBins) {
		 * sectorsDataStr[k] = Double.toString(100*i/(double)energyData.size());
		 * k++; } fileCargo.setData(sectorsDataStr);
		 */
		accumulatedSessionTime = sessionModuleData.getAccumulatedSessionTime() / 1000;
		final double climbingTime = accumulatedSessionTime * energyBins.get(0)
				/ (double) energyData.size();
		fileCargo.setDataByTag(expParams[0], formatTime(climbingTime));
		final double swimmingTime = accumulatedSessionTime * energyBins.get(1)
				/ (double) energyData.size();
		fileCargo.setDataByTag(expParams[1], formatTime(swimmingTime));
		final double floatingTime = accumulatedSessionTime * energyBins.get(2)
				/ (double) energyData.size();
		fileCargo.setDataByTag(expParams[2], formatTime(floatingTime));
		/*
		 * fileCargo.setDataByIndex(0, "" +
		 * movementMeterFilterData.getWhiteSummation());
		 */
	}

	@Override
	public void updateGUICargoData() {
		if (energyData.size() > MIN_SAMPLES) {
			final int newData = energyData.get(energyData.size() - 1);
			gui.addPoint(newData);

			gui.setEnergyLevels(energyLevels);
		}
	}

}
