package control.ui;

import modules.ExperimentModule;
import modules.ModulesManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import ui.RatInfoForm;
import utils.PManager;
import utils.StatusManager.StatusSeverity;

/**
 * Controller of the RatInfoForm GUI window.
 * 
 * @author Creative
 */
public class CtrlRatInfoForm extends ControllerUI
{
	private int rat_num;
	private String grp_name;
	private final RatInfoForm ui;

	/**
	 * Initializes class attributes (RatInfoForm ,InfoController and PManager).
	 */
	public CtrlRatInfoForm()
	{
		pm = PManager.getDefault();
		ui = new RatInfoForm();
		ui.setController(this);
	}

	@Override
	public boolean setVars(final String[] strs)
	{
		if (!strs[0].isEmpty())
		{
			rat_num = Integer.parseInt(strs[0]);
			grp_name = strs[1];
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see control.ui.ControllerUI#show(boolean) Before we show the window, we
	 * collect groups' names from InfoController and load them to GUI. note:
	 * params[0] is reserved for the rat number.
	 */
	@Override
	public void show(final boolean visibility)
	{
		final String[] grps = ((ExperimentModule) ModulesManager.getDefault()
				.getModuleByName("Experiment Module")).getGroupsNames();
		final String[] params = new String[grps.length + 1];

		params[0] = "";
		for (int i = 0; i < grps.length; i++)
			params[i + 1] = grps[i];
		ui.clearForm();
		ui.loadData(params);
		ui.show(visibility);
	}

	/**
	 * Checks data entered and submit it if correct.
	 */
	public void btnOkAction()
	{
		checkAndSubmitData();
	}

	/**
	 * Checks the validity of entered Rat Number & Group name. if the Rat Number
	 * already exists, it asks for confirmation; if the Group name is not valid,
	 * error message is displayed; if all data are correct, we start tracking
	 * activity.
	 */
	public void checkAndSubmitData()
	{
		try
		{

			final int tmp_confirmation = ((ExperimentModule) ModulesManager.getDefault()
					.getModuleByName("Experiment Module")).validateRatAndGroup(
					rat_num,
					grp_name);
			if (tmp_confirmation == 0)
			{
				ModulesManager.getDefault().initialize();
				((ExperimentModule) ModulesManager.getDefault().getModuleByName(
						"Experiment Module")).setCurrentRatAndGroup(rat_num, grp_name);
				startTracking();
			} else if (tmp_confirmation == 1)
			{ // Rat already exists
				final MessageBox msg = new MessageBox(ui.getShell(), SWT.ICON_QUESTION
						| SWT.YES
						| SWT.NO);
				msg.setText("Confirmation");
				msg.setMessage("Rat '" + rat_num + "' already exists, replace it?");
				final int res = msg.open();
				if (res == SWT.YES)
				{
					ModulesManager.getDefault().initialize();
					((ExperimentModule) ModulesManager.getDefault().getModuleByName(
							"Experiment Module")).setCurrentRatAndGroup(rat_num, grp_name);
					startTracking();
				}
			} else if (tmp_confirmation == -1) // Group not found
				pm.status_mgr.setStatus("Please select a group.", StatusSeverity.ERROR);
		} catch (final NumberFormatException e1)
		{
			pm.status_mgr.setStatus(
					"Please enter a valid Rat number.",
					StatusSeverity.ERROR);
		}

	}

	/**
	 * Hides the window and starts the Tracking activity.
	 */
	private void startTracking()
	{
		PManager.main_gui.startTracking();
		show(false);
	}

}
