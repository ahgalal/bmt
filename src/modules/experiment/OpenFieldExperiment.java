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
import filters.avg.AverageFilter;
import filters.ratfinder.RatFinder;
import filters.rearingdetection.RearingDetector;
import filters.recorder.VideoRecorder;
import filters.screendrawer.ScreenDrawer;
import filters.source.SourceFilter;
import filters.subtractionfilter.SubtractorFilter;
import filters.zonesdrawer.ZonesDrawerFilter;

/**
 * @author Creative
 */
public class OpenFieldExperiment extends Experiment {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3168953419336453770L;

	public OpenFieldExperiment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initializeFiltersSetup() {
		final FiltersNamesRequirements openFieldFiltersRequirements = new FiltersNamesRequirements();
		final FiltersConnectionRequirements openFieldConnectionRequirements = new FiltersConnectionRequirements();

		// required filters
		openFieldFiltersRequirements.addFilter("SourceFilter",
				SourceFilter.ID, FilterTrigger.STREAMING);
		openFieldFiltersRequirements.addFilter("ScreenDrawer",
				ScreenDrawer.ID, FilterTrigger.STREAMING);
		openFieldFiltersRequirements.addFilter("ScreenDrawerSec",
				ScreenDrawer.ID, FilterTrigger.PROCESSING);
		openFieldFiltersRequirements.addFilter("RatFinder",
				RatFinder.ID, FilterTrigger.PROCESSING);
		openFieldFiltersRequirements.addFilter("RearingDetector",
				RearingDetector.ID, FilterTrigger.PROCESSING);
		openFieldFiltersRequirements.addFilter("Recorder",
				VideoRecorder.ID, FilterTrigger.MANUAL);
		openFieldFiltersRequirements.addFilter("SubtractionFilter",
				SubtractorFilter.ID, FilterTrigger.PROCESSING);
		openFieldFiltersRequirements.addFilter("AverageFilter",
				AverageFilter.ID, FilterTrigger.PROCESSING);
		openFieldFiltersRequirements.addFilter("ZonesDrawer",
				ZonesDrawerFilter.ID, FilterTrigger.PROCESSING);

		// connections
		openFieldConnectionRequirements.connectFilters("SourceFilter",
				"ZonesDrawer");
		openFieldConnectionRequirements.connectFilters("ZonesDrawer",
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
	protected void initializeModulesSetup() {
		final ModulesNamesRequirements modulesRequirements = new ModulesNamesRequirements();
		modulesRequirements.addModule("RearingModule", RearingModule.moduleID);
		modulesRequirements.addModule("ZonesModule", ZonesModule.moduleID);
		modulesRequirements.addModule("SessionModule", SessionModule.moduleID);

		modulesSetup = new ModulesSetup(modulesRequirements);
	}

	@Override
	protected void initializeParams() {
		final String[] expParameters = new String[] {
				Constants.FILE_REARING_COUNTER, Constants.FILE_ALL_ENTRANCE,
				Constants.FILE_CENTRAL_ENTRANCE, Constants.FILE_CENTRAL_TIME,
				Constants.FILE_TOTAL_DISTANCE, Constants.FILE_SESSION_TIME };
		setParametersList(expParameters);
	}

}
