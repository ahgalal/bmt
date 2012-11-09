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
import utils.PManager.ProgramState;

public class MovementMeterModuleGUI extends PluggedGUI<MovementMeterModule> {
	private Composite	cmpstPlotter;

	private Plotter		plotter;

	public MovementMeterModuleGUI(final MovementMeterModule owner) {
		super(owner);
	}

	public void addPoint(final int x, final int y) {
		plotter.addPoint(x, y);
	}

	@Override
	public void deInitialize() {
		cmpstPlotter.dispose();
	}

	@Override
	public void initialize(final Shell shell, final ExpandBar expandBar,
			final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
		cmpstPlotter = new Composite(grpGraphs, SWT.NONE);
		cmpstPlotter.setBounds(10, 20, 232, 135);

		plotter = new Plotter(cmpstPlotter);
		plotter.initialize();
	}

	@Override
	public void stateGeneralChangeHandler(final ProgramState state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateStreamChangeHandler(final ProgramState state) {
		// TODO Auto-generated method stub

	}

}
