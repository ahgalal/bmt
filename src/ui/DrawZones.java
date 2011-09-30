/***************************************************************************
 *  Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly,
 *  Sarah Hamid and Mohammed Ahmed Ramadan
 *  contact: ceng.ahmedgalal@gmail.com
 *
 *  This file is part of Behavioral Monitoring Tool.
 *
 *  Behavioral Monitoring Tool is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, version 3 of the
 *  License.
 *
 *  Behavioral Monitoring Tool is distributed in the hope that it
 *  will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Behavioral Monitoring Tool.
 *  If not, see <http://www.gnu.org/licenses/>.
 *   
 **************************************************************************/

package ui;

import gfx_panel.GfxPanel;

import java.awt.Point;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import ui.box.InputBox;
import utils.PManager;
import control.ui.ControllerUI;
import control.ui.CtrlDrawZones;

/**
 * Displays and Manipulates zones and their associated shapes.
 * 
 * @author Creative
 */
public class DrawZones extends BaseUI
{
	private CtrlDrawZones controller; // @jve:decl-index=0:
	private Point measure_pnt1, measure_pnt2; // @jve:decl-index=0:
	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="7,-197"
	private Composite composite = null;
	private GfxPanel gfx_panel;

	// boolean is_drawing_now = false;

	/**
	 * Creates GUI components, and links this Shell with the parent Shell.
	 */
	public DrawZones()
	{
		createSShell();
		super.sShell = sShell;
	}

	/**
	 * Gets the GFXPanel instance.
	 * 
	 * @return GFXPanel instance
	 */
	public GfxPanel getGFXPanel()
	{
		return gfx_panel;
	}

	/**
	 * Creates the composite.
	 */
	private void createComposite()
	{
		composite = new Composite(sShell, SWT.BORDER);
		composite.setLayout(null);
		composite.setBounds(5, 5, 640, 480 + 35);
		gfx_panel = new GfxPanel(
				sShell,
				composite,
				composite.getSize().x,
				composite.getSize().y);
		gfx_panel.setEnableSnap(true);
	}

	private Button btn_hide = null;
	private Button btn_save_zones = null;
	private Button btn_load_zones = null;
	private Table tbl_zones = null;
	private Button btn_set_scale = null;
	private String str_real_distance;

