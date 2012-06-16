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

import utils.video.filters.Data;

/**
 * Data of the Zones Module.
 * 
 * @author Creative
 */
public class ZonesModuleData extends Data {
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
    public ZonesModuleData(final String name) {
	super(name);
    }

}
