/**
 * 
 */
package utils.video.filters.avg;

import utils.PManager.ProgramState;
import utils.video.ImageManipulator;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;

/**
 * @author Creative
 */
public class AverageFilter extends VideoFilter {

    private final int maskWidth = 3;
    private final int maskHeight = 3;
    private final int xStepping = 2;
    private final int yStepping = 2;

    public AverageFilter(final String name, final Link link_in,
	    final Link link_out) {
	super(name, link_in, link_out);
    }

    /*
     * (non-Javadoc)
     * 
     * @see utils.StateListener#updateProgramState(utils.PManager.ProgramState)
     */
    @Override
    public void updateProgramState(final ProgramState state) {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see utils.video.filters.VideoFilter#process()
     */
    @Override
    public void process() {
	if (configs.enabled) {
	    final int[] dataIn = link_in.getData();
	    // TODO: extensive array creation ... SLOW .. try to make one
	    // instance
	    // when configs change only
	    final int[] dataOut = new int[configs.common_configs.width
		    * configs.common_configs.height];
	    final int maskArea = maskWidth * maskHeight;
	    int maskAverage = 0;
	    for (int x = 20; x < configs.common_configs.width - maskWidth - 20; x += xStepping)
		for (int y = 20; y < configs.common_configs.height - maskHeight
			- 20; y += yStepping) {
		    maskAverage = 0;
		    for (int xMask = x; xMask < x + maskWidth; xMask++)
			for (int yMask = y; yMask < y + maskHeight; yMask++)
			    maskAverage = ImageManipulator.addRGBInt(
				    dataIn[xMask + configs.common_configs.width
					    * yMask], maskAverage);
		    final int val = ImageManipulator.divideRGBByNumber(
			    maskAverage, maskArea);
		    dataOut[x + maskWidth / 2 + (y + maskHeight / 2)
			    * configs.common_configs.width] = val;

		    dataOut[x + maskWidth / 2 + 1 + (y + 1 + maskHeight / 2)
			    * configs.common_configs.width] = val;
		    dataOut[x + maskWidth / 2 + (y + 1 + maskHeight / 2)
			    * configs.common_configs.width] = val;
		    dataOut[x + maskWidth / 2 + 1 + (y + maskHeight / 2)
			    * configs.common_configs.width] = val;
		}
	    link_out.setData(dataOut);
	}
    }

}
