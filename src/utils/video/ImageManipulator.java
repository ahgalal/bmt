package utils.video;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageManipulator {

	public static int[] gray2RGBInt(byte[] grayarr)
	{
		int[] rgbarr = new int[grayarr.length];
		int valgray;
		for(int i =0;i<grayarr.length;i++)
		{
			valgray = grayarr[i];
			rgbarr[i] = valgray | (valgray<<8)|(valgray<<16); 
		}
		return rgbarr;
	}

	public static byte[] rgbIntArray2GrayByteArray(int[] in)
	{
		int r,g,b;
		byte res[] = new byte[in.length];
		for(int i=0;i<in.length;i++)
		{
			r=in[i] & (0x000000FF);
			g=(in[i] & (0x0000FF00))>>8;
		b=(in[i] & (0x00FF0000))>>16;
			res[i] = (byte) (0.2989*r + 0.5870*g+ 0.1140*b);
		}
		return res;
	}

	public static int[] rgbIntArray2GrayIntArray(int[] in)
	{
		int r,g,b;
		int res[] = new int[in.length];
		for(int i=0;i<in.length;i++)
		{
			r=in[i] & (0x000000FF);
			g=(in[i] & (0x0000FF00))>>8;
		b=(in[i] & (0x00FF0000))>>16;
		res[i] = (int) (0.2989*r + 0.5870*g+ 0.1140*b);
		}
		return res;
	}

	public static BufferedImage loadImage(String ref) {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(ref));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bimg;
	}

}
