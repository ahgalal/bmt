/**
 * 
 */
package gui.integration.recorder;

import gui.executionunit.VideoExecUnitGroup;
import gui.integration.OpenFieldIntegrationTest;

import java.io.File;

import sys.utils.Files;
import sys.utils.Utils;
import utils.DialogBoxUtils;
import utils.PManager;

import com.windowtester.runtime.WidgetSearchException;

/**
 * User starts recording while stream is paused, and stops recording when stream
 * is paused again.
 * 
 * @author Creative
 */
public class OpenFieldIntegrationRecordStreamPausedStopTest extends
		OpenFieldIntegrationTest {
	protected final String	recordedVideoFile	= Files.convertPathToPlatformPath(Utils
														.getResourcesDirPath()
														+ "tmpRecorder.avi");

	@Override
	protected void checks() {
		// check that the file size of video file is > 1MB
		final File f = new File(recordedVideoFile);
		final int expectedSize = 10000;
		assert (f.exists()) : "Video file does not exist";
		assert (f.length() > expectedSize) : "Video file size is less than expected: "
				+ f.length() / 1000 + " KB, expected " + expectedSize;
		PManager.log.print("Recorded video file check: OK", this);

		super.checks();
	}

	@Override
	protected void preSleep1() throws WidgetSearchException {
		// pause stream
		VideoExecUnitGroup.pauseResumeStream();

		// delay
		Utils.sleep(200);

		// start recording
		VideoExecUnitGroup.startRecord();

		// delay
		Utils.sleep(200);

		// resume stream
		VideoExecUnitGroup.pauseResumeStream();
	}

	@Override
	protected void preSleep3() throws WidgetSearchException {
		// pause stream
		VideoExecUnitGroup.pauseResumeStream();

		// delay
		Utils.sleep(200);

		// stop recording
		VideoExecUnitGroup.stopRecord();

		// save video file
		DialogBoxUtils.fillDialog(recordedVideoFile, getUI());

		// resume stream
		VideoExecUnitGroup.pauseResumeStream();
	}

	@Override
	public void setUp() {
		super.setUp();
		sleepTime1 = 3; // after 3 seconds, start recording
		sleepTime2 = 7; // after 7 seconds, stop recording
		sleepTime3 = 10; // remaining of 25, to complete the experiment
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		// delete recorder video file
		Files.deleteFile(recordedVideoFile);
	}
}
