package utils.video;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;

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
import control.ShapeController;

public class VideoProcessor
{
	private int[] bg_image_rgb;
	private boolean bg_is_set;

	private final Point ref_center_point;

	private final FrameIntArray ref_fia;
	private final Graphics gfx_sec_screen, gfx_main_screen;
	private final Component main_screen, secondary_screen;

	private VidInputter v_in;

	private SubtractorFilter subtractor_filter;
	private RatFinder rat_finder;
	private RearingDetector rearing_det;
	private VideoRecorder vid_rec;
	private ScreenDrawer screen_drawer;
	private boolean video_processor_enabled;
	private final FilterManager filter_mgr;
	private final CommonFilterConfigs common_configs;

	public FilterManager getFilterManager()
	{
		return filter_mgr;
	}

	public VideoProcessor(
			final Component cmpnt_main_screen,
			final Component cmpnt_secondary_screen)
	{
		common_configs = new CommonFilterConfigs(0, 0, 0, 0, null, null);
		video_processor_enabled = true;
		ref_fia = new FrameIntArray();
		main_screen = cmpnt_main_screen;
		secondary_screen = cmpnt_secondary_screen;
		gfx_main_screen = main_screen.getGraphics();
		gfx_sec_screen = secondary_screen.getGraphics();
		ref_center_point = new Point();
		filter_mgr = new FilterManager();

	}

	private class RunnableProcessor implements Runnable
	{
		@Override
		public void run()
		{
			PManager.log.print("Started Video Processing", this);

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

			PManager.log.print("Ended Video Processing", this);
		}
	}

	public void displayMoreSettings()
	{
		v_in.displayMoreSettings();
	}

	public int[] getRGBBackground()
	{
		updateBG();
		return bg_image_rgb;
	}

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
	}

	public boolean initialize(final CommonFilterConfigs ip_common_configs)
	{
		updateCommonConfigs(ip_common_configs);

		if (common_configs.vid_library.equals("JMF"))
			v_in = new JMFModule(
					common_configs.format,
					common_configs.width,
					common_configs.height);
		else if (common_configs.vid_library.equals("JMyron"))
			v_in = new JMyronModule();
		else if (common_configs.vid_library.equals("OpenCV"))
			v_in = new OpenCVModule();
		else if (common_configs.vid_library.equals("AGCamLib"))
			v_in = new AGCamLibModule();

		initializeFilters();

		return v_in.initialize(
				ref_fia,
				common_configs.width,
				common_configs.height,
				common_configs.cam_index);
	}

	private void initializeFilters()
	{
		final Point dims = new Point(common_configs.width, common_configs.height);

		final Link src_rgb_link = new Link(dims);
		final Link grey_link = new Link(dims);
		final Link marker_link = new Link(dims);

		// ////////////////////////////////////
		// Rat Finder
		final RatFinderFilterConfigs rat_finder_configs = new RatFinderFilterConfigs(
				"RatFinder",
				100,
				ref_center_point,
				common_configs);

		rat_finder = new RatFinder(
				"RatFinder",
				rat_finder_configs,
				grey_link,
				marker_link);
		final RatFinderData rfd = (RatFinderData) rat_finder.getFilterData();

		// ////////////////////////////////////
		// Rearing Detector
		final RearingFilterConfigs rearingConfigs = new RearingFilterConfigs(
				"RearingDetector",
				1000,
				200,
				200,
				(Point) (rfd.getData()),
				common_configs);
		rearing_det = new RearingDetector(
				"RearingDetector",
				rearingConfigs,
				grey_link,
				null);

		// ////////////////////////////////////
		// Video Recorder
		final RecorderConfigs vid_recorder_configs = new RecorderConfigs(
				"Recorder",
				common_configs);
		vid_rec = new VideoRecorder("Recorder", vid_recorder_configs, src_rgb_link, null);

		// ////////////////////////////////////
		// Screen Drawer
		final ScreenDrawerConfigs scrn_drwr_cnfgs = new ScreenDrawerConfigs(
				"ScreenDrawer",
				gfx_main_screen,
				gfx_sec_screen,
				v_in,
				common_configs,
				true,
				ShapeController.getDefault());
		screen_drawer = new ScreenDrawer(
				"ScreenDrawer",
				scrn_drwr_cnfgs,
				src_rgb_link,
				marker_link,
				null);

		// ////////////////////////////////////
		// Subtraction Filter
		final SubtractionConfigs subtraction_configs = new SubtractionConfigs(
				"SubtractionFilter",
				40,
				common_configs);
		subtractor_filter = new SubtractorFilter(
				"SubtractionFilter",
				subtraction_configs,
				src_rgb_link,
				grey_link);

		// ////////////////////////////////////
		// Source Filter
		final SourceFilterConfigs source_configs = new SourceFilterConfigs(
				"Source Filter",
				common_configs,
				ref_fia);
		final SourceFilter source_filter = new SourceFilter(
				"Source Filter",
				source_configs,
				null,
				src_rgb_link);

		filter_mgr.addFilter(source_filter);
		filter_mgr.addFilter(vid_rec);
		filter_mgr.addFilter(subtractor_filter);
		filter_mgr.addFilter(rearing_det);
		filter_mgr.addFilter(rat_finder);
		filter_mgr.addFilter(screen_drawer);
	}

	public boolean isBgSet()
	{
		return bg_is_set;
	}

	public void startProcessing()
	{
		subtractor_filter.enable(true);
		rat_finder.enable(true);
		rearing_det.enable(true);
		PManager.getDefault().state = ProgramState.TRACKING;
		filter_mgr.submitDataObjects();
	}

	public void startStreaming()
	{
		try
		{
			video_processor_enabled = true;
			while (!v_in.startStream())
				Thread.sleep(100);
			screen_drawer.enable(true);
			final Thread th_main = new Thread(new RunnableProcessor());
			th_main.start();
			PManager.getDefault().state = ProgramState.STREAMING;
		} catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	public void stopProcessing()
	{
		subtractor_filter.enable(false);
		rearing_det.enable(false);
		rat_finder.enable(false);
		PManager.getDefault().state = ProgramState.STREAMING;
	}

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

	private void updateBG()
	{
		bg_image_rgb = ref_fia.frame_data;
		((SubtractorFilter) filter_mgr.getFilterByName("SubtractionFilter")).updateBG();
		bg_is_set = true;
	}

	public void updateFiltersConfigs(final FilterConfigs[] filters_configs)
	{
		for (final FilterConfigs f_cfg : filters_configs)
		{
			f_cfg.common_configs = common_configs;
			filter_mgr.applyConfigsToFilter(f_cfg);
		}
	}

}
