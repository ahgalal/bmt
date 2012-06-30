package gui;

import gui.executionunit.ExperimentExecUnitGroup;

import com.windowtester.runtime.swt.UITestCaseSWT;

/**
 * @author Creative
 */
public class SaveExperimentOLD extends UITestCaseSWT {

	public SaveExperimentOLD() {
		super(utils.PManager.class);
	}
	
	public void testabc() throws Exception {
		ExperimentExecUnitGroup expExecUnitGrp = new ExperimentExecUnitGroup(getUI());
		//expExecUnitGrp.createNewExperiment();
		expExecUnitGrp.loadExperiment(null);
		expExecUnitGrp.editExperiment();
	}

}
