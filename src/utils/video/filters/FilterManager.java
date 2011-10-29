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

package utils.video.filters;

import java.awt.Point;
import java.util.ArrayList;

import modules.ModulesManager;
import modules.zones.ShapeController;
import ui.PluggedGUI;
import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.FrameIntArray;
import utils.video.filters.RatFinder.RatFinder;
import utils.video.filters.RatFinder.RatFinderData;
import utils.video.filters.RatFinder.RatFinderFilterConfigs;
import utils.video.filters.avg.AverageFilter;
import utils.video.filters.rearingdetection.RearingDetector;
import utils.video.filters.rearingdetection.RearingFilterConfigs;
import utils.video.filters.recorder.RecorderConfigs;
import utils.video.filters.recorder.VideoRecorder;
import utils.video.filters.screendrawer.ScreenDrawer;
import utils.video.filters.screendrawer.ScreenDrawerConfigs;
import utils.video.filters.source.SourceFilter;
import utils.video.filters.source.SourceFilterConfigs;
import utils.video.filters.subtractionfilter.SubtractionConfigs;
import utils.video.filters.subtractionfilter.SubtractorFilter;

/**
 * Manager of all filters, to enable, disable filters.
 * 
 * @author Creative
 */
public class FilterManager
{
	private final ArrayList<VideoFilter> arr_filters;
	private SubtractorFilter subtractor_filter;
	private RatFinder rat_finder;
	private RearingDetector rearing_det;
	private VideoRecorder vid_rec;
	private ScreenDrawer screen_drawer;
	private SourceFilter source_filter;
	private AverageFilter avgFilter;

	/**
	 * Initializes the filters' array.
	 */
	public FilterManager()
	{
		arr_filters = new ArrayList<VideoFilter>();
		connectFilters();
	}

	/**
	 * Gets a filter using the filter's name.
	 * 
	 * @param name
	 *            name of the filter to retrieve
	 * @return VideoFilter having the name specified
	 */
	public VideoFilter getFilterByName(final String name)
	{
		for (final VideoFilter vf : arr_filters)
			if (vf.getName().equals(name))
				return vf;
		return null;
	}

	/**
	 * Enables/Disables a filter using its name.
	 * 
	 * @param filter_name
	 *            name of the filter to enable/disable
	 * @param enable
	 *            enable/disable
	 */
	public void enableFilter(final String filter_name, final boolean enable)
	{
		final VideoFilter tmp = getFilterByName(filter_name);
		if (tmp != null)
			tmp.enable(enable);
	}

	/**
	 * Gets a filter using the filter's type.
	 * 
	 * @param type
	 *            type of filter to retrieve (child of VideoFilter class)
	 * @return VideoFilter having type specified (in case of many filters have
	 *         the same type, first one found will be returned)
	 */
	private VideoFilter getFilterByType(final Class<?> type)
	{
		for (final VideoFilter vf : arr_filters)
			if (vf.getClass() == type)
				return vf;
		return null;
	}

	/**
	 * Enables/Disables a filter using its type.
	 * 
	 * @param type
	 *            type of the filter to enable/disable
	 * @param enable
	 *            enable/disable
	 */
	public void enableFilter(final Class<?> type, final boolean enable)
	{
		final VideoFilter tmp = getFilterByType(type);
		if (tmp != null)
			tmp.enable(enable);
	}

	/**
	 * Gets the array of filters.
	 * 
	 * @return array of filters
	 */
	public ArrayList<VideoFilter> getFilters()
	{
		return arr_filters;
	}

	/**
	 * Adds a filter to the array.
	 * 
	 * @param filter
	 *            filter to be added
	 */
	public void addFilter(final VideoFilter filter)
	{
		arr_filters.add(filter);
	}

	/**
	 * Removes filter from the array.
	 * 
	 * @param filter_name
	 *            name of the filter to remove
	 */
	public void removeFilter(final String filter_name)
	{
		final VideoFilter tmp = getFilterByName(filter_name);
		if (tmp != null)
			arr_filters.remove(tmp);
	}

	/**
	 * Removes filter from the array.
	 * 
	 * @param type
	 *            type of the filter to remove (first occurrence only of the
	 *            type will be removed)
	 */
	public void removeFilter(final Class<?> type)
	{
		final VideoFilter tmp = getFilterByType(type);
		if (tmp != null)
			arr_filters.remove(tmp);
	}

	/**
	 * Applies a configuration object to a filter, using the name of the filter
	 * specified in the configuration object.
	 * 
	 * @param cfgs
	 *            configurations object
	 */
	public void applyConfigsToFilter(final FilterConfigs cfgs)
	{
		final VideoFilter tmp_filter = getFilterByName(cfgs.getConfigurablename());
		if (tmp_filter != null)
			tmp_filter.updateConfigs(cfgs);
	}

	/**
	 * Disables all filter.
	 */
	public void disableAll()
	{
		for (final VideoFilter vf : arr_filters)
			vf.enable(false);
	}

	/**
	 * Submits/Registers data objects of all filters to the ModuleManager, for
	 * the modules to receive data from Filters.
	 */
	public void submitDataObjects()
	{
		for (final VideoFilter v : arr_filters)
			if (v.getFilterData() != null)
				ModulesManager.getDefault().addFilterDataObject(v.getFilterData());
	}

