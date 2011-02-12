package control.ui;

import java.awt.Point;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import control.ShapeController;
import control.StatsController;
import control.ZonesController;
import ui.DrawZones;
import utils.PManager;

/**
 * Controller of the DrawZones GUI window
 * @author Creative
 *
 */
public class Ctrl_DrawZones extends ControllerUI {

	private DrawZones ui;
	private ZonesController zone_controller;
	/**
	 * Initializes class attributes (DrawZones , PManager and ZoneController)
	 * then gives the instance of GfxPanel to PManager to share it. 
	 */
	public Ctrl_DrawZones()
	{
		zone_controller=ZonesController.getDefault();
		pm=PManager.getDefault();
		ui=new DrawZones();
		ui.setController(this);
		pm.linkGFXPanelWithShapeCtrlr(ui.getGFXPanel());
	}
	@Override
	public boolean setVars(String[] objs) {
		return true;
	}

	@Override
	public void show(boolean visibility) {
		ui.show(visibility);
	}

	/**
	 * Sends measure points selected by the user along with the real distance
	 * between the two points in reality to the StatsController.
	 * @param measure_pnt1 First point that the user selected
	 * @param measure_pnt2 Second point that the user selected
	 * @param str_real_distance Real distance between the two points
	 */
	public void sendScaletoStatsCtrlr(Point measure_pnt1, Point measure_pnt2, String str_real_distance)
	{
		StatsController.getDefault().setScale(measure_pnt1, measure_pnt2, Integer.parseInt(str_real_distance));
	}

	public void btn_load_zonesAction(Shell shell) {
		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		String file_name=fileDialog.open();
		if(file_name!=null)
			zone_controller.loadZonesFromFile(file_name);
	}
	public void btn_hide_Action() {
		zone_controller.updateZoneMap();
		show(false);		
	}
	public void btn_save_zones_Action(Shell shell) {
		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		String file_name=fileDialog.open();
		if(file_name!=null)
			zone_controller.saveZonesToFile(file_name);
	}
	/**
	 * Gives the measure point to the GUI.
	 * The method call is originated in GfxPanel when the user clicks a new
	 * measure point.
	 * @param pos The new Point (x,y)
	 */
	public void addMeasurePoint(Point pos) {
		ui.addMeasurePoint(pos);
	}
	/**
	 * Clears GUI table
	 */
	public void clearTable() {
		ui.clearTable();
	}
	/**
	 * Updates zone data in the GUI table
	 * @param zonenumber Number of the zone to update its information
	 * @param color the new color of the zone
	 * @param zonetype the new zone type
	 */
	public void editZoneDataInTable(int zonenumber, String color,
			String zonetype) {
		ui.editZoneDataInTable(zonenumber, color, zonetype);		
	}
	/**
	 * Adds the given zone to the GUI table
	 * @param zone_no zone's number
	 * @param zone_col zone's color
	 * @param zone_type zone's type
	 */
	public void addZoneToTable(String zone_no, String zone_col,
			String zone_type) {
		ui.addZoneToTable(zone_no, zone_col, zone_type);
	}
	/**
	 * Selects the zone having "zone_number" in the GUI table
	 * @param zone_number number of the zone to select
	 */
	public void selectZoneInTable(int zone_number) {
		ui.selectZoneInTable(zone_number);		
	}
	public void settingScale(boolean enable) {
		ShapeController.getDefault().setSetting_scale(enable);
	}


}
