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

package modules.experiment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import utils.PManager;
import utils.StatusManager.StatusSeverity;

/**
 * Responsible for saving/loading Experiment's data to/from Text files.
 * 
 * @author Creative
 */
public class TextEngine {
	private BufferedInputStream	bis	= null;
	private FileInputStream		fis	= null;

	private FileOutputStream	out;
	private PrintStream			p;

	/**
	 * Loads Experiment's data from a Text file to an Experiment object.
	 * 
	 * @param fileName
	 *            file path to the text file containing the experiment's
	 *            information
	 * @param exp
	 *            Experiment object to load the information to
	 * @return true: success, false: failure
	 */
	public boolean readExpInfoFromTXTFile(final String fileName,
			final Experiment exp) {
		try {
			String tmpLine = null;
			String[] lineData;
			fis = new FileInputStream(fileName);
			bis = new BufferedInputStream(fis);

			final BufferedReader bufReader = new BufferedReader(
					new InputStreamReader(bis));
			boolean groupRatsLoaded = false;
			Group grpTmp = null;
			while (bufReader.ready()) {
				if (!groupRatsLoaded)
					tmpLine = bufReader.readLine();
				groupRatsLoaded = false;
				if (tmpLine.equals(Constants.H_EXP)) { // load exp. info
					final String tmpName = bufReader.readLine().substring(
							Constants.H_EXP_NAME.length());
					final String tmpType = bufReader.readLine().substring(
							Constants.H_EXP_TYPES.length());
					final String tmpUser = bufReader.readLine().substring(
							Constants.H_EXP_USER.length());
					final String tmpDate = bufReader.readLine().substring(
							Constants.H_EXP_DATE.length());
					final String tmpNotes = bufReader.readLine().substring(
							Constants.H_EXP_NOTES.length());
					// TODO: Minor Bug: if the notes or any string of the above
					// is
					// multi-line, the second line and all next lines will not
					// be
					// loaded!!
					// as a fix: think of Object Serialization/Deserialization.
					exp.setExperimentInfo(tmpName, tmpUser, tmpDate,
							tmpNotes, tmpType);
				} else if (tmpLine.equals(Constants.H_GRP)) { // load grp. info
					final int tmpId = Integer.parseInt(bufReader.readLine()
							.substring(Constants.H_GRP_ID.length()));
					final String tmpName = bufReader.readLine().substring(
							Constants.H_GRP_NAME.length());
					Integer.parseInt(bufReader.readLine().substring(
							Constants.H_GRP_NO_RATS.length()));
					/*
					 * Just a holder to skip the Rats' numbers line, we can know
					 * their numbers later (no need to read, parse etc..)
					 */
					bufReader.readLine();
					final String tmpNotes = bufReader.readLine().substring(
							Constants.H_GRP_NOTES.length());
					grpTmp = new Group(tmpId, tmpName, tmpNotes);
					exp.addGroup(grpTmp);
				} else if (tmpLine.equals(Constants.H_RAT)) {
					tmpLine = bufReader.readLine();
					lineData = readLineData(tmpLine);
					exp.setParametersList(lineData);
					while (bufReader.ready())
						if (!(tmpLine = bufReader.readLine()).substring(0, 1)
								.equals("[")) {
							lineData = readLineData(tmpLine);
							for (int i = 0; i < lineData.length; i++)
								lineData[i] = lineData[i].trim();
							final Rat tmpRat = new Rat(
									exp.getExpParametersList(), lineData);
							grpTmp.addRat(tmpRat);
						} else
							break;
					groupRatsLoaded = true;
				}
			}
			fis.close();
			bis.close();
			return true;
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			PManager.log.print(
					"Error Loading Experiment from file! (File is not found)",
					this, StatusSeverity.ERROR);
			return false;
		} catch (final IOException e) {
			e.printStackTrace();
			PManager.log.print(
					"Error Loading Experiment from file! (File read error)",
					this, StatusSeverity.ERROR);
			return false;
		}
	}

	/**
	 * Parses a line of data into an array of Strings (split char is \t).
	 * 
	 * @param line
	 *            line to parse
	 * @return array of Strings containing line data after splitting
	 */
	private String[] readLineData(final String line) {
		final String[] res = line.split("\t");
		return res;
	}

	/**
	 * Saves the Experiment's information to a Text file.
	 * 
	 * @param filename
	 *            file path to save the experiment information to
	 * @param exp
	 *            the Experiment to save its information
	 */
	public void writeExpInfoToTXTFile(final String filename,
			final Experiment exp) {
		try {
			out = new FileOutputStream(filename);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		p = new PrintStream(out);
		try {
			p.print(exp.expInfo2String());
		} catch (final Exception e) {
			e.printStackTrace();
		}
		p.close();

	}

}
