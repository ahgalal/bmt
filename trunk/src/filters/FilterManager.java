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

import modules.ModulesManager;
import modules.experiment.ExperimentType;
import modules.zones.ShapeController;
import ui.PluggedGUI;
import utils.Logger.Details;
import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.FrameIntArray;
import filters.avg.AverageFilter;
import filters.movementmeter.MovementMeter;
import filters.ratfinder.RatFinder;
import filters.ratfinder.RatFinderData;
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

/**
 * Manager of all filters, to enable, disable filters.
 * 
 * @author Creative
 */
public class FilterManager {
	private final ArrayList<VideoFilter<?, ?>>	filters;
	private final ArrayList<FilterConfigs>		configs;

	private CommonFilterConfigs					commonConfigs;

	/**
	 * Initializes the filters' array.
	 * 
	 * @param refFia
	 * @param commonConfigs
	 */
	public FilterManager(final CommonFilterConfigs commonConfigs,
			final FrameIntArray refFia/* , final ExperimentType expType */) {
		PManager.log.print("instantiating..", this, Details.VERBOSE);
		filters = new ArrayList<VideoFilter<?, ?>>();
		configs = new ArrayList<FilterConfigs>();
		this.commonConfigs = commonConfigs;
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

	public FilterConfigs addFilterConfiguration(final FilterConfigs cfgs,
			final boolean updateExisting) {
		FilterConfigs existing = null;
		for (final FilterConfigs filterConfigs : configs)
			if (filterConfigs.getConfigurablename().equals(
					cfgs.getConfigurablename())) {
				existing = filterConfigs;
				break;
			}
		if (existing != null) {
			if (updateExisting) {
				existing.mergeConfigs(cfgs);
			} /*else
				
				throw new RuntimeException("Error adding an already existing filter configuration, try updating the existing configuration instead.");*/
			return existing;
		} else {
			configs.add(cfgs);
			return cfgs;
		}

	}

	/**
	 * Applies a configuration object to a filter, using the name of the filter
	 * specified in the configuration object.</br>Also adds the filter
	 * configuration to the list if it doesn't exist.
	 * 
	 * @param cfgs
	 *            configurations object
	 */
	public void applyConfigsToFilter(FilterConfigs cfgs) {
		final VideoFilter<?, ?> tmpFilter = getFilterByName(cfgs
				.getConfigurablename());
		if (tmpFilter != null) {
			try {
				// try to add the config to the list, or get the existing one
				// after it is updated by the incoming config
				cfgs = addFilterConfiguration(cfgs, true);
			} catch (final RuntimeException e) {
			} finally {
				tmpFilter.updateConfigs(cfgs);
			}
		}
	}

	/**
	 * Unloads all filters.
	 */
	public void deInitialize() {
		// unregister filters' PluggedGUI from listening to state changes
		// and unload GUI
		for (final PluggedGUI pgui : getFiltersGUI()) {
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
		for (final VideoFilter<?, ?> vf : filters)
			vf.enable(false);
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
		for (final VideoFilter<?, ?> vf : filters)
			if (vf.getName().equals(name))
				return vf;
		return null;
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
		for (final VideoFilter<?, ?> vf : filters)
			if (vf.getClass() == type)
				return vf;
		return null;
	}

	private FilterConfigs getFilterConfigByName(final String configName) {
		for (final FilterConfigs filterConfigs: configs)
			if (filterConfigs.getConfigurablename().equals(configName))
				return filterConfigs;
		return null;
	}

	/**
	 * Gets the array of filters.
	 * 
	 * @return array of filters
	 */
	public ArrayList<VideoFilter<?, ?>> getFilters() {
		return filters;
	}

	@SuppressWarnings("rawtypes")
	public PluggedGUI[] getFiltersGUI() {
		int validGUIsNumber = 0;
		for (final VideoFilter<?, ?> vf : filters)
			if (vf.getGUI() != null)
				validGUIsNumber++;
		final PluggedGUI[] arr = new PluggedGUI[validGUIsNumber];
		int i = 0;
		for (final VideoFilter<?, ?> vf : filters)
			if (vf.getGUI() != null) {
				arr[i] = vf.getGUI();
				i++;
			}
		return arr;
	}

	/**
	 * Gets the registered filters names.
	 * 
	 * @return String array containing the names of registered filters
	 */
	public ArrayList<String> getRegisteredFiltersNames() {
		final ArrayList<String> strNames = new ArrayList<String>();

		for (final VideoFilter<?, ?> vf : filters)
			strNames.add(vf.getName());

		return strNames;
	}

	public void initializeConfigs(final CommonFilterConfigs commonConfigs) {
		this.commonConfigs = commonConfigs;
		final SourceFilterConfigs sourceConfigs = new SourceFilterConfigs(
				"Source Filter", commonConfigs, null);

		final ScreenDrawerConfigs scrnDrwrCnfgs = new ScreenDrawerConfigs(
				"ScreenDrawer", null, null, commonConfigs, true,
				ShapeController.getDefault());

		final RatFinderFilterConfigs ratFinderConfigs = new RatFinderFilterConfigs(
				"RatFinder", commonConfigs);

		final RearingFilterConfigs rearingConfigs = new RearingFilterConfigs(
				"RearingDetector", 1000, 200, 200, null, commonConfigs);

		final RecorderConfigs vidRecorderConfigs = new RecorderConfigs(
				"Recorder", commonConfigs);

		final SubtractionConfigs subtractionConfigs = new SubtractionConfigs(
				"SubtractionFilter", SubtractionConfigs.defaultThreshold,
				commonConfigs);

		final SourceFilterConfigs avgFilterConfigs = new SourceFilterConfigs(
				"Average Filter", commonConfigs, null);

		final SourceFilterConfigs movementFilterConfigs = new SourceFilterConfigs(
				"Movement Meter", commonConfigs, null);

		addFilterConfiguration(sourceConfigs, false);
		addFilterConfiguration(scrnDrwrCnfgs, false);
		addFilterConfiguration(ratFinderConfigs, false);
		addFilterConfiguration(rearingConfigs, false);
		addFilterConfiguration(vidRecorderConfigs, false);
		addFilterConfiguration(subtractionConfigs, false);
		addFilterConfiguration(avgFilterConfigs, false);
		addFilterConfiguration(movementFilterConfigs, false);
	}

	/**
	 * Connects the filters and adds them to the filters array.
	 * 
	 * @param refFia
	 */
	private boolean instantiateAndConnectFilters(final FrameIntArray refFia,
			final ExperimentType expType) {
		String[] filtersNames = null;

		AverageFilter avgFilter;
		MovementMeter movementMeter;
		RatFinder ratFinder;
		RearingDetector rearingDet;
		ScreenDrawer screenDrawer = null;
		SourceFilter sourceFilter;
		SubtractorFilter subtractorFilter;
		VideoRecorder vidRec;

		final FiltersSetup openFieldFiltersSetup = new FiltersSetup(
				new String[] { "RatFinder", "RearingDetector", "Recorder",
						"SubtractionFilter", "Average Filter" });

		final FiltersSetup forcedSwimmingFiltersSetup = new FiltersSetup(
				new String[] { "Recorder", "Movement Meter" });

		PManager.log.print("Connecting video filters", this, Details.VERBOSE);
		final Point dims = new Point(commonConfigs.getWidth(),
				commonConfigs.getHeight());

		final Link srcRGBLink = new Link(dims);
		final Link greyLink = new Link(dims);
		final Link markerLink = new Link(dims);
		final Link avgLink = new Link(dims);
		final Link differentialLink = new Link(dims);

		sourceFilter = new SourceFilter("Source Filter", null, srcRGBLink);
		final SourceFilterConfigs sourceConfigs = new SourceFilterConfigs(
				"Source Filter", commonConfigs, refFia);
		addFilter(sourceFilter);
		applyConfigsToFilter(sourceConfigs);

		switch (expType) {
			case OPEN_FIELD:
				screenDrawer = new ScreenDrawer(
				// "ScreenDrawer", /*src_rgb_link*/avg_link, /*marker_link*/
				// grey_link, null);
						"ScreenDrawer", /* grey_link */
						srcRGBLink/* avg_link */, /* marker_link */
						/* grey_link, */markerLink, null);
				filtersNames = openFieldFiltersSetup.getFiltersNames();
				break;
			case FORCED_SWIMMING:
				screenDrawer = new ScreenDrawer(
				// "ScreenDrawer", /*src_rgb_link*/avg_link, /*marker_link*/
				// grey_link, null);
						"ScreenDrawer", /* grey_link */
						differentialLink/* src_rgb_link *//* avg_link */, /* marker_link */
						srcRGBLink /* grey_link */, null);
				filtersNames = forcedSwimmingFiltersSetup.getFiltersNames();
				break;
		}

		addFilter(screenDrawer);
		applyConfigsToFilter(getFilterConfigByName("ScreenDrawer"));

		if (isWithinArray("RatFinder", filtersNames))
			if (isWithinArray("SubtractionFilter", filtersNames)) {
				ratFinder = new RatFinder("RatFinder", /* grey_link */
				avgLink, markerLink);
				/*
				 * rat_finder = new RatFinder2( "RatFinder", grey_link,
				 * marker_link);
				 */
				addFilter(ratFinder);
				applyConfigsToFilter(getFilterConfigByName("RatFinder"));
			} else
				PManager.log
						.print("Can't find the SubtractionFilter Filter, it is needed to run the RatFinder filter; please check filters list!",
								this, StatusSeverity.ERROR);
		if (isWithinArray("RearingDetector", filtersNames)) {
			rearingDet = new RearingDetector("RearingDetector", greyLink,
					null);
			final RatFinderData rfd = (RatFinderData) getFilterByName(
					"RatFinder").getFilterData();
			if (rfd == null)
				PManager.log
						.print("Can't find the RatFinder Filter, it is needed to run the RearingDetector filter; please check filters list!",
								this, StatusSeverity.ERROR);
			else {
				final RearingFilterConfigs rearingConfigs = new RearingFilterConfigs(
						"RearingDetector", -1, -1, -1, (rfd.getCenterPoint()),
						null);
				addFilter(rearingDet);
				applyConfigsToFilter(rearingConfigs);
			}
		}
		if (isWithinArray("Recorder", filtersNames)) {
			vidRec = new VideoRecorder("Recorder", srcRGBLink, null);
			addFilter(vidRec);
			applyConfigsToFilter(getFilterConfigByName("Recorder"));
		}
		if (isWithinArray("SubtractionFilter", filtersNames)) {
			subtractorFilter = new SubtractorFilter("SubtractionFilter",
					srcRGBLink, greyLink);
			addFilter(subtractorFilter);
			applyConfigsToFilter(getFilterConfigByName("SubtractionFilter"));
		}

		if (isWithinArray("Average Filter", filtersNames)) {
			avgFilter = new AverageFilter("Average Filter", greyLink, avgLink);
			// TODO: create a config class for avg filter
			// avgFilter.configure(source_configs);
			addFilter(avgFilter);
			final SourceFilterConfigs avgFilterConfigs = new SourceFilterConfigs(
					"Average Filter", null, refFia);
			applyConfigsToFilter(avgFilterConfigs);
		}
		if (isWithinArray("Movement Meter", filtersNames)) {
			movementMeter = new MovementMeter("Movement Meter", srcRGBLink,
					differentialLink);
			// TODO: create a config class for MovementMeter
			// movementMeter.configure(source_configs);
			final SourceFilterConfigs movementFilterConfigs = new SourceFilterConfigs(
					"Movement Meter", null, refFia);
			addFilter(movementMeter);
			applyConfigsToFilter(movementFilterConfigs);
		}
		PManager.log.print("finished connecting video filters", this,
				Details.VERBOSE);
		PManager.log.print("loading filters' GUI..", this, Details.VERBOSE);
		PManager.mainGUI.loadPluggedGUI(getFiltersGUI());

		System.out.println("Configurations available: =====");
		for (final FilterConfigs filterConfigs: configs)
			System.out.println(filterConfigs.toString());
		System.out.println("===============================");

		return validateFiltersConfigurations();
	}

	public void instantiateFilters(final FrameIntArray refFia,
			final ExperimentType expType) {
		instantiateAndConnectFilters(refFia, expType);
	}

	private boolean isWithinArray(final String name, final String[] array) {
		for (final String str : array)
			if (str.equals(name))
				return true;
		return false;
	}

	/**
	 * Removes filter from the array.
	 * 
	 * @param type
	 *            type of the filter to remove (first occurrence only of the
	 *            type will be removed)
	 */
	public void removeFilter(final Class<?> type) {
		final VideoFilter<?, ?> tmp = getFilterByType(type);
		if (tmp != null)
			filters.remove(tmp);
	}

	/**
	 * Removes filter from the array.
	 * 
	 * @param filterName
	 *            name of the filter to remove
	 */
	public void removeFilter(final String filterName) {
		final VideoFilter<?, ?> tmp = getFilterByName(filterName);
		if (tmp != null)
			filters.remove(tmp);
	}

	/**
	 * Submits/Registers data objects of all filters to the ModuleManager, for
	 * the modules to receive data from Filters.
	 */
	public void submitDataObjects() {
		for (final VideoFilter<?, ?> v : filters)
			if (v.getFilterData() != null)
				ModulesManager.getDefault().addFilterDataObject(
						v.getFilterData());
	}

	/**
	 * @return
	 */
	private boolean validateFiltersConfigurations() {
		for (final VideoFilter<?, ?> vf : getFilters())
			if (!vf.getConfigs().validate()) {
				PManager.log.print(
						"Filter Configurations failed for: " + vf.getName(),
						this, StatusSeverity.ERROR);
				return false;
			}
		return true;
	}

}
