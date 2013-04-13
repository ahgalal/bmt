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

package filters.ratfinder;

import java.awt.Color;
import java.awt.Point;

import utils.PManager.ProgramState;
import filters.FilterConfigs;
import filters.Link;
import filters.VideoFilter;
import filters.ratfinder.markers.CrossMarker;
import filters.ratfinder.markers.Marker;
import filters.ratfinder.markers.RectangularMarker;

/**
 * Finds the moving object's position.
 * 
 * @author Creative
 */
public class RatFinder extends
		VideoFilter<RatFinderFilterConfigs, RatFinderData> {
	protected final Point	centerPoint;

	private final Point[]	centroidHistory							= new Point[2];
	private int				framesRemainingToEnableCentroidHistory	= 10;

	private int				height;
	private int[]			horiSum;

	protected Marker		marker, marker2;
	protected int[]			outData;
	private final int		searchSideLength						= 600;

	private int				tmpMax;

	private int[]			vertSum;
	private int				width;

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
	public RatFinder(final String name, final Link linkIn, final Link linkOut) {
		super(name, linkIn, linkOut);
		filterData = new RatFinderData();
		centerPoint = filterData.getCenterPoint();
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		this.configs = (RatFinderFilterConfigs) configs;

		marker = new CrossMarker(50, 50, 5, Color.RED, configs
				.getCommonConfigs().getWidth(), configs.getCommonConfigs()
				.getHeight());

		marker2 = new RectangularMarker(configs.getCommonConfigs().getWidth(),
				configs.getCommonConfigs().getHeight(), searchSideLength,
				searchSideLength, Color.RED);

		for (int i = 0; i < centroidHistory.length; i++)
			centroidHistory[i] = new Point(-1, -1);
		framesRemainingToEnableCentroidHistory = 10;

		// super's stuff:
		outData = new int[configs.getCommonConfigs().getWidth()
				* configs.getCommonConfigs().getHeight()];
		this.linkOut.setData(outData);
		specialConfiguration(configs);
		width = configs.getCommonConfigs().getWidth();
		height = configs.getCommonConfigs().getHeight();
		return super.configure(configs);
	}

	/**
	 * Draws a cross at the center of the moving object.
	 * 
	 * @param binaryImage
	 *            image to draw the cross on
	 */
	protected void drawMarkerOnImg(final int[] binaryImage) {
		System.arraycopy(binaryImage, 0, outData, 0, binaryImage.length);

		try {
			marker.draw(outData, centerPoint.x, centerPoint.y);
			marker2.draw(outData, centerPoint.x - searchSideLength / 2,
					centerPoint.y - searchSideLength / 2);
		} catch (final Exception e) {
			System.err.print("Error in marker");
			e.printStackTrace();
		}
	}

	private void lowPassFilterCentroidPosition() {

		// history remains disabled till ex:10 frames elapse, this is to ensure
		// the reliability of the centroid position (after ex:10 frames)
		if (framesRemainingToEnableCentroidHistory == 0) {
			if (centroidHistory[0].x == -1) { // history is not initialized yet
				for (int i = 0; i < centroidHistory.length - 1; i++) {
					centroidHistory[i].x = centerPoint.x;
					centroidHistory[i].y = centerPoint.y;
				}
			} else {
				int sumX = 0, sumY = 0;
				for (final Point p : centroidHistory) {
					sumX += p.x;
					sumY += p.y;
				}
				final int factor = 5;
				centerPoint.x = (centerPoint.x * factor + sumX)
						/ (centroidHistory.length + factor);
				centerPoint.y = (centerPoint.y * factor + sumY)
						/ (centroidHistory.length + factor);

				// update history
				for (int i = 0; i < centroidHistory.length - 1; i++) {
					centroidHistory[i].x = centroidHistory[i + 1].x;
					centroidHistory[i].y = centroidHistory[i + 1].y;
				}

				centroidHistory[centroidHistory.length - 1].x = centerPoint.x;
				centroidHistory[centroidHistory.length - 1].y = centerPoint.y;
			}
		} else
			framesRemainingToEnableCentroidHistory--;
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoUtility#process(int[])
	 */
	@Override
	public void process() {
		if (configs.isEnabled()) {
			//final long t1 = System.currentTimeMillis();
			updateCentroid(linkIn.getData());
			drawMarkerOnImg(linkIn.getData());
			//final long t2 = System.currentTimeMillis();
			// System.out.println(t2-t1);
		}
	}

	protected void specialConfiguration(final FilterConfigs configs) {
		horiSum = new int[configs.getCommonConfigs().getHeight()];
		vertSum = new int[configs.getCommonConfigs().getWidth()];
	}

	/**
	 * Updates the center point (ie: finds the location of the moving object).
	 * 
	 * @param binaryImage
	 *            input image
	 */
	protected void updateCentroid(final int[] binaryImage) {
		final int smallestWhiteAreaSize = 10;
		tmpMax = smallestWhiteAreaSize;

		int y1, y2;

		if (centerPoint.y == 0) {
			y1 = 0;
			y2 = height;
		} else {
			y1 = (centerPoint.y - searchSideLength) < 0 ? 0
					: centerPoint.y - 40;
			y2 = (centerPoint.y + searchSideLength) > height ? height
					: centerPoint.y + searchSideLength;
		}

		for (int y = y1; y < y2; y++) { // Horizontal Sum
			horiSum[y] = 0;
			for (int x = 0; x < width; x++)
				horiSum[y] += binaryImage[y * width + x] & 0xff;
			if (horiSum[y] > tmpMax) {
				centerPoint.y = y;
				tmpMax = horiSum[y];
			}
		}

		tmpMax = smallestWhiteAreaSize;

		int x1, x2;
		if (centerPoint.x == 0) {
			x1 = 0;
			x2 = width;
		} else {
			x1 = (centerPoint.x - searchSideLength) < 0 ? 0
					: centerPoint.x - 40;
			x2 = (centerPoint.x + searchSideLength) > width ? width
					: centerPoint.x + searchSideLength;
		}

		for (int x = x1; x < x2; x++) { // Vertical Sum
			vertSum[x] = 0;
			for (int y = 0; y < height; y++)
				vertSum[x] += binaryImage[y * width + x] & 0xff;
			if (vertSum[x] > tmpMax) {
				centerPoint.x = x;
				tmpMax = vertSum[x];
			}
		}

		// low pass filter on position
		lowPassFilterCentroidPosition();
	}

	@Override
	public void updateProgramState(final ProgramState state) {
		// TODO Auto-generated method stub

	}

}
