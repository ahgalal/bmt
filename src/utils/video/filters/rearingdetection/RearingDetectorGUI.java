package utils.video.filters.rearingdetection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;

/**
 * GUI class for the rearing detector filter.
 * 
 * @author Creative
 */

public class RearingDetectorGUI
{
	private Button btn_rearing_now = null;
	private Button btn_not_rearing = null;

	/**
	 * Enables/disables the rearing now button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnRearingNowEnable(final boolean enable)
	{
		btn_rearing_now.setEnabled(enable);
	}

	/**
	 * Enables/disables the not rearing now button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnNotRearingEnable(final boolean enable)
	{
		btn_not_rearing.setEnabled(enable);
	}

	/**
	 * Initializes/shows the GUI components.
	 * 
	 * @param parent
	 *            parent composite that the components will be children of
	 */
	public RearingDetectorGUI(final Composite parent)
	{
		btn_rearing_now = new Button(parent, SWT.NONE);
		btn_rearing_now.setText("Rearing NOW");
		btn_rearing_now.setSize(new Point(100, 25));
		btn_rearing_now.setLocation(new Point(188, 530));
		btn_not_rearing = new Button(parent, SWT.NONE);
		btn_not_rearing.setBounds(new Rectangle(85, 530, 101, 25));
		btn_not_rearing.setText("Not Rearing");

		btn_not_rearing.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				rearingNow(false);
			}
		});
		btn_rearing_now.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				rearingNow(true);
			}
		});
	}

	/**
	 * Notifies the VideoManager that the rat is (rearing/not rearing) in
	 * reality, so that the VideoManager can start learning the rat's size
	 * when (rearing/not rearing).
	 * 
	 * @param rearing
	 *            is the rat rearing now?
	 */
	public void rearingNow(final boolean rearing)
	{
		if (PManager.getDefault().state == ProgramState.TRACKING)
			((RearingDetector) PManager.getDefault()
					.getVideoManager()
					.getFilterManager()
					.getFilterByName(
							"RearingDetector")).rearingNow(rearing);
		else
			PManager.getDefault().status_mgr.setStatus(
					"Tracking is not running!",
					StatusSeverity.ERROR);
	}
}
