/**
 * 
 */
package gui.executionunit;

import org.eclipse.swt.widgets.Combo;

import utils.DialogBoxUtils;
import utils.PManager;
import utils.ReflectUtils;
import sys.utils.Utils;
import utils.video.FrameIntArray;
import utils.video.input.VidInputter.SourceType;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.ComboItemLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.ShellLocator;

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
	
	public static void startStreamCamera() throws WidgetSearchException, InterruptedException{
		ui.click(new ButtonLocator("Start Stream"));
		Utils.sleep(6000);
	}
	
	public static void pauseResumeStream() throws WidgetSearchException{
		ExperimentExecUnitGroup.activateMainGUI();
		ui.click(new ButtonLocator("Pause/Resume"));
	}
	
	public static void stopStream() throws WidgetSearchException{
		ui.click(new ButtonLocator("Stop Stream"));
	}
	
	public static void startRecord() throws WidgetSearchException{
		ui.click(new ButtonLocator("Start Recording"));
	}
	
	public static void stopRecord() throws WidgetSearchException{
		ui.click(new ButtonLocator("Stop Recording"));
	}
	
	public static void confirmVideoOptions() throws WidgetSearchException{
		ui.click(new ButtonLocator("OK"));
	}
	
	public static void openVideoOptions() throws WidgetSearchException{
		ui.click(new MenuItemLocator("Video/Options .."));
		ui.wait(new ShellShowingCondition("Camera Options"));
	}
	
	public static void selectVideoLib(String libName) throws WidgetSearchException{
		ui.click(new ComboItemLocator(libName, new SWTWidgetLocator(
				Combo.class, 1, new ShellLocator("Camera Options"))));
	}
	
	public static FrameIntArray getFrameIntArray() {
		FrameIntArray fia = (FrameIntArray) ReflectUtils.getField(PManager
				.getDefault().getVideoManager(), "fia");
		return fia;
	}
	
	public static boolean isVideoManagerPaused() {
		boolean paused = (Boolean) ReflectUtils.getField(PManager
				.getDefault().getVideoManager(), "paused");
		return paused;
	}

	public static void selectVideoSource(SourceType cam) throws WidgetSearchException {
		switch (cam) {
			case CAM:
				ui.click(new MenuItemLocator("Video/Source/Camera"));
				ui.click(new MenuItemLocator("Video/Source/Camera"));
				break;
			case FILE:
				ui.click(new MenuItemLocator("Video/Source/Video File .."));
				ui.click(new MenuItemLocator("Video/Source/Video File .."));
			default:
				throw new RuntimeException("Unexpected Source type!");
		}
	}

}
