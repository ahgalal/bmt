package ui.filtergraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import filters.FiltersConnectionRequirements;
import filters.FiltersNamesRequirements;
import filters.FiltersSetup;

public class FilterGraph {

	private final Composite container;
	private Graph graph;
	private ArrayList<GraphNode> nodes;
	private ArrayList<GraphConnection> links;
	public FilterGraph(final Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setSize(parent.getSize());
		container.setLayout(new FillLayout());
		container.setBackground(new Color(Display.getDefault(), 0, 255, 0));

		// test:
		graph= new Graph(container, SWT.NONE);
		
		graph.setLayoutAlgorithm(new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
	}
	
	public void setFilterSetup(FiltersSetup filtersSetup){
		FiltersConnectionRequirements connectionRequirements = filtersSetup.getConnectionRequirements();
		FiltersNamesRequirements filtersNamesRequirements = filtersSetup.getFiltersNamesRequirements();
		
		nodes=new ArrayList<GraphNode>();
		links=new ArrayList<GraphConnection>();
		
		for(Iterator<Entry<String, String>> it = filtersNamesRequirements.getFilters();it.hasNext();){
			Entry<String, String> entry = it.next();
			nodes.add(new GraphNode(graph, SWT.NULL, entry.getKey()));
		}
		
		ArrayList<String[]> connections = connectionRequirements.getConnections();
		
		for(String[] pair:connections){
			String srcName = pair[0];
			String dstName = pair[1];
			GraphConnection graphConnection = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, getNode(srcName), getNode(dstName));
			links.add(graphConnection);
		}
	}
	
	private GraphNode getNode(String name){
		for(GraphNode graphNode:nodes){
			if(graphNode.getText().equals(name))
				return graphNode;
		}
		return null;
	}
	
	public FiltersSetup test(){
		FiltersNamesRequirements filtersRequirements = new FiltersNamesRequirements();
		FiltersConnectionRequirements connectionRequirements = new FiltersConnectionRequirements();
		
			// required filters
			filtersRequirements.addFilter("SourceFilter", "filters.source");
			filtersRequirements.addFilter("ScreenDrawer", "filters.screendrawer");
			filtersRequirements.addFilter("ScreenDrawerSec", "filters.screendrawer");
			filtersRequirements.addFilter("RatFinder", "filters.ratfinder");
			filtersRequirements.addFilter("RearingDetector", "filters.rearingdetector");
			filtersRequirements.addFilter("Recorder", "filters.videorecorder");
			filtersRequirements.addFilter("SubtractionFilter", "filters.subtractor");
			filtersRequirements.addFilter("AverageFilter", "filters.average");
			
			// connections
			connectionRequirements.connectFilters("SourceFilter", "ScreenDrawer");
			connectionRequirements.connectFilters("SourceFilter", "Recorder");
			connectionRequirements.connectFilters("SourceFilter", "SubtractionFilter");
			connectionRequirements.connectFilters("SubtractionFilter", "AverageFilter");
			connectionRequirements.connectFilters("SubtractionFilter", "RearingDetector");

			connectionRequirements.connectFilters("RatFinder", "ScreenDrawerSec");
			connectionRequirements.connectFilters("AverageFilter", "RatFinder");
			
		
		return new FiltersSetup(filtersRequirements , connectionRequirements );
	}

}
