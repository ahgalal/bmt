package utils.video.input;



import hypermedia.video.OpenCV;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


import utils.video.FrameIntArray;
import utils.video.VideoProcessor;




public class OpenCVModule implements Runnable,VidInputter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int status;
	private int cam_index=0;

	// program execution frame rate (millisecond)
	final int FRAME_RATE  = 1000/30;

	private FrameIntArray fia;
	private int width,height;
	OpenCV cv = null;	// OpenCV Object
	Thread t  = null;	// the sample thread
	BufferedImage b_img;
	int data[];
	Graphics g;
	VideoProcessor vp;
	// the input video stream image
	Image frame	= null;
	// list of all face detected area
	Rectangle[] squares = new Rectangle[0];

	/**
	 * Setup Frame and Object(s).
	 */
	public OpenCVModule() {



	}

	@Override
	public void run() {
		cv.capture( width, height,cam_index);
		while( t!=null && cv!=null ) {

			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cv.read();

			//img.setRGB(0, 0, 640, 480, cv.pixels(), 0, 640);

			//System.arraycopy(cv.pixels(), 0, data, 0, cv.pixels().length);
			//awt_frame.getGraphics().drawImage(img, 0, 0, 640, 480, 0, 0, 640, 480, null);
			//g.drawImage(img, 0, 0, null);
			long t1 = System.currentTimeMillis();
			//MemoryImageSource mis = new MemoryImageSource( cv.width, cv.height, cv.pixels(), 0, cv.width );
			int f[]=cv.pixels();
			/*			for(int i=0;i<f.length;i++)
				data[i]=f[i];*/
			/*			if(captute_bg)
			{
				vp.setBg_image(f);
				captute_bg=false;
			}
			 */

			fia.frame_data = f;

			/*			if(start)
				f=vp.camImage2ProcessedImage(f);
			System.arraycopy(f, 0, data, 0, f.length);*/
			long t2 = System.currentTimeMillis();



			//long t3 = System.currentTimeMillis();
			//g.drawImage(b_img, 0, 0, null);
			//b_img.setRGB(0, 0, 640, 480, f, 0, 640);
			//long t4 = System.currentTimeMillis();


			if(fia.frame_data!=null)
			{
				status=1;
				System.out.print(Long.toString(t2-t1) + "\t" +fia.frame_data[50] + "\n");//+
			}
			/*Long.toString(t3-t2) + "\n"+
					Long.toString(t4-t3) +*/// "\n\n");
			//System.out.print(fia.frame_data[50]);
			/*			Image i=null;
			buf.setData(cv.pixels());
			i= buf2img.createImage(buf);
			awt_frame.getGraphics().drawImage(i, 0, 0, null);*/
			if(fia.frame_data!=null)
				status=1;
		}

	}


	@Override
	public boolean StartStream() {
		if(t==null)
			t = new Thread(this);
		t.start();
		return true;
	}

	@Override
	public void StopModule() {
		t=null;
		cv.stop();
		cv.dispose();
		//cv=null;
	}


	public void setWidth(int parseInt) {
		width=parseInt;

	}

	public void setHeight(int parseInt) {
		height=parseInt;

	}



	public int getNumberOfCams() {
		return 1;
	}


	@Override
	public boolean initialize(FrameIntArray fia, int width, int height,int cam_index) {
		this.fia=fia;
		this.width=width;
		this.height = height;
		this.cam_index=cam_index;
		cv = new OpenCV();
		//cv.capture( width, height,cam_index);
		return true;
	}

	@Override
	public void setFormat(String s) {	
		//Empty because OpenCV encapsulates the format in itself and gives us an int[] array of RGB
	}

	@Override
	public int getStatus() {
		return status;		
	}

	@Override
	public int getNumCams() {
		return 0;
	}

	@Override
	public int displayMoreSettings() {
		//not supported for OpenCV
		return 0;
	}

	@Override
	public String getName() {
		return "OpenCV";
	}


}