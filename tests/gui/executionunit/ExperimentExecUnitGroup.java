package gui.executionunit;

import gui.utils.Reflections;
import modules.ExperimentManager;
import modules.experiment.Experiment;
import modules.experiment.ExperimentType;

import org.eclipse.swt.widgets.Display;

import sys.utils.Utils;
import ui.MainGUI;
import utils.DialogBoxUtils;
import utils.PManager;
import utils.ReflectUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.ComboItemLocator;
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
	public static void activateMainGUI() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				((MainGUI)ReflectUtils.getField(PManager.mainGUI, "ui")).setActive();		
			}
		});
	}
	public static void setBackground() throws WidgetSearchException{
		ui.click(new ButtonLocator("Snap Background"));
	}
	
	public static void startTracking(String ratNumber) throws WidgetSearchException{
		ui.click(new ButtonLocator("Start Tracking"));
		ui.wait(new ShellShowingCondition("RatInfo"));
		ui.click(new LabeledTextLocator("Group Name"));
		ui.enterText(ratNumber);
		ui.click(new ButtonLocator("OK"));
	}
	
	public static void stopTracking() throws WidgetSearchException{
		ui.click(new ButtonLocator("Stop Tracking"));
	}
	

	/**
	 * Creates a new Experiment and saved it to file.
	 * 
	 * @throws WidgetSearchException
	 */
	public static void createNewExperiment(ExperimentType expType,String fileName) throws WidgetSearchException {
		ui.click(new MenuItemLocator("Experiment/New Exp.."));
		ui.wait(new ShellShowingCondition("Experiment information"));
		ui.click(new LabeledTextLocator("Name:"));
		ui.enterText("TestExperiment");
		ui.click(new LabeledTextLocator("User:"));
		ui.enterText("AGalal");
		if(expType==ExperimentType.OPEN_FIELD)
			ui.click(new ComboItemLocator("Open Field"));
		else if(expType==ExperimentType.FORCED_SWIMMING)
			ui.click(new ComboItemLocator("Forced Swimming"));
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
		DialogBoxUtils.fillDialog(fileName,ui);
		ui.wait(new ShellDisposedCondition("Experiment information"));
	}
	
	/**
	 * Changes values for basic info, and deletes a group and adds a new group.
	 * 
	 * @throws Exception
	 */
	public static void editExperiment(String saveFile) throws Exception {
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
		DialogBoxUtils.fillDialog(saveFile, ui);
		ui.wait(new ShellDisposedCondition("Experiment information"));
	}
	
	/**
	 * Loads experiment using the input filePath. if filePath is Null default
	 * file is used.
	 * 
	 * @param filePath
	 * @throws WidgetSearchException 
	 * @throws Exception
	 */
	public static void loadExperiment(final String filePath) throws WidgetSearchException {
		String file;
		if ((filePath != null) && (filePath != ""))
			file = filePath;
		else
			throw new RuntimeException(filePath + " is not found!");
		
		ui.click(new XYLocator(new ShellLocator("Behavioral Monitoring Tool"),
				127, 1));
		ui.click(new MenuItemLocator("Experiment/Load Exp."));
		DialogBoxUtils.fillDialog(file,ui);
	}
	
	public static void exportToExcel(String filePath) throws WidgetSearchException{
		String file;
		if ((filePath != null) && (filePath != ""))
			file = filePath;
		else
			throw new RuntimeException("Can't find file: "+ filePath);
		
		ui.click(new MenuItemLocator("Experiment/Export to Excel"));
		DialogBoxUtils.fillDialog(file, ui);
	}
	
	public static void checkExperimentCreated(){
		// get the stored experiment object using reflection
		Experiment saved = Reflections.getLoadedExperiment();
		
		// load the experiment saved in the file
		Experiment loadedFromFile = ExperimentManager.readExperimentFromFile(saved.getFileName());
		
		// compare the two experiments
		assert(Utils.compareExperiments(saved, loadedFromFile)):"Experiments do not match!";
		PManager.log.print("Experiment creation check ... OK", ExperimentExecUnitGroup.class);
	}
}
