/***************************************************************************
 *  Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly,
 *  Sarah Hamid and Mohammed Ahmed Ramadan
 *  contact: ceng.ahmedgalal@gmail.com
 *
 *  This file is part of Behavioral Monitoring Tool.
 *
 *  Behavioral Monitoring Tool is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, version 3 of the
 *  License.
 *
 *  Behavioral Monitoring Tool is distributed in the hope that it
 *  will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Behavioral Monitoring Tool.
 *  If not, see <http://www.gnu.org/licenses/>.
 *   
 **************************************************************************/

package modules.rearing;

import modules.Cargo;
import modules.Module;
import modules.ModuleConfigs;
import modules.experiment.Constants;

import org.eclipse.swt.widgets.Shell;

import utils.video.filters.Data;
import utils.video.filters.rearingdetection.RearingFilterData;

/**
 * Rearing module, keeps record of number of rearings of the rat.
 * 
 * @author Creative
 */
public class RearingModule extends Module
{
	private final RearingModuleData rearing_module_data;
	private boolean is_rearing;
	private RearingFilterData rearing_filter_data;
	private final RearingModuleConfigs rearing_configs;

	/**
	 * Initializes the module.
	 * 
	 * @param name
	 *            module instance's name
	 * @param configs
	 *            RearingModuleConfigs to configure the module
	 */
	public RearingModule(final String name, final RearingModuleConfigs configs)
	{
		super(name, configs);
		rearing_module_data = new RearingModuleData("Rearing Module Data");
		this.data = rearing_module_data;
		filters_data = new Data[1];
		rearing_configs = configs;
		initialize();
	}

	/**
	 * Increments the rearing counter, called by GUI when manual rearing
	 * detection is active.
	 */
	public void incrementRearingCounter()
	{
		rearing_module_data.rearing_ctr++;
	}

	/**
	 * Decrements the rearing counter, called by GUI when manual rearing
	 * detection is active.
	 */
	public void decrementRearingCounter()
	{
		rearing_module_data.rearing_ctr--;
	}

	/**
	 * Gets the number of rearings the rat has made in this experiment.
	 * 
	 * @return number of rearings till now.
	 */
	public int getRearingCounter()
	{
		return rearing_module_data.rearing_ctr;
	}

	@Override
	public void updateGUICargoData()
	{
		gui_cargo.setDataByTag(
				Constants.GUI_REARING_COUNTER,
				Integer.toString(rearing_module_data.rearing_ctr));
	}

	@Override
	public void updateFileCargoData()
	{
		file_cargo.setDataByTag(
				Constants.FILE_REARING_COUNTER,
				Integer.toString(rearing_module_data.rearing_ctr));
	}

	@Override
	public void registerFilterDataObject(final Data data)
	{
		if (data instanceof RearingFilterData)
		{
			rearing_filter_data = (RearingFilterData) data;
			this.filters_data[0] = rearing_filter_data;
		}
	}

	@Override
	public void process()
	{
		if (!is_rearing & rearing_filter_data.isRearing()) // it is rearing
			rearing_module_data.rearing_ctr++;
		is_rearing = rearing_filter_data.isRearing();
	}

	@Override
	public void updateConfigs(final ModuleConfigs config)
	{
		rearing_configs.mergeConfigs(config);
	}

	@Override
	public void deInitialize()
	{

	}

	@Override
	public void initialize()
	{
		gui_cargo = new Cargo(new String[] { Constants.GUI_REARING_COUNTER });
		rearing_module_data.rearing_ctr = 0;
		file_cargo = new Cargo(new String[] { Constants.FILE_REARING_COUNTER });
	}

	@Override
	public void deRegisterDataObject(final Data data)
	{
		if (rearing_filter_data == data)
		{
			rearing_filter_data = null;
		}
		this.filters_data[0] = null;
	}

	@Override
	public boolean amIReady(final Shell shell)
	{
		if (rearing_filter_data != null)
			return true;
		return false;
	}

	@Override
	public void registerModuleDataObject(final Data data)
	{
		// TODO Auto-generated method stub
	}

}
