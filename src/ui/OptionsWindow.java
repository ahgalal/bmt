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
public class OptionsWindow extends BaseUI
{
	private CtrlOptionsWindow controller; // @jve:decl-index=0:
	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="13,18"
	private Label lbl_hysteresis = null;
	private Label lbl_threshold = null;
	private Text txt_hysteresis = null;
	private Button btn_ok = null;
	private Button btn_cancel = null;
	private Label lbl_rearing_thresh = null;
	private Text txt_rearing_thresh = null;
	private Scale scl_sub_thresh = null;
	private Button chk_auto_rearing = null;

	/**
	 * Creates GUI components, and links this Shell with the parent Shell.
	 */
	public OptionsWindow()
	{
		createSShell();
		super.sShell = sShell;
	}

	/**
	 * This method initializes sShell.
	 */
	private void createSShell()
	{
		sShell = new Shell(SWT.APPLICATION_MODAL | SWT.ON_TOP | SWT.TITLE);
		sShell.setText("OptionsWindow");
		sShell.setLayout(null);
		sShell.setSize(new Point(263, 208));
		lbl_hysteresis = new Label(sShell, SWT.NONE);
		lbl_hysteresis.setText("Zone Hysteresis");
		lbl_hysteresis.setBounds(new Rectangle(10, 18, 86, 15));
		lbl_threshold = new Label(sShell, SWT.NONE);
		lbl_threshold.setBounds(new Rectangle(10, 79, 124, 15));
		lbl_threshold.setText("Subtraction Threshold");
		txt_hysteresis = new Text(sShell, SWT.BORDER);
		txt_hysteresis.setBounds(new Rectangle(173, 13, 76, 21));
		txt_hysteresis.setText("20");
		btn_ok = new Button(sShell, SWT.NONE);
		btn_ok.setBounds(new Rectangle(50, 143, 95, 24));
		btn_ok.setText("OK");
		btn_ok.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.setVars(new String[] { txt_hysteresis.getText(),
						txt_rearing_thresh.getText(),
						String.valueOf(scl_sub_thresh.getSelection()),
						String.valueOf(chk_auto_rearing.getSelection()) });
				controller.btnOkAction();
			}
		});
		btn_cancel = new Button(sShell, SWT.NONE);
		btn_cancel.setBounds(new Rectangle(156, 143, 95, 24));
		btn_cancel.setText("Cancel");
		lbl_rearing_thresh = new Label(sShell, SWT.NONE);
		lbl_rearing_thresh.setBounds(new Rectangle(10, 44, 123, 19));
		lbl_rearing_thresh.setText("Rearing Threshold");
		txt_rearing_thresh = new Text(sShell, SWT.BORDER);
		txt_rearing_thresh.setText("200");
		txt_rearing_thresh.setSize(new Point(76, 21));
		txt_rearing_thresh.setLocation(new Point(173, 42));
		scl_sub_thresh = new Scale(sShell, SWT.NONE);
		scl_sub_thresh.setBounds(new Rectangle(139, 67, 116, 35));
		scl_sub_thresh.setMaximum(255);
		chk_auto_rearing = new Button(sShell, SWT.CHECK);
		chk_auto_rearing.setSelection(true);
		chk_auto_rearing.setBounds(new Rectangle(12, 107, 216, 20));
		chk_auto_rearing.setText("enable auto Rearing Detector");
		scl_sub_thresh.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.setVars(new String[] { "-1", "-1",
						String.valueOf(scl_sub_thresh.getSelection()),
						String.valueOf(chk_auto_rearing.getSelection()) });
				controller.updateOptions(true);
			}
		});
		btn_cancel.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				sShell.setVisible(false);
			}
		});
	}

	@Override
	public void loadData(final String[] strArray)
	{
		txt_hysteresis.setText(strArray[0]);
		scl_sub_thresh.setSelection(Integer.parseInt(strArray[1]));
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void setController(final ControllerUI controller)
	{
		super.setController(controller);
		this.controller = (CtrlOptionsWindow) controller;
	}

	@Override
	public void clearForm()
	{
		txt_hysteresis.setText("0");
		txt_rearing_thresh.setText("0");
		scl_sub_thresh.setSelection(0);
	}
}