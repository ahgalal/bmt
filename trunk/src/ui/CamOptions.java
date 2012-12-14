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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import control.ui.ControllerUI;
import control.ui.CtrlCamOptions;

/**
 * @author ShaQ Displays the Camera options and enable user to change them
 */
public class CamOptions extends BaseUI {

	private Button			btnCancel			= null;

	private Button			btnJmyronSettings	= null;
	private Button			btnOK				= null;
	private Combo			cmboCamLib		= null;
	private Combo			cmboCamNum		= null;
	private CtrlCamOptions	controller;				// @jve:decl-index=0:
	private Group			grpFormat			= null;
	private Group			grpFrameRate		= null;
	private Label			lblCamLib			= null;
	private Label			lblCamNum			= null;
	private Label			lblHeight			= null;
	private Label			lblWidth			= null;
	private Button			radbtn15fps		= null;
	private Button			radbtn30fps		= null;
	private Button			radbtnRGB			= null;
	private Button			radbtnYUV			= null;
	private Shell			sShell				= null; // @jve:decl-index=0:visual-constraint="12,18"
	private Text			txtHeight			= null;
	private Text			txtWidth			= null;

	/**
	 * Initializes the GUI components.
	 */
	public CamOptions() {
		createShell();
		super.sShell = sShell;
	}

	@Override
	public void clearForm() {
		txtHeight.setText("");
		txtWidth.setText("");
	}

