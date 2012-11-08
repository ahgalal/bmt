package ui;

import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import utils.PManager.ProgramState;
import utils.StateListener;

public abstract class PluggedGUI<OwnerType> implements StateListener {
	protected OwnerType				owner;
	protected ProgramState	programState=new ProgramState(null, null);
	protected Shell					shell;

	public PluggedGUI(final OwnerType owner) {
		this.owner = owner;
	}

//	public abstract void inIdleState();

	public abstract void initialize(Shell shell, ExpandBar expandBar,
			Menu menuBar, CoolBar coolBar, Group grpGraphs);

/*	public abstract void inStreamingState();

	public abstract void inTrackingState();*/
	
	public abstract void stateStreamChangeHandler(ProgramState state);
	public abstract void stateGeneralChangeHandler(ProgramState state);

	@Override
	public void updateProgramState(final ProgramState state) {
		
		final boolean generalStateChanged = (programState==null?true:(state.getGeneral() != programState.getGeneral()));
		final boolean streamStateChanged =(programState==null?true:(state.getStream()!=programState.getStream())); 
		if (generalStateChanged || streamStateChanged)
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					
					if(generalStateChanged)
						stateGeneralChangeHandler(state);
					
					if(streamStateChanged)
						stateStreamChangeHandler(state);
					
/*					switch (state.getGeneral()) {
						case IDLE:
							inIdleState();
							break;

						case TRACKING:
							inTrackingState();
							break;
					}
					switch (state.getStream()) {
						case STREAMING:
							inStreamingState();
							break;
						case PAUSED: // TODO
							inStreamingState();
							break;
						default:
							break;
					}*/
				}

			});
		programState.setStream(state.getStream());
		programState.setGeneral(state.getGeneral());
	}

}
