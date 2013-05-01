package gui.utils;

import java.util.HashMap;
import java.util.List;

import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

import modules.ExperimentManager;
import modules.experiment.Experiment;
import ui.filtergraph.FilterGraph;
import ui.filtergraph.FilterGraph.Filter;
import utils.ReflectUtils;

public class Reflections{
	public static Experiment getLoadedExperiment(){
		Experiment exp=null;
		exp=(Experiment) ReflectUtils.getField(ExperimentManager.getDefault(), "exp");
		return exp;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Filter, GraphNode> getNodesMap() {
		HashMap<Filter, GraphNode> nodes = (HashMap<Filter, GraphNode>) ReflectUtils.getField(FilterGraph.getDefault(), "nodes");
		return nodes;
	}
	
	@SuppressWarnings("unchecked")
	public static List<GraphConnection> getConnections() {
		HashMap<Filter, GraphNode> nodes = (HashMap<Filter, GraphNode>) ReflectUtils.getField(FilterGraph.getDefault(), "nodes");
		return nodes.values().iterator().next().getGraphModel().getConnections();
	}
}