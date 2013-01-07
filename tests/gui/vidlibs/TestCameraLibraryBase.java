package gui.vidlibs;

import gui.executionunit.ExperimentExecUnitGroup;
import gui.executionunit.VideoExecUnitGroup;
import gui.utils.UITest;
import utils.Utils;
import utils.video.FrameIntArray;
import utils.video.input.VidInputter.SourceType;

/**
 * Tests Video library can:</br> a. start stream</br> b. pause/resume
 * stream</br> c. stop stream</br> d. restart stream</br> e. camera settings
 * modification
 * 
 * @author Creative
 */
public class TestCameraLibraryBase extends UITest {

	protected String		vidLibName		= null;
	private final String	TEST_EXP_FILE	= Utils.getResourcesDirPath()
													+ "TestOpenField.bmt";

	public void testCameraLibrary() throws Exception {
		// Open video options
		VideoExecUnitGroup.openVideoOptions();

		// select input library
		VideoExecUnitGroup.selectVideoLib(vidLibName);

		// confirm options dialog
		VideoExecUnitGroup.confirmVideoOptions();

		// select stream source as camera
		VideoExecUnitGroup.selectVideoSource(SourceType.CAM);
		Utils.sleep(1000);

		// load experiment
		ExperimentExecUnitGroup.loadExperiment(TEST_EXP_FILE);

		// start stream
		VideoExecUnitGroup.startStreamCamera();
		checkStreamData();

		// pause stream
		VideoExecUnitGroup.pauseResumeStream();
		Utils.sleep(1000);
		assert (VideoExecUnitGroup.isVideoManagerPaused()) : "Video Manager is not paused when it should";

		// resume stream
		VideoExecUnitGroup.pauseResumeStream();
		Utils.sleep(2000);
		assert (VideoExecUnitGroup.isVideoManagerPaused() == false) : "Video Manager is not resumed when it should";
		checkStreamData();

		// end stream
		VideoExecUnitGroup.stopStream();
		Utils.sleep(1000);

		// start stream again
		VideoExecUnitGroup.startStreamCamera();
		checkStreamData();

		// end stream
		VideoExecUnitGroup.stopStream();
		Utils.sleep(2000);
	}

	protected void checkStreamData() {
		FrameIntArray fia = VideoExecUnitGroup.getFrameIntArray();
		int[] frameData = fia.getFrameData();
		assert (frameData[frameData.length / 2] != 0) : "Frame data is null";
		System.out.println("Stream data is not null ...OK");
	}

}