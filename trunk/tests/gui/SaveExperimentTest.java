package gui;

import utils.FileReader;
import utils.KeyboardUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.ComboItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.TabItemLocator;

public class SaveExperimentTest extends UITestCaseSWT {

	/**
	 * Create an Instance
	 */
	public SaveExperimentTest() {
		super(utils.PManager.class);
	}

	/**
	 * Main test method.
	 */
	public void testSaveExperiment() throws Exception {
		final String expName = "Exp1";
		final String userName = "AGalal";
		final String grpName = "Grp1";

		final IUIContext ui = getUI();
		ui.click(new MenuItemLocator("Experiment/New Exp.."));
		ui.wait(new ShellShowingCondition("Experiment Information"));
		ui.click(new LabeledTextLocator("Name:"));
		ui.enterText(expName);
		ui.click(new ComboItemLocator("Forced Swimming"));
		ui.click(new LabeledTextLocator("User:"));
		ui.enterText(userName);
		ui.click(new ButtonLocator("Manage Groups.."));
		ui.wait(new ShellShowingCondition("Groups Information"));
		ui.click(new ButtonLocator("+"));
		ui.click(new LabeledTextLocator("Name:"));
		ui.enterText(grpName);
		ui.click(new LabeledTextLocator("Additional Notes:"));
		ui.enterText("Notes1");
		ui.click(new ButtonLocator("+"));
		ui.click(new TabItemLocator("new Group"));
		ui.click(new LabeledTextLocator("Name:"));
		ui.enterText("Grp2");
		ui.click(new LabeledTextLocator("Additional Notes:"));
		ui.enterText("Notes2");
		ui.click(new ButtonLocator("Save"));
		ui.click(new LabeledTextLocator("Additional Notes:"));
		ui.enterText("ExpNotes");
		ui.click(new ButtonLocator("Save"));
		Thread.sleep(100);
		KeyboardUtils.typeText(System.getProperty("user.dir") + "\\Exp.bmt",
				true);
		// ui.ensureThat(new
		// ShellLocator("Behavioral Monitoring Tool").isClosed());
		// ui.close(new ShellLocator("Behavioral Monitoring Tool"));

		final String fileData = FileReader.read(System.getProperty("user.dir")
				+ "\\Exp.bmt");
		assert fileData.contains(expName) && fileData.contains(userName)
				&& fileData.contains(grpName);
		Thread.sleep(1000);
	}

}