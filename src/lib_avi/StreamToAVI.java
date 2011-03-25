package lib_avi;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import lib_avi.AVIOutputStream.VideoFormat;

public class StreamToAVI
{

	private AVIOutputStream avi_op;
	private BufferedImage image;
	private int[] data;
	private State state;

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

	private enum State
	{
		INITIALIZED
	}

}
