/**
 * 
 */
package modules.experiment;

import filters.FiltersConnectionRequirements;
import filters.FiltersNamesRequirements;
import filters.FiltersSetup;

/**
 * @author Creative
 *
 */
public class ForcedSwimmingExperiment extends Experiment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7438910585355105010L;

	/* (non-Javadoc)
	 * @see modules.experiment.Experiment#initializeParams()
	 */
	@Override
	protected void initializeParams() {
		final String[] expParameters = new String[] {
				Constants.CLIMBING, Constants.SWIMMING,
				Constants.FLOATING, Constants.FILE_SESSION_TIME};
		setParametersList(expParameters);
	}

	/* (non-Javadoc)
	 * @see modules.experiment.Experiment#initializeFiltersSetup()
	 */
	@Override
	protected void initializeFiltersSetup() {
		FiltersNamesRequirements forcedSwimmingFiltersRequirements = new FiltersNamesRequirements();
		FiltersConnectionRequirements forcedSwimmingConnectionRequirements = new FiltersConnectionRequirements();

		// required filters
		forcedSwimmingFiltersRequirements.addFilter("SourceFilter",
				"filters.source");
		forcedSwimmingFiltersRequirements.addFilter("ScreenDrawer",
				"filters.screendrawer");
		forcedSwimmingFiltersRequirements.addFilter("ScreenDrawerSec",
				"filters.screendrawer");
		forcedSwimmingFiltersRequirements.addFilter("Recorder",
				"filters.videorecorder");
		forcedSwimmingFiltersRequirements.addFilter("MovementMeter",
				"filters.mevementmeter");

		// connections
		forcedSwimmingConnectionRequirements.connectFilters("SourceFilter",
				"ScreenDrawer");
		forcedSwimmingConnectionRequirements.connectFilters("MovementMeter",
				"ScreenDrawerSec");
		forcedSwimmingConnectionRequirements.connectFilters("SourceFilter",
				"Recorder");
		forcedSwimmingConnectionRequirements.connectFilters("SourceFilter",
				"MovementMeter");

		filtersSetup = new FiltersSetup(
				forcedSwimmingFiltersRequirements,
				forcedSwimmingConnectionRequirements);
	}

}
