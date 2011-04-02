package modules;

import java.awt.Point;
import java.util.ArrayList;

import model.Zone.ZoneType;
import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.Data;
import utils.video.filters.RatFinder.RatFinderData;
import control.ZonesController;

/**
 * Manages zones counters (entrance counters, central time, etc..)
 * 
 * @author Creative
 */
public class ZonesModule extends Module
{

	private static final String GUI_CURRENT_ZONE = "Current Zone";
	private static final String GUI_ALL_ENTRANCE = "All Entrance";
	private static final String GUI_CENTRAL_ENTRANCE = "Central Entrance";
	private static final String GUI_CENTRAL_TIME = "Central Time";
	private static final String GUI_TOTAL_DISTANCE = "Total Distance";
	private static final String FILE_ALL_ENTRANCE = "AZE";
	private static final String FILE_CENTRAL_ENTRANCE = "CZE";
	private static final String FILE_CENTRAL_TIME = "CT";
	private static final String FILE_TOTAL_DISTANCE = "Distance";
	
	private int current_zone_num;
	private final ZonesController zone_controller;
	private long central_start_tmp;
	private boolean central_flag;
	private int updated_zone_number;
	private int all_entrance, central_entrance;
	private long central_zone_time_tmp;
	private int central_zone_time;
	private Point current_position;
	private final Point old_position;
	private long total_distance;
	private float scale;
	private final ArrayList<Point> arr_path; // This array will hold the
	// positions of
	// the object through the whole
	// experiment

	private RatFinderData rat_finder_data;

	private final ZonesModuleConfigs zones_configs;

