package modules.headmotion;

import modules.Cargo;
import modules.Module;
import modules.ModuleData;
import modules.experiment.Constants;
import modules.experiment.ExperimentType;

import org.eclipse.swt.widgets.Shell;

import utils.Logger.Details;
import utils.PManager;
import filters.Data;
import filters.headangle.HeadAngleData;

/**
 * Rearing module, keeps record of number of rearings of the rat.
 * 
 * @author Creative
 */
public class HeadMotionModule
		extends
		Module<HeadMotionModuleGUI, HeadMotionModuleConfigs, HeadMotionModuleData> {
	private static final int	NUM_ANGLE_SECTIONS	= 8;
	public final static String	moduleID	= Constants.MODULE_ID
													+ ".headmotion";
	private final String[]		expParams	= new String[] {
												Constants.FILE_HEAD_ANGLE
											};

	private HeadAngleData		headAngleFilterData;

	/**
	 * Initializes the module.
	 * 
	 * @param name
	 *            module instance's name
	 * @param configs
	 *            RearingModuleConfigs to configure the module
	 */
	public HeadMotionModule(final String name,
			final HeadMotionModuleConfigs configs) {
		super(name, configs);
		
		filtersData = new Data[1];
		initialize();
		gui = new HeadMotionModuleGUI(this,NUM_ANGLE_SECTIONS);
	}

	@Override
	public boolean amIReady(final Shell shell) {
		if (headAngleFilterData != null)
			return true;
		return false;
	}

	@Override
	public void deInitialize() {

	}

	@Override
	public void deRegisterDataObject(final Data data) {
		if (headAngleFilterData == data)
			headAngleFilterData = null;
		this.filtersData[0] = null;
	}

	/**
	 * Gets the number of rearings the rat has made in this experiment.
	 * 
	 * @return number of rearings till now.
	 */
	public int getHeadAngle() {
		return data.getAngle();
	}

	@Override
	public String getID() {
		return moduleID;
	}

	@Override
	public void initialize() {
		PManager.log.print("initializing..", this, Details.VERBOSE);
		data = new HeadMotionModuleData(NUM_ANGLE_SECTIONS);
		guiCargo = new Cargo(new String[] {
			Constants.GUI_HEAD_ANGLE
		});
		data.setAngle(0);
		fileCargo = new Cargo(expParams);
		for (final String str : expParams)
			data.addParameter(str);
		expType = new ExperimentType[] {
			ExperimentType.PARKINSON
		};
		// data.expType=expType;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Module newInstance(final String name) {
		return new HeadMotionModule(name, null);
	}

	@Override
	public void process() {
		data.addAngleValue(headAngleFilterData.getAngle());
	}

	@Override
	public void registerFilterDataObject(final Data data) {
		if (data instanceof HeadAngleData) {
			headAngleFilterData = (HeadAngleData) data;
			this.filtersData[0] = headAngleFilterData;
		}
	}

	@Override
	public void registerModuleDataObject(final ModuleData data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateFileCargoData() {
		fileCargo.setDataByTag(Constants.FILE_HEAD_ANGLE,
				Integer.toString(data.getAngle()));
	}

	@Override
	public void updateGUICargoData() {
		guiCargo.setDataByTag(Constants.GUI_HEAD_ANGLE,
				Integer.toString(data.getAngle()));
		
		int[] frequencies = data.getHistogramFrequencies();
		gui.updateHistogramFrequencies(frequencies);
	}
}
