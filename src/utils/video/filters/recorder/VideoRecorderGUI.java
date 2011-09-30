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

package utils.video.filters.recorder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import utils.PManager;

/**
 * GUI class for the video recorder filter.
 * 
 * @author Creative
 */
public class VideoRecorderGUI
{
	private Button btn_start_record = null;
	private Button btn_stop_record = null;
	private final Shell shell;

	/**
	 * Initializes/shows the GUI components.
	 * 
	 * @param shell
	 *            parent shell of the components
	 * @param parent
	 *            parent composite that the components will be children of
	 */
	public VideoRecorderGUI(final Shell shell, final Composite parent)
	{
		this.shell = shell;
		btn_start_record = new Button(parent, SWT.NONE);
		btn_start_record.setText("Start Recording");
		btn_start_record.setSize(new Point(100, 25));
		btn_start_record.setLocation(new Point(439, 16));
		btn_start_record.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			/**
			 * Starts video recording.
			 */
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				PManager.getDefault()
						.getVideoManager()
						.getFilterManager()
						.enableFilter("Recorder", true);
			}
		});
		btn_stop_record = new Button(parent, SWT.NONE);
		btn_stop_record.setText("Stop Recording");
		btn_stop_record.setSize(new Point(100, 25));
		btn_stop_record.setLocation(new Point(549, 16));
		btn_stop_record.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
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
	}

	/**
	 * Enables/disables the start recording button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnStartRecordEnable(final boolean enable)
	{
		btn_start_record.setEnabled(enable);
	}

	/**
	 * Enables/disables the stop recording button.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void btnStopRecordEnable(final boolean enable)
	{
		btn_stop_record.setEnabled(enable);
	}

	/**
	 * Stops recording frames, and saves the video file.
	 */
	public void stopRecordAction()
	{
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
