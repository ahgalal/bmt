package gui;


import org.junit.Test;

import sys.GUITest;
import utils.PManager;
import utils.ReflectUtils;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;

public class CamStartDefaultTest extends GUITest
{
	@Test
	public void Test()
	{
		bot.menu("Video").menu("Source").menu("Camera").click();
		bot.button("Start Stream").click();
		sleep(2000);

		Link lnk = null;
		pm = PManager.getDefault();
		lnk = (Link)ReflectUtils.getField(VideoFilter.class,
				pm.getVideoManager().getFilterManager().getFilterByName("Source Filter"),
		"link_out");

		assert (lnk!=null): "video stream's link is null";

		assert(lnk.getData()[100]!=0): "video data is 0";

		sleep(1000);
		bot.button("Stop Stream").click();
		sleep(2000);
	}
}
