package utils.video.filters.recorder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import utils.PManager;

public class VideoRecorderGUI
{
	private Button btn_start_record = null;
	private Button btn_stop_record = null;
	private Shell shell;

	public VideoRecorderGUI(final Shell shell, final Composite parent)
	{
		this.shell=shell;
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
						.getVideoProcessor()
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
				stoprecordAction();
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

	public void stoprecordAction()
	{

		PManager.getDefault()
				.getVideoProcessor()
				.getFilterManager()
				.enableFilter("Recorder", false);
		final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		final String file_name = fileDialog.open();
		((VideoRecorder) PManager.getDefault()
				.getVideoProcessor()
				.getFilterManager()
				.getFilterByName(
						"Recorder")).saveVideoFile(file_name);

	}
}
