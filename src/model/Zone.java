package model;

/**
 * Represents a zone that the rat can enter.
 * 
 * @author Creative
 */
public class Zone
{

	private int zone_number; // zone ID, it associates the zone with its shape
	private ZoneType zone_type; // Type of the zone

	/**
	 * Initialized the zone object.
	 * 
	 * @param zoneNumber
	 *            number to assign to the zone
	 * @param zoneType
	 *            type of the zone
	 */
	public Zone(final int zoneNumber, final ZoneType zoneType)
	{
		zone_number = zoneNumber;
		zone_type = zoneType;
	}

	public int getZoneNumber()
	{
		return zone_number;
	}

	public void setZoneNumber(final int zoneNumber)
	{
		zone_number = zoneNumber;
	}

	public ZoneType getZoneType()
	{
		return zone_type;
	}

	public void setZoneType(final ZoneType zoneTpye)
	{
		zone_type = zoneTpye;
	}

	public enum ZoneType
	{
		CENTRAL_ZONE, NORMAL_ZONE;

		/**
		 * Converts a string containing zone type to a zone type Enum.
		 * 
		 * @param str
		 *            string containing the zone type
		 * @return ZoneType enum specified in the string
		 */
		public static ZoneType string2ZoneType(final String str)
		{
			if (str.equals("Normal"))
				return ZoneType.NORMAL_ZONE;
			else if (str.equals("Central"))
				return ZoneType.CENTRAL_ZONE;
			return null;
		}

		/**
		 * Converts the ZoneType to string, useful when saving to file.
		 * 
		 * @param zt
		 *            zone type to convert to string
		 * @return String containing the zone type
		 */
		public static String zoneType2String(final ZoneType zt)
		{
			if (zt == NORMAL_ZONE)
				return "Normal";
			else if (zt == CENTRAL_ZONE)
				return "Central";
			return null;
		}
	}

}
