package gui;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.swt.finder.keyboard.Keyboard;
import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;

import control.ui.CtrlMainGUI;

import sys.GUITest;
import utils.PManager;
import utils.ReflectUtils;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;

public class VideoFileTest extends GUITest
{

	@Override
	@org.junit.Test
	public void Test()
	{
		sleep(100);
		//bot.menu("Video").menu("Source").menu("Video File ..").click();
		ReflectUtils.setField(CtrlMainGUI.class, pm.main_gui, "file_name", "C:\\vid.avi");
		bot.button("Start Stream").click();
		
		sleep(2000);
		Link lnk = null;
		pm = PManager.getDefault();
		lnk = (Link)ReflectUtils.getField(VideoFilter.class,
				pm.getVideoManager().getFilterManager().getFilterByName("Source Filter"),
		"link_out");

		assert (lnk!=null): "video stream's link is null";

		assert(lnk.getData()[100]!=0): "video data is 0";
		
		sleep(3000);
		
		bot.button("Stop Stream").click();
		
/*		Event e = new Event();
		e.character=SWT.ESC;
		e.type=SWT.KeyDown;
		Display.getDefault().post(e);*/
		
		sleep(3000);
		
	}

}
