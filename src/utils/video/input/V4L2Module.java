package utils.video.input;

import java.util.List;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.FrameIntArray;
import utils.video.ImageManipulator;
import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.ImageFormat;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public class V4L2Module implements VidInputter, CaptureCallback {


	private VideoDevice vdevice;
	private FrameGrabber vfg;
	@SuppressWarnings("unused")
	private int width, height, cam_index, status;
	private FrameIntArray fia;
	/*private Thread th_update_image;*/
	private boolean stop_stream;

	@Override
	public boolean startStream()
	{
		
		try {
			vfg.startCapture();
		} catch (V4L4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (true/*ag_cam.start() == ReturnValue.SUCCESS*/)
		{
/*			th_update_image = new Thread(new RunnableAGCamLib());
			th_update_image.start();*/

			return true;
		}
		else
		{
			PManager.log.print("Error Starting the Webcam!", this, StatusSeverity.ERROR);
			return false;
		}
	}

	@Override
	public void stopModule()
	{
		stop_stream = true;
		try
		{
			Thread.sleep(15);
		} catch (final InterruptedException e)
		{
			e.printStackTrace();
		}

		vfg.stopCapture();
		vdevice.releaseFrameGrabber();
		
/*		ag_cam.stop();
		ag_cam.deInitialize();
		ag_cam = null;
*/		/*th_update_image = null;*/

	}

	@Override
	public int getNumCams()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatus()
	{
		return status;
	}

	@Override
	public boolean initialize(
			final FrameIntArray frameData,
			final int width,
			final int height,
			final int camIndex)
	{
		if (vdevice == null)
			try {
				vdevice = new VideoDevice("/dev/video"+camIndex);
			} catch (V4L4JException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		this.width = width;
		this.height = height;
		fia = frameData;
		this.cam_index = camIndex;

		try {
			List<ImageFormat> formats = vdevice.getDeviceInfo().getFormatList().getRGBEncodableFormats();
			vfg = vdevice.getRGBFrameGrabber(width, height, 0, 0,formats.get(0));
			vfg.setCaptureCallback(this);
			vfg.setFrameInterval(1, 30);
		} catch (V4L4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (vfg!=null)
			return true;
		else
		{
			PManager.log.print(
					"Error initializing the Webcam!",
					this,
					StatusSeverity.ERROR);
			return false;
		}
	}

	@Override
	public void setFormat(final String s)
	{
		/**
		 * Empty ... AGCamLib encapsulates the video format .. and returns an
		 * RGB integer array to the VideoManager .. just like OpenCV!
		 */
	}



	@Override
	public int displayMoreSettings()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return "V4L2";
	}

	@Override
	public void exceptionReceived(V4L4JException arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextFrame(VideoFrame vframe) {
/*		if (!stop_stream && th_update_image != null)
		{*/
/*			try
			{
				Thread.sleep(30);
			} catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
*/
			fia.frame_data = ImageManipulator.byteBGR2IntRGB(vframe.getBytes());

			status = 1;
			vframe.recycle();
		/*}*/
	}
	
	


}
