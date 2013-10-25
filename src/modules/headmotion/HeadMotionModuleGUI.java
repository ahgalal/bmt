/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package modules.headmotion;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries.SeriesType;

import ui.PluggedGUI;
import utils.PManager.ProgramState;

/**
 * GUI class for the rearing module.
 * 
 * @author Creative
 */
public class HeadMotionModuleGUI extends PluggedGUI<HeadMotionModule> {

	private IBarSeries	barSeries;
	private Composite	cmpstAngleHistogram;
	private Chart		mainChart;
	private int			numberOfAngleSections	= 0;

	/**
	 * Initializes/shows the GUI components.
	 * 
	 * @param parent
	 *            parent composite that the components will be children of
	 */

	public HeadMotionModuleGUI(final HeadMotionModule owner,
			final int numberOfAngleSections) {
		super(owner);
		this.numberOfAngleSections = numberOfAngleSections;
	}

	@Override
	public void deInitialize() {
		disposeWidget(cmpstAngleHistogram);
	}

	@Override
	public void initialize(final Shell shell, final ExpandBar expandBar,
			final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
		cmpstAngleHistogram = new Composite(grpGraphs, SWT.NONE);
		cmpstAngleHistogram.setBounds(10, 20, grpGraphs.getSize().x-25, grpGraphs.getSize().y - 25);
		mainChart = new Chart(cmpstAngleHistogram, SWT.NONE);
		mainChart.setBounds(0, 0, cmpstAngleHistogram.getSize().x,
				cmpstAngleHistogram.getSize().y);

		final Font font = new Font(Display.getDefault(), "Arial", 10, SWT.None);
		// set titles
		final IAxis xAxis = mainChart.getAxisSet().getXAxis(0);
		final IAxis yAxis = mainChart.getAxisSet().getYAxis(0);

		mainChart.getTitle().setText("Angle Histogram");
		mainChart.getTitle().setVisible(false);
		xAxis.getTitle().setText("Angle");
		xAxis.getTitle().setFont(font);
		xAxis.setCategorySeries(prepareXAxisCategories());
		xAxis.enableCategory(true);
		yAxis.getTitle().setText("Frequency");
		yAxis.getTitle().setFont(font);

		final double[] frequencies = {};
		// create bar series
		barSeries = (IBarSeries) mainChart.getSeriesSet().createSeries(
				SeriesType.BAR, "bar series");
		barSeries.setYSeries(frequencies);
		barSeries.setBarColor(new Color(Display.getDefault(),255, 0, 0));
		mainChart.getLegend().setVisible(false);

		// adjust the axis range
		mainChart.getAxisSet().adjustRange();
	}

	private String[] prepareXAxisCategories() {
		final String[] categories = new String[numberOfAngleSections];

		int min = HeadMotionModuleData.MIN_ANGLE;
		final int sectionWidth = HeadMotionModuleData.ANGLE_BREADTH / numberOfAngleSections;
		for (int i = 0; i < numberOfAngleSections; i++) {
			categories[i] = min + "|" + (min + sectionWidth);
			min += sectionWidth;
		}
		return categories;
	}

	private void redrawChart(final double[] frequenciesDouble) {
		barSeries.setYSeries(frequenciesDouble);
		mainChart.getAxisSet().adjustRange();
		mainChart.redraw();
	}

	@Override
	public void stateGeneralChangeHandler(final ProgramState state) {
	}

	@Override
	public void stateStreamChangeHandler(final ProgramState state) {

	}

	public void updateHistogramFrequencies(final int[] frequencies) {
		final double[] frequenciesDouble = new double[frequencies.length];
		for (int i = 0; i < frequencies.length; i++)
			frequenciesDouble[i] = frequencies[i];

		redrawChart(frequenciesDouble);
	}
}
