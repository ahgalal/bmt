package utils.video;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;

import utils.PManager;
import utils.PManager.ProgramState;
import utils.video.input.AGCamLibModule;
import utils.video.input.JMFModule;
import utils.video.input.JMyronModule;
import utils.video.input.OpenCVModule;
import utils.video.input.VidInputter;
import utils.video.processors.CommonFilterConfigs;
import utils.video.processors.FilterConfigs;
import utils.video.processors.FilterManager;
import utils.video.processors.VideoFilter;
import utils.video.processors.RatFinder.RatFinder;
import utils.video.processors.RatFinder.RatFinderData;
import utils.video.processors.RatFinder.RatFinderFilterConfigs;
import utils.video.processors.rearingdetection.RearingDetector;
import utils.video.processors.rearingdetection.RearingFilterConfigs;
import utils.video.processors.recorder.RecorderConfigs;
import utils.video.processors.recorder.VideoRecorder;
import utils.video.processors.screendrawer.ScreenDrawer;
import utils.video.processors.screendrawer.ScreenDrawerConfigs;
import utils.video.processors.subtractionfilter.SubtractionConfigs;
import utils.video.processors.subtractionfilter.SubtractorFilter;

public class VideoProcessor {
	private int[] bg_image_rgb;
	private boolean bg_is_set;

	private Point ref_center_point;

	private FrameIntArray ref_fia;
	private Graphics gfx_sec_screen,gfx_main_screen;
	private Component main_screen,secondary_screen;
	final int max_thresh=100;
	private String prev_lib;
	private int[] result_image;

	private VidInputter v_in;

	private SubtractorFilter subtractor_filter;
	private RatFinder rat_finder;
	private RearingDetector rearing_det;
	private VideoRecorder vid_rec;
	private ScreenDrawer screen_drawer;
	private boolean video_processor_enabled;
	private FilterManager filter_mgr;
	private CommonFilterConfigs common_configs;

	public FilterManager getFilter_mgr() {
		return filter_mgr;
	}
	


	public VideoProcessor(Component cmpnt_main_screen,Component cmpnt_secondary_screen)
	{
		common_configs=new CommonFilterConfigs(0, 0, 0, 0, null, null);
		video_processor_enabled=true;
		ref_fia = new FrameIntArray();
		main_screen = cmpnt_main_screen;
		secondary_screen = cmpnt_secondary_screen;
		gfx_main_screen=main_screen.getGraphics();
		gfx_sec_screen =secondary_screen.getGraphics();
		prev_lib="";
		ref_center_point=new Point();
		filter_mgr = new FilterManager();

	}

	public int[] process()
	{
		/*
		 * we have 2 options here:
		 * 1. subtract the RGB INT arrays 
		 * 		(width*height*3 subtraction operations)
		 * 2. convert the RGB INT arrays to byte array
		 * 		(Conversion(byte[]) + width*height subtraction operations +
		 * 		Conversion(int[])again )
		 * the 2nd option is implemented right now .. we need to investigate
		 * its performance!
		 */

		result_image=ref_fia.frame_data;

		for(VideoFilter v:filter_mgr.getFilters())
			result_image=v.process(result_image);

		return result_image;
	}

	private class RunnableProcessor implements Runnable
	{
		@Override
		public void run() {
			PManager.log.print("Started Video Processing", this);

			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {}

			while(video_processor_enabled)
			{
				if(v_in.getStatus()==1)
					process();

				try {
					Thread.sleep(1000/common_configs.frame_rate);
				} catch (InterruptedException e) {}
			}
			//////////////////////////////////
			// finish up opened filters/utils:
			//////////////////////////////////
			
			filter_mgr.disableAll();
			
			PManager.log.print("Ended Video Processing", this);
		}
	}



	public void displayMoreSettings()
	{
		v_in.displayMoreSettings();
	}

	private void flipBG()
	{
		bg_image_rgb=flipImage(bg_image_rgb, common_configs.width, common_configs.height);
		((SubtractorFilter)filter_mgr.getFilterByName("SubtractionFilter")).setBg_image(bg_image_rgb);
	}


	private int[] flipImage(int[] img,int width,int height)
	{
		int[] tmp_img = new int[width*height];
		for(int y=0;y<height;y++)
		{
			for(int x=0;x<width;x++)
			{
				tmp_img[y*width+x] = img[(height-y-1)*width+x];
			}
		}
		return tmp_img;
	}

	public int[] getRGBBackground()
	{
		updateBG();
		if(v_in instanceof JMFModule)
			return flipImage(bg_image_rgb, common_configs.width, common_configs.height);
		else
			return bg_image_rgb;
	}

