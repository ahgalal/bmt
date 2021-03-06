/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package filters;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import ui.PluggedGUI;
import utils.Logger.Details;
import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.ConfigsListener;
import utils.video.FrameIntArray;
import filters.FiltersNamesRequirements.FilterRequirement;
import filters.avg.AverageFilter;
import filters.avg.AverageFilterConfigs;
import filters.movementmeter.MovementMeter;
import filters.movementmeter.MovementMeterFilterConfigs;
import filters.ratfinder.RatFinder;
import filters.ratfinder.RatFinderFilterConfigs;
import filters.rearingdetection.RearingDetector;
import filters.rearingdetection.RearingFilterConfigs;
import filters.recorder.RecorderConfigs;
import filters.recorder.VideoRecorder;
import filters.screendrawer.ScreenDrawer;
import filters.screendrawer.ScreenDrawerConfigs;
import filters.source.SourceFilter;
import filters.source.SourceFilterConfigs;
import filters.subtractionfilter.SubtractionConfigs;
import filters.subtractionfilter.SubtractorFilter;
import filters.zonesdrawer.ZonesDrawerConfigs;
import filters.zonesdrawer.ZonesDrawerFilter;

/**
 * Manager of all filters, to enable, disable filters.
 * 
 * @author Creative
 */
public class FilterManager implements ConfigsListener {
	private static FilterManager	self;

	public static FilterManager getDefault() {
		if (self == null)
			throw new RuntimeException(
					"FilterManager is called before it is instantiated");
		return self;
	}

	private FiltersSetup						activeFiltersSetup;
	private CommonFilterConfigs					commonConfigs;
	private final FiltersConfigurationManager	configurationManager;

	private final FiltersCollection				filters;

	private final ArrayList<String>				filtersTriggeredByProcessing	= new ArrayList<String>();

	private final ArrayList<String>				filtersTriggeredByStreaming		= new ArrayList<String>();

	private final FiltersCollection				installedFilters;

	/**
	 * Initializes the filters' array.
	 * 
	 * @param refFia
	 * @param commonConfigs
	 */
	public FilterManager(final CommonFilterConfigs commonConfigs,
			final FrameIntArray refFia/* , final ExperimentType expType */) {
		PManager.log.print("instantiating..", this, Details.VERBOSE);
		filters = new FiltersCollection();

		installedFilters = new FiltersCollection();
		installedFilters.add(new SourceFilter("", null, null));
		installedFilters.add(new AverageFilter("", null, null));
		installedFilters.add(new MovementMeter("", null, null));
		installedFilters.add(new RatFinder("", null, null));
		installedFilters.add(new RearingDetector("", null, null));
		installedFilters.add(new ScreenDrawer("", null, null));
		installedFilters.add(new SubtractorFilter("", null, null));
		installedFilters.add(new VideoRecorder("", null, null));
		installedFilters.add(new ZonesDrawerFilter("", null, null));

		configurationManager = new FiltersConfigurationManager(
				filters.getFilters());
		this.commonConfigs = commonConfigs;
		initializeConfigs(commonConfigs);
		self = this;
	}

	/**
	 * Adds a filter to the array.
	 * 
	 * @param filter
	 *            filter to be added
	 */
	public void addFilter(final VideoFilter<?, ?> filter) {
		filters.add(filter);
	}

	/**
	 * Applies a configuration object to a filter, using the name of the filter
	 * specified in the configuration object.</br>Also adds the filter
	 * configuration to the list if it doesn't exist.
	 * 
	 * @param cfgs
	 *            configurations object
	 */
	public void applyConfigsToFilter(final FilterConfigs cfgs) {
		configurationManager.applyConfigs(cfgs);
	}

	public void applyConfigsToFiltersByID(final FilterConfigs configs) {
		configurationManager.applyConfigsByID(configs);
	}

	private VideoFilter<?, ?> createFilter(final String filterName,
			final String filterID) {
		VideoFilter<?, ?> filter = null;
		for (final Iterator<VideoFilter<?, ?>> it = installedFilters
				.getIterator(); it.hasNext();) {
			final VideoFilter<?, ?> vf = it.next();

			if (vf.getID().equals(filterID)) {
				filter = vf.newInstance(filterName);
				break;
			}
		}
		return filter;
	}