	/**
	 * Initializes video filters, and applies their configurations.
	 * 
	 * @param common_configs
	 *            common configurations
	 * @param fia_src
	 *            image data container from the video input library
	 * @return true: success
	 */
	public boolean configureFilters(
			final CommonFilterConfigs common_configs,
			final FrameIntArray fia_src)
	{
		// ////////////////////////////////////
		// Rat Finder
		final RatFinderFilterConfigs rat_finder_configs = new RatFinderFilterConfigs(
				"RatFinder",
				common_configs);
		final RatFinderData rfd = (RatFinderData) rat_finder.getFilterData();
		rat_finder.configure(rat_finder_configs);

		// ////////////////////////////////////
		// Rearing Detector
		final RearingFilterConfigs rearingConfigs = new RearingFilterConfigs(
				"RearingDetector",
				1000,
				200,
				200,
				(rfd.getCenterPoint()),
				common_configs);
		rearing_det.configure(rearingConfigs);

		// ////////////////////////////////////
		// Video Recorder
		final RecorderConfigs vid_recorder_configs = new RecorderConfigs(
				"Recorder",
				common_configs);
		vid_rec.configure(vid_recorder_configs);

		// ////////////////////////////////////
		// Screen Drawer
		final ScreenDrawerConfigs scrn_drwr_cnfgs = new ScreenDrawerConfigs(
				"ScreenDrawer",
				null,
				null,
				common_configs,
				true,
				ShapeController.getDefault());
		screen_drawer.configure(scrn_drwr_cnfgs);

		// ////////////////////////////////////
		// Subtraction Filter
		final SubtractionConfigs subtraction_configs = new SubtractionConfigs(
				"SubtractionFilter",
				40,
				common_configs);
		subtractor_filter.configure(subtraction_configs);

		// ////////////////////////////////////
		// Source Filter
		final SourceFilterConfigs source_configs = new SourceFilterConfigs(
				"Source Filter",
				common_configs,
				fia_src);
		source_filter.configure(source_configs);
		
		///////////////////////////////////////
		// Average Filter
		// TODO: create a config class for avg filter
		avgFilter.configure(source_configs);

		// ///////////////////////////////////
		// check that configurations of all filters are valid
		for (final VideoFilter vf : getFilters())
			if (!vf.getConfigs().validate())
			{
				PManager.log.print(
						"Filter Configurations failed for: " + vf.getName(),
						this,
						StatusSeverity.ERROR);
				return false;
			}
		return true;
	}

	/**
	 * Connects the filters and adds them to the filters array.
	 */
	private void connectFilters()
	{
		final Point dims = new Point(0, 0);

		final Link src_rgb_link = new Link(dims);
		final Link grey_link = new Link(dims);
		final Link marker_link = new Link(dims);
		final Link avg_link = new Link(dims);

		rat_finder = new RatFinder(
				"RatFinder", grey_link, marker_link);
		/*		rat_finder = new RatFinder2(
						"RatFinder", grey_link, marker_link);*/

		rearing_det = new RearingDetector(
				"RearingDetector", grey_link, null);

		vid_rec = new VideoRecorder("Recorder", src_rgb_link, null);

		screen_drawer = new ScreenDrawer(
				//"ScreenDrawer", /*src_rgb_link*/avg_link, /*marker_link*/ grey_link, null);
				"ScreenDrawer", src_rgb_link/*avg_link*/, marker_link /*grey_link*/, null);

		subtractor_filter = new SubtractorFilter(
				"SubtractionFilter", src_rgb_link, grey_link);

		source_filter = new SourceFilter("Source Filter", null, src_rgb_link);

		avgFilter = new AverageFilter("Average Filter",grey_link, avg_link);
		
		// ////////////////////////////////////
		// add filters to the filter manager
		addFilter(source_filter);
		addFilter(vid_rec);
		addFilter(subtractor_filter);
		addFilter(rearing_det);
		addFilter(rat_finder);
		addFilter(screen_drawer);
		addFilter(avgFilter);

		PManager.main_gui.loadPluggedGUI(getFiltersGUI());
		for (final PluggedGUI fgui : getFiltersGUI())
			PManager.getDefault().addStateListener(fgui);
	}

	/**
	 * Gets the registered filters names.
	 * 
	 * @return String array containing the names of registered filters
	 */
	public ArrayList<String> getRegisteredFiltersNames()
	{
		final ArrayList<String> str_names = new ArrayList<String>();

		for (final VideoFilter vf : arr_filters)
			str_names.add(vf.getName());

		return str_names;
	}

	public PluggedGUI[] getFiltersGUI()
	{
		int validGUIsNumber = 0;
		for (final VideoFilter vf : arr_filters)
		{
			if (vf.getGUI() != null)
			{
				validGUIsNumber++;
			}
		}
		final PluggedGUI[] arr = new PluggedGUI[validGUIsNumber];
		int i = 0;
		for (final VideoFilter vf : arr_filters)
		{
			if (vf.getGUI() != null)
			{
				arr[i] = vf.getGUI();
				i++;
			}
		}
		return arr;
	}

}
