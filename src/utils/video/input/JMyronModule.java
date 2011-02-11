package utils.video.input;

import utils.video.FrameIntArray;
import JMyron.JMyron;

public class JMyronModule implements VidInputter {

	private JMyron jmyron;
	@SuppressWarnings("unused")
	private int width,height,cam_index,status;
	private FrameIntArray fia;
	private Thread th_jmyron_update_image;
	private boolean stop_stream;

	@Override
	public boolean StartStream() {
		jmyron.start(width, height);

		th_jmyron_update_image=new Thread(new RunnableJMyron());
		th_jmyron_update_image.start();

		return true;
	}

	@Override
	public void StopModule() {
		stop_stream=true;
		try {
			Thread.sleep(15);
		} catch (InterruptedException e) {e.printStackTrace();}

		jmyron.stop();
		jmyron = null;
		th_jmyron_update_image=null;

	}

	@Override
	public int getNumCams() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public boolean initialize(FrameIntArray frameData, int width, int height,int camIndex) {
		if(jmyron==null)
			jmyron=new JMyron();
		this.width=width;
		this.height=height;
		fia=frameData;
		this.cam_index=camIndex;

		return true;
	}

	@Override
	public void setFormat(String s) {
		/**
		 *Empty ... JMyron encapsulates the video format .. and returns an RGB
		 *integer array to the VideoProcessor .. just like OpenCV!
		 */
	}

	private class RunnableJMyron implements Runnable
	{
		@Override
		public void run() {
			while(!stop_stream & th_jmyron_update_image!=null)
			{
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {e.printStackTrace();}

				jmyron.update();
				fia.frame_data=jmyron.image();

				status=1;
			}
		}

	}

	@Override
	public int displayMoreSettings() {
		if(jmyron==null)
			jmyron=new JMyron();
		jmyron.settings();
		return 0;
	}

	@Override
	public String getName() {
		return "JMyron";
	}

}
