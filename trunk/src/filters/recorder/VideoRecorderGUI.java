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

package filters.recorder;

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
import org.eclipse.swt.widgets.Group;
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
public class VideoRecorderGUI extends PluggedGUI<VideoRecorder> {

	private Button		btnStartRecordingCBar;

	private Button		btnStartRecordingEBar	= null;
	private Button		btnStopRecordingCBar;
	private Button		btnStopRecordingEBar	= null;
	private CoolItem	cItemRecording;

	private Composite	cmpstRecordingCBar;
	private Composite	cmpstRecordingEBar;

	private boolean		isRecording				= false;

	private ExpandItem	xpndtmRecorder;

	public VideoRecorderGUI(final VideoRecorder owner) {
		super(owner);
	}

	@Override
	public void deInitialize() {
		cmpstRecordingCBar.dispose();
		cmpstRecordingEBar.dispose();
		xpndtmRecorder.dispose();
		cItemRecording.dispose();
	}

	/**
	 * Initializes/shows the GUI components.
	 * 
	 * @param shell
	 *            parent shell of the components
	 * @param parent
	 *            parent composite that the components will be children of
	 */
	@Override
	public void initialize(final Shell shell, final ExpandBar expandBar,
			final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
		// super(shell, expandBar);
		this.shell = shell;

		// //////////////////////////
		// ExpandBar Stuff
		// //////////////////////////
		xpndtmRecorder = new ExpandItem(expandBar, SWT.NONE);
		xpndtmRecorder.setExpanded(true);
		xpndtmRecorder.setText("Recording");

		cmpstRecordingEBar = new Composite(expandBar, SWT.NONE);
		xpndtmRecorder.setControl(cmpstRecordingEBar);

		btnStartRecordingEBar = new Button(cmpstRecordingEBar, SWT.NONE);
		btnStartRecordingEBar.setText("Start Recording");
		btnStartRecordingEBar.setSize(new Point(100, 25));
		btnStartRecordingEBar.setLocation(new Point(10, 10));
		btnStartRecordingEBar
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					/**
					 * Starts video recording.
					 */
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						startRecordAction();
					}
				});
		btnStopRecordingEBar = new Button(cmpstRecordingEBar, SWT.NONE);
		btnStopRecordingEBar.setText("Stop Recording");
		btnStopRecordingEBar.setSize(new Point(100, 25));
		btnStopRecordingEBar.setLocation(new Point(10, 35));
		btnStopRecordingEBar
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					/**
					 * Stops video recording, and asks for a location to save
					 * the video file.
					 */
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						stopRecordAction();
					}
				});
		xpndtmRecorder.setHeight(xpndtmRecorder.getControl().computeSize(
				SWT.DEFAULT, SWT.DEFAULT).y + 10);

		// //////////////////////////
		// CoolBar Stuff
		// //////////////////////////
		cItemRecording = new CoolItem(coolBar, SWT.NONE);
		cmpstRecordingCBar = new Composite(coolBar, SWT.NONE);
		cItemRecording.setControl(cmpstRecordingCBar);
		cmpstRecordingCBar.setLayout(new FillLayout(SWT.HORIZONTAL));

		btnStartRecordingCBar = new Button(cmpstRecordingCBar, SWT.NONE);
		btnStartRecordingCBar.setText("Rcrd");
		btnStartRecordingCBar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				startRecordAction();
			}
		});

		btnStopRecordingCBar = new Button(cmpstRecordingCBar, SWT.NONE);
		btnStopRecordingCBar.setText("stp");
		btnStopRecordingCBar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				stopRecordAction();
			}
		});
		cItemRecording.setSize(70, 30);
	}

	/**
	 * Starts recording frames, and saves the video file.
	 */
	private void startRecordAction() {
		isRecording = true;
		// inTrackingState();
		PManager.getDefault().getVideoManager().getFilterManager()
				.enableFilter("Recorder", true);
	}

	@Override
	public void stateGeneralChangeHandler(final ProgramState state) {
		switch (state.getGeneral()) {
			case IDLE:
				btnStartRecordingEBar.setEnabled(false);
				btnStopRecordingEBar.setEnabled(false);

				btnStartRecordingCBar.setEnabled(false);
				btnStopRecordingCBar.setEnabled(false);

				if (isRecording == true) // was
					// tracking
					// then
					// stopped
					stopRecordAction();
				break;
			case TRACKING:
				if (isRecording) {
					btnStartRecordingEBar.setEnabled(false);
					btnStopRecordingEBar.setEnabled(true);
					btnStartRecordingCBar.setEnabled(false);
					btnStopRecordingCBar.setEnabled(true);
				} else {
					btnStartRecordingEBar.setEnabled(true);
					btnStopRecordingEBar.setEnabled(false);
					btnStartRecordingCBar.setEnabled(true);
					btnStopRecordingCBar.setEnabled(false);
				}
				break;

			default:
				break;
		}
	}

	@Override
	public void stateStreamChangeHandler(final ProgramState state) {
		switch (state.getStream()) {
			case IDLE:
				btnStartRecordingEBar.setEnabled(false);
				btnStopRecordingEBar.setEnabled(false);

				btnStartRecordingCBar.setEnabled(false);
				btnStopRecordingCBar.setEnabled(false);
				break;

			default:
				break;
		}

	}

	/**
	 * Stops recording frames, and saves the video file.
	 */
	private void stopRecordAction() {
		if (isRecording == true) {
			isRecording = false;
			// inTrackingState();
			PManager.getDefault().getVideoManager().getFilterManager()
					.enableFilter("Recorder", false);
			final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
			final String file_name = fileDialog.open();
			if (file_name != null)
				((VideoRecorder) PManager.getDefault().getVideoManager()
						.getFilterManager().getFilterByName("Recorder"))
						.saveVideoFile(file_name);
		}

	}
}
