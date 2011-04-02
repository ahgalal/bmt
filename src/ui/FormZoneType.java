package ui;

import model.Zone.ZoneType;
import modules.ModulesManager;
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
public class FormZoneType
{

	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="10,10"
	private Label lbl_msg = null;
	private Button btn_ok = null;
	private Combo cmbo_type = null;
	private int shape_number;

	/**
	 * This method initializes sShell.
	 */
	private void createSShell()
	{
		sShell = new Shell(SWT.BORDER
				| SWT.DIALOG_TRIM
				| SWT.APPLICATION_MODAL
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
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				((ZonesModule) ModulesManager.getDefault().getModuleByName("Zones Module"))
						.addZone(
								shape_number,
								ZoneType.string2ZoneType(cmbo_type.getItem(cmbo_type.getSelectionIndex())));
				sShell.close();
			}
		});
		createCmboType();
	}

	/**
	 * This method initializes cmbo_type.
	 */
	private void createCmboType()
	{
		cmbo_type = new Combo(sShell, SWT.DROP_DOWN | SWT.READ_ONLY);
		cmbo_type.setText("Normal");
		cmbo_type.setBounds(new Rectangle(83, 30, 121, 26));
		cmbo_type.add("Normal");
		cmbo_type.add("Central");
		cmbo_type.select(0);
	}

	/**
	 * Opens/Shows the window.
	 * 
	 * @param shape_number
	 *            number of the shape being added
	 */
	public void open(final int shape_number)
	{
		this.shape_number = shape_number;
		createSShell();
		sShell.open();
	}

}
