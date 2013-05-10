package gui.integration;

import sys.utils.EnvVar;
import sys.utils.Files;

public class OpenFieldIntegration1280x720FrameTest extends
		OpenFieldIntegrationTest {
	@Override
	public void setUp() {
		super.setUp();
		
		videoFile = Files.convertPathToPlatformPath(EnvVar
				.getEnvVariableValue("BMT_WS")
				+ "/BMT/ants/test/resources/OF_basic_wmv2_1280x720.avi");
	}
}
