package ui.wizards.newexperiment;

import java.util.ArrayList;

import modules.experiment.Grp2GUI;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class GrpsBasicInfoPage extends WizardPage {

	class Group implements Grp2GUI {
		int	id, noRats;
		String	name, ratsNumbering, notes;

		@Override
		public int getId() {
			return id;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public int getNoRats() {
			return noRats;
		}

		@Override
		public String getNotes() {
			return notes;
		}

		@Override
		public String getRatsNumbering() {
			return ratsNumbering;
		}

	}

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
	private Button							btn_del_tab		= null;
	private org.eclipse.swt.widgets.Group	grp_grps_info	= null;
	// private Composite cmpst_tab = null; //DO NOT DELETE!!!
	private int								latest_grp_id;

	// store
	// all the form's tabs' data
	private TabFolder						tabs_grps		= null;

	protected GrpsBasicInfoPage(final String pageName) {
		super(pageName);
		arr_tabs = new ArrayList<TabContents>();
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
		final TabContents tmp_tab = new TabContents();
		tmp_tab.createGUI(name, no_rats, rat_numbers, notes, id);
		latest_grp_id++;
		arr_tabs.add(tmp_tab);
	}

	public void clearForm() {
		for (final TabContents tc : arr_tabs)
			tc.tab.dispose();
		arr_tabs.clear();
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite cmpstPage = new Composite(parent, 0);
		createGrpsInfo(cmpstPage);
		btn_add_tab = new Button(cmpstPage, SWT.NONE);
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
		btn_del_tab = new Button(cmpstPage, SWT.NONE);
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
		setControl(cmpstPage);
	}

	/**
	 * This method initializes grp_grps_info.
	 */
	private void createGrpsInfo(final Composite cmpstPage) {

		grp_grps_info = new org.eclipse.swt.widgets.Group(cmpstPage, SWT.NONE);
		grp_grps_info.setLayout(null);
		grp_grps_info.setText("Information:");
		grp_grps_info.setBounds(new Rectangle(5, 5, 559, 273));
		createTabsGrps();
	}

	/**
	 * This method initializes tabs_grps.
	 */
	private void createTabsGrps() {
		tabs_grps = new TabFolder(grp_grps_info, SWT.NONE);
		tabs_grps.setBounds(new Rectangle(8, 20, 541, 246));
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

	public ArrayList<Grp2GUI> getGroups() {
		final ArrayList<Grp2GUI> groups = new ArrayList<Grp2GUI>();
		for (final TabContents tc : arr_tabs) {
			final Group grp = new Group();
			grp.id = tc.grp_id;
			grp.ratsNumbering = tc.txt_rats_numbers.getText();
			grp.notes = tc.txt_notes.getText();
			grp.name = tc.txt_name.getText();
			grp.noRats = Integer.parseInt(tc.lbl_no_rats_data.getText());
			groups.add(grp);
		}
		return groups;
	}

}
