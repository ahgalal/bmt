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

package modules.zones;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import utils.PManager;
import utils.video.filters.PluggedGUI;

/**
 * GUI class for the ZonesModule.
 * 
 * @author Creative
 */
public class ZonesModuleGUI extends PluggedGUI
{
	/**
	 * Initializes/shows the GUI components.
	 * 
	 * @param mnu_edit
	 *            the Edit menu in MainGUI
	 */
	/*	public ZonesModuleGUI()
	{

	}*/

	/**
	 * Handles the "Zone editor" menu item click action.
	 */
	public void mnutmEditOpenZoneEditorAction()
	{
		PManager.getDefault().drw_zns.show(true);
	}

	@Override
	public void initialize(Shell shell, ExpandBar expandBar, Menu menuBar,CoolBar coolBar)
	{
		Menu mnu_edit = null;
		for(MenuItem miOut: menuBar.getItems())
			if(miOut.getText().equals("Edit"))
				mnu_edit = miOut.getMenu();
		final MenuItem mnutm_edit_openzoneeditor = new MenuItem(mnu_edit, SWT.PUSH);
		mnutm_edit_openzoneeditor.setText("Zone Editor ..");
		mnutm_edit_openzoneeditor.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					final org.eclipse.swt.events.SelectionEvent e)
			{
			}

			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				mnutmEditOpenZoneEditorAction();
			}
		});		
	}

	@Override
	public void inIdleState()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void inStreamingState()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void inTrackingState()
	{
		// TODO Auto-generated method stub

	}
}
