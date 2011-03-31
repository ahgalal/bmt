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

	/**
	 * Shows the messagebox on the screen.
	 * 
	 * @param parent
	 *            Parent shell of this message box
	 * @param message
	 *            message to display in the messagebox's content
	 * @param title
	 *            messagebox's title
	 * @param type
	 *            messagebox's type (Info, warning, error ...)
	 * @return integer representing the answer to the messagebox
	 */
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
