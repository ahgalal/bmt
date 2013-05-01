package ui;

import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import utils.PManager.ProgramState;
import utils.PManager.ProgramState.GeneralState;
import utils.StateListener;

public abstract class PluggedGUI<OwnerType> implements StateListener {
	protected OwnerType		owner;
	protected ProgramState	programState	= new ProgramState(null, null);
	protected Shell			shell;
	private boolean initialized;

	public PluggedGUI(final OwnerType owner) {
		this.owner = owner;
	}
	
	public void disposeWidget(Widget widget){
		if(widget!=null && !widget.isDisposed())
			widget.dispose();
	}

	public abstract void deInitialize();

	public abstract void initialize(Shell shell, ExpandBar expandBar,
			Menu menuBar, CoolBar coolBar, Group grpGraphs);

	public abstract void stateGeneralChangeHandler(ProgramState state);

	public abstract void stateStreamChangeHandler(ProgramState state);
	
	public void trackingStoppedHandler(){
		
	}
	public void trackingStartedHandler() {
		
	}

	@Override
	public void updateProgramState(final ProgramState state) {

		final boolean generalStateChanged = (programState == null ? true
				: (state.getGeneral() != programState.getGeneral()));
		final boolean streamStateChanged = (programState == null ? true
				: (state.getStream() != programState.getStream()));
		if (generalStateChanged || streamStateChanged)
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {

					if (generalStateChanged)
						stateGeneralChangeHandler(state);

					if (streamStateChanged)
						stateStreamChangeHandler(state);
					
					if(programState.getGeneral()==GeneralState.TRACKING &&
							state.getGeneral()==GeneralState.IDLE)
						trackingStoppedHandler();
					else if(programState.getGeneral()==GeneralState.IDLE &&
							state.getGeneral()==GeneralState.TRACKING)
						trackingStartedHandler();
					//stateUpdated=true;
				}



			});
		
/*		// wait till state is updated (use this mechanism instead of using Display.syncExec())
		while(!stateUpdated)
			Utils.sleep(20);
		stateUpdated=false;*/
		programState.setStream(state.getStream());
		programState.setGeneral(state.getGeneral());
	}

	protected void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isInitialized() {
		return initialized;
	}

}
