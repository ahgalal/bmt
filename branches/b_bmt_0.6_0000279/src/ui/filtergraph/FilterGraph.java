package ui.filtergraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;

import utils.PManager;
import utils.PManager.ProgramState.StreamState;
import utils.StatusManager.StatusSeverity;
import filters.FiltersConnectionRequirements;
import filters.FiltersNamesRequirements;
import filters.FiltersNamesRequirements.FilterRequirement;
import filters.FiltersNamesRequirements.FilterTrigger;
import filters.FiltersSetup;

public class FilterGraph {

	public static class Filter {
		private final FilterRequirement	filterRequirement;

		public Filter(final String name, final String id,
				final FilterTrigger trigger) {
			filterRequirement = new FilterRequirement(name, id, trigger);
			setName(name);
			setId(id);
		}

		public String getId() {
			return filterRequirement.getID();
		}

		public String getName() {
			return filterRequirement.getName();
		}

		public FilterTrigger getTrigger() {
			return filterRequirement.getTrigger();
		}

		public void setId(final String id) {
			filterRequirement.setID(id);
		}

		public void setName(final String name) {
			filterRequirement.setName(name);
		}

		public void setTrigger(final FilterTrigger trigger) {
			filterRequirement.setTrigger(trigger);
		}
	}

	private static FilterGraph	self;

	public static FilterGraph getDefault() {
		if (self == null)
			self = new FilterGraph();
		return self;
	}

	private boolean							addingLink;
	private Button							btnSave;
	private Button							btnDelete;
	private Button							btnNewLink;
	private Button							btnNewNode;

	private Button							btnReset;
	private Composite						container;
	private boolean							editingNode;
	private FiltersSetup					filtersSetup;
	private Graph							graph;

	private Group							grpTools;
	private HashMap<Filter, GraphNode>		nodes;
	private ArrayList<String[]>				originalConnectionsReq;
	private ArrayList<FilterRequirement>	originalFiltersNamesRequirements;

	private Shell							shell;

	private GraphNode						srcNode;

