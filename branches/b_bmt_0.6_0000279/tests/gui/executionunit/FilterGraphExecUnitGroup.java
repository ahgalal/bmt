package gui.executionunit;

import gui.utils.Reflections;

import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

import sys.utils.Utils;
import ui.filtergraph.FilterGraph.Filter;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.ComboItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.ListItemLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;

public class FilterGraphExecUnitGroup extends ExecutionUnitGroup {

	private static GraphConnection	connection	= null;

	private static GraphNode		node		= null;

	public static void cancel() throws WidgetSearchException {
		ui.click(new ButtonLocator("Cancel"));
	}

	public static void deleteConnection(final String filterSrcName,
			final String filterDstName) throws WidgetSearchException {
		selectConnection(filterSrcName, filterDstName);
		ui.click(new ButtonLocator("Delete"));
	}

	public static void deleteFilter(final String name)
			throws WidgetSearchException {
		selectFilter(name);
		ui.click(new ButtonLocator("Delete"));
	}

	private static GraphNode getNodeByName(final String name,
			final HashMap<Filter, GraphNode> nodes) {

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				for (final GraphNode tmpNode : nodes.values()) {
					if (tmpNode.getText().equals(name))
						node = tmpNode;
				}
			}
		});

		return node;
	}
	
	public static void addFilter(String filterName, String filterID, String triggerType) throws WidgetSearchException{
		ui.click(new ButtonLocator("Add Node"));
		
		ui.click(new LabeledTextLocator("Name"));
		ui.enterText(filterName);
		ui.click(new ListItemLocator(filterID));
		//ui.click(new ComboItemLocator("MANUAL"));
		//ui.click(new ComboItemLocator("PROCESSING"));
		//ui.click(new ComboItemLocator("STREAMING"));
		ui.click(new ComboItemLocator(triggerType));
		ui.click(new ButtonLocator("OK"));
	}
	
	public static void editFilter(String filterName,String newFilterName, String newFilterID, String newTriggerType) throws WidgetSearchException{
		selectFilter(filterName, true);
		Utils.sleep(500);
		
		if(newFilterName!=null){
			ui.click(new LabeledTextLocator("Name"));
			ui.enterText("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
			ui.enterText(newFilterName);
		}
		
		if(newFilterID!=null)
			ui.click(new ListItemLocator(newFilterID));
		//ui.click(new ComboItemLocator("MANUAL"));
		//ui.click(new ComboItemLocator("PROCESSING"));
		//ui.click(new ComboItemLocator("STREAMING"));
		if(newTriggerType!=null)
			ui.click(new ComboItemLocator(newFilterID));
		ui.click(new ButtonLocator("OK"));
	}


	public static void openFilterGraph() throws WidgetSearchException {
		ui.click(new MenuItemLocator("Edit/Advanced/Filters setup .."));
		Utils.sleep(500);
	}

	public static void reset() throws WidgetSearchException {
		ui.click(new ButtonLocator("Reset"));
	}

	public static void save() throws WidgetSearchException {
		ui.click(new ButtonLocator("Save"));
	}
	
	

	public static void selectConnection(final String filterSrcName,
			final String filterDstName) {
		final List<GraphConnection> connections = Reflections.getConnections();
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				for (final GraphConnection tmpConnection : connections) {
					if (tmpConnection.getSource().getText()
							.equals(filterSrcName)
							&& tmpConnection.getDestination().getText()
									.equals(filterDstName))
						connection = tmpConnection;
				}

				connection.getGraphModel().setSelection(
						new GraphItem[] { connection });
			}
		});

	}
	
	public static void connectFilters(final String filterSrcName,
			final String filterDstName) throws WidgetSearchException {
		ui.click(new ButtonLocator("Add Link"));
		selectFilter(filterSrcName);
		selectFilter(filterDstName);
	}
	
	public static void selectFilter(final String name)
	throws WidgetSearchException {
		selectFilter(name, false);
	}

	public static void selectFilter(final String name,boolean doubleClick)
			throws WidgetSearchException {
		final HashMap<Filter, GraphNode> nodes = Reflections.getNodesMap();
		final GraphNode node = getNodeByName(name, nodes);
		XYLocator xyLocator = new XYLocator(new SWTWidgetLocator(Graph.class), node
				.getLocation().x + 5, node.getLocation().y + 5);
		if(doubleClick)
			ui.click(2,xyLocator);
		else
			ui.click(xyLocator);
	}

	public FilterGraphExecUnitGroup(final IUIContext ui) {
		super(ui);
	}

}