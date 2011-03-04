package utils.video.processors;

public class CommonConfigs {

	public CommonConfigs(int width, int height, int frameRate, int camIndex,
			String vidLibrary, String format) {
		super();
		this.width = width;
		this.height = height;
		frame_rate = frameRate;
		cam_index = camIndex;
		vid_library = vidLibrary;
		this.format = format;
	}

	public int width,height,frame_rate,cam_index;
	public String vid_library,format;
	
	
	
}
