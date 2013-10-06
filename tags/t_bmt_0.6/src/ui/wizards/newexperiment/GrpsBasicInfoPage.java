package ui.wizards.newexperiment;

import java.util.ArrayList;

import modules.ExperimentManager;
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
		public int			grpId;
		public Label		lblName, lblNoRatsData, lblNoRats,
				lblRatsNumbering, lblNotes;
		public TabItem		tab;
		public Text			txtName, txtRatsNumbers, txtNotes;

		public void createGUI(final String name, final String noRats,
				final String ratNumbers, final String notes, final int id) {
			cmpst = new Composite(tabsGrps, SWT.NONE);
			cmpst.setBounds(new Rectangle(4, 24, 328, 358));
			final GridLayout glCmpst = new GridLayout(2, false);
			glCmpst.verticalSpacing = 15;
			cmpst.setLayout(glCmpst);
			lblName = new Label(cmpst, SWT.NONE);
			lblName.setText("Name:");
			txtName = new Text(cmpst, SWT.BORDER);
			final GridData gdTxtName = new GridData(SWT.FILL, SWT.CENTER,
					true, false, 1, 1);
			gdTxtName.widthHint = 140;
			txtName.setLayoutData(gdTxtName);
			txtName.setText(name);
			txtName.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(final ModifyEvent e) {
					tab.setText(txtName.getText());
				}
			});
			lblNoRats = new Label(cmpst, SWT.NONE);
			lblNoRats.setText("Number of Rats:");
			lblNoRatsData = new Label(cmpst, SWT.NONE);
			lblNoRatsData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
					true, false, 1, 1));
			lblNoRatsData.setText(noRats);
			lblRatsNumbering = new Label(cmpst, SWT.NONE);
			lblRatsNumbering.setText("Rats Numbers:");
			txtRatsNumbers = new Text(cmpst, SWT.BORDER);
			txtRatsNumbers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
					true, false, 1, 1));
			txtRatsNumbers.setText(ratNumbers);
			txtRatsNumbers.setEditable(false);
			lblNotes = new Label(cmpst, SWT.NONE);
			final GridData gdLblNotes = new GridData(SWT.LEFT, SWT.TOP,
					false, false, 1, 1);
			gdLblNotes.widthHint = 129;
			lblNotes.setLayoutData(gdLblNotes);
			lblNotes.setText("Additional Notes:");
			txtNotes = new Text(cmpst, SWT.BORDER | SWT.MULTI);
			final GridData gdTxtNotes = new GridData(SWT.FILL, SWT.CENTER,
					true, false, 1, 1);
			gdTxtNotes.heightHint = 100;
			txtNotes.setLayoutData(gdTxtNotes);
			txtNotes.setText(notes);
			tab = new TabItem(tabsGrps, SWT.NONE);
			if (name.equals(""))
				tab.setText("new Group");
			else
				tab.setText(name);
			tab.setControl(cmpst);
			if (id == -1)
				grpId = latestGrpId;
			else
				grpId = id;
		}
	}

	private final ArrayList<TabContents>	tabs;				// array of
	// TabContents
	// to
	private Button							btnAddTab		= null;
	private Button							btnDelTab		= null;
	private org.eclipse.swt.widgets.Group	grpGrpsInfo	= null;
	// private Composite cmpst_tab = null; //DO NOT DELETE!!!
	private int								latestGrpId;

	// store
	// all the form's tabs' data
	private TabFolder						tabsGrps		= null;

	protected GrpsBasicInfoPage(final String pageName) {
		super(pageName);
		tabs = new ArrayList<TabContents>();
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
	 * @param noRats
	 *            number of rats
	 * @param ratNumbers
	 *            numbers of rats
	 * @param notes
	 *            any additional notes
	 */
	public void addNewTab(final int id, final String name,
			final String noRats, final String ratNumbers, final String notes) {
		final TabContents tmpTab = new TabContents();
		tmpTab.createGUI(name, noRats, ratNumbers, notes, id);
		latestGrpId++;
		tabs.add(tmpTab);
	}

	public void clearForm() {
		for (final TabContents tc : tabs)
			tc.tab.dispose();
		tabs.clear();
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite cmpstPage = new Composite(parent, 0);
		createGrpsInfo(cmpstPage);
		btnAddTab = new Button(cmpstPage, SWT.NONE);
		btnAddTab.setBounds(new Rectangle(15, 282, 29, 27));
		btnAddTab.setText("+");
		btnAddTab
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						addNewTab();
					}
				});
		btnDelTab = new Button(cmpstPage, SWT.NONE);
		btnDelTab.setBounds(new Rectangle(49, 282, 29, 27));
		btnDelTab.setText("-");
		btnDelTab
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

		grpGrpsInfo = new org.eclipse.swt.widgets.Group(cmpstPage, SWT.NONE);
		grpGrpsInfo.setLayout(null);
		grpGrpsInfo.setText("Information:");
		grpGrpsInfo.setBounds(new Rectangle(5, 5, 559, 273));
		createTabsGrps();
	}

	/**
	 * This method initializes tabs_grps.
	 */
	private void createTabsGrps() {
		tabsGrps = new TabFolder(grpGrpsInfo, SWT.NONE);
		tabsGrps.setBounds(new Rectangle(8, 20, 541, 246));
	}

	/**
	 * Deletes an existing tab.
	 */
	private void deleteTab() {
		TabItem selectedTab=tabsGrps.getSelection()[0];
		for (final TabContents tc : tabs)
			if(tc.tab==selectedTab){
				tc.txtName.setText(ExperimentManager.DELETED_GROUP_NAME);
				break;
			}
		selectedTab.dispose();
	}

	public ArrayList<Grp2GUI> getGroups() {
		final ArrayList<Grp2GUI> groups = new ArrayList<Grp2GUI>();
		for (final TabContents tc : tabs) {
			final Group grp = new Group();
			grp.id = tc.grpId;
			grp.ratsNumbering = tc.txtRatsNumbers.getText();
			grp.notes = tc.txtNotes.getText();
			grp.name = tc.txtName.getText();
			grp.noRats = Integer.parseInt(tc.lblNoRatsData.getText());
			groups.add(grp);
		}
		return groups;
	}

}
