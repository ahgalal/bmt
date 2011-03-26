package utils.video.processors.recorder;

import java.io.File;

import lib_avi.StreamToAVI;
import lib_avi.AVIOutputStream.VideoFormat;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;
import utils.video.processors.FilterConfigs;
import utils.video.processors.VideoFilter;

public class VideoRecorder extends VideoFilter
{

	private final PManager pm;
	private StreamToAVI avi_saver;
	private final RecorderConfigs recorder_configs;

	public VideoRecorder(final String name, final FilterConfigs configs)
	{
		super(name, configs);
		recorder_configs = (RecorderConfigs) configs;
		pm = PManager.getDefault();
	}

	public void renameVideoFile(final String file_name)
	{
		final File tmp_file = new File("video.avi");
		if (!tmp_file.renameTo(new File(file_name)))
			PManager.log.print("Couldn't rename video file", this, StatusSeverity.ERROR);
	}

	public void close()
	{
		avi_saver.close();
	}

	public void saveVideoFile(final String fileName)
	{
		renameVideoFile(fileName);
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public int[] process(final int[] imageData)
	{
		if (configs.enabled)
			avi_saver.writeFrame(imageData);
		return imageData;
	}

	@Override
	public boolean enable(final boolean enable)
	{
		if (enable)
		{
			if (pm.state == ProgramState.TRACKING)
			{
				avi_saver = new StreamToAVI();
				avi_saver.initialize(
						"video.avi",
						VideoFormat.JPG,
						10,
						recorder_configs.common_configs.width,
						recorder_configs.common_configs.height);
				pm.state = ProgramState.RECORDING;
				configs.enabled = true;
				return true;
			} else
				pm.status_mgr.setStatus(
						"Please start tracking first",
						StatusSeverity.ERROR);
			return false;
		} else
		{
			final Thread th_stop_recording = new Thread(new Runnable() {
				@Override
				public void run()
				{
					configs.enabled = false;
					try
					{
						Thread.sleep(100);
					} catch (final InterruptedException e)
					{
						e.printStackTrace();
					}
					if (pm.state == ProgramState.RECORDING)
					{
						avi_saver.close();
						avi_saver = null;
						pm.state = ProgramState.TRACKING;
					}
				}
			});
			th_stop_recording.start();
			return true;
		}
	}

	@Override
	public boolean initialize()
	{
		return true;
	}
}
