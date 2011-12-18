/**
 * 
 */
package utils.video.filters.movementmeter;

import utils.PManager.ProgramState;
import utils.TimeCalculator;
import utils.video.ImageManipulator;
import utils.video.filters.FilterConfigs;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;

/**
 * @author Creative
 */
public class MovementMeter extends VideoFilter<FilterConfigs, MovementMeterData>
{
	private int[] greyData, prevGreyData;
	private int[] outputData, prevOutputData;
	private int summation;
	private int x1, x2, y1, y2;

	public MovementMeter(final String name, final Link link_in, final Link link_out)
	{
		super(name, link_in, link_out);
		filterData = new MovementMeterData("Movement Meter Data");
	}

	/* (non-Javadoc)
	 * @see utils.StateListener#updateProgramState(utils.PManager.ProgramState)
	 */
	@Override
	public void updateProgramState(final ProgramState state)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean configure(final FilterConfigs configs)
	{
		prevGreyData = new int[configs.common_configs.width
								* configs.common_configs.height];
		prevOutputData = new int[configs.common_configs.width
				* configs.common_configs.height];
		boolean ret = super.configure(configs);
		initializeSearchBounds();
		return ret;
	}

	TimeCalculator tc = new TimeCalculator();

	/* (non-Javadoc)
	 * @see utils.video.filters.VideoFilter#process()
	 */
	@Override
	public void process()
	{
		if (link_in.getData()[640 * 100] == 0)
			System.out.println("link_in has 0");
		greyData = ImageManipulator.rgbIntArray2GrayIntArray(link_in.getData());
		if (greyData[640 * 100] == 0)
			System.out.println("greyData has 0");
		outputData = ImageManipulator.subtractGreyImage(greyData, prevGreyData);
		// tc.start();
		getInterestingAreaBounds(outputData,20);
		//System.out.println("x1,x2 = " + x1 + " , " + x2);
		// System.out.println(tc.end());

		final int tmp = addAllPixelsValues(outputData);
		if (tmp == 0)
		{
			link_out.setData(prevOutputData);
			//link_out.setData(outputData);
		}
		else
		{
			summation = tmp;
			link_out.setData(outputData);

			prevOutputData = outputData;
		}

		prevGreyData = greyData;
		filterData.setWhiteSummation(summation);
	}

	private int addAllPixelsValues(final int[] arr)
	{
		int sum = 0;
		for (int x = x1; x < x2; x++)
		{
			for (int y = y1; y < y2; y++)
			{
				sum += arr[x + y * configs.common_configs.width] & 0x000000FF;
				if (x == x1 || x == x2 - 1)
					arr[x + y * configs.common_configs.width] = 0x000000FF;
			}
		}
		return sum;
	}

	private void getInterestingAreaBounds(final int[] arr,int threshold)
	{
		initializeSearchBounds();
		for (int y = 10; y < configs.common_configs.height; y += 3)
		{
			for (int x1 = 10; x1 < configs.common_configs.width; x1 += 3)
			{
				if ((arr[x1 + y * configs.common_configs.width] & 0x000000FF) > threshold)
				{
					if (x1 < this.x1)
						this.x1 = x1;
					for (int x2 = configs.common_configs.width - 11; x2 > x1; x2 -= 3)
					{
						if ((arr[x2 + y * configs.common_configs.width] & 0x000000FF) > threshold)
							if (x2 > this.x2)
								this.x2 = x2;
					}
					break;
				}
			}
		}
	}
	
	private void initializeSearchBounds()
	{
		x1 = configs.common_configs.width;
		y1 = 0;
		x2 = 0;
		y2 = configs.common_configs.height;
	}

}
