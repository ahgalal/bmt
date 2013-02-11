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

package ui;

import gfx_panel.GfxPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

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
public class DrawZones extends BaseUI {
	private static final int	FRAME_HEIGHT	= 480;
	private static final int	FRAME_WIDTH	= 640;
	private Button			btnHide		= null;
	private Button			btnLoadZones	= null;
	private Button			btnSaveZones	= null;
	private Button			btnSetScale	= null;
	private Composite		composite		= null;

	// boolean is_drawing_now = false;

	private CtrlDrawZones	controller;			// @jve:decl-index=0:

	private GfxPanel		gfxPanel;

	private Point			measurePnt1, measurePnt2; // @jve:decl-index=0:

	private Shell			sShell			= null;	// @jve:decl-index=0:visual-constraint="7,-197"
	private String			strRealDistance;
	private Table			tblZones		= null;

	/**
	 * Creates GUI components, and links this Shell with the parent Shell.
	 */
	public DrawZones() {
		createSShell();
		super.sShell = sShell;
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
	public void addMeasurePoint(final Point pos) {
		if (measurePnt1 == null){
			measurePnt1 = new Point(pos.x, pos.y);
			bgNoScaleMarkers=drawMeasurePoint(pos);
			gfxPanel.refreshDrawingArea();
		}
		else{
			measurePnt2 = new Point(pos.x, pos.y);
			drawMeasurePoint(pos);
			gfxPanel.refreshDrawingArea();
			
			// restore original BG, but don't refresh, to hold the second
			// measure point on screen
			gfxPanel.setBackground(bgNoScaleMarkers);
			bgNoScaleMarkers=null;
		}
		PManager.log.print("New Measure point Added: " + pos.x + " " + pos.y,
				this);
	}

	private int[] drawMeasurePoint(final Point pos) {
		BufferedImage currentBG = gfxPanel.getBGImage();
		int[] srcData = ((DataBufferInt)currentBG.getRaster().getDataBuffer()).getData();
		int[] bgBeforeDrawingMarker = new int[srcData.length];
		System.arraycopy(srcData, 0, bgBeforeDrawingMarker, 0, srcData.length);
		
		// draw marker at click's position
		Graphics graphics = currentBG.getGraphics();
		graphics.setColor(Color.RED);
		graphics.fillOval(pos.x, pos.y, 5, 5);
		
		return bgBeforeDrawingMarker;
	}
	private int[] bgNoScaleMarkers;

	/**
	 * Adds zone to the GUI table.
	 * 
	 * @param zoneNo
	 *            zone number
	 * @param zoneCol
	 *            zone color
	 * @param zoneType
	 *            zone type
	 */
	public void addZoneToTable(final String zoneNo, final String zoneCol,
			final String zoneType) {
		if (Display.getCurrent() != Display.getDefault())
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					guiAddZoneToTable(zoneNo, zoneCol, zoneType);
				}
			});
		else
			guiAddZoneToTable(zoneNo, zoneCol, zoneType);
	}

	@Override
	public void clearForm() {
		clearTable();
		gfxPanel.clearDrawingArea();
	}

	/**
	 * Clears the GUI table, is called from outside the GUI thread.
	 */
	public void clearTable() {
		if (Display.getCurrent() != Display.getDefault())
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					guiClearTable();
				}
			});
		else
			guiClearTable();
	}

	/**
	 * Creates the composite.
	 */
	private void createComposite() {
		composite = new Composite(sShell, SWT.BORDER);
		composite.setLayout(null);
		composite.setBounds(5, 5, FRAME_WIDTH, FRAME_HEIGHT + 35);
		gfxPanel = new GfxPanel(sShell, composite, composite.getSize().x,
				composite.getSize().y);
		gfxPanel.setEnableSnap(true);
	}

	/**
	 * Initializes sShell.
	 */
	private void createSShell() {
		sShell = new Shell(SWT.APPLICATION_MODAL | SWT.ON_TOP | SWT.TITLE);
		sShell.setText("Zone Editor");
		createComposite();
		sShell.setSize(959, 562);
		sShell.setLayout(null);
		btnHide = new Button(sShell, SWT.NONE);
		btnHide.setBounds(new Rectangle(659, 503, 108, 26));
		btnHide.setText("Hide");
		btnSaveZones = new Button(sShell, SWT.NONE);
		btnSaveZones.setText("Save to File");
		btnSaveZones.setBounds(new Rectangle(659, 473, 108, 26));
		btnLoadZones = new Button(sShell, SWT.NONE);
		btnLoadZones.setBounds(new Rectangle(659, 442, 108, 26));
		btnLoadZones.setText("Load from File");
		tblZones = new Table(sShell, SWT.BORDER | SWT.FULL_SELECTION);
		tblZones.setHeaderVisible(true);
		tblZones.setLinesVisible(true);
		tblZones.setBounds(new Rectangle(658, 22, 274, 414));
		btnSetScale = new Button(sShell, SWT.NONE);
		btnSetScale.setBounds(new Rectangle(822, 442, 108, 26));
		btnSetScale.setText("Scaling");
		btnSetScale
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						final MessageBox msgbox = new MessageBox(sShell);
						msgbox.setMessage("Please click on 2 points on the screen , then enter the real distance between them");
						msgbox.setText("Scaling");
						msgbox.open();
						controller.settingScale(true);
						gfxPanel.enableDraw(false);
						final Thread th_2points = new Thread(new Runnable() {
							@Override
							public void run() {
								final InputBox inputboxGetrealDist = new InputBox(
										sShell,
										"Real Distance",
										"Please enter the real distance between the 2 points (in cm):",
										false);
								while ((measurePnt1 == null)
										|| (measurePnt2 == null))
									try {
										Thread.sleep(200);
									} catch (final InterruptedException e) {
										e.printStackTrace();
									}
								Display.getDefault().syncExec(new Runnable() {
									@Override
									public void run() {
										
										strRealDistance = inputboxGetrealDist
												.show();
										gfxPanel.enableDraw(true);
									}
								});
								controller.sendScaletoZonesModule(measurePnt1,
										measurePnt2, strRealDistance);
								measurePnt1 = null;
								measurePnt2 = null;
								controller.settingScale(false);
							}
						},"Scale setting");
						th_2points.start();
					}
				});
		final TableColumn tableColumn = new TableColumn(tblZones, SWT.NONE);
		tableColumn.setWidth(40);
		tableColumn.setText("Num");
		final TableColumn tableColumn1 = new TableColumn(tblZones, SWT.NONE);
		tableColumn1.setWidth(90);
		tableColumn1.setText("Type");
		final TableColumn tableColumn2 = new TableColumn(tblZones, SWT.NONE);
		tableColumn2.setWidth(60);
		tableColumn2.setText("Color");
		btnLoadZones
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						gfxPanel.selectShape(-1);
						controller.btnLoadZonesAction(sShell);
					}
				});
		btnSaveZones
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							final org.eclipse.swt.events.SelectionEvent e) {
						controller.btnSaveZonesAction(sShell);
					}
				});
		btnHide.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					final org.eclipse.swt.events.SelectionEvent e) {
				controller.btnHideAction();
			}
		});

		tblZones.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				gfxPanel.selectShape(Integer.parseInt(tblZones.getItem(
						tblZones.getSelectionIndex()).getText(0)));
			}
		});
	}

	/**
	 * Edits zone in GUI table,is Called from outside the GUI thread.
	 * 
	 * @param zoneNo
	 *            zone number
	 * @param zoneCol
	 *            zone color
	 * @param zoneType
	 *            zone type
	 */
	public void editZoneDataInTable(final int zoneNo, final String zoneCol,
			final String zoneType) {
		if (Display.getCurrent() != Display.getDefault())
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					guiEditZoneDataInTable(zoneNo, zoneCol, zoneType);
				}
			});
		else
			guiEditZoneDataInTable(zoneNo, zoneCol, zoneType);
	}

	/**
	 * Gets the GFXPanel instance.
	 * 
	 * @return GFXPanel instance
	 */
	public GfxPanel getGFXPanel() {
		return gfxPanel;
	}

	/**
	 * Gets a tableitem using the zone number.
	 * 
	 * @param zoneNumber
	 *            zone number to select the table item corresponding to
	 * @return table item having the zone specified by the zone number
	 */
	private TableItem getTableItemByNumber(final int zoneNumber) {
		for (final TableItem ti : tblZones.getItems())
			if (ti.getText(0).equals(Integer.toString(zoneNumber)))
				return ti;
		return null;
	}

	/**
	 * Adds a new zone to the GUI table, is called from the GUI thread.
	 * 
	 * @param zoneNo
	 *            zone number
	 * @param zoneCol
	 *            zone color
	 * @param zoneType
	 *            zone type
	 */
	private void guiAddZoneToTable(final String zoneNo, final String zoneCol,
			final String zoneType) {
		final TableItem tmpTi = new TableItem(tblZones, 0);
		tmpTi.setText(0, zoneNo);
		tmpTi.setText(1, zoneType);
		tmpTi.setText(2, zoneCol);
	}

	/**
	 * Clears the GUI table, is called from within the GUI thread.
	 */
	private void guiClearTable() {
		for (final TableItem ti : tblZones.getItems())
			ti.dispose();

		tblZones.clearAll();
	}

	/**
	 * Edits zone in GUI table,is Called from within the GUI thread.
	 * 
	 * @param zoneNo
	 *            zone number
	 * @param zoneCol
	 *            zone color
	 * @param zoneType
	 *            zone type
	 */
	private void guiEditZoneDataInTable(final int zoneNo,
			final String zoneCol, final String zoneType) {
		final TableItem tmpTi = getTableItemByNumber(zoneNo);
		if (tmpTi != null) {
			tmpTi.setText(1, zoneType);
			tmpTi.setText(2, zoneCol);
		} else
			System.out.print("Tableitem is not found using index: " + zoneNo);
	}

	@Override
	public void loadData(final String[] strArray) {
		// TODO Auto-generated method stub

	}

	/**
	 * Selects zone in the GUI table.
	 * 
	 * @param zoneNumber
	 *            number of the zone to select
	 */
	public void selectZoneInTable(final int zoneNumber) {
		if (Display.getCurrent() != Display.getDefault())
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					final TableItem ti = getTableItemByNumber(zoneNumber);
					if (ti != null)
						tblZones.setSelection(ti);
				}
			});
		else {
			final TableItem ti = getTableItemByNumber(zoneNumber);
			if (ti != null)
				tblZones.setSelection(ti);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setController(final ControllerUI controller) {
		super.setController(controller);
		this.controller = (CtrlDrawZones) controller;
	}

	@Override
	public void show(final boolean visibility) {
		super.show(visibility);
		if (visibility)
			gfxPanel.redrawAllShapes();
	}

}
