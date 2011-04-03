package gfx_panel;

import java.awt.Point;

/**
 * Notifications Receiver from the GfxPanel.
 * 
 * @author Creative
 */
public interface GfxPanelNotifiee
{
	/**
	 * A new shape is added on the panel.
	 * 
	 * @param shape_number
	 *            new shape's number
	 */
	void shapeAdded(int shape_number);

	/**
	 * A shape is modified (moved/resized/color changed).
	 * 
	 * @param shape_number
	 *            modified shape's number
	 */
	void shapeModified(int shape_number);

	/**
	 * A shape is deleted.
	 * 
	 * @param shape_number
	 *            deleted shape's number
	 */
	void shapeDeleted(int shape_number);

	/**
	 * A shape is selected.
	 * 
	 * @param shape_number
	 *            selected shape's number
	 */
	void shapeSelected(int shape_number);

	/**
	 * Mouse is clicked on the panel.
	 * 
	 * @param pos
	 *            position of the mouse's click
	 */
	void mouseClicked(Point pos);

	/**
	 * Drag operation occurred.
	 * 
	 * @param dragged_shape
	 *            dragged shape (moving)
	 * @param dragged_on_shape
	 *            shape being dragged on (still)
	 */
	void dragOccured(int dragged_shape, int dragged_on_shape);
}
