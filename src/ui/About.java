package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import control.ui.Ctrl_About;

public class About extends BaseUI {
	Ctrl_About controller;  //  @jve:decl-index=0:
	
	public  About(){
		createSShell();
		super.sShell=sShell;
	}
	
	//private Ctrl_About controller;  
	
	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="4,7"
	private Label lbl_proj_desc = null;
	private Button btn_OK = null;
	private Group grp_format = null;
	private Label lbl_creator = null;
	private Label lbl_assistants = null;
	private Text textArea = null;
	private Text txt_owner_name = null;
	private Text txt_owner_email = null;
	private Label lbl_name = null;
	private Label lbl_email = null;
	private Text txt_assistants_name = null;
	private Text txt_assistants_email = null;

	/**
	 * This method initializes sShell1	
	 *
	 */
	private void createSShell() {
		sShell = new Shell(SWT.APPLICATION_MODAL | SWT.ON_TOP | SWT.TITLE);
		sShell.setLayout(null);
		sShell.setText("About");
		sShell.setMaximized(false);
		sShell.setSize(new Point(500, 352));
		btn_OK = new Button(sShell, SWT.NONE);
		btn_OK.setBounds(new Rectangle(207, 289, 73, 25));
		btn_OK.setText("OK");
		btn_OK.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				System.out.println("widgetSelected()"); // TODO Auto-generated Event stub widgetSelected()
				sShell.setVisible(false);
			}
		});
		lbl_proj_desc = new Label(sShell, SWT.NONE);
		lbl_proj_desc.setBounds(new Rectangle(11, 5, 112, 17));
		lbl_proj_desc.setText("Project description");
		
		createGrp_format();
		textArea = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		textArea.setBounds(new Rectangle(11, 40, 468, 113));
		textArea.setText("");
	}

	/**
	 * This method initializes grp_format	
	 *
	 */
	private void createGrp_format() {
		grp_format = new Group(sShell, SWT.NONE);
		grp_format.setLayout(null);
		grp_format.setText("Credits");
		grp_format.setBounds(new Rectangle(11, 165, 467, 121));
		lbl_creator = new Label(grp_format, SWT.NONE);
		lbl_creator.setBounds(new Rectangle(11, 30, 35, 15));
		lbl_creator.setText("Owner");
		lbl_assistants = new Label(grp_format, SWT.NONE);
		lbl_assistants.setBounds(new Rectangle(6, 67, 52, 15));
		lbl_assistants.setText("Assistants");
		txt_owner_name = new Text(grp_format, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		txt_owner_name.setBounds(new Rectangle(86, 34, 174, 19));
		txt_owner_name.setText("Ahmed Galal");
		txt_owner_email = new Text(grp_format, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		txt_owner_email.setBounds(new Rectangle(273, 34, 194, 23));
		txt_owner_email.setText("ceng.ahmedgalal@gmail.com");
		lbl_name = new Label(grp_format, SWT.NONE);
		lbl_name.setBounds(new Rectangle(89, 14, 61, 15));
		lbl_name.setText("Name");
		lbl_email = new Label(grp_format, SWT.NONE);
		lbl_email.setBounds(new Rectangle(274, 11, 51, 15));
		lbl_email.setText("E-Mail");
		txt_assistants_name = new Text(grp_format, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		txt_assistants_name.setBounds(new Rectangle(87, 67, 172, 34));
		txt_assistants_name.setText("Ahmed Mohammed Ali         Mohammed Ramadan ");
		txt_assistants_email = new Text(grp_format, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		txt_assistants_email.setBounds(new Rectangle(273, 67, 193, 34));
		txt_assistants_email.setText("a.mohamed.aly0@gmail.com mido_emak@hotmail.com");
	}

	/*
	 * Temporary main generation 
	 */
//	public static void main(String[] args) {
//		// before you run this, make sure to set up the following in
//		// the launch configuration (Arguments->VM Arguments) for the correct SWT lib. path
//		// the following is a windows example,
//		// -Djava.library.path="installation_directory\plugins\org.eclipse.swt.win32_3.0.1\os\win32\x86"
//		org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display
//				.getDefault();
//		About test = new About();
//		//test.createSShell();
//		test.sShell.open();
//	
//		while (!test.sShell.isDisposed()) {
//			if (!display.readAndDispatch())
//				display.sleep();
//		}
//		display.dispose();
//	}  //  @jve:decl-index=0:visual-constraint="245,5"

	@Override
	public void clearForm() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void loadData(String[] strArray) {
		// TODO Auto-generated method stub
		
	}
}
