package utils.video.filters.ratfinder.markers;

import java.awt.Color;

public class RectangularMarker extends Marker {

    private final int width, height;

    public RectangularMarker(final int img_width, final int img_height,
	    final int width, final int height, final Color color) {
	super(img_width, img_height, color);
	this.width = width;
	this.height = height;
    }

    @Override
    public void draw(final int[] img, final int x, final int y) {
	drawRect(img, x, y, width, height);
    }

}
