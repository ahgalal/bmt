package utils.video.input;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.FrameIntArray;
import cam_lib.JAGCamLib;
import cam_lib.ReturnValue;

public class AGCamLibModule  implements VidInputter {

	private JAGCamLib ag_cam;
	@SuppressWarnings("unused")
	private int width,height,cam_index,status;
	private FrameIntArray fia;
	private Thread th_update_image;
	private boolean stop_stream;

	@Override
	public boolean StartStream() {
		if(ag_cam.start()==ReturnValue.SUCCESS)
		{
		th_update_image=new Thread(new RunnableAGCamLib());
		th_update_image.start();

		return true;
		}
		else
		{
			PManager.log.print("Error Starting the Webcam!", this, StatusSeverity.ERROR);
			return false;
		}
	}

	@Override
	public void StopModule() {
		stop_stream=true;
		try {
			Thread.sleep(15);
		} catch (InterruptedException e) {e.printStackTrace();}

		ag_cam.stop();
		ag_cam.deInitialize();
		ag_cam = null;
		th_update_image=null;

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
		if(ag_cam==null)
			ag_cam=new JAGCamLib();
		this.width=width;
		this.height=height;
		fia=frameData;
		this.cam_index=camIndex;
		
		if(ag_cam.initialize(width, height,camIndex)==ReturnValue.SUCCESS)
		return true;
		else
		{
			PManager.log.print("Error initializing the Webcam!", this,StatusSeverity.ERROR);
			return false;
		}
	}

	@Override
	public void setFormat(String s) {
		/**
		 *Empty ... AGCamLib encapsulates the video format .. and returns an RGB
		 *integer array to the VideoProcessor .. just like OpenCV!
		 */
	}

	private class RunnableAGCamLib implements Runnable
	{
		@Override
		public void run() {
			while(!stop_stream & th_update_image!=null)
			{
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {e.printStackTrace();}

				fia.frame_data=ag_cam.grabIntRGBFrame();

				status=1;
			}
		}

	}

	@Override
	public int displayMoreSettings() {
		return 0;
	}

	@Override
	public String getName() {
		return "AGCamLib";
	}

}
