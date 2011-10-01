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

package utils.video.filters.rearingdetection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Shell;

import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.FilterGUI;

/**
 * GUI class for the rearing detector filter.
 * 
 * @author Creative
 */

public class RearingDetectorGUI extends FilterGUI
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
	public RearingDetectorGUI(Shell shell, ExpandBar expandBar)
	{
		super(shell,expandBar);
		
		ExpandItem xpndtmRearing = new ExpandItem(expandBar, SWT.NONE);
		xpndtmRearing.setExpanded(true);
		xpndtmRearing.setText("Rearing Detector");
		
		Composite cmpstRearing = new Composite(expandBar, SWT.NONE);
		xpndtmRearing.setControl(cmpstRearing);
		
		
		
		btn_rearing_now = new Button(cmpstRearing, SWT.NONE);
		btn_rearing_now.setText("Rearing NOW");
		btn_rearing_now.setSize(new Point(100, 25));
		btn_rearing_now.setLocation(new Point(10, 10));
		btn_not_rearing = new Button(cmpstRearing, SWT.NONE);
		btn_not_rearing.setBounds(new Rectangle(10, 35, 101, 25));
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
		xpndtmRearing.setHeight(xpndtmRearing.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y+10);
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
