/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package ui;

import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.StyledText;
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

import control.ui.ControllerUI;
import control.ui.CtrlMainGUI;

/**
 * Main window of the Rat Monitoring Tool, it has links to all program portions.
 * 
 * @author Creative
 */
public class MainGUI extends BaseUI {
	private Frame		awtVideoMain;						// awt frame to
																// display main
																// screen data
	private Frame		awtVideoSec;							// awt frame to
																// display the
																// processed
																// image
	private Button		btnStartStream;
	private Button btnPause;
	private Button		btnStartTracking				= null;
	private Button		btnStopStream;
	private Button		btnStopTracking					= null;
	private Composite	cmpstMain						= null;
	private Composite	cmpstSecondary					= null;
	private Composite	cmpstStreaming;
	private Composite	cmpstTracking;

	private CtrlMainGUI	controller;
	private CoolBar		coolBar;
	private ExpandBar	expandBar;
	private Group		grpStats						= null;

	private Group		grpVideo						= null;
	private Group		grpGraphs;
	private Label		lblStatus						= null;

	private Menu		menuBar							= null;
	private MenuItem	mntmCameraSubMenu;
	private MenuItem	mntmVideoFile;
	// data
	private Menu		mnuEdit						= null;
	private MenuItem	mnuEditItem					= null;

	private Menu		mnuExperiment					= null;
	private MenuItem	mnuExperimentItem				= null;
	private Menu		mnuFile						= null;
	private MenuItem	mnuFileItem					= null;
	private Menu		mnuHelp						= null;

	private MenuItem	mnuHelpItem					= null;
	private MenuItem	mnuitmEditExp;
	private MenuItem	mnutmExperimentExporttoexcel	= null;
	private MenuItem	mnutmExperimentLoadexp		= null;
	private MenuItem	mnutmExperimentNewexp;
	private MenuItem	mnutmFileExit					= null;

	private Menu		mnuVideo;

	private MenuItem	mnuVideoSource					= null;

	private Shell		sShell;

	private Table		tblData						= null;
	private StyledText txtConsole;

