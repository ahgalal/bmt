/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package utils.video.input;

import javax.media.Buffer;
import javax.media.Effect;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;

import utils.video.FrameIntArray;
import utils.video.ImageManipulator;
import utils.video.input.VidInputter.SourceStatus;

/**
 * JMF Effect for grabbing frames from the webcam buffer.
 * 
 * @author Creative
 */
public class JMFGrabber implements Effect {
	private JMFCamFrame					currFrame	= null;
	private final FrameIntArray	fia;
	private final Format[]		informats;
	@SuppressWarnings("unused")
	private Format				informatt;
	private byte[]						nullData;
	private final Format[]		outformats;
	private Format				outformatt;
	private SourceStatus					status;
	private int							width, height;
	private boolean	paused;

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
	public JMFGrabber(final int width, final int height, final FrameIntArray fia) {
		this.fia = fia;
		informats = new Format[1];
		outformats = new Format[1];
		this.width = width;
		this.height = height;
		nullData = new byte[width * height * 3];
		for (int j = 0; j < nullData.length; j++)
			nullData[j] = (byte) 0xFF;
	}

	@Override
	public void close() {
	}

	@Override
	public Object getControl(final String arg0) {
		return null;
	}

	@Override
	public Object[] getControls() {
		return null;
	}

	@Override
	public String getName() {
		return "Analysis Effect";
	}

	/**
	 * Gets the status of the Grabber.
	 * 
	 * @return 1: ready
	 */
	public SourceStatus getStatus() {
		return status;
	}

	@Override
	public Format[] getSupportedInputFormats() {
		return informats;
	}

	@Override
	public Format[] getSupportedOutputFormats(final Format input) {
		return outformats;

		/*
		 * if(input==null) { return outformats; }
		 * if(matches(input,informats)!=null) { return new
		 * Format[]{outformats[0].intersects(input)}; } else { return new
		 * Format[0]; }
		 */
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
	Format matches(final Format in, final Format[] outs) {
		for (int i = 0; i < outs.length; i++)
			if (in.matches(outs[i]))
				return outs[i];

		return null;
	}

	@Override
	public void open() throws ResourceUnavailableException {

	}

	@Override
	public int process(final Buffer inbuf, final Buffer outbuf) { // 85ms
		// final long
		// tstart =
		// System.currentTimeMillis();
		try {
			final int outputDataLength = ((VideoFormat) outformatt)
					.getMaxDataLength();
			// validateByteArraySize(outbuf, outputDataLength);

			outbuf.setLength(outputDataLength);
			outbuf.setFormat(outformatt);
			outbuf.setFlags(inbuf.getFlags());
			outbuf.setData(nullData);
			// Initialize Current Frame
			if (currFrame == null)
				currFrame = new JMFCamFrame((byte[]) inbuf.getData(), width,
						height);
			else
				currFrame.updateBufferData((byte[]) inbuf.getData());
			if(paused==false){
				if (inbuf.getFormat().getClass() == YUVFormat.class) {
					final byte[] da = currFrame.convertYUV2RGB();
					fia.setFrameData(ImageManipulator.flipImage(
							ImageManipulator.byteRGB2IntRGB(da), width, height));
				} else
					fia.setFrameData(ImageManipulator.flipImage(
							currFrame.getRGBIntArray(), width, height));
				// System.out.print("Duration: "+(tstart) + "\n");
				status = SourceStatus.STREAMING;
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
		// long tend=System.currentTimeMillis();
		// System.out.print("Duration: "+(tend-tstart) + "\n");
		// System.out.print("AnalysisEffect | Time: "+tend+"\n");
		return 0;
	}

	@Override
	public void reset() {

	}
	
	public void pause(){
		paused=true;
		status=SourceStatus.PAUSED;
	}
	
	public void resume(){
		paused=false;
	}

	@Override
	public Format setInputFormat(final Format input) {
		informatt = input;
		informats[0] = input;
		return input;
	}

	@Override
	public Format setOutputFormat(final Format output) {
		outformatt = output;
		outformats[0] = output;
		return output;
	}

	/**
	 * Fills the byte array of the buffer to the required size.
	 * 
	 * @param buffer
	 *            buffer to be filled
	 * @param newSize
	 *            size to fill the buffer to
	 * @return validated byte array
	 */
	byte[] validateByteArraySize(final Buffer buffer, final int newSize) {
		final Object objectArray = buffer.getData();
		byte[] typedArray;

		if (objectArray instanceof byte[]) { // is correct type AND not null
			typedArray = (byte[]) objectArray;
			if (typedArray.length >= newSize)
				return typedArray;

			final byte[] tempArray = new byte[newSize]; // re-alloc array
			System.arraycopy(typedArray, 0, tempArray, 0, typedArray.length);
			typedArray = tempArray;
		} else
			typedArray = new byte[newSize];

		buffer.setData(typedArray);
		return typedArray;
	}
}
