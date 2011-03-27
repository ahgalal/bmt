package utils.video;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageManipulator
{

	public static int[] gray2RGBInt(final byte[] grayarr)
	{
		final int[] rgbarr = new int[grayarr.length];
		int valgray;
		for (int i = 0; i < grayarr.length; i++)
		{
			valgray = grayarr[i] & 0xFF;
			rgbarr[i] = valgray | (valgray << 8) | (valgray << 16);
		}
		return rgbarr;
	}
	
	public static int[] flipImage(final int[] img, final int width, final int height)
	{
		final int[] tmp_img = new int[width * height];
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				tmp_img[y * width + x] = img[(height - y - 1) * width + x];
			}
		}
		return tmp_img;
	}
	
	public static int[] byteRGB2IntRGB(final byte[] barr)
	{
		final int[] iarr = new int[barr.length / 3];
		int r, g, b;
		for (int i = 0; i < barr.length; i += 3)
		{
			r = barr[i + 2] & 255;
			g = barr[i + 1] & 255;
			b = barr[i] & 255;
			iarr[i / 3] = b | (g << 8) | (r << 16);
		}
		return iarr;
	}
	

	public static byte[] rgbIntArray2GrayByteArray(final int[] in)
	{
		int r, g, b;
		final byte res[] = new byte[in.length];
		for (int i = 0; i < in.length; i++)
		{
			r = in[i] & (0x000000FF);
			g = (in[i] & (0x0000FF00)) >> 8;
			b = (in[i] & (0x00FF0000)) >> 16;
			res[i] = (byte) (0.2989 * r + 0.5870 * g + 0.1140 * b);
		}
		return res;
	}

	public static int[] rgbIntArray2GrayIntArray(final int[] in)
	{
		int r, g, b;
		final int res[] = new int[in.length];
		for (int i = 0; i < in.length; i++)
		{
			r = in[i] & (0x000000FF);
			g = (in[i] & (0x0000FF00)) >> 8;
			b = (in[i] & (0x00FF0000)) >> 16;
			res[i] = (int) (0.2989 * r + 0.5870 * g + 0.1140 * b);
		}
		return res;
	}

	public static BufferedImage loadImage(final String ref)
	{
		BufferedImage bimg = null;
		try
		{
			bimg = ImageIO.read(new File(ref));
		} catch (final Exception e)
		{
			e.printStackTrace();
		}
		return bimg;
	}

}
