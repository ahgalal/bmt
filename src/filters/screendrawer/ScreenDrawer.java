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

import sys.utils.Utils;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;
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
	public static final String ID = "filters.screendrawer";

	/**
	 * Runnable for drawing the incoming data on the Graphics objects.
	 * 
	 * @author Creative
	 */
	private class RunnableDrawer implements Runnable {
		@Override
		public void run() {
			try {
				Utils.sleep(100);

				while (configs.isEnabled()) {
					if (linkIn.getData() != null) {
						Utils.sleep(1000 / configs.getCommonConfigs()
								.getFrameRate());

						System.arraycopy(linkIn.getData(), 0, dataScreen, 0,
								linkIn.getData().length);

						// draw main screen
						int canvasWidth = configs.getCanvasDims().x;
						int canvasHeight = configs.getCanvasDims().y;

						if (canvasWidth != configs.getCommonConfigs()
								.getWidth()
								|| canvasHeight != configs.getCommonConfigs()
										.getHeight()) {

							configs.getRefGfxScreen().drawImage(
									bufImg.getScaledInstance(canvasWidth,
											canvasHeight, Image.SCALE_DEFAULT),
									0, 0,
									configs.getCommonConfigs().getWidth(),
									configs.getCommonConfigs().getHeight(), 0,
									0, configs.getCommonConfigs().getWidth(),
									configs.getCommonConfigs().getHeight(),
									null);
						} else {
							configs.getRefGfxScreen().drawImage(bufImg, 0, 0,
									configs.getCommonConfigs().getWidth(),
									configs.getCommonConfigs().getHeight(), 0,
									0, configs.getCommonConfigs().getWidth(),
									configs.getCommonConfigs().getHeight(),
									null);
						}
						// draw zones
						configs.getShapeController().drawAllShapes(
								configs.getRefGfxScreen());
						
						// draw frame rate on main screen
						configs.getRefGfxScreen().setColor(Color.GREEN);
						try {

							configs.getRefGfxScreen()
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

	private BufferedImage	bufImg;

	private int[]			dataScreen;
	private final int[]		frameInterval	= new int[3];
	private long			frameTimeStamp;

	private Thread			thDrawer;

	/**
	 * Initializes the filter.
	 * 
	 * @param name
	 *            filter's name
	 * @param linkIn
	 *            input Link for the filter
	 * @param linkOut
	 *            output Link from the filter
	 */
	public ScreenDrawer(final String name, final Link linkIn,
			final Link linkOut) {
		super(name, linkIn, linkOut);
		frameInterval[0] = 1;
		frameInterval[1] = 1;
		frameInterval[2] = 1;
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		this.configs = (ScreenDrawerConfigs) configs;

		bufImg = new BufferedImage(configs.getCommonConfigs().getWidth(),
				configs.getCommonConfigs().getHeight(), BufferedImage.TYPE_INT_RGB);
		dataScreen = ((DataBufferInt) bufImg.getRaster()
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
/*			System.arraycopy(linkIn2.getData(), 0, dataSecScreen, 0,
					linkIn2.getData().length);*/
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
	
	@Override
	public String getID() {
		return ID;
	}

	@Override
	public VideoFilter<?, ?> newInstance(String filterName) {
		return new ScreenDrawer(filterName, null, null);
	}

	@Override
	public void registerDependentData(FilterData data) {
		// TODO Auto-generated method stub
		
	}

}
