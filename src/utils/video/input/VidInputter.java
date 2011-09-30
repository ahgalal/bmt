/***************************************************************************
 *  Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly,
 *  Sarah Hamid and Mohammed Ahmed Ramadan
 *  contact: ceng.ahmedgalal@gmail.com
 *
 *  This file is part of Behavioral Monitoring Tool.
 *
 *  Behavioral Monitoring Tool is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, version 3 of the
 *  License.
 *
 *  Behavioral Monitoring Tool is distributed in the hope that it
 *  will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Behavioral Monitoring Tool.
 *  If not, see <http://www.gnu.org/licenses/>.
 *   
 **************************************************************************/

package utils.video.input;

import utils.video.FrameIntArray;

/**
 * Interface for Video libraries to communicate with the VideoManager.
 * 
 * @author Creative
 */
public interface VidInputter
{
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
	boolean initialize(FrameIntArray frame_data, int width, int height, int cam_index);

	/**
	 * Starts the video stream.
	 * 
	 * @return true if successful
	 */
	boolean startStream();

	/**
	 * Stops the video library and deinitializes it.
	 */
	void stopModule();

	/**
	 * Sets the image format for the library (RGB/YUV).
	 * 
	 * @param format
	 *            new format
	 */
	void setFormat(String format);

	/**
	 * Returns the status of the video library.
	 * 
	 * @return 1 means streaming
	 */
	int getStatus();

	/**
	 * Returns the number of available image capture devices.
	 * 
	 * @return number of available webcams
	 */
	int getNumCams();

	/**
	 * Shows additional cam settings (brightness/contrast/...).
	 * 
	 * @return 0 means success
	 */
	int displayMoreSettings();

	/**
	 * Returns the name of the video library.
	 * 
	 * @return string containing the name of the video library
	 */
	String getName();
}
