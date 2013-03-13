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
public class OpenFieldExperiment extends Experiment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3168953419336453770L;

	public OpenFieldExperiment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initializeFiltersSetup() {
		final FiltersNamesRequirements openFieldFiltersRequirements = new FiltersNamesRequirements();
		final FiltersConnectionRequirements openFieldConnectionRequirements = new FiltersConnectionRequirements();

		// required filters
		openFieldFiltersRequirements
				.addFilter("SourceFilter", "filters.source");
		openFieldFiltersRequirements.addFilter("ScreenDrawer",
				"filters.screendrawer");
		openFieldFiltersRequirements.addFilter("ScreenDrawerSec",
				"filters.screendrawer");
		openFieldFiltersRequirements
				.addFilter("RatFinder", "filters.ratfinder");
		openFieldFiltersRequirements.addFilter("RearingDetector",
				"filters.rearingdetector");
		openFieldFiltersRequirements.addFilter("Recorder",
				"filters.videorecorder");
		openFieldFiltersRequirements.addFilter("SubtractionFilter",
				"filters.subtractor");
		openFieldFiltersRequirements.addFilter("AverageFilter",
				"filters.average");

		// connections
		openFieldConnectionRequirements.connectFilters("SourceFilter",
				"ScreenDrawer");
		openFieldConnectionRequirements.connectFilters("SourceFilter",
				"Recorder");
		openFieldConnectionRequirements.connectFilters("SourceFilter",
				"SubtractionFilter");
		openFieldConnectionRequirements.connectFilters("SubtractionFilter",
				"AverageFilter");
		openFieldConnectionRequirements.connectFilters("SubtractionFilter",
				"RearingDetector");
		openFieldConnectionRequirements.connectFilters("RatFinder",
				"ScreenDrawerSec");
		openFieldConnectionRequirements.connectFilters("AverageFilter",
				"RatFinder");

		filtersSetup = new FiltersSetup(openFieldFiltersRequirements,
				openFieldConnectionRequirements);
	}

	@Override
	protected void initializeParams() {
		final String[] expParameters = new String[] {
				Constants.FILE_REARING_COUNTER, Constants.FILE_ALL_ENTRANCE,
				Constants.FILE_CENTRAL_ENTRANCE, Constants.FILE_CENTRAL_TIME,
				Constants.FILE_TOTAL_DISTANCE, Constants.FILE_SESSION_TIME};
		setParametersList(expParameters);
	}

}
