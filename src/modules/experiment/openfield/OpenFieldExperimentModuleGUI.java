/**
 * 
 */
package modules.experiment.openfield;

import modules.experiment.ExperimentModule;
import modules.experiment.ExperimentModuleGUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import ui.ExternalStrings;
import utils.PManager;
import utils.PManager.ProgramState;

/**
 * @author Creative
 */
public class OpenFieldExperimentModuleGUI extends ExperimentModuleGUI {

	private MenuItem	mnutmEditOptions;

	public OpenFieldExperimentModuleGUI(final ExperimentModule owner) {
		super(owner);
	}

	@Override
	public void deInitialize() {
		disposeWidget(mnutmEditOptions);
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

		Menu mnuEdit = null;
		for (final MenuItem miOut : menuBar.getItems())
			if (miOut.getText().equals("Edit"))
				mnuEdit = miOut.getMenu();
		mnutmEditOptions = new MenuItem(mnuEdit, SWT.PUSH);
		mnutmEditOptions.setText(ExternalStrings.get("MainGUI.Menu.Options"));

		mnutmEditOptions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				// TODO: this will be changed when using the extensible
				// configuration dialog mechanism
				PManager.mainGUI.mnutmEditOptionsAction();
			}
		});
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
