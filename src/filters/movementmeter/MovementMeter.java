/**
 * 
 */
package filters.movementmeter;

import utils.PManager.ProgramState;
import utils.TimeCalculator;
import utils.video.ImageManipulator;
import filters.FilterConfigs;
import filters.Link;
import filters.VideoFilter;

/**
 * @author Creative
 */
public class MovementMeter extends
		VideoFilter<FilterConfigs, MovementMeterData> {
	private int[]	greyData, prevGreyData;
	private int[]	outputData, prevOutputData;
	private int		summation;
	private TimeCalculator	tc	= new TimeCalculator();

	private int		x1, x2, y1, y2;

	public MovementMeter(final String name, final Link linkIn,
			final Link linkOut) {
		super(name, linkIn, linkOut);
		filterData = new MovementMeterData();
	}

	private int addAllPixelsValues(final int[] arr) {
		int sum = 0;
		for (int x = x1; x < x2; x++)
			for (int y = y1; y < y2; y++) {
				sum += arr[x + y * configs.getCommonConfigs().getWidth()] & 0x000000FF;
				if ((x == x1) || (x == x2 - 1))
					arr[x + y * configs.getCommonConfigs().getWidth()] = 0x000000FF;
			}
		return sum;
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		prevGreyData = new int[configs.getCommonConfigs().getWidth()
				* configs.getCommonConfigs().getHeight()];
		prevOutputData = new int[configs.getCommonConfigs().getWidth()
				* configs.getCommonConfigs().getHeight()];
		final boolean ret = super.configure(configs);
		initializeSearchBounds();
		return ret;
	}

	private void getInterestingAreaBounds(final int[] arr, final int threshold) {
		initializeSearchBounds();
		for (int y = 10; y < configs.getCommonConfigs().getHeight(); y += 3)
			for (int x1 = 10; x1 < configs.getCommonConfigs().getWidth(); x1 += 3)
				if ((arr[x1 + y * configs.getCommonConfigs().getWidth()] & 0x000000FF) > threshold) {
					if (x1 < this.x1)
						this.x1 = x1;
					for (int x2 = configs.getCommonConfigs().getWidth() - 11; x2 > x1; x2 -= 3)
						if ((arr[x2 + y * configs.getCommonConfigs().getWidth()] & 0x000000FF) > threshold)
							if (x2 > this.x2)
								this.x2 = x2;
					break;
				}
	}

	private void initializeSearchBounds() {
		x1 = configs.getCommonConfigs().getWidth();
		y1 = 0;
		x2 = 0;
		y2 = configs.getCommonConfigs().getHeight();
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.filters.VideoFilter#process()
	 */
	@Override
	public void process() {
		greyData = ImageManipulator.rgbIntArray2GrayIntArray(linkIn.getData());

		outputData = ImageManipulator.subtractGreyImage(greyData, prevGreyData);
		// tc.start();
		getInterestingAreaBounds(outputData, 20);
		// System.out.println("x1,x2 = " + x1 + " , " + x2);
		// System.out.println(tc.end());

		final int tmp = addAllPixelsValues(outputData);
		if (tmp == 0)
			linkOut.setData(prevOutputData);
		else {
			summation = tmp;
			linkOut.setData(outputData);

			prevOutputData = outputData;
		}

		prevGreyData = greyData;
		filterData.setWhiteSummation(summation);
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
