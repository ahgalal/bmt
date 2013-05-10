/**
 * 
 */
package gui.executionunit;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;

/**
 * @author Creative
 *
 */
public class OptionsExecUnitGroup extends ExecutionUnitGroup {

	public OptionsExecUnitGroup(IUIContext ui) {
		super(ui);
	}
	
	public static void openOptionsDialog() throws WidgetSearchException{
		ui.click(new MenuItemLocator("Edit/Options .."));
		ui.wait(new ShellShowingCondition("OptionsWindow"));
	}
	
	public static void setSubtractionThreshold(int value) throws WidgetSearchException{
		ui.click(2, new XYLocator(new LabeledTextLocator(
		"Subtraction Threshold"), 69, 9));
		ui.enterText(Integer.toString(value));
	}
	
	public static void setRearingThreshold(int value) throws WidgetSearchException{
		ui.click(2, new XYLocator(new LabeledTextLocator("Rearing Threshold"),
				66, 7));
		ui.enterText(Integer.toString(value));
	}
	
	public static void setZoneHysteresis(int value) throws WidgetSearchException{
		ui.click(2, new XYLocator(new LabeledTextLocator("Zone Hysteresis"),
				66, 3));
		ui.enterText(Integer.toString(value));
	}

	public static void clickOK() throws WidgetSearchException {
		ui.click(new ButtonLocator("OK"));		
	}
	
	public static void cancel() throws WidgetSearchException {
		ui.click(new ButtonLocator("Cancel"));		
	}


}
