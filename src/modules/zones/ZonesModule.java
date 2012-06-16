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

package modules.zones;

import gfx_panel.OvalShape;
import gfx_panel.RectangleShape;
import gfx_panel.Shape;

import java.awt.Point;
import java.util.ArrayList;

import modules.Cargo;
import modules.Module;
import modules.ModuleConfigs;
import modules.experiment.Constants;
import modules.zones.Zone.ZoneType;

import org.eclipse.swt.widgets.Shell;

import utils.Logger.Details;
import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.Data;
import utils.video.filters.ratfinder.RatFinderData;

/**
 * Manages zones counters (entrance counters, central time, etc..).
 * 
 * @author Creative
 */
public class ZonesModule extends
	Module<ZonesModuleGUI, ZonesModuleConfigs, ZonesModuleData> {
    private final ShapeController shape_controller;
    private byte[] zone_map;
    private long central_start_tmp;
    private int updated_zone_number;
    private long central_zone_time_tmp;
    private Point current_position;
    private final Point old_position;

    private final ArrayList<Point> arr_path; // This array will hold the
    // positions of
    // the object through the whole
    // experiment

    private RatFinderData rat_finder_data;

    /**
     * Initializes the module.
     * 
     * @param name
     *            module instance's name
     * @param configs
     *            ZonesModuleConfigs object to configure the module
     */
    public ZonesModule(final String name, final ZonesModuleConfigs configs) {
	super(name, configs);
	data = new ZonesModuleData("Zones Module Data");
	old_position = new Point();

	filters_data = new Data[1];
	data.scale = 10;
	arr_path = new ArrayList<Point>();
	data.zones = new ZonesCollection();
	shape_controller = ShapeController.getDefault();
	initialize();
	gui = new ZonesModuleGUI(this);

	// TODO: IMPORTANT update the width & height of the zone_mape when the
	// user changes them.
	// We can make a GLOBAL_CONFIGs object that is accessible everywhere,
	// and
	// modules/filters can REGISTER to it to get NOTIFIED when a value
	// changes,
	// so they can run THEIR update routines.
    }

    /**
     * Gets the zone at the position x,y.
     * 
     * @param x
     *            x co-ordinate of the point
     * @param y
     *            y co-ordinate of the point
     * @return zone's number located at the pixel of x,y
     */
    private int getZone(final int x, final int y) {
	return zone_map[x + y * configs.width];
    }

    /**
     * Fills the zone map with a given number.
     * 
     * @param null_zone_number
     *            Number to fill the zone map array with
     */
    private void initializeZoneMap(final int null_zone_number) {
	for (int i = 0; i < zone_map.length; i++)
	    zone_map[i] = (byte) null_zone_number;
    }

    /**
     * Updates the zone map array according to the shapes setup on the GfxPanel
     * Note: zone map is an array of bytes, we can imagine it as a two dim.
     * array (width*height) , where each byte contains the number of the zone
     * existing at that point (x,y)
     */
    public void updateZoneMap() {
	RectangleShape tmp_rect;
	OvalShape tmp_oval;
	int tmp_zone_number;
	zone_map = new byte[configs.width * configs.height];
	initializeZoneMap(-1);
	for (int i = 0; i < data.zones.getNumberOfZones(); i++) {
	    tmp_zone_number = data.zones.getZoneByNumber(i).getZoneNumber();
	    final Shape tmp_shp = shape_controller
		    .getShapeByNumber(tmp_zone_number);

	    if (tmp_shp instanceof RectangleShape) {
		tmp_rect = (RectangleShape) tmp_shp;
		for (int x = tmp_rect.getX(); x < tmp_rect.getX()
			+ tmp_rect.getWidth(); x++)
		    if (x > -1 & x < configs.width)
			for (int y = tmp_rect.getY(); y < tmp_rect.getY()
				+ tmp_rect.getHeight(); y++)
			    if (y > -1 & y < configs.height)
				zone_map[x + (configs.height - y)
					* configs.width] = (byte) tmp_zone_number;
	    } else if (tmp_shp instanceof OvalShape) {
		tmp_oval = (OvalShape) tmp_shp;
		final int rx = tmp_oval.getWidth() / 2, ry = tmp_oval
			.getHeight() / 2, x_ov = tmp_oval.getX() + rx, y_ov = tmp_oval
			.getY() + ry;
		float x_final, y_final;

		for (int x = tmp_oval.getX(); x < tmp_oval.getX() + rx * 2; x++)
		    if (x > -1 & x < configs.width)
			for (int y = tmp_oval.getY(); y < tmp_oval.getY() + ry
				* 2; y++)
			    if (y > -1 & y < configs.height) {
				x_final = x - x_ov;
				y_final = y - y_ov;
				if ((x_final * x_final) / (rx * rx)
					+ (y_final * y_final) / (ry * ry) < 1)
				    zone_map[x + (configs.height - y)
					    * configs.width] = (byte) tmp_zone_number;
			    }
	    }
	}
    }

    /**
     * Calculates the new zone number , based on the rat's position. Uses a
     * hysteresis function. Example: when the rat moves from zone1 to zone2, it
     * will pass on the boundary between the two zones before entering zone2.
     * the rat is not considered to be in zone2 until it covers certain distance
     * in zone2 away from the boundary.(hysteresis distance) this strategy is
     * used to prevent fluctuations in the total zone entrance counter, in case
     * of the rat staying on the boundary for some time (its detected position
     * fluctuates, so, the zone detection should have immunity against these
     * fluctuations)
     */
    private void zoneHysteresis() {
	if (data.current_zone_num != updated_zone_number
		& updated_zone_number != -1) {
	    int zone_up_left = 0, zone_up_right = 0, zone_down_left = 0, zone_down_right = 0;
	    try {
		zone_up_left = getZone(current_position.x - configs.hyst_value
			/ 2, current_position.y + configs.hyst_value / 2);
		zone_up_right = getZone(current_position.x + configs.hyst_value
			/ 2, current_position.y + configs.hyst_value / 2);
		zone_down_left = getZone(current_position.x
			- configs.hyst_value / 2, current_position.y
			- configs.hyst_value / 2);
		zone_down_right = getZone(current_position.x
			+ configs.hyst_value / 2, current_position.y
			- configs.hyst_value / 2);
	    } catch (final Exception e) {
		PManager.log.print("Error fel index .. zoneHysteresis!", this,
			StatusSeverity.ERROR);
	    }
	    if (zone_up_left != data.current_zone_num
		    & zone_up_right != data.current_zone_num
		    & zone_down_left != data.current_zone_num
		    & zone_down_right != data.current_zone_num)
		updateZoneCounters();
	}
    }

    /**
     * updates (all zones entrance and central zone entrance) counters.
     */
    private void updateZoneCounters() {
	data.current_zone_num = updated_zone_number;
	data.all_entrance++;
	if (data.zones.getZoneByNumber(data.current_zone_num) != null)
	    if (data.zones.getZoneByNumber(data.current_zone_num).getZoneType() == ZoneType.CENTRAL_ZONE)
		data.central_entrance++;
    }

    /**
     * Updates "central zone time" counter , if the rat is in a central zone.
     */
    private void updateCentralZoneTime() {
	if (data.zones.getNumberOfZones() != -1)
	    if (data.current_zone_num != -1
		    && data.zones.getZoneByNumber(data.current_zone_num) != null)
		if (data.zones.getZoneByNumber(data.current_zone_num)
			.getZoneType() == ZoneType.CENTRAL_ZONE
			& !data.central_flag) {
		    central_start_tmp = System.currentTimeMillis();
		    data.central_flag = true;
		} else if (data.zones.getZoneByNumber(data.current_zone_num)
			.getZoneType() == ZoneType.CENTRAL_ZONE
			&& data.central_flag)
		    central_zone_time_tmp = ((System.currentTimeMillis() - central_start_tmp) / 1000);
		else if (data.zones.getZoneByNumber(data.current_zone_num)
			.getZoneType() != ZoneType.CENTRAL_ZONE
			&& data.central_flag) {
		    data.central_zone_time += central_zone_time_tmp;
		    data.central_flag = false;
		}
    }

    /**
     * Updates the zone's information in the GUI table.
     * 
     * @param zonenumber
     *            zone number of the zone to update its information in GUI
     *            table.
     */
    public void updateZoneDataInGUI(final int zonenumber) {
	final Zone z = data.zones.getZoneByNumber(zonenumber);
	PManager.getDefault().drw_zns
		.editZoneDataInTable(
			zonenumber,
			Shape.color2String(shape_controller.getShapeByNumber(
				zonenumber).getColor()),
			ZoneType.zoneType2String(z.getZoneType()));
    }

    /**
     * Adds all zones in the collectoin to the GUI table.
     */
    public void addAllZonesToGUI() {
	int zonenumber;
	for (final Zone z : data.zones.getAllZones())
	    if (z != null) {
		zonenumber = z.getZoneNumber();
		PManager.getDefault().drw_zns.addZoneToTable(
			Integer.toString(zonenumber),
			Shape.color2String(shape_controller.getShapeByNumber(
				zonenumber).getColor()),
			ZoneType.zoneType2String(z.getZoneType()));
	    }
    }

    /**
     * Adds a new zone to the collection.
     * 
     * @param zone_number
     *            zone's number
     * @param type
     *            zones' type
     */
    public void addZone(final int zone_number, final ZoneType type) {
	data.zones.addZone(zone_number, type);
	updateZoneMap();
    }

    /**
     * Selects the zone in the GUI table.
     * 
     * @param zone_number
     *            number of the zone to select in the GUI table.
     */
    public void selectZoneInGUI(final int zone_number) {
	PManager.getDefault().drw_zns.selectZoneInTable(zone_number);
    }

    /**
     * Gets the number of all zones entrance (when the object moves from one
     * zone to another, this counter is incremented).
     * 
     * @return number of all zones entrances
     */
    public int getAllEntrance() {
	return data.all_entrance;
    }

    /**
     * Gets the number of central zones entrances.
     * 
     * @return number of central zones entrances
     */
    public int getCentralEntrance() {
	return data.central_entrance;
    }

    /**
     * Gets the zone number of the zone which the object is in.
     * 
     * @return zone number of the current zone
     */
    public int getCurrentZoneNumber() {
	return data.current_zone_num;
    }

    /**
     * Gets totoal time spent in the central zones_module_data.zones.
     * 
     * @return total time spent in the central zones
     */
    public float getCentralTime() {
	return data.central_zone_time;
    }

    /**
     * Saves the path of the rat in the path array in form of points (x,y).
     * 
     * @param pos
     *            Current rat position
     */
    private void addPointToPosArray(final Point pos) {
	// TODO: implement to add the functionality of path recording.
	// we need to add some tolerance due to noise
	// path.add(new Point(pos.x,pos.y));
    }

    /**
     * Initializes the zone controller instance.
     */
    @Override
    public void initialize() {
	PManager.log.print("initializing..", this, Details.VERBOSE);
	data.current_zone_num = -1;
	central_start_tmp = 0;
	data.central_flag = false;
	data.central_zone_time = 0;
	updated_zone_number = -1;
	data.all_entrance = 0;
	data.central_entrance = 0;
	central_zone_time_tmp = 0;
	data.total_distance = 0;

	arr_path.clear();

	guiCargo = new Cargo(new String[] { Constants.GUI_CURRENT_ZONE,
		Constants.GUI_ALL_ENTRANCE, Constants.GUI_CENTRAL_ENTRANCE,
		Constants.GUI_CENTRAL_TIME, Constants.GUI_TOTAL_DISTANCE });

	fileCargo = new Cargo(new String[] { Constants.FILE_ALL_ENTRANCE,
		Constants.FILE_CENTRAL_ENTRANCE, Constants.FILE_CENTRAL_TIME,
		Constants.FILE_TOTAL_DISTANCE });

	zone_map = new byte[configs.width * configs.height];
	updateZoneMap();
    }

    @Override
    public void process() {
	updated_zone_number = getZone(current_position.x, current_position.y);
	zoneHysteresis();
	updateTotalDistance();
	updateCentralZoneTime();
	addPointToPosArray(current_position);

	if (current_position != null) {
	    old_position.x = current_position.x;
	    old_position.y = current_position.y;
	}
    }

    @Override
    public void updateGUICargoData() {
	guiCargo.setDataByTag(Constants.GUI_CURRENT_ZONE,
		Integer.toString(data.current_zone_num));
	guiCargo.setDataByTag(Constants.GUI_ALL_ENTRANCE,
		Integer.toString(data.all_entrance));
	guiCargo.setDataByTag(Constants.GUI_CENTRAL_ENTRANCE,
		Integer.toString(data.central_entrance));
	guiCargo.setDataByTag(Constants.GUI_CENTRAL_TIME,
		Integer.toString(data.central_zone_time));
	guiCargo.setDataByTag(Constants.GUI_TOTAL_DISTANCE,
		Long.toString(data.total_distance));
    }

    @Override
    public void updateFileCargoData() {
	fileCargo.setDataByTag(Constants.FILE_ALL_ENTRANCE,
		Integer.toString(data.all_entrance));
	fileCargo.setDataByTag(Constants.FILE_CENTRAL_ENTRANCE,
		Integer.toString(data.central_entrance));
	fileCargo.setDataByTag(Constants.FILE_CENTRAL_TIME,
		Integer.toString(data.central_zone_time));
	fileCargo.setDataByTag(Constants.FILE_TOTAL_DISTANCE,
		Long.toString(data.total_distance));
    }

    @Override
    public void updateConfigs(final ModuleConfigs config) {
	if (config instanceof ZonesModuleConfigs)
	    configs.mergeConfigs(config);
    }

    @Override
    public void registerFilterDataObject(final Data data) {
	if (data instanceof RatFinderData) {
	    rat_finder_data = (RatFinderData) data;
	    this.filters_data[0] = rat_finder_data;
	    current_position = rat_finder_data.getCenterPoint();
	}
    }

    @Override
    public void deInitialize() {

    }

    /**
     * Gets total distance covered by the object.
     * 
     * @return total distance covered by the object
     */
    public long getTotalDistance() {
	return data.total_distance;
    }

    /**
     * To Calculate the total distance covered by the rat through the
     * experiment.
     */
    private void updateTotalDistance() {
	if (old_position != null)
	    data.total_distance += current_position.distance(old_position)
		    / data.scale;
    }

    /**
     * Calculates the scale between real world and the cam image. using the
     * distance between two points on the screen and the distance between them
     * in real image.
     * 
     * @param p1
     *            First point of measurement
     * @param p2
     *            Second point of measurement
     * @param real_distance
     *            distance entered by the user as real distance
     */
    public void setScale(final Point p1, final Point p2,
	    final float real_distance) {
	final double screen_distance = p1.distance(p2);
	// note: horizontal scale === vertical scale (the cam is perpendicular
	// on the field)

	final double cmResult = screen_distance / real_distance;
	data.scale = (float) cmResult;
    }

    @Override
    public void deRegisterDataObject(final Data data) {
	if (rat_finder_data == data) {
	    rat_finder_data = null;
	    this.filters_data[0] = null;
	    current_position = null;
	}
    }

    /**
     * Deletes a zone from the collection.
     * 
     * @param zoneNumber
     *            number of the zone to delete
     */
    public void deleteZone(final int zoneNumber) {
	data.zones.deleteZone(zoneNumber);
	updateZoneMap();
	PManager.getDefault().drw_zns.clearTable();
	addAllZonesToGUI();
    }

    /**
     * Loads zones from a text file.
     * 
     * @param fileName
     *            file path to load the zones from
     */
    public void loadZonesFromFile(final String fileName) {
	data.zones.loadZonesFromFile(fileName);
    }

    /**
     * Saves the zones into a text file.
     * 
     * @param fileName
     *            file path of the file to save the zones to
     */
    public void saveZonesToFile(final String fileName) {
	data.zones.saveZonesToFile(fileName);
    }

    @Override
    public boolean amIReady(final Shell shell) {
	if (PManager.getDefault() != null)
	    return true;
	return false;
    }

    @Override
    public void registerModuleDataObject(final Data data) {
	// TODO Auto-generated method stub
    }

}
