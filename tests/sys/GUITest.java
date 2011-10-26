package sys;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.junit.After;
import org.junit.Before;

import utils.PManager;

public abstract class GUITest
{

	protected SWTBot bot;
	protected PManager pm;

	protected void initializeBot()
	{
		System.out.println("initializing bot..");
		bot=new SWTBot();
	}

	protected void waitForGUIToLoad(int delay)
	{
		System.out.println("waiting for gui to load..");
		sleep(delay);
	}

	protected void sleep(int delay)
	{
		try
		{
			Thread.sleep(delay);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() throws Exception
	{
		System.out.println("setUp");
		System.out.println("Thread: " + Thread.currentThread().getName());
		//		PManager.testingMode=true;
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				System.out.println("State:" + Display.getDefault().getThread().getState());
				Display.getDefault().wake();
				System.out.println("State:" + Display.getDefault().getThread().getState());
				Display.getDefault().syncExec(new Runnable() {
					
					@Override
					public void run()
					{
						System.out.println("starting main");
						System.out.println("Thread: " + Thread.currentThread().getName());
						PManager.main(null);
						System.out.println("finished with main");
					}
				});

			}
		}).start();

		//PManager.main(null);
		waitForGUIToLoad(2000);
		initializeBot();
	}

	@After
	public void tearDown() throws Exception
	{
		System.out.println("tearDown ..");
		try{
			Display.getDefault().syncExec(new Runnable() {
				
				@Override
				public void run()
				{
					//PManager.main_gui.closeProgram();
					Display.getDefault().getActiveShell().close();
					//Display.getDefault().close();
				}
			});

		}catch(Exception e){e.printStackTrace();}

		System.out.println("Display is disposed!");
		sleep(2000);
	}

	public abstract void Test();
}