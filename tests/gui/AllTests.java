package gui;

import gui.experiment.CreateExperimentSuiteTest;
import gui.experiment.EditExperimentTest;
import gui.experiment.ExportExperimentToExcelTest;
import gui.integration.DiffExperimentsRunGUITest;
import gui.integration.ForcedSwimmingIntegration1280x720FrameTest;
import gui.integration.OpenFieldIntegration1280x720FrameTest;
import gui.integration.OpenFieldIntegrationManStopStreamWhenPausedTest;
import gui.integration.OpenFieldIntegrationManStopTrackTest;
import gui.integration.recorder.OpenFieldIntegrationRecordStreamAutoStopTest;
import gui.integration.recorder.OpenFieldIntegrationRecordStreamManStopTest;
import gui.integration.recorder.OpenFieldIntegrationRecordStreamPausedStopTest;
import gui.integration.zones.OpenFieldIntegrationZonesCreationDeletionTest;
import gui.vidlibs.AGCamLibTest;
import gui.vidlibs.JMyronLibTest;
import gui.vidlibs.V4L2CamLibTest;
import junit.framework.Test;
import junit.framework.TestSuite;
import utils.PManager;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		// Experiment creation
		suite.addTest(CreateExperimentSuiteTest.suite());
		suite.addTestSuite(ExportExperimentToExcelTest.class);
		suite.addTestSuite(EditExperimentTest.class);
		
		// Frame size
		suite.addTestSuite(OpenFieldIntegration1280x720FrameTest.class);
		suite.addTestSuite(ForcedSwimmingIntegration1280x720FrameTest.class);
		
		// Zones
		suite.addTestSuite(OpenFieldIntegrationZonesCreationDeletionTest.class);
		
		// Experiment loading
		suite.addTest(DiffExperimentsRunGUITest.suite());
		
		// Stop Tracking handling
		suite.addTestSuite(OpenFieldIntegrationManStopStreamWhenPausedTest.class);
		suite.addTestSuite(OpenFieldIntegrationManStopTrackTest.class);
		
		// Recorder
		suite.addTestSuite(OpenFieldIntegrationRecordStreamAutoStopTest.class);
		suite.addTestSuite(OpenFieldIntegrationRecordStreamManStopTest.class);
		suite.addTestSuite(OpenFieldIntegrationRecordStreamPausedStopTest.class);
		
		// Video Libs
		suite.addTestSuite(AGCamLibTest.class);
		suite.addTestSuite(JMyronLibTest.class);
		
		if(PManager.getOS().contains("inux"))
			suite.addTestSuite(V4L2CamLibTest.class);
		//$JUnit-END$
		return suite;
	}

}
