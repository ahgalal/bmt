package gui.executionunit;

import utils.DialogBoxUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.ShellLocator;
import com.windowtester.runtime.swt.locator.TabItemLocator;

/**
 * Contains Execution units for dealing with Experiment.
 * 
 * @author Creative
 */
public class ExperimentExecUnitGroup extends ExecutionUnitGroup {

	public ExperimentExecUnitGroup(final IUIContext ui) {
		super(ui);
	}

	/**
	 * Creates a new Experiment and saved it to file.
	 * 
	 * @throws WidgetSearchException
	 */
	public void createNewExperiment() throws WidgetSearchException {
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
		DialogBoxUtils.fillDialog("TestExp.bmt",ui);
		ui.wait(new ShellDisposedCondition("Experiment information"));
	}

	/**
	 * Changes values for basic info, and deletes a group and adds a new group.
	 * 
	 * @throws Exception
	 */
	public void editExperiment() throws Exception {
		ui.click(new MenuItemLocator("Experiment/Edit Exp."));
		ui.wait(new ShellShowingCondition("Experiment information"));
		ui.click(new XYLocator(new LabeledTextLocator("Name:"), 44, 8));
		ui.enterText("_edit");
		ui.click(new XYLocator(new LabeledTextLocator("User:"), 76, 11));
		ui.enterText("_edit");
		ui.click(new XYLocator(new LabeledTextLocator("Additional Notes:"), 72,
				13));
		ui.enterText("_edit");
		ui.click(new ButtonLocator("&Next >"));
		ui.click(new XYLocator(new LabeledTextLocator("Name:"), 55, 11));
		ui.enterText("_edit");
		ui.click(new XYLocator(new LabeledTextLocator("Additional Notes:"), 55,
				23));
		ui.enterText("_edit");
		ui.click(new ButtonLocator("+"));
		ui.click(new TabItemLocator("new Group"));
		ui.click(new LabeledTextLocator("Name:"));
		ui.enterText("G_new");
		ui.click(new LabeledTextLocator("Additional Notes:"));
		ui.enterText("G_new notes");
		ui.click(new TabItemLocator("Group2"));
		ui.click(new ButtonLocator("-"));
		ui.click(new TabItemLocator("G_new"));
		ui.click(new ButtonLocator("&Finish"));
		DialogBoxUtils.fillDialog("TestExp_editted.bmt", ui);
		ui.wait(new ShellDisposedCondition("Experiment information"));
	}

	/**
	 * Loads experiment using the input filePath. if filePath is Null default
	 * file is used.
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public void loadExperiment(final String filePath) throws Exception {
		String file;
		if ((filePath != null) && (filePath != ""))
			file = filePath;
		else
			file = "E:\\Documents\\eclipse\\Java\\BMT\\BMT\\ants\\test\\resources\\testExp.bmt";
		
		ui.click(new XYLocator(new ShellLocator("Behavioral Monitoring Tool"),
				127, 1));
		ui.click(new MenuItemLocator("Experiment/Load Exp."));
		DialogBoxUtils.fillDialog(file,ui);
	}
	
	public void exportToExcel(String filePath) throws WidgetSearchException{
		String file;
		if ((filePath != null) && (filePath != ""))
			file = filePath;
		else
			file = "E:\\Documents\\eclipse\\Java\\BMT\\BMT\\ants\\test\\resources\\TestExp_excel.xlsx";
		
		ui.click(new MenuItemLocator("Experiment/Export to Excel"));
		DialogBoxUtils.fillDialog(file, ui);
	}
}