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

package utils.video;

import jagvidlib.JAGVidLib.VideoLoadException;
import modules.ModulesManager;
import modules.experiment.Experiment;
import modules.experiment.ExperimentType;
import utils.Logger.Details;
import utils.PManager;
import utils.PManager.ProgramState.GeneralState;
import utils.PManager.ProgramState.StreamState;
import utils.StatusManager.StatusSeverity;
import utils.Utils;
import utils.video.input.AGCamLibModule;
import utils.video.input.AGVidLibConfigs;
import utils.video.input.GStreamerModule;
import utils.video.input.JMFModule;
import utils.video.input.JMyronModule;
import utils.video.input.OpenCVConfigs;
import utils.video.input.OpenCVModule;
import utils.video.input.V4L2Module;
import utils.video.input.VidInputter;
import utils.video.input.VidInputter.SourceStatus;
import utils.video.input.VidInputter.SourceType;
import utils.video.input.VidSourceConfigs;
import utils.video.input.VideoFileModule;
import filters.CommonFilterConfigs;
import filters.FilterConfigs;
import filters.FilterManager;
import filters.VideoFilter;

/**
 * Main Video Manager, manages all video operations.
 * 
 * @author Creative
 */
public class VideoManager {
	/**
	 * Runnable for grabbing image from the input library and pass it to all
	 * filters.
	 * 
	 * @author Creative
	 */
	private class RunnableProcessor implements Runnable {
		private int	counter	= 0;
		private final int MAX_COUNTER = 5;
		protected void checkDeviceReady() {
			counter = 0;
			
			while ((v_in != null) && (v_in.getStatus() == SourceStatus.ERROR)
					&& (counter < MAX_COUNTER)) {
				try {
					Thread.sleep(1000);
					PManager.log.print("Device is not Ready!", this,
							StatusSeverity.ERROR);
					counter++;
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (counter == MAX_COUNTER)
				unloadLibrary();
		}

		@Override
		public void run() {
			PManager.log.print("Started Video Streaming", this);

			/*
			 * try { Thread.sleep(100); } catch (final InterruptedException e1)
			 * { }
			 */

			while (video_processor_enabled) {
				checkDeviceReady();

				if ((v_in != null)
						&& (v_in.getStatus() == SourceStatus.STREAMING))
					for (final VideoFilter<?, ?> v : filter_mgr.getFilters())
						v.process();

				Utils.sleep(1000 / commonConfigs.frame_rate);

				synchronized (this) {
					while (paused)
						try {
							this.wait();
						} catch (final InterruptedException e) {
							PManager.log.print("Stream is Resumed", this);
						}
				}
			}
			// ////////////////////////////////
			// finish up opened filters/utils:
			// ////////////////////////////////

			// auto stop tracking if stream is terminated
			if(counter==MAX_COUNTER && PManager.getDefault().getState().getGeneral()==GeneralState.TRACKING)
				PManager.getDefault().stopTracking();
			PManager.getDefault().getState().setStream(StreamState.IDLE);
			filter_mgr.disableAll();

			PManager.log.print("Ended Video Streaming", this);
		}
	}

	private final CommonFilterConfigs	commonConfigs;

	private FilterManager				filter_mgr;

	private boolean						isInitialized;
	private boolean						paused	= false;
	private final FrameIntArray			ref_fia;
	private Thread						thFiltersProcess;

	@SuppressWarnings("rawtypes")
	private VidInputter					v_in;

	private boolean						video_processor_enabled;

	/**
	 * Initialization.
	 */
	public VideoManager() {
		commonConfigs = new CommonFilterConfigs(640, 480, 30, 0, "VideoFile", null);
		video_processor_enabled = true;
		ref_fia = new FrameIntArray();
		filter_mgr = new FilterManager(commonConfigs, ref_fia/*, expType*/);
		// filter_mgr = new FilterManager(common_configs, ref_fia);
		// filter_mgr.configureFilters(common_configs, ref_fia);
	}

	/**
	 * Display additional settings of the input video library.
	 */
	public void displayMoreSettings() {
		v_in.displayMoreSettings();
	}

	public String[] getAvailableCamLibs() {
		final String os = getOS();
		if (os.equals("Linux"))
			return new String[] { "V4L2", "OpenCV" };
		else if (os.equals("Windows"))
			return new String[] { "AGCamLib", "JMF", "OpenCV", "JMyron" };
		return null;
	}

	public String getDefaultVideoLibrary() {
		final String os = getOS();
		if (os.equals("Linux"))
			return "V4L2";
		else if (os.equals("Windows"))
			return "AGCamLib";
		return os;
	}

	/**
	 * Gets the filter manager.
	 * 
	 * @return instance of the filter manager
	 */
	public FilterManager getFilterManager() {
		return filter_mgr;
	}

	private String getOS() {
		final String os = System.getProperty("os.name");
		PManager.log.print("OS: " + os, this, Details.VERBOSE);
		if (os.contains("Linux"))
			return "Linux";
		else if (os.contains("Windows"))
			return "Windows";

		System.out.print("Unknown OS\n");
		return null;
	}

	@SuppressWarnings("rawtypes")
	public VidInputter getVidInputter() {
		return v_in;
	}

	/**
	 * Initialization of the video library.
	 * 
	 * @param ip_common_configs
	 *            common configurations object, used by almost all filters
	 * @param vidFile
	 * @return true: success
	 */
	@SuppressWarnings("unchecked")
	public boolean initialize(final CommonFilterConfigs ip_common_configs,
			final String vidFile) {
		isInitialized = true;
		
		updateCommonConfigs(ip_common_configs);
		String vid_lib = commonConfigs.vid_library;
		if (vid_lib==null || vid_lib.equals("default"))
			vid_lib = getDefaultVideoLibrary();

		filter_mgr.initializeConfigs(commonConfigs);

		VidSourceConfigs srcConfigs = null;

		if (vid_lib.equals("JMF")) {
			v_in = new JMFModule();
			srcConfigs = new VidSourceConfigs();
		} else if (vid_lib.equals("JMyron")) {
			v_in = new JMyronModule();
			srcConfigs = new VidSourceConfigs();
		} else if (vid_lib.equals("OpenCV")) {
			v_in = new OpenCVModule();
			srcConfigs = new OpenCVConfigs();
		} else if (vid_lib.equals("AGCamLib")) {
			v_in = new AGCamLibModule();
			srcConfigs = new VidSourceConfigs();
		} else if (vid_lib.equals("V4L2")) {
			v_in = new V4L2Module();
			srcConfigs = new VidSourceConfigs();
		} else if (vid_lib.equals("VideoFile"))
			if (vidFile != null)
				if (System.getProperty("os.name").toLowerCase()
						.contains("windows")) {
					v_in = new VideoFileModule();
					srcConfigs = new AGVidLibConfigs();
					((AGVidLibConfigs) srcConfigs).vidFile = vidFile;
				} else {
					v_in = new GStreamerModule();
					srcConfigs = new VidSourceConfigs();
					srcConfigs.videoFilePath= vidFile;
				}

		v_in.setFormat(commonConfigs.format);

		srcConfigs.width = commonConfigs.width;
		srcConfigs.height = commonConfigs.height;
		srcConfigs.camIndex = commonConfigs.cam_index;
		
		PManager.log.print("Vid lib: "+ vid_lib, this);

		return v_in.initialize(ref_fia, srcConfigs);
	}
	
	private void initFilters(final ExperimentType expType){
		if (filter_mgr != null)
			filter_mgr.deInitialize();
		filter_mgr.instantiateFilters(ref_fia, expType);
	}

	public boolean isInitialized() {
		return isInitialized;
	}
	
	/**
	 * Initializes filters and Modules based on the input experiment type.
	 * @param exp
	 */
	public void setupModulesAndFilters(Experiment exp){
		filter_mgr.initializeConfigs(commonConfigs);
		ModulesManager.getDefault().setupModules(exp);
		ModulesManager.getDefault().setModulesWidthandHeight(commonConfigs.width, commonConfigs.height);
		initFilters(exp.type);
		PManager.getDefault().signalProgramStateUpdate();
	}

	public void pauseStream() {
		if ((v_in != null) && (v_in.getStatus() == SourceStatus.STREAMING)) {
			paused = true;
			v_in.pauseStream();
			filter_mgr.enableFilter("ScreenDrawer", false);
		}
	}

	public void resumeStream() {
		if ((v_in != null) && (v_in.getStatus() == SourceStatus.PAUSED)) {
			paused = false;
			v_in.resumeStream();
			//if(thFiltersProcess.)
			thFiltersProcess.interrupt();
			filter_mgr.enableFilter("ScreenDrawer", true);
		}
	}

	/**
	 * Starts processing of the video stream and activating most of the video
	 * filters.
	 */
	public void startProcessing() {
		filter_mgr.enableFilter("SubtractionFilter", true);
		filter_mgr.enableFilter("RatFinder", true);
		filter_mgr.enableFilter("RearingDetector", true);
		filter_mgr.enableFilter("Average Filter", true);
		PManager.getDefault().getState().setGeneral(GeneralState.TRACKING);
	}

	/**
	 * Starts video streaming.
	 */
	public void startStreaming() {
		try {
			video_processor_enabled = true;
			while (!v_in.startStream())
				Thread.sleep(100);
			filter_mgr.enableFilter("ScreenDrawer", true);
			thFiltersProcess = new Thread(new RunnableProcessor());
			thFiltersProcess.start();
			PManager.getDefault().getState().setStream(StreamState.STREAMING);
			filter_mgr.submitDataObjects();

			if (v_in.getType() == SourceType.FILE) {
				while (v_in.getStatus() != SourceStatus.STREAMING)
					Thread.sleep(200);
				PManager.getDefault().pauseResume();
			}

		}catch (VideoLoadException e) {
			PManager.getDefault().statusMgr.setStatus(e.getMessage(), StatusSeverity.ERROR);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops processing of the video stream.
	 */
	public void stopProcessing() {
		filter_mgr.enableFilter("SubtractionFilter", false);
		filter_mgr.enableFilter("RearingDetector", false);
		filter_mgr.enableFilter("RatFinder", false);
		filter_mgr.enableFilter("Average Filter", false);
		// PManager.getDefault().getState().setStream(StreamState.STREAMING);
	}

	/**
	 * Unloads the video library.
	 */
	public void unloadLibrary() {
		// if paused, we need to resume to unlock the paused thread
		if (PManager.getDefault().getState().getStream() == StreamState.PAUSED) {
			resumeStream();
			filter_mgr.enableFilter("ScreenDrawer", false);
		}

		isInitialized = false;
		video_processor_enabled = false;
		Utils.sleep(200);
		v_in.stopModule();
		v_in = null;
	}

	/**
	 * Updates common configurations used by almost all filters.
	 * 
	 * @param common_configs
	 *            common configurations to apply
	 */
	public void updateCommonConfigs(final CommonFilterConfigs common_configs) {
		if (common_configs.cam_index != -1)
			this.commonConfigs.cam_index = common_configs.cam_index;
		if (common_configs.format != null)
			this.commonConfigs.format = common_configs.format;
		if (common_configs.vid_library != null)
			this.commonConfigs.vid_library = common_configs.vid_library;
		if (common_configs.frame_rate != -1)
			this.commonConfigs.frame_rate = common_configs.frame_rate;
		if (common_configs.height != -1)
			this.commonConfigs.height = common_configs.height;
		if (common_configs.width != -1)
			this.commonConfigs.width = common_configs.width;
		this.commonConfigs.validate();
	}

	/**
	 * Updates the configurations of filters.
	 * 
	 * @param filters_configs
	 *            an array of filters configurations, each configuration object
	 *            will be applied to its designated filter.
	 */
	public void updateFiltersConfigs(final FilterConfigs[] filters_configs) {
		for (final FilterConfigs f_cfg : filters_configs) {
			f_cfg.common_configs = commonConfigs;
			filter_mgr.applyConfigsToFilter(f_cfg);
		}
	}

}
