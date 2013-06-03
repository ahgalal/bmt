package gui;

import gui.experiment.CreateExperimentSuiteTest;
import gui.experiment.EditExperimentTest;
import gui.experiment.ExportExperimentToExcelTest;
import gui.integration.DiffExperimentsRunGUITest;
import gui.integration.ForcedSwimmingIntegration1280x720FrameTest;
import gui.integration.OpenFieldIntegration1280x720FrameTest;
import gui.integration.OpenFieldIntegrationManStopStreamWhenPausedTest;
import gui.integration.OpenFieldIntegrationManStopTrackTest;
import gui.integration.configs.OpenFieldIntegrationChangeConfigsTest;
import gui.integration.filtergraph.OpenFieldIntegrationFilterGraphOperationsTest;
import gui.integration.recorder.OpenFieldIntegrationRecordStreamAutoStopTest;
import gui.integration.recorder.OpenFieldIntegrationRecordStreamManStopTest;
import gui.integration.recorder.OpenFieldIntegrationRecordStreamPausedStopTest;
import gui.integration.zones.OpenFieldIntegrationZonesCreationDeletionTest;
import gui.vidlibs.AGCamLibTest;
import gui.vidlibs.JMyronLibTest;
import gui.vidlibs.V4L2CamLibTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import sys.utils.Arrays;
import sys.utils.EnvVar;
import utils.PManager;

public class AllTests {
	private static String[]		disabledTestsArray;
	private static TestSuite	suite;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void addTest(final Object test) {
		String name = "";
		if (test instanceof TestSuite) {
			name = ((TestSuite) test).getName();
			if (!Arrays.equalsOneOf(name, disabledTestsArray)) {
				suite.addTest((Test) test);
				return;
			}
		} else if (test instanceof Class) {
			name = ((Class) test).getName();
			if (!Arrays.equalsOneOf(name, disabledTestsArray)) {
				suite.addTestSuite((Class<? extends TestCase>) test);
				return;
			}
		}
		System.out.println("Test: " + name + " is DISABLED");
	}

	public static Test suite() {
		suite = new TestSuite(AllTests.class.getName());
		// $JUnit-BEGIN$

		final String disabledTests = EnvVar
				.getEnvVariableValue("DISABLED_TESTS");
		disabledTestsArray = disabledTests.split(",");

		// Experiment creation
		addTest(CreateExperimentSuiteTest.suite());
		addTest(ExportExperimentToExcelTest.class);
		addTest(EditExperimentTest.class);

		// Frame size
		addTest(OpenFieldIntegration1280x720FrameTest.class);
		addTest(ForcedSwimmingIntegration1280x720FrameTest.class);

		// Filters' setup
		addTest(OpenFieldIntegrationFilterGraphOperationsTest.class);

		// Zones
		addTest(OpenFieldIntegrationZonesCreationDeletionTest.class);

		// Experiment loading
		addTest(DiffExperimentsRunGUITest.suite());

		// Stop Tracking handling
		addTest(OpenFieldIntegrationManStopStreamWhenPausedTest.class);
		addTest(OpenFieldIntegrationManStopTrackTest.class);

		// Recorder
		addTest(OpenFieldIntegrationRecordStreamAutoStopTest.class);
		addTest(OpenFieldIntegrationRecordStreamManStopTest.class);
		addTest(OpenFieldIntegrationRecordStreamPausedStopTest.class);

		// Configuration persistence (must be the last testcase, as it alters
		// config. values and will affect successive tests if any)
		addTest(OpenFieldIntegrationChangeConfigsTest.class);

		// Video Libs

		if (PManager.getOS().contains("inux"))
			addTest(V4L2CamLibTest.class);
		else {
			addTest(AGCamLibTest.class);
			addTest(JMyronLibTest.class);
		}
		// $JUnit-END$
		return suite;
	}
}
