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

import java.util.ArrayList;
import java.util.Iterator;

import modules.ExperimentManager;
import modules.ModulesManager;
import modules.experiment.Experiment;
import sys.utils.Utils;
import utils.PManager;
import utils.PManager.ProgramState.GeneralState;
import utils.PManager.ProgramState.StreamState;
import utils.StatusManager.StatusSeverity;
import utils.video.input.AGCamLibModule;
import utils.video.input.AGVidFileModule;
import utils.video.input.GStreamerModule;
import utils.video.input.JMyronModule;
import utils.video.input.OpenCVModule;
import utils.video.input.V4L2Module;
import utils.video.input.VidInputter;
import utils.video.input.VidInputter.SourceStatus;
import utils.video.input.VidInputter.SourceType;
import utils.video.input.VidSourceConfigs;
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
		private int			counter		= 0;
		private final int	MAX_COUNTER	= 5;

		protected void checkDeviceReady() {
			counter = 0;

			while ((vInput != null)
					&& ((vInput.getStatus() == SourceStatus.ERROR) || (vInput
							.getStatus() == SourceStatus.INITIALIZING))
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
			if ((counter == MAX_COUNTER)
					|| (vInput.getStatus() == SourceStatus.END_OF_STREAM))
				unloadLibrary();
		}

		@Override
		public void run() {
			PManager.log.print("Started Video Streaming", this);

			while (videoProcessorEnabled) {

				checkDeviceReady();

				final long t1 = System.currentTimeMillis();
				if ((vInput != null)
						&& (vInput.getStatus() == SourceStatus.STREAMING))
					for (final Iterator<VideoFilter<?, ?>> it = filterManager
							.getFilters(); it.hasNext();) {
						final VideoFilter<?, ?> vf = it.next();
						vf.process();
					}
				final long t2 = System.currentTimeMillis();

				// Utils.sleep(1000 / commonConfigs.getFrameRate());

				// adaptive sleep time
				final int filtersLoopTime = (int) (t2 - t1);
				int sleepTime = 1000 / commonConfigs.getFrameRate()
						- filtersLoopTime;
				if (sleepTime < 30)
					sleepTime = 30;
				Utils.sleep(sleepTime);

				// long t3=System.currentTimeMillis();
				// System.out.println(t3-t1);
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
			if (PManager.getDefault().getState().getGeneral() == GeneralState.TRACKING)
				PManager.getDefault().stopTracking();
			PManager.getDefault().getState().setStream(StreamState.IDLE);
			filterManager.disableAll();

			PManager.log.print("Ended Video Streaming", this);
		}
	}

	private final CommonFilterConfigs		commonConfigs;

	private final FrameIntArray				fia;
	private final FilterManager				filterManager;
	private final ArrayList<VidInputter<?>>	installedVidLibs;
	private boolean							isInitialized;
	private boolean							paused	= false;
	private Thread							thFiltersProcess;

	private boolean							videoProcessorEnabled;

	@SuppressWarnings("rawtypes")
	private VidInputter						vInput;

	/**
	 * Initialization.
	 */
	public VideoManager() {
		commonConfigs = new CommonFilterConfigs(640, 480, 30, 0, "VideoFile",
				null);
		videoProcessorEnabled = true;
		fia = new FrameIntArray();
		filterManager = new FilterManager(commonConfigs, fia);
		installedVidLibs = new ArrayList<VidInputter<?>>();

		initializeInstalledVidLibs();
	}

	/**
	 * Display additional settings of the input video library.
	 */
	public void displayMoreSettings() {
		vInput.displayMoreSettings();
	}

	public String[] getAvailableCamLibs() {
		final ArrayList<String> names = new ArrayList<String>();
		int i = 0;
		for (final VidInputter<?> inputter : installedVidLibs) {
			if (inputter.getType() == SourceType.CAM)
				names.add(inputter.getName());
			i++;
		}
		return names.toArray(new String[0]);
	}

	public String getDefaultCamLibrary() {
		for(VidInputter<?> inputter:installedVidLibs)
			if(inputter.getType()==SourceType.CAM)
				return inputter.getName();
		return null;
	}

	/**
	 * Gets the filter manager.
	 * 
	 * @return instance of the filter manager
	 */
	public FilterManager getFilterManager() {
		return filterManager;
	}

	public int getStreamLength() {
		int streamLength = 0;
		if (vInput != null)
			streamLength = vInput.getStreamLength();
		return streamLength;
	}

	public int getStreamPosition() {
		int streamPosition = 0;
		if (vInput != null)
			streamPosition = vInput.getStreamPosition();
		return streamPosition;
	}

	@SuppressWarnings("rawtypes")
	public VidInputter getVidInputter() {
		return vInput;
	}

	private void initFilters(final Experiment exp) {
		if (filterManager != null)
			filterManager.deInitialize();
		filterManager.instantiateFilters(fia, exp.getFiltersSetup());
	}

	/**
	 * Initialization of the video library.
	 * 
	 * @param ipCommonConfigs
	 *            common configurations object, used by almost all filters
	 * @param vidFile
	 * @return true: success
	 */
	@SuppressWarnings("unchecked")
	public boolean initialize(final CommonFilterConfigs ipCommonConfigs,
			final String vidFile) {

		if (ipCommonConfigs.getVidLibrary().equals("Cam")
				&& (commonConfigs.getVidLibrary() != null)
				&& commonConfigs.getVidLibrary().equals("VideoFile"))// cam lib
																		// is
																		// already
																		// set
																		// to
																		// VideoFile
			ipCommonConfigs.setVidLibrary(getDefaultCamLibrary());
		else if (ipCommonConfigs.getVidLibrary().equals("Cam")
				&& (commonConfigs.getVidLibrary() != null)
				&& (commonConfigs.getVidLibrary().equals("VideoFile") == false))
			ipCommonConfigs.setVidLibrary(commonConfigs.getVidLibrary());

		updateCommonConfigs(ipCommonConfigs);
		String vidLib = commonConfigs.getVidLibrary();
		if ((vidLib == null) || vidLib.equals("default"))
			vidLib = getDefaultCamLibrary();

		filterManager.initializeConfigs(commonConfigs);

		VidSourceConfigs srcConfigs = null;

		for (final VidInputter<?> inputter : installedVidLibs) {
			if (inputter.getName().equals(vidLib)) {
				vInput = inputter.newInstance();
			}
		}
		if (vidLib.equals("VideoFile"))
			if (vidFile != null) {
				for (final VidInputter<?> inputter : installedVidLibs) {
					// TODO: we take the first Vid file lib.
					if (inputter.getType() == SourceType.FILE) {
						vInput = inputter.newInstance();
					}
				}
			}

		vInput.setFormat(commonConfigs.getFormat());

		srcConfigs = vInput.newConfigurationInstance();
		srcConfigs.setVideoFilePath(vidFile);
		srcConfigs.setWidth(commonConfigs.getWidth());
		srcConfigs.setHeight(commonConfigs.getHeight());
		srcConfigs.setCamIndex(commonConfigs.getCamIndex());

		PManager.log.print("Vid lib: " + vidLib, this);

		isInitialized = vInput.initialize(fia, srcConfigs);
		return isInitialized;
	}

	private void initializeInstalledVidLibs() {
		final String os = PManager.getOS();
		if (os.equals("Linux")) {
			installedVidLibs.add(new V4L2Module());
			installedVidLibs.add(new OpenCVModule());
			installedVidLibs.add(new GStreamerModule());
		} else if (os.equals("Windows")) {
			installedVidLibs.add(new AGCamLibModule());
			installedVidLibs.add(new OpenCVModule());
			installedVidLibs.add(new JMyronModule());
			installedVidLibs.add(new AGVidFileModule());
		}
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void pauseStream() {
		if ((vInput != null) && (vInput.getStatus() == SourceStatus.STREAMING)) {
			paused = true;
			vInput.pauseStream();
			filterManager.pauseStream();
		}
	}

	public void resumeStream() {
		if ((vInput != null) && (vInput.getStatus() == SourceStatus.PAUSED)) {
			paused = false;
			vInput.resumeStream();
			thFiltersProcess.interrupt();
			filterManager.resumeStream();
		}
	}

	/**
	 * Initializes filters and Modules based on the input experiment type.
	 * 
	 * @param exp
	 */
	public void setupModulesAndFilters(final Experiment exp) {
		// TODO: remove the next 2 lines??
		filterManager.initializeConfigs(commonConfigs);
		ModulesManager.getDefault().initializeConfigs();

		ModulesManager.getDefault().setupModules(exp);
		ModulesManager.getDefault().setModulesWidthandHeight(
				commonConfigs.getWidth(), commonConfigs.getHeight());
		initFilters(exp);
		PManager.getDefault().signalProgramStateUpdate();
	}

	public void signalFiltersSetupChange() {
		ExperimentManager.getDefault().signalFiltersSetupChange();
	}

	/**
	 * Starts processing of the video stream and activating most of the video
	 * filters.
	 */
	public void startProcessing() {
		filterManager.startProcessing();
		PManager.getDefault().getState().setGeneral(GeneralState.TRACKING);
	}

	/**
	 * Starts video streaming.
	 */
	public boolean startStreaming() {
		try {
			videoProcessorEnabled = true;
			while (!vInput.startStream())
				Thread.sleep(100);
			filterManager.startStreaming();

			thFiltersProcess = new Thread(new RunnableProcessor(),
					"Filters process");
			thFiltersProcess.start();
			PManager.getDefault().getState().setStream(StreamState.STREAMING);
			filterManager.submitDataObjects();

			if (vInput.getType() == SourceType.FILE) {
				while (vInput.getStatus() != SourceStatus.STREAMING)
					Thread.sleep(200);
				Thread.sleep(200);
				PManager.getDefault().pauseResume();
			}
			return true;
		} catch (final VideoLoadException e) {
			PManager.getDefault().getStatusMgr()
					.setStatus(e.getMessage(), StatusSeverity.ERROR);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Stops processing of the video stream.
	 */
	public void stopProcessing() {
		filterManager.stopProcessing();
	}

	/**
	 * Unloads the video library.
	 */
	public void unloadLibrary() {
		// if paused, we need to resume to unlock the paused thread
		if (PManager.getDefault().getState().getStream() == StreamState.PAUSED) {
			resumeStream();
			filterManager.stopStreaming();
		}

		isInitialized = false;
		videoProcessorEnabled = false;
		Utils.sleep(200);
		vInput.stopModule();
		vInput = null;
	}

	/**
	 * Updates common configurations used by almost all filters.
	 * 
	 * @param commonConfigs
	 *            common configurations to apply
	 */
	public void updateCommonConfigs(final CommonFilterConfigs commonConfigs) {
		if (commonConfigs.getCamIndex() != -1)
			this.commonConfigs.setCamIndex(commonConfigs.getCamIndex());
		if (commonConfigs.getFormat() != null)
			this.commonConfigs.setFormat(commonConfigs.getFormat());
		if (commonConfigs.getVidLibrary() != null)
			this.commonConfigs.setVidLibrary(commonConfigs.getVidLibrary());
		if (commonConfigs.getFrameRate() != -1)
			this.commonConfigs.setFrameRate(commonConfigs.getFrameRate());
		if (commonConfigs.getHeight() != -1)
			this.commonConfigs.setHeight(commonConfigs.getHeight());
		if (commonConfigs.getWidth() != -1)
			this.commonConfigs.setWidth(commonConfigs.getWidth());
		this.commonConfigs.validate();
	}

	/**
	 * Updates the configurations of filters.
	 * 
	 * @param filtersConfigs
	 *            an array of filters configurations, each configuration object
	 *            will be applied to its designated filter.
	 */
	public void updateFiltersConfigs(final FilterConfigs[] filtersConfigs) {
		for (final FilterConfigs fCfg : filtersConfigs) {
			fCfg.setCommonConfigs(commonConfigs);
			filterManager.applyConfigsToFilter(fCfg);
		}
	}

}
