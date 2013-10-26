package modules.headmotion;

import java.util.ArrayList;

import modules.Cargo;
import modules.Module;
import modules.ModuleData;
import modules.experiment.Constants;
import modules.experiment.ExperimentType;
import modules.headmotion.HeadMotionModuleData.HistogramSection;

import org.eclipse.swt.widgets.Shell;

import utils.Logger.Details;
import utils.PManager;
import filters.Data;
import filters.headangle.HeadAngleData;

/**
 * Head motion module, keeps track of the angle between rodent's head and body.
 * 
 * @author Creative
 */
public class HeadMotionModule
		extends
		Module<HeadMotionModuleGUI, HeadMotionModuleConfigs, HeadMotionModuleData> {
	private static final int	NUM_ANGLE_SECTIONS	= 8;
	public final static String	moduleID	= Constants.MODULE_ID
													+ ".headmotion";

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
		
		String[]		expParams	= getParams();
		fileCargo = new Cargo(expParams);
		for (final String str : expParams)
			data.addParameter(str);
		expType = new ExperimentType[] {
			ExperimentType.PARKINSON
		};
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Module newInstance(final String name) {
		return new HeadMotionModule(name, null);
	}

	@Override
	public void process() {
		
		int angle = headAngleFilterData.getAngle();
		
		// fix angle if out pf range (in case of having the ears with the same color)
		HistogramSection[] sections = data.getAngleHistogram().getSections();
		if(angle < sections[0].min)
			angle+=180;
		else if(angle > sections[sections.length-1].max)
			angle-=180;
		
		data.addAngleValue(angle);
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
		for(int i=0;i<data.getAngleHistogram().getSections().length;i++){
			HistogramSection section=data.getAngleHistogram().getSections()[i];
			fileCargo.setDataByTag("ANGL_"+section.min+"_"+section.max,data.getHistogramFrequencies()[i]+"");
		}
	}
	
	public static String[] getParams(){
		HeadMotionModuleData data = new HeadMotionModuleData(NUM_ANGLE_SECTIONS);
		ArrayList<String> tmpExpParams = new ArrayList<String>();
		for(HistogramSection section:data.getAngleHistogram().getSections())
			tmpExpParams.add("ANGL_"+section.min+"_"+section.max);
		return tmpExpParams.toArray(new String[0]);
	}

	@Override
	public void updateGUICargoData() {
		guiCargo.setDataByTag(Constants.GUI_HEAD_ANGLE,
				Integer.toString(data.getAngle()));
		
		int[] frequencies = data.getHistogramFrequencies();
		gui.updateHistogramFrequencies(frequencies);
	}
}
