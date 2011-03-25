package utils.video.processors.subtractionfilter;

import utils.video.FrameIntArray;
import utils.video.ImageManipulator;
import utils.video.processors.FilterConfigs;
import utils.video.processors.VideoFilter;

public class SubtractorFilter extends VideoFilter
{

	private final FrameIntArray bg_image_gray;
	private final SubtractionConfigs subtraction_configs;

	public SubtractorFilter(final String name, final FilterConfigs configs)
	{
		super(name, configs);
		bg_image_gray = new FrameIntArray();
		this.subtraction_configs = (SubtractionConfigs) configs;
	}

	public void setBgImage(final int[] bgImage)
	{
		bg_image_gray.frame_data = ImageManipulator.rgbIntArray2GrayIntArray(bgImage);
	}

	public byte[] subtractGrayImages(final byte[] img1, final byte[] img2)
	{
		if (img1.length == img2.length)
		{
			final byte[] res = new byte[img1.length];
			short tmp1 = 0, tmp2 = 0, tmp3;
			for (int i = 0; i < img1.length; i++)
			{
				tmp1 = img1[i];
				tmp2 = img2[i];
				if (tmp1 < 0)
					tmp1 = (short) (tmp1 + 256);
				if (tmp2 < 0)
					tmp2 = (short) (tmp2 + 256);
				tmp3 = (short) (tmp1 - tmp2);
				if (tmp3 < 0)
					tmp3 *= -1;
				if (tmp3 < subtraction_configs.threshold)
					res[i] = 0;
				else
					res[i] = (byte) 0xFF;
			}
			return res;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public int[] process(int[] imageData)
	{
		if (configs.enabled)
		{
			if (imageData.length == bg_image_gray.frame_data.length)
			{
				imageData = ImageManipulator.rgbIntArray2GrayIntArray(imageData);
				final int[] res = new int[imageData.length];
				int tmp = 0;
				for (int i = 0; i < imageData.length; i++)
				{
					tmp = imageData[i] - bg_image_gray.frame_data[i];

					if (tmp < 0)
						tmp *= -1;

					if (tmp < subtraction_configs.threshold)
						res[i] = 0;
					else
						res[i] = 0x00FFFFFF;
				}
				return res;
			}
			return null;
		}
		return imageData;
	}

	@Override
	public boolean initialize()
	{
		return true;
	}

}
