/**
 * 
 */
package filters.subtractionfilter;

import java.awt.Point;
import java.util.ArrayList;

import modules.Module;
import modules.ModulesManager;
import modules.experiment.openfield.OpenFieldExperimentModule;
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

import ui.PluggedGUI;
import ui.box.MsgBox;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.PManager.ProgramState.GeneralState;
import utils.PManager.ProgramState.StreamState;
import utils.StatusManager.StatusSeverity;

/**
 * @author Creative
 */
public class SubtractorFilterGUI extends PluggedGUI<SubtractorFilter> {
	private Button		btnSetBackground	= null;
	private Composite	cmpstSubtractionFilter;

	private Point		frameDims;
	private ExpandItem	xpndtmSubtractionFilter;

	// private static final String SUBTRACTION_FILTER_NAME =
	// "SubtractionFilter";
	public SubtractorFilterGUI(final SubtractorFilter owner) {
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
			final int[] bg = owner.updateBG();

			// update Background for ZonesModule
			// TODO: decouple this filter from zones module!
			final ZonesModule zonesModule = (ZonesModule) getModule(ZonesModule.moduleID);
			zonesModule.setBackground(bg, frameDims.x, frameDims.y);
			
			// update Background for OpenFieldModule
			OpenFieldExperimentModule openFieldModule = (OpenFieldExperimentModule) getModule(OpenFieldExperimentModule.moduleID);
			openFieldModule.setBG();

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
		disposeWidget(cmpstSubtractionFilter);
		disposeWidget(xpndtmSubtractionFilter);

	}

	private Module getModule(final String id) {
		Module module = null;
		final ArrayList<Module<?, ?, ?>> modules = ModulesManager.getDefault()
				.getModulesUnderID(id);
		if (modules.size() == 0) {
			MsgBox.show(
					shell,
					"Cannot find any \""
							+ id
							+ "\" modules loaded, one must be loaded in order to set background",
					"Error", SWT.ERROR);
			return null;
		} else if (modules.size() > 1) {
			MsgBox.show(
					shell,
					"More than one \""
							+ id
							+ "\" modules loaded, only one must be loaded in order to set background",
					"Error", SWT.ERROR);
			return null;
		}
		module = modules.get(0);
		return module;
	}

	@Override
	public void initialize(final Shell shell, final ExpandBar expandBar,
			final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
		xpndtmSubtractionFilter = new ExpandItem(expandBar, SWT.NONE);
		xpndtmSubtractionFilter.setExpanded(true);
		xpndtmSubtractionFilter.setText("Open Field");

		cmpstSubtractionFilter = new Composite(expandBar, SWT.NONE);
		xpndtmSubtractionFilter.setControl(cmpstSubtractionFilter);
		btnSetBackground = new Button(cmpstSubtractionFilter, SWT.NONE);
		btnSetBackground.setBounds(new Rectangle(10, 10, 109, 25));
		btnSetBackground.setText("Snap Background");
		btnSetBackground.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				btnSetbgAction();
			}
		});

		xpndtmSubtractionFilter.setHeight(xpndtmSubtractionFilter.getControl().computeSize(
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
