package ui.filtergraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;

import filters.FiltersConnectionRequirements;
import filters.FiltersNamesRequirements;
import filters.FiltersSetup;

public class FilterGraph {

	private static FilterGraph self;

	public static FilterGraph getDefault() {
		if (self == null)
			self = new FilterGraph();
		return self;
	}

	private boolean addingLink;
	private Button btnNewLink;
	private Button btnNewNode;
	private Composite container;
	private FiltersSetup filtersSetup;
	private Graph graph;

	private ArrayList<GraphConnection> links;

	private ArrayList<GraphNode> nodes;
	private Shell shell;
	private GraphNode srcNode;

	/**
	 * @wbp.parser.constructor
	 */
	private FilterGraph() {
		shell = new Shell();
		shell.setSize(640, 480);
		shell.setLayout(new FillLayout());

		shell.addListener(SWT.Close, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				event.doit = false;
				shell.setVisible(false);
			}
		});
		initializeForm(shell);
	}

	private FilterGraph(final Composite parent) {
		initializeForm(parent);
	}

	private void clearGraph() {
		if (nodes != null) {
			disposeAllNodes(nodes);
		}

		if (links != null) {
			// we just need to clear links array, as links are already disposed
			// along with associated nodes.
			links.clear();
		}
	}

	private void disposeAllNodes(final ArrayList<? extends GraphNode> arr) {
		for (final GraphNode w : arr) {
			w.dispose();
		}
		nodes.clear();
	}

	private GraphNode getNode(final String name) {
		for (final GraphNode graphNode : nodes) {
			if (graphNode.getText().equals(name))
				return graphNode;
		}
		return null;
	}

	private void initializeForm(final Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setSize(parent.getSize());
		container.setLayout(new FillLayout());
		container.setBackground(new Color(Display.getDefault(), 0, 255, 0));

		graph = new Graph(container, SWT.NONE);

		graph.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(final MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(final MouseEvent arg0) {
				if (addingLink) {
					final List<?> selection = graph.getSelection();
					if (selection.size() == 1) {
						final Object selectedItem = selection.get(0);
						if (selectedItem instanceof GraphNode) {
							GraphNode selectedNode = (GraphNode) selectedItem;
							if (srcNode == null)
								srcNode = selectedNode;
							else {
								// TODO: update FilterSetup with the new
								// connection
								GraphConnection connection = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, srcNode, selectedNode);
								
								//filtersSetup.getConnectionRequirements().connectFilters(srcNode.getText(), selectedNode.getText());
								
								links.add(connection);
								System.out
										.println("a new link is added with:\nsrc: "
												+ srcNode
												+ "\ndst: "
												+ selectedItem);
								srcNode = null;
								addingLink = false;
							}

						} else
							throw new RuntimeException("Please select a Node");
					} else
						throw new RuntimeException(
								"Please select single filter");
				}
			}

			@Override
			public void mouseUp(final MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		final Button btnDelete = new Button(graph, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final List<?> selection = graph.getSelection();
				final ArrayList<GraphNode> nodesToRemove = new ArrayList<GraphNode>();
				final ArrayList<GraphConnection> connectionsToRemove = new ArrayList<GraphConnection>();
				for (final Object obj : selection) {
					if (obj instanceof GraphNode) {
						nodesToRemove.add((GraphNode) obj);

					} else if (obj instanceof GraphConnection) {
						connectionsToRemove.add((GraphConnection) obj);
					}
				}

				/*
				 * we split the removal process to prevent the
				 * ConcurrentModificationException in the SWT thread
				 */
				for (final GraphConnection graphConnection : connectionsToRemove)
					removeConnection(graphConnection);

				for (final GraphNode graphNode : nodesToRemove)
					removeNode(graphNode);

				selection.clear();
			}
		});
		btnDelete.setBounds(539, 407, 75, 25);
		btnDelete.setText("delete");

		btnNewNode = new Button(graph, SWT.NONE);
		btnNewNode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				// TODO: implement
			}
		});
		btnNewNode.setBounds(539, 376, 75, 25);
		btnNewNode.setText("add node");

		btnNewLink = new Button(graph, SWT.NONE);
		btnNewLink.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				addingLink = true;
			}
		});
		btnNewLink.setBounds(539, 345, 75, 25);
		btnNewLink.setText("add link");

		layout();
	}

	private void layout() {
		graph.setLayoutAlgorithm(new HorizontalTreeLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
	}

	public void openWindow() {
		shell.open();
	}

	private void removeConnection(final GraphConnection connection) {
		connection.dispose();
		links.remove(connection);
	}

	private void removeNode(final GraphNode node) {
		node.dispose();
		nodes.remove(node);
	}
	
	/**
	 * Saves the current filters' connections into the FiltersSetup instance.
	 */
	private void saveToFiltersSetup(){
		// TODO: implement
	}

	public void setFilterSetup(final FiltersSetup filtersSetup) {
		this.filtersSetup = filtersSetup;
		final FiltersConnectionRequirements connectionRequirements = filtersSetup
				.getConnectionRequirements();
		final FiltersNamesRequirements filtersNamesRequirements = filtersSetup
				.getFiltersNamesRequirements();

		clearGraph();

		nodes = new ArrayList<GraphNode>();
		links = new ArrayList<GraphConnection>();

		for (final Iterator<Entry<String, String>> it = filtersNamesRequirements
				.getFilters(); it.hasNext();) {
			final Entry<String, String> entry = it.next();
			nodes.add(new GraphNode(graph, SWT.NULL, entry.getKey()));
		}

		final ArrayList<String[]> connections = connectionRequirements
				.getConnections();

		for (final String[] pair : connections) {
			final String srcName = pair[0];
			final String dstName = pair[1];
			final GraphConnection graphConnection = new GraphConnection(graph,
					ZestStyles.CONNECTIONS_DIRECTED, getNode(srcName),
					getNode(dstName));
			links.add(graphConnection);
		}

		layout();
	}
}
