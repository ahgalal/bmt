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
import modules.ModuleData;
import modules.experiment.Constants;
import modules.experiment.ExperimentType;
import modules.zones.Zone.ZoneType;

import org.eclipse.swt.widgets.Shell;

import utils.Logger.Details;
import utils.PManager;
import utils.StatusManager.StatusSeverity;
import filters.Data;
import filters.FilterData;
import filters.FilterManager;
import filters.headangle.HeadAngleData;
import filters.ratfinder.RatFinderData;
import filters.zonesdrawer.ZonesDrawerConfigs;
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
	private static final int		DEFAULT_CANVAS_HEIGHT	= 480;
	private static final int		DEFAULT_CANVAS_WIDTH	= 640;
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

	private FilterData				ratFinderData;

	private final ShapeController	shapeController;
	private int						updatedZoneNumber;

	private byte[]					zoneMap;
	private final ZonesCollection	zones;

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
		data = new ZonesModuleData();
		oldPosition = new Point();

		filtersData = new Data[1];
		data.setScale(10);
		path = new ArrayList<Point>();
		shapeController = new ShapeController(this);
		zones = new ZonesCollection(this, shapeController);
		initialize();
		gui = new ZonesModuleGUI(this);
		expType = new ExperimentType[] { ExperimentType.OPEN_FIELD,
				ExperimentType.PARKINSON };
	}

	/**
	 * Adds all zones in the collectoin to the GUI table.
	 */
	public void addAllZonesToGUI() {
		int zonenumber;
		for (final Zone z : zones.getAllZones())
			if (z != null) {
				zonenumber = z.getZoneNumber();
				gui.addZoneToTable(
						Integer.toString(zonenumber),
						Shape.color2String(shapeController.getShapeByNumber(
								zonenumber).getColor()),
						ZoneType.zoneType2String(z.getZoneType()));
			}
	}

	public void addMeasurePoint(final Point pos) {
		gui.addMeasurePoint(pos);
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

		zones.addZone(zoneNumber, type);
		gui.addZoneToTable(Integer.toString(zoneNumber), Shape
				.color2String(shapeController.getShapeByNumber(zoneNumber)
						.getColor()), ZoneType.zoneType2String(type));
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
		/*
		 * for (final Point point : path) { //
		 * System.out.println(point.x+"\t"+point.y); }
		 */
	}

	/**
	 * Deletes a zone from the collection.
	 * 
	 * @param zoneNumber
	 *            number of the zone to delete
	 */
	public void deleteZone(final int zoneNumber) {
		zones.deleteZone(zoneNumber);
		updateZoneMap();
		gui.clearTable();
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

	public void editZone(final int zonenumber, final ZoneType zonetype) {
		zones.editZone(zonenumber, zonetype);
		gui.editZoneDataInTable(zonenumber, Shape.color2String(shapeController
				.getShapeByNumber(zonenumber).getColor()), ZoneType
				.zoneType2String(zonetype));
	}

	@Override
	public void filterConfiguration() {
		super.filterConfiguration();
		FilterManager.getDefault().applyConfigsToFilter(
				new ZonesDrawerConfigs("ZonesDrawer", null, shapeController));
	}

	@Override
	public String getID() {
		return moduleID;
	}

	public ShapeController getShapeController() {
		return shapeController;
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
//		if ((x + (y * configs.getCommonConfigs().getWidth())) < zoneMap.length)
//			return zoneMap[x + (y * configs.getCommonConfigs().getWidth())];
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

		if (configs != null)
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
		gui.clearTable();
		zones.loadZonesFromFile(fileName);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Module newInstance(final String name) {
		return new ZonesModule(name, null);
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
		// if (data instanceof RatFinderData) {
		if (data instanceof RatFinderData) {
			ratFinderData = (RatFinderData) data;
			currentPosition = ((RatFinderData) ratFinderData).getCenterPoint();
		} else if (data instanceof HeadAngleData) {
			ratFinderData = (HeadAngleData) data;
			currentPosition = ((HeadAngleData) ratFinderData).getCenterPoint();
		}

		this.filtersData[0] = ratFinderData;

	}

	@Override
	public void registerModuleDataObject(final ModuleData data) {
	}

	/**
	 * Saves the zones into a text file.
	 * 
	 * @param fileName
	 *            file path of the file to save the zones to
	 */
	public void saveZonesToFile(final String fileName) {
		zones.saveZonesToFile(fileName);
	}

	/**
	 * Selects the zone in the GUI table.
	 * 
	 * @param zoneNumber
	 *            number of the zone to select in the GUI table.
	 */
	public void selectZoneInGUI(final int zoneNumber) {
		gui.selectZoneInTable(zoneNumber);
	}

	public void setBackground(final int[] updateRGBBackground, final int x,
			final int y) {
		gui.setBackground(updateRGBBackground, x, y);
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
		if (zones.getNumberOfZones() != -1)
			if ((data.getCurrentZoneNum() != -1)
					&& (zones.getZoneByNumber(data.getCurrentZoneNum()) != null))
				if ((zones.getZoneByNumber(data.getCurrentZoneNum())
						.getZoneType() == ZoneType.CENTRAL_ZONE)
						& !data.isCentralFlag()) {
					centralStartTmp = System.currentTimeMillis();
					data.setCentralFlag(true);
				} else if ((zones.getZoneByNumber(data.getCurrentZoneNum())
						.getZoneType() == ZoneType.CENTRAL_ZONE)
						&& data.isCentralFlag())
					centralZoneTimeTmp = ((System.currentTimeMillis() - centralStartTmp) / 1000L);
				else if ((zones.getZoneByNumber(data.getCurrentZoneNum())
						.getZoneType() != ZoneType.CENTRAL_ZONE)
						&& data.isCentralFlag()) {
					data.setCentralZoneTime((int) (data.getCentralZoneTime() + centralZoneTimeTmp));
					data.setCentralFlag(false);
				}
	}

	@Override
	public void updateConfigs(final ZonesModuleConfigs config) {
		super.updateConfigs(config);
		updateZoneMap();
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
				Integer.toString(data.getCentralZoneTime()) + " s");
		guiCargo.setDataByTag(Constants.GUI_TOTAL_DISTANCE,
				Long.toString(data.getTotalDistance()) + " cm");
	}

	/**
	 * To Calculate the total distance covered by the rat through the
	 * experiment.
	 */
	private void updateTotalDistance() {
		if (oldPosition != null) {
			final int distanceX = ((currentPosition.x - oldPosition.x) * DEFAULT_CANVAS_WIDTH)
					/ configs.getCommonConfigs().getWidth();
			final int distanceY = ((currentPosition.y - oldPosition.y) * DEFAULT_CANVAS_HEIGHT)
					/ configs.getCommonConfigs().getHeight();

			final double distance = Math.sqrt(Math.pow(distanceX, 2)
					+ Math.pow(distanceY, 2));
			final long totalDistance = (long) (data.getTotalDistance() + (distance / data
					.getScale()));
			data.setTotalDistance(totalDistance);
		}
	}

	/**
	 * updates (all zones entrance and central zone entrance) counters.
	 */
	private void updateZoneCounters() {
		data.setCurrentZoneNum(updatedZoneNumber);
		data.setAllEntrance(data.getAllEntrance() + 1);
		if (zones.getZoneByNumber(data.getCurrentZoneNum()) != null)
			if (zones.getZoneByNumber(data.getCurrentZoneNum()).getZoneType() == ZoneType.CENTRAL_ZONE)
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
		final Zone z = zones.getZoneByNumber(zonenumber);
		gui.editZoneDataInTable(zonenumber, Shape.color2String(shapeController
				.getShapeByNumber(zonenumber).getColor()), ZoneType
				.zoneType2String(z.getZoneType()));
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
		final int width = configs.getCommonConfigs().getWidth();
		final int height = configs.getCommonConfigs().getHeight();
		zoneMap = new byte[width * height];
		initializeZoneMap(-1);
		for (final Zone zone : zones.getAllZones()) {
			tmpZoneNumber = zone.getZoneNumber();
			final Shape tmpShp = shapeController
					.getShapeByNumber(tmpZoneNumber);

			if (tmpShp instanceof RectangleShape) {
				tmpRect = (RectangleShape) tmpShp;
				final int zoneStartX = (tmpRect.getX() * width)
						/ DEFAULT_CANVAS_WIDTH;
				final int zoneEndX = zoneStartX
						+ ((tmpRect.getWidth() * width) / DEFAULT_CANVAS_WIDTH);
				final int zoneStartY = (tmpRect.getY() * height)
						/ DEFAULT_CANVAS_HEIGHT;
				final int zoneEndY = zoneStartY
						+ ((tmpRect.getHeight() * height) / DEFAULT_CANVAS_HEIGHT);
				for (int x = zoneStartX; x < zoneEndX; x++)
					if ((x > -1) & (x < width)) {
						for (int y = zoneStartY; y < zoneEndY; y++)
							if ((y > -1) & (y < height))
								zoneMap[x + (y * width)] = (byte) tmpZoneNumber;
					}
			} else if (tmpShp instanceof OvalShape) {
				tmpOval = (OvalShape) tmpShp;
				final int radiusX = (tmpOval.getWidth() * width)
						/ DEFAULT_CANVAS_WIDTH / 2;
				final int radiusY = (tmpOval.getHeight() * height)
						/ DEFAULT_CANVAS_HEIGHT / 2;

				final int centerX = tmpOval.getX() + radiusX;
				final int centerY = tmpOval.getY() + radiusY;

				float xRelToCenter, yRelToCenter;

				for (int x = tmpOval.getX(); x < (tmpOval.getX() + (radiusX * 2)); x++)

					if ((x > -1) & (x < width)) {
						xRelToCenter = x - centerX;
						for (int y = tmpOval.getY(); y < (tmpOval.getY() + (radiusY * 2)); y++)
							if ((y > -1) & (y < height)) {
								yRelToCenter = y - centerY;
								/**@formatter:off
								 * ellipse eq:
								 *      x*x       y*y
								 *    ------- + ------- = 1
								 *     rx*rx     ry*ry
								 */
								//@formatter:on
								if ((((xRelToCenter * xRelToCenter) / (radiusX * radiusX)) + ((yRelToCenter * yRelToCenter) / (radiusY * radiusY))) < 1)
									zoneMap[x + (y * width)] = (byte) tmpZoneNumber;
							}
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
				zoneUpLeft = getZone(
						currentPosition.x - (configs.getHystValue() / 2),
						currentPosition.y + (configs.getHystValue() / 2));
				zoneUpRight = getZone(
						currentPosition.x + (configs.getHystValue() / 2),
						currentPosition.y + (configs.getHystValue() / 2));
				zoneDownLeft = getZone(
						currentPosition.x - (configs.getHystValue() / 2),
						currentPosition.y - (configs.getHystValue() / 2));
				zoneDownRight = getZone(
						currentPosition.x + (configs.getHystValue() / 2),
						currentPosition.y - (configs.getHystValue() / 2));

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
