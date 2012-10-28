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

package filters.subtractionfilter;

import filters.FilterConfigs;
import filters.FilterData;
import filters.Link;
import filters.VideoFilter;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.StatusManager.StatusSeverity;
import utils.video.FrameIntArray;
import utils.video.ImageManipulator;

/**
 * Subtracts the input image from a background image and produces an image of
 * differences (binary image).
 * 
 * @author Creative
 */
public class SubtractorFilter extends
		VideoFilter<SubtractionConfigs, FilterData> {
	private final FrameIntArray	bg_image_gray;

	private int[]				local_data;

	/**
	 * Initializes the filter.
	 * 
	 * @param name
	 *            filter's name
	 * @param linkIn
	 *            input Link for the filter
	 * @param linkOut
	 *            output Link from the filter
	 */
	public SubtractorFilter(final String name, final Link linkIn,
			final Link linkOut) {
		super(name, linkIn, linkOut);
		bg_image_gray = new FrameIntArray();
	}

	@Override
	public boolean configure(final FilterConfigs configs) {
		this.configs = (SubtractionConfigs) configs;
		local_data = new int[configs.common_configs.width
				* configs.common_configs.height];

		return super.configure(configs);
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public void process() {
		int tmp;
		final int threshMask = configs.threshold | configs.threshold << 8
				| configs.threshold << 16;
		if (configs.enabled)
			if (link_in.getData().length == bg_image_gray.frame_data.length) {
				local_data = ImageManipulator.rgbIntArray2GrayIntArray(link_in
						.getData());
				tmp = 0;
				for (int i = 0; i < local_data.length; i++) {
					tmp = local_data[i] - bg_image_gray.frame_data[i];

					if (tmp < 0)
						tmp *= -1;

					if (tmp < threshMask)
						local_data[i] = 0;
					else
						local_data[i] = 0x00FFFFFF;
				}
				link_out.setData(local_data);
			}
	}

	/**
	 * Subtract two images and produces a thresholed byte array image.
	 * 
	 * @param img1
	 *            input image 1
	 * @param img2
	 *            input image 2
	 * @return difference image as a byte array (binary image)
	 */
	@SuppressWarnings("unused")
	private byte[] subtractGrayImages(final byte[] img1, final byte[] img2) {
		if (img1.length == img2.length) {
			final byte[] res = new byte[img1.length];
			short tmp1 = 0, tmp2 = 0, tmp3;
			for (int i = 0; i < img1.length; i++) {
				tmp1 = img1[i];
				tmp2 = img2[i];
				if (tmp1 < 0)
					tmp1 = (short) (tmp1 + 256);
				if (tmp2 < 0)
					tmp2 = (short) (tmp2 + 256);
				tmp3 = (short) (tmp1 - tmp2);
				if (tmp3 < 0)
					tmp3 *= -1;
				if (tmp3 < configs.threshold)
					res[i] = 0;
				else
					res[i] = (byte) 0xFF;
			}
			return res;
		}
		return null;
	}

	/**
	 * Updates the background image of the filter.
	 */
	public void updateBG() {
		if (link_in.getData() != null)
			bg_image_gray.frame_data = ImageManipulator
					.rgbIntArray2GrayIntArray(link_in.getData());
		else
			PManager.log.print("Error updating BG, data is null", this,
					StatusSeverity.ERROR);
	}

	@Override
	public void updateProgramState(final ProgramState state) {
		// TODO Auto-generated method stub

	}

}
