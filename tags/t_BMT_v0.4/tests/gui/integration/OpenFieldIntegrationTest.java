package gui.integration;

import gui.executionunit.ExperimentExecUnitGroup;
import gui.executionunit.VideoExecUnitGroup;
import gui.executionunit.ZonesExecUnitGroup;
import gui.utils.Reflections;
import gui.utils.UITest;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CancellationException;

import modules.experiment.Constants;
import modules.experiment.Experiment;
import modules.experiment.Group;
import modules.experiment.Rat;

import org.apache.tools.ant.util.FileUtils;

import com.windowtester.runtime.WidgetSearchException;

import sys.utils.EnvVar;
import sys.utils.Files;
import utils.PManager;
import utils.Utils;

/**
 * Tests the integration of Open Field Experiment, that all filters/modules work
 * as expected.
 * 
 * @author Creative
 */
public class OpenFieldIntegrationTest extends UITest {

	protected int		allEntranceMax		= 23;
	protected int		allEntranceMin		= 21;
	protected int		centralEntranceMax	= 5;
	protected int		centralEntranceMin	= 4;
	protected int		centralTimeMax		= 3;
	protected int		centralTimeMin		= 1;
	private String		expFileNameOF;
	protected String	expFileNameOF_orig;
	protected String	groupName			= "Group1";
	protected int		ratNumber			= 0;
	protected String	scale				= "60";
	protected int	sessionTimeMin			= 23;
	protected int	sessionTimeMax			= 25;
	protected int		sleepTime1			= 30;
	protected int		sleepTime2			= 0;
	protected int		sleepTime3			= 0;
	protected int		totalDistanceMax	= 420;
	protected int		totalDistanceMin	= 320;
	protected String	videoFile;
	protected String	zonesFile;
	protected boolean terminateTest=false;

	private void checkParamValue(final Rat rat, final String param,
			final int valueMin, final int valueMax) {
		final String actualStrValue = rat.getValueByParameterName(param);
		final double actualDblValue = Double.parseDouble(actualStrValue);
		assert ((actualDblValue <= valueMax) && (actualDblValue >= valueMin)) : "Unexpected param value for: "
				+ param
				+ " expected valueMin/Max: "
				+ valueMin
				+ "/"
				+ valueMax + " ,actual value: " + actualStrValue;
	}

	private void checkParamValue(final Rat rat, final String param,
			final String value) {
		final String actualValue = rat.getValueByParameterName(param);
		assert (actualValue.equals(value)) : "Unexpected param value for: "
				+ param + " expected value: " + value + " ,actual value: "
				+ actualValue;
	}

	/**
	 * Executed before checking experiment's params' values.
	 * @throws WidgetSearchException
	 */
	protected void preChecking()  throws WidgetSearchException{
	}

	/**
	 * Executed before loading an experiment.
	 * @throws WidgetSearchException
	 */
	protected void preLoadExperiment() throws WidgetSearchException {
	}

	/**
	 * Executed before loading video file.
	 * @throws WidgetSearchException
	 */
	protected void preLoadVideoFile() throws WidgetSearchException {
	}

	/**
	 * Executed before loading zones file (before opening Zone Editor dialog).
	 * @throws WidgetSearchException
	 */
	protected void preLoadZones() throws WidgetSearchException {
	}

	/**
	 * Executed before setting background.
	 * @throws WidgetSearchException
	 */
	protected void preSetBackground() throws WidgetSearchException {
	}

	/**
	 * Executed before setting scale (before opening Zone Editor dialog).
	 * @throws WidgetSearchException
	 */
	protected void preSetScale()  throws WidgetSearchException{
	}

	protected void preSleep1()  throws WidgetSearchException{
	}

	protected void preSleep2() throws WidgetSearchException {
	}

	protected void preSleep3() throws WidgetSearchException {
	}

	/**
	 * Executed before starting tracking.
	 * @throws WidgetSearchException
	 * @throws Exception 
	 */
	protected void preStartTracking() throws Exception{
	}

