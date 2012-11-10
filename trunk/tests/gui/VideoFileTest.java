package gui;

import filters.Link;
import sys.GUITest;
import utils.PManager;

public class VideoFileTest extends GUITest {

	@Override
	@org.junit.Test
	public void Test() {
		sleep(100);
		// bot.menu("Video").menu("Source").menu("Video File ..").click();
		// TODO: use windowtester| ReflectUtils.setField(CtrlMainGUI.class,
		// pm.main_gui, "file_name", "C:\\vid.avi");
		// TODO: use windowtester| bot.button("Start Stream").click();

		sleep(2000);
		final Link lnk = null;
		pm = PManager.getDefault();
		// TODO: use windowtester| lnk =
		// (Link)ReflectUtils.getField(VideoFilter.class,pm.getVideoManager().getFilterManager().getFilterByName("Source Filter"),
		// "link_out");

		assert (lnk != null) : "video stream's link is null";

		assert (lnk.getData()[100] != 0) : "video data is 0";

		sleep(3000);

		// TODO: use windowtester| bot.button("Stop Stream").click();

		/*
		 * Event e = new Event(); e.character=SWT.ESC; e.type=SWT.KeyDown;
		 * Display.getDefault().post(e);
		 */

		sleep(3000);

	}

}
