/**
 * 
 */
package modules.experiment.openfield;

import java.awt.Point;
import java.util.ArrayList;

import modules.Module;
import modules.ModulesManager;
import modules.experiment.ExperimentModule;
import modules.experiment.ExperimentModuleGUI;
import modules.zones.ZonesModule;

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

import ui.box.MsgBox;
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
	private static final String	SUBTRACTION_FILTER_NAME	= "SubtractionFilter";
	private Button				btnSetBackground		= null;
	private Composite			cmpstOptions;

	private Point				frameDims;

	private ExpandItem			xpndtmOptions;

	public OpenFieldExperimentModuleGUI(final ExperimentModule owner) {
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
		if ((PManager.getDefault().getState().getStream() == StreamState.STREAMING)
				|| (PManager.getDefault().getState().getStream() == StreamState.PAUSED)) {
			final SubtractorFilter subtractorFilter = (SubtractorFilter) PManager
					.getDefault().getVideoManager().getFilterManager()
					.getFilterByName(SUBTRACTION_FILTER_NAME);
			if (subtractorFilter != null)
				subtractorFilter.updateBG();
			else {
				PManager.getDefault()
						.getStatusMgr()
						.setStatus(
								"Subtractor filter: \""
										+ SUBTRACTION_FILTER_NAME
										+ "\" is not found, please check Filters' setup",
								StatusSeverity.ERROR);
				return;
			}

			ArrayList<Module<?, ?, ?>> zoneModules = ModulesManager.getDefault().getModulesUnderID(ZonesModule.moduleID);
			if(zoneModules.size()==0){
				MsgBox.show(shell, "Cannot find any \"" + ZonesModule.moduleID+ "\" modules loaded, one must be loaded in order to set background", "Error", SWT.ERROR);
				return;
			}
			else if(zoneModules.size()>1){
				MsgBox.show(shell, "More than one \"" + ZonesModule.moduleID+ "\" modules loaded, only one must be loaded in order to set background", "Error", SWT.ERROR);
				return;
			}
			
			// update Background
			((ZonesModule)zoneModules.get(0)).setBackground(
					((OpenFieldExperimentModule) owner)
					.updateRGBBackground(),
			frameDims.x, frameDims.y);
			
		} else if (PManager.getDefault().getState().getGeneral() == GeneralState.TRACKING)
			PManager.getDefault()
					.getStatusMgr()
					.setStatus("Background can't be taken while tracking.",
							StatusSeverity.ERROR);
		else
			PManager.getDefault()
					.getStatusMgr()
					.setStatus("Please start the camera first.",
							StatusSeverity.ERROR);
	}

	@Override
	public void deInitialize() {
		disposeWidget(cmpstOptions);
		disposeWidget(xpndtmOptions);
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
		xpndtmOptions = new ExpandItem(expandBar, SWT.NONE);
		xpndtmOptions.setExpanded(true);
		xpndtmOptions.setText("Open Field");

		cmpstOptions = new Composite(expandBar, SWT.NONE);
		xpndtmOptions.setControl(cmpstOptions);
		btnSetBackground = new Button(cmpstOptions, SWT.NONE);
		btnSetBackground.setBounds(new Rectangle(10, 10, 109, 25));
		btnSetBackground.setText("Snap Background");
		btnSetBackground.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				btnSetbgAction();
			}
		});

		xpndtmOptions.setHeight(xpndtmOptions.getControl().computeSize(
				SWT.DEFAULT, SWT.DEFAULT).y + 10);
	}

	public void setFrameDims(final Point dims) {
		frameDims = dims;
	}

	@Override
	public void stateGeneralChangeHandler(final ProgramState state) {
		if (state.getGeneral() == GeneralState.TRACKING)
			btnSetBackgroundEnable(false);
	}

	@Override
	public void stateStreamChangeHandler(final ProgramState state) {
		switch (state.getStream()) {
			case STREAMING:
				if (state.getGeneral() != GeneralState.TRACKING)
					btnSetBackgroundEnable(true);
				break;
			case PAUSED:
				if (state.getGeneral() != GeneralState.TRACKING)
					btnSetBackgroundEnable(true);
				break;
			case IDLE:
				btnSetBackgroundEnable(false);
				break;
			default:
				break;
		}
	}

}
