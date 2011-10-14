/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

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
