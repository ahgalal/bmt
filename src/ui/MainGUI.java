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

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import utils.video.filters.PluggedGUI;
import control.ui.ControllerUI;
import control.ui.CtrlMainGUI;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.core.databinding.beans.PojoObservables;

/**
 * Main window of the Rat Monitoring Tool, it has links to all program portions.
 * 
 * @author Creative
 */
public class MainGUI extends BaseUI
{
	private DataBindingContext m_bindingContext;

	private CtrlMainGUI controller;
	private Shell sShell;
	private Group grp_video = null;
	private Group grp_options = null;
	private Group grp_stats = null;
	private Composite cmpst_main = null;
	private Frame awt_video_main; // awt frame to display main screen data
	private Menu menuBar = null;
	private MenuItem mnu_file_item = null;
	private MenuItem mnu_edit_item = null;
	private Composite cmpstTracking;

	private MenuItem mnuVideoSource = null;
	private MenuItem mnu_help_item = null;
	private Menu mnu_file = null;
	private Menu mnu_edit = null;

	private Menu mnu_camera = null;
	private Menu mnuVideo;
	private Menu mnu_help = null;
	private MenuItem mnutm_file_exit = null;

	private MenuItem mnutm_camera_start = null;
	private Button btnStartTracking = null;
	private Composite cmpst_secondary = null;
	private Frame awt_video_sec; // awt frame to display the processed image
	private MenuItem mntmCameraSubMenu;
	private MenuItem mntmVideoFile;
	// data
	private Button btn_setbg = null;
	private Button btnStopTracking = null;
	private Label lbl_status = null;
	private Table tbl_data = null;
	private CoolBar coolBar;
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

