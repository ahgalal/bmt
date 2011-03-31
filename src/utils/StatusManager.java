package utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * Manages the status of an SWT Shell.
 * 
 * @author Creative
 */
public class StatusManager
{

	private Label lbl; // the label to display the status
	private Color clr_black, clr_red, clr;

	/**
	 * Initialization.
	 * 
	 * @param lbl_status
	 *            the label to display status
	 */
	public void initialize(final Label lbl_status)
	{
		this.lbl = lbl_status;
		clr_red = new Color(lbl_status.getDisplay(), 255, 0, 0);
		clr_black = new Color(lbl_status.getDisplay(), 0, 0, 0);
	}

	/**
	 * Sets the label to the given string.
	 * 
	 * @param msg
	 *            message to display in the label
	 * @param svrty
	 *            severity of the message
	 */
	public void setStatus(final String msg, final StatusSeverity svrty)
	{
		if (lbl != null)
		{
			if (svrty == StatusSeverity.ERROR)
				clr = clr_red;
			else if (svrty == StatusSeverity.WARNING) // ERROR-> RED
				clr = clr_black;

			lbl.setForeground(clr);
			lbl.setText(msg);

			final Thread th_reset_status = new Thread(new RunnableResetFormStatus());

			th_reset_status.start();
		}
	}

	/**
	 * Responsible for clearing the label after certain amount of time.
	 * 
	 * @author Creative
	 */
	private class RunnableResetFormStatus implements Runnable
	{

		@Override
		public void run()
		{
			try
			{
				Thread.sleep(4000);
			} catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run()
				{
					if (!lbl.isDisposed())
						lbl.setText("");
				}
			});
		}

	}

	/**
	 * Enumeration of the severity of message.
	 * 
	 * @author Creative
	 */
	public enum StatusSeverity
	{
		/**
		 * ERROR: for Error messages WARNING: for normal messages
		 */
		ERROR, WARNING;
	}
}
