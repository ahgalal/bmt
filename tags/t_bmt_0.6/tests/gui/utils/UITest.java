package gui.utils;

import gui.executionunit.ExperimentExecUnitGroup;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;

import com.windowtester.runtime.swt.UITestCaseSWT;

public class UITest extends UITestCaseSWT {
	
	@Before
	public void setUp(){
		new ExperimentExecUnitGroup(getUI());
		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				Shell shell = (Shell)getUI().getActiveWindow();
				if(shell!=null)
					shell.setLocation(0, 0);
				else
					System.err.println("Couldnot find main shell!");
			}
		});
	}
	
	public UITest() {
		super(utils.PManager.class);
	}
}
