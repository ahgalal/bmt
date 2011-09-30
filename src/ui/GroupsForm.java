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

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
public class GroupsForm extends BaseUI
{

	/**
	 * Creates GUI components, and links this Shell with the parent Shell.
	 */
	public GroupsForm()
	{
		arr_tabs = new ArrayList<TabContents>();
		createSShell();
		super.sShell = this.sShell;
	}

	private final ArrayList<TabContents> arr_tabs; // array of TabContents to
	// store
	// all the form's tabs' data
	private Shell sShell = null;
	private Group grp_grps_info = null;
	private TabFolder tabs_grps = null;
	// private Composite cmpst_tab = null; //DO NOT DELETE!!!
	private int latest_grp_id;
	private Button btn_add_tab = null;
	private Button btn_del_tab = null;
	private Button btn_save = null;
	private Button btn_cancel = null;
	private CtrlGroupsForm controller;

	// Do NOT DELETE .. they are for GUI Design process
	/*
	 * private Label lbl_name = null; private Label lbl_no_rats = null; private
	 * Label lbl_rats_numbering = null; private Label lbl_notes = null; private
	 * Text txt_name = null; private Text txt_no_rats = null; private Text
	 * txt_rats_numbers = null; private Text txt_notes = null;
	 */
	// End Do Not DELETE :D

	@Override
	public void clearForm()
	{
		for (final TabContents tc : arr_tabs)
			tc.tab.dispose();
		arr_tabs.clear();
	}

