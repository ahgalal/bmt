/**
 * 
 */
package filters.source;

import filters.FilterData;

/**
 * @author Creative
 */
public class SourceFilterData extends FilterData {
	private int[]	frame;

	public SourceFilterData(final String name) {
		super(name);
	}

	public int[] getData() {
		return frame;
	}

	public void setData(final int[] data) {
		this.frame = data;
	}

}
