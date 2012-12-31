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

package utils.video.input;

import utils.video.FrameIntArray;

/**
 * Parent of All Video libraries to communicate with the VideoManager.
 * 
 * @author Creative
 */
public abstract class VidInputter<ConfigsType extends VidSourceConfigs> {
	protected ConfigsType	configs;
	protected FrameIntArray	fia;
	protected SourceStatus			status;
	protected boolean paused=false;
	
	public static enum SourceStatus{
		STREAMING,PAUSED,END_OF_STREAM,ERROR,INITIALIZING;
	}
	
	public static enum SourceType{
		FILE,CAM;
	}
	
	public abstract SourceType getType();
	
	/**
	 * Shows additional cam settings (brightness/contrast/...).
	 * 
	 * @return 0 means success
	 */
	public abstract int displayMoreSettings();

	/**
	 * Returns the name of the video library.
	 * 
	 * @return string containing the name of the video library
	 */
	public abstract String getName();

	/**
	 * Returns the number of available image capture devices.
	 * 
	 * @return number of available webcams
	 */
	public abstract int getNumCams();

	/**
	 * Returns the status of the video library.
	 * 
	 * @return
	 */
	public abstract SourceStatus getStatus();

	/**
	 * Initializes the video library.
	 * 
	 * @param frameData
	 *            frame data carrier object
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 * @return true if the initialization was successful
	 */
	public abstract boolean initialize(FrameIntArray frameData,
			ConfigsType configs);

	/**
	 * Sets the image format for the library (RGB/YUV).
	 * 
	 * @param format
	 *            new format
	 */
	public abstract void setFormat(String format);

	/**
	 * Starts the video stream.
	 * 
	 * @return true if successful
	 */
	public abstract boolean startStream();

	/**
	 * Stops the video library and deinitializes it.
	 */
	public abstract void stopModule();
	
	public void pauseStream() {
		paused=true;
		status=SourceStatus.PAUSED;
	}

	public void resumeStream() {
		paused=false;
	}

	public int getStreamPosition() {
		return -1;
	}

	public int getStreamLength() {
		return -1;
	}
}
