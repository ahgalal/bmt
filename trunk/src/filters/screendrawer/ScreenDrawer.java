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

package filters.screendrawer;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;
import utils.Utils;
import filters.FilterConfigs;
import filters.FilterData;
import filters.Link;
import filters.VideoFilter;

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
			int waitCount = 0;
			while ((configs.getRefGfxSecScreen() == null)
					|| (configs.getRefGfxSecScreen() == null))
				Utils.sleep(100);
			waitCount++;
			if (waitCount == 10)
				PManager.log.print("Drawing Screen is NULL!", this,
						StatusSeverity.ERROR);

			try {
				Utils.sleep(100);

				while (configs.isEnabled()) {
					configs.getShapeController()
							.drawaAllShapes(configs.getRefGfxMainScreen());
					Utils.sleep(1000 / configs.getCommonConfigs().getFrameRate());

					if (linkIn.getData() != null) {
						
						System.arraycopy(linkIn.getData(), 0,
								dataMainScreen, 0, linkIn.getData().length);

						configs.getRefGfxMainScreen().drawImage(bufImgMain, 0,
								0, configs.getCommonConfigs().getWidth(),
								configs.getCommonConfigs().getHeight(), 0, 0,
								configs.getCommonConfigs().getWidth(),
								configs.getCommonConfigs().getHeight(), null);
						configs.getRefGfxMainScreen().setColor(Color.GREEN);
						try {
							configs.getRefGfxMainScreen()
									.drawString(
											"FPS="
													+ 3000
													/ (frameInterval[0]
															+ frameInterval[1] + frameInterval[2]),
											configs.getCommonConfigs().getWidth() - 60,
											configs.getCommonConfigs().getHeight() - 10);
						} catch (final Exception e) {
							e.printStackTrace();
						}
						if (configs.isEnableSecScreen())
							configs.getRefGfxSecScreen().drawImage(bufImgSec
									.getScaledInstance(289, 214,
											Image.SCALE_DEFAULT), 0, 0,
									configs.getCommonConfigs().getWidth(),
									configs.getCommonConfigs().getHeight(), 0, 0,
									configs.getCommonConfigs().getWidth(),
									configs.getCommonConfigs().getHeight(), null);
					} else
						PManager.log.print("invalid frame! .. skipping!", this);
				}
			} catch (final Exception e) {
				PManager.log.print("Error in thDrawer!!!!", this,
						StatusSeverity.ERROR);
				e.printStackTrace();
			}
		}
	}

	private BufferedImage	bufImgMain;

	private BufferedImage	bufImgSec;

	private int[]			dataMainScreen;
	private int[]			dataSecScreen;
	private final int[]		frameInterval	= new int[3];
	private long			frameTimeStamp;
	private final Link		linkIn2;

	private Thread			thDrawer;

	/**
	 * Initializes the filter.
	 * 
	 * @param name
	 *            filter's name
	 * @param linkIn
	 *            main input Link for the filter
	 * @param linkIn2
	 *            secondary input Link for the filter
	 * @param linkOut
	 *            output Link from the filter
	 */
	public ScreenDrawer(final String name, final Link linkIn,
			final Link linkIn2, final Link linkOut) {
		super(name, linkIn, linkOut);
		this.linkIn2 = linkIn2;
		frameInterval[0] = 1;
		frameInterval[1] = 1;
		frameInterval[2] = 1;
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		this.configs = (ScreenDrawerConfigs) configs;

		bufImgMain = new BufferedImage(configs.getCommonConfigs().getWidth(),
				configs.getCommonConfigs().getHeight(), BufferedImage.TYPE_INT_RGB);
		dataMainScreen = ((DataBufferInt) bufImgMain.getRaster()
				.getDataBuffer()).getData();

		bufImgSec = new BufferedImage(configs.getCommonConfigs().getWidth(),
				configs.getCommonConfigs().getHeight(), BufferedImage.TYPE_INT_RGB);
		dataSecScreen = ((DataBufferInt) bufImgSec.getRaster()
				.getDataBuffer()).getData();
		return super.configure(configs);
	}

	@Override
	public boolean enable(final boolean enable) {
		if (enable) {
			thDrawer = new Thread(new RunnableDrawer(),"Screen drawer");
			thDrawer.start();
		}
		return super.enable(enable);
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public void process() {
		if (configs.isEnabled()) {
			System.arraycopy(linkIn2.getData(), 0, dataSecScreen, 0,
					linkIn2.getData().length);
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
