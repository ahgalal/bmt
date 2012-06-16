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

    private final boolean enable_cancel;
    private final String msg, title;
    // private boolean finished;
    private final Shell parent;
    private String str_input;

    /**
     * Initialization of the object.
     * 
     * @param parent
     *            parent shell of this box
     * @param title
     *            box title
     * @param message
     *            box message
     * @param enable_cancel
     *            show cancel button
     */
    public InputBox(final Shell parent, final String title,
	    final String message, final boolean enable_cancel) {
	msg = message;
	this.enable_cancel = enable_cancel;
	this.title = title;
	this.parent = parent;
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

	return str_input;
    }

    private Shell sShell = null; // @jve:decl-index=0:visual-constraint="10,10"
    private Label lbl_msg = null;
    private Text txt_input = null;
    private Button btn_ok = null;
    private Button btn_cancel = null;

    /**
     * This method initializes sShell.
     */
    private void createSShell() {
	if (enable_cancel)
	    sShell = new Shell(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM
		    | SWT.ON_TOP);
	else
	    sShell = new Shell(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM
		    | SWT.ON_TOP | SWT.TITLE);
	sShell.setText(title);
	sShell.setSize(new Point(405, 154));
	sShell.setLayout(null);
	lbl_msg = new Label(sShell, SWT.NONE);
	lbl_msg.setBounds(new Rectangle(8, 10, 387, 46));
	lbl_msg.setText(msg);
	txt_input = new Text(sShell, SWT.BORDER);
	txt_input.setBounds(new Rectangle(11, 64, 384, 24));
	btn_ok = new Button(sShell, SWT.NONE);
	btn_ok.setBounds(new Rectangle(226, 96, 84, 26));
	btn_ok.setText("OK");
	btn_ok.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
	    @Override
	    public void widgetSelected(
		    final org.eclipse.swt.events.SelectionEvent e) {
		str_input = txt_input.getText();
		sShell.dispose();
	    }
	});
	if (enable_cancel) {
	    btn_cancel = new Button(sShell, SWT.NONE);
	    btn_cancel.setBounds(new Rectangle(314, 96, 84, 26));
	    btn_cancel.setText("Cancel");
	    btn_cancel
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

}
