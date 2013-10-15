/**
 * 
 */
package modules.experiment;

import modules.ModulesNamesRequirements;
import modules.ModulesSetup;
import modules.session.SessionModule;
import modules.zones.ZonesModule;
import filters.FiltersConnectionRequirements;
import filters.FiltersNamesRequirements;
import filters.FiltersNamesRequirements.FilterTrigger;
import filters.FiltersSetup;
import filters.headangle.HeadAngleFilter;
import filters.ratfinder.RatFinder;
import filters.recorder.VideoRecorder;
import filters.screendrawer.ScreenDrawer;
import filters.source.SourceFilter;
import filters.zonesdrawer.ZonesDrawerFilter;

/**
 * @author Creative
 */
public class ParkinsonExperiment extends Experiment {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2082444267076170139L;

	/*
	 * (non-Javadoc)
	 * @see modules.experiment.Experiment#initializeFiltersSetup()
	 */
	@Override
	protected void initializeFiltersSetup() {

		final FiltersNamesRequirements openFieldFiltersRequirements = new FiltersNamesRequirements();
		final FiltersConnectionRequirements openFieldConnectionRequirements = new FiltersConnectionRequirements();

		// required filters
		openFieldFiltersRequirements.addFilter("SourceFilter", SourceFilter.ID,
				FilterTrigger.STREAMING);
		openFieldFiltersRequirements.addFilter("ScreenDrawer", ScreenDrawer.ID,
				FilterTrigger.STREAMING);
		openFieldFiltersRequirements.addFilter("ScreenDrawerSec",
				ScreenDrawer.ID, FilterTrigger.PROCESSING);
		openFieldFiltersRequirements.addFilter("HeadAngle", HeadAngleFilter.ID,
				FilterTrigger.PROCESSING);
		openFieldFiltersRequirements.addFilter("Recorder", VideoRecorder.ID,
				FilterTrigger.MANUAL);
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
				"HeadAngle");
		openFieldConnectionRequirements.connectFilters("HeadAngle",
				"ScreenDrawerSec");
		filtersSetup = new FiltersSetup(openFieldFiltersRequirements,
				openFieldConnectionRequirements);
	}

	/*
	 * (non-Javadoc)
	 * @see modules.experiment.Experiment#initializeModulesSetup()
	 */
	@Override
	protected void initializeModulesSetup() {
		final ModulesNamesRequirements modulesRequirements = new ModulesNamesRequirements();
		modulesRequirements.addModule("ZonesModule", ZonesModule.moduleID);
		modulesRequirements.addModule("SessionModule", SessionModule.moduleID);

		modulesSetup = new ModulesSetup(modulesRequirements);
	}

	/*
	 * (non-Javadoc)
	 * @see modules.experiment.Experiment#initializeParams()
	 */
	@Override
	protected void initializeParams() {
		final String[] expParameters = new String[] {
				Constants.FILE_ALL_ENTRANCE, Constants.FILE_CENTRAL_ENTRANCE,
				Constants.FILE_CENTRAL_TIME, Constants.FILE_TOTAL_DISTANCE,
				Constants.FILE_SESSION_TIME };
		setParametersList(expParameters);
	}

}
