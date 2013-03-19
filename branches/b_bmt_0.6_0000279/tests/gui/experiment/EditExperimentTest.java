package gui.experiment;

import gui.executionunit.ExperimentExecUnitGroup;
import gui.utils.Reflections;
import gui.utils.UITest;

import java.io.File;

import modules.ExperimentManager;
import modules.experiment.Experiment;

import org.junit.After;

import sys.utils.Utils;

/**
 * Tests editing experiments of types "Open Field" and "Forced Swimming -TODO-",
 * by saving the experiment info to file, then loads the file and compares the
 * saved info with the edited experiment.
 * @author Creative
 *
 */
public class EditExperimentTest extends UITest {

	private final String TEST_EXP_FILE=Utils.getResourcesDirPath()+"TestOpenField.bmt";
	private final String TEST_EXP_EDIT_FILE=Utils.getResourcesDirPath()+"TestOpenField_edit.bmt";
	
	/**
	 * Main test method.
	 */
	public void testEditExperimentOF() throws Exception {
		ExperimentExecUnitGroup.loadExperiment(TEST_EXP_FILE);
		Experiment original = Reflections.getLoadedExperiment();
		ExperimentExecUnitGroup.editExperiment(TEST_EXP_EDIT_FILE);
		Experiment loadedFromFile = ExperimentManager.readExperimentFromFile(TEST_EXP_EDIT_FILE);
		
		assert(loadedFromFile.getDate().equals(original.getDate()));
		assert(loadedFromFile.getGroups().size()==2);
		assert(loadedFromFile.getGroupByID(0).getName().equals(original.getGroupByID(0).getName()));
		assert(loadedFromFile.getGroupByID(0).getNotes().equals(original.getGroupByID(0).getNotes()));
		assert(loadedFromFile.getGroupByID(1)==null);// group 2 is deleted
		assert(loadedFromFile.getGroupByID(2).getName().equals("G_new"));
		assert(loadedFromFile.getName().equals(original.getName()));
		assert(loadedFromFile.getUser().equals(original.getUser()));
		assert(loadedFromFile.getNotes().equals(original.getNotes()));
		assert(loadedFromFile.getNotes().equals(original.getNotes()));
	}
	
	// TODO: test Forced swimming experiment edit
	
	@After
	public void tearDown(){
		File f = new File(TEST_EXP_EDIT_FILE);
		try {
			// delay to be able to delete the save file!
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		f.delete();
	}
}