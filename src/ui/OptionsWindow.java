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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import control.ui.ControllerUI;
import control.ui.CtrlOptionsWindow;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import utils.PManager;

/**
 * @author Creative Gives you an option to edit the default values of Hysteresis
 *         & Threshold then choose the subtraction threshold
 */
public class OptionsWindow extends BaseUI {
	private Button				btnCancel			= null;
	private Button				btnOk				= null;
	private Button				chkAutoRearing	= null;
	private CtrlOptionsWindow	controller;				// @jve:decl-index=0:
	private Label				lblHysteresis		= null;
	private Label				lblRearingThresh	= null;
	private Label				lblThreshold		= null;
	private Shell sShell_1;
	private Text				txtHysteresis		= null;
	private Text				txtRearingThresh	= null;
	private Text txtSubtractionThreshold;

	/**
	 * Creates GUI components, and links this Shell with the parent Shell.
	 */
	public OptionsWindow() {
		createSShell();
		super.sShell = sShell_1;
	}

	@Override
	public void clearForm() {
		txtHysteresis.setText("0");
		txtRearingThresh.setText("0");
		txtSubtractionThreshold.setText("0");
	}

	/**
	 * This method initializes sShell.
	 */
	private void createSShell() {
		sShell_1 = new Shell(SWT.APPLICATION_MODAL | SWT.ON_TOP | SWT.TITLE);
		sShell_1.setText("OptionsWindow");
		sShell_1.setSize(new Point(247, 165));
		sShell_1.setLayout(new GridLayout(3, false));
		lblHysteresis = new Label(sShell_1, SWT.NONE);
		lblHysteresis.setText("Zone Hysteresis");
		txtHysteresis = new Text(sShell_1, SWT.BORDER);
		txtHysteresis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtHysteresis.setText("20");
		new Label(sShell_1, SWT.NONE);
		lblRearingThresh = new Label(sShell_1, SWT.NONE);
		lblRearingThresh.setText("Rearing Threshold");
		txtRearingThresh = new Text(sShell_1, SWT.BORDER);
		txtRearingThresh.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtRearingThresh.setText("200");
		new Label(sShell_1, SWT.NONE);
		lblThreshold = new Label(sShell_1, SWT.NONE);
		lblThreshold.setText("Subtraction Threshold");
		GridData gd_sclSubThresh = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_sclSubThresh.widthHint = 42;

		
		txtSubtractionThreshold = new Text(sShell_1, SWT.BORDER);
		txtSubtractionThreshold.setText("40");
		txtSubtractionThreshold.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtSubtractionThreshold.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				if(PManager.mainGUI!=null){
					controller.setVars(new String[] { "-1", "-1",
							String.valueOf(txtSubtractionThreshold.getText()),
							String.valueOf(chkAutoRearing.getSelection()) });
					controller.updateOptions(true);
				}
			}
		});
		new Label(sShell_1, SWT.NONE);
		chkAutoRearing = new Button(sShell_1, SWT.CHECK);
		chkAutoRearing.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		chkAutoRearing.setSelection(true);
		chkAutoRearing.setText("enable auto Rearing Detector");
		new Label(sShell_1, SWT.NONE);
		btnOk = new Button(sShell_1, SWT.NONE);
		GridData gd_btnOk = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnOk.widthHint = 95;
		btnOk.setLayoutData(gd_btnOk);
		btnOk.setText("OK");
		btnOk.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					final org.eclipse.swt.events.SelectionEvent e) {
				controller.setVars(new String[] { txtHysteresis.getText(),
						txtRearingThresh.getText(),
						String.valueOf(txtSubtractionThreshold.getText()),
						String.valueOf(chkAutoRearing.getSelection()) });
				controller.btnOkAction();
			}
		});
		btnCancel = new Button(sShell_1, SWT.NONE);
		GridData gd_btnCancel = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 99;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("Cancel");
		btnCancel
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						sShell.setVisible(false);
					}
				});
	}

	@Override
	public void loadData(final String[] strArray) {
		txtHysteresis.setText(strArray[0]);
		txtSubtractionThreshold.setText(strArray[1]);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setController(final ControllerUI controller) {
		super.setController(controller);
		this.controller = (CtrlOptionsWindow) controller;
	}
}