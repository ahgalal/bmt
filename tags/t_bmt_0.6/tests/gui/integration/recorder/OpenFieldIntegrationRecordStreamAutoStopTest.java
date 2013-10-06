/**
 * 
 */
package gui.integration.recorder;

import utils.DialogBoxUtils;

import com.windowtester.runtime.WidgetSearchException;

/**
 * User starts recording while tracking (stream is running), leaves the end
 * tracking event to stop recording.
 * 
 * @author Creative
 */
public class OpenFieldIntegrationRecordStreamAutoStopTest extends
		OpenFieldIntegrationRecordStreamManStopTest {


	@Override
	protected void preSleep3() throws WidgetSearchException {
		// empty, just to override that of the parent
	}
	
	@Override
	protected void preChecking() throws WidgetSearchException {
		// save video file
		DialogBoxUtils.fillDialog(recordedVideoFile, getUI());
	}

	@Override
	public void setUp() {
		super.setUp();
		sleepTime1 = 3; // after 3 seconds, start recording
		sleepTime2 = 20; // keep tracking running
		sleepTime3 = 0;
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
