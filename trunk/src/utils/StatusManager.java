/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * Manages the status of an SWT Shell.
 * 
 * @author Creative
 */
public class StatusManager {

    private Label lbl; // the label to display the status
    private Color clr_black, clr_red, clr;

    /**
     * Initialization.
     * 
     * @param lbl_status
     *            the label to display status
     */
    public void initialize(final Label lbl_status) {
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
    public void setStatus(final String msg, final StatusSeverity svrty) {
	if (lbl != null) {
	    if (svrty == StatusSeverity.ERROR)
		clr = clr_red;
	    else if (svrty == StatusSeverity.WARNING) // ERROR-> RED
		clr = clr_black;

	    lbl.setForeground(clr);
	    lbl.setText(msg);

	    final Thread th_reset_status = new Thread(
		    new RunnableResetFormStatus());

	    th_reset_status.start();
	}
    }

    /**
     * Responsible for clearing the label after certain amount of time.
     * 
     * @author Creative
     */
    private class RunnableResetFormStatus implements Runnable {

	@Override
	public void run() {
	    try {
		Thread.sleep(4000);
	    } catch (final InterruptedException e) {
		e.printStackTrace();
	    }
	    Display.getDefault().asyncExec(new Runnable() {

		@Override
		public void run() {
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
    public enum StatusSeverity {
	/**
	 * ERROR: for Error messages WARNING: for normal messages.
	 */
	ERROR, WARNING;
    }
}
