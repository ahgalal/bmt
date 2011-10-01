package utils.video;

import java.awt.Point;

import modules.zones.ShapeController;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;
import utils.video.filters.FilterManager;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;
import utils.video.filters.RatFinder.RatFinder;
import utils.video.filters.RatFinder.RatFinderData;
import utils.video.filters.RatFinder.RatFinderFilterConfigs;
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
import utils.video.input.AGCamLibModule;
import utils.video.input.JMFModule;
import utils.video.input.JMyronModule;
import utils.video.input.OpenCVModule;
import utils.video.input.VidInputter;

/**
 * Main Video Manager, manages all video operations.
 * 
 * @author Creative
 */
public class VideoProcessor
{
	private int[] bg_image_rgb;
	private boolean bg_is_set;

	private final FrameIntArray ref_fia;

	private VidInputter v_in;

	private boolean video_processor_enabled;
	private final FilterManager filter_mgr;
	private final CommonFilterConfigs common_configs;

	/**
	 * Gets the filter manager.
	 * 
	 * @return instance of the filter manager
	 */
	public FilterManager getFilterManager()
	{
		return filter_mgr;
	}

	/**
	 * Initialization.
	 */
	public VideoProcessor()
	{
		common_configs = new CommonFilterConfigs(0, 0, 0, 0, null, null);
		video_processor_enabled = true;
		ref_fia = new FrameIntArray();
		filter_mgr = new FilterManager();
	}

	/**
	 * Runnable for grabing image from the input library and pass it to all
	 * filters.
	 * 
	 * @author Creative
	 */
	private class RunnableProcessor implements Runnable
	{
		@Override
		public void run()
		{
			PManager.log.print("Started Video Streaming", this);

			try
			{
				Thread.sleep(100);
			} catch (final InterruptedException e1)
			{
			}

			while (video_processor_enabled)
			{
				while (v_in.getStatus() != 1)
				{
					try
					{
						Thread.sleep(1000);
						PManager.log.print(
								"Device is not Ready!",
								this,
								StatusSeverity.ERROR);
					} catch (final InterruptedException e)
					{
						e.printStackTrace();
					}
					PManager.log.print("Device is not ready yet!", this);
				}
				if (v_in.getStatus() == 1)
					for (final VideoFilter v : filter_mgr.getFilters())
						v.process();

				try
				{
					Thread.sleep(1000 / common_configs.frame_rate);
				} catch (final InterruptedException e)
				{
				}
			}
			// ////////////////////////////////
			// finish up opened filters/utils:
			// ////////////////////////////////

			filter_mgr.disableAll();

			PManager.log.print("Ended Video Streaming", this);
		}
	}

	/**
	 * Display additional settings of the input video library.
	 */
	public void displayMoreSettings()
	{
		v_in.displayMoreSettings();
	}

	/**
	 * Updates the RGB background of the subtraction filter with the current
	 * image, and returns the current image.
	 * 
	 * @return
	 */
	public int[] updateRGBBackground()
	{
		bg_image_rgb = ref_fia.frame_data;
		bg_is_set = true;
		return bg_image_rgb;
	}

	/**
	 * Updates common configurations used by almost all filters.
	 * 
	 * @param common_configs
	 *            common configurations to apply
	 */
	public void updateCommonConfigs(final CommonFilterConfigs common_configs)
	{
		if (common_configs.cam_index != -1)
			this.common_configs.cam_index = common_configs.cam_index;
		if (common_configs.format != null)
			this.common_configs.format = common_configs.format;
		if (common_configs.vid_library != null)
			this.common_configs.vid_library = common_configs.vid_library;
		if (common_configs.frame_rate != -1)
			this.common_configs.frame_rate = common_configs.frame_rate;
		if (common_configs.height != -1)
			this.common_configs.height = common_configs.height;
		if (common_configs.width != -1)
			this.common_configs.width = common_configs.width;
		common_configs.validate();
	}

	/**
	 * Initialization of the video library and vifeo filters.
	 * 
	 * @param ip_common_configs
	 *            common configurations object, used by almost all filters
	 * @return true: success
	 */
	public boolean initialize(final CommonFilterConfigs ip_common_configs)
	{
		updateCommonConfigs(ip_common_configs);

		if (common_configs.vid_library.equals("JMF"))
			v_in = new JMFModule();
		else if (common_configs.vid_library.equals("JMyron"))
			v_in = new JMyronModule();
		else if (common_configs.vid_library.equals("OpenCV"))
			v_in = new OpenCVModule();
		else if (common_configs.vid_library.equals("AGCamLib"))
			v_in = new AGCamLibModule();

		initializeFilters();

		v_in.setFormat(common_configs.format);

		return v_in.initialize(
				ref_fia,
				common_configs.width,
				common_configs.height,
				common_configs.cam_index);
	}

