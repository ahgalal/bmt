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
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import utils.PManager;
import utils.PManager.ProgramState;
import utils.video.filters.PluggedGUI;

/**
 * GUI class for the video recorder filter.
 * 
 * @author Creative
 */
public class VideoRecorderGUI extends PluggedGUI
{

	private Button btn_start_record = null;
	private Button btn_stop_record = null;

	/**
	 * Initializes/shows the GUI components.
	 * 
	 * @param shell
	 *            parent shell of the components
	 * @param parent
	 *            parent composite that the components will be children of
	 */
	public void initialize(final Shell shell,final ExpandBar expandBar, Menu menuBar,CoolBar coolBar)
	{
		//super(shell, expandBar);
		this.shell = shell;
		ExpandItem xpndtmRecorder = new ExpandItem(expandBar, SWT.NONE);
		xpndtmRecorder.setExpanded(true);
		xpndtmRecorder.setText("Recording");
		
		Composite cmpstRecording = new Composite(expandBar, SWT.NONE);
		xpndtmRecorder.setControl(cmpstRecording);
		
		btn_start_record = new Button(cmpstRecording, SWT.NONE);
		btn_start_record.setText("Start Recording");
		btn_start_record.setSize(new Point(100, 25));
		btn_start_record.setLocation(new Point(10, 10));
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
		btn_stop_record = new Button(cmpstRecording, SWT.NONE);
		btn_stop_record.setText("Stop Recording");
		btn_stop_record.setSize(new Point(100, 25));
		btn_stop_record.setLocation(new Point(10, 35));
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
		xpndtmRecorder.setHeight(xpndtmRecorder.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y+10);
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

	@Override
	public void inIdleState()
	{
		btn_start_record.setEnabled(false);
		btn_stop_record.setEnabled(false);
	}

	@Override
	public void inStreamingState()
	{
		btn_start_record.setEnabled(false);
		btn_stop_record.setEnabled(false);
		
		if(programState== ProgramState.TRACKING) // was tracking then stopped
			stopRecordAction();
	}

	@Override
	public void inTrackingState()
	{
		btn_start_record.setEnabled(true);
		btn_stop_record.setEnabled(false);		
	}
	
	

	
	
}
