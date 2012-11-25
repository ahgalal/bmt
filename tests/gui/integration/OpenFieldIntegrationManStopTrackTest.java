package gui.integration;

import gui.executionunit.ExperimentExecUnitGroup;

import com.windowtester.runtime.WidgetSearchException;

/**
 * User manually stops tracking, Expected: experiment is saved correctly, with
 * all correct values of parameters
 * 
 * @author Creative
 */
public class OpenFieldIntegrationManStopTrackTest extends
		OpenFieldIntegrationTest {

	@Override
	protected void preSleep2() throws WidgetSearchException {
		super.preSleep2();

		// stop tracking
		ExperimentExecUnitGroup.stopTracking();
	}

	@Override
	public void setUp() {
		super.setUp();
		sleepTime1 = 10;
		sleepTime2 = 5;
		sessionTimeMin = 10;
		sessionTimeMax = 10;
		centralTimeMin = centralEntranceMax = 0;
		centralEntranceMin = centralEntranceMax = 0;
		allEntranceMin = 10;
		allEntranceMax = 12;
		totalDistanceMin = 180;
		totalDistanceMax = 220;
	}

}
