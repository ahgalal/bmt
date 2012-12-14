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

package filters;

import utils.PManager;
import utils.StatusManager.StatusSeverity;

/**
 * Common configurations used by almost all VideoFilters.
 * 
 * @author Creative
 */
public class CommonFilterConfigs {

	/**
	 * Check the documentation of the constructor.
	 */
	private String	vidLibrary;

	private String	format;

	/**
	 * Check the documentation of the constructor.
	 */
	private int		width;

	private int	height;

	private int	frameRate;

	private int	camIndex;

	/**
	 * Initializations.
	 * 
	 * @param width
	 *            video image's width
	 * @param height
	 *            video image's height
	 * @param frameRate
	 *            frame rate of the video stream
	 * @param camIndex
	 *            index of the camera to use
	 * @param vidLibrary
	 *            video library used
	 * @param format
	 *            video format used (RGB/YUV..)
	 */
	public CommonFilterConfigs(final int width, final int height,
			final int frameRate, final int camIndex, final String vidLibrary,
			final String format) {
		super();
		this.setWidth(width);
		this.setHeight(height);
		this.setFrameRate(frameRate);
		this.setCamIndex(camIndex);
		this.setVidLibrary(vidLibrary);
		this.setFormat(format);
	}

	/**
	 * Checks that All common configurations are set. (for testing purposes
	 * only)
	 * 
	 * @return true: success
	 */
	public boolean validate() {
		if ((getWidth() <= 0) || (getHeight() <= 0) || (getFrameRate() <= 0)
				|| (getCamIndex() < 0) || (getVidLibrary() == null)
		/* || format == null */) // Ignoring format check as it is optional
		{
			PManager.log.print("Common Configs are not completely configured!",
					this, StatusSeverity.ERROR);
			return false;
		}
		return true;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	public void setVidLibrary(String vidLibrary) {
		this.vidLibrary = vidLibrary;
	}

	public String getVidLibrary() {
		return vidLibrary;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}

	public int getFrameRate() {
		return frameRate;
	}

	public void setCamIndex(int camIndex) {
		this.camIndex = camIndex;
	}

	public int getCamIndex() {
		return camIndex;
	}

}
