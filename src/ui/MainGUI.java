package ui;

import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import control.ui.ControllerUI;
import control.ui.Ctrl_MainGUI;

/**
 * Main window of the Rat Monitoring Tool, it has links to all program portions.
 * @author Creative
 */
public class MainGUI extends BaseUI{

	private Ctrl_MainGUI controller;
	private Shell sShell = null;
	private Group grp_video = null;
	private Group grp_options = null;
	private Group grp_stats = null;
	private Composite cmpst_main = null;
	public Frame awt_video_main;					//awt frame to display main screen data
	private Menu menuBar = null;
	private MenuItem mnu_file_item=null;
	private MenuItem mnu_edit_item=null;
	private MenuItem mnu_experiment_item=null;
	private MenuItem mnu_camera_item=null;
	private Menu mnu_file=null;
	private Menu mnu_edit=null;
	private Menu mnu_experiment=null;
	private Menu mnu_camera=null;
	private MenuItem mnutm_file_exit = null;
	private MenuItem mnutm_experiment_loadexp = null;
	private MenuItem mnutm_experiment_exporttoexcel = null;
	private MenuItem mnutm_camera_start = null;
	private Button btn_start_tracking = null;
	private Composite cmpst_secondary = null;
	public Frame awt_video_sec;						//awt frame to display the processed image data
	private Label lbl_zone_number = null;
	private Label lbl_central_time = null;
	private Label lbl_session_time = null;
	private Label lbl_central_counter = null;
	private Label lbl_zones_entrance = null;
	private Label lbl_total_distance = null;
	private Button btn_setbg = null;
	private Button btn_stop_tracking = null;
	private Label lbl_status = null;
	private Button btn_start_record = null;
	private Button btn_stop_record = null;
	private Label lbl_rearing_ctr = null;
	private Button btn_rearing_now = null;
	private Button btn_not_rearing = null;
	private Button btn_sub_rearing = null;
	private Button btn_add_rearing = null;


	/**
	 * Creates GUI components, and links this Shell with the parent Shell.
	 */
	public MainGUI()
	{
		createSShell();
		super.sShell=this.sShell;
	}
	
	public void clearForm()
	{
		lbl_session_time.setText(   "Session Time:           " + "0");
		lbl_central_counter.setText("Central Zones Entrance: " + "0");
		lbl_zone_number.setText(    "Current Zone:           " + "0");
		lbl_central_time.setText(   "Central Time:           " + "0.0");
		lbl_zones_entrance.setText( "All Zones Entrance:     " + "0");
		lbl_total_distance.setText( "Total Distance:         " + "0" + " cm");
		lbl_rearing_ctr.setText(	"Rearing counter:        " + "0");
	}
	