	/**
	 * @wbp.parser.constructor
	 */
	private FilterGraph() {
		shell = new Shell();
		shell.setSize(640, 480);

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

	private boolean addNewNode(final Filter filter) {
		// look for name duplication
		if (getNode(filter.getName()) == null) {
			final GraphNode node = new GraphNode(graph, SWT.NULL,
					filter.getName());
			nodes.put(filter, node);
			return true;
		}
		return false;
	}

	public boolean addUpdateNode(final Filter filter) {
		if (editingNode == false) {
			return addNewNode(filter);
		} else {
			
			// we are editing
			final List<?> selection = graph.getSelection();
			if (selection.size() == 1) {
				final Object selectedItem = selection.get(0);
				if (selectedItem instanceof GraphNode) {
					final GraphNode selectedNode = (GraphNode) selectedItem;

					// update node's name
					selectedNode.setText(filter.getName());

					// remove old map entry
					nodes.remove(getKey(selectedNode, nodes));

					// add new map entry
					nodes.put(filter, selectedNode);
					return true;
				}
			}
			editingNode = false;
		}
		layout();
		return true;
	}

	private void cancelAddingLink() {
		srcNode = null;
		addingLink = false;
	}

	private void clearGraph() {
		if (nodes != null) {
			disposeAllNodes(nodes.values());
		}
	}

	private void deleteSelectedItem() {
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

	private void disposeAllNodes(final Collection<? extends GraphNode> arr) {
		for (final GraphNode w : arr) {
			w.dispose();
		}
		nodes.clear();
	}

	private Object getKey(final Object value, final HashMap<?, ?> map) {
		Object key = null;
		for (final Object tmpKey : map.keySet()) {
			if (map.get(tmpKey) == value) {
				key = tmpKey;
				break;
			}
		}
		return key;
	}

	private GraphNode getNode(final String name) {
		for (final GraphNode graphNode : nodes.values()) {
			if (graphNode.getText().equals(name))
				return graphNode;
		}
		return null;
	}

	private void initializeForm(final Composite parent) {
		shell.setLayout(new GridLayout(1, false));
		container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		container.setSize(parent.getSize());
		container.setLayout(new GridLayout(2, false));

		final Group grpLayout = new Group(container, SWT.NONE);
		grpLayout.setLayout(new GridLayout(1, false));
		grpLayout.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		grpLayout.setText("Layout");

		graph = new Graph(grpLayout, SWT.BORDER);
		graph.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		graph.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(final MouseEvent arg0) {
				final List<?> selection = graph.getSelection();
				if (selection.size() == 1) {
					final Object selectedItem = selection.get(0);
					if (selectedItem instanceof GraphNode) {
						final GraphNode selectedNode = (GraphNode) selectedItem;
						showFilterNodeProperties();
						FilterNodeProperties.getDefault().loadFilter(
								(Filter) getKey(selectedNode, nodes));
						editingNode = true;
					}
				}
			}

			@Override
			public void mouseDown(final MouseEvent arg0) {
				if (addingLink) {
					final List<?> selection = graph.getSelection();
					if (selection.size() == 1) {
						final Object selectedItem = selection.get(0);
						if (selectedItem instanceof GraphNode) {
							final GraphNode selectedNode = (GraphNode) selectedItem;
							if (srcNode == null)
								srcNode = selectedNode;
							else {

								// look for multiple connections to the same
								// filter (this is not supported)
								if (isFilterInputConnected(selectedNode)) {
									showErrorMessage("Filter \""
											+ selectedNode.getText()
											+ "\" cannot be connected twice on its input port");
								} else {
									new GraphConnection(graph,
											ZestStyles.CONNECTIONS_DIRECTED,
											srcNode, selectedNode);

									System.out
											.println("a new link is added with:\nsrc: "
													+ srcNode
													+ "\ndst: "
													+ selectedItem);
								}

								// either we displayed an error, or successfully
								// created a link, we need to erase the
								// linkAdding flag
								cancelAddingLink();
							}

						} else
							showErrorMessage("Please select a Node");
					} else {
						if (selection.size() > 1)
							showErrorMessage("Please select single filter");
						else if (selection.size() == 0)
							showErrorMessage("Please select a filter");
					}
				}

				graph.forceFocus();
			}

			@Override
			public void mouseUp(final MouseEvent arg0) {
			}
		});

		graph.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(final KeyEvent arg0) {
			}

			@Override
			public void keyReleased(final KeyEvent arg0) {
				if (arg0.keyCode == SWT.DEL) {
					deleteSelectedItem();
				}
			}
		});

		grpTools = new Group(container, SWT.NONE);
		grpTools.setText("Tools");

		btnNewNode = new Button(grpTools, SWT.NONE);
		btnNewNode.setLocation(10, 21);
		btnNewNode.setSize(75, 25);
		btnNewNode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				editingNode = false;
				showFilterNodeProperties();
			}

		});
		btnNewNode.setText("Add Node");

		btnNewLink = new Button(grpTools, SWT.PUSH);
		btnNewLink.setLocation(10, 52);
		btnNewLink.setSize(75, 25);
		btnNewLink.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				srcNode = null;
				addingLink = true;
			}
		});
		btnNewLink.setText("Add Link");

		btnSave = new Button(grpTools, SWT.NONE);
		btnSave.setLocation(10, 321);
		btnSave.setSize(75, 25);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (saveToFiltersSetup())
					shell.close();
			}
		});
		btnSave.setText("Save");

		final Button btnCancel = new Button(grpTools, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				shell.close();
			}
		});
		btnCancel.setBounds(10, 384, 75, 25);
		btnCancel.setText("Cancel");

		btnDelete = new Button(grpTools, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				deleteSelectedItem();
			}

		});
		btnDelete.setBounds(10, 83, 75, 25);
		btnDelete.setText("Delete");

		btnReset = new Button(grpTools, SWT.NONE);
		btnReset.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				resetToOriginalSetup();
			}

		});
		btnReset.setBounds(10, 352, 75, 25);
		btnReset.setText("Reset");

		layout();
	}

	/**
	 * Checks if node's input port is connected.
	 * 
	 * @param node
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isFilterInputConnected(final GraphNode node) {
		for (final GraphConnection connection : (List<GraphConnection>) graph
				.getConnections()) {
			if (connection.getDestination().getText().equals(node.getText()))
				return true;
		}
		return false;
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
	}

	private void removeNode(final GraphNode node) {
		node.dispose();
		nodes.remove(getKey(node, nodes));
	}

	private void resetToOriginalSetup() {
		setRequirements(originalConnectionsReq,
				originalFiltersNamesRequirements);
		layout();
	}

	/**
	 * Saves the current filters' connections into the FiltersSetup instance.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean saveToFiltersSetup() {
		// make sure we are neither streaming nor paused
		if ((PManager.getDefault().getState().getStream() != StreamState.STREAMING)
				&& (PManager.getDefault().getState().getStream() != StreamState.PAUSED)) {

			// clear filters in the FiltersNamesRequirements
			filtersSetup.getFiltersNamesRequirements().clearFilterNames();

			// add filters on the graph to filterSetup's
			// FiltersNamesRequirements
			for (final Filter filter : nodes.keySet()) {
				filtersSetup.getFiltersNamesRequirements().addFilter(
						filter.getName(), filter.getId(), filter.getTrigger());
			}

			// clear connections in ConnectionRequirements
			filtersSetup.getConnectionRequirements().clearConnections();

			// connect filters based on graph
			final List<GraphConnection> connections = graph.getConnections();
			for (final GraphConnection connection : connections) {
				filtersSetup.getConnectionRequirements().connectFilters(
						connection.getSource().getText(),
						connection.getDestination().getText());
			}

			return PManager.getDefault().getVideoManager()
					.signalFiltersSetupChange();
		} else {
			PManager.getDefault()
					.getStatusMgr()
					.setStatus(
							"Filter configuration can't be changed while streaming, please stop stream and try again.",
							StatusSeverity.ERROR);
			return false;
		}
	}

	public void setFilterSetup(final FiltersSetup filtersSetup) {
		this.filtersSetup = filtersSetup;
		final FiltersConnectionRequirements connectionRequirements = filtersSetup
				.getConnectionRequirements();
		final FiltersNamesRequirements filtersNamesRequirements = filtersSetup
				.getFiltersNamesRequirements();

		final ArrayList<FilterRequirement> filtersNamesRequirementsArray = new ArrayList<FilterRequirement>();
		for (final Iterator<FilterRequirement> it = filtersNamesRequirements
				.getFilters(); it.hasNext();) {
			final FilterRequirement next = it.next();
			filtersNamesRequirementsArray.add(next);
		}
		setRequirements(connectionRequirements.getConnections(),
				filtersNamesRequirementsArray);

		layout();
	}

	private void setRequirements(final ArrayList<String[]> connectionsReq,
			final ArrayList<FilterRequirement> filtersNamesRequirements) {
		clearGraph();

		nodes = new HashMap<Filter, GraphNode>();
		originalConnectionsReq = new ArrayList<String[]>();
		originalFiltersNamesRequirements = new ArrayList<FilterRequirement>();

		for (final FilterRequirement filterRequirement : filtersNamesRequirements) {
			final String filterName = filterRequirement.getName();
			final String filterID = filterRequirement.getID();
			nodes.put(
					new Filter(filterName, filterID, filterRequirement
							.getTrigger()), new GraphNode(graph, SWT.NULL,
							filterName));
			try {
				originalFiltersNamesRequirements
						.add((FilterRequirement) filterRequirement.clone());
			} catch (final CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		for (final String[] pair : connectionsReq) {
			final String srcName = pair[0];
			final String dstName = pair[1];
			new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED,
					getNode(srcName), getNode(dstName));

			originalConnectionsReq.add(new String[] { srcName, dstName });
		}
	}

	private void showErrorMessage(final String message) {
		final MessageBox box = new MessageBox(shell, SWT.ERROR);
		box.setMessage(message);
		box.open();
	}

	private void showFilterNodeProperties() {
		FilterNodeProperties.getDefault().show();
		FilterNodeProperties.getDefault().setFilterIDs(
				PManager.getDefault().getVideoManager().getFilterManager()
						.getFiltersIDs());
	}
}
