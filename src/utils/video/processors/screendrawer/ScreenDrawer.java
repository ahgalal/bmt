package utils.video.processors.screendrawer;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.input.JMFModule;
import utils.video.processors.VideoFilter;
import control.ShapeController;

public class ScreenDrawer extends VideoFilter{

	
	private BufferedImage buf_img_main,buf_img_sec;
	private int[] data_main_screen,data_sec_screen;
	private ShapeController shape_controller;
	@SuppressWarnings("unchecked")
	private Class cls_lib_usd;
	private PManager pm;
	private ScreenDrawerConfigs scrn_drwr_cnfgs;

	public ScreenDrawer(String name)
	{
		scrn_drwr_cnfgs= new ScreenDrawerConfigs(null,null,null,null,null,true);
		this.name=name;
		pm=PManager.getDefault();
		configs=scrn_drwr_cnfgs;

		shape_controller=ShapeController.getDefault();
	}

	public void updateImageData(int[] processed_img)
	{

	}

	@Override
	public boolean enable(boolean enable) {
		if(enable)
		{
			Thread th_drawer = new Thread(new RunnableDrawer());
			th_drawer.start();
		}
		return super.enable(enable);
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
				while(scrn_drwr_cnfgs.v_in.getStatus()!=1)
				{
					Thread.sleep(1000);
					pm.log.print("Device not ready yet .. osbor showayya! =)",this);
				}
				while(scrn_drwr_cnfgs.enabled)
				{
					shape_controller.drawaAllShapes(scrn_drwr_cnfgs.ref_gfx_main_screen);
					try {
						Thread.sleep(1000/scrn_drwr_cnfgs.common_configs.frame_rate);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if(scrn_drwr_cnfgs.ref_fia.frame_data!=null)
					{
						System.arraycopy(scrn_drwr_cnfgs.ref_fia.frame_data, 0, data_main_screen, 0, scrn_drwr_cnfgs.ref_fia.frame_data.length);

						if(cls_lib_usd==JMFModule.class){
							scrn_drwr_cnfgs.ref_gfx_main_screen.drawImage(buf_img_main, 0, 0, scrn_drwr_cnfgs.common_configs.width, scrn_drwr_cnfgs.common_configs.height, 0, scrn_drwr_cnfgs.common_configs.height, scrn_drwr_cnfgs.common_configs.width, 0,null);
							if(scrn_drwr_cnfgs.enable_sec_screen)
								scrn_drwr_cnfgs.ref_gfx_sec_screen.drawImage(buf_img_sec.getScaledInstance(289, 214, Image.SCALE_DEFAULT), 0, 0, 289, 214, 0, 214, 289, 0,null);
						}
						else{
							scrn_drwr_cnfgs.ref_gfx_main_screen.drawImage(buf_img_main, 0, 0, scrn_drwr_cnfgs.common_configs.width, scrn_drwr_cnfgs.common_configs.height, 0, 0, scrn_drwr_cnfgs.common_configs.width, scrn_drwr_cnfgs.common_configs.height,null);
							if(scrn_drwr_cnfgs.enable_sec_screen)
								scrn_drwr_cnfgs.ref_gfx_sec_screen.drawImage(buf_img_sec.getScaledInstance(289, 214, Image.SCALE_DEFAULT), 0, 0, scrn_drwr_cnfgs.common_configs.width, scrn_drwr_cnfgs.common_configs.height, 0, 0, scrn_drwr_cnfgs.common_configs.width, scrn_drwr_cnfgs.common_configs.height,null);
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

	/* (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public int[] process(int[] imageData) {
		if(configs.enabled) 
			System.arraycopy(imageData, 0, data_sec_screen, 0, imageData.length);
		return imageData;
	}

	@Override
	public boolean initialize() {
		buf_img_main = new BufferedImage(scrn_drwr_cnfgs.common_configs.width, scrn_drwr_cnfgs.common_configs.height, BufferedImage.TYPE_INT_RGB);
		data_main_screen=((DataBufferInt)buf_img_main.getRaster().getDataBuffer()).getData();

		buf_img_sec = new BufferedImage(scrn_drwr_cnfgs.common_configs.width, scrn_drwr_cnfgs.common_configs.height, BufferedImage.TYPE_INT_RGB);
		data_sec_screen=((DataBufferInt)buf_img_sec.getRaster().getDataBuffer()).getData();

		cls_lib_usd=scrn_drwr_cnfgs.v_in.getClass();
		return true;
	}
}
