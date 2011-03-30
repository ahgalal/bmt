package utils.video;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageManipulator
{

	public static byte[] intRGB2ByteRGB(final int[] dImg_1DimInt)
	{ // 4ms

		final byte[] res = new byte[dImg_1DimInt.length * 3];
		for (int i = 0; i < res.length; i += 3)
		{
			res[i] = (byte) ((byte) dImg_1DimInt[i / 3] & (255)); // B
			res[i + 1] = (byte) ((dImg_1DimInt[i / 3] >> 8) & (255)); // G
			res[i + 2] = (byte) ((byte) (dImg_1DimInt[i / 3] >> 16) & (255)); // R

		}

		return res;
	}
	
	public byte[] grayByteArray2RGBByteArray(final byte[] grayarr)
	{
		final byte[] rgbarr = new byte[grayarr.length * 3];
		int valgray;
		for (int i = 0; i < grayarr.length * 3; i += 3)
		{
			valgray = grayarr[i / 3];
			rgbarr[i] = rgbarr[i + 1] = rgbarr[i + 2] = (byte) valgray;
		}
		return rgbarr;
	}

	public static int[][] intArrayTo2DIntArray(
			final int[] arr,
			final int width,
			final int height)
	{
		final int[][] res = new int[width][height];
		for (int i = 0; i < arr.length; i++)
		{
			res[i % width][i / width] = (byte) arr[i];
		}
		return res;
	}

	public static byte rgb2GrayValue(final int r, final int g, final int b)
	{
		return (byte) (0.2989 * r + 0.5870 * g + 0.1140 * b);
	}

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

	public static byte[][] singleDim2DoubleDim(
			final byte[] imgin,
			final int width,
			final int height)
	{
		final byte[][] res = new byte[width][height];
		for (int i = 0; i < imgin.length; i++)
		{
			res[i % width][i / width] = imgin[i];
		}
		return res;
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

	public static byte[] rgbByteArray2GrayByteArray(final byte[] rgb_byte_arr)
	{
		final byte[] gray_arr = new byte[rgb_byte_arr.length / 3];
		int r, g, b;
		for (int i = 0; i < rgb_byte_arr.length; i += 3)
		{
			r = rgb_byte_arr[i + 2] & 0xff;
			g = rgb_byte_arr[i + 1] & 0xff;
			b = rgb_byte_arr[i] & 0xff;
			gray_arr[i / 3] = (byte) (0.2989 * r + 0.5870 * g + 0.1140 * b);
		}
		return gray_arr;
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
