package ui;

import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import utils.PManager;
import utils.PManager.ProgramState;
import utils.StateListener;

public abstract class PluggedGUI<OwnerType> implements StateListener {
	protected OwnerType				owner;
	protected PManager.ProgramState	programState;
	protected Shell					shell;

	public PluggedGUI(final OwnerType owner) {
		this.owner = owner;
	}

	public abstract void inIdleState();

	public abstract void initialize(Shell shell, ExpandBar expandBar,
			Menu menuBar, CoolBar coolBar, Group grpGraphs);

	public abstract void inStreamingState();

	public abstract void inTrackingState();

	@Override
	public void updateProgramState(final ProgramState state) {
		if (state != programState)
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					switch (state) {
						case IDLE:
							inIdleState();
							break;
						case STREAMING:
							inStreamingState();
							break;
						case TRACKING:
							inTrackingState();
							break;
					}
				}
			});
		programState = state;
	}

}
