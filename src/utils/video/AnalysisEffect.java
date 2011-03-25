package utils.video;

import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.media.Buffer;
import javax.media.Effect;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;

import utils.video.input.JMFCamFrame;

public class AnalysisEffect implements Effect
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
		//final long tstart = System.currentTimeMillis();
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
				fia.frame_data = byteArr2RGBIntArr(da);
			} else
			{
				fia.frame_data = curr_frame.getRGBIntArray();
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

	public byte[] grayByteArray2RGBByteArray(final byte[] grayarr) // 3ms
	{
		// long tstart=System.currentTimeMillis();
		final byte[] rgbarr = new byte[grayarr.length * 3];
		int valgray;
		for (int i = 0; i < grayarr.length * 3; i += 3)
		{
			valgray = grayarr[i / 3];
			rgbarr[i] = rgbarr[i + 1] = rgbarr[i + 2] = (byte) valgray;
		}
		// long tend=System.currentTimeMillis();
		// System.out.print("Duration: "+(tend-tstart) + "\n");
		return rgbarr;
	}

	public AnalysisEffect(final int width, final int height, final FrameIntArray fia)
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

	public int[] byteArr2RGBIntArr(final byte[] barr)
	{
		final int[] iarr = new int[barr.length / 3];
		int r, g, b;
		for (int i = 0; i < barr.length; i += 3)
		{
			r = barr[i + 2];
			g = barr[i + 1];
			b = barr[i];
			iarr[i / 3] = (b & 0xff) | ((g & 0xff) << 8) | ((r & 0xff) << 16);
		}
		return iarr;
	}

	/*
	 * public void setBackGroundImage(Buffer buf) { if(bg_frame==null) bg_frame
	 * = new JMFCamFrame(JMFCamFrame.intRGB2ByteRGB( (int[])
	 * buf.getData()),width,height); else
	 * bg_frame.updateBufferData(JMFCamFrame.intRGB2ByteRGB( (int[])
	 * buf.getData())); } public void setBackGroundImage() { byte[] data = new
	 * byte[width*height*3]; System.arraycopy(curr_frame.getRGBByteArray(), 0,
	 * data, 0, curr_frame.getRGBByteArray().length); if(bg_frame==null)
	 * bg_frame = new JMFCamFrame(data,width,height); else
	 * bg_frame.updateBufferData(data); }
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

		/*
		 * //outformats[0]=outformatt = output; //return outformatt; if (output
		 * == null || matches(output, outformats) == null) return null;
		 * RGBFormat incoming = (RGBFormat) output; Dimension size =
		 * incoming.getSize(); int maxDataLength = incoming.getMaxDataLength();
		 * int lineStride = incoming.getLineStride(); float frameRate =
		 * incoming.getFrameRate(); int flipped = incoming.getFlipped(); int
		 * endian = incoming.getEndian(); if (size == null) return null; if
		 * (maxDataLength < size.width * size.height * 3) maxDataLength =
		 * size.width * size.height * 3; if (lineStride < size.width * 3)
		 * lineStride = size.width * 3; if (flipped != Format.FALSE) flipped =
		 * Format.FALSE; outformatt = outformats[0].intersects(new
		 * RGBFormat(size, maxDataLength, null, frameRate, Format.NOT_SPECIFIED,
		 * Format.NOT_SPECIFIED, Format.NOT_SPECIFIED, Format.NOT_SPECIFIED,
		 * Format.NOT_SPECIFIED, lineStride, Format.NOT_SPECIFIED,
		 * Format.NOT_SPECIFIED)); //System.out.println("final outputformat = "
		 * + outputFormat); return outformatt;
		 */

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

	/*
	 * private void reduceNoise(JMFCamFrame frame,int mask_length) { byte[]
	 * newdata =new byte[width*height*3]; byte[] olddata =
	 * frame.getRGBByteArray(); int sum=0; for(int
	 * x=0+mask_length/2;x<width-mask_length/2;x++) { for(int
	 * y=0+mask_length/2;y<height-mask_length/2;y++) { for(int c=0;c<3;c++)
	 * //color loop { for(int m =-mask_length/2;m<=mask_length/2;m++) { for(int
	 * n =-mask_length/2;n<=mask_length/2;n++) { sum+= (
	 * olddata[((y+m)*width+(x+n))*3 +c] ); } } newdata[((y)*width+x)*3
	 * +c]=(byte) (sum/(mask_length*mask_length)); sum=0; } } }
	 * frame.getBuffer().setData(newdata); }
	 */

	/*
	 * private BufferedImage buf2BufferedImage(Buffer bufin) { BufferedImage
	 * bufimg = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	 * //bufimg.setRGB(0, 0, width,height, byteArr2RGBIntArr((byte[])
	 * bufin.getData()), 0, width); BufferToImage b2i = new
	 * BufferToImage((VideoFormat)bufin.getFormat());
	 * bufimg.getGraphics().drawImage(b2i.createImage(bufin), 0,
	 * 0,width,height,0,height,width,0, null) ; return bufimg; }
	 */

}
