package gui.utils;

import gui.executionunit.ExperimentExecUnitGroup;

import org.junit.Before;

import com.windowtester.runtime.swt.UITestCaseSWT;

public class UITest extends UITestCaseSWT {
	
	@Before
	public void setUp(){
		new ExperimentExecUnitGroup(getUI());
	}
	
	public UITest() {
		super(utils.PManager.class);
	}
}
