package ui.filtergraph;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TempFilterGraph extends Shell {
	public TempFilterGraph() {
		FilterGraph filterGraph=new FilterGraph(this);
	}
	
	public static void main(String[] args){
		TempFilterGraph tempFilterGraph =new TempFilterGraph();
		
		Display display=Display.getDefault();
		tempFilterGraph.open();
		while (!tempFilterGraph.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		
	}
	
	@Override
	protected void checkSubclass() {
		// TODO Auto-generated method stub
	}
	
}
