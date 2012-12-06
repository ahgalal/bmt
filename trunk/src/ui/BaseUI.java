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

package ui;

import org.eclipse.swt.widgets.Shell;

import control.ui.ControllerUI;

/**
 * Parent of all GUI windows that have a controller.
 * 
 * @author Creative
 */
public abstract class BaseUI {

	@SuppressWarnings("rawtypes")
	protected ControllerUI	controller; // Controller instance
	protected Shell			sShell;	// Shell fo the GUI (SWT)

	/**
	 * Clears GUI data of the controls.
	 */
	public abstract void clearForm();

	/**
	 * Loads data from an array of strings to the GUI controls, by a specific
	 * order.
	 * 
	 * @param str_array
	 *            array of strings that contains data to load to the GUI
	 *            controls
	 */
	public abstract void loadData(String[] str_array);

	/**
	 * Registers the controller with the GUI. the Controller is the one who
	 * instantiates the GUI, so it has to register itself with the GUI for the
	 * GUI to be able to communicate with the controller.
	 * 
	 * @param controller
	 *            Controller instance
	 */
	@SuppressWarnings("rawtypes")
	public void setController(final ControllerUI controller) {
		this.controller = controller;
	}

	/**
	 * Shows/Hides the GUI window.
	 * 
	 * @param visibility
	 *            visible: true/false
	 */
	public void show(final boolean visibility) {
		sShell.setVisible(visibility);
		if(visibility)
			sShell.setActive();
	}

	public void unloadGUI() {
		if (sShell != null)
			sShell.dispose();
	}

}
