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
	private FormData	fdGrpInfo;
	private Group		grpInfo1;
	private Label		lblDate		= null;
	private Label		lblName		= null;
	private Label		lblNotes		= null;
	private Label		lblUsername	= null;
	private Label		lblExpDate		= null;
	private Text		txtName		= null;
	private Text		txtNotes		= null;
	private Text		txtUser		= null;

	protected ExpBasicInfoPage(final String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite cmpstPage = new Composite(parent, 0);
		createGrpInfo(cmpstPage);
		final FormData fdBtnSave = new FormData();
		fdBtnSave.top = new FormAttachment(grpInfo1, 6);
		fdBtnSave.right = new FormAttachment(100, -117);
		fdBtnSave.left = new FormAttachment(0, 144);
		final FormData fdBtnCancel = new FormData();
		fdBtnCancel.top = new FormAttachment(grpInfo1, 6);
		fdBtnCancel.right = new FormAttachment(100, -10);
		setLabelToDate();

		setControl(cmpstPage);
	}

	/**
	 * This method initializes grp_info.
	 */
	private void createGrpInfo(final Composite parent) {
		parent.setLayout(new FormLayout());
		grpInfo1 = new Group(parent, SWT.NONE);
		grpInfo1.setLayout(new GridLayout(2, false));
		fdGrpInfo = new FormData();
		fdGrpInfo.right = new FormAttachment(0, 564);
		fdGrpInfo.left = new FormAttachment(0, 5);
		fdGrpInfo.bottom = new FormAttachment(0, 276);
		fdGrpInfo.top = new FormAttachment(0, 5);
		grpInfo1.setLayoutData(fdGrpInfo);
		grpInfo1.setText("Information:");
		lblName = new Label(grpInfo1, SWT.NONE);
		lblName.setText("Name:");
		txtName = new Text(grpInfo1, SWT.BORDER);
		final GridData gdTxtName = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gdTxtName.widthHint = 125;
		txtName.setLayoutData(gdTxtName);

		final Label lblType = new Label(grpInfo1, SWT.NONE);
		lblType.setText("Type");

		cmboType = new Combo(grpInfo1, SWT.READ_ONLY);
		cmboType.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1));
		cmboType.setItems(new String[] { "Open Field", "Forced Swimming" });
		cmboType.setText("Open Field");
		lblUsername = new Label(grpInfo1, SWT.NONE);
		lblUsername.setText("User:");
		txtUser = new Text(grpInfo1, SWT.BORDER);
		txtUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		lblDate = new Label(grpInfo1, SWT.NONE);
		lblDate.setText("Date:");
		lblExpDate = new Label(grpInfo1, SWT.NONE);
		lblExpDate.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		lblExpDate.setText("Current Date");
		lblNotes = new Label(grpInfo1, SWT.NONE);
		final GridData gdLblNotes = new GridData(SWT.LEFT, SWT.TOP, false,
				false, 1, 1);
		gdLblNotes.widthHint = 125;
		lblNotes.setLayoutData(gdLblNotes);
		lblNotes.setText("Additional Notes:");
		txtNotes = new Text(grpInfo1, SWT.BORDER | SWT.MULTI);
		final GridData gdTxtNotes = new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1);
		gdTxtNotes.heightHint = 124;
		txtNotes.setLayoutData(gdTxtNotes);
	}

	public String getExpDate() {
		return lblExpDate.getText();
	}

	public String getExpName() {
		return txtName.getText();
	}

	public String getExpNotes() {
		return txtNotes.getText();
	}

	public String getExpType() {
		return cmboType.getText();
	}

	public String getUserName() {
		return txtUser.getText();
	}

	public void loadData(final String[] strArray) {
		txtName.setText(strArray[0]);
		txtUser.setText(strArray[1]);
		txtNotes.setText(strArray[2]);
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
