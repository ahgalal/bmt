/**
 * 
 */
package modules.experiment;

import modules.ModulesNamesRequirements;
import modules.ModulesSetup;
import modules.rearing.RearingModule;
import modules.session.SessionModule;
import modules.zones.ZonesModule;
import filters.FiltersConnectionRequirements;
import filters.FiltersNamesRequirements;
import filters.FiltersNamesRequirements.FilterTrigger;
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
				.addFilter("SourceFilter", "filters.source",FilterTrigger.STREAMING);
		openFieldFiltersRequirements.addFilter("ScreenDrawer",
				"filters.screendrawer",FilterTrigger.STREAMING);
		openFieldFiltersRequirements.addFilter("ScreenDrawerSec",
				"filters.screendrawer",FilterTrigger.PROCESSING);
		openFieldFiltersRequirements
				.addFilter("RatFinder", "filters.ratfinder",FilterTrigger.PROCESSING);
		openFieldFiltersRequirements.addFilter("RearingDetector",
				"filters.rearingdetector",FilterTrigger.PROCESSING);
		openFieldFiltersRequirements.addFilter("Recorder",
				"filters.videorecorder",FilterTrigger.MANUAL);
		openFieldFiltersRequirements.addFilter("SubtractionFilter",
				"filters.subtractor",FilterTrigger.PROCESSING);
		openFieldFiltersRequirements.addFilter("AverageFilter",
				"filters.average",FilterTrigger.PROCESSING);

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

	@Override
	protected void initializeModulesSetup() {
		ModulesNamesRequirements modulesRequirements = new ModulesNamesRequirements();
		modulesRequirements.addModule("RearingModule", RearingModule.moduleID);
		modulesRequirements.addModule("ZonesModule", ZonesModule.moduleID);
		modulesRequirements.addModule("SessionModule", SessionModule.moduleID);
		
		modulesSetup = new ModulesSetup(modulesRequirements );
	}

}
