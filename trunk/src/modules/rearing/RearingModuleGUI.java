/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package modules.rearing;

import modules.ModulesManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
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
import utils.PManager;
import utils.PManager.ProgramState;

/**
 * GUI class for the rearing module.
 * 
 * @author Creative
 */
public class RearingModuleGUI extends PluggedGUI<RearingModule> {
	private Button	btn_add_rearing	= null;

	private Button	btn_sub_rearing	= null;

	public RearingModuleGUI(final RearingModule owner) {
		super(owner);
	}

	/**
	 * Initializes/shows the GUI components.
	 * 
	 * @param parent
	 *            parent composite that the components will be children of
	 */
	/*
	 * public RearingModuleGUI(final Composite parent) { }
	 */

	/**
	 * Handles the "Add Rearing" button click action.
	 */
	public void btnAddRearingAction() {
		if (PManager.getDefault().state == ProgramState.TRACKING
		/* || PManager.getDefault().state == ProgramState.RECORDING */)
			((RearingModule) ModulesManager.getDefault().getModuleByName(
					"Rearing Module")).incrementRearingCounter();
	}

	/**
	 * Handles the "Subtract Rearing" button click action.
	 */
	public void btnSubRearingAction() {
		if (PManager.getDefault().state == ProgramState.TRACKING
		/* || PManager.getDefault().state == ProgramState.RECORDING */)
			((RearingModule) ModulesManager.getDefault().getModuleByName(
					"Rearing Module")).decrementRearingCounter();
	}

	@Override
	public void inIdleState() {
		btn_add_rearing.setEnabled(false);
		btn_sub_rearing.setEnabled(false);
	}

	@Override
	public void initialize(final Shell shell, final ExpandBar expandBar,
			final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
		final ExpandItem xpndtmManualRearing = new ExpandItem(expandBar,
				SWT.NONE);
		xpndtmManualRearing.setExpanded(true);
		xpndtmManualRearing.setText("Manual Rearing");

		final Composite cmpstManualRearing = new Composite(expandBar, SWT.NONE);
		xpndtmManualRearing.setControl(cmpstManualRearing);

		btn_sub_rearing = new Button(cmpstManualRearing, SWT.NONE);
		btn_sub_rearing.setBounds(new Rectangle(10, 10, 28, 21));
		btn_sub_rearing.setText("-");
		btn_sub_rearing
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						btnSubRearingAction();
					}
				});
		btn_add_rearing = new Button(cmpstManualRearing, SWT.NONE);
		btn_add_rearing.setText("+");
		btn_add_rearing.setSize(new Point(28, 21));
		btn_add_rearing.setLocation(new Point(40, 10));

		btn_add_rearing
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						btnAddRearingAction();
					}
				});
		xpndtmManualRearing.setHeight(xpndtmManualRearing.getControl()
				.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 10);
	}

	@Override
	public void inStreamingState() {
		btn_add_rearing.setEnabled(false);
		btn_sub_rearing.setEnabled(false);
	}

	@Override
	public void inTrackingState() {
		btn_add_rearing.setEnabled(true);
		btn_sub_rearing.setEnabled(true);
	}
}
