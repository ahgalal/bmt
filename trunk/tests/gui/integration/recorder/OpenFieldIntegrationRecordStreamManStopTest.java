/**
 * 
 */
package gui.integration.recorder;

import java.io.File;

import gui.executionunit.VideoExecUnitGroup;
import gui.integration.OpenFieldIntegrationTest;
import sys.utils.Files;
import utils.DialogBoxUtils;
import utils.PManager;
import utils.Utils;

import com.windowtester.runtime.WidgetSearchException;

/**
 * User starts recording while tracking (stream is running), and stops recording
 * before end tracking.
 * 
 * @author Creative
 */
public class OpenFieldIntegrationRecordStreamManStopTest extends
		OpenFieldIntegrationTest {
	protected final String	recordedVideoFile	= Files.convertPathToPlatformPath(Utils
														.getResourcesDirPath()
														+ "tmpRecorder.avi");

	@Override
	protected void preSleep2() throws WidgetSearchException {
		super.preSleep2();
		// start recording
		VideoExecUnitGroup.StartRecord();
	}

	@Override
	protected void preSleep3() throws WidgetSearchException {
		super.preSleep3();
		// stop recording
		VideoExecUnitGroup.StopRecord();

		// save video file
		DialogBoxUtils.fillDialog(recordedVideoFile, getUI());
	}

	@Override
	public void setUp() {
		super.setUp();
		sleepTime1 = 3; // after 3 seconds, start recording
		sleepTime2 = 7; // after 7 seconds, stop recording
		sleepTime3 = 10; // remaining of 25, to complete the experiment
	}
	
	@Override
	protected void checks() {
		// check that the file size of video file is > 1MB
		File f = new File(recordedVideoFile);
		int expectedSize=10000;
		assert(f.exists()):"Video file does not exist";
		assert(f.length()>expectedSize):"Video file size is less than expected: "+ f.length()/1000+ " KB, expected "+expectedSize;
		PManager.log.print("Recorded video file check: OK", this);
		super.checks();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		// delete recorder video file
		Files.deleteFile(recordedVideoFile);
	}
}
