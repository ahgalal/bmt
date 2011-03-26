package modules;

import utils.video.processors.Data;
import utils.video.processors.rearingdetection.RearingData;

public class RearingModule extends Module
{

	private int rearing_ctr;
	private boolean is_rearing;
	private RearingData rearing_data;
	private RearingModuleConfigs rearing_configs;

	// private RearingConfigs

	public RearingModule(final String name, final RearingModuleConfigs configs)
	{
		super(name, configs);

		data = new Data[1];
		rearing_configs=configs;
		initialize();
	}

	public void incrementRearingCounter()
	{
		rearing_ctr++;
	}

	public void decrementRearingCounter()
	{
		rearing_ctr--;
	}

	public int getRearingCounter()
	{
		return rearing_ctr;
	}

	@Override
	public void updateGUICargoData()
	{
		gui_cargo.setDataByIndex(0, Integer.toString(rearing_ctr));
	}

	@Override
	public void updateFileCargoData()
	{
		file_cargo.setDataByIndex(0, Integer.toString(rearing_ctr));
	}

	@Override
	public void updateDataObject(final Data data)
	{
		if (data instanceof RearingData)
		{
			rearing_data = (RearingData) data;
			this.data[0] = rearing_data;
		}
	}

	@Override
	public void process()
	{
		if (!is_rearing & rearing_data.isRearing()) // it is rearing
			rearing_ctr++;
		is_rearing = rearing_data.isRearing();
	}

	@Override
	public void updateConfigs(final ModuleConfigs config)
	{
		rearing_configs.mergeConfigs(config);
	}

	@Override
	public void deInitialize()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize()
	{
		gui_cargo = new Cargo(new String[] { "Rearing Counter" });

		file_cargo = new Cargo(new String[] { "RRNG" });
	}

}
