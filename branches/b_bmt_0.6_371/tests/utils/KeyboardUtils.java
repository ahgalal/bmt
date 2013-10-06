package utils;

import java.awt.AWTException;
import java.awt.Robot;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

public class KeyboardUtils {
	public static void pressKeySWT(final int key) {
		final Event e = new Event();
		e.type = SWT.KeyDown;
		e.keyCode = key;
		Display.getDefault().post(e);
	}
	
	public static void pressKeyAWT(final int key) throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(key);
		robot.keyRelease(key);
	}
	
	public static void mousePressAWT(int x,int y,int button) throws AWTException{
		Robot robot = new Robot();
		robot.mouseMove(x, y);
		robot.mousePress(button);
	}
	
	public static void mouseReleaseAWT(int button) throws AWTException{
		Robot robot = new Robot();
		robot.mouseRelease(button);
	}
	
	public static void mouseMoveAWT(int x,int y) throws AWTException{
		Robot robot = new Robot();
		robot.mouseMove(x, y);
	}

	public static void typeText(final String text, final boolean pressEnter) {
		final Display display = Display.getDefault();
		final Event e = new Event();
		for (final char c : text.toLowerCase().toCharArray()) {
			e.type = SWT.KeyDown;
			e.keyCode = 0;
			if ((c == ':') || (c == '_') || (c == '+')) {
				e.keyCode = SWT.SHIFT; // Post the shift
				display.post(e);
			}
			e.keyCode = 0;
			e.character = c;
			display.post(e); // Post the char (shift is already on)

			try {
				Thread.sleep(10);
			} catch (final InterruptedException e1) {
			}

			e.type = SWT.KeyUp;
			display.post(e); // Post char's key up (shift is still ON)

			if ((c == ':') || (c == '_') || (c == '+')) {
				e.keyCode = SWT.SHIFT;
				display.post(e); // Post shift's key up
			}
		}
		if (pressEnter) {
			e.character = 0;
			e.type = SWT.KeyDown;
			e.keyCode = SWT.CR;
			display.post(e);
		}

	}
}
