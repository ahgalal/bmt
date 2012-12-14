/**
 * 
 */
package gui.executionunit;

import utils.DialogBoxUtils;
import utils.Utils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
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
		ui.click(new ButtonLocator("Load from File"));
		DialogBoxUtils.fillDialog(zonesFile, ui);
		ui.click(new ButtonLocator("Hide"));
		Utils.sleep(200);
	}

	public static void setScale(final String scale)
			throws WidgetSearchException, InterruptedException {
		Utils.sleep(100);
		ui.click(new MenuItemLocator("Edit/Zone Editor .."));
		ui.wait(new ShellShowingCondition("Zone Editor"));
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
		ui.click(new ButtonLocator("Hide"));
	}

	public ZonesExecUnitGroup(final IUIContext ui) {
		super(ui);
	}

}
