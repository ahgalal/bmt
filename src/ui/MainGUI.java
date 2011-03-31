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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import control.ui.ControllerUI;
import control.ui.CtrlMainGUI;

/**
 * Main window of the Rat Monitoring Tool, it has links to all program portions.
 * 
 * @author Creative
 */
public class MainGUI extends BaseUI
{

	private CtrlMainGUI controller;
	private Shell sShell = null;
	private Group grp_video = null;
	private Group grp_options = null;
	private Group grp_stats = null;
	private Composite cmpst_main = null;
	private Frame awt_video_main; // awt frame to display main screen data
	private Menu menuBar = null;
	private MenuItem mnu_file_item = null;
	private MenuItem mnu_edit_item = null;
	private MenuItem mnu_experiment_item = null;
	private MenuItem mnu_camera_item = null;
	private MenuItem mnu_help_item = null;
	private Menu mnu_file = null;
	private Menu mnu_edit = null;
	private Menu mnu_experiment = null;
	private Menu mnu_camera = null;
	private Menu mnu_help = null;
	private MenuItem mnutm_file_exit = null;
	private MenuItem mnutm_experiment_loadexp = null;
	private MenuItem mnutm_experiment_exporttoexcel = null;
	private MenuItem mnuitm_edt_exp;
	private MenuItem mnutm_camera_start = null;
	private Button btn_start_tracking = null;
	private Composite cmpst_secondary = null;
	private Frame awt_video_sec; // awt frame to display the processed image
	// data
	private Button btn_setbg = null;
	private Button btn_stop_tracking = null;
	private Label lbl_status = null;
	private Button btn_start_record = null;
	private Button btn_stop_record = null;
	private Button btn_rearing_now = null;
	private Button btn_not_rearing = null;
	private Button btn_sub_rearing = null;
	private Button btn_add_rearing = null;
	private Table tbl_data = null;

	/**
	 * Creates GUI components, and links this Shell with the parent Shell.
	 */
	public MainGUI()
	{
		createSShell();
		super.sShell = this.sShell;
	}

	@Override
	public void clearForm()
	{
		tbl_data.removeAll();
	}

	/**
	 * Closes the window.
	 */
	public void closeWindow()
	{
		if (getAwtVideoMain() != null)
			getAwtVideoMain().dispose();
		if (getAwtVideoSec() != null)
			getAwtVideoSec().dispose();
		sShell.dispose();
	}

	/**
	 * This method initializes cmpst_main.
	 */
	private void createCmpstMain()
	{
		cmpst_main = new Composite(grp_video, SWT.EMBEDDED | SWT.BORDER);
		cmpst_main.setLayout(new GridLayout());
		cmpst_main.setLocation(new Point(8, 20));
		cmpst_main.setSize(new Point(640, 480));
		createMainAWTFrame();

	}

	/**
	 * This method initializes cmpst_secondary.
	 */
	private void createCmpstSecondary()
	{
		cmpst_secondary = new Composite(grp_stats, SWT.EMBEDDED | SWT.BORDER);
		cmpst_secondary.setLayout(new GridLayout());
		cmpst_secondary.setBounds(new Rectangle(8, 20, 289, 214));
		createSecAWTFrame();
	}

