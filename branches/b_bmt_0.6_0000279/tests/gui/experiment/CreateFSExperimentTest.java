package gui.experiment;

import gui.executionunit.ExperimentExecUnitGroup;
import gui.integration.ForcedSwimmingIntegrationTest;
import modules.experiment.ExperimentType;

import com.windowtester.runtime.WidgetSearchException;

/**
 * Tests creating new experiments of type "Forced Swimming",
 * by saving the experiment info to file, then loads the file and compares the
 * saved info with the created experiment. then runs a tracking session.
 * @author Creative
 *
 */
public class CreateFSExperimentTest extends ForcedSwimmingIntegrationTest {
	
	@Override
	protected void fullScenario() throws WidgetSearchException, Exception,
			InterruptedException {
		/************* create experiment ********/
		preLoadExperiment();
		ExperimentExecUnitGroup.createNewExperiment(ExperimentType.FORCED_SWIMMING,expFileName);
		ExperimentExecUnitGroup.checkExperimentCreated();

		afterExperimentLoad();
	}
	
}