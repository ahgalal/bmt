/**
 * 
 */
package modules.experiment.openfield;

import modules.experiment.ExperimentModuleGUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import utils.PManager;
import utils.PManager.ProgramState;
import utils.PManager.ProgramState.GeneralState;
import utils.PManager.ProgramState.StreamState;
import utils.StatusManager.StatusSeverity;
import filters.subtractionfilter.SubtractorFilter;

/**
 * @author Creative
 */
public class OpenFieldExperimentModuleGUI extends ExperimentModuleGUI {
	private Button	btnSetBackground	= null;

	public OpenFieldExperimentModuleGUI(final OpenFieldExperimentModule owner) {
		super(owner);
	}

	/**
	 * Enables/disables the set background button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnSetBackgroundEnable(final boolean enable) {
		btnSetBackground.setEnabled(enable);
	}

	/**
	 * Sets the background of DrawZones window and the VideoManager to the
	 * current cam. image at that instant.
	 */
	public void btnSetbgAction() {
		if (PManager.getDefault().getState().getStream() == StreamState.STREAMING ||
				PManager.getDefault().getState().getStream() == StreamState.PAUSED) {
			PManager.getDefault().drw_zns
					.setBackground(((OpenFieldExperimentModule) owner)
							.updateRGBBackground());
			((SubtractorFilter) PManager.getDefault().getVideoManager()
					.getFilterManager().getFilterByName("SubtractionFilter"))
					.updateBG();
		} else if (PManager.getDefault().getState().getGeneral() == GeneralState.TRACKING)
			PManager.getDefault().statusMgr.setStatus(
					"Background can't be taken while tracking.",
					StatusSeverity.ERROR);
		else
			PManager.getDefault().statusMgr.setStatus(
					"Please start the camera first.", StatusSeverity.ERROR);
	}

	/*
	 * (non-Javadoc)
	 * @see PluggedGUI#initialize(org.eclipse.swt.widgets.Shell,
	 * org.eclipse.swt.widgets.ExpandBar, org.eclipse.swt.widgets.Menu,
	 * org.eclipse.swt.widgets.CoolBar, org.eclipse.swt.widgets.Group)
	 */
	@Override
	public void initialize(final Shell shell, final ExpandBar expandBar,
			final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
		final ExpandItem xpndtmOptions = new ExpandItem(expandBar, SWT.NONE);
		xpndtmOptions.setExpanded(true);
		xpndtmOptions.setText("Open Field");

		final Composite cmpstOptions = new Composite(expandBar, SWT.NONE);
		xpndtmOptions.setControl(cmpstOptions);
		btnSetBackground = new Button(cmpstOptions, SWT.NONE);
		btnSetBackground.setBounds(new Rectangle(10, 10, 109, 25));
		btnSetBackground.setText("Set Background");
		btnSetBackground.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				btnSetbgAction();
			}
		});

		xpndtmOptions.setHeight(xpndtmOptions.getControl().computeSize(
				SWT.DEFAULT, SWT.DEFAULT).y + 10);
	}


	@Override
	public void stateStreamChangeHandler(ProgramState state) {
		switch (state.getStream()) {
			case STREAMING:
				if(state.getGeneral()!=GeneralState.TRACKING)
					btnSetBackgroundEnable(true);
				break;
			case PAUSED:
				if(state.getGeneral()!=GeneralState.TRACKING)
					btnSetBackgroundEnable(true);
				break;
			case IDLE:
				btnSetBackgroundEnable(false);
				break;
			default:
				break;
		}
	}

	@Override
	public void stateGeneralChangeHandler(ProgramState state) {
		if(state.getGeneral()==GeneralState.TRACKING)
			btnSetBackgroundEnable(false);
	}

}
