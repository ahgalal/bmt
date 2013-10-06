package modules.movementmeter;

import java.util.ArrayList;

import modules.Cargo;
import modules.Module;
import modules.ModuleData;
import modules.experiment.Constants;
import modules.experiment.ExperimentType;
import modules.session.SessionModuleData;

import org.eclipse.swt.widgets.Shell;

import utils.Logger.Details;
import utils.PManager;
import utils.PManager.ProgramState.StreamState;
import filters.Data;
import filters.movementmeter.MovementMeterData;

public class MovementMeterModule
		extends
		Module<MovementMeterModuleGUI, MovementMeterModuleConfigs, MovementMeterModuleData> {
	private static final int			ENERGY_DIVISION_FACTOR	= 200;
	private static final int			GARBAGE_FRAMES			= 10;
	private static final int			MIN_SAMPLES				= 31;
	public final static String			moduleID				= Constants.MODULE_ID
																		+ ".movementmeter";
	private static final int			OLD_VAL_COUNT			= 40;
	private ArrayList<Integer>			energyBins;
	ArrayList<Integer>					energyDataRaw			= new ArrayList<Integer>();
	private final ArrayList<Integer>	energyDataSmooth;
	private int[]						energyLevels;
	private double[]					energyLevelsRatio;
	private String[]					expParams;
	private boolean						ignoredFramesNormalized;
	private int							maxEnergy;
	private int							minEnergy;
	private MovementMeterData			movementMeterFilterData;
	private final int					noEnergyLevels			= 2;
	private final int[]					oldValues				= new int[OLD_VAL_COUNT];
	private long						pauseTime				= 0;

	private long						pauseTimeStamp;

	private int							sectorizeFlag;

	private SessionModuleData			sessionModuleData;

	private long						startTimeStamp			= 0;

	public MovementMeterModule(final String name,
			final MovementMeterModuleConfigs config) {
		super(name, config);
		filtersData = new Data[1];
		energyDataSmooth = new ArrayList<Integer>();
		gui = new MovementMeterModuleGUI(this, noEnergyLevels);
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

	// @formatter:off
	/**
	 *              A
	 *              |                    ___ 
	 *  Climbing(0) |-------------------/---\---------
	 *              |          ________/     \
	 *              |         /               \
	 *  Swimming(1) |--------/-----------------\------
	 *              |     __/                   \___ 
	 *              |    /
	 * Floating (2) |---/----------------------------- 
	 *              |  /
	 *              | / 
	 *              |/_________________________________
	 *                                                
	 * 
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
		energyDataSmooth.clear();
		energyDataRaw.clear();
		expParams = new String[noEnergyLevels];
		energyBins = new ArrayList<Integer>();
		energyLevels = new int[noEnergyLevels];
		energyLevelsRatio = new double[] { 1, 0.2 };

		for (int i = 0; i < noEnergyLevels; i++) {
			expParams[i] = "eLevel_" + i;
			energyBins.add(0);
		}

		// ////// in case of 3 levels:
		// expParams[0] = Constants.CLIMBING;
		expParams[0] = Constants.SWIMMING;
		expParams[1] = Constants.FLOATING;
		// //////

		fileCargo = new Cargo(expParams);
		guiCargo = new Cargo(expParams);

		for (final String param : expParams)
			data.addParameter(param);

		expType = new ExperimentType[] { ExperimentType.FORCED_SWIMMING };
		maxEnergy = 0;
		minEnergy = 100000000;
		ignoredFramesNormalized = false;
		sectorizeFlag = 0;

		pauseTime = 0;
		pauseTimeStamp = 0;
		startTimeStamp = 0;
	}

	private void initializeStartTimestamp() {
		if (startTimeStamp == 0) {
			startTimeStamp = System.currentTimeMillis();
			// handling the case when tracking is started when stream is paused,
			// we need to set the pause timestamp to be the same as start time
			// stamp, because tracking is paused at the same instant it is
			// started.
			if (PManager.getDefault().getState().getStream() == StreamState.PAUSED)
				pauseTimeStamp = startTimeStamp;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Module newInstance(final String name) {
		return new MovementMeterModule(name, null);
	}

	@Override
	public void pause() {
		pauseTimeStamp = System.currentTimeMillis();
		super.pause();
	}

	@Override
	public void process() {

		initializeStartTimestamp();

		int newVal = movementMeterFilterData.getWhiteSummation()
				/ ENERGY_DIVISION_FACTOR;
		final int size = energyDataSmooth.size();

		// Semi-Stable: size > IGNORED_FRAMES & ignored frames are not added yet
		// normalize ignored frames
		if (/* (size <= MIN_SAMPLES IGNORED_FRAMES) || */!ignoredFramesNormalized) {

			// add average to energy array
			energyDataSmooth.add(newVal);
			energyDataRaw.add(newVal);

			// System.out.println("size: "+ size +" val: "+newVal);

			if (size > MIN_SAMPLES + GARBAGE_FRAMES) {
				// calculate average, ignoring garbage frames
				int sum = 0;
				for (int i = 0; i < energyDataSmooth.size(); i++) {
					if (i > GARBAGE_FRAMES)
						sum += energyDataSmooth.get(i);
				}
				sum += newVal;
				final int avg = sum / (size + 1 - GARBAGE_FRAMES);

				// System.out.println("avg: "+avg);

				// set all energy values in the interval [0,MIN_SAMPLES] to the
				// avg value
				for (int i = 0; i < energyDataSmooth.size(); i++) {
					energyDataSmooth.set(i, avg);
					energyDataRaw.set(i, avg);
				}

				// set flag to prevent executing this part again
				ignoredFramesNormalized = true;
			}
		}

		// Stable: size > MIN_SAMPLES + IGNORED_FRAMES
		else if (size > MIN_SAMPLES + GARBAGE_FRAMES /* + IGNORED_FRAMES */) {
			for (int i = 0; i < OLD_VAL_COUNT; i++) {
				// oldValues[i] = energyData.get(size - i - 1);
				oldValues[i] = energyDataRaw.get(size - i - 1);
			}
			// filter out abrupt changes (spikes)
			if (Math.abs(newVal - oldValues[0]) > getMaxEnergy() / 2)
				newVal = oldValues[0];// +(newVal - oldVal1)/5;

			newVal = smoothCurve(newVal);

			if (newVal > getMaxEnergy())
				setMaxEnergy(newVal);

			if (newVal < getMinEnergy())
				setMinEnergy(newVal);
			energyDataSmooth.add(newVal);
			energyDataRaw.add(newVal);
		}

		sectorizeFlag++;
		if (sectorizeFlag % 30 == 0) {
			sectorizeEnergy();
			updateGUISectors();
		}
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

	@Override
	public void resume() {
		if (pauseTimeStamp > 0) { // we were paused before, so we must calculate
									// pause time
			final long resumeTimeStamp = System.currentTimeMillis();
			pauseTime += resumeTimeStamp - pauseTimeStamp;

		}
		super.resume();
	}

	private void sectorizeEnergy() {
		// clear bins
		for (int i = 0; i < noEnergyLevels; i++)
			energyBins.set(i, 0);

		// sectorize
		for (final int energy : energyDataSmooth) {
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

		/*
		 * double[] numCeoff={0.0321,0.0642,0.0321}; // Wn=0.13 (2 Hz) double[]
		 * denCeoff={1.0000,-1.4331,0.5615};
		 */

		final double[] numCeoff = { 0.1041, 0.2082, 0.1041 }; // Wn=0.26 (4 Hz)
		final double[] denCeoff = { 1.0000, -0.9034, 0.3197 };

		/*
		 * double[] numCeoff={0.2929,0.5858,0.2929}; // Wn=0.5 (7.5 Hz) double[]
		 * denCeoff={1.0000,-0.0000,0.1716};
		 */

		final double num = numCeoff[0] * newVal + numCeoff[1] * oldValues[0]
				+ numCeoff[2] * oldValues[1];
		final double part2 = denCeoff[1] * oldValues[0] + denCeoff[2]
				* oldValues[1];

		final double filteredVal = num - part2;

		// System.out.println(oldValues[1] +" " + oldValues[0]+ " " +
		// newVal+" : " + filteredVal);
		return (int) Math.round(filteredVal);
	}

	private void updateEnergyLevels() {
		final int energySwing = getMaxEnergy() - getMinEnergy();

		for (int i = 0; i < energyLevels.length; i++) {
			energyLevels[i] = (int) (energyLevelsRatio[i] * energySwing);
		}

	}

	@Override
	public void updateFileCargoData() {
		sectorizeEnergy();

		final int accumulatedSessionTime = sessionModuleData
				.getAccumulatedSessionTime();
		System.out.println(accumulatedSessionTime);
		for (int i = 0; i < noEnergyLevels; i++) {
			final double time = accumulatedSessionTime * energyBins.get(i)
					/ (double) (energyDataSmooth.size() * 1000);
			fileCargo.setDataByTag(expParams[i], formatTime(time));
		}
	}

	@Override
	public void updateGUICargoData() {
		/*
		 * to prevent taking last energy value in the interval [0,MIN_SAMPLES]
		 * before normalization
		 */
		if (energyDataSmooth.size() > MIN_SAMPLES + GARBAGE_FRAMES + 10) {
			final int newData = energyDataSmooth
					.get(energyDataSmooth.size() - 1);

			final double timeDelta = (System.currentTimeMillis()
					- startTimeStamp - pauseTime)
					/ (double) 1000;
			gui.addPoint(timeDelta, newData);

/*			System.out.println("startTimeStamp: " + startTimeStamp);
			System.out.println("pauseTimeStamp:" + pauseTimeStamp);
			System.out.println("total pause time: " + pauseTime);*/

			gui.setEnergyLevels(energyLevels);
		}
	}

	private void updateGUISectors() {
		final int accumulatedSessionTime = sessionModuleData
				.getAccumulatedSessionTime() / 1000;
		for (int i = 0; i < noEnergyLevels; i++) {
			final double time = accumulatedSessionTime * energyBins.get(i)
					/ (double) energyDataSmooth.size();
			guiCargo.setDataByTag(expParams[i], formatTime(time) + " s");
		}
	}

}
