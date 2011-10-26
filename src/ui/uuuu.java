package ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class uuuu
{
static Shell s1 = new Shell();
static Shell s2 = new Shell();

public static void main(String[] args)
{
	final Display display = Display.getDefault();
	s1.setBounds(0, 0, 50, 50);
	s2.setBounds(50, 50, 50, 50);
	
	s1.setVisible(true);
	s2.setVisible(true);
	
	
	while (!s1.isDisposed())
	{
		if (!display.readAndDispatch())
			display.sleep();
	}
	
}

}
