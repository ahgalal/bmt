package utils.video.processors;

import java.io.File;

import lib_avi.StreamToAVI;
import lib_avi.AVIOutputStream.VideoFormat;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;

public class VideoRecorder extends VideoFilter{

	private PManager pm;
	private StreamToAVI avi_saver;
	private int width;
	private int height;

	public VideoRecorder(int width, int height) {
		this.width = width;
		this.height = height;
		pm=PManager.getDefault();
	}

	public void renameVideoFile(String file_name)
	{
		File tmp_file = new File("video.avi");
		tmp_file.renameTo(new File(file_name));
	}

	public void close() {
		avi_saver.close();		
	}

	public void saveVideoFile(String fileName) {
		renameVideoFile(fileName);		
	}

	/* (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public int[] process(int[] imageData) {
		if(enabled)
			avi_saver.writeFrame(imageData);
		return imageData;
	}

	@Override
	public boolean enable(boolean enable) {
		if(enable)
		{
			if(pm.state==ProgramState.TRACKING)
			{
				avi_saver = new StreamToAVI();
				avi_saver.initialize("video.avi", VideoFormat.JPG, 10, width, height);
				pm.state=ProgramState.RECORDING;
				enabled=true;
				return true;
			}
			else
				pm.status_mgr.setStatus("Please start tracking first", StatusSeverity.ERROR);
			return false;
		}
		else
		{
			Thread th_stop_recording = new Thread(new Runnable() {
				@Override
				public void run() {
					enabled=false;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(pm.state==ProgramState.RECORDING)
					{
						avi_saver.close();
						avi_saver=null;
						pm.state=ProgramState.TRACKING;
					}		
				}
			});
			th_stop_recording.start();
			return true;
		}
	}
}
