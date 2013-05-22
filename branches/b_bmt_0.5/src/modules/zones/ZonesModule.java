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

import java.awt.Point;
import java.util.ArrayList;

import modules.Cargo;
import modules.Module;
import modules.ModuleConfigs;
import modules.ModuleData;
import modules.experiment.Constants;
import modules.experiment.ExperimentType;
import modules.zones.Zone.ZoneType;

import org.eclipse.swt.widgets.Shell;

import utils.Logger.Details;
import utils.PManager;
import utils.StatusManager.StatusSeverity;
import filters.Data;
import filters.ratfinder.RatFinderData;
import gfx_panel.OvalShape;
import gfx_panel.RectangleShape;
import gfx_panel.Shape;

/**
 * Manages zones counters (entrance counters, central time, etc..).
 * 
 * @author Creative
 */
public class ZonesModule extends
		Module<ZonesModuleGUI, ZonesModuleConfigs, ZonesModuleData> {
	public static final int			DEFAULT_HYSTRISES_VALUE	= 50;
	public final static String		moduleID				= Constants.MODULE_ID
																	+ ".zones";
	private long					centralStartTmp;
	private long					centralZoneTimeTmp;
	private Point					currentPosition;
	private final String[]			expParams				= new String[] {
			Constants.FILE_ALL_ENTRANCE, Constants.FILE_CENTRAL_ENTRANCE,
			Constants.FILE_CENTRAL_TIME, Constants.FILE_TOTAL_DISTANCE };
	private final Point				oldPosition;
	private final ArrayList<Point>	path;										// This

	private RatFinderData			ratFinderData;

	private final ShapeController	shapeController;
	private int						updatedZoneNumber;

	private byte[]					zoneMap;

	/**
	 * Initializes the module.
	 * 
	 * @param name
	 *            module instance's name
	 * @param configs
	 *            ZonesModuleConfigs object to configure the module
	 */
	public ZonesModule(final ZonesModuleConfigs configs) {
		super(configs);
		data = new ZonesModuleData();
		oldPosition = new Point();

		filtersData = new Data[1];
		data.setScale(10);
		path = new ArrayList<Point>();
		data.setZones(new ZonesCollection());
		shapeController = ShapeController.getDefault();
		initialize();
		gui = new ZonesModuleGUI(this);
		expType = new ExperimentType[] { ExperimentType.OPEN_FIELD };
		// data.expType=expType;
		// TODO: IMPORTANT update the width & height of the zone_mape when the
		// user changes them.
		// We can make a GLOBAL_CONFIGs object that is accessible everywhere,
		// and
		// modules/filters can REGISTER to it to get NOTIFIED when a value
		// changes,
		// so they can run THEIR update routines.
	}

	/**
	 * Adds all zones in the collectoin to the GUI table.
	 */
	public void addAllZonesToGUI() {
		int zonenumber;
		for (final Zone z : data.getZones().getAllZones())
			if (z != null) {
				zonenumber = z.getZoneNumber();
				PManager.getDefault()
						.getDrawZns()
						.addZoneToTable(
								Integer.toString(zonenumber),
								Shape.color2String(shapeController
										.getShapeByNumber(zonenumber)
										.getColor()),
								ZoneType.zoneType2String(z.getZoneType()));
			}
	}

	/**
	 * Saves the path of the rat in the path array in form of points (x,y).
	 * 
	 * @param pos
	 *            Current rat position
	 */
	private void addPointToPosArray(final Point pos) {
		path.add(new Point(pos.x, pos.y));
	}

	/**
	 * Adds a new zone to the collection.
	 * 
	 * @param zoneNumber
	 *            zone's number
	 * @param type
	 *            zones' type
	 */
	public void addZone(final int zoneNumber, final ZoneType type) {
		data.getZones().addZone(zoneNumber, type);
		updateZoneMap();
	}

	@Override
	public boolean amIReady(final Shell shell) {
		if (PManager.getDefault() != null)
			return true;
		return false;
	}

	@Override
	public void deInitialize() {
/*		for (final Point point : path) {
			// System.out.println(point.x+"\t"+point.y);
		}*/
	}

	/**
	 * Deletes a zone from the collection.
	 * 
	 * @param zoneNumber
	 *            number of the zone to delete
	 */
	public void deleteZone(final int zoneNumber) {
		data.getZones().deleteZone(zoneNumber);
		updateZoneMap();
		PManager.getDefault().getDrawZns().clearTable();
		addAllZonesToGUI();
	}

	@Override
	public void deRegisterDataObject(final Data data) {
		if (ratFinderData == data) {
			ratFinderData = null;
			this.filtersData[0] = null;
			currentPosition = null;
		}
	}

	@Override
	public String getID() {
		return moduleID;
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
		if (x + y * configs.getWidth() < zoneMap.length)
			return zoneMap[x + y * configs.getWidth()];
		return -1;
	}

	/**
	 * Initializes the zone controller instance.
	 */
	@Override
	public void initialize() {
		PManager.log.print("initializing..", this, Details.VERBOSE);
		data.setCurrentZoneNum(-1);
		centralStartTmp = 0;
		data.setCentralFlag(false);
		data.setCentralZoneTime(0);
		updatedZoneNumber = -1;
		data.setAllEntrance(0);
		data.setCentralEntrance(0);
		centralZoneTimeTmp = 0;
		data.setTotalDistance(0);

		path.clear();

		guiCargo = new Cargo(new String[] { Constants.GUI_CURRENT_ZONE,
				Constants.GUI_ALL_ENTRANCE, Constants.GUI_CENTRAL_ENTRANCE,
				Constants.GUI_CENTRAL_TIME, Constants.GUI_TOTAL_DISTANCE });

		fileCargo = new Cargo(expParams);
		for (final String param : expParams)
			data.addParameter(param);

		updateZoneMap();
	}

	/**
	 * Fills the zone map with a given number.
	 * 
	 * @param nullNoneNumber
	 *            Number to fill the zone map array with
	 */
	private void initializeZoneMap(final int nullNoneNumber) {
		for (int i = 0; i < zoneMap.length; i++)
			zoneMap[i] = (byte) nullNoneNumber;
	}

	/**
	 * Loads zones from a text file.
	 * 
	 * @param fileName
	 *            file path to load the zones from
	 */
	public void loadZonesFromFile(final String fileName) {
		data.getZones().loadZonesFromFile(fileName);
	}

	@Override
	public void process() {
		updatedZoneNumber = getZone(currentPosition.x, currentPosition.y);
		zoneHysteresis();
		updateTotalDistance();
		updateCentralZoneTime();
		addPointToPosArray(currentPosition);

		if (currentPosition != null) {
			oldPosition.x = currentPosition.x;
			oldPosition.y = currentPosition.y;
		}
	}

	@Override
	public void registerFilterDataObject(final Data data) {
		if (data instanceof RatFinderData) {
			ratFinderData = (RatFinderData) data;
			this.filtersData[0] = ratFinderData;
			currentPosition = ratFinderData.getCenterPoint();
		}
	}

	@Override
	public void registerModuleDataObject(final ModuleData data) {
		// TODO Auto-generated method stub
	}

	/**
	 * Saves the zones into a text file.
	 * 
	 * @param fileName
	 *            file path of the file to save the zones to
	 */
	public void saveZonesToFile(final String fileName) {
		data.getZones().saveZonesToFile(fileName);
	}

	/**
	 * Selects the zone in the GUI table.
	 * 
	 * @param zoneNumber
	 *            number of the zone to select in the GUI table.
	 */
	public void selectZoneInGUI(final int zoneNumber) {
		PManager.getDefault().getDrawZns().selectZoneInTable(zoneNumber);
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
	 * @param realDistance
	 *            distance entered by the user as real distance
	 */
	public void setScale(final Point p1, final Point p2,
			final float realDistance) {
		final double screenDistance = p1.distance(p2);
		// note: horizontal scale === vertical scale (the cam is perpendicular
		// on the field)

		final double cmResult = screenDistance / realDistance;
		data.setScale((float) cmResult);
	}

	/**
	 * Updates "central zone time" counter , if the rat is in a central zone.
	 */
	private void updateCentralZoneTime() {
		if (data.getZones().getNumberOfZones() != -1)
			if ((data.getCurrentZoneNum() != -1)
					&& (data.getZones().getZoneByNumber(
							data.getCurrentZoneNum()) != null))
				if ((data.getZones().getZoneByNumber(data.getCurrentZoneNum())
						.getZoneType() == ZoneType.CENTRAL_ZONE)
						& !data.isCentralFlag()) {
					centralStartTmp = System.currentTimeMillis();
					data.setCentralFlag(true);
				} else if ((data.getZones()
						.getZoneByNumber(data.getCurrentZoneNum())
						.getZoneType() == ZoneType.CENTRAL_ZONE)
						&& data.isCentralFlag())
					centralZoneTimeTmp = ((System.currentTimeMillis() - centralStartTmp) / 1000L);
				else if ((data.getZones()
						.getZoneByNumber(data.getCurrentZoneNum())
						.getZoneType() != ZoneType.CENTRAL_ZONE)
						&& data.isCentralFlag()) {
					data.setCentralZoneTime((int) (data.getCentralZoneTime() + centralZoneTimeTmp));
					data.setCentralFlag(false);
				}
	}

	@Override
	public void updateConfigs(final ModuleConfigs config) {
		if (config instanceof ZonesModuleConfigs){
			configs.mergeConfigs(config);
			updateZoneMap();
		}
	}

	@Override
	public void updateFileCargoData() {
		fileCargo.setDataByTag(Constants.FILE_ALL_ENTRANCE,
				Integer.toString(data.getAllEntrance()));
		fileCargo.setDataByTag(Constants.FILE_CENTRAL_ENTRANCE,
				Integer.toString(data.getCentralEntrance()));
		fileCargo.setDataByTag(Constants.FILE_CENTRAL_TIME,
				Integer.toString(data.getCentralZoneTime()));
		fileCargo.setDataByTag(Constants.FILE_TOTAL_DISTANCE,
				Long.toString(data.getTotalDistance()));
	}

	@Override
	public void updateGUICargoData() {
		guiCargo.setDataByTag(Constants.GUI_CURRENT_ZONE,
				Integer.toString(data.getCurrentZoneNum()));
		guiCargo.setDataByTag(Constants.GUI_ALL_ENTRANCE,
				Integer.toString(data.getAllEntrance()));
		guiCargo.setDataByTag(Constants.GUI_CENTRAL_ENTRANCE,
				Integer.toString(data.getCentralEntrance()));
		guiCargo.setDataByTag(Constants.GUI_CENTRAL_TIME,
				Integer.toString(data.getCentralZoneTime()));
		guiCargo.setDataByTag(Constants.GUI_TOTAL_DISTANCE,
				Long.toString(data.getTotalDistance()));
	}

	/**
	 * To Calculate the total distance covered by the rat through the
	 * experiment.
	 */
	private void updateTotalDistance() {
		if (oldPosition != null)
			data.setTotalDistance((long) (data.getTotalDistance() + (currentPosition
					.distance(oldPosition) / data.getScale())));
	}

	/**
	 * updates (all zones entrance and central zone entrance) counters.
	 */
	private void updateZoneCounters() {
		data.setCurrentZoneNum(updatedZoneNumber);
		data.setAllEntrance(data.getAllEntrance() + 1);
		if (data.getZones().getZoneByNumber(data.getCurrentZoneNum()) != null)
			if (data.getZones().getZoneByNumber(data.getCurrentZoneNum())
					.getZoneType() == ZoneType.CENTRAL_ZONE)
				data.setCentralEntrance(data.getCentralEntrance() + 1);
	}

	/**
	 * Updates the zone's information in the GUI table.
	 * 
	 * @param zonenumber
	 *            zone number of the zone to update its information in GUI
	 *            table.
	 */
	public void updateZoneDataInGUI(final int zonenumber) {
		final Zone z = data.getZones().getZoneByNumber(zonenumber);
		PManager.getDefault()
				.getDrawZns()
				.editZoneDataInTable(
						zonenumber,
						Shape.color2String(shapeController.getShapeByNumber(
								zonenumber).getColor()),
						ZoneType.zoneType2String(z.getZoneType()));
	}

	/**
	 * Updates the zone map array according to the shapes setup on the GfxPanel
	 * Note: zone map is an array of bytes, we can imagine it as a two dim.
	 * array (width*height) , where each byte contains the number of the zone
	 * existing at that point (x,y)
	 */
	public void updateZoneMap() {
		RectangleShape tmpRect;
		OvalShape tmpOval;
		int tmpZoneNumber;
		zoneMap = new byte[configs.getWidth() * configs.getHeight()];
		initializeZoneMap(-1);
		for (final Zone zone : data.getZones().getAllZones()) {
			tmpZoneNumber = zone.getZoneNumber();
			final Shape tmpShp = shapeController
					.getShapeByNumber(tmpZoneNumber);

			if (tmpShp instanceof RectangleShape) {
				tmpRect = (RectangleShape) tmpShp;
				for (int x = tmpRect.getX(); x < tmpRect.getX()
						+ tmpRect.getWidth(); x++)
					if ((x > -1) & (x < configs.getWidth()))
						for (int y = tmpRect.getY(); y < tmpRect.getY()
								+ tmpRect.getHeight(); y++)
							if ((y > -1) & (y < configs.getHeight()))
								zoneMap[x + (/* configs.height - */y)
										* configs.getWidth()] = (byte) tmpZoneNumber;
			} else if (tmpShp instanceof OvalShape) {
				tmpOval = (OvalShape) tmpShp;
				final int rx = tmpOval.getWidth() / 2, ry = tmpOval.getHeight() / 2, ovX = tmpOval
						.getX() + rx, ovY = tmpOval.getY() + ry;
				float xFinal, yFinal;

				for (int x = tmpOval.getX(); x < tmpOval.getX() + rx * 2; x++)
					if ((x > -1) & (x < configs.getWidth()))
						for (int y = tmpOval.getY(); y < tmpOval.getY() + ry
								* 2; y++)
							if ((y > -1) & (y < configs.getHeight())) {
								xFinal = x - ovX;
								yFinal = y - ovY;
								if ((xFinal * xFinal) / (rx * rx)
										+ (yFinal * yFinal) / (ry * ry) < 1)
									zoneMap[x + (configs.getHeight() - y)
											* configs.getWidth()] = (byte) tmpZoneNumber;
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
		if ((data.getCurrentZoneNum() != updatedZoneNumber)
				& (updatedZoneNumber != -1)) {
			int zoneUpLeft = 0, zoneUpRight = 0, zoneDownLeft = 0, zoneDownRight = 0;
			try {
				zoneUpLeft = getZone(currentPosition.x - configs.getHystValue()
						/ 2, currentPosition.y + configs.getHystValue() / 2);
				zoneUpRight = getZone(
						currentPosition.x + configs.getHystValue() / 2,
						currentPosition.y + configs.getHystValue() / 2);
				zoneDownLeft = getZone(
						currentPosition.x - configs.getHystValue() / 2,
						currentPosition.y - configs.getHystValue() / 2);
				zoneDownRight = getZone(
						currentPosition.x + configs.getHystValue() / 2,
						currentPosition.y - configs.getHystValue() / 2);

			} catch (final Exception e) {
				PManager.log.print("Error fel index .. zoneHysteresis!", this,
						StatusSeverity.ERROR);
			}

			if ((zoneUpLeft != data.getCurrentZoneNum())
					& (zoneUpRight != data.getCurrentZoneNum())
					& (zoneDownLeft != data.getCurrentZoneNum())
					& (zoneDownRight != data.getCurrentZoneNum())) {
				updateZoneCounters();

			}

		}
	}

}