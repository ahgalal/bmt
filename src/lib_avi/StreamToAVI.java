package lib_avi;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import lib_avi.AVIOutputStream.VideoFormat;

/**
 * Video Stream to AVI Utility.
 * 
 * @author Creative
 */
public class StreamToAVI
{
	private AVIOutputStream avi_op;
	private BufferedImage image;
	private int[] data;
	private State state;

	/**
	 * Initialization.
	 * 
	 * @param filename
	 *            filename to save the video frames to
	 * @param format
	 *            video format
	 * @param framerate
	 *            frame rate of the video file
	 * @param width
	 *            video frame width
	 * @param height
	 *            video frame height
	 */
	public void initialize(
			final String filename,
			final VideoFormat format,
			final int framerate,
			final int width,
			final int height)
	{
		try
		{
			avi_op = new AVIOutputStream(new File(filename), format);
			avi_op.setFrameRate(framerate);
			avi_op.setVideoDimension(width, height);
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			data = ((DataBufferInt) (image.getRaster().getDataBuffer())).getData();
			state = State.INITIALIZED;
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Writes video frame to the video file.
	 * 
	 * @param frame_data
	 *            video frame image
	 */
	public void writeFrame(final int[] frame_data)
	{
		if (state == State.INITIALIZED)
		{
			System.arraycopy(frame_data, 0, data, 0, frame_data.length);
			try
			{
				avi_op.writeFrame(image);
			} catch (final IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Closes the AVI session.
	 */
	public void close()
	{
		try
		{
			avi_op.close();
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Specification of the state of the object.
	 * 
	 * @author Creative
	 */
	private enum State
	{
		INITIALIZED
	}

}
