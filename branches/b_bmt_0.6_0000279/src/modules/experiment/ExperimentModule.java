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

package modules.experiment;

import modules.Cargo;
import modules.ExperimentManager;
import modules.Module;
import modules.ModuleConfigs;
import modules.ModuleData;
import modules.ModulesManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import sys.utils.Utils;
import utils.Logger.Details;
import utils.PManager;
import filters.Data;

/**
 * Experiment Module, it saves, loads and updated experiment's data.
 * 
 * @author Creative
 */
public abstract class ExperimentModule
extends
Module<ExperimentModuleGUI, ExperimentModuleConfigs, ExperimentModuleData> {
	private boolean	forceReady			= false;
	private String[] expParams = new String[]{Constants.FILE_RAT_NUMBER,
			Constants.FILE_GROUP_NAME };
	private boolean	msgboxPending		= true;

	private boolean	ratFrmShown	= false;
	

	public ExperimentModule(
			final ExperimentModuleConfigs config) {
		super(config);
		initialize();
	}
	protected void addDefaultModuleDataParams() {
		for(String param:expParams)
		data.addParameter(param);		
	}
	@Override
	public boolean amIReady(final Shell shell) {
		ratFrmShown = false;
		msgboxPending = true;
		forceReady = false;
		if (ExperimentManager.getDefault().isExperimentPresent()) {
			// if we had an empty experiment (no parameters), we assign the
			// set of parameters from the module manager to the exp.
			if (ExperimentManager.getDefault().getNumberOfExpParams() == 0)
				configs.exp.setParametersList(ModulesManager.getDefault()
						.getCodeNames());
			if (ExperimentManager.getDefault().getNumberOfExpParams() != ModulesManager
					.getDefault().getNumberOfFileParameters())
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						final MessageBox mbox = new MessageBox(shell,
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						mbox.setMessage("Experiment loaded has some Modules"
								+ " which are not active now, you can't save the"
								+ " experiment when done!, Continue?");
						mbox.setText("Continue?");
						if (mbox.open() == SWT.YES) {
							PManager.getDefault().getFrmRat().show(true);
							ratFrmShown = true;
							msgboxPending = false;
						} else
							msgboxPending = false;
					}
				});
			else {
				PManager.getDefault().getFrmRat().show(true);
				ratFrmShown = true;
				msgboxPending = false;
			}

		} else
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					final MessageBox mbox = new MessageBox(shell,
							SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					mbox.setMessage("No experiment is loaded!, Continue?");
					mbox.setText("Continue?");
					if (mbox.open() == SWT.YES)
						forceReady = true;
					else
						forceReady = false;
					msgboxPending = false;
				}
			});
		while (msgboxPending)
			try {
				Thread.sleep(200);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			if (forceReady)
				return true;
			return waitForRatFrm();
	}

	@Override
	public void deInitialize() {
	}

	@Override
	public void deRegisterDataObject(final Data data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
		PManager.log.print("initializing..", this, Details.VERBOSE);
		guiCargo = new Cargo(new String[] { Constants.GUI_EXP_NAME,
				Constants.GUI_GROUP_NAME, Constants.GUI_RAT_NUMBER,
				Constants.GUI_EXP_TYPE });
		
		fileCargo = new Cargo(expParams);
	}

	/*	*//**
	 * Initializes the Experiment module.
	 * 
	 * @param name
	 *            module's name
	 * @param config
	 *            module's configurations
	 */

	@Override
	public void process() {

	}

	@Override
	public void registerFilterDataObject(final Data data) {
	}

	@Override
	public void registerModuleDataObject(final ModuleData data) {
		if(data.getParameters().size()>0)
			ExperimentManager.getDefault().addExpParams(data.getParameters());
	}

	@Override
	public void updateConfigs(final ModuleConfigs config) {
	}

	@Override
	public void updateFileCargoData() {
		fileCargo.setDataByTag(Constants.FILE_RAT_NUMBER,
				Integer.toString(configs.getCurrRatNumber()));
		fileCargo.setDataByTag(Constants.FILE_GROUP_NAME,
				configs.getCurrGrpName());
	}

	@Override
	public void updateGUICargoData() {
		guiCargo.setDataByTag(Constants.GUI_EXP_NAME, configs.exp.getName());
		guiCargo.setDataByTag(Constants.GUI_GROUP_NAME,
				configs.getCurrGrpName());
		guiCargo.setDataByTag(Constants.GUI_EXP_TYPE,
				configs.exp.getType().toString());
		guiCargo.setDataByTag(Constants.GUI_RAT_NUMBER,
				Integer.toString(configs.getCurrRatNumber()));
	}

	/**
	 * Waits till user takes action in the RatInfoForm.
	 * 
	 * @return true: a valid rat is entered, false: cancel is pressed
	 */
	private boolean waitForRatFrm() {
		while (ratFrmShown
				&& (!PManager.getDefault().getFrmRat().isValidRatEntered() && !PManager
						.getDefault().getFrmRat().isCancelled()))
			//System.out.println("sleeping ..1");
		Utils.sleep(200);
		//System.out.println("sleeping ..2");
		if (PManager.getDefault().getFrmRat().isValidRatEntered())
			return true;
		else
			return false;
	}

}
