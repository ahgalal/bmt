package modules.fstmovementmeter;

import java.util.ArrayList;

import modules.Cargo;
import modules.Module;
import modules.ModuleConfigs;

import org.eclipse.swt.widgets.Shell;

import utils.video.filters.Data;
import utils.video.filters.movementmeter.MovementMeterData;

public class MovementMeterModule extends Module<MovementMeterModuleGUI,ModuleConfigs,Data>
{
	private MovementMeterData movementMeterFilterData;
	private final ArrayList<Integer> energyData;
	private int time = 0;

	public MovementMeterModule(final String name, final ModuleConfigs config)
	{
		super(name, config);
		filters_data = new Data[1];
		energyData = new ArrayList<Integer>();
		gui = new MovementMeterModuleGUI();
	}

	@Override
	public void initialize()
	{
		gui_cargo = new Cargo(new String[] { "Energy" });
	}

	@Override
	public void deInitialize()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updateConfigs(final ModuleConfigs config)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void process()
	{
		energyData.add(movementMeterFilterData.getWhiteSummation());
	}

	@Override
	public void updateGUICargoData()
	{
		gui_cargo.setData(new String[] { "" + movementMeterFilterData.getWhiteSummation() });
		gui.addPoint(
				time++,
				movementMeterFilterData.getWhiteSummation() / 2000);
	}

	@Override
	public void updateFileCargoData()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void registerFilterDataObject(final Data data)
	{
		if (data instanceof MovementMeterData)
		{
			movementMeterFilterData = (MovementMeterData) data;
			this.filters_data[0] = movementMeterFilterData;
		}
	}

	@Override
	public void registerModuleDataObject(final Data data)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void deRegisterDataObject(final Data data)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean amIReady(final Shell shell)
	{
		if (movementMeterFilterData != null)
			return true;
		else
			return false;
	}

}
