package modules.movementmeter;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.LineStyle;

import ui.GraphShell;
import ui.PluggedGUI;
import utils.PManager.ProgramState;

public class MovementMeterModuleGUI extends PluggedGUI<MovementMeterModule> {
	private Chart				mainChart;
	private Chart				secChart;

	private Composite			cmpstPlotter;

	private ILineSeries			mainLineSeries;
	private ILineSeries			secLineSeries;

	private ArrayList<Double>	plotData;
	private int					updatePlotterDataFlag	= 0;

	public MovementMeterModuleGUI(final MovementMeterModule owner) {
		super(owner);
	}

	public void addPoint(final int y) {

		plotData.add((double) y);
		if (updatePlotterDataFlag % 5 == 0) {
			final double[] tmpData = new double[plotData.size()];
			int i = 0;
			for (final Double d : plotData) {
				tmpData[i] = d;
				i++;
			}
			mainLineSeries.setYSeries(tmpData);
			mainChart.getAxisSet().adjustRange();
			mainChart.redraw();
			
			if(secChartParent.getShell().isVisible()){
				secLineSeries.setYSeries(tmpData);
				secChart.getAxisSet().adjustRange();
				secChart.redraw();
			}
		}
		updatePlotterDataFlag++;
	}

	private ILineSeries createChart(final Chart chart,boolean full) {
		// set titles
		if(full){
			chart.getTitle().setText("Energy");
			chart.getAxisSet().getXAxis(0).getTitle().setText("time");
		}
		else{
			chart.getTitle().setVisible(false);
			chart.getAxisSet().getXAxis(0).getTitle().setVisible(false);
		}
		
		chart.getAxisSet().getYAxis(0).getTitle().setText("Energy");
		
		// create line series
		final ILineSeries lineSeries = (ILineSeries) chart.getSeriesSet()
				.createSeries(SeriesType.LINE, "line series");
		lineSeries.setLineStyle(LineStyle.SOLID);
		lineSeries.setSymbolSize(1);
		lineSeries.setVisibleInLegend(false);

		// adjust the axis range
		chart.getAxisSet().adjustRange();
		final Composite parent=chart.getParent();
		chart.setBounds(0, 0, parent.getBounds().width-20,
				parent.getBounds().height-20);
		
		return lineSeries;
	}

	@Override
	public void deInitialize() {
		cmpstPlotter.dispose();
		if(secChartParent!=null)
			secChartParent.dispose();
	}
	private GraphShell secChartParent;
	@Override
	public void initialize(final Shell shell, final ExpandBar expandBar,
			final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
		cmpstPlotter = new Composite(grpGraphs, SWT.NONE);
		cmpstPlotter.setBounds(10, 20, 232, 135);
		plotData = new ArrayList<Double>();
		mainChart = new Chart(cmpstPlotter, SWT.NONE);
		mainLineSeries=createChart(mainChart,false);
		
		secChartParent =new GraphShell(SWT.DIALOG_TRIM| SWT.RESIZE);
		secChartParent.getShell().setBounds(100, 100, 400, 300);
		secChart=new Chart(secChartParent.getShell() , SWT.NONE);
		secChartParent.setControl(secChart);
		secLineSeries=createChart(secChart,true);


		mainChart.getPlotArea().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				secChartParent.getShell().setVisible(true);
				secChartParent.getShell().setActive();
			}
		});
	}

	@Override
	public void stateGeneralChangeHandler(final ProgramState state) {
	}

	@Override
	public void stateStreamChangeHandler(final ProgramState state) {
	}

	@Override
	public void trackingStartedHandler() {
		super.trackingStartedHandler();
		System.out.println("trackingStartedHandler");
		plotData.clear();
	}

}
