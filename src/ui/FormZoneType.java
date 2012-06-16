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

import modules.ModulesManager;
import modules.zones.Zone.ZoneType;
import modules.zones.ZonesModule;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Enables the user to input the new zone's type.
 * 
 * @author Creative
 */
public class FormZoneType {

	private Button	btn_ok		= null;
	private Combo	cmbo_type	= null;
	private Label	lbl_msg		= null;
	private int		shape_number;
	private Shell	sShell		= null; // @jve:decl-index=0:visual-constraint="10,10"

	/**
	 * This method initializes cmbo_type.
	 */
	private void createCmboType() {
		cmbo_type = new Combo(sShell, SWT.DROP_DOWN | SWT.READ_ONLY);
		cmbo_type.setText("Normal");
		cmbo_type.setBounds(new Rectangle(83, 30, 121, 26));
		cmbo_type.add("Normal");
		cmbo_type.add("Central");
		cmbo_type.select(0);
	}

	/**
	 * This method initializes sShell.
	 */
	private void createSShell() {
		sShell = new Shell(SWT.BORDER | SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL
				| SWT.ON_TOP);
		sShell.setText("Zone Type:");
		sShell.setSize(new Point(299, 124));
		sShell.setLayout(null);
		lbl_msg = new Label(sShell, SWT.NONE);
		lbl_msg.setBounds(new Rectangle(11, 7, 271, 19));
		lbl_msg.setText("Please select the type of the new zone:");
		btn_ok = new Button(sShell, SWT.NONE);
		btn_ok.setBounds(new Rectangle(104, 62, 84, 28));
		btn_ok.setText("OK");
		btn_ok.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					final org.eclipse.swt.events.SelectionEvent e) {
				((ZonesModule) ModulesManager.getDefault().getModuleByName(
						"Zones Module")).addZone(shape_number, ZoneType
						.string2ZoneType(cmbo_type.getItem(cmbo_type
								.getSelectionIndex())));
				sShell.close();
			}
		});
		createCmboType();
	}

	/**
	 * Opens/Shows the window.
	 * 
	 * @param shape_number
	 *            number of the shape being added
	 */
	public void open(final int shape_number) {
		this.shape_number = shape_number;
		createSShell();
		sShell.open();
	}

}
