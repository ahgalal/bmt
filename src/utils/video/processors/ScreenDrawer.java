package utils.video.processors;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.FrameIntArray;
import utils.video.input.JMFModule;
import utils.video.input.VidInputter;
import control.ShapeController;

public class ScreenDrawer extends VideoFilter{

	private Graphics gfx_main_screen;
	private VidInputter v_in;
	private boolean drawing_enabled;
	private int frame_rate;
	private FrameIntArray fia;
	private boolean enable_sec_screen;
	private Graphics gfx_sec_screen;
	private BufferedImage buf_img_main,buf_img_sec;
	private int width;
	private int height;
	private int[] data_main_screen,data_sec_screen;
	private ShapeController shape_controller;
	@SuppressWarnings("unchecked")
	private Class cls_lib_usd;
	private PManager pm;

	public ScreenDrawer(Graphics gfxMainScreen,Graphics gfxSecScreen,int frameRate,
			FrameIntArray fia, int width, int height,VidInputter v_in)
	{
		pm=PManager.getDefault();
		this.v_in=v_in;
		gfx_main_screen = gfxMainScreen;
		gfx_sec_screen = gfxSecScreen;
		frame_rate = frameRate;
		this.fia = fia;

		this.width = width;
		this.height = height;


		buf_img_main = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		data_main_screen=((DataBufferInt)buf_img_main.getRaster().getDataBuffer()).getData();

		buf_img_sec = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		data_sec_screen=((DataBufferInt)buf_img_sec.getRaster().getDataBuffer()).getData();

		cls_lib_usd=v_in.getClass();
		shape_controller=ShapeController.getDefault();
	}

	public void updateImageData(int[] processed_img)
	{

	}

	public void start()
	{
		Thread th_drawer = new Thread(new RunnableDrawer());
		drawing_enabled=true;
		th_drawer.start();
	}

	public void stop()
	{
		drawing_enabled=false;
	}


	private class RunnableDrawer implements Runnable
	{
		@Override
		public void run() {
			try {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				while(v_in.getStatus()!=1)
				{
					Thread.sleep(1000);
					pm.log.print("Device not ready yet .. osbor showayya! =)",this);
				}
				while(drawing_enabled)
				{
					shape_controller.drawaAllShapes(gfx_main_screen);
					try {
						Thread.sleep(1000/frame_rate);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if(fia.frame_data!=null)
					{
						System.arraycopy(fia.frame_data, 0, data_main_screen, 0, fia.frame_data.length);

						if(cls_lib_usd==JMFModule.class){
							gfx_main_screen.drawImage(buf_img_main, 0, 0, width, height, 0, height, width, 0,null);
							if(enable_sec_screen)
								gfx_sec_screen.drawImage(buf_img_sec.getScaledInstance(289, 214, Image.SCALE_DEFAULT), 0, 0, 289, 214, 0, 214, 289, 0,null);
						}
						else{
							gfx_main_screen.drawImage(buf_img_main, 0, 0, width, height, 0, 0, width, height,null);
							if(enable_sec_screen)
								gfx_sec_screen.drawImage(buf_img_sec.getScaledInstance(289, 214, Image.SCALE_DEFAULT), 0, 0, width, height, 0, 0, width, height,null);
						}
					}
					else
						pm.log.print("got non-ready state from cam module! .. skipping frame",this);
				}
			} catch (Exception e) {
				pm.log.print("Error in th_drawer!!!!",this,StatusSeverity.ERROR);
				e.printStackTrace();
			}
		}
	}


	public void enableSecScreen(boolean en) {
		enable_sec_screen=en;
	}

	/* (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public int[] process(int[] imageData) {
		if(enabled) 
			System.arraycopy(imageData, 0, data_sec_screen, 0, imageData.length);
		return imageData;
	}
}
