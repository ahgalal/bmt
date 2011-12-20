package utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReader
{

	public static String read(final String fileName)
	{
		FileInputStream fis;
		BufferedInputStream bis;
		BufferedReader buf_rdr;
		final StringBuffer strBuf = new StringBuffer();
		try
		{
			fis = new FileInputStream(fileName);
			bis = new BufferedInputStream(fis);
			buf_rdr = new BufferedReader(new InputStreamReader(bis));

			while (buf_rdr.ready())
			{
				strBuf.append(buf_rdr.readLine() + "\n");
			}
		} catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
		return strBuf.toString();
	}

}