	private void createCoolBar()
	{
		coolBar = new CoolBar(sShell, SWT.FLAT);

		//////////////////////////////////////////////
		// Stream Control
		CoolItem cItemStreamControl = new CoolItem(coolBar, SWT.NONE);
		Composite cmpstStreamControl = new Composite(coolBar, SWT.NONE);
		cItemStreamControl.setControl(cmpstStreamControl);
		cmpstStreamControl.setLayout(new FillLayout(SWT.HORIZONTAL));
		Button btnStartStreaming = new Button(cmpstStreamControl, SWT.NONE);
		btnStartStreaming.setText("strt");
		btnStartStreaming.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				controller.startStreamingAction();
			}
		});

		Button btnStopStreaming = new Button(cmpstStreamControl, SWT.NONE);
		btnStopStreaming.setText("stp");
		btnStopStreaming.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				controller.stopStreamingAction();
			}
		});
		cItemStreamControl.setSize(100, 30);
		
		//////////////////////////////////////////////
		// Tracking Control
		CoolItem cItemTrackingControl = new CoolItem(coolBar, SWT.NONE);
		Composite cmpstTrackingControl = new Composite(coolBar, SWT.NONE);
		cItemTrackingControl.setControl(cmpstTrackingControl);
		cmpstTrackingControl.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button btnStartTracking = new Button(cmpstTrackingControl, SWT.NONE);
		btnStartTracking.setText("track");
		btnStartTracking.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				controller.startTrackingAction();
			}
		});

		Button btnStopTracking = new Button(cmpstTrackingControl, SWT.NONE);
		btnStopTracking.setText("stp");
		btnStopTracking.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				controller.stopTrackingAction();
			}
		});
		cItemTrackingControl.setSize(100, 30);
		
		
		coolBar.setBounds(5, 5, 300, 30);
		coolBar.setLocked(true);
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
		grp_stats.setBounds(new Rectangle(678, 61, 301, 563));

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
	private ExpandBar expandBar;
	private Composite cmpstStreaming;
	private Button btnStartStream;
	private Button btnStopStream;
	private void createExpandBar()
	{
		Group grpOptions = new Group(sShell, SWT.NONE);
		grpOptions.setText("Controls");
		grpOptions.setBounds(985, 5, 159, 563);

		expandBar = new ExpandBar(grpOptions, SWT.NONE);
		expandBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		expandBar.setBounds(10, 21, 139, 532);

		//////////////////////////////////////////////
		// Stream Controls
		ExpandItem xpndtmStreaming = new ExpandItem(expandBar, SWT.NONE);
		xpndtmStreaming.setExpanded(true);
		xpndtmStreaming.setText("Streaming");

		cmpstStreaming = new Composite(expandBar, SWT.NONE);
		xpndtmStreaming.setControl(cmpstStreaming);

		btnStartStream = new Button(cmpstStreaming, SWT.NONE);
		btnStartStream.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.startStreamingAction();
			}
		});
		btnStartStream.setBounds(10, 10, 109, 25);
		btnStartStream.setText("Start Stream");

		btnStopStream = new Button(cmpstStreaming, SWT.NONE);
		btnStopStream.setBounds(10, 37, 109, 25);
		btnStopStream.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.stopStreamingAction();
			}
		});
		btnStopStream.setText("Stop Stream");

		xpndtmStreaming.setHeight(xpndtmStreaming.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y+10);


		//////////////////////////////////////////////
		// Tracking Controls
		ExpandItem xpndtmTracking = new ExpandItem(expandBar, SWT.NONE);
		xpndtmTracking.setExpanded(true);
		xpndtmTracking.setText("Tracking");

		cmpstTracking = new Composite(expandBar, SWT.NONE);
		xpndtmTracking.setControl(cmpstTracking);

		btnStartTracking = new Button(cmpstTracking, SWT.NONE);
		btnStartTracking.setBounds(new Rectangle(10, 35, 109, 25));
		btnStartTracking.setText("Start Tracking");
		btnStartTracking.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.startTrackingAction();
			}
		});

		btnStopTracking = new Button(cmpstTracking, SWT.NONE);
		btnStopTracking.setBounds(new Rectangle(10, 61, 109, 25));
		btnStopTracking.setText("Stop Tracking");
		btnStopTracking.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.stopTrackingAction();
			}
		});

		btn_setbg = new Button(cmpstTracking, SWT.NONE);
		btn_setbg.setBounds(new Rectangle(10, 10, 109, 25));
		btn_setbg.setText("Set Background");
		btn_setbg.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.btnSetbgAction();
			}
		});

		xpndtmTracking.setHeight(xpndtmTracking.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y+10);

	}

	/**
	 * This method initializes grp_video.
	 */
	private void createGrpVideo()
	{
		grp_video = new Group(sShell, SWT.NONE);
		grp_video.setLayout(null);
		grp_video.setText("Video:");

		grp_video.setBounds(new Rectangle(10, 61, 665, 510));
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

	private boolean radioSelection;
	/**
	 * This method initializes sShell.
	 */
	private void createSShell()
	{
		sShell = new Shell(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		sShell.setText("Behavioral Monitoring Tool");
		createExpandBar();
		createCoolBar();
		createGrpVideo();
		createGrpOptions();
		createGrpStats();
		sShell.setSize(new Point(1160, 650));
		sShell.setLayout(null);
		lbl_status = new Label(getShell(), SWT.NONE);
		lbl_status.setBounds(new Rectangle(10, 574, 656, 20));
		lbl_status.setText("");

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

		mnuVideoSource = new MenuItem(menuBar, SWT.CASCADE); // camera
		mnuVideoSource.setText("Video");
		mnuVideo = new Menu(mnuVideoSource);
		mnuVideoSource.setMenu(mnuVideo);

		MenuItem mntmSource = new MenuItem(mnuVideo, SWT.CASCADE);
		mntmSource.setText("Source");

		Menu menu = new Menu(mntmSource);
		mntmSource.setMenu(menu);
		class MntmSourceRadioHandler extends SelectionAdapter
		{
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(radioSelection==true)
				{
					if(((MenuItem)e.widget).getText().equals("Video File .."))
						controller.setVideoFileMode();
					radioSelection=false;
				}
				else
					radioSelection=true;
			}
		}
		
		mntmVideoFile= new MenuItem(menu, SWT.RADIO);
		mntmVideoFile.setText("Video File ..");
		mntmCameraSubMenu = new MenuItem(menu, SWT.RADIO);
		mntmCameraSubMenu.setText("Camera");
		mntmVideoFile.setSelection(true);
		
		mntmVideoFile.addSelectionListener(new MntmSourceRadioHandler());
		mntmCameraSubMenu.addSelectionListener(new MntmSourceRadioHandler());


		MenuItem mntmVidOptions = new MenuItem(mnuVideo, SWT.NONE);
		mntmVidOptions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.mnutmCameraOptionsAction();
			}
		});
		mntmVidOptions.setText("Options ..");

		mnu_edit_item.setText("Edit");
		mnu_edit = new Menu(mnu_edit_item);
		mnu_edit_item.setMenu(mnu_edit);

		final MenuItem mnutm_edit_options = new MenuItem(mnu_edit, SWT.PUSH);
		mnutm_edit_options.setText("Options ..");
		mnutm_edit_options.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				controller.mnutmEditOptionsAction();
			}
		});

		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			@Override
			public void shellClosed(final org.eclipse.swt.events.ShellEvent e)
			{
				controller.closeProgram();
			}
		});

		mnu_help_item = new MenuItem(menuBar, SWT.CASCADE); // help
		mnu_help_item.setText("Help");
		mnu_help = new Menu(mnu_help_item);
		mnu_help_item.setMenu(mnu_help);
		final MenuItem mnutm_help_about = new MenuItem(mnu_help, SWT.PUSH);
		mnutm_help_about.setText("About");
		mnutm_help_about.setEnabled(true);
		mnutm_help_about.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.mnutmHelpAboutAction();
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
		btnStartTracking.setEnabled(enable);
	}

	/**
	 * Enables/disables the stop tracking button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnStopTrackingEnable(final boolean enable)
	{
		btnStopTracking.setEnabled(enable);
	}

	/**
	 * Enables/disables the start streaming button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnStartStreamingEnable(final boolean enable)
	{
		btnStopStream.setEnabled(enable);
	}

	/**
	 * Enables/disables the stop streaming button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnStopStreamingEnable(final boolean enable)
	{
		btnStopStream.setEnabled(enable);
	}

	/**
	 * Loads GUI instances for the available video filters.
	 * 
	 * @param filtersGUI
	 *            ArrayList of available filters' gui
	 */
	public void loadPluggedGUI(final PluggedGUI[] pGUIs)
	{
		for(PluggedGUI pgui:pGUIs)
			pgui.initialize(sShell, expandBar,menuBar,coolBar);

		coolBar.layout();
		//coolBar.setLocked(true);

	}

	public String getSelectedInputMode()
	{
		if(mntmCameraSubMenu.getSelection())
			return "CAM";
		else if(mntmVideoFile.getSelection())
			return "VIDEOFILE";
		return null;
	}
}