	public void updateCommonConfigs(CommonFilterConfigs common_configs)
	{
		if(common_configs.cam_index!=-1)
			this.common_configs.cam_index=common_configs.cam_index;
		if(common_configs.format!=null)
			this.common_configs.format = common_configs.format;
		if(common_configs.vid_library!=null)
			this.common_configs.vid_library = common_configs.vid_library;
		if(common_configs.frame_rate!=-1)
			this.common_configs.frame_rate=common_configs.frame_rate;
		if(common_configs.height!=-1)
			this.common_configs.height=common_configs.height;
		if(common_configs.width!=-1)
			this.common_configs.width=common_configs.width;
	}


	//public boolean initialize(String v_lib,int frame_rate,int cam_index,int w,int h,int thresh)
	public boolean initialize(CommonFilterConfigs ip_common_configs)
	{
		updateCommonConfigs(ip_common_configs);

		if (common_configs.vid_library.equals("JMF"))
			v_in = new JMFModule(common_configs.format,common_configs.width,common_configs.height);
		else if (common_configs.vid_library.equals("JMyron"))
			v_in = new JMyronModule();
		else if (common_configs.vid_library.equals("OpenCV"))
			v_in = new OpenCVModule();
		else if (common_configs.vid_library.equals("AGCamLib"))
			v_in = new AGCamLibModule();


		
		if(bg_is_set)
			if((prev_lib.equals("JMF") & (v_in.getName().equals("JMyron")|v_in.getName().equals("OpenCV")))|
					((prev_lib.equals("JMyron"))|(prev_lib.equals("OpenCV")) & v_in.getName().equals("JMF")))
				flipBG();
		initializeFilters();


		return v_in.initialize(ref_fia, common_configs.width, common_configs.height, common_configs.cam_index);
	}
	
	private void initializeFilters()
	{
		//////////////////////////////////////
		// Rat Finder
		RatFinderFilterConfigs rat_finder_configs = new RatFinderFilterConfigs("RatFinder",max_thresh, ref_center_point,common_configs);
		rat_finder = new RatFinder("RatFinder",rat_finder_configs );
		RatFinderData rfd = (RatFinderData) rat_finder.getFilterData();
		
		//////////////////////////////////////
		// Rearing Detector
		RearingFilterConfigs rearingConfigs = new RearingFilterConfigs("RearingDetector",1000,200, 200,(Point) (rfd.getData()),common_configs);
		rearing_det = new RearingDetector("RearingDetector",rearingConfigs);

		//////////////////////////////////////
		// Video Recorder	
		RecorderConfigs vid_recorder_configs=new RecorderConfigs("Recorder",common_configs);
		vid_rec= new VideoRecorder("Recorder",vid_recorder_configs);

		//////////////////////////////////////
		// Screen Drawer
		ScreenDrawerConfigs scrn_drwr_cnfgs = new ScreenDrawerConfigs("ScreenDrawer",gfx_main_screen, gfx_sec_screen, v_in, ref_fia,common_configs,true);
		screen_drawer = new ScreenDrawer("ScreenDrawer",scrn_drwr_cnfgs);

		//////////////////////////////////////
		// Subtraction Filter
		SubtractionConfigs subtraction_configs = new SubtractionConfigs("SubtractionFilter",40,common_configs);
		subtractor_filter = new SubtractorFilter("SubtractionFilter",subtraction_configs);


		filter_mgr.addFilter(vid_rec);
		filter_mgr.addFilter(subtractor_filter);
		filter_mgr.addFilter(rearing_det);
		
		filter_mgr.addFilter(rat_finder);
		
		filter_mgr.addFilter(screen_drawer);
		
		
	}

	public boolean isBg_set() {
		return bg_is_set;
	}




	public void startProcessing()
	{
		subtractor_filter.enable(true);
		rat_finder.enable(true);
		rearing_det.enable(true);
		PManager.getDefault().state=ProgramState.TRACKING;
		filter_mgr.submitDataObjects();
	}

	public void startStreaming()
	{
		video_processor_enabled=true;
		boolean start=v_in.StartStream();
		while(!start);
		screen_drawer.enable(true);
		Thread th_main = new Thread(new RunnableProcessor());
		th_main.start();
		PManager.getDefault().state=ProgramState.STREAMING;
	}

	public void stopProcessing()
	{
		subtractor_filter.enable(false);
		rearing_det.enable(false);
		rat_finder.enable(false);
		PManager.getDefault().state=ProgramState.STREAMING;
	}

	public void unloadLibrary()
	{
		video_processor_enabled=false;
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		v_in.StopModule();
		prev_lib=v_in.getName();
		v_in=null;
		PManager.getDefault().state=ProgramState.IDLE;
	}

	private void updateBG()
	{
		bg_image_rgb=ref_fia.frame_data;
		((SubtractorFilter)filter_mgr.getFilterByName("SubtractionFilter")).setBg_image(bg_image_rgb);
		bg_is_set=true;
	}

	public void updateFiltersConfigs(FilterConfigs[] filters_configs)
	{
		for(FilterConfigs f_cfg: filters_configs)
		{
			f_cfg.common_configs = common_configs;
			filter_mgr.applyConfigsToFilter(f_cfg);
		}
	}

}
