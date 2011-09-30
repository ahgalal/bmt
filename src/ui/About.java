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
public class About extends BaseUI
{
	@SuppressWarnings("unused")
	private CtrlAbout controller; // @jve:decl-index=0:

	/**
	 * Initializes the GUI components.
	 */
	public About()
	{
		createSShell();
		super.sShell = sShell;
	}

	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="4,7"
	private Label lbl_proj_desc = null;
	private Button btn_OK = null;
	private Group grp_format = null;
	private Text textArea = null;
	private Text txt_owner_name = null;
	private Text txt_owner_email = null;

	/**
	 * This method initializes sShell1.
	 */
	private void createSShell()
	{
		sShell = new Shell(SWT.TITLE | SWT.APPLICATION_MODAL);
		sShell.setLayout(null);
		sShell.setText("About");
		sShell.setMaximized(false);
		sShell.setSize(new Point(467, 326));
		btn_OK = new Button(sShell, SWT.NONE);
		btn_OK.setBounds(new Rectangle(378, 271, 73, 25));
		btn_OK.setText("OK");
		btn_OK.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				sShell.setVisible(false);
			}
		});
		lbl_proj_desc = new Label(sShell, SWT.NONE);
		lbl_proj_desc.setBounds(new Rectangle(11, 5, 112, 17));
		lbl_proj_desc.setText("Project description:");

		createGrpFormat();
		textArea = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		textArea.setEditable(false);
		textArea.setBounds(new Rectangle(11, 29, 443, 124));
		textArea.setText("Behavioural Monitoring Tool ( Ein BMT ) : this software is used to record and analyse some behavioural expermients (open field , water maze , forced swimming)  in neuroscience. the program was developed in collaboration with neuropharmacology lab at Faculty of Medicine,,  Ain Shams University . The sofware has 3 modules , each module is concerned with one of the three experminets.\nEin BMT (Open Field Module):\na test arena is divided into a number of zones. a rat or mouse is put inside the arena. rat movements are recorded and a number of parameters (number of zones, time spent in central zones, rearing behaviour, distance travelled) are analysed.");
	}

	/**
	 * This method initializes grp_format.
	 */
	private void createGrpFormat()
	{
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

	@Override
	public void clearForm()
	{
	}

	@Override
	public void loadData(final String[] strArray)
	{
	}

	@Override
	public void setController(final ControllerUI controller)
	{
		super.setController(controller);
		this.controller = (CtrlAbout) controller;
	}

}
