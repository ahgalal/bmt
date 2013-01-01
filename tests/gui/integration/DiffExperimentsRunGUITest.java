package gui.integration;

import junit.framework.Test;
import junit.framework.TestSuite;

public class DiffExperimentsRunGUITest {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				DiffExperimentsRunGUITest.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(ForcedSwimmingIntegrationTest.class);
		suite.addTestSuite(OpenFieldIntegrationTest.class);
		suite.addTestSuite(ForcedSwimmingIntegrationTest.class);
		//$JUnit-END$
		return suite;
	}

}