	/**
	 * This method initializes grp_options.
	 */
	private void createGrpOptions()
	{
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
		btn_start_record.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.btnStartRecordAction();
			}
		});
		btn_stop_record = new Button(grp_options, SWT.NONE);
		btn_stop_record.setText("Stop Recording");
		btn_stop_record.setSize(new Point(100, 25));
		btn_stop_record.setLocation(new Point(549, 16));
		btn_stop_record.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.stoprecordAction();
			}
		});
		btn_stop_tracking.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.btnStopTrackingAction();
			}
		});
		btn_start_tracking.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.btnStartTrackingAction();
			}
		});
	}

	public void btnStartRecordEnable(final boolean enable)
	{
		btn_start_record.setEnabled(enable);
	}

	public void btnStopRecordEnable(final boolean enable)
	{
		btn_stop_record.setEnabled(enable);
	}

	public void btnRearingNowEnable(final boolean enable)
	{
		btn_rearing_now.setEnabled(enable);
	}

	public void btnNotRearingEnable(final boolean enable)
	{
		btn_not_rearing.setEnabled(enable);
	}

	public void editExpMenuItemEnable(final boolean enable)
	{
		mnuitm_edt_exp.setEnabled(enable);
	}

	public void exportExpToExcelMenuItemEnable(final boolean enable)
	{
		mnutm_experiment_exporttoexcel.setEnabled(enable);
	}

	/**
	 * This method initializes grp_stats.
	 */
	private void createGrpStats()
	{
		grp_stats = new Group(sShell, SWT.NONE);
		grp_stats.setLayout(null);
		grp_stats.setText("Variables:");
		createCmpstSecondary();
		grp_stats.setBounds(new Rectangle(678, 6, 301, 563));
		btn_rearing_now = new Button(grp_stats, SWT.NONE);
		btn_rearing_now.setText("Rearing NOW");
		btn_rearing_now.setSize(new Point(100, 25));
		btn_rearing_now.setLocation(new Point(188, 530));
		btn_not_rearing = new Button(grp_stats, SWT.NONE);
		btn_not_rearing.setBounds(new Rectangle(85, 530, 101, 25));
		btn_not_rearing.setText("Not Rearing");
		btn_sub_rearing = new Button(grp_stats, SWT.NONE);
		btn_sub_rearing.setBounds(new Rectangle(44, 528, 28, 21));
		btn_sub_rearing.setText("-");
		btn_sub_rearing.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.btnSubRearingAction();
			}
		});
		btn_add_rearing = new Button(grp_stats, SWT.NONE);
		btn_add_rearing.setText("+");
		btn_add_rearing.setSize(new Point(28, 21));
		btn_add_rearing.setLocation(new Point(14, 528));
		tbl_data = new Table(grp_stats, SWT.BORDER);
		tbl_data.setHeaderVisible(true);
		tbl_data.setLinesVisible(true);
		tbl_data.setBounds(new Rectangle(9, 243, 284, 272));
		final TableColumn tableColumn = new TableColumn(tbl_data, SWT.NONE);
		tableColumn.setWidth(140);
		tableColumn.setText("Name");
		final TableColumn tableColumn1 = new TableColumn(tbl_data, SWT.NONE);
		tableColumn1.setWidth(140);
		tableColumn1.setText("Value");
		btn_add_rearing.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.btnAddRearingAction();
			}
		});
		btn_not_rearing.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.btnNotRearingAction();
			}
		});
		btn_rearing_now.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.btnRearingNowAction();
			}
		});
	}

	/**
	 * This method initializes grp_video.
	 */
	private void createGrpVideo()
	{
		grp_video = new Group(sShell, SWT.NONE);
		grp_video.setLayout(null);
		grp_video.setText("Video:");

		grp_video.setBounds(new Rectangle(7, 5, 665, 510));
		createCmpstMain();
	}

	private void createMainAWTFrame()
	{
		awt_video_main = SWT_AWT.new_Frame(cmpst_main);
		getAwtVideoMain().setVisible(true);
		getAwtVideoMain().setSize(cmpst_main.getSize().x, cmpst_main.getSize().y);
	}

	private void createSecAWTFrame()
	{
		awt_video_sec = SWT_AWT.new_Frame(cmpst_secondary);
		getAwtVideoSec().setVisible(true);
		getAwtVideoSec().setSize(cmpst_secondary.getSize().x, cmpst_secondary.getSize().y);
	}

	/**
	 * This method initializes sShell.
	 */
	private void createSShell()
	{
		sShell = new Shell(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		sShell.setText("Behavioral Monitoring Tool");
		createGrpVideo();
		createGrpOptions();
		createGrpStats();
		sShell.setSize(new Point(995, 649));
		sShell.setLayout(null);
		lbl_status = new Label(getShell(), SWT.NONE);
		lbl_status.setBounds(new Rectangle(7, 577, 656, 20));
		lbl_status.setText("");
		btn_setbg.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.btnSetbgAction();
			}
		});

		menuBar = new Menu(sShell, SWT.BAR);
		sShell.setMenuBar(menuBar);

		mnu_file_item = new MenuItem(menuBar, SWT.CASCADE); // file
		mnu_edit_item = new MenuItem(menuBar, SWT.CASCADE); // edit
		mnu_file_item.setText("File");
		mnu_file = new Menu(mnu_file_item);
		mnu_file_item.setMenu(mnu_file);
		mnutm_file_exit = new MenuItem(mnu_file, 0);
		mnutm_file_exit.setText("Exit");
		mnutm_file_exit.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0)
			{
			}

			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.closeProgram();
			}
		});

		mnu_camera_item = new MenuItem(menuBar, SWT.CASCADE); // camera
		mnu_camera_item.setText("Camera");
		mnu_camera = new Menu(mnu_camera_item);
		mnu_camera_item.setMenu(mnu_camera);
		mnutm_camera_start = new MenuItem(mnu_camera, 0);
		mnutm_camera_start.setText("Start");
		mnutm_camera_start.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					final org.eclipse.swt.events.SelectionEvent e)
			{
			}

			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.mnutmCameraStartAction();
			}
		});

		final MenuItem mnutm_camera_options = new MenuItem(mnu_camera, SWT.PUSH);
		mnutm_camera_options.setText("Options ..");
		final MenuItem mnuitm_stop_camera = new MenuItem(mnu_camera, SWT.PUSH);
		mnuitm_stop_camera.setText("Stop");
		mnuitm_stop_camera.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					final org.eclipse.swt.events.SelectionEvent e)
			{
			}

			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.mnuitmStopCameraAction();
			}
		});
		mnutm_camera_options.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					final org.eclipse.swt.events.SelectionEvent e)
			{
			}

			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.mnutmCameraOptionsAction();
			}
		});

		mnu_edit_item.setText("Edit");
		mnu_edit = new Menu(mnu_edit_item);
		mnu_edit_item.setMenu(mnu_edit);
		final MenuItem mnutm_edit_openzoneeditor = new MenuItem(mnu_edit, SWT.PUSH);
		mnutm_edit_openzoneeditor.setText("Zone Editor ..");
		mnutm_edit_openzoneeditor.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					final org.eclipse.swt.events.SelectionEvent e)
			{
			}

			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.mnutmEditOpenZoneEditorAction();
			}
		});

		final MenuItem mnutm_edit_options = new MenuItem(mnu_edit, SWT.PUSH);
		mnutm_edit_options.setText("Options ..");
		mnutm_edit_options.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					final org.eclipse.swt.events.SelectionEvent e)
			{
			}

			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.mnutmEditOptionsAction();
			}
		});

		mnu_experiment_item = new MenuItem(menuBar, SWT.CASCADE); // experiment
		mnu_experiment_item.setText("Experiment");
		mnu_experiment = new Menu(mnu_experiment_item);
		mnu_experiment_item.setMenu(mnu_experiment);
		final MenuItem mnutm_experiment_newexp = new MenuItem(mnu_experiment, SWT.PUSH);
		mnutm_experiment_loadexp = new MenuItem(mnu_experiment, 0);
		mnutm_experiment_loadexp.setText("Load Exp.");
		mnutm_experiment_loadexp.setEnabled(true);
		mnuitm_edt_exp = new MenuItem(mnu_experiment, SWT.PUSH);
		mnutm_experiment_exporttoexcel = new MenuItem(mnu_experiment, 0);
		mnutm_experiment_exporttoexcel.setText("Export to Excel");
		mnutm_experiment_exporttoexcel.setEnabled(true);
		mnutm_experiment_exporttoexcel.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.mnutmExperimentExportToExcelAction();
			}

			public void widgetDefaultSelected(
					final org.eclipse.swt.events.SelectionEvent e)
			{
			}
		});
		mnutm_experiment_newexp.setText("New Exp..");

		mnuitm_edt_exp.setText("Edit Exp.");
		mnuitm_edt_exp.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					final org.eclipse.swt.events.SelectionEvent e)
			{
			}

			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.mnuitmEditExpAction();
			}
		});
		mnutm_experiment_newexp.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					final org.eclipse.swt.events.SelectionEvent e)
			{
			}

			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.mnutmExperimentNewExpAction();
			}
		});

		mnutm_experiment_loadexp.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0)
			{
			}

			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.mnutmExperimentLoadexpAction(sShell);
			}
		});

		mnu_help_item = new MenuItem(menuBar, SWT.CASCADE); // help
		mnu_help_item.setText("Help");
		mnu_help = new Menu(mnu_help_item);
		mnu_help_item.setMenu(mnu_help);
		final MenuItem mnutm_help_about = new MenuItem(mnu_help, SWT.PUSH);
		mnutm_help_about.setText("About");
		mnutm_help_about.setEnabled(true);
		mnutm_help_about.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.mnutmHelpAboutAction();
			}

			public void widgetDefaultSelected(
					final org.eclipse.swt.events.SelectionEvent e)
			{
			}
		});
		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			@Override
			public void shellClosed(final org.eclipse.swt.events.ShellEvent e)
			{
				controller.closeProgram();
			}
		});
		clearForm();
	}

	/**
	 * Gets the Shell instance of the GUI.
	 * 
	 * @return Shell instance of the GUI
	 */
	public Shell getShell()
	{
		return sShell;
	}

	/**
	 * Gets the label that displays the status messages.
	 * 
	 * @return Label that displays status messages of the GUI
	 */
	public Label getStatusLabel()
	{
		return lbl_status;
	}

	@Override
	public void loadData(final String[] strArray)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setController(final ControllerUI controller)
	{
		super.setController(controller);
		this.controller = (CtrlMainGUI) super.controller;
	}

	public Frame getAwtVideoMain()
	{
		return awt_video_main;
	}

	public Frame getAwtVideoSec()
	{
		return awt_video_sec;
	}

	public void fillDataTable(final String[] names, final String[] values)
	{
		if (names != null)
		{
			for (int i = 0; i < names.length; i++)
			{
				final TableItem ti = new TableItem(tbl_data, 0);
				ti.setText(names[i]);
			}
		}

		if (values != null)
			for (int i = 0; i < values.length; i++)
				if (values[i] != null)
					tbl_data.getItem(i).setText(1, values[i]);
	}

}
