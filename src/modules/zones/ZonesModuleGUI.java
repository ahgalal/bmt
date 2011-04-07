package modules.zones;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import utils.PManager;

public class ZonesModuleGUI
{
	public ZonesModuleGUI(Menu mnu_edit)
	{
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
	
	/**
	 * Handles the "Zone editor" menu item click action.
	 */
	public void mnutmEditOpenZoneEditorAction()
	{
		PManager.getDefault().drw_zns.show(true);
	}
}
