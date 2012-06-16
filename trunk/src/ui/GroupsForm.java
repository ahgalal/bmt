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

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import control.ui.ControllerUI;
import control.ui.CtrlGroupsForm;

/**
 * Displays Groups information and enable the user to edit them.
 * 
 * @author Creative
 */
public class GroupsForm extends BaseUI {

	/**
	 * Represents the information contained in a single tab.
	 * 
	 * @author Creative
	 */
	public class TabContents {
		public Composite	cmpst;
		public int			grp_id;
		public Label		lbl_name, lbl_no_rats_data, lbl_no_rats,
				lbl_rats_numbering, lbl_notes;
		public TabItem		tab;
		public Text			txt_name, txt_rats_numbers, txt_notes;

		public void createGUI(final String name, final String no_rats,
				final String rat_numbers, final String notes, final int id) {
			cmpst = new Composite(tabs_grps, SWT.NONE);
			cmpst.setBounds(new Rectangle(4, 24, 328, 358));
			final GridLayout gl_cmpst = new GridLayout(2, false);
			gl_cmpst.verticalSpacing = 15;
			cmpst.setLayout(gl_cmpst);
			lbl_name = new Label(cmpst, SWT.NONE);
			lbl_name.setText("Name:");
			txt_name = new Text(cmpst, SWT.BORDER);
			final GridData gd_txt_name = new GridData(SWT.FILL, SWT.CENTER,
					true, false, 1, 1);
			gd_txt_name.widthHint = 140;
			txt_name.setLayoutData(gd_txt_name);
			txt_name.setText(name);
			txt_name.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(final ModifyEvent e) {
					tab.setText(txt_name.getText());
				}
			});
			lbl_no_rats = new Label(cmpst, SWT.NONE);
			lbl_no_rats.setText("Number of Rats:");
			lbl_no_rats_data = new Label(cmpst, SWT.NONE);
			lbl_no_rats_data.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
					true, false, 1, 1));
			lbl_no_rats_data.setText(no_rats);
			lbl_rats_numbering = new Label(cmpst, SWT.NONE);
			lbl_rats_numbering.setText("Rats Numbers:");
			txt_rats_numbers = new Text(cmpst, SWT.BORDER);
			txt_rats_numbers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
					true, false, 1, 1));
			txt_rats_numbers.setText(rat_numbers);
			txt_rats_numbers.setEditable(false);
			lbl_notes = new Label(cmpst, SWT.NONE);
			final GridData gd_lbl_notes = new GridData(SWT.LEFT, SWT.TOP,
					false, false, 1, 1);
			gd_lbl_notes.widthHint = 129;
			lbl_notes.setLayoutData(gd_lbl_notes);
			lbl_notes.setText("Additional Notes:");
			txt_notes = new Text(cmpst, SWT.BORDER | SWT.MULTI);
			final GridData gd_txt_notes = new GridData(SWT.FILL, SWT.CENTER,
					true, false, 1, 1);
			gd_txt_notes.heightHint = 100;
			txt_notes.setLayoutData(gd_txt_notes);
			txt_notes.setText(notes);
			tab = new TabItem(tabs_grps, SWT.NONE);
			if (name.equals(""))
				tab.setText("new Group");
			else
				tab.setText(name);
			tab.setControl(cmpst);
			if (id == -1)
				grp_id = latest_grp_id;
			else
				grp_id = id;
		}
	}

	private final ArrayList<TabContents>	arr_tabs;				// array of
																	// TabContents
																	// to
	private Button							btn_add_tab		= null;
	private Button							btn_cancel		= null;
	private Button							btn_del_tab		= null;
	private Button							btn_save		= null;
	private CtrlGroupsForm					controller;
	private Group							grp_grps_info	= null;
	// private Composite cmpst_tab = null; //DO NOT DELETE!!!
	private int								latest_grp_id;
	// store
	// all the form's tabs' data
	private Shell							sShell			= null;
	private TabFolder						tabs_grps		= null;

	/**
	 * Creates GUI components, and links this Shell with the parent Shell.
	 */
	public GroupsForm() {
		arr_tabs = new ArrayList<TabContents>();
		createSShell();
		super.sShell = this.sShell;
	}

	/**
	 * Adds a new tab to the GUI (blank tab), to enter a new group's
	 * information.
	 */
	private void addNewTab() {
		addNewTab(-1, "", "0", "", "");
	}

	/**
	 * Adds a new tab to the GUI, and load data into it.
	 * 
	 * @param id
	 *            group id
	 * @param name
	 *            group name
	 * @param no_rats
	 *            number of rats
	 * @param rat_numbers
	 *            numbers of rats
	 * @param notes
	 *            any additional notes
	 */
	public void addNewTab(final int id, final String name,
			final String no_rats, final String rat_numbers, final String notes) {
		if (sShell != null) {
			final TabContents tmp_tab = new TabContents();
			tmp_tab.createGUI(name, no_rats, rat_numbers, notes, id);
			latest_grp_id++;
			arr_tabs.add(tmp_tab);
		}
	}

	@Override
	public void clearForm() {
		for (final TabContents tc : arr_tabs)
			tc.tab.dispose();
		arr_tabs.clear();
	}

	/**
	 * This method initializes grp_grps_info.
	 */
	private void createGrpsInfo() {
		grp_grps_info = new Group(sShell, SWT.NONE);
		grp_grps_info.setLayout(null);
		grp_grps_info.setText("Information:");
		grp_grps_info.setBounds(new Rectangle(5, 5, 356, 273));
		createTabsGrps();
	}

	/*
	 * public Text txt_name, txt_rats_numbers, txt_notes; public Label lbl_name,
	 * lbl_no_rats_data, lbl_no_rats, lbl_rats_numbering, lbl_notes; public
	 * Composite cmpst; public TabItem tab; public int grp_id;
	 */

	/**
	 * This method initializes sShell.
	 */
	private void createSShell() {
		sShell = new Shell(SWT.APPLICATION_MODAL | SWT.TITLE | SWT.ON_TOP);
		sShell.setText("Groups Information");
		createGrpsInfo();
		sShell.setLayout(null);
		sShell.setSize(new Point(372, 342));
		btn_save = new Button(sShell, SWT.NONE);
		btn_save.setBounds(new Rectangle(167, 280, 91, 25));
		btn_save.setText("Save");
		btn_save.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					final org.eclipse.swt.events.SelectionEvent e) {
				controller.btnSaveAction(arr_tabs);
			}
		});
		btn_cancel = new Button(sShell, SWT.NONE);
		btn_cancel.setBounds(new Rectangle(257, 280, 91, 25));
		btn_cancel.setText("Cancel");
		btn_add_tab = new Button(sShell, SWT.NONE);
		btn_add_tab.setBounds(new Rectangle(15, 282, 29, 27));
		btn_add_tab.setText("+");
		btn_add_tab
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						addNewTab();
					}
				});
		btn_del_tab = new Button(sShell, SWT.NONE);
		btn_del_tab.setBounds(new Rectangle(49, 282, 29, 27));
		btn_del_tab.setText("-");
		btn_del_tab
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						deleteTab();
					}
				});
		btn_cancel
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						sShell.setVisible(false);
					}
				});
	}

	/**
	 * This method initializes tabs_grps.
	 */
	private void createTabsGrps() {
		tabs_grps = new TabFolder(grp_grps_info, SWT.NONE);
		tabs_grps.setBounds(new Rectangle(8, 20, 341, 246));
		// createCmpst_tabs(tabs_no);
		// addNewTab();
		/*
		 * TabItem tabItem = new TabItem(tabs_grps, SWT.NONE);
		 * tabItem.setText("Grppp"); tabItem.setControl(cmpst_tab);
		 */
		/*
		 * String name=""; String no_rats=""; String rat_numbers=""; String
		 * notes=""; int id=0;
		 */

	}

	/**
	 * Deletes an existing tab.
	 */
	private void deleteTab() {
		tabs_grps.getSelection()[0].dispose();
	}

	/**
	 * @return the arr_tabs
	 */
	public ArrayList<TabContents> getArr_tabs() {
		return arr_tabs;
	}

	@Override
	public void loadData(final String[] strArray) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setController(final ControllerUI controller) {
		super.setController(controller);
		this.controller = (CtrlGroupsForm) super.controller;
	}

}
