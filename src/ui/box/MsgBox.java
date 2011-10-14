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
