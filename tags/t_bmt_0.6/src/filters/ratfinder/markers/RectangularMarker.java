package filters.ratfinder.markers;

import java.awt.Color;

public class RectangularMarker extends Marker {

	private final int	width, height;

	public RectangularMarker(final int imgWidth, final int imgHeight,
			final int width, final int height, final Color color) {
		super(imgWidth, imgHeight, color);
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(final int[] img, final int x, final int y) {
		drawRect(img,x<0?0:x,y<0?0:y, width, height);
	}

}
