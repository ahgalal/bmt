/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package ui.filtergraph;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * This snippet creates a very simple graph where Rock is connected to Paper
 * which is connected to scissors which is connected to rock.
 * 
 * The nodes a layed out using a SpringLayout Algorithm, and they can be moved
 * around.
 * 
 * 
 * @author Ian Bull
 * 
 */
public class GraphSnippet1 {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the shell
		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setText("GraphSnippet1");
		shell.setLayout(new FillLayout());
		shell.setSize(400, 400);

		final Graph g = new Graph(shell, SWT.NONE);
		GraphNode n = new GraphNode(g, SWT.NONE, "Paper");
		GraphNode n2 = new GraphNode(g, SWT.NONE, "Rock");
		GraphNode n3 = new GraphNode(g, SWT.NONE, "Scissors");
		
g.addMouseListener(new MouseListener() {
	
	@Override
	public void mouseUp(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseDown(MouseEvent arg0) {
		if(g.getSelection().size()>0)
			System.out.println("selected node: " + ((GraphNode)g.getSelection().get(0)).getText());
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
});
		
		new GraphConnection(g, SWT.NONE, n, n2);
		new GraphConnection(g, SWT.NONE, n2, n3);
		new GraphConnection(g, SWT.NONE, n3, n);
		g.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
	}
}