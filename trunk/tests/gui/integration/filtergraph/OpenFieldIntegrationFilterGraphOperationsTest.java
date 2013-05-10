package gui.integration.filtergraph;

import filters.movementmeter.MovementMeter;
import gui.executionunit.FilterGraphExecUnitGroup;
import gui.integration.OpenFieldIntegrationTest;
import sys.utils.Utils;

/**
 * Tests Adding/Editing/Deleting of filters, and runs OpenField experiment to
 * check tracking is functioning correctly after Filters' changes.
 * 
 * @author Creative
 */
public class OpenFieldIntegrationFilterGraphOperationsTest extends
		OpenFieldIntegrationTest {
	@Override
	protected void afterExperimentLoad() throws Exception {

		// Open filter graph window
		FilterGraphExecUnitGroup.openFilterGraph();

		// Delete ZoneDrawer filter
		FilterGraphExecUnitGroup.deleteFilter("AverageFilter");
		
		FilterGraphExecUnitGroup
		.connectFilters("SubtractionFilter", "RatFinder");
		
		// Add MovementFilter between SoruceFilter and ScreenDrawer
		FilterGraphExecUnitGroup.addFilter("Movement", MovementMeter.ID,
				"STREAMING");
		
		FilterGraphExecUnitGroup.deleteConnection("SourceFilter", "ZonesDrawer");
		
		FilterGraphExecUnitGroup
				.connectFilters("SourceFilter", "Movement");
		FilterGraphExecUnitGroup
		.connectFilters("Movement", "ZonesDrawer");

		// Rename AverageFilter to AverageFilterRenamed
		FilterGraphExecUnitGroup.editFilter("RearingDetector",
				"RearingDetectorRenamed", null, null);

		FilterGraphExecUnitGroup.save();
		Utils.sleep(500);

		super.afterExperimentLoad();
	}
}
