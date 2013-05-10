package gui.vidlibs;

import java.io.File;

import sys.utils.EnvVar;
import sys.utils.Files;

public class JMyronLibTest extends TestCameraLibraryBase {

	private final String[]	nativeLibsFileRelativePaths	= new String[] {
			"libs/JMyron/DSVL.dll", "libs/JMyron/JMyron.dll",
			"libs/JMyron/myron_ezcam.dll"				};

	@Override
	public void setUp() {
		// copy JMyron native libs
		final String projectRoot = Files.convertPathToPlatformPath(EnvVar
				.getEnvVariableValue("BMT_WS")) + "/BMT";
		for (final String fileRelativePath : nativeLibsFileRelativePaths) {
			final File file = new File(projectRoot + "/" + fileRelativePath);
			Files.copy(file.getAbsolutePath(),
					projectRoot + "/" + file.getName());
		}

		super.setUp();
		vidLibName = "JMyron";
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		final String projectRoot = Files.convertPathToPlatformPath(EnvVar
				.getEnvVariableValue("BMT_WS")) + "/BMT";
		for (final String fileRelativePath : nativeLibsFileRelativePaths) {
			final File fileSrc = new File(projectRoot + "/" + fileRelativePath);
			final File fileDst = new File(projectRoot + "/" + fileSrc.getName());
			fileDst.deleteOnExit();
		}

	}
}
