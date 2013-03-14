package ui.filtergraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

import ui.box.InputBox;
import utils.PManager;
import filters.FiltersConnectionRequirements;
import filters.FiltersNamesRequirements;
import filters.FiltersNamesRequirements.FilterRequirement;
import filters.FiltersNamesRequirements.FilterTrigger;
import filters.FiltersSetup;

public class FilterGraph {

	private static FilterGraph self;

	public static FilterGraph getDefault() {
		if (self == null)
			self = new FilterGraph();
		return self;
	}
	
	private static class Filter{
		private FilterRequirement filterRequirement;
		private int numberInPorts=0;
		private int numberOutPorts=0;
		public void setName(String name) {
			filterRequirement.setName(name);
		}
		public String getName() {
			return filterRequirement.getName();
		}
		public void setId(String id) {
			filterRequirement.setID(id);
		}
		public String getId() {
			return filterRequirement.getID();
		}
		
		public FilterTrigger getTrigger(){
			return filterRequirement.getTrigger();
		}
		
		public void setTrigger(FilterTrigger trigger){
			filterRequirement.setTrigger(trigger);
		}
		
		public Filter(String name,String id, FilterTrigger trigger) {
			filterRequirement=new FilterRequirement(name, id, trigger);
			setName(name);
			setId(id);
		}
	}

	private boolean addingLink;
	private Button btnNewLink;
	private Button btnNewNode;
	private Composite container;
	private FiltersSetup filtersSetup;
	private Graph graph;

	private HashMap<Filter,GraphNode> nodes;
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
			disposeAllNodes(nodes.values());
		}
	}

	private void disposeAllNodes(final Collection<? extends GraphNode> arr) {
		for (final GraphNode w : arr) {
			w.dispose();
		}
		nodes.clear();
	}

	private GraphNode getNode(final String name) {
		for (final GraphNode graphNode : nodes.values()) {
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
								new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, srcNode, selectedNode);
								
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
				InputBox boxID = new InputBox(shell, "Filter ID", "Please enter new filter's ID", true);
				String id = boxID.show();
				if(id!=null){
					InputBox boxName = new InputBox(shell, "Filter Name", "Please enter new filter's name", true);
					String name = boxName.show();
					if(name!=null){
						InputBox triggerBox = new InputBox(shell, "Filter Trigger", "Please enter new filter's trigger type", true);
						String trigger = triggerBox.show();
						if(trigger!=null){
							
							FilterTrigger filterTrigger=null;
							if(trigger.equals("manual"))
								filterTrigger=FilterTrigger.MANUAL;
							else if(trigger.equals("processing"))
								filterTrigger=FilterTrigger.PROCESSING;
							else if(trigger.equals("streaming"))
								filterTrigger=FilterTrigger.STREAMING;
							
							Filter filter = new Filter(name, id,filterTrigger);
							GraphNode node = new GraphNode(graph, SWT.NULL,name);
							nodes.put(filter , node);
							layout();
						}else{
							// TODO: implement cancel
						}
					}else{
						// TODO: implement cancel
					}
				}else{
					// TODO: implement cancel
				}
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
		
		Button btnApply = new Button(graph, SWT.NONE);
		btnApply.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveToFiltersSetup();
			}
		});
		btnApply.setBounds(539, 314, 75, 25);
		btnApply.setText("apply");

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
	}

	private void removeNode(final GraphNode node) {
		node.dispose();
		nodes.remove(getKey(node, nodes));
	}
	
	private Object getKey(Object value,HashMap<?,?> map){
		Object key=null;
		for(Object tmpKey:map.keySet()){
			if(map.get(tmpKey)==value){
				key=tmpKey;
				break;
			}
		}
		return key;
	}
	
	/**
	 * Saves the current filters' connections into the FiltersSetup instance.
	 */
	@SuppressWarnings("unchecked")
	private void saveToFiltersSetup(){
		filtersSetup.getFiltersNamesRequirements().clearFilterNames();
		for(Filter filter:nodes.keySet()){
			filtersSetup.getFiltersNamesRequirements().addFilter(filter.getName(), filter.getId(),filter.getTrigger());
		}
		
		filtersSetup.getConnectionRequirements().clearConnections();
		
		for(GraphConnection connection:(List<GraphConnection>)graph.getConnections()){
			filtersSetup.getConnectionRequirements().connectFilters(connection.getSource().getText(), connection.getDestination().getText());	
		}
		
		PManager.getDefault().getVideoManager().signalFiltersSetupChange();
	}
	
	public void setFilterSetup(final FiltersSetup filtersSetup) {
		this.filtersSetup = filtersSetup;
		final FiltersConnectionRequirements connectionRequirements = filtersSetup
				.getConnectionRequirements();
		final FiltersNamesRequirements filtersNamesRequirements = filtersSetup
				.getFiltersNamesRequirements();

		clearGraph();

		nodes = new HashMap<Filter,GraphNode>();

		for (final Iterator<FilterRequirement> it = filtersNamesRequirements
				.getFilters(); it.hasNext();) {
			final FilterRequirement filterRequirement = it.next();
			String filterName = filterRequirement.getName();
			String filterID = filterRequirement.getID();
			nodes.put(new Filter(filterName, filterID,filterRequirement.getTrigger()), new GraphNode(graph, SWT.NULL, filterName));
		}

		final ArrayList<String[]> connectionsReq = connectionRequirements
				.getConnections();

		for (final String[] pair : connectionsReq) {
			final String srcName = pair[0];
			final String dstName = pair[1];
			new GraphConnection(graph,
					ZestStyles.CONNECTIONS_DIRECTED, getNode(srcName),
					getNode(dstName));
		}

		layout();
	}
}
