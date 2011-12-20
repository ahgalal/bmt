package modules.movementmeter;

import java.util.ArrayList;

import modules.Cargo;
import modules.Module;
import modules.ModuleConfigs;

import org.eclipse.swt.widgets.Shell;

import utils.Logger.Details;
import utils.PManager;
import utils.video.filters.Data;
import utils.video.filters.movementmeter.MovementMeterData;

public class MovementMeterModule extends
	Module<MovementMeterModuleGUI, ModuleConfigs, Data> {
    private MovementMeterData movementMeterFilterData;
    private final ArrayList<Integer> energyData;
    private int time = 0;
    private final int noEnergyLevels=5;
    private int[] sectorsData;

    public MovementMeterModule(final String name, final ModuleConfigs config) {
	super(name, config);
	filters_data = new Data[1];
	energyData = new ArrayList<Integer>();
	gui = new MovementMeterModuleGUI(this);

	initialize();
    }

    @Override
    public void initialize() {
	PManager.log.print("initializing..", this, Details.VERBOSE);
	guiCargo = new Cargo(new String[] { "Energy" });
	energyData.clear();
	String[] energyLevelsNames = new String[noEnergyLevels];
	for(int i=0;i<noEnergyLevels;i++)
	    energyLevelsNames[i]="eLevel_"+i;
	fileCargo = new Cargo(energyLevelsNames);
    }

    @Override
    public void deInitialize() {
	sectorizeEnergy();
    }

    @Override
    public void updateConfigs(final ModuleConfigs config) {
	// TODO Auto-generated method stub

    }

    @Override
    public void process() {
	energyData.add(movementMeterFilterData.getWhiteSummation());
    }

    @Override
    public void updateGUICargoData() {
	guiCargo.setDataByIndex(0,
		"" + movementMeterFilterData.getWhiteSummation());
	gui.addPoint(time++, movementMeterFilterData.getWhiteSummation() / 2000);
    }

    private void sectorizeEnergy() {
	int max = 0, min = 100000000;
	
	sectorsData = new int[noEnergyLevels];
	for (final int i : energyData)
	    if (i > max)
		max = i;
	    else if (i < min)
		min = i;

	int[] levels = new int[noEnergyLevels];
	int levelHeight = (max - min)/noEnergyLevels;
	
	for(int k=0;k<noEnergyLevels;k++)
	    levels[k]=levelHeight*k;
	
	for(int i:energyData)
	    for(int j=1;j<noEnergyLevels;j++)
		if(i<levels[j] && i> levels[j-1])
		    sectorsData[j-1]++;
	
	
	System.out.println("Sectors data:\n");
	for(int i:sectorsData)
	    System.out.println(i);
	
    }

    @Override
    public void updateFileCargoData() {
	String[] sectorsDataStr = new String[sectorsData.length];
	int k=0;
	for(int i:sectorsData){
	    sectorsDataStr[k]=Integer.toString(i);
	    k++;
	}
	fileCargo.setData(sectorsDataStr);
	
	
/*	fileCargo.setDataByIndex(0,
		"" + movementMeterFilterData.getWhiteSummation());*/
    }

    @Override
    public void registerFilterDataObject(final Data data) {
	if (data instanceof MovementMeterData) {
	    movementMeterFilterData = (MovementMeterData) data;
	    this.filters_data[0] = movementMeterFilterData;
	}
    }

    @Override
    public void registerModuleDataObject(final Data data) {
	// TODO Auto-generated method stub

    }

    @Override
    public void deRegisterDataObject(final Data data) {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean amIReady(final Shell shell) {
	if (movementMeterFilterData != null)
	    return true;
	else
	    return false;
    }

}
