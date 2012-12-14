package utils.video.input;

public class VidSourceConfigs {
	private int	width;
	private int	height;
	private int	camIndex;
	private String videoFilePath;
	public void setWidth(int width) {
		this.width = width;
	}
	public int getWidth() {
		return width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getHeight() {
		return height;
	}
	public void setCamIndex(int camIndex) {
		this.camIndex = camIndex;
	}
	public int getCamIndex() {
		return camIndex;
	}
	public void setVideoFilePath(String videoFilePath) {
		this.videoFilePath = videoFilePath;
	}
	public String getVideoFilePath() {
		return videoFilePath;
	}
}