	/**
	 * Closes the window.
	 */
	public void closeWindow() {
		if(awt_video_main!=null)
			awt_video_main.dispose();
		if(awt_video_sec!=null)
			awt_video_sec.dispose();
		sShell.dispose();		
	}
	/**
	 * This method initializes cmpst_main	
	 *
	 */
	private void createCmpst_main() {
		cmpst_main = new Composite(grp_video, SWT.EMBEDDED| SWT.BORDER);
		cmpst_main.setLayout(new GridLayout());
		cmpst_main.setLocation(new Point(8, 20));
		cmpst_main.setSize(new Point(640, 480));
		createMainAWTFrame();

	}
	/**
	 * This method initializes cmpst_secondary	
	 *
	 */
	private void createCmpst_secondary() {
		cmpst_secondary = new Composite(grp_stats, SWT.EMBEDDED| SWT.BORDER);
		cmpst_secondary.setLayout(new GridLayout());
		cmpst_secondary.setBounds(new Rectangle(8, 20, 289, 214));
		createSecAWTFrame();
	}
	/**
	 * This method initializes grp_options	
	 *
	 */
	private void createGrp_options() {
		grp_options = new Group(sShell, SWT.NONE);
		grp_options.setLayout(null);
		grp_options.setText("Options:");
		grp_options.setBounds(new Rectangle(7, 520, 665, 51));
		btn_setbg = new Button(grp_options, SWT.NONE);
		btn_setbg.setBounds(new Rectangle(18, 16, 123, 25));
		btn_setbg.setText("Set Background");
		btn_start_tracking = new Button(grp_options, SWT.NONE);
		btn_start_tracking.setBounds(new Rectangle(147, 16, 123, 25));
		btn_start_tracking.setText("Start Tracking");
		btn_stop_tracking = new Button(grp_options, SWT.NONE);
		btn_stop_tracking.setBounds(new Rectangle(278, 16, 123, 25));
		btn_stop_tracking.setText("Stop Tracking");
		btn_start_record = new Button(grp_options, SWT.NONE);
		btn_start_record.setText("Start Recording");
		btn_start_record.setSize(new Point(100, 25));
		btn_start_record.setLocation(new Point(439, 16));
		btn_start_record
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.btn_start_record_Action();
			}
		});
		btn_stop_record = new Button(grp_options, SWT.NONE);
		btn_stop_record.setText("Stop Recording");
		btn_stop_record.setSize(new Point(100, 25));
		btn_stop_record.setLocation(new Point(549, 16));
		btn_stop_record
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.stoprecordAction();
			}
		});
		btn_stop_tracking
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.btn_stop_tracking_Action();
			}
		});
		btn_start_tracking
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.btn_start_tracking_Action();
			}
		});
	}
	
	public void btn_start_record_enable(boolean enable)
	{
		btn_start_record.setEnabled(enable);
	}
	public void btn_stop_record_enable(boolean enable)
	{
		btn_stop_record.setEnabled(enable);
	}
	public void btn_rearing_now_enable(boolean enable)
	{
		btn_rearing_now.setEnabled(enable);
	}
	public void btn_not_rearing_enable(boolean enable)
	{
		btn_not_rearing.setEnabled(enable);
	}
	
	/**
	 * This method initializes grp_stats	
	 *
	 */
	private void createGrp_stats() {
		grp_stats = new Group(sShell, SWT.NONE);
		grp_stats.setLayout(null);
		grp_stats.setText("Variables:");
		createCmpst_secondary();
		grp_stats.setBounds(new Rectangle(678, 6, 301, 518));
		lbl_zone_number = new Label(grp_stats, SWT.NONE);
		lbl_zone_number.setBounds(new Rectangle(18, 240, 267, 15));
		lbl_zone_number.setText("Current Zone: ");
		lbl_central_time = new Label(grp_stats, SWT.NONE);
		lbl_central_time.setBounds(new Rectangle(18, 300, 267, 17));
		lbl_central_time.setText("Central Time:");
		lbl_session_time = new Label(grp_stats, SWT.NONE);
		lbl_session_time.setBounds(new Rectangle(18, 330, 267, 18));
		lbl_session_time.setText("Session Time:");
		lbl_central_counter = new Label(grp_stats, SWT.NONE);
		lbl_central_counter.setBounds(new Rectangle(18, 270, 267, 15));
		lbl_central_counter.setText("Central Zones Entrance:");
		lbl_zones_entrance = new Label(grp_stats, SWT.NONE);
		lbl_zones_entrance.setBounds(new Rectangle(18, 389, 267, 16));
		lbl_zones_entrance.setText("All Zones Entrance:");
		lbl_total_distance = new Label(grp_stats, SWT.NONE);
		lbl_total_distance.setBounds(new Rectangle(18, 419, 267, 16));
		lbl_total_distance.setText("Total Distance:");
		lbl_rearing_ctr = new Label(grp_stats, SWT.NONE);
		lbl_rearing_ctr.setBounds(new Rectangle(18, 358, 203, 17));
		lbl_rearing_ctr.setText("Rearing counter:");
		btn_rearing_now = new Button(grp_stats, SWT.NONE);
		btn_rearing_now.setText("Rearing NOW");
		btn_rearing_now.setSize(new Point(100, 25));
		btn_rearing_now.setLocation(new Point(188, 479));
		btn_not_rearing = new Button(grp_stats, SWT.NONE);
		btn_not_rearing.setBounds(new Rectangle(85, 479, 101, 25));
		btn_not_rearing.setText("Not Rearing");
		btn_sub_rearing = new Button(grp_stats, SWT.NONE);
		btn_sub_rearing.setBounds(new Rectangle(256, 355, 28, 21));
		btn_sub_rearing.setText("-");
		btn_sub_rearing
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						controller.btn_sub_rearing_Action();
					}
				});
		btn_add_rearing = new Button(grp_stats, SWT.NONE);
		btn_add_rearing.setText("+");
		btn_add_rearing.setSize(new Point(28, 21));
		btn_add_rearing.setLocation(new Point(226, 355));
		btn_add_rearing
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						controller.btn_add_rearing_Action();
					}
				});
		btn_not_rearing
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.btn_not_rearing_Action();
			}
		});
		btn_rearing_now
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.btn_rearing_now_Action();
			}
		});
	}
	/**
	 * This method initializes grp_video	
	 *
	 */
	private void createGrp_video() {
		grp_video = new Group(sShell, SWT.NONE);
		grp_video.setLayout(null);
		grp_video.setText("Video:");

		grp_video.setBounds(new Rectangle(7, 5, 665, 510));
		createCmpst_main();
	}

	private void createMainAWTFrame()
	{
		awt_video_main=SWT_AWT.new_Frame(cmpst_main);	
		awt_video_main.setVisible(true);
		awt_video_main.setSize(cmpst_main.getSize().x, cmpst_main.getSize().y);
	}

	private void createSecAWTFrame()
	{
		awt_video_sec = SWT_AWT.new_Frame(cmpst_secondary);
		awt_video_sec.setVisible(true);
		awt_video_sec.setSize(cmpst_secondary.getSize().x, cmpst_secondary.getSize().y);
	}

	/**   
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		sShell.setText("Tracking Object");
		createGrp_video();
		createGrp_options();
		createGrp_stats();
		sShell.setSize(new Point(995, 649));
		sShell.setLayout(null);
		lbl_status = new Label(getShell(), SWT.NONE);
		lbl_status.setBounds(new Rectangle(7, 577, 656, 20));
		lbl_status.setText("");
		btn_setbg.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.btn_setbg_Action();
			}
		});

		menuBar = new Menu(sShell, SWT.BAR);
		sShell.setMenuBar(menuBar);

		mnu_file_item = new MenuItem(menuBar, SWT.CASCADE); //file
		mnu_edit_item = new MenuItem(menuBar, SWT.CASCADE); // edit
		mnu_file_item.setText("File");
		mnu_file = new Menu(mnu_file_item);
		mnu_file_item.setMenu(mnu_file);
		mnutm_file_exit = new MenuItem(mnu_file,0);
		mnutm_file_exit.setText("Exit");
		mnutm_file_exit.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.closeProgram();
			}
		});

		mnu_camera_item = new MenuItem(menuBar, SWT.CASCADE); //camera
		mnu_camera_item.setText("Camera");
		mnu_camera = new Menu(mnu_camera_item);
		mnu_camera_item.setMenu(mnu_camera);
		mnutm_camera_start = new MenuItem(mnu_camera,0);
		mnutm_camera_start.setText("Start");
		mnutm_camera_start.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					org.eclipse.swt.events.SelectionEvent e) {
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.mnutm_camera_start_Action();
			}
		});

		MenuItem mnutm_camera_options = new MenuItem(mnu_camera, SWT.PUSH);
		mnutm_camera_options.setText("Options ..");
		MenuItem mnuitm_stop_camera = new MenuItem(mnu_camera, SWT.PUSH);
		mnuitm_stop_camera.setText("Stop");
		mnuitm_stop_camera
		.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					org.eclipse.swt.events.SelectionEvent e) {
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.mnuitm_stop_camera_Action();
			}
		});
		mnutm_camera_options
		.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					org.eclipse.swt.events.SelectionEvent e) {
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.mnutm_camera_options_Action();
			}
		});

		mnu_edit_item.setText("Edit");
		mnu_edit = new Menu(mnu_edit_item);
		mnu_edit_item.setMenu(mnu_edit);
		MenuItem mnutm_edit_openzoneeditor = new MenuItem(mnu_edit, SWT.PUSH);
		mnutm_edit_openzoneeditor.setText("Zone Editor ..");
		mnutm_edit_openzoneeditor
		.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					org.eclipse.swt.events.SelectionEvent e) {
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.mnutm_edit_openzoneeditor_Action();
			}
		});

		MenuItem mnutm_edit_options = new MenuItem(mnu_edit, SWT.PUSH);
		mnutm_edit_options.setText("Options ..");
		mnutm_edit_options
		.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					org.eclipse.swt.events.SelectionEvent e) {
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.mnutm_edit_options_Action();
			}
		});

		mnu_experiment_item = new MenuItem(menuBar, SWT.CASCADE); // experiment
		mnu_experiment_item.setText("Experiment");
		mnu_experiment = new Menu(mnu_experiment_item);
		mnu_experiment_item.setMenu(mnu_experiment);
		MenuItem mnutm_experiment_newexp = new MenuItem(mnu_experiment, SWT.PUSH);
		mnutm_experiment_loadexp = new MenuItem(mnu_experiment,0);
		mnutm_experiment_loadexp.setText("Load Exp.");
		mnutm_experiment_loadexp.setEnabled(true);
		MenuItem mnuitm_edt_exp = new MenuItem(mnu_experiment, SWT.PUSH);
		mnutm_experiment_exporttoexcel = new MenuItem(mnu_experiment,0);
		mnutm_experiment_exporttoexcel.setText("Export to Excel");
		mnutm_experiment_exporttoexcel.setEnabled(true);
		mnutm_experiment_exporttoexcel
		.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.mnutm_experiment_exporttoexcel_Action();
			}
			public void widgetDefaultSelected(
					org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		mnutm_experiment_newexp.setText("New Exp..");

		mnuitm_edt_exp.setText("Edit Exp.");
		mnuitm_edt_exp
		.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					org.eclipse.swt.events.SelectionEvent e) {
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.mnuitm_edt_exp_Action();
			}
		});
		mnutm_experiment_newexp
		.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					org.eclipse.swt.events.SelectionEvent e) {
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.mnutm_experiment_newexp_Action();
			}
		});

		mnutm_experiment_loadexp.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {   
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				controller.mnutm_experiment_loadexp_Action(sShell);
			}
		});

		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				controller.closeProgram();
			}
		});
		clearForm();
	}

	/**
	 * Gets the Shell instance of the GUI.
	 * @return Shell instance of the GUI
	 */
	public Shell getShell() {
		return sShell;
	}

	/**
	 * Gets the label that displays the status messages.
	 * @return Label that displays status messages of the GUI
	 */
	public Label getStatusLabel()
	{
		return lbl_status;
	}

	@Override
	public void loadData(String[] strArray) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setController(ControllerUI controller) {
		super.setController(controller);
		this.controller=(Ctrl_MainGUI) super.controller;
	}
	
	/**
	 * Updates counters and statistics.
	 * @param tot_distance total distance by the rat
	 * @param current_zone current zone
	 * @param session_time total session time till now
	 * @param central_counter central zone counter
	 * @param normal_counter all zones entrance counter
	 * @param central_time central zone time
	 * @param rearing_ctr rearing counter
	 */
	public void updateStats(long tot_distance,int current_zone,float session_time,int central_counter,int normal_counter,float central_time, int rearing_ctr)
	{
		if(session_time!=-1)
			lbl_session_time.setText("Session Time:           " + Float.toString(session_time));
		if(central_counter!=-1)
			lbl_central_counter.setText("Central Zones Entrance: " + Integer.toString(central_counter));
		if(current_zone!=-1)
			lbl_zone_number.setText("Current Zone:           " + Integer.toString(current_zone));
		if(central_time!=-1)
			lbl_central_time.setText("Central Time:           " + Float.toString(central_time));
		if(normal_counter!=-1)
			lbl_zones_entrance.setText("All Zones Entrance:     " + Integer.toString(normal_counter));
		if(tot_distance!=-1)
			lbl_total_distance.setText("Total Distance:         " + Double.toString(tot_distance) + " cm");
		if(rearing_ctr!=-1)
			lbl_rearing_ctr.setText(	"Rearing counter:        " + Integer.toString(rearing_ctr));
	}
}
