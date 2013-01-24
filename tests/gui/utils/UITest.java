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
				((Shell)getUI().getActiveWindow()).setLocation(0, 0);
			}
		});
		
	}
	
	public UITest() {
		super(utils.PManager.class);
	}
}
