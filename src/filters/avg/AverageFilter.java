/**
 * 
 */
package filters.avg;

import utils.PManager.ProgramState;
import utils.video.ImageManipulator;
import filters.FilterConfigs;
import filters.FilterData;
import filters.Link;
import filters.VideoFilter;

/**
 * @author Creative
 */
public class AverageFilter extends VideoFilter<AverageFilterConfigs, FilterData> {

	public static final String ID = "filters.average";
	private short[]		currentFramGrayMap;
	private int[]		dataOut;
	private int			height;

	private final int	maskHeight	= 30;

	private final int	maskWidth	= 30;
	private final int	THRESHOLD	= 20;
	private int			width;

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
		return configure;
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.filters.VideoFilter#process()
	 */
	@Override
	public void process() {
		if (configs.isEnabled()) {
			//final long t1 = System.currentTimeMillis();
			final int[] dataIn = linkIn.getData();

			final int maskArea = maskWidth * maskHeight;
			int maskSum = 0;
			for (int i = 0; i < dataOut.length; i++)
				dataOut[i] = 0;

			// form current frame's gray map (current frame is the subtraction
			// between cam frame and background, thresholded by the Subtraction
			// filter)
			currentFramGrayMap = ImageManipulator
					.formGrayMapFromGrayImage(dataIn);

			// only use average filter for effective pixels to enhance
			// performance
			for (int i = 0; i < currentFramGrayMap.length; i++) {
				final int x = i % width;
				final int y = i / width;

				if ((currentFramGrayMap[i] < THRESHOLD)
						|| (x + maskWidth / 2 > width)
						|| (y + maskHeight / 2 > height)
						|| (x - maskWidth / 2 < 0) || (y - maskHeight / 2 < 0))
					continue;

				// calculate mask's average value
				maskSum = 0;
				for (int xMask = x - maskWidth / 2; xMask < x + maskWidth / 2; xMask++)
					for (int yMask = y - maskHeight / 2; yMask < y + maskHeight
							/ 2; yMask++)
						maskSum = currentFramGrayMap[xMask + width * yMask]
								+ maskSum;
				final int maskAverage = maskSum / maskArea;

				// set pixel's value to the mask's avg value
				dataOut[x + y * width] = ImageManipulator
						.formGrayValueFromGrayIntensity((short) maskAverage);

			}

			linkOut.setData(dataOut);
			//final long t2 = System.currentTimeMillis();
			//System.out.println(t2 - t1);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see utils.StateListener#updateProgramState(utils.PManager.ProgramState)
	 */
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
		return new AverageFilter(filterName, null, null);
	}

	@Override
	public void registerDependentData(FilterData data) {
		// TODO Auto-generated method stub
		
	}
}
