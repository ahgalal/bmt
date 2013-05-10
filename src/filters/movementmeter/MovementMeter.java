/**
 * 
 */
package filters.movementmeter;

import utils.PManager.ProgramState;
import utils.video.ImageManipulator;
import filters.FilterConfigs;
import filters.FilterData;
import filters.Link;
import filters.VideoFilter;

/**
 * @author Creative
 */
public class MovementMeter extends
		VideoFilter<FilterConfigs, MovementMeterData> {
	public static final String ID = "filters.mevementmeter";
	private int[]	greyData, prevGreyData;
	private int[]	outputData, prevOutputData;
	private int		summation;

	private int		x1, x2, y1, y2;

	public MovementMeter(final String name, final Link linkIn,
			final Link linkOut) {
		super(name, linkIn, linkOut);
		filterData = new MovementMeterData();
	}

	private int addAllPixelsValues(final int[] arr) {
		int sum = 0;
		int width = configs.getCommonConfigs().getWidth();
		for (int x = x1; x < x2; x++)
			for (int y = y1; y < y2; y++) {
				
				sum += arr[x + y * width] & 0x000000FF;
				if ((x == x1) || (x == x2 - 1))
					arr[x + y * width] = 0x000000FF;
			}
		return sum;
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		
		prevGreyData = new int[configs.getCommonConfigs().getWidth()
				* configs.getCommonConfigs().getHeight()];
		prevOutputData = new int[configs.getCommonConfigs().getWidth()
				* configs.getCommonConfigs().getHeight()];
		greyData = new int[prevGreyData.length];
		final boolean ret = super.configure(configs);
		initializeSearchBounds();
		return ret;
	}

	private void getInterestingAreaBounds(final int[] arr, final int threshold) {
		initializeSearchBounds();
		int height = configs.getCommonConfigs().getHeight();
		int width = configs.getCommonConfigs().getWidth();
		for (int y = 10; y < height; y += 3) {
			for (int x1 = 10; x1 < width; x1 += 3)
				if ((arr[x1 + y * width] & 0x000000FF) > threshold) {
					if (x1 < this.x1)
						this.x1 = x1;
					for (int x2 = width - 11; x2 > x1; x2 -= 3)
						if ((arr[x2 + y * width] & 0x000000FF) > threshold)
							if (x2 > this.x2)
								this.x2 = x2;
					break;
				}
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
		greyData = ImageManipulator.rgbIntArray2GrayIntArray(linkIn.getData(),greyData);

		outputData = ImageManipulator.subtractGreyImage(greyData, prevGreyData,20);
		// tc.start();
		getInterestingAreaBounds(outputData, 20);
		// System.out.println("x1,x2 = " + x1 + " , " + x2);
		// System.out.println(tc.end());

		final int tmp = addAllPixelsValues(outputData);
		
		// pseudo coloring the image
		pseudoColors(outputData);
		if (tmp == 0)
			linkOut.setData(prevOutputData);
		else {
			summation = tmp;
			
			linkOut.setData(outputData);

			prevOutputData = outputData;
		}

		System.arraycopy(greyData, 0, prevGreyData, 0, greyData.length);
		filterData.setWhiteSummation(summation);
		//System.out.println(summation);
	}

	private void pseudoColors(int[] outputData2) {
		/**
		 * 0-20: 	green
		 * 21-50:	blue
		 * 51-255:	red
		 */
		int greenMin=0;
		int greenMax=20;
		int blueMin=21;
		int blueMax=50;
		int redMin=51;
		int redMax=255;
		int width = configs.getCommonConfigs().getWidth();
		// we work on the interesting area only, to save processing power
		for (int x = x1; x < x2; x++)
			for (int y = y1; y < y2; y++) {
			int i = x+y*width;
			int grayVal=outputData2[i]>>16;
			if(grayVal>greenMin &&grayVal<=greenMax){
				int g=(int) (100+(grayVal/(float)greenMax)*155);
				outputData2[i]=0 | (g << 8) | (0 << 16);
			}else if(grayVal>blueMin &&grayVal<blueMax){
				int b=(int) (100+((grayVal-blueMin)/(float)(blueMax-blueMin))*155);
				outputData2[i]=b | (0 << 8) | (0 << 16);
			}else if(grayVal>redMin &&grayVal<redMax){
				int r=(int) (100+((grayVal-redMin)/(float)(redMax-redMin))*155);
				outputData2[i]=0 | (0 << 8) | (r << 16);
			}else
				outputData2[i]=0;
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
		return new MovementMeter(filterName, null, null);
	}

	@Override
	public void registerDependentData(FilterData data) {
		// TODO Auto-generated method stub
		
	}

}