	/**
	 * This method initializes cmbo_cam_lib.
	 */
	private void createCmboCamLib() {
		cmboCamLib = new Combo(sShell, SWT.READ_ONLY);
		cmboCamLib.setBounds(new Rectangle(184, 14, 116, 23));

		cmboCamLib.add("AGCamLib");
		cmboCamLib.add("JMF");
		cmboCamLib.add("OpenCV");
		cmboCamLib.add("JMyron");
		cmboCamLib.setText("AGCamLib");

		cmboCamLib.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				final String library = cmboCamLib.getText();
				if (library.equals("OpenCV")) {
					radbtnRGB.setEnabled(false);
					radbtnYUV.setEnabled(false);
					btnJmyronSettings.setVisible(false);
					cmboCamNum.setEnabled(true);
				} else if (library.equals("JMF")) {
					radbtnRGB.setEnabled(true);
					radbtnYUV.setEnabled(true);
					btnJmyronSettings.setVisible(false);
					cmboCamNum.setEnabled(true);
				} else if (library.equals("JMyron")) {
					radbtnRGB.setEnabled(false);
					radbtnYUV.setEnabled(false);
					btnJmyronSettings.setVisible(true);
					cmboCamNum.setEnabled(false);
				} else if (library.equals("AGCamLib")) {
					radbtnRGB.setEnabled(false);
					radbtnYUV.setEnabled(false);
					btnJmyronSettings.setVisible(false);
					cmboCamNum.setEnabled(true);
				}
			}
		});
	}

	/**
	 * This method initializes cmbo_cam_num.
	 */
	private void createCmboCamNum() {
		cmboCamNum = new Combo(sShell, SWT.READ_ONLY);
		cmboCamNum.setBounds(new Rectangle(184, 46, 116, 23));
		cmboCamNum.add("0");
		cmboCamNum.add("1");
		cmboCamNum.add("2");
		cmboCamNum.setText("0");
	}

	/**
	 * This method initializes grp_format.
	 */
	private void createGrpFormat() {
		grpFormat = new Group(sShell, SWT.NONE);
		grpFormat.setLayout(null);
		grpFormat.setText("Format");
		grpFormat.setBounds(new Rectangle(159, 121, 146, 49));
		radbtnRGB = new Button(grpFormat, SWT.RADIO);
		radbtnRGB.setBounds(new Rectangle(8, 20, 43, 16));
		radbtnRGB.setText("RGB");
		radbtnYUV = new Button(grpFormat, SWT.RADIO);
		radbtnYUV.setBounds(new Rectangle(74, 21, 43, 16));
		radbtnYUV.setText("YUV");
		radbtnYUV.setSelection(true);
	}

	/**
	 * This method initializes grp_frame_rate.
	 */
	private void createGrpFrameRate() {
		grpFrameRate = new Group(sShell, SWT.NONE);
		grpFrameRate.setLayout(null);
		grpFrameRate.setBounds(new Rectangle(15, 120, 135, 48));
		grpFrameRate.setText("Frame Rate");
		radbtn15fps = new Button(grpFrameRate, SWT.RADIO);
		radbtn15fps.setBounds(new Rectangle(8, 20, 52, 16));
		radbtn15fps.setText("15 fps");

		radbtn30fps = new Button(grpFrameRate, SWT.RADIO);
		radbtn30fps.setBounds(new Rectangle(86, 20, 52, 16));
		radbtn30fps.setText("30 fps");
		radbtn30fps.setSelection(true);

	}

	/**
	 * This method initializes sShell.
	 */
	private void createShell() {
		// pm=PManager.getDefault();
		sShell = new Shell(SWT.DIALOG_TRIM | SWT.SYSTEM_MODAL);
		sShell.setText("Camera Options");
		sShell.setLayout(null);
		sShell.setSize(new Point(318, 239));
		sShell.setMaximized(false);
		btnOK = new Button(sShell, SWT.NONE);
		btnOK.setBounds(new Rectangle(231, 180, 73, 25));
		btnOK.setText("OK");
		btnOK.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			@Override
			public void mouseDown(final org.eclipse.swt.events.MouseEvent e) {
				/**
				 * this piece of code will be executed in case of 1 or 2: 1. we
				 * haven't chosen JMyron 2. we have chosen JMyron, but haven't
				 * set it's advanced settings
				 */
				controller.setVars(new String[] { txtWidth.getText(),
						txtHeight.getText(),
						radbtn15fps.getSelection() ? "15" : "30",
						cmboCamLib.getText(),
						radbtnRGB.getSelection() ? "RGB" : "YUV",
						cmboCamNum.getText() });
				controller.btnOkAction();
			}
		});
		txtWidth = new Text(sShell, SWT.BORDER);
		txtWidth.setBounds(new Rectangle(61, 80, 76, 21));

		txtWidth.setText("640");
		txtHeight = new Text(sShell, SWT.BORDER);
		txtHeight.setBounds(new Rectangle(208, 80, 76, 21));

		txtHeight.setText("480");
		lblWidth = new Label(sShell, SWT.NONE);
		lblWidth.setBounds(new Rectangle(15, 80, 35, 15));
		lblWidth.setText("Width:");
		lblHeight = new Label(sShell, SWT.NONE);
		lblHeight.setBounds(new Rectangle(163, 80, 39, 15));
		lblHeight.setText("Height:");
		createGrpFrameRate();
		createGrpFormat();
		lblCamNum = new Label(sShell, SWT.NONE);
		lblCamNum.setBounds(new Rectangle(15, 44, 83, 21));
		lblCamNum.setText("Cam. Number:");
		createCmboCamNum();
		createCmboCamLib();
		lblCamLib = new Label(sShell, SWT.NONE);
		lblCamLib.setBounds(new Rectangle(15, 17, 112, 17));
		lblCamLib.setText("Cam. Video Library:");
		btnJmyronSettings = new Button(sShell, SWT.NONE);
		btnJmyronSettings.setText("More ..");
		btnJmyronSettings.setSize(new Point(73, 25));
		btnJmyronSettings.setVisible(false);
		btnJmyronSettings.setLocation(new Point(155, 180));
		btnCancel = new Button(sShell, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.setSize(new Point(73, 25));
		btnCancel.setLocation(new Point(10, 181));
		btnCancel
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						sShell.setVisible(false);
					}
				});
		btnJmyronSettings
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						/**
						 * in case of JMyron ONLY (button only appears in case
						 * of JMyron) we initialize the video processor using
						 * the settings in the GUI .... then display the
						 * advanced settings of JMyron. notice the varible
						 * "lib_is_already_created", it prevents the OK button
						 * of the GUI from unloading the VP and loosing all it's
						 * data(including the advanced settings!)
						 */
						controller.setVars(new String[] { txtWidth.getText(),
								txtHeight.getText(),
								radbtn15fps.getSelection() ? "15" : "30",
								cmboCamLib.getText(),
								radbtnRGB.getSelection() ? "RGB" : "YUV",
								cmboCamNum.getText() });
						controller.btnJmyronSettingsAction();
					}
				});
	}

	@Override
	public void loadData(final String[] strArray) {
		if (strArray.length == 6) {
			final String library = strArray[0];
			final int camNum = Integer.parseInt(strArray[1]);
			final int width = Integer.parseInt(strArray[2]);
			final int height = Integer.parseInt(strArray[3]);
			final int frameRate = Integer.parseInt(strArray[4]);
			final String format = strArray[5];

			try {

				cmboCamLib.setText(library);
				cmboCamNum.setText(String.valueOf(camNum));
				txtWidth.setText(String.valueOf(width));
				txtHeight.setText(String.valueOf(height));
				switch (frameRate) {
					case 15:
						radbtn15fps.setSelection(true);
						break;
					case 30:
						radbtn30fps.setSelection(true);
						break;
				}
				if (format.equals("RGB"))
					radbtnRGB.setSelection(true);
				else if (format.equals("YUV"))
					radbtnYUV.setSelection(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		} else
			System.out.print("Error in number of argument of GUI!\n");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setController(final ControllerUI controller) {
		super.setController(controller);
		this.controller = (CtrlCamOptions) controller;
	}

	public void setVidLibs(final String[] libs) {
		cmboCamLib.removeAll();
		for (final String lib : libs)
			cmboCamLib.add(lib);
		cmboCamLib.setText(libs[0]);
	}
}