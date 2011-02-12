package utils.video;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import utils.PManager;
import utils.PManager.ProgramState;
import utils.video.input.JMFModule;
import utils.video.input.JMyronModule;
import utils.video.input.OpenCVModule;
import utils.video.input.VidInputter;
import utils.video.processors.RatFinder;
import utils.video.processors.RearingDetector;
import utils.video.processors.ScreenDrawer;
import utils.video.processors.SubtractorFilter;
import utils.video.processors.VideoFilter;
import utils.video.processors.VideoRecorder;
import control.StatsController;

public class VideoProcessor {
	private FrameIntArray bg_image_gray,subtraction_result;
	private int[] bg_image_rgb;
	private boolean bg_is_set;

	private Point center_point;

	private FrameIntArray fia;
	private String format = "YUV";
	private int frame_rate = 30;
	private Graphics gfx_sec_screen,gfx_main_screen;
	private Component main_screen,secondary_screen;
	final int max_thresh=100;
	private PManager pm;
	private String prev_lib;
	private boolean record_video;
	private int thresh;
	private int[] result_image;

	private VidInputter v_in;

	private int width,height;
	private SubtractorFilter subtractor_filter;
	private RatFinder rat_finder;
	private RearingDetector rearing_det;
	private VideoRecorder vid_rec;
	private ScreenDrawer screen_drawer;
	private boolean video_processor_enabled;
	private ArrayList<VideoFilter> vid_filters;

	public VideoProcessor(Component cmpnt_main_screen,Component cmpnt_secondary_screen)
	{
		pm=PManager.getDefault();
		video_processor_enabled=true;
		fia = new FrameIntArray();
		main_screen = cmpnt_main_screen;
		secondary_screen = cmpnt_secondary_screen;
		gfx_main_screen=main_screen.getGraphics();
		gfx_sec_screen =secondary_screen.getGraphics();
		prev_lib="";
		center_point=new Point();
		vid_filters= new ArrayList<VideoFilter>();
	}

	public VideoFilter[] getVideoFilters()
	{
		VideoFilter[] tmp_arr = new VideoFilter[vid_filters.size()];
		vid_filters.toArray(tmp_arr);
		return tmp_arr;
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

		result_image=fia.frame_data;

		for(VideoFilter v:vid_filters)
			result_image=v.process(result_image);

		if(pm.state==ProgramState.TRACKING | pm.state==ProgramState.RECORDING)
		{
			boolean is_rearing=rearing_det.isRearing(subtraction_result.frame_data);
			StatsController.getDefault().updateStats(center_point,is_rearing);
		}
		return result_image;
	}

	private class RunnableProcessor implements Runnable
	{
		@Override
		public void run() {
			pm.log.print("Started Video Processing", this);

			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {}

			while(video_processor_enabled)
			{
				process();

				try {
					Thread.sleep(1000/frame_rate);
				} catch (InterruptedException e) {}
			}
			//////////////////////////////////
			// finish up opened filters/utils:
			//////////////////////////////////
			if(record_video)
				stopRecordingVideo();
			screen_drawer.stop();
			pm.log.print("Ended Video Processing", this);
		}
	}



	public void displayMoreSettings()
	{
		v_in.displayMoreSettings();
	}

	public void enableSecondaryScreen(boolean en)
	{
		screen_drawer.enableSecScreen(en);
	}

	private void flipBG()
	{
		bg_image_rgb=flipImage(bg_image_rgb, width, height);
		bg_image_gray.frame_data = ImageManipulator.rgbIntArray2GrayIntArray(bg_image_rgb);
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

	public int getNumCams(String lib)
	{
		return 0;
	}

	public int[] getRGBBackground()
	{
		updateBG();
		if(v_in instanceof JMFModule)
			return flipImage(bg_image_rgb, width, height);
		else
			return bg_image_rgb;
	}
	public int getThresh() {
		return thresh;
	}

	public boolean initialize(String v_lib,int frame_rate,int cam_index,int w,int h,int thresh)
	{
		width=w;
		height=h;
		this.thresh=thresh;
		setFrameRate(frame_rate);

		if (v_lib.equals("JMF"))
			v_in = new JMFModule(format,width,height);
		else if (v_lib.equals("JMyron"))
			v_in = new JMyronModule();
		else if (v_lib.equals("OpenCV"))
			v_in = new OpenCVModule();

		bg_image_gray = new FrameIntArray();
		if(bg_is_set)
			if((prev_lib.equals("JMF") & (v_in.getName().equals("JMyron")|v_in.getName().equals("OpenCV")))|
					((prev_lib.equals("JMyron"))|(prev_lib.equals("OpenCV")) & v_in.getName().equals("JMF")))
				flipBG();

		//////////////////////////////////////
		//initialization of Filters/Utilities:
		//////////////////////////////////////
		rat_finder = new RatFinder(height, width, max_thresh,center_point);
		rearing_det = new RearingDetector(width, height,center_point,200,200);
		vid_rec= new VideoRecorder(width, height);
		screen_drawer = new ScreenDrawer(gfx_main_screen, gfx_sec_screen,
				frame_rate, fia, width, height,v_in);
		subtraction_result= new FrameIntArray();
		subtractor_filter = new SubtractorFilter(bg_image_gray,subtraction_result,thresh);
		
		vid_filters.add(vid_rec);
		vid_filters.add(subtractor_filter);
		vid_filters.add(rearing_det);
		vid_filters.add(screen_drawer);
		vid_filters.add(rat_finder);


		return v_in.initialize(fia, width, height, cam_index);
	}

	public boolean isBg_set() {
		return bg_is_set;
	}


	public void setBg_image() {
		updateBG();
		bg_image_gray.frame_data = ImageManipulator.rgbIntArray2GrayIntArray(bg_image_rgb);
	}

	public void setBg_image(int[] bgImage) {
		bg_image_gray.frame_data = ImageManipulator.rgbIntArray2GrayIntArray(bgImage);
	}

	public void setFormat(String format)
	{
		this.format = format;
	}

	public void setFrameRate(int fr)
	{
		frame_rate = fr;
	}

	public void setThreshold(int t, int rearingThresh)
	{
		if(t!=-1)
		{
			thresh=t;
			subtractor_filter.setSubtractionThreshold(rearingThresh);
		}
		if(rearingThresh!=-1)
			rearing_det.setRearing_thresh(rearingThresh);
	}

	public void startProcessing()
	{
		subtractor_filter.enable(true);
		rat_finder.enable(true);
		rearing_det.enable(true);
		PManager.getDefault().state=ProgramState.TRACKING;
	}

	public void startStreaming()
	{
		video_processor_enabled=true;
		boolean start=v_in.StartStream();
		while(!start);
		screen_drawer.enable(true);
		screen_drawer.start();
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
		bg_image_rgb=fia.frame_data;
		bg_is_set=true;
	}

	public void rearingNow(boolean rearing) {
		rearing_det.rearinNow(rearing);
	}

	public void startRecordingVideo() {
		record_video=vid_rec.enable(true);
	}

	public void stopRecordingVideo() {
		record_video=false;
		vid_rec.enable(false);		
	}

	public void saveVideoFile(String fileName) {
		vid_rec.saveVideoFile(fileName);		
	}

	public void enableFilter(Class<?> cls,	boolean enable) {
		for(VideoFilter v: vid_filters)
		{
			if(v.getClass()==cls)
				v.enable(enable);
		}
	}

}
