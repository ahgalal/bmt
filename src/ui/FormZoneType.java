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

	private Button	btnOk		= null;
	private Combo	cmboType	= null;
	private Label	lblMsg		= null;
	private int		shapeNumber;
	private Shell	sShell		= null; // @jve:decl-index=0:visual-constraint="10,10"

	/**
	 * This method initializes cmbo_type.
	 */
	private void createCmboType() {
		cmboType = new Combo(sShell, SWT.DROP_DOWN | SWT.READ_ONLY);
		cmboType.setText("Normal");
		cmboType.setBounds(new Rectangle(83, 30, 121, 26));
		cmboType.add("Normal");
		cmboType.add("Central");
		cmboType.select(0);
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
		lblMsg = new Label(sShell, SWT.NONE);
		lblMsg.setBounds(new Rectangle(11, 7, 271, 19));
		lblMsg.setText("Please select the type of the new zone:");
		btnOk = new Button(sShell, SWT.NONE);
		btnOk.setBounds(new Rectangle(104, 62, 84, 28));
		btnOk.setText("OK");
		btnOk.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					final org.eclipse.swt.events.SelectionEvent e) {
				((ZonesModule) ModulesManager.getDefault().getModuleByName(
						"Zones Module")).addZone(shapeNumber, ZoneType
						.string2ZoneType(cmboType.getItem(cmboType
								.getSelectionIndex())));
				sShell.close();
			}
		});
		createCmboType();
	}

	/**
	 * Opens/Shows the window.
	 * 
	 * @param shapeNumber
	 *            number of the shape being added
	 */
	public void open(final int shapeNumber) {
		this.shapeNumber = shapeNumber;
		createSShell();
		sShell.open();
	}

}
