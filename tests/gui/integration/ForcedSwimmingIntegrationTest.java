package gui.integration;

import gui.executionunit.VideoExecUnitGroup;
import gui.utils.Reflections;
import modules.experiment.Constants;
import modules.experiment.Experiment;
import modules.experiment.Group;
import modules.experiment.Rat;
import sys.utils.EnvVar;
import sys.utils.Files;
import utils.PManager;

import com.windowtester.runtime.WidgetSearchException;

/**
 * Tests the integration of Forced Swimming Experiment, that all filters/modules
 * work as expected.
 * 
 * @author Creative
 */
public class ForcedSwimmingIntegrationTest extends
		ExperimentIntegrationTestBase {
	
	// TODO: tighten constrains
	protected int	climbingTimeMax	= 9;
	protected int	climbingTimeMin	= 2;
	protected int	floatingTimeMax	= 26;
	protected int	floatingTimeMin	= 10;
	protected int	swimmingTimeMax	= 25; // need to be ~15
	protected int	swimmingTimeMin	= 4;

	@Override
	protected void afterStartTracking() throws WidgetSearchException {
		// resume tracking
		VideoExecUnitGroup.pauseResumeStream();
		
		super.afterStartTracking();
	}

	@Override
	protected void checks() {
		super.checks();
		final Experiment exp = Reflections.getLoadedExperiment();

		final Group grp = exp.getGroupByName(groupName);
		final Rat rat0 = grp.getRatByNumber(ratNumber);

		checkParamValue(rat0, Constants.CLIMBING, climbingTimeMin,
				climbingTimeMax);
		checkParamValue(rat0, Constants.SWIMMING, swimmingTimeMin,
				swimmingTimeMax);
		checkParamValue(rat0, Constants.FLOATING, floatingTimeMin,
				floatingTimeMax);
		PManager.log.print("Experiment info check: OK", this);
	}

	@Override
	public void setUp() {
		super.setUp();
		expFileName_orig = Files.convertPathToPlatformPath(EnvVar
				.getEnvVariableValue("BMT_WS")
				+ "/BMT/ants/test/resources/TestForcedSwimming.bmt");
		expFileName = expFileName_orig + "_tmp";

		videoFile = Files.convertPathToPlatformPath(EnvVar
				.getEnvVariableValue("BMT_WS")
				+ "/BMT/ants/test/resources/FST_base.avi");

		// TODO: tighten constraints
		sessionTimeMin = 35;
		sessionTimeMax = 37;
		sleepTime1 = 39;
		sleepTime2 = 0;
		sleepTime3 = 0;

		copyExperimentFile();
	}

}