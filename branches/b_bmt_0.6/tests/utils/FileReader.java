package utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReader {

	public static String read(final String fileName) {
		FileInputStream fis;
		BufferedInputStream bis;
		BufferedReader bufReader;
		final StringBuffer strBuf = new StringBuffer();
		try {
			fis = new FileInputStream(fileName);
			bis = new BufferedInputStream(fis);
			bufReader = new BufferedReader(new InputStreamReader(bis));

			while (bufReader.ready()) {
				strBuf.append(bufReader.readLine() + "\n");
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

}
