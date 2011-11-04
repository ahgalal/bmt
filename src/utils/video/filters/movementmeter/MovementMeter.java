/**
 * 
 */
package utils.video.filters.movementmeter;

import utils.PManager.ProgramState;
import utils.video.ImageManipulator;
import utils.video.filters.FilterConfigs;
import utils.video.filters.FilterData;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;

/**
 * @author Creative
 *
 */
public class MovementMeter extends VideoFilter<FilterConfigs,FilterData>
{
	private int[] localData,prevLocalData,outputData;
	private int summation;
	
	public MovementMeter(String name, Link link_in, Link link_out)
	{
		super(name, link_in, link_out);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see utils.StateListener#updateProgramState(utils.PManager.ProgramState)
	 */
	@Override
	public void updateProgramState(ProgramState state)
	{
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean configure(FilterConfigs configs)
	{
		prevLocalData=new int[configs.common_configs.width*configs.common_configs.height];
		return super.configure(configs);
	}
	
	/* (non-Javadoc)
	 * @see utils.video.filters.VideoFilter#process()
	 */
	@Override
	public void process()
	{
		localData = ImageManipulator.rgbIntArray2GrayIntArray(link_in.getData());
		outputData = ImageManipulator.subtractImage(localData, prevLocalData);
		summation = addAllPixelsValues();
		link_out.setData(outputData);
		prevLocalData=localData;
	}
	
	private int addAllPixelsValues()
	{
		int sum=0;
		for(int i=0;i<localData.length;i++)
			sum+=localData[i];
		return 0;
	}

}
