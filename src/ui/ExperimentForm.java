/***************************************************************************
 *  Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly,
 *  Sarah Hamid and Mohammed Ahmed Ramadan
 *  contact: ceng.ahmedgalal@gmail.com
 *
 *  This file is part of Behavioral Monitoring Tool.
 *
 *  Behavioral Monitoring Tool is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, version 3 of the
 *  License.
 *
 *  Behavioral Monitoring Tool is distributed in the hope that it
 *  will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Behavioral Monitoring Tool.
 *  If not, see <http://www.gnu.org/licenses/>.
 *   
 **************************************************************************/

package ui;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import control.ui.ControllerUI;
import control.ui.CtrlExperimentForm;

/**
 * Displays Experiment information and enable the user to edit them.
 * 
 * @author Creative
 */
public class ExperimentForm extends BaseUI
{

	// private boolean is_filled;
	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="10,10"
	private Group grp_info = null;
	private Button btn_save = null;
	private Button btn_cancel = null;
	private Label lbl_name = null;
	private Label lbl_no_groups = null;
	private Label lbl_date = null;
	private Label lbl_username = null;
	private Label lbl_notes = null;
	private Text txt_name = null;
	private Text txt_user = null;
	private Text txt_notes = null;
	private Label lbl_current_date = null;
	private Button btn_mng_grps = null;
	private CtrlExperimentForm controller;

	/**
	 * Creates GUI components, and links this Shell with the parent Shell.
	 */
	public ExperimentForm()
	{
		createSShell();
		super.sShell = this.sShell;
	}

	/**
	 * This method initializes sShell.
	 */
	private void createSShell()
	{
		sShell = new Shell(SWT.APPLICATION_MODAL | SWT.ON_TOP | SWT.TITLE);
		sShell.setText("Experiment Information");
		createGrpInfo();
		sShell.setSize(new Point(366, 352));
		sShell.setLayout(null);
		btn_save = new Button(sShell, SWT.NONE);
		btn_save.setBounds(new Rectangle(158, 289, 96, 25));
		btn_save.setText("Save");
		btn_save.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.setVars(new String[] { txt_name.getText(), txt_user.getText(),
						lbl_current_date.getText(), txt_notes.getText() });
				controller.btnSaveAction(sShell);
			}
		});
		btn_cancel = new Button(sShell, SWT.NONE);
		btn_cancel.setBounds(new Rectangle(258, 289, 96, 25));
		btn_cancel.setText("Cancel");
		btn_cancel.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				sShell.setVisible(false);
			}
		});

		setLabelToDate();
	}

	/**
	 * This method initializes grp_info.
	 */
	private void createGrpInfo()
	{
		grp_info = new Group(sShell, SWT.NONE);
		grp_info.setLayout(null);
		grp_info.setText("Information:");
		grp_info.setBounds(new Rectangle(5, 5, 352, 275));
		lbl_name = new Label(grp_info, SWT.NONE);
		lbl_name.setBounds(new Rectangle(12, 23, 92, 15));
		lbl_name.setText("Name:");
		lbl_no_groups = new Label(grp_info, SWT.NONE);
		lbl_no_groups.setBounds(new Rectangle(12, 99, 92, 15));
		lbl_no_groups.setText("Groups:");
		lbl_date = new Label(grp_info, SWT.NONE);
		lbl_date.setBounds(new Rectangle(12, 137, 92, 15));
		lbl_date.setText("Date:");
		lbl_username = new Label(grp_info, SWT.NONE);
		lbl_username.setBounds(new Rectangle(12, 61, 92, 15));
		lbl_username.setText("User:");
		lbl_notes = new Label(grp_info, SWT.NONE);
		lbl_notes.setBounds(new Rectangle(12, 175, 92, 15));
		lbl_notes.setText("Additional Notes:");
		txt_name = new Text(grp_info, SWT.BORDER);
		txt_name.setBounds(new Rectangle(153, 16, 192, 21));
		txt_user = new Text(grp_info, SWT.BORDER);
		txt_user.setBounds(new Rectangle(153, 56, 192, 21));
		txt_notes = new Text(grp_info, SWT.BORDER | SWT.MULTI);
		txt_notes.setBounds(new Rectangle(153, 177, 194, 88));
		lbl_current_date = new Label(grp_info, SWT.NONE);
		lbl_current_date.setBounds(new Rectangle(153, 138, 192, 15));
		lbl_current_date.setText("Current Date");
		btn_mng_grps = new Button(grp_info, SWT.NONE);
		btn_mng_grps.setText("Manage Groups..");
		btn_mng_grps.setSize(new Point(119, 25));
		btn_mng_grps.setLocation(new Point(154, 93));
		btn_mng_grps.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.btnMngGrpsAction();
			}
		});
	}

	/**
	 * Sets the label to the current date.
	 */
	private void setLabelToDate()
	{
		lbl_current_date.setText(Calendar.getInstance().getTime().toString());
	}

	@Override
	public void clearForm()
	{
		txt_name.setText("");
		txt_notes.setText("");
		txt_user.setText("");
		setLabelToDate();
	}

	@Override
	public void loadData(final String[] strArray)
	{
		txt_name.setText(strArray[0]);
		txt_user.setText(strArray[1]);
		txt_notes.setText(strArray[2]);
		lbl_current_date.setText(strArray[3]);
	}

	@Override
	public void setController(final ControllerUI controller)
	{
		super.setController(controller);
		this.controller = (CtrlExperimentForm) super.controller;
	}

}
