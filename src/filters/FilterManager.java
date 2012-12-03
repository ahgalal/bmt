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
	private final ArrayList<VideoFilter<?, ?>>	arr_filters;
	private final ArrayList<FilterConfigs>		arrConfigs;

	private CommonFilterConfigs					common_configs;

	/**
	 * Initializes the filters' array.
	 * 
	 * @param ref_fia
	 * @param common_configs
	 */
	public FilterManager(final CommonFilterConfigs common_configs,
			final FrameIntArray ref_fia/* , final ExperimentType expType */) {
		PManager.log.print("instantiating..", this, Details.VERBOSE);
		arr_filters = new ArrayList<VideoFilter<?, ?>>();
		arrConfigs = new ArrayList<FilterConfigs>();
		this.common_configs = common_configs;
		// instantiateFilters(common_configs, ref_fia, expType);
	}

	/**
	 * Adds a filter to the array.
	 * 
	 * @param filter
	 *            filter to be added
	 */
	public void addFilter(final VideoFilter<?, ?> filter) {
		arr_filters.add(filter);
	}

	public FilterConfigs addFilterConfiguration(final FilterConfigs cfgs,
			final boolean updateExisting) {
		FilterConfigs existing = null;
		for (final FilterConfigs filterConfigs : arrConfigs)
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
			arrConfigs.add(cfgs);
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
		final VideoFilter<?, ?> tmp_filter = getFilterByName(cfgs
				.getConfigurablename());
		if (tmp_filter != null) {
			try {
				// try to add the config to the list, or get the existing one
				// after it is updated by the incoming config
				cfgs = addFilterConfiguration(cfgs, true);
			} catch (final RuntimeException e) {
			} finally {
				tmp_filter.updateConfigs(cfgs);
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

		arr_filters.clear();

		// TODO: ??
	}

	/**
	 * Disables all filter.
	 */
	public void disableAll() {
		for (final VideoFilter<?, ?> vf : arr_filters)
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
	 * @param filter_name
	 *            name of the filter to enable/disable
	 * @param enable
	 *            enable/disable
	 */
	public void enableFilter(final String filter_name, final boolean enable) {
		final VideoFilter<?, ?> tmp = getFilterByName(filter_name);
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
		for (final VideoFilter<?, ?> vf : arr_filters)
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
		for (final VideoFilter<?, ?> vf : arr_filters)
			if (vf.getClass() == type)
				return vf;
		return null;
	}

	private FilterConfigs getFilterConfigByName(final String configName) {
		for (final FilterConfigs configs : arrConfigs)
			if (configs.getConfigurablename().equals(configName))
				return configs;
		return null;
	}

	/**
	 * Gets the array of filters.
	 * 
	 * @return array of filters
	 */
	public ArrayList<VideoFilter<?, ?>> getFilters() {
		return arr_filters;
	}

	@SuppressWarnings("rawtypes")
	public PluggedGUI[] getFiltersGUI() {
		int validGUIsNumber = 0;
		for (final VideoFilter<?, ?> vf : arr_filters)
			if (vf.getGUI() != null)
				validGUIsNumber++;
		final PluggedGUI[] arr = new PluggedGUI[validGUIsNumber];
		int i = 0;
		for (final VideoFilter<?, ?> vf : arr_filters)
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
		final ArrayList<String> str_names = new ArrayList<String>();

		for (final VideoFilter<?, ?> vf : arr_filters)
			str_names.add(vf.getName());

		return str_names;
	}

	public void initializeConfigs(final CommonFilterConfigs commonConfigs) {
		common_configs = commonConfigs;
		final SourceFilterConfigs source_configs = new SourceFilterConfigs(
				"Source Filter", common_configs, null);

		final ScreenDrawerConfigs scrn_drwr_cnfgs = new ScreenDrawerConfigs(
				"ScreenDrawer", null, null, common_configs, true,
				ShapeController.getDefault());

		final RatFinderFilterConfigs rat_finder_configs = new RatFinderFilterConfigs(
				"RatFinder", common_configs);

		final RearingFilterConfigs rearingConfigs = new RearingFilterConfigs(
				"RearingDetector", 1000, 200, 200, null, common_configs);

		final RecorderConfigs vid_recorder_configs = new RecorderConfigs(
				"Recorder", common_configs);

		final SubtractionConfigs subtraction_configs = new SubtractionConfigs(
				"SubtractionFilter", SubtractionConfigs.defaultThreshold,
				common_configs);

		final SourceFilterConfigs avgFilterConfigs = new SourceFilterConfigs(
				"Average Filter", common_configs, null);

		final SourceFilterConfigs movementFilterConfigs = new SourceFilterConfigs(
				"Movement Meter", common_configs, null);

		addFilterConfiguration(source_configs, false);
		addFilterConfiguration(scrn_drwr_cnfgs, false);
		addFilterConfiguration(rat_finder_configs, false);
		addFilterConfiguration(rearingConfigs, false);
		addFilterConfiguration(vid_recorder_configs, false);
		addFilterConfiguration(subtraction_configs, false);
		addFilterConfiguration(avgFilterConfigs, false);
		addFilterConfiguration(movementFilterConfigs, false);
	}

	/**
	 * Connects the filters and adds them to the filters array.
	 * 
	 * @param ref_fia
	 */
	private boolean instantiateAndConnectFilters(final FrameIntArray ref_fia,
			final ExperimentType expType) {
		String[] filtersNames = null;

		AverageFilter avgFilter;
		MovementMeter movementMeter;
		RatFinder rat_finder;
		RearingDetector rearing_det;
		ScreenDrawer screen_drawer = null;
		SourceFilter source_filter;
		SubtractorFilter subtractor_filter;
		VideoRecorder vid_rec;

		final FiltersSetup openFieldFiltersSetup = new FiltersSetup(
				new String[] { "RatFinder", "RearingDetector", "Recorder",
						"SubtractionFilter", "Average Filter" });

		final FiltersSetup forcedSwimmingFiltersSetup = new FiltersSetup(
				new String[] { "Recorder", "Movement Meter" });

		PManager.log.print("Connecting video filters", this, Details.VERBOSE);
		final Point dims = new Point(common_configs.width,
				common_configs.height);

		final Link src_rgb_link = new Link(dims);
		final Link grey_link = new Link(dims);
		final Link marker_link = new Link(dims);
		final Link avg_link = new Link(dims);
		final Link differentialLink = new Link(dims);

		source_filter = new SourceFilter("Source Filter", null, src_rgb_link);
		final SourceFilterConfigs source_configs = new SourceFilterConfigs(
				"Source Filter", common_configs, ref_fia);
		addFilter(source_filter);
		applyConfigsToFilter(source_configs);
		// source_filter.configure(source_configs);

		switch (expType) {
			case OPEN_FIELD:
				screen_drawer = new ScreenDrawer(
				// "ScreenDrawer", /*src_rgb_link*/avg_link, /*marker_link*/
				// grey_link, null);
						"ScreenDrawer", /* grey_link */
						src_rgb_link/* avg_link */, /* marker_link */
						/* grey_link, */marker_link, null);
				filtersNames = openFieldFiltersSetup.getFiltersNames();
				break;
			case FORCED_SWIMMING:
				screen_drawer = new ScreenDrawer(
				// "ScreenDrawer", /*src_rgb_link*/avg_link, /*marker_link*/
				// grey_link, null);
						"ScreenDrawer", /* grey_link */
						differentialLink/* src_rgb_link *//* avg_link */, /* marker_link */
						src_rgb_link /* grey_link */, null);
				filtersNames = forcedSwimmingFiltersSetup.getFiltersNames();
				break;
		}

		addFilter(screen_drawer);
		applyConfigsToFilter(getFilterConfigByName("ScreenDrawer"));

		if (isWithinArray("RatFinder", filtersNames))
			if (isWithinArray("SubtractionFilter", filtersNames)) {
				rat_finder = new RatFinder("RatFinder", /* grey_link */
				avg_link, marker_link);
				/*
				 * rat_finder = new RatFinder2( "RatFinder", grey_link,
				 * marker_link);
				 */
				addFilter(rat_finder);
				applyConfigsToFilter(getFilterConfigByName("RatFinder"));
			} else
				PManager.log
						.print("Can't find the SubtractionFilter Filter, it is needed to run the RatFinder filter; please check filters list!",
								this, StatusSeverity.ERROR);
		if (isWithinArray("RearingDetector", filtersNames)) {
			rearing_det = new RearingDetector("RearingDetector", grey_link,
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
				addFilter(rearing_det);
				applyConfigsToFilter(rearingConfigs);
			}
		}
		if (isWithinArray("Recorder", filtersNames)) {
			vid_rec = new VideoRecorder("Recorder", src_rgb_link, null);
			addFilter(vid_rec);
			applyConfigsToFilter(getFilterConfigByName("Recorder"));
		}
		if (isWithinArray("SubtractionFilter", filtersNames)) {
			subtractor_filter = new SubtractorFilter("SubtractionFilter",
					src_rgb_link, grey_link);
			addFilter(subtractor_filter);
			applyConfigsToFilter(getFilterConfigByName("SubtractionFilter"));
		}

		if (isWithinArray("Average Filter", filtersNames)) {
			avgFilter = new AverageFilter("Average Filter", grey_link, avg_link);
			// TODO: create a config class for avg filter
			// avgFilter.configure(source_configs);
			addFilter(avgFilter);
			final SourceFilterConfigs avgFilterConfigs = new SourceFilterConfigs(
					"Average Filter", null, ref_fia);
			applyConfigsToFilter(avgFilterConfigs);
		}
		if (isWithinArray("Movement Meter", filtersNames)) {
			movementMeter = new MovementMeter("Movement Meter", src_rgb_link,
					differentialLink);
			// TODO: create a config class for MovementMeter
			// movementMeter.configure(source_configs);
			final SourceFilterConfigs movementFilterConfigs = new SourceFilterConfigs(
					"Movement Meter", null, ref_fia);
			addFilter(movementMeter);
			applyConfigsToFilter(movementFilterConfigs);
		}
		PManager.log.print("finished connecting video filters", this,
				Details.VERBOSE);
		PManager.log.print("loading filters' GUI..", this, Details.VERBOSE);
		PManager.main_gui.loadPluggedGUI(getFiltersGUI());

		System.out.println("Configurations available: =====");
		for (final FilterConfigs configs : arrConfigs)
			System.out.println(configs.toString());
		System.out.println("===============================");

		return validateFiltersConfigurations();
	}

	public void instantiateFilters(final FrameIntArray ref_fia,
			final ExperimentType expType) {
		instantiateAndConnectFilters(ref_fia, expType);
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
			arr_filters.remove(tmp);
	}

	/**
	 * Removes filter from the array.
	 * 
	 * @param filter_name
	 *            name of the filter to remove
	 */
	public void removeFilter(final String filter_name) {
		final VideoFilter<?, ?> tmp = getFilterByName(filter_name);
		if (tmp != null)
			arr_filters.remove(tmp);
	}

	/**
	 * Submits/Registers data objects of all filters to the ModuleManager, for
	 * the modules to receive data from Filters.
	 */
	public void submitDataObjects() {
		for (final VideoFilter<?, ?> v : arr_filters)
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
