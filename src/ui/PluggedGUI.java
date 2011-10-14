package ui;

import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import utils.PManager;
import utils.PManager.ProgramState;
import utils.StateListener;

public abstract class PluggedGUI implements StateListener
{
	protected Shell shell;
	protected PManager.ProgramState programState;

	public abstract void initialize(
			Shell shell,
			ExpandBar expandBar,
			Menu menuBar,
			CoolBar coolBar);

	@Override
	public void updateProgramState(final ProgramState state)
	{
		if (state != programState)
		{
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run()
				{
					switch (state)
					{
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
		}
		programState = state;
	}

	public abstract void inIdleState();

	public abstract void inStreamingState();

	public abstract void inTrackingState();

}
