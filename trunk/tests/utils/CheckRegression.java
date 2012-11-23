package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class CheckRegression extends Task {

	private String	logsDirPath;

	private boolean checkPass(final File file) {

		try {
			final FileInputStream fstream = new FileInputStream(file);
			final DataInputStream in = new DataInputStream(fstream);
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (strLine.contains("<testsuite errors=\"0\" failures=\"0\"")) {
					in.close();
					return true;
				}
			}
			in.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void checkRegression() {

		try {
			final File regressDir = new File(logsDirPath);
			final ArrayList<String> resPass = new ArrayList<String>();
			final ArrayList<String> resFail = new ArrayList<String>();
			final ArrayList<String> toBePrinted = new ArrayList<String>();
			for (final File f : regressDir.listFiles()) {
				if (f.getAbsolutePath().contains(".xml"))
					if (checkPass(f))
						resPass.add(removeFilePrefix(f.getName()));
					else
						resFail.add(removeFilePrefix(f.getName()));
			}

			final FileWriter fstream = new FileWriter(logsDirPath
					+ File.separatorChar + "regress.log");
			final BufferedWriter out = new BufferedWriter(fstream);

			toBePrinted.add("v------------ Passes -------------v");
			for (final String s : resPass) {
				toBePrinted.add(s + ":\t\t Pass");
			}
			if (resFail.size() > 0)
				toBePrinted.add("v------------ Failures -------------v");
			for (final String s : resFail) {
				toBePrinted.add(s + ":\t\t Fail");
			}

			final String res = "Regression: Pass: " + resPass.size()
					+ " , Fail: " + resFail.size();
			toBePrinted.add(res);

			for (final String string : toBePrinted) {
				System.out.println(string);
				out.write(string);
				out.newLine();
			}

			out.close();
			final String html = "<HTML><BODY>" + getHTMLTable(resPass, resFail)
					+ "</BODY></HTML>";

			final FileWriter fstreamHTML = new FileWriter(logsDirPath
					+ File.separatorChar + "regress.html");

			fstreamHTML.write(html);
			fstreamHTML.close();

		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	// called to execute the task
	@Override
	public void execute() throws BuildException {
		checkRegression();
	}

	private String getHTMLTable(final ArrayList<String> resPass,
			final ArrayList<String> resFail) {
		final StringBuilder strBuilder = new StringBuilder();

		strBuilder.append("<table border=\"1\">");
		for (final String pass : resPass) {
			strBuilder.append("<tr>");
			strBuilder.append("<td>" + pass + "</td>");
			strBuilder.append("<td bgcolor=\"#00FF00\">PASS</td>");
			strBuilder.append("</tr>");
		}
		for (final String fail : resFail) {
			strBuilder.append("<tr>");
			strBuilder.append("<td>" + fail + "</td>");
			strBuilder.append("<td bgcolor=\"#FF0000\">PASS</td>");
			strBuilder.append("</tr>");
		}
		return strBuilder.toString();
	}

	// called to initialize the task
	@Override
	public void init() throws BuildException {
	}

	private String removeFilePrefix(final String fileName) {
		return fileName.substring(fileName.indexOf('-') + 1);
	}

	/**
	 * @param logsDirPath
	 *            the logsDirPath to set
	 */
	public void setLogsDirPath(final String logsDirPath) {
		this.logsDirPath = logsDirPath;
	}
}