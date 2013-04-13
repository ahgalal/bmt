/**
 * 
 */
package filters.source;

import modules.experiment.Constants;
import filters.FilterData;

/**
 * @author Creative
 */
public class SourceFilterData extends FilterData {
	private int[]	frame;
	public final static String dataID=Constants.FILTER_ID+".source.data";
	public int[] getData() {
		return frame;
	}

	public void setData(final int[] data) {
		this.frame = data;
	}

	@Override
	public String getId() {
		return dataID;
	}
}
