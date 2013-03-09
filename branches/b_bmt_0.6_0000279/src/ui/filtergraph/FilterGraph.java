package ui.filtergraph;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class FilterGraph {
	
	public void test(){
		
	}

	private static class FilterBox {

		private final Composite box;
		private final Label name;

		public FilterBox(final Composite parent, final String boxName) {
			box = new Composite(parent, SWT.BORDER);
			box.setSize(40, 40);
			name = new Label(box, 0);
			name.setText(boxName);
			name.setBounds(0, 0, 20, 10);
		}
		
		public void setPosition(int x,int y){
			box.setLocation(x, y);
		}

	}

	private static class Port {
		public static enum PortDirection {
			IN, OUT;
		}

		private PortDirection direction;
		private Label name;
		private Composite port;
	}

	private final Composite container;

	public FilterGraph(final Composite parent) {
		container = new Composite(parent, 0);
		container.setSize(parent.getSize());

		// test:
		container.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				FilterBox filterBox=new FilterBox(container, "testBox");
				filterBox.setPosition(arg0.x, arg0.y);
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
