/**
 * 
 */
package filters.headangle;

import java.awt.Point;

import utils.PManager.ProgramState;
import utils.video.ImageManipulator;
import utils.video.ImageManipulator.CentroidFinder;
import filters.CommonFilterConfigs;
import filters.FilterConfigs;
import filters.FilterData;
import filters.Link;
import filters.VideoFilter;

/**
 * @author Creative
 */
public class HeadAngleFilter extends
		VideoFilter<HeadAngleConfigs, HeadAngleData> {
	public static final String	ID	= "filters.headangle";
	private int[]				blankImageData;

	private Point				bodyPosition1;
	private Point				bodyPosition2;
	protected CentroidFinder	centroidFinder;

	private Point				earPosition1;

	private Point				earPosition2;

	private int[]				outputImageData;

	private int[]				tmpProcessedData;

	public HeadAngleFilter(final String name, final Link linkIn,
			final Link linkOut) {
		super(name, linkIn, linkOut);
		filterData = new HeadAngleData();
		centroidFinder = new CentroidFinder();
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		final CommonFilterConfigs commonConfigs = configs.getCommonConfigs();
		tmpProcessedData = new int[commonConfigs.getWidth()
				* commonConfigs.getHeight()];
		centroidFinder.initialize(commonConfigs.getWidth(),
				commonConfigs.getHeight());
		outputImageData = new int[commonConfigs.getWidth()
				* commonConfigs.getHeight()];

		blankImageData = new int[commonConfigs.getWidth()
				* commonConfigs.getHeight()];
		
		bodyPosition1 = new Point();
		bodyPosition2 = new Point();
		earPosition1 = new Point();
		earPosition2 = new Point();
		return super.configure(configs);
	}

	private Point getBlobCentroid(final int[] origImg, Point centroid,
			final int r, final int rThreshold, final int g,
			final int gThreshold, final int b, final int bThreshold) {
		ImageManipulator.filterImageRGB(origImg, tmpProcessedData, r,
				rThreshold, g, gThreshold, b, bThreshold);
		if (centroid == null)
			centroid = new Point();
		centroidFinder.updateCentroid(tmpProcessedData, centroid);
		return centroid;
	}

	@Override
	public String getID() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * @see filters.VideoFilter#newInstance(java.lang.String)
	 */
	@Override
	public VideoFilter<?, ?> newInstance(final String filterName) {
		return new HeadAngleFilter(filterName, null, null);
	}

	/*
	 * (non-Javadoc)
	 * @see filters.VideoFilter#process()
	 */
	@Override
	public void process() {
		if (getConfigs().isEnabled()) {
			final int[] origImg = getLinkIn().getData();
			final long t1 = System.currentTimeMillis();
			System.arraycopy(blankImageData, 0, outputImageData, 0,
					origImg.length);
			// find body points
			// body point 1
			getBlobCentroid(origImg, bodyPosition1, 246, 20, 0, 20, 0, 20);

			/*
			 * final CrossMarker marker = new CrossMarker(50, 50, 5, Color.RED,
			 * 640, 480); marker.draw(tmpProcessedData, color1Position.x,
			 * color1Position.y);
			 */

			// body point 2
			getBlobCentroid(origImg, bodyPosition2, 0, 20, 247, 20, 6, 20);

			// find ear points
			// ear point 1
			getBlobCentroid(origImg, earPosition1, 0, 20, 209, 20, 247, 20);

			// ear point 2
			getBlobCentroid(origImg, earPosition2, 239, 20, 247, 20, 0, 20);

			final Point[] earLinePoints = ImageManipulator.getLinePoints(
					earPosition1, earPosition2);
			for (final Point pt : earLinePoints) {
				outputImageData[pt.x + (pt.y * 640)] = 0x00FF0000;
				outputImageData[pt.x + 1 + (pt.y * 640)] = 0x00FF0000;
				outputImageData[(pt.x - 1) + (pt.y * 640)] = 0x00FF0000;
			}

			final Point[] bodyLinePoints = ImageManipulator.getLinePoints(
					bodyPosition1, bodyPosition2);
			for (final Point pt : bodyLinePoints) {
				outputImageData[pt.x + (pt.y * 640)] = 0x00FF0000;
				outputImageData[pt.x + 1 + (pt.y * 640)] = 0x00FF0000;
				outputImageData[(pt.x - 1) + (pt.y * 640)] = 0x00FF0000;
			}

			getLinkOut().setData(outputImageData);

			final long t2 = System.currentTimeMillis();
			System.out.println(t2 - t1);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see filters.VideoFilter#registerDependentData(filters.FilterData)
	 */
	@Override
	public void registerDependentData(final FilterData data) {
		// TODO Auto-generated method stub

	}

	private int rgbToInt(final int r, final int g, final int b) {
		final String rHex = Integer.toHexString(r);
		final String gHex = Integer.toHexString(g);
		final String bHex = Integer.toHexString(b);
		return Integer.decode("0x" + rHex + gHex + bHex);
	}

	/*
	 * (non-Javadoc)
	 * @see utils.StateListener#updateProgramState(utils.PManager.ProgramState)
	 */
	@Override
	public void updateProgramState(final ProgramState state) {
		// TODO Auto-generated method stub

	}

}
