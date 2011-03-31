package utils.video;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Collection of static methods to handle images and convert between different
 * representations (int rgb, byte rgb, int grayscale ..).
 * 
 * @author Creative
 */
public class ImageManipulator
{

	/**
	 * Converts an int[] RGB image to byte[] RGB image.
	 * 
	 * @param int_arr
	 *            input image
	 * @return converted image as an array of bytes
	 */
	public static byte[] intRGB2ByteRGB(final int[] int_arr)
	{ // 4ms

		final byte[] res = new byte[int_arr.length * 3];
		for (int i = 0; i < res.length; i += 3)
		{
			res[i] = (byte) ((byte) int_arr[i / 3] & (255)); // B
			res[i + 1] = (byte) ((int_arr[i / 3] >> 8) & (255)); // G
			res[i + 2] = (byte) ((byte) (int_arr[i / 3] >> 16) & (255)); // R

		}

		return res;
	}

	/**
	 * Converts a byte[] Grayscale image to a byte[] RGB image. notice that the
	 * size of the output RGB image is triple that of the input Grayscale image.
	 * 
	 * @param gray_arr
	 *            input grayscale byte array
	 * @return RGB byte array image
	 */
	public byte[] grayByteArray2RGBByteArray(final byte[] gray_arr)
	{
		final byte[] rgbarr = new byte[gray_arr.length * 3];
		int valgray;
		for (int i = 0; i < gray_arr.length * 3; i += 3)
		{
			valgray = gray_arr[i / 3];
			rgbarr[i] = rgbarr[i + 1] = rgbarr[i + 2] = (byte) valgray;
		}
		return rgbarr;
	}

	/**
	 * Converts single dimensional integer array image to a two dimensional
	 * integer array image.
	 * 
	 * @param arr
	 *            input single dimensional array
	 * @param width
	 *            image's width
	 * @param height
	 *            image's height
	 * @return two dimensional integer array image
	 */
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

	/**
	 * Gets the Grayscale value for an RGB value.
	 * 
	 * @param r
	 *            Red
	 * @param g
	 *            Green
	 * @param b
	 *            Blue
	 * @return Grayscale value
	 */
	public static byte rgb2GrayValue(final int r, final int g, final int b)
	{
		return (byte) (0.2989 * r + 0.5870 * g + 0.1140 * b);
	}

	/**
	 * Converts a byte[] Grayscale image to int[] RGB image (representation
	 * changes, but the image remains Gray colors).
	 * 
	 * @param gray_arr
	 *            input Grayscale image
	 * @return integer array of the RGB image
	 */
	public static int[] gray2RGBInt(final byte[] gray_arr)
	{
		final int[] rgbarr = new int[gray_arr.length];
		int valgray;
		for (int i = 0; i < gray_arr.length; i++)
		{
			valgray = gray_arr[i] & 0xFF;
			rgbarr[i] = valgray | (valgray << 8) | (valgray << 16);
		}
		return rgbarr;
	}

	/**
	 * Flips the input image vertically.
	 * 
	 * @param img
	 *            input image
	 * @param width
	 *            image's width
	 * @param height
	 *            image's height
	 * @return flipped int[] image
	 */
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

	/**
	 * Converts a byte[] RGB image to int[] RGB image.
	 * 
	 * @param byte_arr
	 *            input byte[] RGB image
	 * @return integer array RGB image
	 */
	public static int[] byteRGB2IntRGB(final byte[] byte_arr)
	{
		final int[] iarr = new int[byte_arr.length / 3];
		int r, g, b;
		for (int i = 0; i < byte_arr.length; i += 3)
		{
			r = byte_arr[i + 2] & 255;
			g = byte_arr[i + 1] & 255;
			b = byte_arr[i] & 255;
			iarr[i / 3] = b | (g << 8) | (r << 16);
		}
		return iarr;
	}

	/**
	 * Converts single dimensional byte array image to a two dimensional byte
	 * array image.
	 * 
	 * @param arr
	 *            input single dimensional array
	 * @param width
	 *            image's width
	 * @param height
	 *            image's height
	 * @return two dimensional byte array image
	 */
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

	/**
	 * Converts an int[] RGB image to byte[] Grayscale image.
	 * 
	 * @param in
	 *            input RGB image as array of integers
	 * @return Grayscale image as an array of bytes
	 */
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

	/**
	 * Converts an RGB byte[] image to Grayscale byte[] image.
	 * 
	 * @param rgb_byte_arr
	 *            RGB image byte array
	 * @return Grayscale image as an array of bytes
	 */
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

	/**
	 * Converts an RGB int[] image to Grayscale int[] image.
	 * 
	 * @param in
	 *            RGB image integer array
	 * @return Grayscale image integer array
	 */
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

	/**
	 * Loads an image file into a buffered image.
	 * 
	 * @param file_path
	 *            path to the image file
	 * @return BufferedImage containing the loaded image's data
	 */
	public static BufferedImage loadImage(final String file_path)
	{
		BufferedImage bimg = null;
		try
		{
			bimg = ImageIO.read(new File(file_path));
		} catch (final Exception e)
		{
			e.printStackTrace();
		}
		return bimg;
	}

}
