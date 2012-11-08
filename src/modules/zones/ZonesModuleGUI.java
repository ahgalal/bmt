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

package modules.zones;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import ui.PluggedGUI;
import utils.PManager;
import utils.PManager.ProgramState;

/**
 * GUI class for the ZonesModule.
 * 
 * @author Creative
 */
public class ZonesModuleGUI extends PluggedGUI<ZonesModule> {
	/**
	 * Initializes/shows the GUI components.
	 * 
	 * @param mnu_edit
	 *            the Edit menu in MainGUI
	 */
	/*
	 * public ZonesModuleGUI() { }
	 */

	public ZonesModuleGUI(final ZonesModule owner) {
		super(owner);
	}

	@Override
	public void initialize(final Shell shell, final ExpandBar expandBar,
			final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
		Menu mnu_edit = null;
		for (final MenuItem miOut : menuBar.getItems())
			if (miOut.getText().equals("Edit"))
				mnu_edit = miOut.getMenu();
		final MenuItem mnutm_edit_openzoneeditor = new MenuItem(mnu_edit,
				SWT.PUSH);
		mnutm_edit_openzoneeditor.setText("Zone Editor ..");
		mnutm_edit_openzoneeditor
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					@Override
					public void widgetDefaultSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
					}

					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						mnutmEditOpenZoneEditorAction();
					}
				});
	}

	/**
	 * Handles the "Zone editor" menu item click action.
	 */
	public void mnutmEditOpenZoneEditorAction() {
		PManager.getDefault().drw_zns.show(true);
	}

	@Override
	public void stateStreamChangeHandler(ProgramState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateGeneralChangeHandler(ProgramState state) {
		// TODO Auto-generated method stub
		
	}
}
