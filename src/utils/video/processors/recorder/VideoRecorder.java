package utils.video.processors.recorder;

import java.io.File;

import lib_avi.StreamToAVI;
import lib_avi.AVIOutputStream.VideoFormat;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;
import utils.video.processors.VideoFilter;

public class VideoRecorder extends VideoFilter{

	private PManager pm;
	private StreamToAVI avi_saver;
	private RecorderConfigs recorder_configs;
	public VideoRecorder(String name) {
		recorder_configs=new RecorderConfigs(null);
		configs=recorder_configs;
		pm=PManager.getDefault();
		this.name=name;
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
		if(configs.enabled)
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
				avi_saver.initialize("video.avi", VideoFormat.JPG, 10, recorder_configs.common_configs.width, recorder_configs.common_configs.height);
				pm.state=ProgramState.RECORDING;
				configs.enabled=true;
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
					configs.enabled=false;
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

	@Override
	public boolean initialize() {
		return true;
	}
}
