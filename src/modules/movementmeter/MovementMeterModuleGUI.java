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

public class MovementMeterModuleGUI extends PluggedGUI<MovementMeterModule> {
    public MovementMeterModuleGUI(final MovementMeterModule owner) {
	super(owner);
    }

    private Composite cmpstPlotter;
    private Plotter plotter;

    @Override
    public void initialize(final Shell shell, final ExpandBar expandBar,
	    final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
	this.owner = owner;
	cmpstPlotter = new Composite(grpGraphs, SWT.NONE);
	cmpstPlotter.setBounds(10, 20, 232, 135);

	plotter = new Plotter(cmpstPlotter);
	plotter.initialize();
    }

    public void addPoint(final int x, final int y) {
	plotter.addPoint(x, y);
    }

    @Override
    public void inIdleState() {
	// TODO Auto-generated method stub

    }

    @Override
    public void inStreamingState() {
	// TODO Auto-generated method stub

    }

    @Override
    public void inTrackingState() {
	// TODO Auto-generated method stub

    }

}
