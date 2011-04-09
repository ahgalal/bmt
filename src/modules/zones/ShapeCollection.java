package modules.zones;

import java.awt.Graphics;

/**
 * Container for Shapes with the ability to draw them.
 * 
 * @author Creative
 */
public interface ShapeCollection
{
	/**
	 * Draws all shapes stored on the graphics object specified.
	 * 
	 * @param gfx
	 *            Graphics object to draw the shapes on
	 */
	void drawaAllShapes(final Graphics gfx);
}
