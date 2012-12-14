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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import control.ui.ControllerUI;
import control.ui.CtrlOptionsWindow;

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
	private Scale				sclSubThresh		= null;
	private Shell				sShell				= null; // @jve:decl-index=0:visual-constraint="13,18"
	private Text				txtHysteresis		= null;
	private Text				txtRearingThresh	= null;

	/**
	 * Creates GUI components, and links this Shell with the parent Shell.
	 */
	public OptionsWindow() {
		createSShell();
		super.sShell = sShell;
	}

	@Override
	public void clearForm() {
		txtHysteresis.setText("0");
		txtRearingThresh.setText("0");
		sclSubThresh.setSelection(0);
	}

	/**
	 * This method initializes sShell.
	 */
	private void createSShell() {
		sShell = new Shell(SWT.APPLICATION_MODAL | SWT.ON_TOP | SWT.TITLE);
		sShell.setText("OptionsWindow");
		sShell.setLayout(null);
		sShell.setSize(new Point(263, 208));
		lblHysteresis = new Label(sShell, SWT.NONE);
		lblHysteresis.setText("Zone Hysteresis");
		lblHysteresis.setBounds(new Rectangle(10, 18, 86, 15));
		lblThreshold = new Label(sShell, SWT.NONE);
		lblThreshold.setBounds(new Rectangle(10, 79, 124, 15));
		lblThreshold.setText("Subtraction Threshold");
		txtHysteresis = new Text(sShell, SWT.BORDER);
		txtHysteresis.setBounds(new Rectangle(173, 13, 76, 21));
		txtHysteresis.setText("20");
		btnOk = new Button(sShell, SWT.NONE);
		btnOk.setBounds(new Rectangle(50, 143, 95, 24));
		btnOk.setText("OK");
		btnOk.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					final org.eclipse.swt.events.SelectionEvent e) {
				controller.setVars(new String[] { txtHysteresis.getText(),
						txtRearingThresh.getText(),
						String.valueOf(sclSubThresh.getSelection()),
						String.valueOf(chkAutoRearing.getSelection()) });
				controller.btnOkAction();
			}
		});
		btnCancel = new Button(sShell, SWT.NONE);
		btnCancel.setBounds(new Rectangle(156, 143, 95, 24));
		btnCancel.setText("Cancel");
		lblRearingThresh = new Label(sShell, SWT.NONE);
		lblRearingThresh.setBounds(new Rectangle(10, 44, 123, 19));
		lblRearingThresh.setText("Rearing Threshold");
		txtRearingThresh = new Text(sShell, SWT.BORDER);
		txtRearingThresh.setText("200");
		txtRearingThresh.setSize(new Point(76, 21));
		txtRearingThresh.setLocation(new Point(173, 42));
		sclSubThresh = new Scale(sShell, SWT.NONE);
		sclSubThresh.setBounds(new Rectangle(139, 67, 116, 35));
		sclSubThresh.setMaximum(255);
		chkAutoRearing = new Button(sShell, SWT.CHECK);
		chkAutoRearing.setSelection(true);
		chkAutoRearing.setBounds(new Rectangle(12, 107, 216, 20));
		chkAutoRearing.setText("enable auto Rearing Detector");
		sclSubThresh
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						controller.setVars(new String[] { "-1", "-1",
								String.valueOf(sclSubThresh.getSelection()),
								String.valueOf(chkAutoRearing.getSelection()) });
						controller.updateOptions(true);
					}
				});
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
		sclSubThresh.setSelection(Integer.parseInt(strArray[1]));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setController(final ControllerUI controller) {
		super.setController(controller);
		this.controller = (CtrlOptionsWindow) controller;
	}
}