	/**
	 * Initializes sShell.
	 */
	private void createSShell()
	{
		sShell = new Shell(SWT.APPLICATION_MODAL | SWT.ON_TOP | SWT.TITLE);
		sShell.setText("Zone Editor");
		createComposite();
		sShell.setSize(959, 562);
		sShell.setLayout(null);
		btn_hide = new Button(sShell, SWT.NONE);
		btn_hide.setBounds(new Rectangle(659, 503, 108, 26));
		btn_hide.setText("Hide");
		btn_save_zones = new Button(sShell, SWT.NONE);
		btn_save_zones.setText("Save to File");
		btn_save_zones.setBounds(new Rectangle(659, 473, 108, 26));
		btn_load_zones = new Button(sShell, SWT.NONE);
		btn_load_zones.setBounds(new Rectangle(659, 442, 108, 26));
		btn_load_zones.setText("Load from File");
		tbl_zones = new Table(sShell, SWT.BORDER | SWT.FULL_SELECTION);
		tbl_zones.setHeaderVisible(true);
		tbl_zones.setLinesVisible(true);
		tbl_zones.setBounds(new Rectangle(658, 22, 274, 414));
		btn_set_scale = new Button(sShell, SWT.NONE);
		btn_set_scale.setBounds(new Rectangle(822, 442, 108, 26));
		btn_set_scale.setText("Scaling");
		btn_set_scale.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				final MessageBox msgbox = new MessageBox(sShell);
				msgbox.setMessage("Please click on 2 points on the screen , then enter the real distance between them");
				msgbox.setText("Scaling");
				msgbox.open();
				controller.settingScale(true);
				gfx_panel.enableDraw(false);
				final Thread th_2points = new Thread(new Runnable() {
					@Override
					public void run()
					{
						final InputBox inputbox_getreal_dist = new InputBox(
								sShell,
								"Real Distance",
								"Please enter the real distance between the 2 points (in cm):",
								false);
						while (measure_pnt1 == null || measure_pnt2 == null)
							try
							{
								Thread.sleep(200);
							} catch (final InterruptedException e)
							{
								e.printStackTrace();
							}
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run()
							{
								str_real_distance = inputbox_getreal_dist.show();
								gfx_panel.enableDraw(true);
							}
						});
						controller.sendScaletoStatsCtrlr(
								measure_pnt1,
								measure_pnt2,
								str_real_distance);
						measure_pnt1 = null;
						measure_pnt2 = null;
						controller.settingScale(false);
					}
				});
				th_2points.start();
			}
		});
		final TableColumn tableColumn = new TableColumn(tbl_zones, SWT.NONE);
		tableColumn.setWidth(40);
		tableColumn.setText("Num");
		final TableColumn tableColumn1 = new TableColumn(tbl_zones, SWT.NONE);
		tableColumn1.setWidth(90);
		tableColumn1.setText("Type");
		final TableColumn tableColumn2 = new TableColumn(tbl_zones, SWT.NONE);
		tableColumn2.setWidth(60);
		tableColumn2.setText("Color");
		btn_load_zones.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				gfx_panel.selectShape(-1);
				controller.btnLoadZonesAction(sShell);
			}
		});
		btn_save_zones.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.btnSaveZonesAction(sShell);
			}
		});
		btn_hide.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e)
			{
				controller.btnHideAction();
			}
		});

		tbl_zones.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				gfx_panel.selectShape(Integer.parseInt(tbl_zones.getItem(
						tbl_zones.getSelectionIndex()).getText(0)));
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0)
			{
			}
		});
	}

	@Override
	public void show(final boolean visibility)
	{
		super.show(visibility);
		if (visibility)
			gfx_panel.redrawAllShapes();
	}

	/**
	 * Adds zone to the GUI table.
	 * 
	 * @param zone_no
	 *            zone number
	 * @param zone_col
	 *            zone color
	 * @param zone_type
	 *            zone type
	 */
	public void addZoneToTable(
			final String zone_no,
			final String zone_col,
			final String zone_type)
	{
		if (Display.getCurrent() != Display.getDefault())
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run()
				{
					guiAddZoneToTable(zone_no, zone_col, zone_type);
				}
			});
		else
			guiAddZoneToTable(zone_no, zone_col, zone_type);
	}

	/**
	 * Edits zone in GUI table,is Called from outside the GUI thread.
	 * 
	 * @param zone_no
	 *            zone number
	 * @param zone_col
	 *            zone color
	 * @param zone_type
	 *            zone type
	 */
	public void editZoneDataInTable(
			final int zone_no,
			final String zone_col,
			final String zone_type)
	{
		if (Display.getCurrent() != Display.getDefault())
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run()
				{
					guiEditZoneDataInTable(zone_no, zone_col, zone_type);
				}
			});
		else
			guiEditZoneDataInTable(zone_no, zone_col, zone_type);
	}

	/**
	 * Edits zone in GUI table,is Called from within the GUI thread.
	 * 
	 * @param zone_no
	 *            zone number
	 * @param zone_col
	 *            zone color
	 * @param zone_type
	 *            zone type
	 */
	private void guiEditZoneDataInTable(
			final int zone_no,
			final String zone_col,
			final String zone_type)
	{
		final TableItem tmp_ti = getTableItemByNumber(zone_no);
		if (tmp_ti != null)
		{
			tmp_ti.setText(1, zone_type);
			tmp_ti.setText(2, zone_col);
		}
		else
			System.out.print("Tableitem is not found using index: " + zone_no);
	}

	/**
	 * Clears the GUI table, is called from outside the GUI thread.
	 */
	public void clearTable()
	{
		if (Display.getCurrent() != Display.getDefault())
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run()
				{
					guiClearTable();
				}
			});
		else
			guiClearTable();
	}

	/**
	 * Clears the GUI table, is called from within the GUI thread.
	 */
	private void guiClearTable()
	{
		for (final TableItem ti : tbl_zones.getItems())
			ti.dispose();

		tbl_zones.clearAll();
	}

	/**
	 * Adds a new zone to the GUI table, is called from the GUI thread.
	 * 
	 * @param zone_no
	 *            zone number
	 * @param zone_col
	 *            zone color
	 * @param zone_type
	 *            zone type
	 */
	private void guiAddZoneToTable(
			final String zone_no,
			final String zone_col,
			final String zone_type)
	{
		final TableItem tmp_ti = new TableItem(tbl_zones, 0);
		tmp_ti.setText(0, zone_no);
		tmp_ti.setText(1, zone_type);
		tmp_ti.setText(2, zone_col);
	}

	/**
	 * Selects zone in the GUI table.
	 * 
	 * @param zone_number
	 *            number of the zone to select
	 */
	public void selectZoneInTable(final int zone_number)
	{
		if (Display.getCurrent() != Display.getDefault())
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run()
				{
					final TableItem ti = getTableItemByNumber(zone_number);
					if (ti != null)
						tbl_zones.setSelection(ti);
				}
			});
		else
		{
			final TableItem ti = getTableItemByNumber(zone_number);
			if (ti != null)
				tbl_zones.setSelection(ti);
		}
	}

	/**
	 * Gets a tableitem using the zone number.
	 * 
	 * @param zone_number
	 *            zone number to select the table item corresponding to
	 * @return table item having the zone specified by the zone number
	 */
	private TableItem getTableItemByNumber(final int zone_number)
	{
		for (final TableItem ti : tbl_zones.getItems())
		{
			if (ti.getText(0).equals(Integer.toString(zone_number)))
				return ti;

		}
		return null;
	}

	/**
	 * Adds a new measure point. Details: when setting the scale, user needs to
	 * select 2 points on the screen, then writes the real distance between them
	 * in the real world. so, this method adds a new measure point (the first
	 * one or the second) there is a thread responsible of handling the two
	 * points.
	 * 
	 * @param pos
	 *            position of the new point (x,y)
	 */
	public void addMeasurePoint(final Point pos)
	{
		if (measure_pnt1 == null)
			measure_pnt1 = new Point(pos.x, pos.y);
		else
			measure_pnt2 = new Point(pos.x, pos.y);
		PManager.log.print("New Measure point Added: " + pos.x + " " + pos.y, this);
	}

	@Override
	public void loadData(final String[] strArray)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setController(final ControllerUI controller)
	{
		super.setController(controller);
		this.controller = (CtrlDrawZones) controller;
	}

	@Override
	public void clearForm()
	{
		clearTable();
		gfx_panel.clearDrawingArea();
	}

}
