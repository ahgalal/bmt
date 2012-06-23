/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package utils.video.filters.screendrawer;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.FilterConfigs;
import utils.video.filters.FilterData;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;

/**
 * Draws the incoming data streams on Graphics objects.
 * 
 * @author Creative
 */
public class ScreenDrawer extends VideoFilter<ScreenDrawerConfigs, FilterData> {
	/**
	 * Runnable for drawing the incoming data on the Graphics objects.
	 * 
	 * @author Creative
	 */
	private class RunnableDrawer implements Runnable {
		@Override
		public void run() {
			int wait_count = 0;
			while ((configs.ref_gfx_sec_screen == null)
					|| (configs.ref_gfx_sec_screen == null))
				try {
					Thread.sleep(100);
					wait_count++;
					if (wait_count == 10)
						PManager.log.print("Drawing Screen is NULL!", this,
								StatusSeverity.ERROR);
				} catch (final InterruptedException e2) {
					e2.printStackTrace();
				}

			try {
				try {
					Thread.sleep(100);
				} catch (final InterruptedException e1) {
					e1.printStackTrace();
				}

				while (configs.enabled) {
					configs.shape_controller
							.drawaAllShapes(configs.ref_gfx_main_screen);
					try {
						Thread.sleep(1000 / configs.common_configs.frame_rate);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}

					if (link_in.getData() != null) {
						System.arraycopy(link_in.getData(), 0,
								data_main_screen, 0, link_in.getData().length);

						configs.ref_gfx_main_screen.drawImage(buf_img_main, 0,
								0, configs.common_configs.width,
								configs.common_configs.height, 0, 0,
								configs.common_configs.width,
								configs.common_configs.height, null);
						configs.ref_gfx_main_screen.setColor(Color.GREEN);
						configs.ref_gfx_main_screen
								.drawString(
										"FPS="
												+ 3000
												/ (frameInterval[0]
														+ frameInterval[1] + frameInterval[2]),
										configs.common_configs.width - 60,
										configs.common_configs.height - 10);
						if (configs.enable_sec_screen)
							configs.ref_gfx_sec_screen.drawImage(buf_img_sec
									.getScaledInstance(289, 214,
											Image.SCALE_DEFAULT), 0, 0,
									configs.common_configs.width,
									configs.common_configs.height, 0, 0,
									configs.common_configs.width,
									configs.common_configs.height, null);
					} else
						PManager.log.print("invalid frame! .. skipping!", this);
				}
			} catch (final Exception e) {
				PManager.log.print("Error in th_drawer!!!!", this,
						StatusSeverity.ERROR);
				e.printStackTrace();
			}
		}
	}

	private BufferedImage	buf_img_main;

	private BufferedImage	buf_img_sec;

	private int[]			data_main_screen;
	private int[]			data_sec_screen;
	private final int[]		frameInterval	= new int[3];
	private long			frameTimeStamp;
	private final Link		link_in2;

	private Thread			th_drawer;

	/**
	 * Initializes the filter.
	 * 
	 * @param name
	 *            filter's name
	 * @param linkIn
	 *            main input Link for the filter
	 * @param link_in2
	 *            secondary input Link for the filter
	 * @param linkOut
	 *            output Link from the filter
	 */
	public ScreenDrawer(final String name, final Link linkIn,
			final Link link_in2, final Link linkOut) {
		super(name, linkIn, linkOut);
		this.link_in2 = link_in2;
		frameInterval[0] = 1;
		frameInterval[1] = 1;
		frameInterval[2] = 1;
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		this.configs = (ScreenDrawerConfigs) configs;

		buf_img_main = new BufferedImage(configs.common_configs.width,
				configs.common_configs.height, BufferedImage.TYPE_INT_RGB);
		data_main_screen = ((DataBufferInt) buf_img_main.getRaster()
				.getDataBuffer()).getData();

		buf_img_sec = new BufferedImage(configs.common_configs.width,
				configs.common_configs.height, BufferedImage.TYPE_INT_RGB);
		data_sec_screen = ((DataBufferInt) buf_img_sec.getRaster()
				.getDataBuffer()).getData();
		return super.configure(configs);
	}

	@Override
	public boolean enable(final boolean enable) {
		if (enable) {
			th_drawer = new Thread(new RunnableDrawer());
			th_drawer.start();
		}
		return super.enable(enable);
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public void process() {
		if (configs.enabled) {
			System.arraycopy(link_in2.getData(), 0, data_sec_screen, 0,
					link_in2.getData().length);
			frameInterval[2] = frameInterval[1];
			frameInterval[1] = frameInterval[0];
			frameInterval[0] = (int) (System.currentTimeMillis() - frameTimeStamp);
			frameTimeStamp = System.currentTimeMillis();
		}
	}

	@Override
	public void updateProgramState(final ProgramState state) {
		// TODO Auto-generated method stub

	}

}
