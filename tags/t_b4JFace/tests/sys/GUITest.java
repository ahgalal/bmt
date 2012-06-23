package sys;

import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;

import utils.PManager;

public abstract class GUITest {

	// TODO: use windowtester| protected SWTBot bot;
	protected PManager	pm;

	protected void initializeBot() {
		System.out.println("initializing bot..");
		// TODO: use windowtester| bot=new SWTBot();
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("setUp");
		System.out.println("Thread: " + Thread.currentThread().getName());
		// PManager.testingMode=true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("State:"
						+ Display.getDefault().getThread().getState());
				Display.getDefault().wake();
				System.out.println("State:"
						+ Display.getDefault().getThread().getState());
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						System.out.println("starting main");
						System.out.println("Thread: "
								+ Thread.currentThread().getName());
						PManager.main(null);
						System.out.println("finished with main");
					}
				});

			}
		}).start();

		// PManager.main(null);
		waitForGUIToLoad(1000);
		initializeBot();
	}

	protected void sleep(final int delay) {
		try {
			Thread.sleep(delay);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown ..");
		try {
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					PManager.main_gui.closeProgram();
					// Display.getDefault().getActiveShell().close();
					// Display.getDefault().close();
				}
			});
		} catch (final SecurityException se) {

		} catch (final Exception e) {
			e.printStackTrace();
		}

		System.out.println("Display is disposed!");
		sleep(1000);
	}

	public abstract void Test();

	protected void waitForGUIToLoad(final int delay) {
		System.out.println("waiting for gui to load..");
		sleep(delay);
	}
}