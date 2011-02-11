package gfx_panel;

import java.awt.Point;

public interface GfxPanel_Notifiee {

	public void shapeAdded(int shape_number);
	public void shapeModified(int shape_number);
	public void shapeDeleted(int shape_number);
	public void shapeSelected(int shape_number);
	public void mouseClicked(Point pos);
	public void dragOccured(int dragged_shape,int dragged_on_shape);
}
