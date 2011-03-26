package utils.video.filters;

public class CommonFilterConfigs
{

	public CommonFilterConfigs(
			final int width,
			final int height,
			final int frameRate,
			final int camIndex,
			final String vidLibrary,
			final String format)
	{
		super();
		this.width = width;
		this.height = height;
		frame_rate = frameRate;
		cam_index = camIndex;
		vid_library = vidLibrary;
		this.format = format;
	}

	public int width, height, frame_rate, cam_index;
	public String vid_library, format;

}
