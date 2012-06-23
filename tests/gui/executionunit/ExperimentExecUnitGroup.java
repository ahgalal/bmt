package gui.executionunit;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.TabItemLocator;

/**
 * Contains Execution units for dealing with Experiment.
 * @author Creative
 *
 */
public class ExperimentExecUnitGroup extends ExecutionUnitGroup{

	public ExperimentExecUnitGroup(IUIContext ui) {
		super(ui);
	}
	
	/**
	 * Creates a new Experiment and saved it to file.
	 * @throws WidgetSearchException
	 */
	public void createNewExperiment() throws WidgetSearchException{
		ui.click(new MenuItemLocator("Experiment/New Exp.."));
		ui.wait(new ShellShowingCondition("Experiment information"));
		ui.click(new LabeledTextLocator("Name:"));
		ui.enterText("TestExperiment");
		ui.click(new LabeledTextLocator("User:"));
		ui.enterText("AGalal");
		ui.click(new LabeledTextLocator("Additional Notes:"));
		ui.enterText("This Note is for the Experiment.");
		ui.click(new ButtonLocator("&Next >"));
		ui.click(new ButtonLocator("+"));
		ui.click(new LabeledTextLocator("Name:"));
		ui.enterText("Group1");
		ui.click(new LabeledTextLocator("Additional Notes:"));
		ui.enterText("This note is for Group1");
		ui.click(new ButtonLocator("+"));
		ui.click(new TabItemLocator("new Group"));
		ui.click(new LabeledTextLocator("Name:"));
		ui.enterText("Group2");
		ui.click(new LabeledTextLocator("Additional Notes:"));
		ui.enterText("This note is for Group2");
		ui.click(new ButtonLocator("&Finish"));
		ui.enterText("TestExp.bmt");
		ui.keyClick(WT.CR); 
		ui.wait(new ShellDisposedCondition("Experiment information"));
	}
	
}
