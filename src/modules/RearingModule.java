package modules;

import utils.video.filters.Data;
import utils.video.filters.rearingdetection.RearingData;

/**
 * Rearing module, keeps record of number of rearings of the rat.
 * 
 * @author Creative
 */
public class RearingModule extends Module
{

	private int rearing_ctr;
	private boolean is_rearing;
	private RearingData rearing_data;
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

		data = new Data[1];
		rearing_configs = configs;
		initialize();
	}

	/**
	 * Increments the rearing counter, called by GUI when manual rearing
	 * detection is active.
	 */
	public void incrementRearingCounter()
	{
		rearing_ctr++;
	}

	/**
	 * Decrements the rearing counter, called by GUI when manual rearing
	 * detection is active.
	 */
	public void decrementRearingCounter()
	{
		rearing_ctr--;
	}

	/**
	 * Gets the number of rearings the rat has made in this experiment.
	 * 
	 * @return number of rearings till now.
	 */
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
	public void registerDataObject(final Data data)
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

	@Override
	public void deRegisterDataObject(Data data)
	{
		if(rearing_data==data)
		{
			rearing_data=null;
			this.data[0]=null;
		}
	}

}
