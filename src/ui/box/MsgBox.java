package ui.box;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Encapsulates an SWT message box.
 * 
 * @author Creative
 */
public class MsgBox
{

	public static int show(
			final Shell parent,
			final String message,
			final String title,
			final int type)
	{
		final MessageBox msgbox = new MessageBox(parent, type);
		msgbox.setMessage(message);
		msgbox.setText(title);
		return msgbox.open();
	}
}
