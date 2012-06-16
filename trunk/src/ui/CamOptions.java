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

    /**
     * Initializes the GUI components.
     */
    public CamOptions() {
	createShell();
	super.sShell = sShell;
    }

    private Shell sShell = null; // @jve:decl-index=0:visual-constraint="12,18"
    private Button btn_OK = null;
    private Text txt_width = null;
    private Text txt_height = null;
    private Label lbl_width = null;
    private Label lbl_height = null;
    private Group grp_frame_rate = null;
    private Button radbtn_15fps = null;
    private Button radbtn_30fps = null;
    private Group grp_format = null;
    private Button radbtn_rgb = null;
    private Button radbtn_yuv = null;
    private Label lbl_cam_num = null;
    private Combo cmbo_cam_num = null;
    private Combo cmbo_cam_lib = null;
    private Label lbl_cam_lib = null;
    private Button btn_jmyron_settings = null;
    private CtrlCamOptions controller; // @jve:decl-index=0:
    private Button btn_cancel = null;

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
	btn_OK = new Button(sShell, SWT.NONE);
	btn_OK.setBounds(new Rectangle(231, 180, 73, 25));
	btn_OK.setText("OK");
	btn_OK.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
	    @Override
	    public void mouseDown(final org.eclipse.swt.events.MouseEvent e) {
		/**
		 * this piece of code will be executed in case of 1 or 2: 1. we
		 * haven't chosen JMyron 2. we have chosen JMyron, but haven't
		 * set it's advanced settings
		 */
		controller.setVars(new String[] { txt_width.getText(),
			txt_height.getText(),
			radbtn_15fps.getSelection() ? "15" : "30",
			cmbo_cam_lib.getText(),
			radbtn_rgb.getSelection() ? "RGB" : "YUV",
			cmbo_cam_num.getText() });
		controller.btnOkAction();
	    }
	});
	txt_width = new Text(sShell, SWT.BORDER);
	txt_width.setBounds(new Rectangle(61, 80, 76, 21));

	txt_width.setText("640");
	txt_height = new Text(sShell, SWT.BORDER);
	txt_height.setBounds(new Rectangle(208, 80, 76, 21));

	txt_height.setText("480");
	lbl_width = new Label(sShell, SWT.NONE);
	lbl_width.setBounds(new Rectangle(15, 80, 35, 15));
	lbl_width.setText("Width:");
	lbl_height = new Label(sShell, SWT.NONE);
	lbl_height.setBounds(new Rectangle(163, 80, 39, 15));
	lbl_height.setText("Height:");
	createGrpFrameRate();
	createGrpFormat();
	lbl_cam_num = new Label(sShell, SWT.NONE);
	lbl_cam_num.setBounds(new Rectangle(15, 44, 83, 21));
	lbl_cam_num.setText("Cam. Number:");
	createCmboCamNum();
	createCmboCamLib();
	lbl_cam_lib = new Label(sShell, SWT.NONE);
	lbl_cam_lib.setBounds(new Rectangle(15, 17, 112, 17));
	lbl_cam_lib.setText("Cam. Video Library:");
	btn_jmyron_settings = new Button(sShell, SWT.NONE);
	btn_jmyron_settings.setText("More ..");
	btn_jmyron_settings.setSize(new Point(73, 25));
	btn_jmyron_settings.setVisible(false);
	btn_jmyron_settings.setLocation(new Point(155, 180));
	btn_cancel = new Button(sShell, SWT.NONE);
	btn_cancel.setText("Cancel");
	btn_cancel.setSize(new Point(73, 25));
	btn_cancel.setLocation(new Point(10, 181));
	btn_cancel
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
		    @Override
		    public void widgetSelected(
			    final org.eclipse.swt.events.SelectionEvent e) {
			sShell.setVisible(false);
		    }
		});
	btn_jmyron_settings
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
			controller.setVars(new String[] { txt_width.getText(),
				txt_height.getText(),
				radbtn_15fps.getSelection() ? "15" : "30",
				cmbo_cam_lib.getText(),
				radbtn_rgb.getSelection() ? "RGB" : "YUV",
				cmbo_cam_num.getText() });
			controller.btnJmyronSettingsAction();
		    }
		});
    }

    /**
     * This method initializes grp_frame_rate.
     */
    private void createGrpFrameRate() {
	grp_frame_rate = new Group(sShell, SWT.NONE);
	grp_frame_rate.setLayout(null);
	grp_frame_rate.setBounds(new Rectangle(15, 120, 135, 48));
	grp_frame_rate.setText("Frame Rate");
	radbtn_15fps = new Button(grp_frame_rate, SWT.RADIO);
	radbtn_15fps.setBounds(new Rectangle(8, 20, 52, 16));
	radbtn_15fps.setText("15 fps");

	radbtn_30fps = new Button(grp_frame_rate, SWT.RADIO);
	radbtn_30fps.setBounds(new Rectangle(86, 20, 52, 16));
	radbtn_30fps.setText("30 fps");
	radbtn_30fps.setSelection(true);

    }

    /**
     * This method initializes grp_format.
     */
    private void createGrpFormat() {
	grp_format = new Group(sShell, SWT.NONE);
	grp_format.setLayout(null);
	grp_format.setText("Format");
	grp_format.setBounds(new Rectangle(159, 121, 146, 49));
	radbtn_rgb = new Button(grp_format, SWT.RADIO);
	radbtn_rgb.setBounds(new Rectangle(8, 20, 43, 16));
	radbtn_rgb.setText("RGB");
	radbtn_yuv = new Button(grp_format, SWT.RADIO);
	radbtn_yuv.setBounds(new Rectangle(74, 21, 43, 16));
	radbtn_yuv.setText("YUV");
	radbtn_yuv.setSelection(true);
    }

    /**
     * This method initializes cmbo_cam_num.
     */
    private void createCmboCamNum() {
	cmbo_cam_num = new Combo(sShell, SWT.READ_ONLY);
	cmbo_cam_num.setBounds(new Rectangle(184, 46, 116, 23));
	cmbo_cam_num.add("0");
	cmbo_cam_num.add("1");
	cmbo_cam_num.add("2");
	cmbo_cam_num.setText("0");
    }

    /**
     * This method initializes cmbo_cam_lib.
     */
    private void createCmboCamLib() {
	cmbo_cam_lib = new Combo(sShell, SWT.READ_ONLY);
	cmbo_cam_lib.setBounds(new Rectangle(184, 14, 116, 23));

	cmbo_cam_lib.add("AGCamLib");
	cmbo_cam_lib.add("JMF");
	cmbo_cam_lib.add("OpenCV");
	cmbo_cam_lib.add("JMyron");
	cmbo_cam_lib.setText("AGCamLib");

	cmbo_cam_lib.addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(final SelectionEvent e) {
		final String library = cmbo_cam_lib.getText();
		if (library.equals("OpenCV")) {
		    radbtn_rgb.setEnabled(false);
		    radbtn_yuv.setEnabled(false);
		    btn_jmyron_settings.setVisible(false);
		    cmbo_cam_num.setEnabled(true);
		} else if (library.equals("JMF")) {
		    radbtn_rgb.setEnabled(true);
		    radbtn_yuv.setEnabled(true);
		    btn_jmyron_settings.setVisible(false);
		    cmbo_cam_num.setEnabled(true);
		} else if (library.equals("JMyron")) {
		    radbtn_rgb.setEnabled(false);
		    radbtn_yuv.setEnabled(false);
		    btn_jmyron_settings.setVisible(true);
		    cmbo_cam_num.setEnabled(false);
		} else if (library.equals("AGCamLib")) {
		    radbtn_rgb.setEnabled(false);
		    radbtn_yuv.setEnabled(false);
		    btn_jmyron_settings.setVisible(false);
		    cmbo_cam_num.setEnabled(true);
		}
	    }

	    @Override
	    public void widgetDefaultSelected(final SelectionEvent e) {
	    }
	});
    }

    @Override
    public void loadData(final String[] str_array) {
	if (str_array.length == 6) {
	    final String library = str_array[0];
	    final int cam_num = Integer.parseInt(str_array[1]);
	    final int width = Integer.parseInt(str_array[2]);
	    final int height = Integer.parseInt(str_array[3]);
	    final int frame_rate = Integer.parseInt(str_array[4]);
	    final String format = str_array[5];

	    try {

		cmbo_cam_lib.setText(library);
		cmbo_cam_num.setText(String.valueOf(cam_num));
		txt_width.setText(String.valueOf(width));
		txt_height.setText(String.valueOf(height));
		switch (frame_rate) {
		case 15:
		    radbtn_15fps.setSelection(true);
		    break;
		case 30:
		    radbtn_30fps.setSelection(true);
		    break;
		}
		if (format.equals("RGB"))
		    radbtn_rgb.setSelection(true);
		else if (format.equals("YUV"))
		    radbtn_yuv.setSelection(true);
	    } catch (final Exception e) {
		e.printStackTrace();
	    }
	} else
	    System.out.print("Error in number of argument of GUI!\n");
    }

    public void setVidLibs(final String[] libs) {
	cmbo_cam_lib.removeAll();
	for (final String lib : libs)
	    cmbo_cam_lib.add(lib);
	cmbo_cam_lib.setText(libs[0]);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setController(final ControllerUI controller) {
	super.setController(controller);
	this.controller = (CtrlCamOptions) controller;
    }

    @Override
    public void clearForm() {
	txt_height.setText("");
	txt_width.setText("");
    }
}