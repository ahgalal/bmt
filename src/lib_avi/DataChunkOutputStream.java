/**
 * @(#)DataChunkOutputStream.java 1.0.1 2010-04-05 Copyright (c) 2008-2010
 *                                Werner Randelshofer Hausmatt 10, CH-6405
 *                                Immensee, Switzerland All rights reserved. The
 *                                copyright of this software is owned by Werner
 *                                Randelshofer. You may not use, copy or modify
 *                                this software, except in accordance with the
 *                                license agreement you entered into with Werner
 *                                Randelshofer. For details see accompanying
 *                                license terms.
 */
package lib_avi;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * This output stream filter supports common data types used inside of AVI RIFF
 * Data Chunks.
 * 
 * @author Werner Randelshofer
 * @version 1.0.1 2010-04-05 Removed unused constants. <br>
 *          1.0 2008-08-11 Created.
 */
public class DataChunkOutputStream extends FilterOutputStream
{

	/**
	 * The number of bytes written to the data output stream so far. If this
	 * counter overflows, it will be wrapped to Integer.MAX_VALUE.
	 */
	protected long written;

	public DataChunkOutputStream(final OutputStream out)
	{
		super(out);
	}

	/**
	 * Writes an Atom Type identifier (4 bytes).
	 * 
	 * @param s
	 *            A string with a length of 4 characters.
	 */
	public void writeType(final String s) throws IOException
	{
		if (s.length() != 4)
		{
			throw new IllegalArgumentException("type string must have 4 characters");
		}

		try
		{
			out.write(s.getBytes("ASCII"), 0, 4);
			incCount(4);
		} catch (final UnsupportedEncodingException e)
		{
			throw new InternalError(e.toString());
		}
	}

	/**
	 * Writes out a <code>byte</code> to the underlying output stream as a
	 * 1-byte value. If no exception is thrown, the counter <code>written</code>
	 * is incremented by <code>1</code>.
	 * 
	 * @param v
	 *            a <code>byte</code> value to be written.
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterOutputStream#out
	 */
	public final void writeByte(final int v) throws IOException
	{
		out.write(v);
		incCount(1);
	}

	/**
	 * Writes <code>len</code> bytes from the specified byte array starting at
	 * offset <code>off</code> to the underlying output stream. If no exception
	 * is thrown, the counter <code>written</code> is incremented by
	 * <code>len</code>.
	 * 
	 * @param b
	 *            the data.
	 * @param off
	 *            the start offset in the data.
	 * @param len
	 *            the number of bytes to write.
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterOutputStream#out
	 */
	@Override
	public synchronized void write(final byte b[], final int off, final int len)
			throws IOException
	{
		out.write(b, off, len);
		incCount(len);
	}

	/**
	 * Writes the specified byte (the low eight bits of the argument
	 * <code>b</code>) to the underlying output stream. If no exception is
	 * thrown, the counter <code>written</code> is incremented by <code>1</code>
	 * .
	 * <p>
	 * Implements the <code>write</code> method of <code>OutputStream</code>.
	 * 
	 * @param b
	 *            the <code>byte</code> to be written.
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterOutputStream#out
	 */
	@Override
	public synchronized void write(final int b) throws IOException
	{
		out.write(b);
		incCount(1);
	}

	/**
	 * Writes an <code>int</code> to the underlying output stream as four bytes,
	 * high byte first. If no exception is thrown, the counter
	 * <code>written</code> is incremented by <code>4</code>.
	 * 
	 * @param v
	 *            an <code>int</code> to be written.
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterOutputStream#out
	 */
	public void writeInt(final int v) throws IOException
	{
		out.write((v >>> 0) & 0xff);
		out.write((v >>> 8) & 0xff);
		out.write((v >>> 16) & 0xff);
		out.write((v >>> 24) & 0xff);
		incCount(4);
	}

	/**
	 * Writes an unsigned 32 bit integer value.
	 * 
	 * @param v
	 *            The value
	 * @throws java.io.IOException
	 */
	public void writeUInt(final long v) throws IOException
	{
		out.write((int) ((v >>> 0) & 0xff));
		out.write((int) ((v >>> 8) & 0xff));
		out.write((int) ((v >>> 16) & 0xff));
		out.write((int) ((v >>> 24) & 0xff));
		incCount(4);
	}

	/**
	 * Writes a signed 16 bit integer value.
	 * 
	 * @param v
	 *            The value
	 * @throws java.io.IOException
	 */
	public void writeShort(final int v) throws IOException
	{
		out.write(((v >>> 0) & 0xff));
		out.write(((v >> 8) & 0xff));
		incCount(2);
	}

	public void writeLong(final long v) throws IOException
	{
		out.write((int) (v >>> 0) & 0xff);
		out.write((int) (v >>> 8) & 0xff);
		out.write((int) (v >>> 16) & 0xff);
		out.write((int) (v >>> 24) & 0xff);
		out.write((int) (v >>> 32) & 0xff);
		out.write((int) (v >>> 40) & 0xff);
		out.write((int) (v >>> 48) & 0xff);
		out.write((int) (v >>> 56) & 0xff);
		incCount(8);
	}

	public void writeUShort(final int v) throws IOException
	{
		out.write(((v >>> 0) & 0xff));
		out.write(((v >> 8) & 0xff));
		incCount(2);
	}

	/**
	 * Increases the written counter by the specified value until it reaches
	 * Long.MAX_VALUE.
	 */
	protected void incCount(final int value)
	{
		long temp = written + value;
		if (temp < 0)
		{
			temp = Long.MAX_VALUE;
		}
		written = temp;
	}

	/**
	 * Returns the current value of the counter <code>written</code>, the number
	 * of bytes written to this data output stream so far. If the counter
	 * overflows, it will be wrapped to Integer.MAX_VALUE.
	 * 
	 * @return the value of the <code>written</code> field.
	 * @see java.io.DataOutputStream#written
	 */
	public final long size()
	{
		return written;
	}

	/**
	 * Sets the value of the counter <code>written</code> to 0.
	 */
	public void clearCount()
	{
		written = 0;
	}

}
