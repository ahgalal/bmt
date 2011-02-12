package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ui.box.MsgBox;

import control.ui.ControllerUI;
import control.ui.Ctrl_RatInfoForm;

/**
 * @author Creative
 *Enables you to identify your Rat's Number & the Group Name that your rat will join
 */
public class RatInfoForm extends BaseUI{
	private Ctrl_RatInfoForm controller;  //  @jve:decl-index=0:

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="13,18"
	private Label lbl_ratnumber = null;
	private Label lbl_groupname = null;
	private Text txt_ratnumber = null;
	private Button btn_ok = null;
	private Button btn_cancel = null;
	private Combo cmbo_grp_names = null;

	/**
	 * Creates GUI components, and links this Shell with the parent Shell.
	 */
	public RatInfoForm() {
		createSShell();
		super.sShell=this.sShell;
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell(SWT.APPLICATION_MODAL | SWT.ON_TOP | SWT.TITLE| SWT.TITLE | SWT.CLOSE);
		sShell.setText("RatInfo");
		sShell.setLayout(null);
		sShell.setSize(new Point(201, 138));
		lbl_ratnumber = new Label(sShell, SWT.NONE);
		lbl_ratnumber.setText("Rat Number");
		lbl_ratnumber.setBounds(new Rectangle(10, 18, 86, 15));
		lbl_groupname = new Label(sShell, SWT.NONE);
		lbl_groupname.setBounds(new Rectangle(10, 50, 86, 15));
		lbl_groupname.setText("Group Name");
		txt_ratnumber = new Text(sShell, SWT.BORDER);
		txt_ratnumber.setBounds(new Rectangle(113, 14, 73, 21));
		btn_ok = new Button(sShell, SWT.NONE);
		btn_ok.setBounds(new Rectangle(13, 79, 82, 24));
		btn_ok.setText("OK");
		btn_ok.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(controller.setVars(new String[] {txt_ratnumber.getText(),cmbo_grp_names.getText()}))
					controller.btn_ok_Action();
				else
					MsgBox.show(sShell, "Please enter valid rat number", "Error", SWT.ERROR);
			}
		});
		btn_cancel = new Button(sShell, SWT.NONE);
		btn_cancel.setBounds(new Rectangle(104, 79, 82, 24));
		btn_cancel.setText("Cancel");
		createCmbo_grp_names();
		btn_cancel.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				sShell.setVisible(false);
			}
		});
	}

	public Shell getShell()
	{
		return sShell;
	}

	/**
	 * This method initializes cmbo_grp_names	
	 *
	 */

	private void createCmbo_grp_names() {
		cmbo_grp_names = new Combo(sShell, SWT.READ_ONLY);
		cmbo_grp_names.setBounds(new Rectangle(115, 45, 73, 23));
	}

	/**
	 * Loads grps_names(groups names) it has(entered before) to the cmbo_grp_names(Combo)
	 */
	private void loadGroupsToCombo(String[] grps_names)
	{
		for(int i=0;i<grps_names.length;i++)
		{
			cmbo_grp_names.add(grps_names[i]);
		}
		cmbo_grp_names.select(0);
	}
	@Override
	public void loadData(String[] strArray) {
		txt_ratnumber.setText(strArray[0]);
		String[] grps = new String[strArray.length-1];
		for(int i=1;i<strArray.length;i++)
			grps[i-1]=strArray[i];
		loadGroupsToCombo(grps);
	}
	@Override
	public void setController(ControllerUI controller) {
		super.setController(controller);
		this.controller=(Ctrl_RatInfoForm) super.controller;
	}

	/**
	 * Shows/Hides the GUI controlled by a child of this class
	 */
	@Override
	public void show(boolean visibility) {
		super.show(visibility);
	}
	@Override
	public void clearForm() {
		txt_ratnumber.setText("");
		cmbo_grp_names.removeAll();
	}


}



