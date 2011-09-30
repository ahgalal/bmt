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

import java.awt.Frame;
import java.util.ArrayList;

import modules.experiment.ExperimentModuleGUI;
import modules.rearing.RearingModuleGUI;
import modules.zones.ZonesModuleGUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import utils.video.filters.rearingdetection.RearingDetectorGUI;
import utils.video.filters.recorder.VideoRecorderGUI;
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

	private MenuItem mnu_camera_item = null;
	private MenuItem mnu_help_item = null;
	private Menu mnu_file = null;
	private Menu mnu_edit = null;

	private Menu mnu_camera = null;
	private Menu mnu_help = null;
	private MenuItem mnutm_file_exit = null;

	private MenuItem mnutm_camera_start = null;
	private Button btn_start_tracking = null;
	private Composite cmpst_secondary = null;
	private Frame awt_video_sec; // awt frame to display the processed image
	// data
	private Button btn_setbg = null;
	private Button btn_stop_tracking = null;
	private Label lbl_status = null;
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
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run()
			{
				tbl_data.removeAll();
			}
		});
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

	/**
	 * Creates the Main screen's AWT frame.
	 */
	private void createMainAWTFrame()
	{
		awt_video_main = SWT_AWT.new_Frame(cmpst_main);
		getAwtVideoMain().setVisible(true);
		getAwtVideoMain().setSize(cmpst_main.getSize().x, cmpst_main.getSize().y);
	}

	/**
	 * Creates the Secondary screen's AWT frame.
	 */
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

	/**
	 * Gets the Main screen's AWT frame.
	 * 
	 * @return AWT frame of the main screen
	 */
	public Frame getAwtVideoMain()
	{
		return awt_video_main;
	}

	/**
	 * Gets the Secondary screen's AWT frame.
	 * 
	 * @return AWT frame of the Secondary screen
	 */
	public Frame getAwtVideoSec()
	{
		return awt_video_sec;
	}

	/**
	 * Fill the info/statistics table.
	 * 
	 * @param names
	 *            names of fields in the table (left column)
	 * @param values
	 *            values to fill the table with (right column)
	 */
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

	/**
	 * Enables/disables the start tracking button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnStartTrackingEnable(final boolean enable)
	{
		btn_start_tracking.setEnabled(enable);
	}

	/**
	 * Enables/disables the start tracking button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnStopTrackingEnable(final boolean enable)
	{
		btn_stop_tracking.setEnabled(enable);
	}

	public VideoRecorderGUI vid_rec_gui;
	public RearingDetectorGUI rearing_det_gui;

	/**
	 * Loads GUI instances for the available video filters.
	 * 
	 * @param filters
	 *            ArrayList of available filters
	 */
	public void loadFiltersGUI(final ArrayList<String> filters)
	{
		if (filters.contains("Recorder"))
			vid_rec_gui = new VideoRecorderGUI(sShell, grp_options);
		if (filters.contains("RearingDetector"))
			rearing_det_gui = new RearingDetectorGUI(grp_stats);

	}

	public RearingModuleGUI rearing_module_gui;
	public ExperimentModuleGUI experiment_module_gui;
	public ZonesModuleGUI zones_module_gui;

	/**
	 * Loads the GUI instances for the available modules.
	 * 
	 * @param modules
	 *            ArrayList of available modules
	 */
	public void loadModulesGUI(final ArrayList<String> modules)
	{
		if (modules.contains("Rearing Module"))
			rearing_module_gui = new RearingModuleGUI(grp_stats);
		if (modules.contains("Experiment Module"))
			experiment_module_gui = new ExperimentModuleGUI(menuBar, sShell);
		if (modules.contains("Zones Module"))
			zones_module_gui = new ZonesModuleGUI(mnu_edit);

	}

}