	/**
	 * Creates GUI components, and links this Shell with the parent Shell.
	 */
	public MainGUI() {
		createSShell();
		super.sShell = this.sShell;
		grpGraphs = new Group(sShell, SWT.NONE);
		grpGraphs.setText("Graphs");
		grpGraphs.setBounds(10, 511, 969, 165);
		
		txtConsole = new StyledText(sShell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txtConsole.setBounds(10, 511, 662, 82);
		txtConsole.setEditable(false);
		txtConsole.setVisible(false); // TODO: reenable console when graphs are in a separate window
	}

	/**
	 * Enables/disables the start streaming button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnStartStreamingEnable(final boolean enable) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				btnStartStream.setEnabled(enable);
			}
		});
	}

	/**
	 * Enables/disables the start tracking button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnStartTrackingEnable(final boolean enable) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				btnStartTracking.setEnabled(enable);
			}
		});
	}

	/**
	 * Enables/disables the stop streaming button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnStopStreamingEnable(final boolean enable) {
		btnStopStream.setEnabled(enable);
	}
	
	public void btnPauseStreamingEnable(final boolean enable) {
		btnPause.setEnabled(enable);
	}

	/**
	 * Enables/disables the stop tracking button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnStopTrackingEnable(final boolean enable) {
		btnStopTracking.setEnabled(enable);
	}

	@Override
	public void clearForm() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				tblData.removeAll();
			}
		});
	}

	/**
	 * Closes the window.
	 */
	public void closeWindow() {
		if (getAwtVideoMain() != null)
			getAwtVideoMain().dispose();
		if (getAwtVideoSec() != null)
			getAwtVideoSec().dispose();
		cmpstSecondary.dispose();
		cmpstMain.dispose();
		coolBar.dispose();
		expandBar.dispose();
		sShell.dispose();
	}

	/**
	 * This method initializes cmpst_main.
	 */
	private void createCmpstMain() {
		cmpstMain = new Composite(grpVideo, SWT.EMBEDDED | SWT.BORDER);
		cmpstMain.setLayout(new GridLayout());
		cmpstMain.setLocation(new Point(5, 15));
		cmpstMain.setSize(new Point(640, 480));
		createMainAWTFrame();
	}

	/**
	 * This method initializes cmpst_secondary.
	 */
	private void createCmpstSecondary() {
		cmpstSecondary = new Composite(grpStats, SWT.EMBEDDED | SWT.BORDER);
		cmpstSecondary.setLayout(new GridLayout());
		cmpstSecondary.setBounds(new Rectangle(8, 20, 289, 214));
		createSecAWTFrame();
	}

	private void createCoolBar() {
		coolBar = new CoolBar(sShell, SWT.FLAT);

		// ////////////////////////////////////////////
		// Stream Control
		final CoolItem cItemStreamControl = new CoolItem(coolBar, SWT.NONE);
		final Composite cmpstStreamControl = new Composite(coolBar, SWT.NONE);
		cItemStreamControl.setControl(cmpstStreamControl);
		cmpstStreamControl.setLayout(new FillLayout(SWT.HORIZONTAL));
		final Button btnStartStreaming = new Button(cmpstStreamControl,
				SWT.NONE);
		btnStartStreaming.setText("strt");
		btnStartStreaming.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				controller.startStreamingAction();
			}
		});

		final Button btnStopStreaming = new Button(cmpstStreamControl, SWT.NONE);
		btnStopStreaming.setText("stp");
		btnStopStreaming.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				controller.stopStreamingAction();
			}
		});
		cItemStreamControl.setSize(100, 30);

		// ////////////////////////////////////////////
		// Tracking Control
		final CoolItem cItemTrackingControl = new CoolItem(coolBar, SWT.NONE);
		final Composite cmpstTrackingControl = new Composite(coolBar, SWT.NONE);
		cItemTrackingControl.setControl(cmpstTrackingControl);
		cmpstTrackingControl.setLayout(new FillLayout(SWT.HORIZONTAL));

		final Button btnStartTracking = new Button(cmpstTrackingControl,
				SWT.NONE);
		btnStartTracking.setText("track");
		btnStartTracking.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				controller.startTrackingAction();
			}
		});

		final Button btnStopTracking = new Button(cmpstTrackingControl,
				SWT.NONE);
		btnStopTracking.setText("stp");
		btnStopTracking.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				controller.stopTrackingAction();
			}
		});
		cItemTrackingControl.setSize(100, 30);

		// coolBar.setLocked(true);
	}

	private void createExpandBar() {
		final Group grpOptions = new Group(sShell, SWT.NONE);
		grpOptions.setText("Controls");
		grpOptions.setBounds(985, 5, 159, 671);

		expandBar = new ExpandBar(grpOptions, SWT.NONE);
		expandBar.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		expandBar.setBounds(10, 21, 139, 640);

		// ////////////////////////////////////////////
		// Stream Controls
		final ExpandItem xpndtmStreaming = new ExpandItem(expandBar, SWT.NONE);
		xpndtmStreaming.setExpanded(true);
		xpndtmStreaming.setText("Streaming");

		cmpstStreaming = new Composite(expandBar, SWT.NONE);
		xpndtmStreaming.setControl(cmpstStreaming);

		btnStartStream = new Button(cmpstStreaming, SWT.NONE);
		btnStartStream.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				controller.startStreamingAction();
			}
		});
		btnStartStream.setBounds(10, 10, 109, 25);
		btnStartStream.setText(ExternalStrings.get("MainGUI.StartStream")); //$NON-NLS-1$
		btnStartStream.setEnabled(false);

		btnStopStream = new Button(cmpstStreaming, SWT.NONE);
		btnStopStream.setBounds(10, 37, 109, 25);
		btnStopStream.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				controller.stopStreamingAction();
			}
		});
		btnStopStream.setText(ExternalStrings.get("MainGUI.StopStream"));
		
		btnPause = new Button(cmpstStreaming, SWT.NONE);
		btnPause.setLocation(10, 64);
		btnPause.setSize(109, 25);
		btnPause.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.pauseResumeAction();
			}
		});
		btnPause.setText("Pause/Resume");

		xpndtmStreaming.setHeight(xpndtmStreaming.getControl().computeSize(
				SWT.DEFAULT, SWT.DEFAULT).y + 10);

		// ////////////////////////////////////////////
		// Tracking Controls
		final ExpandItem xpndtmTracking = new ExpandItem(expandBar, SWT.NONE);
		xpndtmTracking.setExpanded(true);
		xpndtmTracking.setText(ExternalStrings.get("MainGUI.Tracking")); //$NON-NLS-1$

		cmpstTracking = new Composite(expandBar, SWT.NONE);
		xpndtmTracking.setControl(cmpstTracking);

		btnStartTracking = new Button(cmpstTracking, SWT.NONE);
		btnStartTracking.setBounds(new Rectangle(10, 10, 109, 25));
		btnStartTracking.setText(ExternalStrings.get("MainGUI.StartTracking")); //$NON-NLS-1$
		btnStartTracking
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						controller.startTrackingAction();
					}
				});
		btnStartTracking.setEnabled(false);

		btnStopTracking = new Button(cmpstTracking, SWT.NONE);
		btnStopTracking.setBounds(new Rectangle(10, 37, 109, 25));
		btnStopTracking.setText(ExternalStrings.get("MainGUI.StopTracking")); //$NON-NLS-1$
		btnStopTracking
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						controller.stopTrackingAction();
					}
				});

		xpndtmTracking.setHeight(xpndtmTracking.getControl().computeSize(
				SWT.DEFAULT, SWT.DEFAULT).y + 10);

	}

	/**
	 * This method initializes grp_stats.
	 */
	private void createGrpGraphs() {
		/*
		 * Button button = new Button(grpGraphs, SWT.NONE);
		 * button.addSelectionListener(new SelectionAdapter() {
		 * @Override public void widgetSelected(SelectionEvent e) {
		 * System.out.println("B1"); } }); button.setBounds(52, 50, 75, 25);
		 * button.setText("1"); Button button_1 = new Button(grpGraphs,
		 * SWT.NONE); button_1.setBounds(152, 50, 75, 25);
		 * button_1.setText("2"); button_1.addSelectionListener(new
		 * SelectionAdapter() {
		 * @Override public void widgetSelected(SelectionEvent e) {
		 * System.out.println("B2"); } });
		 */
	}

	/**
	 * This method initializes grp_stats.
	 */
	private void createGrpStats() {
		grpStats = new Group(sShell, SWT.NONE);
		grpStats.setLayout(null);
		grpStats.setText("Analysis:");
		createCmpstSecondary();
		grpStats.setBounds(new Rectangle(678, 5, 301, 500));

		tblData = new Table(grpStats, SWT.BORDER);
		tblData.setHeaderVisible(true);
		tblData.setLinesVisible(true);
		tblData.setBounds(new Rectangle(9, 243, 284, 251));
		final TableColumn tableColumn = new TableColumn(tblData, SWT.NONE);
		tableColumn.setWidth(140);
		tableColumn.setText("Name");
		final TableColumn tableColumn1 = new TableColumn(tblData, SWT.NONE);
		tableColumn1.setWidth(140);
		tableColumn1.setText("Value");

	}

	/**
	 * This method initializes grp_video.
	 */
	private void createGrpVideo() {
		grpVideo = new Group(sShell, SWT.NONE);
		grpVideo.setLayout(null);
		grpVideo.setText("Video:");

		grpVideo.setBounds(new Rectangle(10, 5, 665, 500));
		createCmpstMain();
	}

	/**
	 * Creates the Main screen's AWT frame.
	 */
	private void createMainAWTFrame() {
		awtVideoMain = SWT_AWT.new_Frame(cmpstMain);
		getAwtVideoMain().setVisible(true);
		getAwtVideoMain().setSize(cmpstMain.getSize().x,
				cmpstMain.getSize().y);
	}

	/**
	 * Creates the Secondary screen's AWT frame.
	 */
	private void createSecAWTFrame() {
		awtVideoSec = SWT_AWT.new_Frame(cmpstSecondary);
		getAwtVideoSec().setVisible(true);
		getAwtVideoSec().setSize(cmpstSecondary.getSize().x,
				cmpstSecondary.getSize().y);
	}

	/**
	 * This method initializes sShell.
	 */
	private void createSShell() {
		sShell = new Shell(/*SWT.APPLICATION_MODAL | */SWT.DIALOG_TRIM);
		sShell.setText("Behavioral Monitoring Tool");
		createExpandBar();
		createCoolBar();
		createGrpVideo();
		createGrpStats();
		createGrpGraphs();
		sShell.setSize(new Point(1160, 733));
		sShell.setLayout(null);
		lblStatus = new Label(getShell(), SWT.NONE);
		lblStatus.setBounds(new Rectangle(10, 672, 656, 20));
		lblStatus.setText("");

		menuBar = new Menu(sShell, SWT.BAR);
		sShell.setMenuBar(menuBar);

		mnuFileItem = new MenuItem(menuBar, SWT.CASCADE); // file
		mnuEditItem = new MenuItem(menuBar, SWT.CASCADE); // edit
		mnuFileItem.setText(ExternalStrings.get("MainGUI.Menu.File")); //$NON-NLS-1$
		mnuFile = new Menu(mnuFileItem);
		mnuFileItem.setMenu(mnuFile);
		mnutmFileExit = new MenuItem(mnuFile, 0);
		mnutmFileExit.setText(ExternalStrings.get("MainGUI.Menu.File.Exit")); //$NON-NLS-1$
		mnutmFileExit
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					@Override
					public void widgetDefaultSelected(final SelectionEvent arg0) {
					}

					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						controller.closeProgram();
					}
				});

		mnuVideoSource = new MenuItem(menuBar, SWT.CASCADE); // camera
		mnuVideoSource.setText(ExternalStrings.get("MainGUI.Menu.Video")); //$NON-NLS-1$
		mnuVideo = new Menu(mnuVideoSource);
		mnuVideoSource.setMenu(mnuVideo);

		final MenuItem mntmSource = new MenuItem(mnuVideo, SWT.CASCADE);
		mntmSource.setText(ExternalStrings.get("MainGUI.Menu.Video.Source")); //$NON-NLS-1$

		final Menu menu = new Menu(mntmSource);
		mntmSource.setMenu(menu);
		mntmVideoFile = new MenuItem(menu, SWT.RADIO);
		mntmVideoFile.setText(ExternalStrings
				.get("MainGUI.Menu.Video.Source.VideoFile")); //$NON-NLS-1$
		mntmCameraSubMenu = new MenuItem(menu, SWT.RADIO);
		mntmCameraSubMenu.setText(ExternalStrings
				.get("MainGUI.Menu.Video.Source.Camera")); //$NON-NLS-1$
		mntmVideoFile.setSelection(true);

		final MenuItem mntmVidOptions = new MenuItem(mnuVideo, SWT.NONE);
		mntmVidOptions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				controller.mnutmCameraOptionsAction();
			}
		});
		mntmVidOptions.setText(ExternalStrings
				.get("MainGUI.Menu.Video.Source.Options")); //$NON-NLS-1$

		mnuEditItem.setText(ExternalStrings.get("MainGUI.Menu.Edit")); //$NON-NLS-1$
		mnuEdit = new Menu(mnuEditItem);
		mnuEditItem.setMenu(mnuEdit);
		mnuEditItem.setEnabled(false);

		final MenuItem mnutmEditOptions = new MenuItem(mnuEdit, SWT.PUSH);
		mnutmEditOptions.setText(ExternalStrings.get("MainGUI.Menu.Options")); //$NON-NLS-1$
		mnutmEditOptions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				controller.mnutmEditOptionsAction();
			}
		});

		sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			@Override
			public void shellClosed(final org.eclipse.swt.events.ShellEvent e) {
				controller.closeProgram();
			}
		});

		mnuExperimentItem = new MenuItem(menuBar, SWT.CASCADE); // experiment
		mnuExperimentItem.setText(ExternalStrings
				.get("MainGUI.Menu.Experiment")); //$NON-NLS-1$
		mnuExperiment = new Menu(mnuExperimentItem);
		mnuExperimentItem.setMenu(mnuExperiment);
		mnutmExperimentNewexp = new MenuItem(mnuExperiment, SWT.PUSH);
		mnutmExperimentLoadexp = new MenuItem(mnuExperiment, 0);
		mnutmExperimentLoadexp.setText(ExternalStrings
				.get("MainGUI.Menu.Exp.Load")); //$NON-NLS-1$
		mnutmExperimentLoadexp.setEnabled(true);
		mnuitmEditExp = new MenuItem(mnuExperiment, SWT.PUSH);
		mnuitmEditExp.setEnabled(false);
		mnutmExperimentExporttoexcel = new MenuItem(mnuExperiment, 0);
		mnutmExperimentExporttoexcel.setText(ExternalStrings
				.get("MainGUI.Menu.Exp.ExporttoExcel")); //$NON-NLS-1$
		mnutmExperimentExporttoexcel.setEnabled(false);
		mnutmExperimentExporttoexcel
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					@Override
					public void widgetDefaultSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
					}

					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						controller.mnutmExperimentExportToExcelAction();
					}
				});
		mnutmExperimentNewexp.setText(ExternalStrings
				.get("MainGUI.Menu.Exp.New")); //$NON-NLS-1$

		mnuitmEditExp.setText(ExternalStrings.get("MainGUI.Menu.Exp.Edit")); //$NON-NLS-1$
		mnuitmEditExp
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					@Override
					public void widgetDefaultSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
					}

					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						controller.mnuitmEditExpAction();
					}
				});
		mnutmExperimentNewexp
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					@Override
					public void widgetDefaultSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
					}

					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						controller.mnutmExperimentNewExpAction();
					}
				});

		mnutmExperimentLoadexp
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					@Override
					public void widgetDefaultSelected(final SelectionEvent arg0) {
					}

					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						controller.mnutmExperimentLoadexpAction(sShell);
					}
				});

		mnuHelpItem = new MenuItem(menuBar, SWT.CASCADE); // help
		mnuHelpItem.setText(ExternalStrings.get("MainGUI.Menu.Help")); //$NON-NLS-1$
		mnuHelp = new Menu(mnuHelpItem);
		mnuHelpItem.setMenu(mnuHelp);
		final MenuItem mnutmHelpAbout = new MenuItem(mnuHelp, SWT.PUSH);
		mnutmHelpAbout.setText(ExternalStrings.get("MainGUI.Menu.About")); //$NON-NLS-1$
		mnutmHelpAbout.setEnabled(true);
		mnutmHelpAbout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(
					final org.eclipse.swt.events.SelectionEvent e) {
				controller.mnutmHelpAboutAction();
			}
		});

		clearForm();
	}

	/**
	 * Fill the info/statistics table.
	 * 
	 * @param names
	 *            names of fields in the table (left column)
	 * @param values
	 *            values to fill the table with (right column)
	 */
	public void fillDataTable(final String[] names, final String[] values) {
		if (names != null)
			for (int i = 0; i < names.length; i++) {
				final TableItem ti = new TableItem(tblData, 0);
				ti.setText(names[i]);
			}

		if (values != null)
			for (int i = 0; i < values.length; i++)
				if (values[i] != null)
					tblData.getItem(i).setText(1, values[i]);
	}

	/**
	 * Gets the Main screen's AWT frame.
	 * 
	 * @return AWT frame of the main screen
	 */
	public Frame getAwtVideoMain() {
		return awtVideoMain;
	}

	/**
	 * Gets the Secondary screen's AWT frame.
	 * 
	 * @return AWT frame of the Secondary screen
	 */
	public Frame getAwtVideoSec() {
		return awtVideoSec;
	}

	public String getSelectedInputMode() {
		if (mntmCameraSubMenu.getSelection())
			return "CAM";
		else if (mntmVideoFile.getSelection())
			return "VIDEOFILE";
		return null;
	}

	/**
	 * Gets the Shell instance of the GUI.
	 * 
	 * @return Shell instance of the GUI
	 */
	public Shell getShell() {
		return sShell;
	}

	/**
	 * Gets the label that displays the status messages.
	 * 
	 * @return Label that displays status messages of the GUI
	 */
	public Label getStatusLabel() {
		return lblStatus;
	}
	
	public StyledText getConsoleText() {
		return txtConsole;
	}

	@Override
	public void loadData(final String[] strArray) {

	}

	/**
	 * Loads GUI instances for the available video filters.
	 * 
	 * @param filtersGUI
	 *            ArrayList of available filters' gui
	 */
	public void loadPluggedGUI(final PluggedGUI<?>[] pGUIs) {
		for (final PluggedGUI<?> pgui : pGUIs)
			pgui.initialize(sShell, expandBar, menuBar, coolBar, grpGraphs);

		coolBar.setBounds(5, 5, 300, 30);
		coolBar.layout();
		coolBar.setLocked(true);
		coolBar.setVisible(false);
	}

	public void setActive() {
		sShell.setActive();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setController(final ControllerUI controller) {
		super.setController(controller);
		this.controller = (CtrlMainGUI) super.controller;
	}

	public void setExperimantLoaded(final boolean loaded) {
		mnuitmEditExp.setEnabled(loaded);
		mnutmExperimentExporttoexcel.setEnabled(loaded);
		mnuEditItem.setEnabled(loaded);
	}
}
