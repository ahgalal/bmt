package utils.video.input;

import utils.video.FrameIntArray;

public interface VidInputter
{
	boolean initialize(FrameIntArray frame_data, int width, int height, int cam_index);

	boolean startStream();

	void stopModule();

	void setFormat(String s);

	int getStatus();

	int getNumCams();

	int displayMoreSettings();

	String getName();
	// public void setCamIndex(int selectionIndex);
}
