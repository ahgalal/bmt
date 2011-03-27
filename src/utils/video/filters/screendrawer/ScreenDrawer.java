package utils.video.filters.screendrawer;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.FilterConfigs;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;

public class ScreenDrawer extends VideoFilter
{

	private final BufferedImage buf_img_main, buf_img_sec;
	private final int[] data_main_screen, data_sec_screen;
	private final ScreenDrawerConfigs scrn_drwr_cnfgs;
	private final Link link_in2;

	public ScreenDrawer(
			final String name,
			final FilterConfigs configs,
			final Link link_in,
			final Link link_in2,
			final Link link_out)
	{
		super(name, configs, link_in, link_out);
		scrn_drwr_cnfgs = (ScreenDrawerConfigs) configs;

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
		this.link_in2 = link_in2;
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

				while (configs.enabled)
				{
					scrn_drwr_cnfgs.shape_controller.drawaAllShapes(scrn_drwr_cnfgs.ref_gfx_main_screen);
					try
					{
						Thread.sleep(1000 / scrn_drwr_cnfgs.common_configs.frame_rate);
					} catch (final InterruptedException e)
					{
						e.printStackTrace();
					}

					if (link_in.getData() != null)
					{
						System.arraycopy(
								link_in.getData(),
								0,
								data_main_screen,
								0,
								link_in.getData().length);

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
	public void process()
	{
		if (configs.enabled)
			System.arraycopy(
					link_in2.getData(),
					0,
					data_sec_screen,
					0,
					link_in2.getData().length);
	}

	@Override
	public boolean initialize()
	{
		return true;
	}
}
