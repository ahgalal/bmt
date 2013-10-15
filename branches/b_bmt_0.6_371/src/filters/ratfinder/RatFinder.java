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
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

import utils.PManager.ProgramState;
import utils.video.ImageManipulator.CentroidFinder;
import filters.FilterConfigs;
import filters.FilterData;
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

	public static final String			ID					= "filters.ratfinder";
	private static final int			PATH_QUEUE_LENGTH	= 100;

	protected final Point				centerPoint;

	protected final CentroidFinder		centroidFinder;

	private int							height;

	protected Marker					marker, marker2;

	protected int[]						outData;
	private Graphics					outputGraphics;
	private BufferedImage				outputImage;

	protected ArrayBlockingQueue<Point>	pathQueue;

	private int							width;

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
		centroidFinder = new CentroidFinder();
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		this.configs = (RatFinderFilterConfigs) configs;

		width = configs.getCommonConfigs().getWidth();
		height = configs.getCommonConfigs().getHeight();

		outputImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		outData = ((DataBufferInt) outputImage.getRaster().getDataBuffer())
				.getData();
		outputGraphics = outputImage.getGraphics();

		this.linkOut.setData(outData);
		specialConfiguration(configs);

		marker = new CrossMarker(50, 50, 5, Color.RED, width, height);

		marker2 = new RectangularMarker(width, height, width / 4, width / 4,
				Color.RED);
		pathQueue = new ArrayBlockingQueue<Point>(PATH_QUEUE_LENGTH);

		return super.configure(configs);
	}

	/**
	 * Draws a cross at the center of the moving object.
	 */
	protected void drawMarkerOnImg() {
		try {
			marker.draw(outData, centerPoint.x, centerPoint.y);
			marker2.draw(outData, centerPoint.x - (width / 8), centerPoint.y
					- (width / 8));
		} catch (final Exception e) {
			System.err.print("Error in marker");
			e.printStackTrace();
		}
	}

	private void drawPathOnImg() {
		if (centroidFinder.isStableCentroid()) {
			// update path queue
			if (pathQueue.size() < PATH_QUEUE_LENGTH) {
				pathQueue.add(new Point(centerPoint.x, centerPoint.y));
			} else {
				final Point head = pathQueue.poll();
				head.x = centerPoint.x;
				head.y = centerPoint.y;
				pathQueue.add(head); // added to tail
			}

			final Point prevPt = new Point(-1, -1);
			// draw path on image
			for (final Iterator<Point> it = pathQueue.iterator(); it.hasNext();) {
				final Point pt = it.next();

				if (prevPt.x != -1) {

					outputGraphics.setColor(Color.RED);
					try {
						outputGraphics.drawLine(pt.x - 1, pt.y - 1,
								prevPt.x - 1, prevPt.y - 1);
						outputGraphics.drawLine(pt.x, pt.y, prevPt.x, prevPt.y);
						outputGraphics.drawLine(pt.x + 1, pt.y + 1,
								prevPt.x + 1, prevPt.y + 1);
					} catch (final Exception e) {
						System.err
								.println("Error in path drawing, index out of range");
						e.printStackTrace();
					}

				} else {
					outputGraphics.drawLine(pt.x, pt.y, pt.x, pt.y);
				}

				prevPt.x = pt.x;
				prevPt.y = pt.y;
			}
		}
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public VideoFilter<?, ?> newInstance(final String filterName) {
		return new RatFinder(filterName, null, null);
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoUtility#process(int[])
	 */
	@Override
	public void process() {
		if (configs.isEnabled()) {
			// final long t1 = System.currentTimeMillis();
			final int[] data = linkIn.getData();
			centroidFinder.updateCentroid(data, centerPoint);
			System.arraycopy(data, 0, outData, 0, data.length);
			// final long t1 = System.currentTimeMillis();
			drawMarkerOnImg();
			// final long t2 = System.currentTimeMillis();
			drawPathOnImg();
			// System.out.println(t2-t1);
		}
	}

	@Override
	public void registerDependentData(final FilterData data) {

	}

	protected void specialConfiguration(final FilterConfigs configs) {
		centroidFinder.initialize(width, height);
	}

	@Override
	public void updateProgramState(final ProgramState state) {
		// TODO Auto-generated method stub

	}
}
