package gui;

import sys.GUITest;
import sys.SWTBotRecorder;
import ui.ExternalStrings;
import utils.PManager;

/**
 * @author Creative
 *
 */
public class SaveExperimentOLD extends GUITest
{


	/* (non-Javadoc)
	 * @see sys.GUITest#Test()
	 */
	@Override
	@org.junit.Test
	public void Test()
	{
		PManager.testingMode=true;
		sleep(100);
		//TODO: use windowtester| bot.menu(ExternalStrings.get("MainGUI.Menu.Experiment")).menu(ExternalStrings.get("MainGUI.Menu.Exp.New")).click();
		sleep(100);
		new SWTBotRecorder();
		//TODO: use windowtester| bot.shell("Experiment Information").activate();
/*		bot.button("Cancel").click();
		bot.button("1").click();
		bot.button("2").click();
		bot.button("1").click();
		bot.button("2").click();
		bot.button("1").click();
*/
		
		//bot.button("Cancel").click();
		//bot.menu("Source").menu("Video File ..").click();
		//bot.menu("Source").menu("Camera").click();
		
		
		
		sleep(100000);
		//TODO: use windowtester| bot.text("TestName").setText("Test Experiment");
		//TODO: use windowtester| bot.text("TestNotes").setText("Test Notes");
		//TODO: use windowtester| bot.text("TestUser").setText("AGalal");
		//bot

		
		
		sleep(1000);
	}

}
