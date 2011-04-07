package modules.rearing;

import modules.ModulesManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import utils.PManager;
import utils.PManager.ProgramState;

/**
 * GUI class for the rearing module.
 * 
 * @author Creative
 */
public class RearingModuleGUI
{
	private Button btn_sub_rearing = null;
	private Button btn_add_rearing = null;

	/**
	 * Initializes/shows the GUI components.
	 * 
	 * @param parent
	 *            parent composite that the components will be children of
	 */
	public RearingModuleGUI(final Composite parent)
	{
		btn_sub_rearing = new Button(parent, SWT.NONE);
		btn_sub_rearing.setBounds(new Rectangle(44, 528, 28, 21));
		btn_sub_rearing.setText("-");
		btn_sub_rearing.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				btnSubRearingAction();
			}
		});
		btn_add_rearing = new Button(parent, SWT.NONE);
		btn_add_rearing.setText("+");
		btn_add_rearing.setSize(new Point(28, 21));
		btn_add_rearing.setLocation(new Point(14, 528));

		btn_add_rearing.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				btnAddRearingAction();
			}
		});
	}

	/**
	 * Handles the "Subtract Rearing" button click action.
	 */
	public void btnSubRearingAction()
	{
		if (PManager.getDefault().state == ProgramState.TRACKING
				|| PManager.getDefault().state == ProgramState.RECORDING)
			((RearingModule) ModulesManager.getDefault()
					.getModuleByName("Rearing Module")).decrementRearingCounter();
	}

	/**
	 * Handles the "Add Rearing" button click action.
	 */
	public void btnAddRearingAction()
	{
		if (PManager.getDefault().state == ProgramState.TRACKING
				|| PManager.getDefault().state == ProgramState.RECORDING)
			((RearingModule) ModulesManager.getDefault()
					.getModuleByName("Rearing Module")).incrementRearingCounter();
	}
}
