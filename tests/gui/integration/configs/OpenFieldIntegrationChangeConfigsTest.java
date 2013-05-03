/**
 * 
 */
package gui.integration.configs;

import filters.FilterManager;
import filters.FiltersCollection;
import filters.VideoFilter;
import gui.executionunit.ExperimentExecUnitGroup;
import gui.executionunit.OptionsExecUnitGroup;
import gui.executionunit.VideoExecUnitGroup;
import gui.integration.OpenFieldIntegrationTest;
import modules.Module;
import modules.ModulesManager;
import sys.utils.Utils;
import utils.ReflectUtils;

import com.windowtester.runtime.WidgetSearchException;

/**
 * Test is too complicated :( sorry
 * 
 * @author Creative
 */
public class OpenFieldIntegrationChangeConfigsTest extends
		OpenFieldIntegrationTest {
	private int	pass			= 0;
	private int	rearingThresh	= 12;
	private int	subThreshold	= 100;
	private int	zoneHyst		= 20;

	@Override
	protected void afterExperimentLoad() throws Exception {
		if (pass == 0) {
			setConfigValues();
		} else if (pass == 2) {
			// check params' values
			System.out.println("Checking config params after Experiment load");
			checkConfigValues();
		}

		super.afterExperimentLoad();
	}

	private void checkConfigValues() {
		final String subtractionThresholdActual = getFilterConfigValue(
				"SubtractionFilter", "threshold");
		final String rearingThreshActual = getFilterConfigValue(
				"RearingDetector", "rearingThresh");
		final String zoneHystValueActual = getModuleConfigValue("ZonesModule",
				"hystValue");

		System.out.println("pass: " + pass);
		assert (Integer.parseInt(subtractionThresholdActual) == subThreshold) : "Subtraction threshold does not match: "
				+ subtractionThresholdActual + " expected: " + subThreshold;
		System.out.println("Subtraction Threshold= " + subThreshold + " OK");
		assert (Integer.parseInt(rearingThreshActual) == rearingThresh) : "Rearing threshold does not match: "
				+ rearingThresh + " expected: " + rearingThresh;
		System.out.println("Rearing Threshold= " + rearingThresh + " OK");
		assert (Integer.parseInt(zoneHystValueActual) == zoneHyst) : "Zone Hysteresis does not match: "
				+ zoneHystValueActual + " expected: " + zoneHyst;
		System.out.println("Zone Hysteresis= " + zoneHyst + " OK");
	}

	@Override
	protected void checks() {
		// nothing, as no Experiment params checks are intended in this test
	}

	public String getFilterConfigValue(final String filterName,
			final String configParamName) {

		// get FilterCollection
		final FiltersCollection filtersCollection = (FiltersCollection) ReflectUtils
				.getField(FilterManager.getDefault(), "filters");

		// get filter
		final VideoFilter<?, ?> filter = filtersCollection
				.getFilterByName(filterName);

		// get configs
		final Object configs = ReflectUtils.getField(filter, "configs");

		// get configParam
		final Object param = ReflectUtils.getField(configs, configParamName);

		return param.toString();
	}

	public String getModuleConfigValue(final String moduleName,
			final String configParamName) {

		// get module
		final Module<?, ?, ?> module = ModulesManager.getDefault()
				.getModuleByName(moduleName);

		// get configs
		final Object configs = ReflectUtils.getField(module, "configs");

		// get configParam
		final Object param = ReflectUtils.getField(configs, configParamName);

		return param.toString();
	}

	@Override
	protected void preSleep1() throws Exception {
		super.preSleep1();
		if (pass == 0) {
			// wait for 2 seconds till tracking is fully stable
			Utils.sleep(2000);

			// check params' values
			System.out.println("Checking config params starting Tracking");
			checkConfigValues();

			// alter configuration values
			rearingThresh++;
			subThreshold++;
			zoneHyst++;
			setConfigValues();

			// Stop tracking
			ExperimentExecUnitGroup.stopTracking();

			// Pause stream
			VideoExecUnitGroup.pauseResumeStream();

			// Start tracking again
			ratNumber++;
			pass++;
			afterVideoLoad();

		} else if (pass == 1) {
			// wait for 2 seconds till tracking is fully stable
			Utils.sleep(2000);

			// check params' values
			System.out.println("Checking config params restarting Tracking");
			checkConfigValues();

			// Stop tracking
			ExperimentExecUnitGroup.stopTracking();

			// Stop streaming
			VideoExecUnitGroup.stopStream();

			pass++;
			fullScenario();
		} else if (pass == 2) {
			// wait for 2 seconds till tracking is fully stable
			Utils.sleep(2000);

			// check params' values
			System.out
					.println("Checking config params after Start Tracking of the reloaded experiment");
			checkConfigValues();
		}
	}

	private void setConfigValues() throws WidgetSearchException {
		OptionsExecUnitGroup.openOptionsDialog();
		OptionsExecUnitGroup.setRearingThreshold(rearingThresh);
		OptionsExecUnitGroup.setSubtractionThreshold(subThreshold);
		OptionsExecUnitGroup.setZoneHysteresis(zoneHyst);
		OptionsExecUnitGroup.clickOK();
	}

	@Override
	public void setUp() {
		super.setUp();
		sleepTime1 = 0;
		sleepTime2 = 0;
		sleepTime3 = 0;
	}
}
