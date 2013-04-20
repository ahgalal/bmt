package gui.integration;

import sys.utils.EnvVar;
import sys.utils.Files;

/**
 * 
 */

/**
 * @author Creative
 *
 */
public class ForcedSwimmingIntegration1280x720FrameTest extends
		ForcedSwimmingIntegrationTest {
	@Override
	public void setUp() {
		super.setUp();
		
		videoFile = Files.convertPathToPlatformPath(EnvVar
				.getEnvVariableValue("BMT_WS")
				+ "/BMT/ants/test/resources/FST_1280x720.avi");
	}
}
