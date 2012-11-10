package gui.executionunit;

import java.util.ArrayList;

import modules.ExperimentManager;
import modules.experiment.Experiment;
import modules.experiment.ExperimentType;
import utils.DialogBoxUtils;
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
	

	/**
	 * Creates a new Experiment and saved it to file.
	 * 
	 * @throws WidgetSearchException
	 */
	public static void createNewExperiment(ExperimentType expType) throws WidgetSearchException {
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
		DialogBoxUtils.fillDialog("TestExp.bmt",ui);
		ui.wait(new ShellDisposedCondition("Experiment information"));
		// get the stored experiment object using reflection
		Experiment saved = Reflections.getLoadedExperiment();
		
		// load the experiment saved in the file
		Experiment loadedFromFile = ExperimentManager.readExperimentFromFile(saved.fileName);
		
		// compare the two experiments
		assert(compareExperiments(saved, loadedFromFile)):"Experiments do not match!";
	}
	
	public static boolean equalsOneOf(Object obj,Object[] arr){
		for(Object o:arr)
			if(o.equals(obj))
				return true;
		return false;
	}
	public static boolean equalsOneOf(Object obj,ArrayList<Object> arr){
		for(Object o:arr)
			if(o.equals(obj))
				return true;
		return false;
	}
	
	public static boolean compareExperiments(Experiment exp1,Experiment exp2){
		if(exp1.type!=exp2.type)
			return false;
		if(exp1.getDate().equals(exp2.getDate())==false)
			return false;
		if(exp1.getExpParametersList().length!=exp2.getExpParametersList().length)
			return false;
		for(String str1:exp1.getExpParametersList())
			if(equalsOneOf(str1, exp2.getExpParametersList())==false)
				return false;
		
		// TODO: check actual group data, not just number of groups
		if(exp1.getGroups().size()!=exp2.getGroups().size())
			return false;
		if(exp1.getName().equals(exp2.getName())==false)
			return false;
		if(exp1.getNotes().equals(exp2.getNotes())==false)
			return false;
		if(exp1.getUser().equals(exp2.getUser())==false)
			return false;
		
		return true;
	}
	
	public static class Reflections{
		public static Experiment getLoadedExperiment(){
			Experiment exp=null;
			exp=(Experiment) ReflectUtils.getField(ExperimentManager.getDefault(), "exp");
			return exp;
		}
	}

	/**
	 * Changes values for basic info, and deletes a group and adds a new group.
	 * 
	 * @throws Exception
	 */
	public static void editExperiment() throws Exception {
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
	public static void loadExperiment(final String filePath) throws Exception {
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
	
	public static void exportToExcel(String filePath) throws WidgetSearchException{
		String file;
		if ((filePath != null) && (filePath != ""))
			file = filePath;
		else
			file = "E:\\Documents\\eclipse\\Java\\BMT\\BMT\\ants\\test\\resources\\TestExp_excel.xlsx";
		
		ui.click(new MenuItemLocator("Experiment/Export to Excel"));
		DialogBoxUtils.fillDialog(file, ui);
	}
}
