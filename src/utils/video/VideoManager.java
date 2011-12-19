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

import modules.experiment.Experiment.ExperimentType;
import utils.Logger.Details;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;
import utils.video.filters.FilterManager;
import utils.video.filters.VideoFilter;
import utils.video.input.AGCamLibModule;
import utils.video.input.AGVidLibConfigs;
import utils.video.input.JMFModule;
import utils.video.input.JMyronModule;
import utils.video.input.OpenCVConfigs;
import utils.video.input.OpenCVModule;
import utils.video.input.V4L2Module;
import utils.video.input.VidInputter;
import utils.video.input.VidSourceConfigs;
import utils.video.input.VideoFileModule;

/**
 * Main Video Manager, manages all video operations.
 * 
 * @author Creative
 */
public class VideoManager {
    private final FrameIntArray ref_fia;

    @SuppressWarnings("rawtypes")
    private VidInputter v_in;

    private boolean video_processor_enabled;
    private FilterManager filter_mgr;
    private final CommonFilterConfigs commonConfigs;
    private boolean isInitialized;

    /**
     * Gets the filter manager.
     * 
     * @return instance of the filter manager
     */
    public FilterManager getFilterManager() {
	return filter_mgr;
    }

    /**
     * Initialization.
     */
    public VideoManager() {
	commonConfigs = new CommonFilterConfigs(640, 480, 30, 0, null, null);
	video_processor_enabled = true;
	ref_fia = new FrameIntArray();
	// filter_mgr = new FilterManager(common_configs, ref_fia);
	// filter_mgr.configureFilters(common_configs, ref_fia);
    }

    /**
     * Runnable for grabbing image from the input library and pass it to all
     * filters.
     * 
     * @author Creative
     */
    private class RunnableProcessor implements Runnable {
	private int counter = 0;

	@Override
	public void run() {
	    PManager.log.print("Started Video Streaming", this);

	    try {
		Thread.sleep(100);
	    } catch (final InterruptedException e1) {
	    }

	    while (video_processor_enabled) {
		counter = 0;
		while (v_in != null && v_in.getStatus() != 1 && counter < 5)
		    try {
			Thread.sleep(1000);
			PManager.log.print("Device is not Ready!", this,
				StatusSeverity.ERROR);
			counter++;
		    } catch (final InterruptedException e) {
			e.printStackTrace();
		    }
		// PManager.log.print("Device is not ready yet!", this);
		if (v_in != null && v_in.getStatus() == 1)
		    for (final VideoFilter<?, ?> v : filter_mgr.getFilters())
			v.process();

		try {
		    Thread.sleep(1000 / commonConfigs.frame_rate);
		} catch (final InterruptedException e) {
		}
		if (counter == 5)
		    unloadLibrary();
	    }
	    // ////////////////////////////////
	    // finish up opened filters/utils:
	    // ////////////////////////////////

	    PManager.getDefault().stopTracking();
	    filter_mgr.disableAll();

	    PManager.log.print("Ended Video Streaming", this);
	}
    }

    /**
     * Display additional settings of the input video library.
     */
    public void displayMoreSettings() {
	v_in.displayMoreSettings();
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
	common_configs.validate();
    }

    private String getOS() {
	final String os = System.getProperty("os.name");
	PManager.log.print("OS: " + os, this, Details.VERBOSE);
	if (os.indexOf("Linux") != -1)
	    return "Linux";
	else if (os.indexOf("Windows") != -1)
	    return "Windows";

	System.out.print("Unknown OS\n");
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

    public String[] getAvailableVidLibs() {
	final String os = getOS();
	if (os.equals("Linux"))
	    return new String[] { "V4L2", "OpenCV" };
	else if (os.equals("Windows"))
	    return new String[] { "AGCamLib", "JMF", "OpenCV", "JMyron" };
	return null;
    }

    /**
     * Initialization of the video library and video filters.
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
	if (vid_lib.equals("default"))
	    vid_lib = getDefaultVideoLibrary();

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
		    v_in = new OpenCVModule();
		    srcConfigs = new OpenCVConfigs();
		    ((OpenCVConfigs) srcConfigs).fileName = vidFile;
		}

	v_in.setFormat(commonConfigs.format);

	srcConfigs.width = commonConfigs.width;
	srcConfigs.height = commonConfigs.height;
	srcConfigs.camIndex = commonConfigs.cam_index;

	return v_in.initialize(ref_fia, srcConfigs);
    }

    public void initializeFilters(final ExperimentType expType) {
	filter_mgr = new FilterManager(commonConfigs, ref_fia, expType);
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
	PManager.getDefault().state = ProgramState.TRACKING;
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
	    final Thread th_main = new Thread(new RunnableProcessor());
	    th_main.start();
	    PManager.getDefault().state = ProgramState.STREAMING;
	    filter_mgr.submitDataObjects();
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
	PManager.getDefault().state = ProgramState.STREAMING;
    }

    /**
     * Unloads the video library.
     */
    public void unloadLibrary() {
	isInitialized = false;
	video_processor_enabled = false;
	try {
	    Thread.sleep(200);
	} catch (final InterruptedException e) {
	    e.printStackTrace();
	}
	v_in.stopModule();
	v_in = null;
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

    @SuppressWarnings("rawtypes")
    public VidInputter getVidInputter() {
	return v_in;
    }

    public boolean isInitialized() {
	return isInitialized;
    }

}
