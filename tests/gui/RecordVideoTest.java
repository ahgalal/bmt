package gui;

import org.eclipse.swt.SWT;

import sys.GUITest;
import utils.KeyboardUtils;

public class RecordVideoTest extends GUITest
{
	@org.junit.Test
	@Override
	public void Test()
	{
		bot.menu("Video").menu("Source").menu("Camera").click();
		bot.button("Start Stream").click();
		sleep(2000);

		System.out.println("setting background..");
		bot.button("Set Background").click();
		sleep(1000);
		System.out.println("start tracking..");
		bot.button("Start Tracking").click();
		sleep(100);
		KeyboardUtils.pressKey(SWT.CR);
		sleep(100);
		bot.button("Start Recording").click();
		sleep(3000); // time of recording
		bot.button("Stop Recording").click();
		KeyboardUtils.typeText("TestVideo.avi", true);
		sleep(2000);
	}

}
