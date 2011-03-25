package ui;

import org.eclipse.swt.widgets.Shell;

import control.ui.ControllerUI;

/**
 * Parent of all GUI windows that have a controller.
 * 
 * @author Creative
 */
public abstract class BaseUI
{

	protected Shell sShell; // Shell fo the GUI (SWT)
	ControllerUI controller; // Controller instance

	public void show(final boolean visibility)
	{
		sShell.setVisible(visibility);
	}

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
	public void setController(final ControllerUI controller)
	{
		this.controller = controller;
	}

	/**
	 * Clears GUI data of the controls.
	 */
	public abstract void clearForm();

}
