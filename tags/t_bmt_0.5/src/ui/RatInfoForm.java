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

package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ui.box.MsgBox;
import control.ui.ControllerUI;
import control.ui.CtrlRatInfoForm;

/**
 * Enables you to identify your Rat's Number & the Group Name that your rat will
 * join.
 * 
 * @author Creative
 */
public class RatInfoForm extends BaseUI {
	private Button			btnCancel		= null;

	private Button			btnOk			= null;
	private Combo			cmboGrpNames	= null;
	private CtrlRatInfoForm	controller;			// @jve:decl-index=0:
	private Label			lblGroupname	= null;
	private Label			lblRatnumber	= null;
	private Shell			sShell			= null; // @jve:decl-index=0:visual-constraint="13,18"
	private Text			txtRatnumber	= null;

	/**
	 * Creates GUI components, and links this Shell with the parent Shell.
	 */
	public RatInfoForm() {
		createSShell();
		super.sShell = this.sShell;
		sShell.setDefaultButton(btnOk);
	}

	@Override
	public void clearForm() {
		txtRatnumber.setText("");
		cmboGrpNames.removeAll();
	}

	/**
	 * This method initializes cmbo_grp_names.
	 */

	private void createCmboGrpNames() {
		cmboGrpNames = new Combo(sShell, SWT.READ_ONLY);
		cmboGrpNames.setBounds(new Rectangle(115, 45, 73, 23));
	}

	/**
	 * This method initializes sShell.
	 */
	private void createSShell() {
		sShell = new Shell(SWT.APPLICATION_MODAL/* | SWT.ON_TOP*/ | SWT.DIALOG_TRIM);
		sShell.setText("RatInfo");
		sShell.setLayout(null);
		sShell.setSize(new Point(201, 138));
		sShell.addListener (SWT.Close, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				controller.cancelAction();
				arg0.doit=false;
			}
		});
			
		lblRatnumber = new Label(sShell, SWT.NONE);
		lblRatnumber.setText("Rat Number");
		lblRatnumber.setBounds(new Rectangle(10, 18, 86, 15));
		lblGroupname = new Label(sShell, SWT.NONE);
		lblGroupname.setBounds(new Rectangle(10, 50, 86, 15));
		lblGroupname.setText("Group Name");
		txtRatnumber = new Text(sShell, SWT.BORDER);
		txtRatnumber.setBounds(new Rectangle(113, 14, 73, 21));
		btnOk = new Button(sShell, SWT.NONE);
		btnOk.setBounds(new Rectangle(13, 79, 82, 24));
		btnOk.setText("OK");
		btnOk.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					final org.eclipse.swt.events.SelectionEvent e) {
				
				try {
					if(txtRatnumber.getText().equals("")){
						throw new RuntimeException();
					}
					Integer.parseInt(txtRatnumber.getText());
					
				} catch (RuntimeException e1) {
					MsgBox.show(sShell, "Please enter valid rat number",
							"Error", SWT.ERROR);
					return;
				}
				
				if (controller.setVars(new String[] { txtRatnumber.getText(),
						cmboGrpNames.getText() }))
					controller.btnOkAction();
				else
					MsgBox.show(sShell, "Please enter valid rat number",
							"Error", SWT.ERROR);
			}
		});
		btnCancel = new Button(sShell, SWT.NONE);
		btnCancel.setBounds(new Rectangle(104, 79, 82, 24));
		btnCancel.setText("Cancel");
		createCmboGrpNames();
		btnCancel
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						controller.cancelAction();
					}
				});
	}

	/**
	 * Gets the SWT shell of this GUI.
	 * 
	 * @return SWT shell of this GUI window
	 */
	public Shell getShell() {
		return sShell;
	}

	@Override
	public void loadData(final String[] strArray) {
		txtRatnumber.setText(strArray[0]);
		final String[] grps = new String[strArray.length - 1];
		for (int i = 1; i < strArray.length; i++)
			grps[i - 1] = strArray[i];
		loadGroupsToCombo(grps);
	}

	/**
	 * Loads grps_names(groups names) it has(entered before) to the
	 * cmbo_grp_names(Combo).
	 * 
	 * @param grpsNames
	 *            names of the groups to load into the combobox
	 */
	private void loadGroupsToCombo(final String[] grpsNames) {
		for (int i = 0; i < grpsNames.length; i++)
			cmboGrpNames.add(grpsNames[i]);
		cmboGrpNames.select(0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setController(final ControllerUI controller) {
		super.setController(controller);
		this.controller = (CtrlRatInfoForm) super.controller;
	}

	/**
	 * Shows/Hides the GUI controlled by a child of this class.
	 * 
	 * @param visibility
	 *            true: visible, false: invisible
	 */
	@Override
	public void show(final boolean visibility) {
		super.show(visibility);
		if(visibility)
			txtRatnumber.setFocus();
	}

}
