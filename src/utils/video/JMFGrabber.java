package utils.video;

import javax.media.Buffer;
import javax.media.Effect;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;

import utils.video.input.JMFCamFrame;

/**
 * JMF Effect for grabbing frames from the webcam buffer.
 * 
 * @author Creative
 */
public class JMFGrabber implements Effect
{
	private final FrameIntArray fia;
	private final Format[] informats;
	private final Format[] outformats;
	@SuppressWarnings("unused")
	private Format informatt;
	private Format outformatt;
	JMFCamFrame curr_frame = null;
	int width, height;
	byte[] null_data;
	private int status;

	@Override
	public Format[] getSupportedInputFormats()
	{
		return informats;
	}

	/**
	 * Gets the status of the Grabber.
	 * 
	 * @return 1: ready
	 */
	public int getStatus()
	{
		return status;
	}

	@Override
	public Format[] getSupportedOutputFormats(final Format input)
	{
		return outformats;

		/*
		 * if(input==null) { return outformats; }
		 * if(matches(input,informats)!=null) { return new
		 * Format[]{outformats[0].intersects(input)}; } else { return new
		 * Format[0]; }
		 */
	}

	/**
	 * Fills the byte array of the buffer to the required size.
	 * 
	 * @param buffer
	 *            buffer to be filled
	 * @param newSize
	 *            size to fill the buffer to
	 * @return
	 */
	byte[] validateByteArraySize(final Buffer buffer, final int newSize)
	{
		final Object objectArray = buffer.getData();
		byte[] typedArray;

		if (objectArray instanceof byte[])
		{ // is correct type AND not null
			typedArray = (byte[]) objectArray;
			if (typedArray.length >= newSize)
			{ // is sufficient capacity
				return typedArray;
			}

			final byte[] tempArray = new byte[newSize]; // re-alloc array
			System.arraycopy(typedArray, 0, tempArray, 0, typedArray.length);
			typedArray = tempArray;
		} else
		{
			typedArray = new byte[newSize];
		}

		buffer.setData(typedArray);
		return typedArray;
	}

	@Override
	public int process(final Buffer inbuf, final Buffer outbuf)
	{ // 85ms
		// final long tstart = System.currentTimeMillis();
		try
		{
			final int outputDataLength = ((VideoFormat) outformatt).getMaxDataLength();
			// validateByteArraySize(outbuf, outputDataLength);

			outbuf.setLength(outputDataLength);
			outbuf.setFormat(outformatt);
			outbuf.setFlags(inbuf.getFlags());
			outbuf.setData(null_data);
			// Initialize Current Frame
			if (curr_frame == null)
				curr_frame = new JMFCamFrame((byte[]) inbuf.getData(), width, height);
			else
				curr_frame.updateBufferData((byte[]) inbuf.getData());
			if (inbuf.getFormat().getClass() == YUVFormat.class)
			{
				final byte[] da = curr_frame.convertYUV2RGB(null);
				fia.frame_data = ImageManipulator.flipImage(
						ImageManipulator.byteRGB2IntRGB(da),
						width,
						height);
			} else
			{
				fia.frame_data = ImageManipulator.flipImage(
						curr_frame.getRGBIntArray(),
						width,
						height);
			}
			// System.out.print("Duration: "+(tstart) + "\n");
			status = 1;

		} catch (final Exception e)
		{
			e.printStackTrace();
		}
		// long tend=System.currentTimeMillis();
		// System.out.print("Duration: "+(tend-tstart) + "\n");
		// System.out.print("AnalysisEffect | Time: "+tend+"\n");
		return 0;
	}

	/**
	 * Initializes the Grabber.
	 * 
	 * @param width
	 *            image's width
	 * @param height
	 *            image's height
	 * @param fia
	 *            Frame data container
	 */
	public JMFGrabber(final int width, final int height, final FrameIntArray fia)
	{
		this.fia = fia;
		informats = new Format[1];
		outformats = new Format[1];
		this.width = width;
		this.height = height;
		null_data = new byte[width * height * 3];
		for (int j = 0; j < null_data.length; j++)
			null_data[j] = (byte) 0xFF;
	}

	/**
	 * Matches two JMF formats.
	 * 
	 * @param in
	 *            first format
	 * @param outs
	 *            second format
	 * @return if the two formats are matched, one of them is returned, else
	 *         returns null
	 */
	Format matches(final Format in, final Format outs[])
	{
		for (int i = 0; i < outs.length; i++)
		{
			if (in.matches(outs[i]))
				return outs[i];
		}

		return null;
	}

	@Override
	public Format setInputFormat(final Format input)
	{
		informatt = input;
		informats[0] = input;
		return input;
	}

	@Override
	public Format setOutputFormat(final Format output)
	{
		outformatt = output;
		outformats[0] = output;
		return output;
	}

	@Override
	public void close()
	{
	}

	@Override
	public String getName()
	{
		return "Analysis Effect";
	}

	@Override
	public void open() throws ResourceUnavailableException
	{

	}

	@Override
	public void reset()
	{

	}

	@Override
	public Object getControl(final String arg0)
	{
		return null;
	}

	@Override
	public Object[] getControls()
	{
		return null;
	}
}
