/**
 * 
 */
package gui.executionunit;

import utils.DialogBoxUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.swt.locator.ButtonLocator;

/**
 * @author Creative
 *
 */
public class VideoExecUnitGroup extends ExecutionUnitGroup {

	public VideoExecUnitGroup(IUIContext ui) {
		super(ui);
	}
	
	public static void startStreamVideo(String videoFile) throws WidgetSearchException, InterruptedException{
		ui.click(new ButtonLocator("Start Stream"));
		DialogBoxUtils.fillDialog(videoFile, ui);
		Thread.sleep(200);
	}
	
	public static void pauseResumeStream() throws WidgetSearchException{
		ui.click(new ButtonLocator("Pause/Resume"));
	}
	
	public static void StopStream() throws WidgetSearchException{
		ui.click(new ButtonLocator("Stop Stream"));
	}
	
	public static void StartRecord() throws WidgetSearchException{
		ui.click(new ButtonLocator("Start Recording"));
	}
	
	public static void StopRecord() throws WidgetSearchException{
		ui.click(new ButtonLocator("Stop Recording"));
	}

}
