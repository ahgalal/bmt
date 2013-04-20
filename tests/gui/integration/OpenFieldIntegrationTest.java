package gui.integration;

import gui.executionunit.ExperimentExecUnitGroup;
import gui.executionunit.VideoExecUnitGroup;
import gui.executionunit.ZonesExecUnitGroup;
import gui.utils.Reflections;
import modules.experiment.Constants;
import modules.experiment.Experiment;
import modules.experiment.Group;
import modules.experiment.Rat;
import sys.utils.EnvVar;
import sys.utils.Files;
import sys.utils.Utils;
import utils.PManager;

import com.windowtester.runtime.WidgetSearchException;

/**
 * Tests the integration of Open Field Experiment, that all filters/modules work
 * as expected.
 * 
 * @author Creative
 */
public class OpenFieldIntegrationTest extends ExperimentIntegrationTestBase {

	protected int		allEntranceMax		= 23;
	protected int		allEntranceMin		= 21;
	protected int		centralEntranceMax	= 5;
	protected int		centralEntranceMin	= 4;
	protected int		centralTimeMax		= 3;
	protected int		centralTimeMin		= 1;
	protected String	scale				= "60";

	protected int		totalDistanceMax	= 350;
	protected int		totalDistanceMin	= 240; // TODO: tighten value
	protected String	zonesFile;

	protected void afterLoadZones() throws Exception {
		/************* set scale ********/
		preSetScale();
		ZonesExecUnitGroup.setScale(scale);

		afterSettingScale();
	}

	protected void afterSetBackground() throws Exception {
		/************* load zones ********/
		preLoadZones();
		ZonesExecUnitGroup.loadZones(zonesFile);

		afterLoadZones();
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
/*
		*//************* start tracking ********//*
		preStartTracking();
		startTracking();
		Thread.sleep(1000);*/
	}
	
	@Override
	protected void afterStartTracking() throws WidgetSearchException {
		/************* resume streaming ********/
		VideoExecUnitGroup.pauseResumeStream();
		
		super.afterStartTracking();
	}
	
	@Override
	protected void preStartTracking() throws Exception {
		/************* set background ********/
		preSetBackground();
		Utils.sleep(500);
		ExperimentExecUnitGroup.setBackground();

		afterSetBackground();
	}

	/**
	 * Checks recorded rat parameters.
	 */
	@Override
	protected void checks() {
		super.checks();
		final Experiment exp = Reflections.getLoadedExperiment();

		final Group grp = exp.getGroupByName(groupName);
		final Rat rat0 = grp.getRatByNumber(ratNumber);

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

	/**
	 * Executed before loading zones file (before opening Zone Editor dialog).
	 * 
	 * @throws WidgetSearchException
	 */
	protected void preLoadZones() throws WidgetSearchException {
	}

	/**
	 * Executed before setting background.
	 * 
	 * @throws WidgetSearchException
	 */
	protected void preSetBackground() throws WidgetSearchException {
	}

	/**
	 * Executed before setting scale (before opening Zone Editor dialog).
	 * 
	 * @throws WidgetSearchException
	 */
	protected void preSetScale() throws WidgetSearchException {
	}

	@Override
	public void setUp() {
		super.setUp();
		expFileName_orig = Files.convertPathToPlatformPath(EnvVar
				.getEnvVariableValue("BMT_WS")
				+ "/BMT/ants/test/resources/TestOpenField.bmt");
		expFileName = expFileName_orig + "_tmp";

		videoFile = Files.convertPathToPlatformPath(EnvVar
				.getEnvVariableValue("BMT_WS")
				+ "/BMT/ants/test/resources/OF_basic_wmv2.avi");

		zonesFile = Files.convertPathToPlatformPath(EnvVar
				.getEnvVariableValue("BMT_WS")
				+ "/BMT/ants/test/resources/zones_test.bmt");

		sessionTimeMin = 17; // TODO: tighten
		sessionTimeMax = 24;
		sleepTime1 = 24;
		sleepTime2 = 0;
		sleepTime3 = 0;

		copyExperimentFile();
	}
}