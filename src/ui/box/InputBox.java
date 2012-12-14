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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Displays an inputbox to take user input.
 * 
 * @author Creative
 */
public class InputBox {

	private Button			btnCancel	= null;
	private Button			btnOk		= null;
	private final boolean	enableCancel;
	private Label			lblMsg		= null;

	private final String	msg, title;

	// private boolean finished;
	private final Shell		parent;

	private Shell			sShell		= null; // @jve:decl-index=0:visual-constraint="10,10"
	private String			strInput;
	private Text			txtInput	= null;

	/**
	 * Initialization of the object.
	 * 
	 * @param parent
	 *            parent shell of this box
	 * @param title
	 *            box title
	 * @param message
	 *            box message
	 * @param enableCancel
	 *            show cancel button
	 */
	public InputBox(final Shell parent, final String title,
			final String message, final boolean enableCancel) {
		msg = message;
		this.enableCancel = enableCancel;
		this.title = title;
		this.parent = parent;
	}

	/**
	 * This method initializes sShell.
	 */
	private void createSShell() {
		if (enableCancel)
			sShell = new Shell(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM
					| SWT.ON_TOP);
		else
			sShell = new Shell(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM
					| SWT.ON_TOP | SWT.TITLE);
		sShell.setText(title);
		sShell.setSize(new Point(405, 154));
		sShell.setLayout(null);
		lblMsg = new Label(sShell, SWT.NONE);
		lblMsg.setBounds(new Rectangle(8, 10, 387, 46));
		lblMsg.setText(msg);
		txtInput = new Text(sShell, SWT.BORDER);
		txtInput.setBounds(new Rectangle(11, 64, 384, 24));
		btnOk = new Button(sShell, SWT.NONE);
		btnOk.setBounds(new Rectangle(226, 96, 84, 26));
		btnOk.setText("OK");
		btnOk.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					final org.eclipse.swt.events.SelectionEvent e) {
				strInput = txtInput.getText();
				sShell.dispose();
			}
		});
		if (enableCancel) {
			btnCancel = new Button(sShell, SWT.NONE);
			btnCancel.setBounds(new Rectangle(314, 96, 84, 26));
			btnCancel.setText("Cancel");
			btnCancel
					.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
						@Override
						public void widgetSelected(
								final org.eclipse.swt.events.SelectionEvent e) {
							sShell.dispose();
						}
					});
		}
		sShell.open();
	}

	/**
	 * Creates the GUI components and displays the box.
	 * 
	 * @return the string entered by the user
	 */
	public String show() {
		createSShell();
		final Display display = parent.getDisplay();
		while (!sShell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();

		return strInput;
	}

}
