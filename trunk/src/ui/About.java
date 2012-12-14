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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import control.ui.ControllerUI;
import control.ui.CtrlAbout;

/**
 * About dialog box, displays program description and credits.
 * 
 * @author Creative
 */
public class About extends BaseUI {
	private Button		btnOK			= null;

	@SuppressWarnings("unused")
	private CtrlAbout	controller;			// @jve:decl-index=0:

	private Group		grpFormat		= null;
	private Label		lblProjDesc	= null;
	private Shell		sShell1;
	private Text		textArea		= null;
	private Text		txtOwnerEmail	= null;
	private Text		txtOwnerName	= null;

	/**
	 * Initializes the GUI components.
	 */
	public About() {
		createSShell();
		super.sShell = sShell1;
	}

	@Override
	public void clearForm() {
	}

	/**
	 * This method initializes grp_format.
	 */
	private void createGrpFormat() {
		grpFormat = new Group(sShell1, SWT.NONE);
		grpFormat.setLayout(null);
		grpFormat.setText("Credits:");
		grpFormat.setBounds(new Rectangle(11, 165, 444, 105));
		txtOwnerName = new Text(grpFormat, SWT.MULTI | SWT.WRAP);
		txtOwnerName.setBounds(new Rectangle(10, 20, 236, 82));
		txtOwnerName.setEditable(false);
		txtOwnerName.setEnabled(true);
		txtOwnerName
				.setText("- Ahmed Galal El-Din (Software)\n- Ahmed Mohammed Ali (Software)\n- Mohammed Ahmed Ramadan (Software)\n- Mohammed Mostafa (Neuro-Science)\n- Sarah Mohammed Hamed (Software)");
		txtOwnerEmail = new Text(grpFormat, SWT.MULTI | SWT.WRAP);
		txtOwnerEmail.setBounds(new Rectangle(266, 20, 169, 61));
		txtOwnerEmail.setEditable(false);
		txtOwnerEmail
				.setText("ceng.ahmedgalal@gmail.com\na.mohamed.aly0@gmail.com\nmido_emak@hotmail.com\neltabbal@gmail.com");
	}

	/**
	 * This method initializes sShell1.
	 */
	private void createSShell() {
		sShell1 = new Shell(SWT.TITLE | SWT.APPLICATION_MODAL);
		sShell1.setLayout(null);
		sShell1.setText("About");
		sShell1.setMaximized(false);
		sShell1.setSize(new Point(467, 326));
		btnOK = new Button(sShell1, SWT.NONE);
		btnOK.setBounds(new Rectangle(378, 271, 73, 25));
		btnOK.setText("OK");
		btnOK.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					final org.eclipse.swt.events.SelectionEvent e) {
				sShell.setVisible(false);
			}
		});
		lblProjDesc = new Label(sShell1, SWT.NONE);
		lblProjDesc.setBounds(new Rectangle(11, 5, 112, 17));
		lblProjDesc.setText("Project description:");

		createGrpFormat();
		textArea = new Text(sShell1, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		textArea.setEditable(false);
		textArea.setBounds(new Rectangle(11, 29, 443, 124));
		textArea.setText("Behavioural Monitoring Tool ( Ein BMT ) : this software is used to record and analyse some behavioural expermients (open field , water maze , forced swimming)  in neuroscience. the program was developed in collaboration with neuropharmacology lab at Faculty of Medicine,,  Ain Shams University . The sofware has 3 modules , each module is concerned with one of the three experminets.\nEin BMT (Open Field Module):\na test arena is divided into a number of zones. a rat or mouse is put inside the arena. rat movements are recorded and a number of parameters (number of zones, time spent in central zones, rearing behaviour, distance travelled) are analysed.");
	}

	@Override
	public void loadData(final String[] strArray) {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setController(final ControllerUI controller) {
		super.setController(controller);
		this.controller = (CtrlAbout) controller;
	}
}
