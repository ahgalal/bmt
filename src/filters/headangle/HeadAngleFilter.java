/**
 * 
 */
package filters.headangle;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.swt.graphics.Color;

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
import filters.ratfinder.markers.RectangularMarker;

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
		gui = new HeadAngleGUI(this);
		filterData = new HeadAngleData();
	}

	private double calculateLineAngle(final Point point1, final Point point2) {
		double bodyLineAngle;
		final double mBody = (point1.y - point2.y)
				/ (double) (point1.x - point2.x);
		bodyLineAngle = Math.toDegrees(Math.atan(mBody));
		return bodyLineAngle;
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		final HeadAngleConfigs angleConfigs = (HeadAngleConfigs) configs;
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

		final Color bpColor1 = angleConfigs.getBpColor1();
		final Color bpColor2 = angleConfigs.getBpColor2();
		final Color earColor1 = angleConfigs.getEarColor1();
		final Color earColor2 = angleConfigs.getEarColor2();
		if ((bpColor1 != null) && (bpColor2 != null) && (earColor1 != null)
				&& (earColor2 != null)) {
			final RGB bp1RGB = new RGB(bpColor1.getRed(), bpColor1.getGreen(),
					bpColor1.getBlue());
			final RGB bp2RGB = new RGB(bpColor2.getRed(), bpColor2.getGreen(),
					bpColor2.getBlue());
			final RGB ear1RGB = new RGB(earColor1.getRed(),
					earColor1.getGreen(), earColor1.getBlue());
			final RGB ear2RGB = new RGB(earColor2.getRed(),
					earColor2.getGreen(), earColor2.getBlue());
			System.out.println(bp1RGB + "\n" + bp2RGB + "\n" + ear1RGB + "\n"+ ear2RGB);
			colors = new RGB[] {
					bp1RGB, // BP1
					bp2RGB, // BP2
					ear1RGB,// EAR1
					ear2RGB	// EAR2
			};
		}

//		 colors = new RGB[] {
//		 //new RGB(246, 0, 0), // BP1
//				 //new RGB(26,100,58), // BP1 // rat's green
//				 new RGB(240,248,0), // yellow
//				 
//		 new RGB(0, 247, 6), // BP2
//		 new RGB(0, 209, 247),// EAR1
//		 new RGB(239, 247, 0)// EAR2
//		 };

		thresholds = new RGB[] {
				new RGB(30, 30, 30),
				new RGB(30, 30, 30),
				new RGB(30, 30, 30),
				new RGB(30, 30, 30)
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
	Comparator<Blob> blobComparator = new Comparator<Blob>() {
		@Override
		public int compare(Blob o1, Blob o2) {
			if(o1.getArea()>o2.getArea())
				return -1;
			else
				return 1;
		}
	};
	/*
	 * (non-Javadoc)
	 * @see filters.VideoFilter#newInstance(java.lang.String)
	 */
	@Override
	public VideoFilter<?, ?> newInstance(final String filterName) {
		return new HeadAngleFilter(filterName, null, null);
	}
	ArrayList<Blob> list = new ArrayList<ImageManipulator.Blob>();
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

			// good performance, different color for each point
			/*			blobFinder.updateBlobsCentroids(origImg, tmpProcessedData, colors,
					blobs, thresholds);*/

			CommonFilterConfigs commonConfigs = getConfigs().getCommonConfigs();

			int width = commonConfigs.getWidth();
			int height = commonConfigs.getHeight();
			for(int i =0;i<colors.length-1 /* only 3 colors!*/;i++){
				ImageManipulator.filterImageRGB(origImg, tmpProcessedData, colors[i],thresholds[i]);
				Collection<Blob> tmpBlobs = blobFinder.getBlobs(tmpProcessedData,configs.getP1().x,configs.getP2().x,configs.getP1().y,configs.getP2().y);

				list.clear();
				list.addAll(tmpBlobs);

				Collections.sort(list, blobComparator);

				if(list.size()>0)
					switch (i) {
						case 0:
							bodyPosition1 =list.get(0);	
							break;
						case 1:
							bodyPosition2 = list.get(0);
							break;
						case 2:
							if(list.size()>=2){
								earPosition1 = list.get(0);
								
								earPosition2 = list.get(1);
								if(earPosition1.getCentroid().x>640 ||earPosition1.getCentroid().x<0)
									System.out.println("sssss");
								System.out.println("Blobs: "+list.size());
							}
							break;
						default:
							break;
					}
				for(Blob blob:list){
					RectangularMarker marker = new RectangularMarker(width, height, blob.getWidth(), blob.getHeight(),java.awt.Color.GREEN);

					if(i==2){
						marker.draw(outputImageData, blob.getCentroid().x-blob.getWidth()/2, blob.getCentroid().y-blob.getHeight()/2);
						System.out.println(blob);
					}
				}
			}
			
			blobs = new Blob[] {
					bodyPosition1, bodyPosition2, earPosition1, earPosition2
			};
			
			final Point[] earLinePoints = ImageManipulator
					.getLinePoints(earPosition1.getCentroid(),
							earPosition2.getCentroid(), 0.5);
			int imgLength = origImg.length;
			for (final Point pt : earLinePoints) {
				int ptIdx = pt.x + (pt.y * width);
				if(ptIdx<imgLength && ptIdx>-1)
					outputImageData[ptIdx] = 0x000000FF;
				ptIdx = pt.x + 1 + (pt.y * width);
				if(ptIdx<imgLength && ptIdx>-1)
					outputImageData[ptIdx] = 0x000000FF;
				ptIdx = (pt.x - 1) + (pt.y * width);
				if(ptIdx<imgLength && ptIdx>-1)
					outputImageData[ptIdx] = 0x000000FF;
			}

			final Point bodyPoint1 = bodyPosition1.getCentroid();
			final Point bodyPoint2 = bodyPosition2.getCentroid();
			final Point[] bodyLinePoints = ImageManipulator.getLinePoints(
					bodyPoint1, bodyPoint2, 1);
			for (final Point pt : bodyLinePoints) {
				int ptIdx = pt.x + (pt.y * width);
				if(ptIdx<imgLength && ptIdx>-1)
					outputImageData[ptIdx] = 0x00FF0000;
				ptIdx = pt.x + 1 + (pt.y * width);
				if(ptIdx<imgLength && ptIdx>-1)
					outputImageData[ptIdx] = 0x00FF0000;
				ptIdx = (pt.x - 1) + (pt.y * width);
				if(ptIdx<imgLength && ptIdx>-1)
					outputImageData[ptIdx] = 0x00FF0000;
			}

			getLinkOut().setData(outputImageData);

			double angle;
			double bodyLineAngle;
			double earsLineAngle;
			double earsLineAngleShifted;

			bodyLineAngle = calculateLineAngle(bodyPoint1, bodyPoint2);
			earsLineAngle = calculateLineAngle(earPosition1.getCentroid(),
					earPosition2.getCentroid());

			earsLineAngleShifted = earsLineAngle + 90;

			angle = bodyLineAngle - earsLineAngleShifted;
			filterData.setAngle((int) angle);
			int xAvg = 0, yAvg = 0;
			for (final Blob blob : blobs) {
				xAvg += blob.getCentroid().x;
				yAvg += blob.getCentroid().y;
			}
			xAvg /= blobs.length;
			yAvg /= blobs.length;
			filterData.getCenterPoint().x = xAvg;
			filterData.getCenterPoint().y = yAvg;
			// System.out.println(angle);
			final long t2 = System.currentTimeMillis();
			// System.out.println(t2 - t1);
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
