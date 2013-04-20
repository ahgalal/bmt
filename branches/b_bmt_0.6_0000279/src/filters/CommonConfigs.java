/**
 * 
 */
package filters;

/**
 * @author Creative
 */
public abstract class CommonConfigs {
	private int	height;

	private int	width;

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

	public void setWidth(final int width) {
		this.width = width;
	}
	
	public void merge(CommonConfigs other){
		if(other.getWidth()!=-1)
			width=other.getWidth();
		if(other.getHeight()!=-1)
			height=other.getHeight();
	}

	public abstract boolean validate();
}
