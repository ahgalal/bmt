/**
 * 
 */
package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Creative
 */
public class GraphShell {
	public static final int	MARGIN	= 40;
	private Control			control;
	private final Shell		shell;

	public GraphShell(final int style) {
		shell = new Shell(style);
		initialize();
	}

	public Control getControl() {
		return control;
	}

	public Shell getShell() {
		return shell;
	}

	public void initialize() {
		shell.addControlListener(new ControlListener() {
			@Override
			public void controlMoved(final ControlEvent arg0) {
			}

			@Override
			public void controlResized(final ControlEvent arg0) {
				if (control != null)
					control.setSize(shell.getBounds().width - MARGIN,
							shell.getBounds().height - MARGIN);
			}
		});

		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				shell.setVisible(false);
				e.doit = false;
			}
		});
	}

	public void setControl(final Control control) {
		this.control = control;

	}

	public void dispose() {
		shell.dispose();		
	}

}
