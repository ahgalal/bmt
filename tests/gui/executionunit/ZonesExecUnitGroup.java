/**
 * 
 */
package gui.executionunit;

import java.awt.Point;
import java.awt.event.KeyEvent;

import sys.utils.Utils;
import utils.DialogBoxUtils;
import utils.KeyboardUtils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WaitTimedOutException;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.ComboItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.ShellLocator;

/**
 * @author Creative
 */
public class ZonesExecUnitGroup extends ExecutionUnitGroup {

	public static void loadZones(final String zonesFile)
			throws WidgetSearchException {
		ui.click(new MenuItemLocator("Edit/Zone Editor .."));
		ui.wait(new ShellShowingCondition("Zone Editor"));
		basicLoadZones(zonesFile);
		hideZoneEditor();
		Utils.sleep(200);
	}

	public static void basicLoadZones(final String zonesFile)
			throws WidgetSearchException {
		ui.click(new ButtonLocator("Load from File"));
		DialogBoxUtils.fillDialog(zonesFile, ui);
	}

	public static void setScale(final String scale)
			throws WidgetSearchException, InterruptedException {
		ExperimentExecUnitGroup.activateMainGUI();
		
		openZoneEditor();
		ui.click(new ButtonLocator("Scaling"));
		DialogBoxUtils.fillDialog("", ui);
		ui.click(new XYLocator(new ShellLocator("Zone Editor"), 124, 50));
		ui.click(new XYLocator(new ShellLocator("Zone Editor"), 610, 50));
		ui.wait(new ShellShowingCondition("Real Distance"));
		ui.click(new LabeledTextLocator(
				"Please enter the real distance between the 2 points (in cm):"));
		ui.enterText(scale);
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Real Distance"));
		hideZoneEditor();
	}

	public static void hideZoneEditor() throws WidgetSearchException {
		ui.click(new ButtonLocator("Hide"));
	}

	public static void openZoneEditor() throws WidgetSearchException {
		Utils.sleep(100);
		ui.click(new MenuItemLocator("Edit/Zone Editor .."));
		ui.wait(new ShellShowingCondition("Zone Editor"));
	}

	public ZonesExecUnitGroup(final IUIContext ui) {
		super(ui);
	}
	
	public static void deleteShape(int x,int y) throws WaitTimedOutException, Exception{
		// delete shape
		ui.click(new XYLocator(new ShellLocator("Zone Editor"), x, y));
		Utils.sleep(200);
		
		// AWT delete
		KeyboardUtils.pressKeyAWT(KeyEvent.VK_DELETE);
	}
	
	public static void createRectangleShape(int x1,int y1,int x2,int y2,String shapeType, String zoneType) throws WaitTimedOutException, Exception{

		// create a new shape
		if(shapeType.equals("Rectangle"))
			ui.click(new ButtonLocator("Add Rectangle"));
		else if(shapeType.equals("Circle"))
			ui.click(new ButtonLocator("Add Circle"));
		
		final Point activeShellLocation = getActiveShellLocation();
		
		int xOffset,yOffset;
		xOffset = activeShellLocation.x;
		yOffset=activeShellLocation.y;
		
		// offset for the position of drawing area relative to the active Shell
		xOffset+=12;
		yOffset+=34;

		KeyboardUtils.mousePressAWT(x1+xOffset, y1+yOffset, KeyEvent.BUTTON1_MASK);
		KeyboardUtils.mouseMoveAWT(x2+xOffset, y2+yOffset);
		KeyboardUtils.mouseReleaseAWT(KeyEvent.BUTTON1_MASK);
		
		ui.wait(new ShellShowingCondition("Zone Type:"));
		ui.click(new ComboItemLocator(zoneType));
		ui.click(new ButtonLocator("OK"));
		ui.ensureThat(new ShellLocator("Zone Type:").isClosed());
		ui.wait(new ShellDisposedCondition("Zone Type:"));
	}
	
	public static void saveZonesToFile(String filePath) throws WidgetSearchException{
		ui.click(new ButtonLocator("Save to File"));
		DialogBoxUtils.fillDialog(filePath, ui);
	}

}
