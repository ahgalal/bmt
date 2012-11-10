package gui;

import gui.executionunit.ExperimentExecUnitGroup;
import modules.experiment.ExperimentType;

import com.windowtester.runtime.swt.UITestCaseSWT;

/**
 * Tests creating new experiments of types "Open Field" and "Forced Swimming",
 * by saving the experiment info to file, then loads the file and compares the
 * saved info with the created experiment.
 * @author Creative
 *
 */
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
	public void testSaveExperimentOF() throws Exception {
		new ExperimentExecUnitGroup(getUI());
		ExperimentExecUnitGroup.createNewExperiment(ExperimentType.OPEN_FIELD);
	}
	
	public void testSaveExperimentFS() throws Exception {
		new ExperimentExecUnitGroup(getUI());
		ExperimentExecUnitGroup.createNewExperiment(ExperimentType.FORCED_SWIMMING);
	}

}