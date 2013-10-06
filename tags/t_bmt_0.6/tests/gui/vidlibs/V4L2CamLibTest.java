package gui.vidlibs;

import org.junit.Assume;

import utils.PManager;

public class V4L2CamLibTest extends TestCameraLibraryBase {
	@Override
	public void setUp() {
		super.setUp();
		vidLibName = "V4L2";
		Assume.assumeTrue(PManager.getOS().contains("inux"));
	}
}
