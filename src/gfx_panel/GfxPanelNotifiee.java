package gfx_panel;

import java.awt.Point;

public interface GfxPanelNotifiee
{

	void shapeAdded(int shape_number);

	void shapeModified(int shape_number);

	void shapeDeleted(int shape_number);

	void shapeSelected(int shape_number);

	void mouseClicked(Point pos);

	void dragOccured(int dragged_shape, int dragged_on_shape);
}
