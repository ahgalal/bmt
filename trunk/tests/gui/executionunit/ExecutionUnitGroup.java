package gui.executionunit;

import com.windowtester.runtime.IUIContext;

public class ExecutionUnitGroup {
	protected static IUIContext	ui;

	public ExecutionUnitGroup(final IUIContext ui) {
		ExecutionUnitGroup.ui = ui;
	}
}
