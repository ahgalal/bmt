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
import utils.PManager.ProgramState.GeneralState;

/**
 * GUI class for the rearing module.
 * 
 * @author Creative
 */
public class RearingModuleGUI extends PluggedGUI<RearingModule> {
	private Button		btnAddRearing	= null;

	private Button		btnSubRearing	= null;

	private Composite	cmpstManualRearing;

	private ExpandItem	xpndtmManualRearing;

	/**
	 * Initializes/shows the GUI components.
	 * 
	 * @param parent
	 *            parent composite that the components will be children of
	 */
	/*
	 * public RearingModuleGUI(final Composite parent) { }
	 */

	public RearingModuleGUI(final RearingModule owner) {
		super(owner);
	}

	/**
	 * Handles the "Add Rearing" button click action.
	 */
	public void btnAddRearingAction() {
		if (PManager.getDefault().getState().getGeneral() == GeneralState.TRACKING
		/* || PManager.getDefault().state == ProgramState.RECORDING */)
			((RearingModule) ModulesManager.getDefault().getModuleByID(
					RearingModule.moduleID)).incrementRearingCounter();
	}

	/**
	 * Handles the "Subtract Rearing" button click action.
	 */
	public void btnSubRearingAction() {
		if (PManager.getDefault().getState().getGeneral() == GeneralState.TRACKING
		/* || PManager.getDefault().state == ProgramState.RECORDING */)
			((RearingModule) ModulesManager.getDefault().getModuleByID(
					RearingModule.moduleID)).decrementRearingCounter();
	}

	@Override
	public void deInitialize() {
		disposeWidget(cmpstManualRearing);
		disposeWidget(xpndtmManualRearing);
	}

	@Override
	public void initialize(final Shell shell, final ExpandBar expandBar,
			final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
		xpndtmManualRearing = new ExpandItem(expandBar, SWT.NONE);
		xpndtmManualRearing.setExpanded(true);
		xpndtmManualRearing.setText("Manual Rearing");

		cmpstManualRearing = new Composite(expandBar, SWT.NONE);
		xpndtmManualRearing.setControl(cmpstManualRearing);

		btnSubRearing = new Button(cmpstManualRearing, SWT.NONE);
		btnSubRearing.setBounds(new Rectangle(10, 10, 28, 21));
		btnSubRearing.setText("-");
		btnSubRearing
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						btnSubRearingAction();
					}
				});
		btnAddRearing = new Button(cmpstManualRearing, SWT.NONE);
		btnAddRearing.setText("+");
		btnAddRearing.setSize(new Point(28, 21));
		btnAddRearing.setLocation(new Point(40, 10));

		btnAddRearing
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
	public void stateGeneralChangeHandler(final ProgramState state) {
		switch (state.getGeneral()) {
			case IDLE:
				btnAddRearing.setEnabled(false);
				btnSubRearing.setEnabled(false);
				break;
			case TRACKING:
				btnAddRearing.setEnabled(true);
				btnSubRearing.setEnabled(true);
				break;
			default:
				btnAddRearing.setEnabled(false);
				btnSubRearing.setEnabled(false);
				break;
		}
	}

	@Override
	public void stateStreamChangeHandler(final ProgramState state) {

	}
}