	/**
	 * Unloads all filters.
	 */
	public void deInitialize() {
		// unregister filters' PluggedGUI from listening to state changes
		// and unload GUI
		for (final PluggedGUI<?> pgui : getFiltersGUI()) {
			PManager.getDefault().removeStateListener(pgui);
			pgui.deInitialize();
		}

		filters.clear();

		// TODO: ??
	}

	/**
	 * Disables all filter.
	 */
	public void disableAll() {
		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
				.hasNext();) {
			final VideoFilter<?, ?> vf = it.next();
			vf.enable(false);
		}
	}

	/**
	 * Enables/Disables a filter using its type.
	 * 
	 * @param type
	 *            type of the filter to enable/disable
	 * @param enable
	 *            enable/disable
	 */
	public void enableFilter(final Class<?> type, final boolean enable) {
		final VideoFilter<?, ?> tmp = getFilterByType(type);
		if (tmp != null)
			tmp.enable(enable);
	}

	/**
	 * Enables/Disables a filter using its name.
	 * 
	 * @param filterName
	 *            name of the filter to enable/disable
	 * @param enable
	 *            enable/disable
	 */
	public void enableFilter(final String filterName, final boolean enable) {
		final VideoFilter<?, ?> tmp = getFilterByName(filterName);
		if (tmp != null)
			tmp.enable(enable);
	}

	/**
	 * Gets a filter using the filter's name.
	 * 
	 * @param name
	 *            name of the filter to retrieve
	 * @return VideoFilter having the name specified
	 */
	public VideoFilter<?, ?> getFilterByName(final String name) {
		return filters.getFilterByName(name);
	}

	/**
	 * Gets a filter using the filter's type.
	 * 
	 * @param type
	 *            type of filter to retrieve (child of VideoFilter class)
	 * @return VideoFilter having type specified (in case of many filters have
	 *         the same type, first one found will be returned)
	 */
	private VideoFilter<?, ?> getFilterByType(final Class<?> type) {
		return filters.getFilterByType(type);
	}

	/**
	 * Gets the array of filters.
	 * 
	 * @return array of filters
	 */
	public Iterator<VideoFilter<?, ?>> getFilters() {
		return filters.getIterator();
	}

	@SuppressWarnings("rawtypes")
	public PluggedGUI[] getFiltersGUI() {
		int validGUIsNumber = 0;
		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
				.hasNext();) {
			final VideoFilter<?, ?> vf = it.next();
			if (vf.getGUI() != null)
				validGUIsNumber++;
		}
		final PluggedGUI[] arr = new PluggedGUI[validGUIsNumber];
		int i = 0;
		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
				.hasNext();) {
			final VideoFilter<?, ?> vf = it.next();
			if (vf.getGUI() != null) {
				arr[i] = vf.getGUI();
				i++;
			}
		}
		return arr;
	}

	public String[] getFiltersIDs() {
		final ArrayList<String> ids = new ArrayList<String>();
		for (final Iterator<VideoFilter<?, ?>> it = installedFilters
				.getIterator(); it.hasNext();) {
			final VideoFilter<?, ?> vf = it.next();
			ids.add(vf.getID());
		}
		return ids.toArray(new String[0]);
	}

	/**
	 * Gets the registered filters names.
	 * 
	 * @return String array containing the names of registered filters
	 */
	public ArrayList<String> getRegisteredFiltersNames() {
		final ArrayList<String> strNames = new ArrayList<String>();

		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
				.hasNext();) {
			final VideoFilter<?, ?> vf = it.next();
			strNames.add(vf.getName());
		}
		return strNames;
	}

	/**
	 * Adds default configuration instance of each installed configuration to
	 * the ConfigurationManager list of configs.
	 * 
	 * @param commonConfigs
	 */
	private void initializeConfigs(final CommonFilterConfigs commonConfigs) {
		this.commonConfigs = commonConfigs;
		final SourceFilterConfigs sourceConfigs = new SourceFilterConfigs(
				"SourceFilter", commonConfigs, null);

		final ScreenDrawerConfigs scrnDrwrCnfgs = new ScreenDrawerConfigs(
				"ScreenDrawer", null, commonConfigs, null);
		final ScreenDrawerConfigs scrnDrwrCnfgsSec = new ScreenDrawerConfigs(
				"ScreenDrawerSec", null, commonConfigs, null);

		final RatFinderFilterConfigs ratFinderConfigs = new RatFinderFilterConfigs(
				"RatFinder", commonConfigs);

		final RearingFilterConfigs rearingConfigs = new RearingFilterConfigs(
				"RearingDetector", 1000, 200, 200, commonConfigs);

		final RecorderConfigs vidRecorderConfigs = new RecorderConfigs(
				"Recorder", commonConfigs);

		final SubtractionConfigs subtractionConfigs = new SubtractionConfigs(
				"SubtractionFilter", SubtractionConfigs.defaultThreshold,
				commonConfigs);

		final AverageFilterConfigs avgFilterConfigs = new AverageFilterConfigs(
				"AverageFilter", commonConfigs);

		final MovementMeterFilterConfigs movementFilterConfigs = new MovementMeterFilterConfigs(
				"MovementMeter", commonConfigs);
		final ZonesDrawerConfigs zonesDrawerConfigs = new ZonesDrawerConfigs(
				"ZonesDrawer", commonConfigs, null);

		configurationManager.reset();
		configurationManager.addConfiguration(sourceConfigs, false);
		configurationManager.addConfiguration(scrnDrwrCnfgs, false);
		configurationManager.addConfiguration(scrnDrwrCnfgsSec, false);
		configurationManager.addConfiguration(ratFinderConfigs, false);
		configurationManager.addConfiguration(rearingConfigs, false);
		configurationManager.addConfiguration(vidRecorderConfigs, false);
		configurationManager.addConfiguration(subtractionConfigs, false);
		configurationManager.addConfiguration(avgFilterConfigs, false);
		configurationManager.addConfiguration(movementFilterConfigs, false);
		configurationManager.addConfiguration(zonesDrawerConfigs, false);
	}

	/**
	 * Connects the filters and adds them to the filters array.
	 * 
	 * @param refFia
	 */
	private boolean instantiateAndConnectFilters(final FrameIntArray refFia,
			final FiltersSetup filtersSetup) {

		filters.clear();
		filtersTriggeredByStreaming.clear();
		filtersTriggeredByProcessing.clear();
		activeFiltersSetup = filtersSetup;

		// extract filters' names and ID's, create filters' instances
		final FiltersNamesRequirements filtersNamesRequirements = filtersSetup
				.getFiltersNamesRequirements();
		for (final Iterator<FilterRequirement> it = filtersNamesRequirements
				.getFilters(); it.hasNext();) {
			final FilterRequirement entry = it.next();
			final String filterName = entry.getName();
			final String filterID = entry.getID();

			final VideoFilter<?, ?> filter = createFilter(filterName, filterID);
			filters.add(filter);

			switch (entry.getTrigger()) {
				case STREAMING:
					filtersTriggeredByStreaming.add(filterName);
					break;
				case PROCESSING:
					filtersTriggeredByProcessing.add(filterName);
					break;
				default:
					break;
			}
		}

		// pass filtersCollection to the filterSetup to connect them
		filtersSetup.setFiltersCollection(filters);
		boolean connectFilters = filtersSetup.connectFilters(new Point(commonConfigs.getWidth(),
				commonConfigs.getHeight()));
				
		// in case of connection errors
		if(!connectFilters)
			return false;
		
		// set filter's configurations to the default values
		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
				.hasNext();) {
			final VideoFilter<?, ?> vf = it.next();
			FilterConfigs filterConfig = configurationManager
					.getConfigByName(vf.getName());
			if (filterConfig == null) {
				// apply default configs to the filter and display a warning
				filterConfig = configurationManager.createDefaultConfigs(
						vf.getName(), vf.getID(), commonConfigs);
				configurationManager.addConfiguration(filterConfig, false);
				PManager.log.print("Default Configs is applied to filter: "
						+ vf.getName(), this, StatusSeverity.WARNING);
			}

			applyConfigsToFilter(filterConfig);
		}

		final SourceFilterConfigs sourceConfigs = new SourceFilterConfigs(
				"SourceFilter", commonConfigs, refFia);
		applyConfigsToFilter(sourceConfigs);

		// broadcast filters' data
		final ArrayList<FilterData> filtersData = new ArrayList<FilterData>();
		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
				.hasNext();) {
			final VideoFilter<?, ?> vf = it.next();
			if (vf.getFilterData() != null)
				filtersData.add((FilterData) vf.getFilterData());
		}

		for (final FilterData fd : filtersData) {
			for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
					.hasNext();) {
				final VideoFilter<?, ?> vf = it.next();
				vf.registerDependentData(fd);
			}
		}

		PManager.log.print("finished connecting video filters", this,
				Details.VERBOSE);
		PManager.log.print("loading filters' GUI..", this, Details.VERBOSE);

		System.out.println("Filters connections: =====");

		/*
		 * for (Iterator<VideoFilter<?, ?>>
		 * it=filters.getIterator();it.hasNext();){ VideoFilter<?, ?> vf =
		 * it.next(); ArrayList<VideoFilter<?, ?>> srcFilters =
		 * filtersSetup.getFiltersByLinkOut(vf.getLinkIn());
		 * if(srcFilters.size()>0)
		 * System.out.println(srcFilters.get(0).getName()
		 * +"\t\t--->\t\t"+vf.getName()); }
		 */

		System.out.println("Filters connections end: =====");

		PManager.mainGUI.loadPluggedGUI(getFiltersGUI());

		//configurationManager.printConfiguration();

		return true;//validateFiltersConfigurations();
	}

	public boolean instantiateFilters(final FrameIntArray refFia,
			final FiltersSetup setup) {
		return instantiateAndConnectFilters(refFia, setup);
	}

	public void pauseStream() {
		stopStreaming();
		/*
		 * enableFilter("ScreenDrawer", false); enableFilter("ScreenDrawerSec",
		 * false);
		 */
	}

	/**
	 * Removes filter from the array.
	 * 
	 * @param type
	 *            type of the filter to remove (first occurrence only of the
	 *            type will be removed)
	 */
	public void removeFilter(final Class<?> type) {
		filters.removeFilter(type);
	}

	/**
	 * Removes filter from the array.
	 * 
	 * @param filterName
	 *            name of the filter to remove
	 */
	public void removeFilter(final String filterName) {
		filters.removeFilter(filterName);
	}

	public void resumeStream() {
		startStreaming();
		/*
		 * enableFilter("ScreenDrawer", true); enableFilter("ScreenDrawerSec",
		 * true);
		 */
	}

	public void startProcessing() {

		for (final String filterName : filtersTriggeredByProcessing) {
			enableFilter(filterName, true);
		}
		/*
		 * enableFilter("SubtractionFilter", true); enableFilter("RatFinder",
		 * true); enableFilter("RearingDetector", true);
		 * enableFilter("AverageFilter", true);
		 */
	}

	public void startStreaming() {
		for (final String filterName : filtersTriggeredByStreaming) {
			enableFilter(filterName, true);
		}

		/*
		 * enableFilter("ScreenDrawer", true); enableFilter("ScreenDrawerSec",
		 * true);
		 */
	}

	public void stopProcessing() {

		for (final String filterName : filtersTriggeredByProcessing) {
			enableFilter(filterName, false);
		}
		/*
		 * enableFilter("SubtractionFilter", false);
		 * enableFilter("RearingDetector", false); enableFilter("RatFinder",
		 * false); enableFilter("Average Filter", false);
		 */
	}

	public void stopStreaming() {
		for (final String filterName : filtersTriggeredByStreaming) {
			enableFilter(filterName, false);
		}
	}

	/**
	 * Submits/Registers data objects of all filters to the ModuleManager, for
	 * the modules to receive data from Filters.
	 */
	public ArrayList<Data> getDataObjects() {
		ArrayList<Data> dataObjs=new ArrayList<Data>();
		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
		.hasNext();) {
			final VideoFilter<?, ?> vf = it.next();
			if (vf.getFilterData() != null)
				dataObjs.add(vf.getFilterData());
		}
		return dataObjs;
	}

	@Override
	public void updateConfigs(final CommonConfigs commonConfigs) {

		// loop on all filters
		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
				.hasNext();) {
			final VideoFilter<?, ?> videoFilter = it.next();

			final FilterConfigs configs = configurationManager
					.getConfigByName(videoFilter.getName());

			// update common configs
			configs.getCommonConfigs().merge(commonConfigs);

			// re-apply configs
			configurationManager.applyConfigs(configs);
		}
		// notify FilterSetup
		activeFiltersSetup.connectFilters(new Point(commonConfigs.getWidth(),
				commonConfigs.getHeight()));
	}

	/**
	 * @return
	 */
	public boolean validateFiltersConfigurations() {
		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
				.hasNext();) {
			final VideoFilter<?, ?> vf = it.next();
			if (!vf.getConfigs().validate()) {
				PManager.log.print(
						"Filter Configurations failed for: " + vf.getName(),
						this, StatusSeverity.ERROR);
				return false;
			}
		}
		return true;
	}
}
