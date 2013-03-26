/**
 * 
 */
package modules.experiment;

import modules.ModulesNamesRequirements;
import modules.ModulesSetup;
import modules.movementmeter.MovementMeterModule;
import modules.session.SessionModule;
import filters.FiltersConnectionRequirements;
import filters.FiltersNamesRequirements;
import filters.FiltersNamesRequirements.FilterTrigger;
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
				"filters.source",FilterTrigger.STREAMING);
		forcedSwimmingFiltersRequirements.addFilter("ScreenDrawer",
				"filters.screendrawer",FilterTrigger.STREAMING);
		forcedSwimmingFiltersRequirements.addFilter("ScreenDrawerSec",
				"filters.screendrawer",FilterTrigger.PROCESSING);
		forcedSwimmingFiltersRequirements.addFilter("Recorder",
				"filters.videorecorder",FilterTrigger.MANUAL);
		forcedSwimmingFiltersRequirements.addFilter("MovementMeter",
				"filters.mevementmeter",FilterTrigger.PROCESSING);

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

	@Override
	protected void initializeModulesSetup() {
		ModulesNamesRequirements modulesRequirements = new ModulesNamesRequirements();
		modulesRequirements.addModule("SessionModule", SessionModule.moduleID);
		modulesRequirements.addModule("MovementMeterModule", MovementMeterModule.moduleID);
		
		modulesSetup = new ModulesSetup(modulesRequirements );
	}

}
