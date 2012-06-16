/**
 * 
 */
package utils.video.filters.source;

import utils.video.filters.FilterData;

/**
 * @author Creative
 * 
 */
public class SourceFilterData extends FilterData {
    private int[] frame;

    public SourceFilterData(final String name) {
	super(name);
    }

    public void setData(final int[] data) {
	this.frame = data;
    }

    public int[] getData() {
	return frame;
    }

}
