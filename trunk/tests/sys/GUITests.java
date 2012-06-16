package sys;

import java.security.Permission;

import gui.CamStartDefaultTest;
import gui.SetBackgroundTest;
import gui.VideoFileTest;
import junit.framework.Test;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import sys.NoExitTestCase.ExitException;
import utils.PManager;

import junit.framework.TestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses( {
	CamStartDefaultTest.class,
	VideoFileTest.class,
	SetBackgroundTest.class
})
public class GUITests
{
    protected static class ExitException extends SecurityException 
    {
        public final int status;
        public ExitException(int status) 
        {
                super("There is no escape!");
                this.status = status;
        }
    }

    private static class NoExitSecurityManager extends SecurityManager 
    {
        @Override
        public void checkPermission(Permission perm) 
        {
                // allow anything.
        }
        @Override
        public void checkPermission(Permission perm, Object context) 
        {
                // allow anything.
        }
        @Override
        public void checkExit(int status) 
        {
                //super.checkExit(status);
                throw new ExitException(status);
        }
    }
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		//System.setSecurityManager(new NoExitSecurityManager());
		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				Shell s = new Shell();
				s.setVisible(false);
			}
		});

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	/*	public static Test suite()
	{
		TestSuite suite = new TestSuite(GUITests.class.getName());
		//$JUnit-BEGIN$
		//suite.addTest(TestSuite.createTest(CamStartDefaultTest.class, "cam"));
		//$JUnit-END$
		return suite;
	}*/

}