	/**
	 * This method initializes sShell.
	 */
	private void createSShell()
	{
		sShell = new Shell(SWT.APPLICATION_MODAL | SWT.TITLE | SWT.ON_TOP);
		sShell.setText("Groups Information");
		createGrpsInfo();
		sShell.setLayout(null);
		sShell.setSize(new Point(372, 377));
		btn_save = new Button(sShell, SWT.NONE);
		btn_save.setBounds(new Rectangle(165, 315, 91, 25));
		btn_save.setText("Save");
		btn_save.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.btnSaveAction(arr_tabs);
			}
		});
		btn_cancel = new Button(sShell, SWT.NONE);
		btn_cancel.setBounds(new Rectangle(255, 315, 91, 25));
		btn_cancel.setText("Cancel");
		btn_add_tab = new Button(sShell, SWT.NONE);
		btn_add_tab.setBounds(new Rectangle(13, 317, 29, 27));
		btn_add_tab.setText("+");
		btn_add_tab.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				addNewTab();
			}
		});
		btn_del_tab = new Button(sShell, SWT.NONE);
		btn_del_tab.setBounds(new Rectangle(47, 317, 29, 27));
		btn_del_tab.setText("-");
		btn_del_tab.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				deleteTab();
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
	public void setController(final ControllerUI controller)
	{
		super.setController(controller);
		this.controller = (CtrlGroupsForm) super.controller;
	}

	/**
	 * This method initializes grp_grps_info.
	 */
	private void createGrpsInfo()
	{
		grp_grps_info = new Group(sShell, SWT.NONE);
		grp_grps_info.setLayout(null);
		grp_grps_info.setText("Information:");
		grp_grps_info.setBounds(new Rectangle(5, 5, 356, 311));
		createTabsGrps();
	}

	/**
	 * This method initializes tabs_grps.
	 */
	private void createTabsGrps()
	{
		tabs_grps = new TabFolder(grp_grps_info, SWT.NONE);
		tabs_grps.setBounds(new Rectangle(8, 20, 341, 281));
		// createCmpst_tabs(tabs_no);
		// addNewTab();
		/*
		 * TabItem tabItem = new TabItem(tabs_grps, SWT.NONE);
		 * tabItem.setText("Grppp"); tabItem.setControl(cmpst_tab);
		 */
	}

	/**
	 * This method initializes cmpst_tab
	 */
	/*
	 * private void createCmpst_tab() { //for Design Purposes ONLY cmpst_tab =
	 * new Composite(tabs_grps, SWT.NONE); cmpst_tab.setLayout(null);
	 * cmpst_tab.setBounds(new Rectangle(4, 24, 328, 358)); lbl_name = new
	 * Label(cmpst_tab, SWT.NONE); lbl_name.setBounds(new Rectangle(14, 14, 92,
	 * 15)); lbl_name.setText("Name:"); lbl_no_rats = new Label(cmpst_tab,
	 * SWT.NONE); lbl_no_rats.setBounds(new Rectangle(14, 56, 92, 15));
	 * lbl_no_rats.setText("Number of Rats:"); lbl_rats_numbering = new
	 * Label(cmpst_tab, SWT.NONE); lbl_rats_numbering.setBounds(new
	 * Rectangle(14, 98, 92, 15)); lbl_rats_numbering.setText("Rats Numbers:");
	 * lbl_notes = new Label(cmpst_tab, SWT.NONE); lbl_notes.setBounds(new
	 * Rectangle(14, 140, 92, 15)); lbl_notes.setText("Additional Notes:");
	 * txt_name = new Text(cmpst_tab, SWT.BORDER); txt_name.setBounds(new
	 * Rectangle(140, 14, 183, 21)); txt_no_rats = new Text(cmpst_tab,
	 * SWT.BORDER); txt_no_rats.setBounds(new Rectangle(140, 56, 183, 21));
	 * txt_rats_numbers = new Text(cmpst_tab, SWT.BORDER);
	 * txt_rats_numbers.setBounds(new Rectangle(140, 98, 183, 21)); txt_notes =
	 * new Text(cmpst_tab, SWT.BORDER | SWT.MULTI); txt_notes.setBounds(new
	 * Rectangle(140, 140, 183, 99)); }
	 */

	/*
	 * private void createCmpst_tabs(int no_tabs) { for(int i=0;i<no_tabs;i++) {
	 * final TabContents tmp_tab= new TabContents(); tmp_tab.cmpst=new
	 * Composite(tabs_grps, SWT.NONE); tmp_tab.cmpst.setLayout(null);
	 * tmp_tab.cmpst.setBounds(new Rectangle(4, 24, 328, 358)); tmp_tab.lbl_name
	 * = new Label(tmp_tab.cmpst, SWT.NONE); tmp_tab.lbl_name.setBounds(new
	 * Rectangle(14, 14, 92, 15)); tmp_tab.lbl_name.setText("Name:");
	 * tmp_tab.lbl_no_rats = new Label(tmp_tab.cmpst, SWT.NONE);
	 * tmp_tab.lbl_no_rats.setBounds(new Rectangle(14, 56, 92, 15));
	 * tmp_tab.lbl_no_rats.setText("Number of Rats:");
	 * tmp_tab.lbl_rats_numbering = new Label(tmp_tab.cmpst, SWT.NONE);
	 * tmp_tab.lbl_rats_numbering.setBounds(new Rectangle(14, 98, 92, 15));
	 * tmp_tab.lbl_rats_numbering.setText("Rats Numbers:"); tmp_tab.lbl_notes =
	 * new Label(tmp_tab.cmpst, SWT.NONE); tmp_tab.lbl_notes.setBounds(new
	 * Rectangle(14, 140, 92, 15));
	 * tmp_tab.lbl_notes.setText("Additional Notes:"); tmp_tab.txt_name = new
	 * Text(tmp_tab.cmpst, SWT.BORDER); tmp_tab.txt_name.setBounds(new
	 * Rectangle(140, 14, 183, 21)); tmp_tab.txt_no_rats = new
	 * Text(tmp_tab.cmpst, SWT.BORDER); tmp_tab.txt_no_rats.setBounds(new
	 * Rectangle(140, 56, 183, 21)); tmp_tab.txt_rats_numbers = new
	 * Text(tmp_tab.cmpst, SWT.BORDER); tmp_tab.txt_rats_numbers.setBounds(new
	 * Rectangle(140, 98, 183, 21)); tmp_tab.txt_notes = new Text(tmp_tab.cmpst,
	 * SWT.BORDER | SWT.MULTI); tmp_tab.txt_notes.setBounds(new Rectangle(140,
	 * 140, 183, 99)); tmp_tab.tab = new TabItem(tabs_grps, SWT.NONE);
	 * tmp_tab.tab.setText("Group "+i); tmp_tab.tab.setControl(tmp_tab.cmpst);
	 * arr_tabs.add(tmp_tab); } }
	 */

	/**
	 * Adds a new tab to the GUI (blank tab), to enter a new group's
	 * information.
	 */
	private void addNewTab()
	{
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
	public void addNewTab(
			final int id,
			final String name,
			final String no_rats,
			final String rat_numbers,
			final String notes)
	{
		if (sShell != null)
		{
			final TabContents tmp_tab = new TabContents();
			tmp_tab.cmpst = new Composite(tabs_grps, SWT.NONE);
			tmp_tab.cmpst.setLayout(null);
			tmp_tab.cmpst.setBounds(new Rectangle(4, 24, 328, 358));
			tmp_tab.lbl_name = new Label(tmp_tab.cmpst, SWT.NONE);
			tmp_tab.lbl_name.setBounds(new Rectangle(14, 14, 92, 15));
			tmp_tab.lbl_name.setText("Name:");
			tmp_tab.lbl_no_rats = new Label(tmp_tab.cmpst, SWT.NONE);
			tmp_tab.lbl_no_rats.setBounds(new Rectangle(14, 56, 92, 15));
			tmp_tab.lbl_no_rats.setText("Number of Rats:");
			tmp_tab.lbl_rats_numbering = new Label(tmp_tab.cmpst, SWT.NONE);
			tmp_tab.lbl_rats_numbering.setBounds(new Rectangle(14, 98, 92, 15));
			tmp_tab.lbl_rats_numbering.setText("Rats Numbers:");
			tmp_tab.lbl_notes = new Label(tmp_tab.cmpst, SWT.NONE);
			tmp_tab.lbl_notes.setBounds(new Rectangle(14, 140, 92, 15));
			tmp_tab.lbl_notes.setText("Additional Notes:");
			tmp_tab.txt_name = new Text(tmp_tab.cmpst, SWT.BORDER);
			tmp_tab.txt_name.setBounds(new Rectangle(140, 14, 183, 21));
			tmp_tab.txt_name.setText(name);
			tmp_tab.txt_name.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(final ModifyEvent e)
				{
					tmp_tab.tab.setText(tmp_tab.txt_name.getText());
				}
			});
			tmp_tab.lbl_no_rats_data = new Label(tmp_tab.cmpst, SWT.NONE);
			tmp_tab.lbl_no_rats_data.setBounds(new Rectangle(140, 56, 183, 21));
			tmp_tab.lbl_no_rats_data.setText(no_rats);
			tmp_tab.txt_rats_numbers = new Text(tmp_tab.cmpst, SWT.BORDER);
			tmp_tab.txt_rats_numbers.setBounds(new Rectangle(140, 98, 183, 21));
			tmp_tab.txt_rats_numbers.setText(rat_numbers);
			tmp_tab.txt_rats_numbers.setEditable(false);
			tmp_tab.txt_notes = new Text(tmp_tab.cmpst, SWT.BORDER | SWT.MULTI);
			tmp_tab.txt_notes.setBounds(new Rectangle(140, 140, 183, 99));
			tmp_tab.txt_notes.setText(notes);
			tmp_tab.tab = new TabItem(tabs_grps, SWT.NONE);
			if (name.equals(""))
				tmp_tab.tab.setText("new Group");
			else
				tmp_tab.tab.setText(name);
			tmp_tab.tab.setControl(tmp_tab.cmpst);
			if (id == -1)
				tmp_tab.grp_id = latest_grp_id;
			else
				tmp_tab.grp_id = id;
			latest_grp_id++;
			arr_tabs.add(tmp_tab);
		}
	}

	/**
	 * Deletes an existing tab.
	 */
	private void deleteTab()
	{
		tabs_grps.getSelection()[0].dispose();
	}

	/**
	 * Represents the information contained in a single tab.
	 * 
	 * @author Creative
	 */
	public class TabContents
	{
		public Text txt_name, txt_rats_numbers, txt_notes;
		public Label lbl_name, lbl_no_rats_data, lbl_no_rats, lbl_rats_numbering,
				lbl_notes;
		public Composite cmpst;
		public TabItem tab;
		public int grp_id;
	}

	@Override
	public void loadData(final String[] strArray)
	{
		// TODO Auto-generated method stub

	}

}
