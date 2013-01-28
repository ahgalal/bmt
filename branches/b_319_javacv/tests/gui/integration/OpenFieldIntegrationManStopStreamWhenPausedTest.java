/**
 * 
 */
package gui.integration;

import gui.executionunit.VideoExecUnitGroup;

import java.util.concurrent.CancellationException;

import utils.Utils;

/**
 * User manually stops streaming before start tracking (while stream is paused)
 * then loads video file again and starts normal tracking. Expected: second file
 * load shall give correct experiment parameters, and not affected by the first
 * video file load.
 * 
 * @author Creative
 */
public class OpenFieldIntegrationManStopStreamWhenPausedTest extends
		OpenFieldIntegrationTest {
	private boolean	enablePreTracking	= true;

	@Override
	protected void checks() {
		super.checks();
		throw new CancellationException();
	}

	@Override
	protected void preStartTracking() throws Exception {
		super.preStartTracking();
		if (enablePreTracking) {
			// resume stream
			VideoExecUnitGroup.pauseResumeStream();

			// sleep for 2 secs
			Utils.sleep(2000);

			// pause stream
			VideoExecUnitGroup.pauseResumeStream();

			// stop stream
			VideoExecUnitGroup.stopStream();

			// continue the default scenario starting at the point after loading
			// experiment
			enablePreTracking = false;
			afterExperimentLoad();
		}
	}

	@Override
	public void setUp() {
		super.setUp();
		sessionTimeMin = 18;
		sessionTimeMax = 23;
		centralTimeMin = 1;
		sleepTime1 = 25;
	}

}
