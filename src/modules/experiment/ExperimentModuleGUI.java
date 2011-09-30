/***************************************************************************
 *  Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly,
 *  Sarah Hamid and Mohammed Ahmed Ramadan
 *  contact: ceng.ahmedgalal@gmail.com
 *
 *  This file is part of Behavioral Monitoring Tool.
 *
 *  Behavioral Monitoring Tool is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, version 3 of the
 *  License.
 *
 *  Behavioral Monitoring Tool is distributed in the hope that it
 *  will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Behavioral Monitoring Tool.
 *  If not, see <http://www.gnu.org/licenses/>.
 *   
 **************************************************************************/

package modules.experiment;

import modules.ModulesManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import utils.PManager;
import utils.StatusManager.StatusSeverity;

/**
 * GUI class for the Experiment Module.
 * 
 * @author Creative
 */
public class ExperimentModuleGUI
{
	private final Shell shell;
	private MenuItem mnu_experiment_item = null;
	private Menu mnu_experiment = null;
	private MenuItem mnutm_experiment_loadexp = null;
	private MenuItem mnutm_experiment_exporttoexcel = null;
	private final MenuItem mnuitm_edt_exp;
	private final MenuItem mnutm_experiment_newexp;

	/**
	 * Initializes/shows the GUI components.
	 * 
	 * @param menuBar
	 *            menubar of MainGUI
	 * @param shell
	 *            parent shell (MainGUI's shell)
	 */
	public ExperimentModuleGUI(final Menu menuBar, final Shell shell)
	{
		this.shell = shell;
		mnu_experiment_item = new MenuItem(menuBar, SWT.CASCADE); // experiment
		mnu_experiment_item.setText("Experiment");
		mnu_experiment = new Menu(mnu_experiment_item);
		mnu_experiment_item.setMenu(mnu_experiment);
		mnutm_experiment_newexp = new MenuItem(mnu_experiment, SWT.PUSH);
		mnutm_experiment_loadexp = new MenuItem(mnu_experiment, 0);
		mnutm_experiment_loadexp.setText("Load Exp.");
		mnutm_experiment_loadexp.setEnabled(true);
		mnuitm_edt_exp = new MenuItem(mnu_experiment, SWT.PUSH);
		mnutm_experiment_exporttoexcel = new MenuItem(mnu_experiment, 0);
		mnutm_experiment_exporttoexcel.setText("Export to Excel");
		mnutm_experiment_exporttoexcel.setEnabled(true);
		mnutm_experiment_exporttoexcel.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				mnutmExperimentExportToExcelAction();
			}

			public void widgetDefaultSelected(
					final org.eclipse.swt.events.SelectionEvent e)
			{
			}
		});
		mnutm_experiment_newexp.setText("New Exp..");

		mnuitm_edt_exp.setText("Edit Exp.");
		mnuitm_edt_exp.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					final org.eclipse.swt.events.SelectionEvent e)
			{
			}

			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				mnuitmEditExpAction();
			}
		});
		mnutm_experiment_newexp.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetDefaultSelected(
					final org.eclipse.swt.events.SelectionEvent e)
			{
			}

			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				mnutmExperimentNewExpAction();
			}
		});

		mnutm_experiment_loadexp.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0)
			{
			}

			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				mnutmExperimentLoadexpAction(shell);
			}
		});
	}

	/**
	 * Shows a save file dialog to save the exported Excel data to that file.
	 */
	public void mnutmExperimentExportToExcelAction()
	{
		final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setFilterExtensions(new String[] { "*.xlsx" });
		final String file_name = fileDialog.open();
		if (file_name != null)
			((ExperimentModule) ModulesManager.getDefault().getModuleByName(
					"Experiment Module")).writeToExcelFile(file_name);
	}

	/**
	 * Handles the "Edit Experiment" menu item click action.
	 */
	public void mnuitmEditExpAction()
	{
		PManager.getDefault().frm_exp.show(true);
	}

	/**
	 * Shows the new ExperimentForm and unloads the previous experiment.
	 */
	public void mnutmExperimentNewExpAction()
	{
		PManager.getDefault().frm_exp.clearForm();
		PManager.getDefault().frm_grps.clearForm();
		PManager.main_gui.clearForm();
		PManager.getDefault().frm_exp.show(true);
		((ExperimentModule) ModulesManager.getDefault().getModuleByName(
				"Experiment Module")).unloadExperiment();
	}

	/**
	 * Loads an experiment from file: Shows Open Dialog box, Unloads the
	 * previous experiment and loads the new experiment from the selected file.
	 * 
	 * @param sShell
	 *            parent shell for the open dialogbox
	 */
	public void mnutmExperimentLoadexpAction(final Shell sShell)
	{
		final FileDialog fileDialog = new FileDialog(sShell, SWT.OPEN);
		final String file_name = fileDialog.open();
		if (file_name != null)
		{
			PManager.getDefault().frm_exp.clearForm();
			PManager.getDefault().frm_grps.clearForm();
			PManager.main_gui.clearForm();
			((ExperimentModule) ModulesManager.getDefault().getModuleByName(
					"Experiment Module")).unloadExperiment();
			((ExperimentModule) ModulesManager.getDefault().getModuleByName(
					"Experiment Module")).loadInfoFromTXTFile(file_name);
			((ExperimentModule) ModulesManager.getDefault().getModuleByName(
					"Experiment Module")).setExpFileName(file_name);
			PManager.getDefault().status_mgr.setStatus(
					"Experiment Loaded Successfully!",
					StatusSeverity.WARNING);
		}
	}

	/**
	 * Enables/disables the edit experiment menu item.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void editExpMenuItemEnable(final boolean enable)
	{
		mnuitm_edt_exp.setEnabled(enable);
	}

	/**
	 * Enables/disables the export experiment to excel menu item.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void exportExpToExcelMenuItemEnable(final boolean enable)
	{
		mnutm_experiment_exporttoexcel.setEnabled(enable);
	}
}
