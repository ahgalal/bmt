package utils.video.input;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import utils.video.ImageManipulator;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class JavaCVUtils {
	public static void updateImageData(int[] im, IplImage iplImage, int[] intBuffer) {
		ByteBuffer in = iplImage.getByteBuffer();
		IntBuffer asIntBuffer = in.asIntBuffer();
		if (intBuffer == null)
			intBuffer = new int[asIntBuffer.capacity()];
		asIntBuffer.get(intBuffer);
		ImageManipulator.intRGBBuf2IntRGB(intBuffer, im);			
	}
}
