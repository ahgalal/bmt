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

package utils.video.filters.recorder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import ui.PluggedGUI;
import utils.PManager;
import utils.PManager.ProgramState;

/**
 * GUI class for the video recorder filter.
 * 
 * @author Creative
 */
public class VideoRecorderGUI extends PluggedGUI
{

	private Button btnStartRecordingEBar = null;
	private Button btnStopRecordingEBar = null;
	private Button btnStartRecordingCBar;
	private Button btnStopRecordingCBar;

	private boolean isRecording = false;

	/**
	 * Initializes/shows the GUI components.
	 * 
	 * @param shell
	 *            parent shell of the components
	 * @param parent
	 *            parent composite that the components will be children of
	 */
	@Override
	public void initialize(
			final Shell shell,
			final ExpandBar expandBar,
			final Menu menuBar,
			final CoolBar coolBar)
	{
		// super(shell, expandBar);
		this.shell = shell;

		// //////////////////////////
		// ExpandBar Stuff
		// //////////////////////////
		final ExpandItem xpndtmRecorder = new ExpandItem(expandBar, SWT.NONE);
		xpndtmRecorder.setExpanded(true);
		xpndtmRecorder.setText("Recording");

		final Composite cmpstRecordingEBar = new Composite(expandBar, SWT.NONE);
		xpndtmRecorder.setControl(cmpstRecordingEBar);

		btnStartRecordingEBar = new Button(cmpstRecordingEBar, SWT.NONE);
		btnStartRecordingEBar.setText("Start Recording");
		btnStartRecordingEBar.setSize(new Point(100, 25));
		btnStartRecordingEBar.setLocation(new Point(10, 10));
		btnStartRecordingEBar.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			/**
			 * Starts video recording.
			 */
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				startRecordAction();
			}
		});
		btnStopRecordingEBar = new Button(cmpstRecordingEBar, SWT.NONE);
		btnStopRecordingEBar.setText("Stop Recording");
		btnStopRecordingEBar.setSize(new Point(100, 25));
		btnStopRecordingEBar.setLocation(new Point(10, 35));
		btnStopRecordingEBar.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			/**
			 * Stops video recording, and asks for a location to save the video
			 * file.
			 */
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				stopRecordAction();
			}
		});
		xpndtmRecorder.setHeight(xpndtmRecorder.getControl().computeSize(
				SWT.DEFAULT,
				SWT.DEFAULT).y + 10);

		// //////////////////////////
		// CoolBar Stuff
		// //////////////////////////
		final CoolItem cItemRecording = new CoolItem(coolBar, SWT.NONE);
		final Composite cmpstRecordingCBar = new Composite(coolBar, SWT.NONE);
		cItemRecording.setControl(cmpstRecordingCBar);
		cmpstRecordingCBar.setLayout(new FillLayout(SWT.HORIZONTAL));

		btnStartRecordingCBar = new Button(cmpstRecordingCBar, SWT.NONE);
		btnStartRecordingCBar.setText("Rcrd");
		btnStartRecordingCBar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				startRecordAction();
			}
		});

		btnStopRecordingCBar = new Button(cmpstRecordingCBar, SWT.NONE);
		btnStopRecordingCBar.setText("stp");
		btnStopRecordingCBar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				stopRecordAction();
			}
		});
		cItemRecording.setSize(70, 30);
	}

	/**
	 * Stops recording frames, and saves the video file.
	 */
	private void stopRecordAction()
	{
		if (programState == ProgramState.TRACKING && isRecording == true)
		{
			isRecording = false;
			inTrackingState();
			PManager.getDefault()
					.getVideoManager()
					.getFilterManager()
					.enableFilter("Recorder", false);
			final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
			final String file_name = fileDialog.open();
			((VideoRecorder) PManager.getDefault()
					.getVideoManager()
					.getFilterManager()
					.getFilterByName(
							"Recorder")).saveVideoFile(file_name);
		}

	}

	@Override
	public void inIdleState()
	{
		btnStartRecordingEBar.setEnabled(false);
		btnStopRecordingEBar.setEnabled(false);

		btnStartRecordingCBar.setEnabled(false);
		btnStopRecordingCBar.setEnabled(false);
	}

	@Override
	public void inStreamingState()
	{
		btnStartRecordingEBar.setEnabled(false);
		btnStopRecordingEBar.setEnabled(false);

		btnStartRecordingCBar.setEnabled(false);
		btnStopRecordingCBar.setEnabled(false);

		if (programState == ProgramState.TRACKING && isRecording == true) // was
																			// tracking
																			// then
																			// stopped
			stopRecordAction();
	}

	@Override
	public void inTrackingState()
	{
		if (isRecording)
		{
			btnStartRecordingEBar.setEnabled(false);
			btnStopRecordingEBar.setEnabled(true);
			btnStartRecordingCBar.setEnabled(false);
			btnStopRecordingCBar.setEnabled(true);
		}
		else
		{
			btnStartRecordingEBar.setEnabled(true);
			btnStopRecordingEBar.setEnabled(false);
			btnStartRecordingCBar.setEnabled(true);
			btnStopRecordingCBar.setEnabled(false);
		}
	}

	/**
	 * Starts recording frames, and saves the video file.
	 */
	private void startRecordAction()
	{
		isRecording = true;
		inTrackingState();
		PManager.getDefault()
				.getVideoManager()
				.getFilterManager()
				.enableFilter("Recorder", true);
	}
}
