/**
 * 
 */
package filters.headangle;

import java.awt.Point;

import utils.PManager.ProgramState;
import utils.video.ImageManipulator;
import utils.video.ImageManipulator.Blob;
import utils.video.ImageManipulator.BlobFinder;
import utils.video.ImageManipulator.RGB;
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

	protected BlobFinder		blobFinder;
	private Blob[]				blobs;
	private Blob				bodyPosition1;

	private Blob				bodyPosition2;
	private RGB[]				colors;
	private Blob				earPosition1;
	private Blob				earPosition2;
	private int[]				outputImageData;
	private RGB[]				thresholds;
	private int[]				tmpProcessedData;

	public HeadAngleFilter(final String name, final Link linkIn,
			final Link linkOut) {
		super(name, linkIn, linkOut);
		filterData = new HeadAngleData();
		blobFinder = new BlobFinder();
		gui=new HeadAngleGUI(this);
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		final CommonFilterConfigs commonConfigs = configs.getCommonConfigs();
		final int height = commonConfigs.getHeight();
		final int width = commonConfigs.getWidth();
		tmpProcessedData = new int[width * height];
		blobFinder.initialize(width, height);
		outputImageData = new int[width * height];

		blankImageData = new int[width * height];

		bodyPosition1 = new Blob();
		bodyPosition2 = new Blob();
		earPosition1 = new Blob();
		earPosition2 = new Blob();

		colors = new RGB[] {
				new RGB(246, 0, 0),
				new RGB(0, 247, 6),
				new RGB(0, 209, 247),
				new RGB(239, 247, 0)
		};

		thresholds = new RGB[] {
				new RGB(20, 20, 20),
				new RGB(20, 20, 20),
				new RGB(20, 20, 20),
				new RGB(20, 20, 20)
		};
		blobs = new Blob[] {
				bodyPosition1, bodyPosition2, earPosition1, earPosition2
		};

		return super.configure(configs);
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
					blankImageData.length);
			System.arraycopy(blankImageData, 0, tmpProcessedData, 0,
					blankImageData.length);
			// find body points

			blobFinder.updateBlobsCentroids(origImg, tmpProcessedData, colors,
					blobs, thresholds);

			// // body point 1
			// long tCentroid1 = System.currentTimeMillis();
			// RGB thresholds2 = new RGB(5, 20, 20);
			// RGB rgb = new RGB(246, 0, 0);
			// getBlobCentroid(origImg, bodyPosition1, rgb,
			// thresholds2);
			//
			// long tCentroid2 = System.currentTimeMillis();
			// System.out.println(tCentroid2-tCentroid1);

			// long tBlobs1 = System.currentTimeMillis();
			// Collection<utils.video.ImageManipulator.Blob> blobs =
			// ImageManipulator.getBlobs(tmpProcessedData);
			// long tBlobs2 = System.currentTimeMillis();
			/*
			 * System.out.println("blob count: " + blobs.size() + " time: " +
			 * (tBlobs2 - tBlobs1));
			 */
			// System.out.println((tBlobs2 - tBlobs1));
			// utils.video.ImageManipulator.Blob[] blobsArr = blobs
			// .toArray(new utils.video.ImageManipulator.Blob[0]);

			// final CrossMarker marker = new CrossMarker(50, 50, 5, Color.RED,
			// 640, 480);
			// Point centroid = blobsArr[0].getCentroid();
			// marker.draw(outputImageData, centroid.x,
			// centroid.y);

			// // body point 2
			// rgb.setRGB(0, 247, 6);
			// thresholds2.setRGB(20, 20, 20);
			// getBlobCentroid(origImg, bodyPosition2, rgb,thresholds2);
			//
			// // find ear points
			// // ear point 1
			// rgb.setRGB(0, 209, 247);
			// thresholds2.setRGB(20, 20, 20);
			// getBlobCentroid(origImg, bodyPosition2, rgb,thresholds2);
			//
			// // ear point 2
			// rgb.setRGB(239, 247, 0);
			// thresholds2.setRGB(20, 20, 20);
			// getBlobCentroid(origImg, bodyPosition2, rgb,thresholds2);

			final Point[] earLinePoints = ImageManipulator.getLinePoints(
					earPosition1.getCentroid(), earPosition2.getCentroid());
			for (final Point pt : earLinePoints) {
				outputImageData[pt.x + (pt.y * 640)] = 0x00FF0000;
				outputImageData[pt.x + 1 + (pt.y * 640)] = 0x00FF0000;
				outputImageData[(pt.x - 1) + (pt.y * 640)] = 0x00FF0000;
			}

			final Point[] bodyLinePoints = ImageManipulator.getLinePoints(
					bodyPosition1.getCentroid(), bodyPosition2.getCentroid());
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

	/*
	 * (non-Javadoc)
	 * @see utils.StateListener#updateProgramState(utils.PManager.ProgramState)
	 */
	@Override
	public void updateProgramState(final ProgramState state) {
		// TODO Auto-generated method stub

	}

}
