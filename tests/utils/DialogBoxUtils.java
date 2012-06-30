package utils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;

public class DialogBoxUtils {

	public static void fillDialog(String text,IUIContext ui){
		ui.enterText("TestExp_editted.bmt");
		ui.keyClick(WT.CR);
	}
}
