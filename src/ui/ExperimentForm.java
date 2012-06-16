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

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import utils.PManager;
import control.ui.ControllerUI;
import control.ui.CtrlExperimentForm;

/**
 * Displays Experiment information and enable the user to edit them.
 * 
 * @author Creative
 */
public class ExperimentForm extends BaseUI {

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
    private Combo cmboType;
    private FormData fd_grp_info;

    /**
     * Creates GUI components, and links this Shell with the parent Shell.
     */
    public ExperimentForm() {
	createSShell();
	super.sShell = this.sShell;
    }

    /**
     * This method initializes sShell.
     */
    private void createSShell() {
	sShell = new Shell(SWT.APPLICATION_MODAL | SWT.ON_TOP | SWT.TITLE);
	sShell.setText("Experiment Information");
	createGrpInfo();
	sShell.setSize(new Point(363, 360));
	btn_save = new Button(sShell, SWT.NONE);
	final FormData fd_btn_save = new FormData();
	fd_btn_save.top = new FormAttachment(0, 301);
	fd_btn_save.left = new FormAttachment(0, 141);
	btn_save.setLayoutData(fd_btn_save);
	btn_save.setText("Save");
	btn_save.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
	    @Override
	    public void widgetSelected(
		    final org.eclipse.swt.events.SelectionEvent e) {
		controller.setVars(new String[] { txt_name.getText(),
			txt_user.getText(), lbl_current_date.getText(),
			txt_notes.getText(), cmboType.getText() });
		controller.btnSaveAction(sShell);
	    }
	});
	btn_cancel = new Button(sShell, SWT.NONE);
	fd_btn_save.right = new FormAttachment(100, -120);
	final FormData fd_btn_cancel = new FormData();
	fd_btn_cancel.top = new FormAttachment(grp_info, 6);
	fd_btn_cancel.left = new FormAttachment(btn_save, 14);
	fd_btn_cancel.right = new FormAttachment(100, -10);
	btn_cancel.setLayoutData(fd_btn_cancel);
	btn_cancel.setText("Cancel");
	btn_cancel
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
		    @Override
		    public void widgetSelected(
			    final org.eclipse.swt.events.SelectionEvent e) {
			sShell.setVisible(false);
		    }
		});

	setLabelToDate();
    }

    private void loadTestData() {
	txt_name.setText("TestName");
	txt_notes.setText("TestNotes");
	txt_user.setText("TestUser");
    }

    /**
     * This method initializes grp_info.
     */
    private void createGrpInfo() {
	sShell.setLayout(new FormLayout());
	grp_info = new Group(sShell, SWT.NONE);
	grp_info.setLayout(new GridLayout(2, false));
	fd_grp_info = new FormData();
	fd_grp_info.right = new FormAttachment(0, 352);
	fd_grp_info.left = new FormAttachment(0, 5);
	fd_grp_info.bottom = new FormAttachment(0, 295);
	fd_grp_info.top = new FormAttachment(0, 5);
	grp_info.setLayoutData(fd_grp_info);
	grp_info.setText("Information:");
	lbl_name = new Label(grp_info, SWT.NONE);
	lbl_name.setText("Name:");
	txt_name = new Text(grp_info, SWT.BORDER);
	final GridData gd_txt_name = new GridData(SWT.FILL, SWT.CENTER, true,
		false, 1, 1);
	gd_txt_name.widthHint = 125;
	txt_name.setLayoutData(gd_txt_name);

	final Label lblType = new Label(grp_info, SWT.NONE);
	lblType.setText("Type");

	cmboType = new Combo(grp_info, SWT.READ_ONLY);
	cmboType.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,
		1, 1));
	cmboType.setItems(new String[] { "Open Field", "Forced Swimming" });
	cmboType.setText("Open Field");
	lbl_username = new Label(grp_info, SWT.NONE);
	lbl_username.setText("User:");
	txt_user = new Text(grp_info, SWT.BORDER);
	txt_user.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
		1, 1));
	lbl_date = new Label(grp_info, SWT.NONE);
	lbl_date.setText("Date:");
	lbl_current_date = new Label(grp_info, SWT.NONE);
	lbl_current_date.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
		false, 1, 1));
	lbl_current_date.setText("Current Date");
	lbl_no_groups = new Label(grp_info, SWT.NONE);
	lbl_no_groups.setText("Groups:");
	btn_mng_grps = new Button(grp_info, SWT.NONE);
	btn_mng_grps.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
		false, 1, 1));
	btn_mng_grps.setText("Manage Groups..");
	btn_mng_grps
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
		    @Override
		    public void widgetSelected(
			    final org.eclipse.swt.events.SelectionEvent e) {
			controller.btnMngGrpsAction();
		    }
		});
	lbl_notes = new Label(grp_info, SWT.NONE);
	final GridData gd_lbl_notes = new GridData(SWT.LEFT, SWT.TOP, false,
		false, 1, 1);
	gd_lbl_notes.widthHint = 125;
	lbl_notes.setLayoutData(gd_lbl_notes);
	lbl_notes.setText("Additional Notes:");
	txt_notes = new Text(grp_info, SWT.BORDER | SWT.MULTI);
	final GridData gd_txt_notes = new GridData(SWT.FILL, SWT.FILL, true,
		false, 1, 1);
	gd_txt_notes.heightHint = 124;
	txt_notes.setLayoutData(gd_txt_notes);
    }

    /**
     * Sets the label to the current date.
     */
    private void setLabelToDate() {
	lbl_current_date.setText(Calendar.getInstance().getTime().toString());
    }

    @Override
    public void clearForm() {
	txt_name.setText("");
	txt_notes.setText("");
	txt_user.setText("");
	setLabelToDate();
	if (PManager.testingMode)
	    loadTestData();
    }

    @Override
    public void loadData(final String[] strArray) {
	txt_name.setText(strArray[0]);
	txt_user.setText(strArray[1]);
	txt_notes.setText(strArray[2]);
	lbl_current_date.setText(strArray[3]);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setController(final ControllerUI controller) {
	super.setController(controller);
	this.controller = (CtrlExperimentForm) super.controller;
    }
}
