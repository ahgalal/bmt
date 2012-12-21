package modules.movementmeter;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
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

	public class Curve {
		private final ArrayList<Double>	data;
		private final Chart				mainChart;
		private final ILineSeries		mainLineSeries;
		private final Chart				secChart;
		private final ILineSeries		secLineSeries;
		private int						updatePlotterDataFlag	= 0;

		public Curve(final Chart mainChart, final Chart secChart,
				final ILineSeries mainLineSeries,
				final ILineSeries secLineSeries) {
			this.mainLineSeries = mainLineSeries;
			this.secLineSeries = secLineSeries;
			this.mainChart = mainChart;
			this.secChart = secChart;
			data = new ArrayList<Double>();
		}

		public void addPoint(final int y) {
			addPoint(y, false);
		}

		public void addPoint(final int y, final boolean level) {
			final double value = y;
			data.add(value);
			if (level) {
				for (int i = 0; i < data.size(); i++)
					data.set(i, value);
			}

			if (updatePlotterDataFlag % 5 == 0) {
				final double[] tmpData = new double[data.size()];
				int i = 0;
				for (final Double d : data) {
					tmpData[i] = d;
					i++;
				}
				updateChart(mainLineSeries, mainChart, tmpData);

				if (secChartParent.getShell().isVisible()) {
					updateChart(secLineSeries, secChart, tmpData);
				}
			}
			updatePlotterDataFlag++;
		}

		public void clear() {
			data.clear();
		}

		public ArrayList<Double> getData() {
			return data;
		}

		public ILineSeries getMainLineSeries() {
			return mainLineSeries;
		}

		public ILineSeries getSecLineSeries() {
			return secLineSeries;
		}

		private void updateChart(final ILineSeries lineSeries,
				final Chart chart, final double[] data) {
			lineSeries.setYSeries(data);
			chart.getAxisSet().adjustRange();
			chart.redraw();
		}
	}

	private Composite			cmpstPlotter;
	private Curve				energyCurve;
	private ArrayList<Curve>	levelCurves;
	private GraphShell			secChartParent;

	public MovementMeterModuleGUI(final MovementMeterModule owner) {
		super(owner);
	}

	public void addPoint(final int y) {
		energyCurve.addPoint(y);
	}

	private ILineSeries createChart(final Chart chart, final boolean full,
			final String name, final Color color) {
		// set titles
		if (full) {
			chart.getTitle().setText("Energy");
			chart.getAxisSet().getXAxis(0).getTitle().setText("time");
		} else {
			chart.getTitle().setVisible(false);
			chart.getAxisSet().getXAxis(0).getTitle().setVisible(false);
		}

		chart.getAxisSet().getYAxis(0).getTitle().setText("Energy");

		// create line series
		final ILineSeries lineSeries = (ILineSeries) chart.getSeriesSet()
				.createSeries(SeriesType.LINE, name);
		lineSeries.setLineStyle(LineStyle.SOLID);
		lineSeries.setSymbolSize(1);
		lineSeries.setLineColor(color);
		lineSeries.setVisibleInLegend(false);

		// adjust the axis range
		chart.getAxisSet().adjustRange();
		final Composite parent = chart.getParent();
		chart.setBounds(0, 0, parent.getBounds().width - 20,
				parent.getBounds().height - 20);

		return lineSeries;
	}

	@Override
	public void deInitialize() {
		cmpstPlotter.dispose();
		if (secChartParent != null)
			secChartParent.dispose();
	}

	@Override
	public void initialize(final Shell shell, final ExpandBar expandBar,
			final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
		cmpstPlotter = new Composite(grpGraphs, SWT.NONE);
		cmpstPlotter.setBounds(10, 20, 232, 135);
		levelCurves = new ArrayList<Curve>();

		// data = new ArrayList<Double>();
		/*
		 * mainLevelsILineSeries=new ArrayList<ILineSeries>();
		 * secLevelsILineSeries=new ArrayList<ILineSeries>();
		 */
		final Chart mainChart = new Chart(cmpstPlotter, SWT.NONE);
		final ILineSeries mainLineSeries = createChart(mainChart, false,
				"energy", Display.getDefault().getSystemColor(SWT.COLOR_BLUE));

		secChartParent = new GraphShell(SWT.DIALOG_TRIM | SWT.RESIZE);
		secChartParent.getShell().setBounds(100, 100, 400, 300);
		final Chart secChart = new Chart(secChartParent.getShell(), SWT.NONE);
		secChartParent.setControl(secChart);
		final ILineSeries secLineSeries = createChart(secChart, true, "energy",
				Display.getDefault().getSystemColor(SWT.COLOR_BLUE));

		mainChart.getPlotArea().addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(final MouseEvent arg0) {
				secChartParent.getShell().setVisible(true);
				secChartParent.getShell().setActive();
			}

			@Override
			public void mouseDown(final MouseEvent arg0) {
			}

			@Override
			public void mouseUp(final MouseEvent arg0) {
			}
		});

		energyCurve = new Curve(mainChart, secChart, mainLineSeries,
				secLineSeries);

		final ILineSeries mainLevelSeries1 = createChart(mainChart, false,
				"level1", Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		final ILineSeries mainLevelSeries2 = createChart(mainChart, false,
				"level2", Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		final ILineSeries mainLevelSeries3 = createChart(mainChart, false,
				"level3", Display.getDefault().getSystemColor(SWT.COLOR_BLACK));

		final ILineSeries secLevelSeries1 = createChart(secChart, true,
				"level1", Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		final ILineSeries secLevelSeries2 = createChart(secChart, true,
				"level2", Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		final ILineSeries secLevelSeries3 = createChart(secChart, true,
				"level3", Display.getDefault().getSystemColor(SWT.COLOR_BLACK));

		levelCurves.add(new Curve(mainChart, secChart, mainLevelSeries1,
				secLevelSeries1));
		levelCurves.add(new Curve(mainChart, secChart, mainLevelSeries2,
				secLevelSeries2));
		levelCurves.add(new Curve(mainChart, secChart, mainLevelSeries3,
				secLevelSeries3));
	}

	public void setEnergyLevels(final int levels[]) {
		if (levels.length == levelCurves.size()) {
			for (int i = 0; i < levels.length; i++)
				levelCurves.get(i).addPoint(levels[i], true);
		} else
			throw new RuntimeException("Size mismatch");
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
		energyCurve.clear();
	}

}
