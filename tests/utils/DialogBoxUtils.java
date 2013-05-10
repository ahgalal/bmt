package utils;


import sys.utils.Utils;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;

public class DialogBoxUtils {

	public static void fillDialog(String text,IUIContext ui){
		Utils.sleep(500);
		ui.enterText(text);
		ui.keyClick(WT.CR);
	}
}
