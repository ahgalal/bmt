package utils.video.input;

import utils.video.FrameIntArray;

public interface VidInputter {
	public boolean initialize(FrameIntArray frame_data,int width,int height,int cam_index);
	public boolean StartStream();
	public void StopModule();
	public void setFormat(String s);
	public int getStatus();
	public int getNumCams();
	public int displayMoreSettings();
	public String getName();
	//public void setCamIndex(int selectionIndex);
}
