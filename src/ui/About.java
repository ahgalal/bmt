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
	private Text textArea = null;
	private Text txt_owner_name = null;
	private Text txt_owner_email = null;
	/**
	 * This method initializes sShell1	
	 *
	 */
	private void createSShell() {
		sShell = new Shell(SWT.APPLICATION_MODAL | SWT.ON_TOP | SWT.TITLE);
		sShell.setLayout(null);
		sShell.setText("About");
		sShell.setMaximized(false);
		sShell.setSize(new Point(467, 326));
		btn_OK = new Button(sShell, SWT.NONE);
		btn_OK.setBounds(new Rectangle(378, 271, 73, 25));
		btn_OK.setText("OK");
		btn_OK.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				System.out.println("widgetSelected()"); // TODO Auto-generated Event stub widgetSelected()
				sShell.setVisible(false);
			}
		});
		lbl_proj_desc = new Label(sShell, SWT.NONE);
		lbl_proj_desc.setBounds(new Rectangle(11, 5, 112, 17));
		lbl_proj_desc.setText("Project description:");
		
		createGrp_format();
		textArea = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		textArea.setBounds(new Rectangle(11, 29, 443, 124));
		textArea.setText("");
	}

	/**
	 * This method initializes grp_format	
	 *
	 */
	private void createGrp_format() {
		grp_format = new Group(sShell, SWT.NONE);
		grp_format.setLayout(null);
		grp_format.setText("Credits:");
		grp_format.setBounds(new Rectangle(11, 165, 444, 105));
		txt_owner_name = new Text(grp_format, SWT.MULTI | SWT.WRAP);
		txt_owner_name.setBounds(new Rectangle(10, 20, 236, 82));
		txt_owner_name.setEditable(false);
		txt_owner_name.setEnabled(true);
		txt_owner_name.setText("- Ahmed Galal El-Din (Software)\n- Ahmed Mohammed Ali (Software)\n- Mohammed Ahmed Ramadan (Software)\n- Mohammed Mostafa (Neuro-Science)\n- Sarah Mohammed Hamed (Software)");
		txt_owner_email = new Text(grp_format, SWT.MULTI | SWT.WRAP);
		txt_owner_email.setBounds(new Rectangle(266, 20, 169, 61));
		txt_owner_email.setEditable(false);
		txt_owner_email.setText("ceng.ahmedgalal@gmail.com\na.mohamed.aly0@gmail.com\nmido_emak@hotmail.com\neltabbal@gmail.com");
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
