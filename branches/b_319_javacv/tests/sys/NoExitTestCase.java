package sys;

import java.security.Permission;

import junit.framework.TestCase;

public class NoExitTestCase extends TestCase {

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
			super.checkExit(status);
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

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setSecurityManager(new NoExitSecurityManager());
	}

	@Override
	protected void tearDown() throws Exception {
		System.setSecurityManager(null); // or save and restore original
		super.tearDown();
	}

	public void testExit() throws Exception {
		try {
			System.exit(42);
		} catch (final ExitException e) {
			assertEquals("Exit status", 42, e.status);
		}
	}

	public void testNoExit() throws Exception {
		System.out.println("Printing works");
	}
}