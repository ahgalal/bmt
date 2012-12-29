package gui.integration;

import gui.executionunit.ExperimentExecUnitGroup;
import gui.executionunit.VideoExecUnitGroup;
import gui.utils.Reflections;
import gui.utils.UITest;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CancellationException;

import modules.experiment.Constants;
import modules.experiment.Experiment;
import modules.experiment.Group;
import modules.experiment.Rat;

import org.apache.tools.ant.util.FileUtils;

import utils.PManager;
import utils.Utils;

import com.windowtester.runtime.WidgetSearchException;

public class ExperimentIntegrationTestBase extends UITest {
	protected String	expFileName;
	protected String	expFileName_orig;
	protected String	groupName		= "Group1";
	protected int		ratNumber		= 1;
	protected int		sessionTimeMax	= 25;
	protected int		sessionTimeMin	= 23;
	protected int		sleepTime1		= 30;
	protected int		sleepTime2		= 0;
	protected int		sleepTime3		= 0;
	protected boolean	terminateTest	= false;
	protected String	videoFile;

	protected void afterExperimentLoad() throws Exception {
		/************* load video file ********/
		preLoadVideoFile();
		VideoExecUnitGroup.startStreamVideo(videoFile);

		afterVideoLoad();
	}
	
	protected void startTracking() throws WidgetSearchException{
		ExperimentExecUnitGroup.startTracking(Integer.toString(ratNumber));
	}

	protected void afterStartTracking() throws WidgetSearchException {
		/************* sleep ********/
		sleepings();

		/************* checking ********/
		preChecking();
		checks();
	}

	protected void afterVideoLoad() throws Exception {
		/************* pre-tracking ********/
		preStartTracking();
		
		/************* tracking ********/
		startTracking();
		
		/************* post-tracking ********/
		afterStartTracking();
	}

	protected void checkParamValue(final Rat rat, final String param,
			final int valueMin, final int valueMax) {
		final String actualStrValue = rat.getValueByParameterName(param);
		final double actualDblValue = Double.parseDouble(actualStrValue);
		assert ((actualDblValue <= valueMax) && (actualDblValue >= valueMin)) : "Unexpected param value for: "
				+ param
				+ " expected valueMin/Max: "
				+ valueMin
				+ "/"
				+ valueMax + " ,actual value: " + actualStrValue;
	}

	protected void checkParamValue(final Rat rat, final String param,
			final String value) {
		final String actualValue = rat.getValueByParameterName(param);
		assert (actualValue.equals(value)) : "Unexpected param value for: "
				+ param + " expected value: " + value + " ,actual value: "
				+ actualValue;
	}

	/**
	 * Checks recorded rat parameters.
	 */
	protected void checks() {
		final Experiment exp = Reflections.getLoadedExperiment();

		final Group grp = exp.getGroupByName(groupName);
		final Rat rat0 = grp.getRatByNumber(ratNumber);

		checkParamValue(rat0, Constants.FILE_RAT_NUMBER, ratNumber, ratNumber);
		checkParamValue(rat0, Constants.FILE_GROUP_NAME, groupName);
		checkParamValue(rat0, Constants.FILE_SESSION_TIME, sessionTimeMin,
				sessionTimeMax);
		PManager.log.print("Experiment info check: OK", this);
	}

	protected void copyExperimentFile() {
		try {
			// make a copy of the original file
			FileUtils.getFileUtils().copyFile(new File(expFileName_orig),
					new File(expFileName));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	protected void fullScenario() throws WidgetSearchException, Exception,
			InterruptedException {
		/************* load experiment ********/
		preLoadExperiment();
		ExperimentExecUnitGroup.loadExperiment(expFileName);

		afterExperimentLoad();
	}

	/**
	 * Executed before checking experiment's params' values.
	 * 
	 * @throws WidgetSearchException
	 */
	protected void preChecking() throws WidgetSearchException {
	}

	/**
	 * Executed before loading an experiment.
	 * 
	 * @throws WidgetSearchException
	 */
	protected void preLoadExperiment() throws WidgetSearchException {
	}

	/**
	 * Executed before loading video file.
	 * 
	 * @throws WidgetSearchException
	 */
	protected void preLoadVideoFile() throws WidgetSearchException {
	}

	protected void preSleep1() throws WidgetSearchException {
	}

	protected void preSleep2() throws WidgetSearchException {
	}

	protected void preSleep3() throws WidgetSearchException {
	}

	/**
	 * Executed before starting tracking.
	 * 
	 * @throws WidgetSearchException
	 * @throws Exception
	 */
	protected void preStartTracking() throws Exception {
	}

	protected void sleepings() throws WidgetSearchException {
		preSleep1();
		Utils.sleep(sleepTime1 * 1000);
		preSleep2();
		Utils.sleep(sleepTime2 * 1000);
		preSleep3();
		Utils.sleep(sleepTime3 * 1000);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		sys.utils.Files.deleteFile(expFileName);
	}

	public void testIntegration(){
		try {
			fullScenario();
		} catch (final CancellationException e) {
			PManager.log.print("Test is explicitly terminated", this);
		} catch (final WidgetSearchException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
