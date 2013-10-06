package gui.experiment;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests creating new experiments of types "Open Field" and "Forced Swimming",
 * and run a tracking session for each created experiment.
 * 
 * @author Creative
 * 
 */
public class CreateExperimentSuiteTest  {

	public static Test suite() {
		final TestSuite suite = new TestSuite(
				CreateExperimentSuiteTest.class.getName());
		// $JUnit-BEGIN$
		suite.addTestSuite(CreateOFExperimentTest.class);
		suite.addTestSuite(CreateFSExperimentTest.class);
		suite.addTestSuite(CreateOFExperimentTest.class);
		// $JUnit-END$
		return suite;
	}

}
