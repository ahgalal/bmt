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

package filters.rearingdetection;

import utils.PManager.ProgramState;
import filters.FilterConfigs;
import filters.Link;
import filters.VideoFilter;

/**
 * Detects whether the rat is rearing or not.
 * 
 * @author Creative
 */
public class RearingDetector extends
		VideoFilter<RearingFilterConfigs, RearingFilterData> {
	/**
	 * Runnable to calculate the mean rat area.
	 * 
	 * @author Creative
	 */
	private class NormalRatAreaThread implements Runnable {
		private static final long	ratAreaTrainingTime	= 3;

		@Override
		public void run() {
			final long timerStart = (System.currentTimeMillis() / 1000);
			int tmpRatAreaSum = 0, numSamples = 0;
			while (!rearingNow
					&& ((System.currentTimeMillis() / 1000) - timerStart < ratAreaTrainingTime)) {
				numSamples++;
				tmpRatAreaSum += currentRatArea;

				try {
					Thread.sleep(400);
				} catch (final InterruptedException e) {
				}
			}
			rearingNow = false;
			normalRatArea = tmpRatAreaSum / numSamples;
			System.out.print("normal rat area: " + normalRatArea + "\n");
		}
	}

	private int		currentRatArea;

	private boolean	isRearing;
	private int		normalRatArea;
	private boolean	rearingNow;

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
	public RearingDetector(final String name, final Link linkIn,
			final Link linkOut) {
		super(name, linkIn, linkOut);
		filterData = new RearingFilterData();

		// super's stuff:
		gui = new RearingDetectorGUI(this);
		filterData.setRearing(false);
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		this.configs = (RearingFilterConfigs) configs;
		return super.configure(configs);
	}

	@Override
	public void process() {
		final int[] imageData = linkIn.getData();
		if (configs.isEnabled())
			if (imageData != null) {
				int whiteArea = 0;
				for (int x = configs.getCenterPoint().x
						- (configs.getMarginX() / 2); x < configs.getCenterPoint().x
						+ (configs.getMarginX() / 2); x++)
					for (int y = configs.getCenterPoint().y
							- (configs.getMarginY() / 2); y < configs.getCenterPoint().y
							+ (configs.getMarginY() / 2); y++)
						if ((x < configs.getCommonConfigs().getWidth()) & (x >= 0)
								& (y < configs.getCommonConfigs().getHeight())
								& (y >= 0))
							if (imageData[x + y * configs.getCommonConfigs().getWidth()] == 0x00FFFFFF)
								whiteArea++;

				currentRatArea = whiteArea;
				if (currentRatArea < configs.getRearingThresh())
					isRearing = true;
				else
					isRearing = false;
			}
		filterData.setRearing(isRearing);
	}

	/**
	 * Used to train the filter of the white area of the rat when
	 * (walking/rearing).
	 * 
	 * @param rearing
	 *            whether the rat is rearing now or not
	 */
	public void rearingNow(final boolean rearing) {

		if (rearing) {
			rearingNow = true;
			configs.setRearingThresh((normalRatArea + currentRatArea) / 2);
			System.out.print("Rearing threshold: " + configs.getRearingThresh()
					+ "\n");
		} else {
			rearingNow = false;
			final Thread thRearing = new Thread(new NormalRatAreaThread());
			thRearing.start();
			System.out.print("Rearing Training Started" + "\n");
		}

	}

	@Override
	public void updateProgramState(final ProgramState state) {
		// TODO Auto-generated method stub

	}

}
