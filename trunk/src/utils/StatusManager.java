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

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * Manages the status of an SWT Shell.
 * 
 * @author Creative
 */
public class StatusManager {

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

	private Color	clrBlack, clrRed, clr;

	private StyledText	txt;						// the label to display the
												// status

	/**
	 * Initialization.
	 * 
	 * @param styledText
	 *            the label to display status
	 */
	public void initialize(final StyledText styledText) {
		this.txt = styledText;
		clrRed = new Color(styledText.getDisplay(), 255, 0, 0);
		clrBlack = new Color(styledText.getDisplay(), 0, 0, 0);
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
		if (txt != null) {
			if (svrty == StatusSeverity.ERROR)// ERROR-> RED
				clr = clrRed;
			else if (svrty == StatusSeverity.WARNING) 
				clr = clrBlack;
			else
				clr = clrBlack;

			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					int start = txt.getText().length();
					StyleRange styleRange=new StyleRange(start, msg.length(), clr, null);
					
					txt.append(msg+"\n");
					txt.setStyleRange(styleRange);
					txt.setTopIndex(txt.getLineCount() - 1);
				}
			});
		}
	}
}
