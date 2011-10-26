package sys;

import gui.CamStartDefaultTest;
import gui.VideoFileTest;
import junit.framework.Test;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import utils.PManager;

import junit.framework.TestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { CamStartDefaultTest.class,VideoFileTest.class })
public class GUITests
{
	private static Shell dummyShell;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
//		final Display display = Display.getDefault();
//		dummyShell = new Shell();
//		dummyShell.setBounds(0, 0, 50, 50);
//		/*		Thread t = new Thread(new Runnable() {
//
//			@Override
//			public void run()
//			{
//				// TODO Auto-generated method stub
//				display.asyncExec(new Runnable() {
//
//					@Override
//					public void run()
//					{
//						// TODO Auto-generated method stub
//
//					}
//				});
//
//
//			}
//		});
//		t.start();*/
//		new Thread(new Runnable() {
//
//			@Override
//			public void run()
//			{
//				// TODO Auto-generated method stub
//				Display.getDefault().syncExec(new Runnable() {
//
//					@Override
//					public void run()
//					{
//						dummyShell.setVisible(true);
//						while (!dummyShell.isDisposed())
//						{
//							if (!display.readAndDispatch())
//								display.sleep();
//						}
//					}
//				});
//			}
//		}).start();
//
//
//
//		Thread.sleep(100);
//		/*		while (!dummyShell.isDisposed())
//		{
//			if (!display.readAndDispatch())
//				display.sleep();
//		}*/
//
//		System.out.println("Thread: " + Thread.currentThread().getName());

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		dummyShell.dispose();
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
