package gui.experiment;

import gui.executionunit.ExperimentExecUnitGroup;
import gui.utils.Reflections;
import gui.utils.UITest;
import modules.ExperimentManager;
import modules.experiment.Experiment;
import modules.experiment.ExperimentType;
import utils.Utils;

/**
 * Tests creating new experiments of types "Open Field" and "Forced Swimming",
 * by saving the experiment info to file, then loads the file and compares the
 * saved info with the created experiment.
 * @author Creative
 *
 */
public class CreateExperimentTest extends UITest {

	public void testCreateExperimentOF() throws Exception {
		ExperimentExecUnitGroup.createNewExperiment(ExperimentType.OPEN_FIELD);
		checkExperimentCreated();
	}
	
	public void testCreateExperimentFS() throws Exception {
		ExperimentExecUnitGroup.createNewExperiment(ExperimentType.FORCED_SWIMMING);
		checkExperimentCreated();
	}
	
	private void checkExperimentCreated(){
		// get the stored experiment object using reflection
		Experiment saved = Reflections.getLoadedExperiment();
		
		// load the experiment saved in the file
		Experiment loadedFromFile = ExperimentManager.readExperimentFromFile(saved.fileName);
		
		// compare the two experiments
		assert(Utils.compareExperiments(saved, loadedFromFile)):"Experiments do not match!";
	}

}