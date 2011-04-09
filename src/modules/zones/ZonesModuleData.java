package modules.zones;

import utils.video.filters.Data;

/**
 * Data of the Zones Module.
 * 
 * @author Creative
 */
public class ZonesModuleData extends Data
{
	public float scale;
	public int current_zone_num;
	public int all_entrance, central_entrance;
	public int central_zone_time;
	public long total_distance;
	public boolean central_flag;
	public ZonesCollection zones;

	/**
	 * Initializes the Data.
	 * 
	 * @param name
	 *            name of the data instance
	 */
	public ZonesModuleData(final String name)
	{
		super(name);
	}

}
