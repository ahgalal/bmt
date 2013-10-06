package gui.executionunit;

import java.awt.Point;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.windowtester.runtime.IUIContext;

public class ExecutionUnitGroup {
	protected static IUIContext	ui;

	public ExecutionUnitGroup(final IUIContext ui) {
		ExecutionUnitGroup.ui = ui;
	}
	
	public static Point getActiveShellLocation() {
		final Point activeShellLocation = new Point();
		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				Shell activeShell = Display.getDefault().getActiveShell();
				activeShellLocation.x=activeShell.getLocation().x;
				activeShellLocation.y=activeShell.getLocation().y;
			}
		});
		return activeShellLocation;
	}
}
