/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package modules.rearing;

import modules.Cargo;
import modules.Module;
import modules.ModuleConfigs;
import modules.ModuleData;
import modules.experiment.Constants;
import modules.experiment.ExperimentType;

import org.eclipse.swt.widgets.Shell;

import utils.Logger.Details;
import utils.PManager;
import filters.Data;
import filters.rearingdetection.RearingFilterData;

/**
 * Rearing module, keeps record of number of rearings of the rat.
 * 
 * @author Creative
 */
public class RearingModule extends
		Module<RearingModuleGUI, RearingModuleConfigs, RearingModuleData> {
	private boolean				is_rearing;
	private RearingFilterData	rearing_filter_data;
	private String[]	expParams=new String[] { Constants.FILE_REARING_COUNTER };

	/**
	 * Initializes the module.
	 * 
	 * @param name
	 *            module instance's name
	 * @param configs
	 *            RearingModuleConfigs to configure the module
	 */
	public RearingModule(final String name, final RearingModuleConfigs configs) {
		super(name, configs);
		data = new RearingModuleData("Rearing Module Data");
		filters_data = new Data[1];
		initialize();
		gui = new RearingModuleGUI(this);
	}

	@Override
	public boolean amIReady(final Shell shell) {
		if (rearing_filter_data != null)
			return true;
		return false;
	}

	/**
	 * Decrements the rearing counter, called by GUI when manual rearing
	 * detection is active.
	 */
	public void decrementRearingCounter() {
		data.rearing_ctr--;
	}

	@Override
	public void deInitialize() {

	}

	@Override
	public void deRegisterDataObject(final Data data) {
		if (rearing_filter_data == data)
			rearing_filter_data = null;
		this.filters_data[0] = null;
	}

	/**
	 * Gets the number of rearings the rat has made in this experiment.
	 * 
	 * @return number of rearings till now.
	 */
	public int getRearingCounter() {
		return data.rearing_ctr;
	}

	/**
	 * Increments the rearing counter, called by GUI when manual rearing
	 * detection is active.
	 */
	public void incrementRearingCounter() {
		data.rearing_ctr++;
	}

	@Override
	public void initialize() {
		PManager.log.print("initializing..", this, Details.VERBOSE);
		guiCargo = new Cargo(new String[] { Constants.GUI_REARING_COUNTER });
		data.rearing_ctr = 0;
		fileCargo = new Cargo(expParams);
		for(String str:expParams)
			data.addParameter(str);
		expType = new ExperimentType[]{ExperimentType.OPEN_FIELD};
		//data.expType=expType;
	}

	@Override
	public void process() {
		if (!is_rearing & rearing_filter_data.isRearing()) // it is rearing
			data.rearing_ctr++;
		is_rearing = rearing_filter_data.isRearing();
	}

	@Override
	public void registerFilterDataObject(final Data data) {
		if (data instanceof RearingFilterData) {
			rearing_filter_data = (RearingFilterData) data;
			this.filters_data[0] = rearing_filter_data;
		}
	}

	@Override
	public void registerModuleDataObject(final ModuleData data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateConfigs(final ModuleConfigs config) {
		configs.mergeConfigs(config);
	}

	@Override
	public void updateFileCargoData() {
		fileCargo.setDataByTag(Constants.FILE_REARING_COUNTER,
				Integer.toString(data.rearing_ctr));
	}

	@Override
	public void updateGUICargoData() {
		guiCargo.setDataByTag(Constants.GUI_REARING_COUNTER,
				Integer.toString(data.rearing_ctr));
	}
}
