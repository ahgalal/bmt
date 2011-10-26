package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

public class KeyboardUtils
{
	public static void pressKey(int key)
	{
		Event e = new Event();
		e.type = SWT.KeyDown;
		e.keyCode = key;
		Display.getDefault().post(e);
	}
	
	public static void typeText(String text, boolean pressEnter)
	{
		Display display = Display.getDefault();
		Event e = new Event();
		e.type = SWT.KeyDown;
		for(char c:text.toCharArray())
		{
			e.character = c;
			display.post(e);
			// TODO: need delay??
		}
		if(pressEnter)
		{
			e.keyCode = SWT.CR;
			display.post(e);
		}
		
	}
}
