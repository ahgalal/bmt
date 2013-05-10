package gui.integration;

import gui.executionunit.ExperimentExecUnitGroup;
import gui.executionunit.VideoExecUnitGroup;

import java.io.File;

import sys.utils.Files;
import utils.BMTUtils;
import utils.DialogBoxUtils;
import utils.PManager;

import com.windowtester.runtime.WidgetSearchException;

/**
 * User manually stops tracking, Expected: experiment is saved correctly, with
 * all correct values of parameters.</br> User starts recording while tracking
 * (stream is running), leaves the end tracking event to stop recording.
 * 
 * @author Creative
 */
public class OpenFieldIntegrationManStopTrackTest extends
		OpenFieldIntegrationTest {

	protected final String	recordedVideoFile	= Files.convertPathToPlatformPath(BMTUtils
			.getResourcesDirPath()
			+ "tmpRecorder.avi");
	
	@Override
	protected void preSleep2() throws WidgetSearchException {
		super.preSleep2();

		// stop tracking
		ExperimentExecUnitGroup.stopTracking();
	}
	
	@Override
	protected void preSleep1() throws Exception {
		super.preSleep1();
		
		// start recording
		VideoExecUnitGroup.startRecord();
	}
	
	@Override
	protected void preChecking() throws WidgetSearchException {
		// save video file
		DialogBoxUtils.fillDialog(recordedVideoFile, getUI());
	}
	
	@Override
	protected void checks() {
		// check that the file size of video file is > 1MB
		File f = new File(recordedVideoFile);
		int expectedSize=10000;
		assert(f.exists()):"Video file does not exist";
		assert(f.length()>expectedSize):"Video file size is less than expected: "+ f.length()/1000+ " KB, expected "+expectedSize;
		PManager.log.print("Video file check: OK", this);
		
		// parent checks
		super.checks();
		PManager.log.print("Experiment info check: OK", this);
	}

	@Override
	public void setUp() {
		super.setUp();
		sleepTime1 = 10; // record for 10 secs
		sleepTime2 = 5; // then stop tracking and wait 5 secs
		sleepTime3 = 5; // rest of stream
		sessionTimeMin = 10;
		sessionTimeMax = 11;
		centralTimeMin = 0;centralEntranceMax = 1;
		centralEntranceMin = 0;centralEntranceMax = 1;
		allEntranceMin = 9;
		allEntranceMax = 12;
		totalDistanceMin = 150;
		totalDistanceMax = 220;
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		// delete recorder video file
		Files.deleteFile(recordedVideoFile);
	}

}
