package ui.filtergraph;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TempFilterGraph {
	static Shell shell;
	
	public static void main(String[] args){
		Display display=Display.getDefault();
		shell=new Shell(display);
		shell.setSize(640, 480);
		shell.setLayout(new FillLayout());
		
		
		FilterGraph filterGraph=new FilterGraph(shell);
		filterGraph.setFilterSetup(filterGraph.test());
		shell.open();
		
		
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		
	}
	
	
}