	/**
	 * Initializes the module.
	 * 
	 * @param name
	 *            module instance's name
	 * @param configs
	 *            ZonesModuleConfigs object to configure the module
	 */
	public ZonesModule(final String name, final ZonesModuleConfigs configs)
	{
		super(name, configs);
		old_position = new Point();
		zones_configs = configs;

		data = new Data[1];
		zone_controller = ZonesController.getDefault();
		scale = 10;
		arr_path = new ArrayList<Point>();
		initialize();
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
	private void zoneHysteresis()
	{
		if (current_zone_num != updated_zone_number & updated_zone_number != -1)
		{
			int zone_up_left = 0, zone_up_right = 0, zone_down_left = 0, zone_down_right = 0;
			try
			{
				zone_up_left = zone_controller.getZone(current_position.x
						- zones_configs.hyst_value
						/ 2, current_position.y + zones_configs.hyst_value / 2);
				zone_up_right = zone_controller.getZone(current_position.x
						+ zones_configs.hyst_value
						/ 2, current_position.y + zones_configs.hyst_value / 2);
				zone_down_left = zone_controller.getZone(current_position.x
						- zones_configs.hyst_value
						/ 2, current_position.y - zones_configs.hyst_value / 2);
				zone_down_right = zone_controller.getZone(current_position.x
						+ zones_configs.hyst_value
						/ 2, current_position.y - zones_configs.hyst_value / 2);
			} catch (final Exception e)
			{
				PManager.log.print(
						"Error fel index .. zoneHysteresis!",
						this,
						StatusSeverity.ERROR);
			}
			if (zone_up_left != current_zone_num
					& zone_up_right != current_zone_num
					& zone_down_left != current_zone_num
					& zone_down_right != current_zone_num) // we are in a new
			// zone :)
			{
				updateZoneCounters();
			}
		}
	}

	/**
	 * updates (all zones entrance and central zone entrance) counters.
	 */
	private void updateZoneCounters()
	{
		current_zone_num = updated_zone_number;
		all_entrance++;
		if (zone_controller.getZoneByNumber(current_zone_num) != null)
			if (zone_controller.getZoneByNumber(current_zone_num).getZoneType() == ZoneType.CENTRAL_ZONE)
				central_entrance++;
	}

	/**
	 * Updates "central zone time" counter , if the rat is in a central zone.
	 */
	private void updateCentralZoneTime()
	{
		if (zone_controller.getNumberOfZones() != -1)
		{
			if (current_zone_num != -1
					& zone_controller.getZoneByNumber(current_zone_num) != null)
				if (zone_controller.getZoneByNumber(current_zone_num).getZoneType() == ZoneType.CENTRAL_ZONE
						& central_flag == false)
				{
					central_start_tmp = System.currentTimeMillis();
					central_flag = true;
				} else if (zone_controller.getZoneByNumber(current_zone_num)
						.getZoneType() == ZoneType.CENTRAL_ZONE
						&& central_flag == true)
					central_zone_time_tmp = ((System.currentTimeMillis() - central_start_tmp) / 1000);
				else if (zone_controller.getZoneByNumber(current_zone_num).getZoneType() != ZoneType.CENTRAL_ZONE
						&& central_flag == true)
				{
					central_zone_time += central_zone_time_tmp;
					central_flag = false;
				}
		}
	}

	/**
	 * Gets the number of all zones entrance (when the object moves from one
	 * zone to another, this counter is incremented).
	 * 
	 * @return number of all zones entrances
	 */
	public int getAllEntrance()
	{
		return all_entrance;
	}

	/**
	 * Gets the number of central zones entrances.
	 * 
	 * @return number of central zones entrances
	 */
	public int getCentralEntrance()
	{
		return central_entrance;
	}

	/**
	 * Gets the zone number of the zone which the object is in.
	 * 
	 * @return zone number of the current zone
	 */
	public int getCurrentZoneNumber()
	{
		return current_zone_num;
	}

	/**
	 * Gets totoal time spent in the central zones.
	 * 
	 * @return total time spent in the central zones
	 */
	public float getCentralTime()
	{
		return central_zone_time;
	}

	/**
	 * Saves the path of the rat in the path array in form of points (x,y).
	 * 
	 * @param pos
	 *            Current rat position
	 */
	private void addPointToPosArray(final Point pos)
	{
		// TODO: implement to add the functionality of path recording.
		// we need to add some tolerance due to noise
		// path.add(new Point(pos.x,pos.y));
	}

	/**
	 * Initializes the zone controller instance.
	 */

	@Override
	public void initialize()
	{
		current_zone_num = -1;
		central_start_tmp = 0;
		central_flag = false;
		central_zone_time = 0;
		updated_zone_number = -1;
		all_entrance = 0;
		central_entrance = 0;
		central_zone_time_tmp = 0;
		total_distance = 0;

		arr_path.clear();

		gui_cargo = new Cargo(new String[] {
				GUI_CURRENT_ZONE,
				GUI_ALL_ENTRANCE,
				GUI_CENTRAL_ENTRANCE,
				GUI_CENTRAL_TIME,
				GUI_TOTAL_DISTANCE });

		file_cargo = new Cargo(new String[] {
				FILE_ALL_ENTRANCE,
				FILE_CENTRAL_ENTRANCE,
				FILE_CENTRAL_TIME,
				FILE_TOTAL_DISTANCE });
	}

	@Override
	public void process()
	{
		updated_zone_number = zone_controller.getZone(
				current_position.x,
				current_position.y);
		zoneHysteresis();
		updateTotalDistance();
		updateCentralZoneTime();
		addPointToPosArray(current_position);

		if (current_position != null)
		{
			old_position.x = current_position.x;
			old_position.y = current_position.y;
		}
	}

	@Override
	public void updateGUICargoData()
	{
		gui_cargo.setDataByTag(GUI_CURRENT_ZONE, Integer.toString(current_zone_num));
		gui_cargo.setDataByTag(GUI_ALL_ENTRANCE, Integer.toString(all_entrance));
		gui_cargo.setDataByTag(GUI_CENTRAL_ENTRANCE, Integer.toString(central_entrance));
		gui_cargo.setDataByTag(GUI_CENTRAL_TIME, Integer.toString(central_zone_time));
		gui_cargo.setDataByTag(GUI_TOTAL_DISTANCE, Long.toString(total_distance));
	}

	@Override
	public void updateFileCargoData()
	{
		file_cargo.setDataByTag(FILE_ALL_ENTRANCE, Integer.toString(all_entrance));
		file_cargo.setDataByTag(FILE_CENTRAL_ENTRANCE, Integer.toString(central_entrance));
		file_cargo.setDataByTag(FILE_CENTRAL_TIME, Integer.toString(central_zone_time));
		file_cargo.setDataByTag(FILE_TOTAL_DISTANCE, Long.toString(total_distance));
	}

	@Override
	public void updateConfigs(final ModuleConfigs config)
	{
		if (config instanceof ZonesModuleConfigs)
			zones_configs.mergeConfigs(config);
	}

	@Override
	public void registerDataObject(final Data data)
	{
		if (data instanceof RatFinderData)
		{
			rat_finder_data = (RatFinderData) data;
			this.data[0] = rat_finder_data;
			current_position = rat_finder_data.getCenterPoint();
		}
	}

	@Override
	public void deInitialize()
	{

	}

	/**
	 * Gets total distance covered by the object.
	 * 
	 * @return
	 */
	public long getTotalDistance()
	{
		return total_distance;
	}

	/**
	 * To Calculate the total distance covered by the rat through the
	 * experiment.
	 */
	private void updateTotalDistance()
	{
		if (old_position != null)
			total_distance += current_position.distance(old_position) / scale;
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
	public void setScale(final Point p1, final Point p2, final float real_distance)
	{
		final double screen_distance = p1.distance(p2);
		// note: horizontal scale === vertical scale (the cam is perpendicular
		// on the field)

		final double cmResult = screen_distance / real_distance;
		scale = (float) cmResult;
	}

	@Override
	public void deRegisterDataObject(final Data data)
	{
		if (rat_finder_data == data)
		{
			rat_finder_data = null;
			this.data[0] = null;
			current_position = null;
		}
	}

}
