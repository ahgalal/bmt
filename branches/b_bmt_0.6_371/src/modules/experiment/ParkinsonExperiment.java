/**
 * 
 */
package modules.experiment;

import java.util.ArrayList;

import modules.ModulesNamesRequirements;
import modules.ModulesSetup;
import modules.headmotion.HeadMotionModule;
import modules.session.SessionModule;
import modules.zones.ZonesModule;
import filters.FiltersConnectionRequirements;
import filters.FiltersNamesRequirements;
import filters.FiltersNamesRequirements.FilterTrigger;
import filters.FiltersSetup;
import filters.headangle.HeadAngleFilter;
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
		modulesRequirements.addModule("HeadMotionModule", HeadMotionModule.moduleID);

		modulesSetup = new ModulesSetup(modulesRequirements);
	}

	/*
	 * (non-Javadoc)
	 * @see modules.experiment.Experiment#initializeParams()
	 */
	@Override
	protected void initializeParams() {
		ArrayList<String> tmpParams = new ArrayList<String>();
		tmpParams.add(Constants.FILE_ALL_ENTRANCE);
		tmpParams.add(Constants.FILE_CENTRAL_ENTRANCE);
		tmpParams.add(Constants.FILE_CENTRAL_TIME);
		tmpParams.add(Constants.FILE_TOTAL_DISTANCE);
		tmpParams.add(Constants.FILE_SESSION_TIME);
		
		// FIXME: architectural issue with modules contributing dynamic list of params
		String[] headMotionParams = HeadMotionModule.getParams();
		for(String param:headMotionParams)
			tmpParams.add(param);
		
		final String[] expParameters = tmpParams.toArray(new String[0]);
		setParametersList(expParameters);
	}

}