	/**
	 * Initializes video filters, and applies their configurations.
	 * 
	 * @return true: success
	 */
	private boolean initializeFilters()
	{
		final Point dims = new Point(common_configs.width, common_configs.height);

		final Link src_rgb_link = new Link(dims);
		final Link grey_link = new Link(dims);
		final Link marker_link = new Link(dims);

		SubtractorFilter subtractor_filter;
		RatFinder rat_finder;
		RearingDetector rearing_det;
		VideoRecorder vid_rec;
		ScreenDrawer screen_drawer;
		SourceFilter source_filter;

		// ////////////////////////////////////
		// Rat Finder
		final RatFinderFilterConfigs rat_finder_configs = new RatFinderFilterConfigs(
				"RatFinder",
				common_configs);
		rat_finder = new RatFinder(
				"RatFinder",
				grey_link,
				marker_link);
		rat_finder.configure(rat_finder_configs);
		final RatFinderData rfd = (RatFinderData) rat_finder.getFilterData();

		// ////////////////////////////////////
		// Rearing Detector
		final RearingFilterConfigs rearingConfigs = new RearingFilterConfigs(
				"RearingDetector",
				1000,
				200,
				200,
				(rfd.getCenterPoint()),
				common_configs);
		rearing_det = new RearingDetector(
				"RearingDetector",
				grey_link,
				null);
		rearing_det.configure(rearingConfigs);
		// ////////////////////////////////////
		// Video Recorder
		final RecorderConfigs vid_recorder_configs = new RecorderConfigs(
				"Recorder",
				common_configs);
		vid_rec = new VideoRecorder("Recorder", src_rgb_link, null);
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
		screen_drawer = new ScreenDrawer(
				"ScreenDrawer",
				src_rgb_link,
				marker_link,
				null);
		screen_drawer.configure(scrn_drwr_cnfgs);
		// ////////////////////////////////////
		// Subtraction Filter
		final SubtractionConfigs subtraction_configs = new SubtractionConfigs(
				"SubtractionFilter",
				40,
				common_configs);
		subtractor_filter = new SubtractorFilter(
				"SubtractionFilter",
				src_rgb_link,
				grey_link);
		subtractor_filter.configure(subtraction_configs);
		// ////////////////////////////////////
		// Source Filter
		final SourceFilterConfigs source_configs = new SourceFilterConfigs(
				"Source Filter",
				common_configs,
				ref_fia);
		source_filter = new SourceFilter("Source Filter", null, src_rgb_link);
		source_filter.configure(source_configs);
		// ////////////////////////////////////
		// add filters to the filter manager
		filter_mgr.addFilter(source_filter);
		filter_mgr.addFilter(vid_rec);
		filter_mgr.addFilter(subtractor_filter);
		filter_mgr.addFilter(rearing_det);
		filter_mgr.addFilter(rat_finder);
		filter_mgr.addFilter(screen_drawer);

		// ///////////////////////////////////
		// check that configurations of all filters are valid
		for (final VideoFilter vf : filter_mgr.getFilters())
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
	 * Checks if the Background (Subtraction filter) has been set.
	 * 
	 * @return
	 */
	public boolean isBgSet()
	{
		return bg_is_set;
	}

	/**
	 * Starts processing of the video stream and activating most of the video
	 * filters.
	 */
	public void startProcessing()
	{
		filter_mgr.enableFilter("SubtractionFilter", true);
		filter_mgr.enableFilter("RatFinder", true);
		filter_mgr.enableFilter("RearingDetector", true);
		PManager.getDefault().state = ProgramState.TRACKING;
		filter_mgr.submitDataObjects();
	}

	/**
	 * Starts video streaming.
	 */
	public void startStreaming()
	{
		try
		{
			video_processor_enabled = true;
			while (!v_in.startStream())
				Thread.sleep(100);
			filter_mgr.enableFilter("ScreenDrawer", true);
			final Thread th_main = new Thread(new RunnableProcessor());
			th_main.start();
			PManager.getDefault().state = ProgramState.STREAMING;
		} catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Stops processing of the video stream.
	 */
	public void stopProcessing()
	{
		filter_mgr.enableFilter("SubtractionFilter", false);
		filter_mgr.enableFilter("RearingDetector", false);
		filter_mgr.enableFilter("RatFinder", false);
		PManager.getDefault().state = ProgramState.STREAMING;
	}

	/**
	 * Unloads the video library.
	 */
	public void unloadLibrary()
	{
		video_processor_enabled = false;
		try
		{
			Thread.sleep(200);
		} catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
		v_in.stopModule();
		v_in = null;
		PManager.getDefault().state = ProgramState.IDLE;
	}

	/**
	 * Updates the configurations of filters.
	 * 
	 * @param filters_configs
	 *            an array of filters configurations, each configuration object
	 *            will be applied to its designated filter.
	 */
	public void updateFiltersConfigs(final FilterConfigs[] filters_configs)
	{
		for (final FilterConfigs f_cfg : filters_configs)
		{
			f_cfg.common_configs = common_configs;
			filter_mgr.applyConfigsToFilter(f_cfg);
		}
	}

}
