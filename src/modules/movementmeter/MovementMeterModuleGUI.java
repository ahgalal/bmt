package modules.movementmeter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import plotter.Plotter;

import ui.PluggedGUI;

public class MovementMeterModuleGUI extends PluggedGUI<MovementMeterModule>
{
	public MovementMeterModuleGUI(MovementMeterModule owner)
	{
		super(owner);
	}

	private Composite cmpstPlotter;
	private Plotter plotter;
	@Override
	public void initialize(
			Shell shell,
			ExpandBar expandBar,
			Menu menuBar,
			CoolBar coolBar,
			Group grpGraphs)
	{
		this.owner=owner;
		cmpstPlotter = new Composite(grpGraphs, SWT.NONE);
		cmpstPlotter.setBounds(10, 20, 232, 135);

		plotter = new Plotter(cmpstPlotter);
		plotter.initialize();
	}
	
	public void addPoint(int x,int y)
	{
		plotter.addPoint(x, y);
	}

	@Override
	public void inIdleState()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void inStreamingState()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void inTrackingState()
	{
		// TODO Auto-generated method stub

	}

}
