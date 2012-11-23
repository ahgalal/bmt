package sys.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Files {
	/**
	 * Deletes all files and subdirectories under dir. Author: exampledepot.com
	 * 
	 * @param dir
	 * @return true if all deletions were successful.
	 */
	public static boolean deleteDir(final File dir) {
		if (dir.isDirectory()) {
			final String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				final boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}
	
	public static Object fileToObject(final String filePath) {
		try {
			final ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream(new File(filePath)));
			return ois.readObject();
		} catch (final FileNotFoundException e) {
			System.err
					.println("Error loading Object: " + e.getMessage() + "\n");
		} catch (final IOException e) {
			System.err
					.println("Error loading Object: " + e.getMessage() + "\n");
		} catch (final ClassNotFoundException e) {
			System.err
					.println("Error loading Object: " + e.getMessage() + "\n");
		}
		return null;
	}
	
	public static boolean objectToFile(final Object o, final String filePath) {
		try {
			final ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(new File(filePath)));
			try {
				oos.writeObject(o);
				return true;
			} catch (final Exception e) {
				System.err.println("Error writing Object: " + e.getMessage()
						+ "\n");
			}

		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void writeStringToFile(final String str, final String filePath) {
		try {
			// Create file
			final FileWriter fstream = new FileWriter(filePath);
			final BufferedWriter out = new BufferedWriter(fstream);
			out.write(str);
			// Close the output stream
			out.close();
		} catch (final Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		try {
			if(file.exists())
				file.delete();
		} catch (Exception e) {
			System.err.println("Cannot delete file: "+fileName);
			e.printStackTrace();
		}
	}

	public static String convertPathToPlatformPath(String string) {
		return string.replace("/", File.separator);
	}
}
