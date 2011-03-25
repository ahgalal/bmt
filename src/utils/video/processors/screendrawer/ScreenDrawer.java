package utils.video.processors.screendrawer;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.input.JMFModule;
import utils.video.processors.FilterConfigs;
import utils.video.processors.VideoFilter;
import control.ShapeController;

public class ScreenDrawer extends VideoFilter
{

	private final BufferedImage buf_img_main, buf_img_sec;
	private final int[] data_main_screen, data_sec_screen;
	private final ShapeController shape_controller;
	@SuppressWarnings("unchecked")
	private final Class cls_lib_usd;
	private final ScreenDrawerConfigs scrn_drwr_cnfgs;

	public ScreenDrawer(final String name, final FilterConfigs configs)
	{
		super(name, configs);
		scrn_drwr_cnfgs = (ScreenDrawerConfigs) configs;

		shape_controller = ShapeController.getDefault();

		buf_img_main = new BufferedImage(
				scrn_drwr_cnfgs.common_configs.width,
				scrn_drwr_cnfgs.common_configs.height,
				BufferedImage.TYPE_INT_RGB);
		data_main_screen = ((DataBufferInt) buf_img_main.getRaster().getDataBuffer()).getData();

		buf_img_sec = new BufferedImage(
				scrn_drwr_cnfgs.common_configs.width,
				scrn_drwr_cnfgs.common_configs.height,
				BufferedImage.TYPE_INT_RGB);
		data_sec_screen = ((DataBufferInt) buf_img_sec.getRaster().getDataBuffer()).getData();

		cls_lib_usd = scrn_drwr_cnfgs.v_in.getClass();
	}

	@Override
	public boolean enable(final boolean enable)
	{
		if (enable)
		{
			final Thread th_drawer = new Thread(new RunnableDrawer());
			th_drawer.start();
		}
		return super.enable(enable);
	}

	private class RunnableDrawer implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				try
				{
					Thread.sleep(100);
				} catch (final InterruptedException e1)
				{
					e1.printStackTrace();
				}
				while (scrn_drwr_cnfgs.v_in.getStatus() != 1)
				{
					Thread.sleep(1000);
					PManager.log.print("Device not ready yet .. osbor showayya! =)", this);
				}
				while (scrn_drwr_cnfgs.enabled)
				{
					shape_controller.drawaAllShapes(scrn_drwr_cnfgs.ref_gfx_main_screen);
					try
					{
						Thread.sleep(1000 / scrn_drwr_cnfgs.common_configs.frame_rate);
					} catch (final InterruptedException e)
					{
						e.printStackTrace();
					}

					if (scrn_drwr_cnfgs.ref_fia.frame_data != null)
					{
						System.arraycopy(
								scrn_drwr_cnfgs.ref_fia.frame_data,
								0,
								data_main_screen,
								0,
								scrn_drwr_cnfgs.ref_fia.frame_data.length);

						if (cls_lib_usd == JMFModule.class)
						{
							scrn_drwr_cnfgs.ref_gfx_main_screen.drawImage(
									buf_img_main,
									0,
									0,
									scrn_drwr_cnfgs.common_configs.width,
									scrn_drwr_cnfgs.common_configs.height,
									0,
									scrn_drwr_cnfgs.common_configs.height,
									scrn_drwr_cnfgs.common_configs.width,
									0,
									null);
							if (scrn_drwr_cnfgs.enable_sec_screen)
								scrn_drwr_cnfgs.ref_gfx_sec_screen.drawImage(
										buf_img_sec.getScaledInstance(
												289,
												214,
												Image.SCALE_DEFAULT),
										0,
										0,
										289,
										214,
										0,
										214,
										289,
										0,
										null);
						} else
						{
							scrn_drwr_cnfgs.ref_gfx_main_screen.drawImage(
									buf_img_main,
									0,
									0,
									scrn_drwr_cnfgs.common_configs.width,
									scrn_drwr_cnfgs.common_configs.height,
									0,
									0,
									scrn_drwr_cnfgs.common_configs.width,
									scrn_drwr_cnfgs.common_configs.height,
									null);
							if (scrn_drwr_cnfgs.enable_sec_screen)
								scrn_drwr_cnfgs.ref_gfx_sec_screen.drawImage(
										buf_img_sec.getScaledInstance(
												289,
												214,
												Image.SCALE_DEFAULT),
										0,
										0,
										scrn_drwr_cnfgs.common_configs.width,
										scrn_drwr_cnfgs.common_configs.height,
										0,
										0,
										scrn_drwr_cnfgs.common_configs.width,
										scrn_drwr_cnfgs.common_configs.height,
										null);
						}
					} else
						PManager.log.print(
								"got non-ready state from cam module! .. skipping frame",
								this);
				}
			} catch (final Exception e)
			{
				PManager.log.print("Error in th_drawer!!!!", this, StatusSeverity.ERROR);
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public int[] process(final int[] imageData)
	{
		if (configs.enabled)
			System.arraycopy(imageData, 0, data_sec_screen, 0, imageData.length);
		return imageData;
	}

	@Override
	public boolean initialize()
	{
		return true;
	}
}
