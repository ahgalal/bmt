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
public interface GfxPanelNotifiee {
	/**
	 * Drag operation occurred.
	 * 
	 * @param draggedShape
	 *            dragged shape (moving)
	 * @param draggedOnShape
	 *            shape being dragged on (still)
	 */
	void dragOccured(int draggedShape, int draggedOnShape);

	/**
	 * Mouse is clicked on the panel.
	 * 
	 * @param pos
	 *            position of the mouse's click
	 */
	void mouseClicked(Point pos);

	/**
	 * A new shape is added on the panel.
	 * 
	 * @param shapeNumber
	 *            new shape's number
	 */
	void shapeAdded(int shapeNumber);

	/**
	 * A shape is deleted.
	 * 
	 * @param shapeNumber
	 *            deleted shape's number
	 */
	void shapeDeleted(int shapeNumber);

	/**
	 * A shape is modified (moved/resized/color changed).
	 * 
	 * @param shapeNumber
	 *            modified shape's number
	 */
	void shapeModified(int shapeNumber);

	/**
	 * A shape is selected.
	 * 
	 * @param shapeNumber
	 *            selected shape's number
	 */
	void shapeSelected(int shapeNumber);
}
