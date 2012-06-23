package sys;

import gui.CamStartDefaultTest;
import gui.SetBackgroundTest;
import gui.VideoFileTest;

import java.security.Permission;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ CamStartDefaultTest.class, VideoFileTest.class,
		SetBackgroundTest.class })
public class GUITests {
	protected static class ExitException extends SecurityException {
		public final int	status;

		public ExitException(final int status) {
			super("There is no escape!");
			this.status = status;
		}
	}

	private static class NoExitSecurityManager extends SecurityManager {
		@Override
		public void checkExit(final int status) {
			// super.checkExit(status);
			throw new ExitException(status);
		}

		@Override
		public void checkPermission(final Permission perm) {
			// allow anything.
		}

		@Override
		public void checkPermission(final Permission perm, final Object context) {
			// allow anything.
		}
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// System.setSecurityManager(new NoExitSecurityManager());
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final Shell s = new Shell();
				s.setVisible(false);
			}
		});

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/*
	 * public static Test suite() { TestSuite suite = new
	 * TestSuite(GUITests.class.getName()); //$JUnit-BEGIN$
	 * //suite.addTest(TestSuite.createTest(CamStartDefaultTest.class, "cam"));
	 * //$JUnit-END$ return suite; }
	 */

}
