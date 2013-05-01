package gui.integration.filtergraph;

import filters.movementmeter.MovementMeter;
import gui.executionunit.FilterGraphExecutionUnitGroup;
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
		FilterGraphExecutionUnitGroup.openFilterGraph();

		// Delete ZoneDrawer filter
		FilterGraphExecutionUnitGroup.deleteFilter("ZonesDrawer");

		// Add MovementFilter between SoruceFilter and ScreenDrawer
		FilterGraphExecutionUnitGroup.addFilter("Movement", MovementMeter.ID,
				"STREAMING");
		FilterGraphExecutionUnitGroup
				.connectFilters("SourceFilter", "Movement");
		FilterGraphExecutionUnitGroup
				.connectFilters("Movement", "ScreenDrawer");

		// Rename AverageFilter to AverageFilterRenamed
		FilterGraphExecutionUnitGroup.editFilter("AverageFilter",
				"AverageFilterRenamed", null, null);

		FilterGraphExecutionUnitGroup.save();
		Utils.sleep(500);

		super.afterExperimentLoad();
	}
}
