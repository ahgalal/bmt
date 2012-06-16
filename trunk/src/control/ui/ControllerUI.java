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

package control.ui;

import ui.BaseUI;
import utils.PManager;

/**
 * The parent of all GUI controllers.
 * 
 * @author Creative
 */
public abstract class ControllerUI<UIType extends BaseUI> {
    protected UIType ui;
    protected PManager pm;

    /**
     * Shows/Hides the GUI controlled by a child of this class.
     * 
     * @param visibility
     *            true: visible, false: invisible
     */
    public abstract void show(boolean visibility);

    /**
     * Passes an array of strings to the GUI window, the GUI will manipulate
     * this array and display strings in their proper positions.
     * 
     * @param strs
     *            array of strings to pass to the GUI
     * @return true: success, false: failure
     */
    public abstract boolean setVars(String[] strs);

    public final void unloadGUI() {
	ui.unloadGUI();
    }

}
