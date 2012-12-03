package ui.wizards.newexperiment;

import java.util.Calendar;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ExpBasicInfoPage extends WizardPage {

	private Combo		cmboType;
	private FormData	fd_grp_info;
	private Group		grp_info_1;
	private Label		lbl_date		= null;
	private Label		lbl_name		= null;
	private Label		lbl_notes		= null;
	private Label		lbl_username	= null;
	private Label		lblExpDate		= null;
	private Text		txt_name		= null;
	private Text		txt_notes		= null;
	private Text		txt_user		= null;

	protected ExpBasicInfoPage(final String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite cmpstPage = new Composite(parent, 0);
		createGrpInfo(cmpstPage);
		final FormData fd_btn_save = new FormData();
		fd_btn_save.top = new FormAttachment(grp_info_1, 6);
		fd_btn_save.right = new FormAttachment(100, -117);
		fd_btn_save.left = new FormAttachment(0, 144);
		final FormData fd_btn_cancel = new FormData();
		fd_btn_cancel.top = new FormAttachment(grp_info_1, 6);
		fd_btn_cancel.right = new FormAttachment(100, -10);
		setLabelToDate();

		setControl(cmpstPage);
	}

	/**
	 * This method initializes grp_info.
	 */
	private void createGrpInfo(final Composite parent) {
		parent.setLayout(new FormLayout());
		grp_info_1 = new Group(parent, SWT.NONE);
		grp_info_1.setLayout(new GridLayout(2, false));
		fd_grp_info = new FormData();
		fd_grp_info.right = new FormAttachment(0, 564);
		fd_grp_info.left = new FormAttachment(0, 5);
		fd_grp_info.bottom = new FormAttachment(0, 276);
		fd_grp_info.top = new FormAttachment(0, 5);
		grp_info_1.setLayoutData(fd_grp_info);
		grp_info_1.setText("Information:");
		lbl_name = new Label(grp_info_1, SWT.NONE);
		lbl_name.setText("Name:");
		txt_name = new Text(grp_info_1, SWT.BORDER);
		final GridData gd_txt_name = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_txt_name.widthHint = 125;
		txt_name.setLayoutData(gd_txt_name);

		final Label lblType = new Label(grp_info_1, SWT.NONE);
		lblType.setText("Type");

		cmboType = new Combo(grp_info_1, SWT.READ_ONLY);
		cmboType.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1));
		cmboType.setItems(new String[] { "Open Field", "Forced Swimming" });
		cmboType.setText("Open Field");
		cmboType.setEnabled(false); // TODO: re-enable FS in v0.5
		lbl_username = new Label(grp_info_1, SWT.NONE);
		lbl_username.setText("User:");
		txt_user = new Text(grp_info_1, SWT.BORDER);
		txt_user.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		lbl_date = new Label(grp_info_1, SWT.NONE);
		lbl_date.setText("Date:");
		lblExpDate = new Label(grp_info_1, SWT.NONE);
		lblExpDate.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		lblExpDate.setText("Current Date");
		lbl_notes = new Label(grp_info_1, SWT.NONE);
		final GridData gd_lbl_notes = new GridData(SWT.LEFT, SWT.TOP, false,
				false, 1, 1);
		gd_lbl_notes.widthHint = 125;
		lbl_notes.setLayoutData(gd_lbl_notes);
		lbl_notes.setText("Additional Notes:");
		txt_notes = new Text(grp_info_1, SWT.BORDER | SWT.MULTI);
		final GridData gd_txt_notes = new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1);
		gd_txt_notes.heightHint = 124;
		txt_notes.setLayoutData(gd_txt_notes);
	}

	public String getExpDate() {
		return lblExpDate.getText();
	}

	public String getExpName() {
		return txt_name.getText();
	}

	public String getExpNotes() {
		return txt_notes.getText();
	}

	public String getExpType() {
		return cmboType.getText();
	}

	public String getUserName() {
		return txt_user.getText();
	}

	public void loadData(final String[] strArray) {
		txt_name.setText(strArray[0]);
		txt_user.setText(strArray[1]);
		txt_notes.setText(strArray[2]);
		lblExpDate.setText(strArray[3]);
		cmboType.setText(strArray[4]);
	}

	/**
	 * Sets the label to the current date.
	 */
	private void setLabelToDate() {
		lblExpDate.setText(Calendar.getInstance().getTime().toString());
	}
}
