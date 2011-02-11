package ui.box;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;


/**
 * Encapsulates an SWT message box.
 * @author Creative
 */
public class MsgBox {

	public  static int show(Shell parent,String message,String title,int type)
	{
		MessageBox msgbox = new MessageBox(parent, type);
		msgbox.setMessage(message);
		msgbox.setText(title);
		return msgbox.open();
	}
}
