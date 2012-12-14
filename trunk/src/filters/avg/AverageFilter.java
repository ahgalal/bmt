/**
 * 
 */
package filters.avg;

import utils.PManager.ProgramState;
import utils.video.ImageManipulator;
import filters.FilterData;
import filters.Link;
import filters.VideoFilter;
import filters.source.SourceFilterConfigs;

/**
 * @author Creative
 */
public class AverageFilter extends VideoFilter<SourceFilterConfigs, FilterData> {

	private final int	maskHeight	= 7;
	private final int	maskWidth	= 7;
	private final int	xStepping	= 2;
	private final int	yStepping	= 2;

	public AverageFilter(final String name, final Link linkIn,
			final Link linkOut) {
		super(name, linkIn, linkOut);
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.filters.VideoFilter#process()
	 */
	@Override
	public void process() {
		if (configs.isEnabled()) {
			final int[] dataIn = linkIn.getData();
			// TODO: extensive array creation ... SLOW .. try to make one
			// instance
			// when configs change only
			final int[] dataOut = new int[configs.getCommonConfigs().getWidth()
					* configs.getCommonConfigs().getHeight()];
			final int maskArea = maskWidth * maskHeight;
			int maskAverage = 0;
			for (int x = 20; x < configs.getCommonConfigs().getWidth() - maskWidth - 20; x += xStepping)
				for (int y = 20; y < configs.getCommonConfigs().getHeight() - maskHeight
						- 20; y += yStepping) {
					maskAverage = 0;
					for (int xMask = x; xMask < x + maskWidth; xMask++)
						for (int yMask = y; yMask < y + maskHeight; yMask++)
							maskAverage = ImageManipulator.addRGBInt(
									dataIn[xMask + configs.getCommonConfigs().getWidth()
											* yMask], maskAverage);
					final int val = ImageManipulator.divideRGBByNumber(
							maskAverage, maskArea);
					dataOut[x + maskWidth / 2 + (y + maskHeight / 2)
							* configs.getCommonConfigs().getWidth()] = val;

					dataOut[x + maskWidth / 2 + 1 + (y + 1 + maskHeight / 2)
							* configs.getCommonConfigs().getWidth()] = val;
					dataOut[x + maskWidth / 2 + (y + 1 + maskHeight / 2)
							* configs.getCommonConfigs().getWidth()] = val;
					dataOut[x + maskWidth / 2 + 1 + (y + maskHeight / 2)
							* configs.getCommonConfigs().getWidth()] = val;
				}
			linkOut.setData(dataOut);
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

}
