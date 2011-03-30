package utils.video.filters.recorder;

import java.io.File;

import lib_avi.StreamToAVI;
import lib_avi.AVIOutputStream.VideoFormat;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.FilterConfigs;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;

/**
 * Records video frames from the incoming data stream.
 * @author Creative
 *
 */
public class VideoRecorder extends VideoFilter
{

	private final PManager pm;
	private StreamToAVI avi_saver;
	private final RecorderConfigs recorder_configs;

	/**
	 * Initializes the filter.
	 * @param name filter's name
	 * @param configs filter's configurations
	 * @param link_in input Link for the filter
	 * @param link_out output Link from the filter
	 */
	public VideoRecorder(final String name, final FilterConfigs configs,Link link_in,Link link_out)
	{
		super(name, configs,link_in,link_out);
		recorder_configs = (RecorderConfigs) configs;
		pm = PManager.getDefault();
	}

	/**
	 * Renames the current video file to the given name.
	 * @param file_name new name of the video file
	 */
	public void renameVideoFile(final String file_name)
	{
		final File tmp_file = new File("video.avi");
		if (!tmp_file.renameTo(new File(file_name)))
			PManager.log.print("Couldn't rename video file", this, StatusSeverity.ERROR);
	}

	/**
	 * Closes the video writer.
	 */
	public void close()
	{
		avi_saver.close();
	}

	/**
	 * Saves the current video with the given file name.
	 * @param fileName name of the file to save data to
	 */
	public void saveVideoFile(final String fileName)
	{
		renameVideoFile(fileName);
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public void process()
	{
		if (configs.enabled)
		{
			final int[] imageData=link_in.getData();
			avi_saver.writeFrame(imageData);
		}
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