	@Override
	public void setUp() {
		super.setUp();
		expFileNameOF_orig = Files.convertPathToPlatformPath(EnvVar
				.getEnvVariableValue("BMT_WS")
				+ "/BMT/ants/test/resources/TestOpenField.bmt");
		expFileNameOF = expFileNameOF_orig + "_tmp";

		videoFile = Files.convertPathToPlatformPath(EnvVar
				.getEnvVariableValue("BMT_WS")
				+ "/BMT/ants/test/resources/OF_basic_wmv2.avi");

		zonesFile = Files.convertPathToPlatformPath(EnvVar
				.getEnvVariableValue("BMT_WS")
				+ "/BMT/ants/test/resources/zones_test.bmt");

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
		
		try {
			fullScenario();
		} catch (CancellationException e) {
			PManager.log.print("Test is explicitly terminated", this);
		}
	}

	protected void fullScenario() throws WidgetSearchException, Exception,
			InterruptedException {
		/************* load experiment ********/
		preLoadExperiment();
		ExperimentExecUnitGroup.loadExperiment(expFileNameOF);
		
		afterExperimentLoad();
	}

	protected void afterExperimentLoad() throws Exception {
		/************* load video file ********/
		preLoadVideoFile();
		VideoExecUnitGroup.startStreamVideo(videoFile);

		afterVideoLoad();
	}

	protected void afterVideoLoad() throws Exception {
		/************* set background ********/
		preSetBackground();
		Utils.sleep(500);
		ExperimentExecUnitGroup.setBackground();

		afterSetBackground();
	}

	protected void afterSetBackground() throws Exception {
		/************* load zones ********/
		preLoadZones();
		ZonesExecUnitGroup.loadZones(zonesFile);
		
		afterLoadZones();
	}

	protected void afterLoadZones() throws Exception {
		/************* set scale ********/
		preSetScale();
		ZonesExecUnitGroup.setScale(scale);

		afterSettingScale();
	}

	protected void afterSettingScale() throws Exception {
		/************* resume streaming ********/
		VideoExecUnitGroup.pauseResumeStream();
		// wait till rat enters the arena
		Utils.sleep(1000);
		// pause stream
		VideoExecUnitGroup.pauseResumeStream();
		// wait a while :D
		Utils.sleep(200);

		/************* start tracking ********/
		preStartTracking();
		ExperimentExecUnitGroup.startTracking(Integer.toString(ratNumber));
		Thread.sleep(1000);

		/************* resume streaming ********/
		VideoExecUnitGroup.pauseResumeStream();

		afterStartTracking();
	}

	protected void afterStartTracking() throws WidgetSearchException {
		/************* sleep ********/
		sleepings();

		/************* checking ********/
		preChecking();
		checks();
	}

	protected void sleepings() throws WidgetSearchException {
		preSleep1();
		Utils.sleep(sleepTime1*1000);
		preSleep2();
		Utils.sleep(sleepTime2*1000);
		preSleep3();
		Utils.sleep(sleepTime3*1000);
	}

	/**
	 * Checks recorded rat parameters.
	 */
	protected void checks() {
		final Experiment exp = Reflections.getLoadedExperiment();

		final Group grp = exp.getGroupByName(groupName);
		final Rat rat0 = grp.getRatByNumber(ratNumber);

		checkParamValue(rat0, Constants.FILE_RAT_NUMBER, ratNumber, ratNumber);
		checkParamValue(rat0, Constants.FILE_GROUP_NAME, groupName);
		checkParamValue(rat0, Constants.FILE_SESSION_TIME, sessionTimeMin,sessionTimeMax);
		checkParamValue(rat0, Constants.FILE_ALL_ENTRANCE, allEntranceMin,
				allEntranceMax);
		checkParamValue(rat0, Constants.FILE_CENTRAL_ENTRANCE,
				centralEntranceMin, centralEntranceMax);
		checkParamValue(rat0, Constants.FILE_CENTRAL_TIME, centralTimeMin,
				centralTimeMax);
		checkParamValue(rat0, Constants.FILE_TOTAL_DISTANCE, totalDistanceMin,
				totalDistanceMax);
		// TODO: check rearing counter
		// checkParamValue(rat0, Constants.FILE_REARING_COUNTER, "");
		PManager.log.print("Experiment info check: OK", this);
	}

}