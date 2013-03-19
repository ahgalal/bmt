package gui.experiment;

import com.windowtester.runtime.WidgetSearchException;

import gui.executionunit.ExperimentExecUnitGroup;
import gui.integration.OpenFieldIntegrationTest;
import modules.experiment.ExperimentType;

/**
 * Tests creating new experiments of type "Open Field",
 * by saving the experiment info to file, then loads the file and compares the
 * saved info with the created experiment. then runs a tracking session.
 * @author Creative
 *
 */
public class CreateOFExperimentTest extends OpenFieldIntegrationTest {
	
	@Override
	protected void fullScenario() throws WidgetSearchException, Exception,
			InterruptedException {
		/************* create experiment ********/
		preLoadExperiment();
		ExperimentExecUnitGroup.createNewExperiment(ExperimentType.OPEN_FIELD,expFileName);
		ExperimentExecUnitGroup.checkExperimentCreated();

		afterExperimentLoad();
	}
}