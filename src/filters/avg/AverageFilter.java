/**
 * 
 */
package filters.avg;

import java.awt.Point;

import utils.PManager.ProgramState;
import utils.video.ImageManipulator;
import filters.FilterConfigs;
import filters.FilterData;
import filters.Link;
import filters.VideoFilter;

/**
 * @author Creative
 */
public class AverageFilter extends
		VideoFilter<AverageFilterConfigs, FilterData> {

	public static final String	ID			= "filters.average";
	private short[]				currentFrameGrayMap;
	private int[]				dataOut;
	private int					height;

	private final int			MASK_HEIGHT	= 30;
	private final int			MASK_WIDTH	= 30;
	private final int			THRESHOLD	= 20;
	private int					width;

	public AverageFilter(final String name, final Link linkIn,
			final Link linkOut) {
		super(name, linkIn, linkOut);
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		final boolean configure = super.configure(configs);
		dataOut = new int[configs.getCommonConfigs().getWidth()
				* configs.getCommonConfigs().getHeight()];
		width = configs.getCommonConfigs().getWidth();
		height = configs.getCommonConfigs().getHeight();
		
		effectivePixels=new Point[width*height/2];
		for(int i=0;i<effectivePixels.length;i++)
			effectivePixels[i]=new Point();
		return configure;
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public VideoFilter<?, ?> newInstance(final String filterName) {
		return new AverageFilter(filterName, null, null);
	}
	private Point[] effectivePixels; 
	/*
	 * (non-Javadoc)
	 * @see utils.video.filters.VideoFilter#process()
	 */
	@Override
	public void process() {
		if (configs.isEnabled()) {
			 final long t1 = System.currentTimeMillis();
			final int[] dataIn = linkIn.getData();
			int effectivePixelsNumber=0;
			final int maskArea = MASK_WIDTH * MASK_HEIGHT;
			for (int i = 0; i < dataOut.length; i++)
				dataOut[i] = 0;
			
			// form current frame's gray map (current frame is the subtraction
			// between cam frame and background, thresholded by the Subtraction
			// filter)
			currentFrameGrayMap = ImageManipulator
					.formGrayMapFromGrayImage(dataIn);

			
			
			// only use average filter for effective pixels to enhance
			// performance
			for (int i = 0; i < currentFrameGrayMap.length; i++) {
				final int x = i % width;
				final int y = i / width;

				if ((currentFrameGrayMap[i] < THRESHOLD)
						|| (x + MASK_WIDTH / 2 > width)
						|| (y + MASK_HEIGHT / 2 > height)
						|| (x - MASK_WIDTH / 2 < 0)
						|| (y - MASK_HEIGHT / 2 < 0))
					continue;
				effectivePixels[effectivePixelsNumber].x=x;
				effectivePixels[effectivePixelsNumber].y=y;
				effectivePixelsNumber++;
			}

			for(int i=0;i<effectivePixelsNumber;i++){
				Point p=effectivePixels[i];
				// calculate mask's average value
				int maskAverage = getMaskAverage(p.x, p.y,maskArea);

				// set pixel's value to the mask's avg value
				dataOut[p.x + p.y * width] = ImageManipulator
				.formGrayValueFromGrayIntensity((short) maskAverage);
			}

			linkOut.setData(dataOut);
			 final long t2 = System.currentTimeMillis();
			 System.out.println(t2 - t1);
		}
	}

	private int getMaskAverage(final int x, final int y, int maskArea) {
		int maskSum = 0;
		for (int xMask = x - MASK_WIDTH / 2; xMask < x + MASK_WIDTH / 2; xMask++)
			for (int yMask = y - MASK_HEIGHT / 2; yMask < y
					+ MASK_HEIGHT / 2; yMask++)
				maskSum = currentFrameGrayMap[xMask + width * yMask]
						+ maskSum;
		return maskSum / maskArea;
	}

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
