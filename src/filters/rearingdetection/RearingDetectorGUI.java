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

package filters.rearingdetection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import ui.PluggedGUI;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.PManager.ProgramState.GeneralState;
import utils.StatusManager.StatusSeverity;

/**
 * GUI class for the rearing detector filter.
 * 
 * @author Creative
 */

public class RearingDetectorGUI extends PluggedGUI<RearingDetector> {
	private Button		btnNotRearing	= null;

	private Button		btnRearingNow	= null;

	private Composite	cmpstRearing;

	private ExpandItem	xpndtmRearing;

	public RearingDetectorGUI(final RearingDetector owner) {
		super(owner);
	}

	/**
	 * Enables/disables the not rearing now button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnNotRearingEnable(final boolean enable) {
		btnNotRearing.setEnabled(enable);
	}

	/**
	 * Enables/disables the rearing now button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnRearingNowEnable(final boolean enable) {
		btnRearingNow.setEnabled(enable);
	}

	@Override
	public void deInitialize() {
		cmpstRearing.dispose();
		xpndtmRearing.dispose();
	}

	@Override
	public void initialize(final Shell shell, final ExpandBar expandBar,
			final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
		xpndtmRearing = new ExpandItem(expandBar, SWT.NONE);
		xpndtmRearing.setExpanded(true);
		xpndtmRearing.setText("Rearing Detector");

		cmpstRearing = new Composite(expandBar, SWT.NONE);
		xpndtmRearing.setControl(cmpstRearing);

		btnRearingNow = new Button(cmpstRearing, SWT.NONE);
		btnRearingNow.setText("Rearing NOW");
		btnRearingNow.setSize(new Point(100, 25));
		btnRearingNow.setLocation(new Point(10, 10));
		btnNotRearing = new Button(cmpstRearing, SWT.NONE);
		btnNotRearing.setBounds(new Rectangle(10, 35, 101, 25));
		btnNotRearing.setText("Not Rearing");

		btnNotRearing
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						rearingNow(false);
					}
				});
		btnRearingNow
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						rearingNow(true);
					}
				});
		xpndtmRearing.setHeight(xpndtmRearing.getControl().computeSize(
				SWT.DEFAULT, SWT.DEFAULT).y + 10);
	}

	/**
	 * Notifies the VideoManager that the rat is (rearing/not rearing) in
	 * reality, so that the VideoManager can start learning the rat's size when
	 * (rearing/not rearing).
	 * 
	 * @param rearing
	 *            is the rat rearing now?
	 */
	public void rearingNow(final boolean rearing) {
		if (PManager.getDefault().getState().getGeneral() == GeneralState.TRACKING)
			((RearingDetector) PManager.getDefault().getVideoManager()
					.getFilterManager().getFilterByName("RearingDetector"))
					.rearingNow(rearing);
		else
			PManager.getDefault().getStatusMgr().setStatus(
					"Tracking is not running!", StatusSeverity.ERROR);
	}

	@Override
	public void stateGeneralChangeHandler(final ProgramState state) {
		switch (state.getGeneral()) {
			case IDLE:
				btnNotRearing.setEnabled(false);
				btnRearingNow.setEnabled(false);
				break;
			case TRACKING:
				btnNotRearing.setEnabled(true);
				btnRearingNow.setEnabled(true);
				break;
			default:
				btnNotRearing.setEnabled(false);
				btnRearingNow.setEnabled(false);
				break;
		}
	}

	@Override
	public void stateStreamChangeHandler(final ProgramState state) {
		// TODO Auto-generated method stub

	}
}
