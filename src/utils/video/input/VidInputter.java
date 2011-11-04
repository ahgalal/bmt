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
public abstract class VidInputter<ConfigsType extends VidSourceConfigs>
{
	protected ConfigsType configs;
	protected int status;
	protected FrameIntArray fia;
	
	/**
	 * Initializes the video library.
	 * 
	 * @param frame_data
	 *            frame data carrier object
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 * @param cam_index
	 *            camera index to use
	 * @return true if the initialization was successful
	 */
	public abstract boolean initialize(FrameIntArray frame_data, ConfigsType configs);

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

	/**
	 * Sets the image format for the library (RGB/YUV).
	 * 
	 * @param format
	 *            new format
	 */
	public abstract void setFormat(String format);

	/**
	 * Returns the status of the video library.
	 * 
	 * @return 1 means streaming
	 */
	public abstract int getStatus();

	/**
	 * Returns the number of available image capture devices.
	 * 
	 * @return number of available webcams
	 */
	public abstract int getNumCams();

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
}
