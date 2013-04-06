package gui.experiment;

import gui.executionunit.ExperimentExecUnitGroup;
import gui.utils.UITest;

import java.io.File;

import com.windowtester.runtime.WidgetSearchException;

import sys.utils.EnvVar;
import sys.utils.Files;
import utils.PManager;

/**
 * Tests exporting the loaded experiment to an excel sheet.</br> Check is based
 * on the exported xlsx file size.
 * 
 * @author Creative
 */
public class ExportExperimentToExcelTest extends UITest {

	private final String	expExcelFileName	= Files.convertPathToPlatformPath(EnvVar
														.getEnvVariableValue("BMT_WS")
														+ "/testExcelExport.xlsx");
	private final String	expFileNameOF			= Files.convertPathToPlatformPath(EnvVar
														.getEnvVariableValue("BMT_WS")
														+ "/BMT/ants/test/resources/TestOpenField.bmt");
	private final String	expFileNameFS			= Files.convertPathToPlatformPath(EnvVar
			.getEnvVariableValue("BMT_WS")
			+ "/BMT/ants/test/resources/TestForcedSwimming.bmt");

	private void checkExperimentExported(int minSize) {
		final File exportedXlsxFile = new File(expExcelFileName);

		// check file size, TODO: look for a better check than this (maybe read
		// xlsx file contents and check them)
		assert (exportedXlsxFile.length() > minSize) : "file size is less than expected: "+exportedXlsxFile.length()+ " expected size: "+minSize;
		PManager.log.print("File size check ... OK", this);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		sys.utils.Files.deleteFile(expExcelFileName);
	}

	public void testExportExperimentToExcelOF() throws Exception {
		actualTest(expFileNameOF);
		checkExperimentExported(3700);
		
		actualTest(expFileNameFS);
		checkExperimentExported(3700);
	}
	
	private void actualTest(String expFileName) throws Exception, WidgetSearchException {
		// load experiment
		ExperimentExecUnitGroup.loadExperiment(expFileName);
		// export to excel
		final String filePath = expExcelFileName;
		ExperimentExecUnitGroup.exportToExcel(filePath);
		
	}

}