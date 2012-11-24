package gui.integration;

import gui.executionunit.ExperimentExecUnitGroup;
import gui.executionunit.VideoExecUnitGroup;
import gui.executionunit.ZonesExecUnitGroup;
import gui.utils.Reflections;
import gui.utils.UITest;

import java.io.File;
import java.io.IOException;

import modules.experiment.Constants;
import modules.experiment.Experiment;
import modules.experiment.Group;
import modules.experiment.Rat;

import org.apache.tools.ant.util.FileUtils;

import sys.utils.EnvVar;
import sys.utils.Files;
import utils.Utils;

/**
 * Tests the integration of Open Field Experiment, that all filters/modules work
 * as expected.
 * 
 * @author Creative
 */
public class OpenFieldIntegrationTest extends UITest {

	private final String	expFileNameOF		= Files.convertPathToPlatformPath(EnvVar
														.getEnvVariableValue("BMT_WS")
														+ "/BMT/ants/test/resources/TestOpenField_copy.bmt");

	private final String	expFileNameOF_orig	= Files.convertPathToPlatformPath(EnvVar
														.getEnvVariableValue("BMT_WS")
														+ "/BMT/ants/test/resources/TestOpenField.bmt");
	private final String	videoFile			= Files.convertPathToPlatformPath(EnvVar
														.getEnvVariableValue("BMT_WS")
														+ "/BMT/ants/test/resources/OF_basic_wmv2.avi");

	private final String	zonesFile			= Files.convertPathToPlatformPath(EnvVar
														.getEnvVariableValue("BMT_WS")
														+ "/BMT/ants/test/resources/zones_test.bmt");

	private void checkParamValue(final Rat rat, final String param,
			final String value) {
		final String actualValue = rat.getValueByParameterName(param);
		assert (actualValue.equals(value)) : "Unexpected param value for: "
				+ param + " expected value: " + value + " ,actual value: "
				+ actualValue;
	}
	
	private void checkParamValue(final Rat rat, final String param,
			final int valueMin,int valueMax) {
		final String actualStrValue = rat.getValueByParameterName(param);
		int actualIntValue=Integer.parseInt(actualStrValue);
		assert (actualIntValue<=valueMax && actualIntValue>=valueMin ) : "Unexpected param value for: "
				+ param + " expected valueMin/Max: " + valueMin+"/"+valueMax + " ,actual value: "
				+ actualStrValue;
	}

	@Override
	public void setUp() {
		super.setUp();
		try {
			// make a copy of the original file
			FileUtils.getFileUtils().copyFile(new File(expFileNameOF_orig),
					new File(expFileNameOF));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		sys.utils.Files.deleteFile(expFileNameOF);
	}

	public void testIntegrationOF() throws Exception {
		// load experiment
		ExperimentExecUnitGroup.loadExperiment(expFileNameOF);

		// load video file
		VideoExecUnitGroup.startStreamVideo(videoFile);

		Utils.sleep(500);
		// set background
		ExperimentExecUnitGroup.setBackground();
		
		// load zones
		ZonesExecUnitGroup.loadZones(zonesFile);
		
		// set scale
		ZonesExecUnitGroup.setScale("60");
		
		// resume streaming 
		VideoExecUnitGroup.pauseResumeStream();
		// wait till rat enters the arena
		Utils.sleep(1000); 
		// pause stream
		VideoExecUnitGroup.pauseResumeStream();
		// wait a while :D
		Utils.sleep(200);

		// start tracking
		ExperimentExecUnitGroup.startTracking(Integer.toString(0));
		Thread.sleep(1000);

		// resume streaming
		VideoExecUnitGroup.pauseResumeStream();

		// sleep till stream ends
		Thread.sleep(30000);

		// check recorder rat parameters
		final Experiment exp = Reflections.getLoadedExperiment();
		final Group grp = exp.getGroupByName("Group1");
		final Rat rat0 = grp.getRatByNumber(0);

		checkParamValue(rat0, Constants.FILE_RAT_NUMBER, "0");
		checkParamValue(rat0, Constants.FILE_GROUP_NAME, "Group1");
		checkParamValue(rat0, Constants.FILE_SESSION_TIME, "23.0");
		checkParamValue(rat0, Constants.FILE_ALL_ENTRANCE, 22,23);
		checkParamValue(rat0, Constants.FILE_CENTRAL_ENTRANCE, "4");
		checkParamValue(rat0, Constants.FILE_CENTRAL_TIME, 2,3);
		checkParamValue(rat0, Constants.FILE_TOTAL_DISTANCE, 320, 420);
		// TODO: check rearing counter
		//checkParamValue(rat0, Constants.FILE_REARING_COUNTER, "");
	}

}