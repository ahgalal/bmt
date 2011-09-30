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

package modules.zones;

import gfx_panel.OvalShape;
import gfx_panel.RectangleShape;
import gfx_panel.Shape;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import modules.zones.Zone.ZoneType;

import org.eclipse.swt.graphics.RGB;

import utils.PManager;

/**
 * Collection of zones, with the facilities to add, remove and get zones in an
 * easy way.
 * 
 * @author Creative
 */
public class ZonesCollection
{

	private final ArrayList<Zone> zones;
	private final ShapeController shape_controller;

	/**
	 * Initializes the Collection.
	 */
	public ZonesCollection()
	{
		zones = new ArrayList<Zone>();
		shape_controller = ShapeController.getDefault();
	}

	/**
	 * Gets the number of zones stored in the collection.
	 * 
	 * @return integer representing the number of zones
	 */
	public int getNumberOfZones()
	{
		return zones.size();
	}

	/**
	 * Gets all zones stored in the collection.
	 * 
	 * @return array of zones
	 */
	public Zone[] getAllZones()
	{
		final Zone[] tmp_znz_array = new Zone[getNumberOfZones()];
		zones.toArray(tmp_znz_array);
		return tmp_znz_array;
	}

	/**
	 * Saves zones & shapes information to a file, to be loaded later.
	 * 
	 * @param file_path
	 *            File path to save the information to.
	 */
	public void saveZonesToFile(final String file_path)
	{
		write2file(file_path, prepareShapesZonesDescription());
	}

	/**
	 * Writes a string to a file.
	 * 
	 * @param path
	 *            file path to write the string to
	 * @param data
	 *            String data to write to the file
	 */
	private void write2file(final String path, final String data)
	{
		FileOutputStream out; // declare a file output object
		PrintStream p; // declare a print stream object
		try
		{ // Create a new file output stream // connected to "myfile.txt"
			out = new FileOutputStream(path); // Connect print stream to the
			// output stream
			p = new PrintStream(out);
			p.print(data);
			p.close();
		} catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Reads file contents and puts them in a String.
	 * 
	 * @param path
	 *            file path to rread data from
	 * @return String containing the contents of the file
	 */
	@SuppressWarnings("deprecation")
	private String readFromFile(final String path)
	{
		// String res = "";
		final StringBuffer res_buf = new StringBuffer();
		final File file = new File(path);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try
		{
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			while (dis.available() != 0)
			{
				// res += dis.readLine() + "\n";
				res_buf.append(dis.readLine() + "\n");
			}
			fis.close();
			bis.close();
			dis.close();

		} catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
		return res_buf.toString();
	}

	/**
	 * Converts the zones & shapes information of the current zones to a String.
	 * to be saved to a file for later loading.
	 * 
	 * @return a String containing all zones & shapes information
	 */
	private String prepareShapesZonesDescription()
	{
		// String res = "";
		final StringBuffer res_buf = new StringBuffer();
		RectangleShape tmp_rect = null;
		OvalShape tmp_oval = null;
		Shape tmp_shp = null;
		String width_diameterx = "", height_diametery = "";
		for (int i = 0; i < shape_controller.getNumberOfShapes(); i++)
		{
			tmp_shp = shape_controller.getShapeByIndex(i);
			if (tmp_shp instanceof RectangleShape)
			{
				tmp_rect = (RectangleShape) tmp_shp;
				// res += "Rectangle" + System.getProperty("line.separator");
				res_buf.append("Rectangle" + System.getProperty("line.separator"));
				width_diameterx = String.valueOf(tmp_rect.getWidth());
				height_diametery = String.valueOf(tmp_rect.getHeight());
			}
			else if (tmp_shp instanceof OvalShape)
			{
				tmp_oval = (OvalShape) tmp_shp;
				// res += "Oval" + System.getProperty("line.separator");
				res_buf.append("Oval" + System.getProperty("line.separator"));
				width_diameterx = String.valueOf(tmp_oval.getWidth());
				height_diametery = String.valueOf(tmp_oval.getHeight());
			}
			res_buf.append(tmp_shp.getShapeNumber()
					+ System.getProperty("line.separator")
					+ tmp_shp.getX()
					+ System.getProperty("line.separator")
					+ tmp_shp.getY()
					+ System.getProperty("line.separator")
					+ width_diameterx
					+ System.getProperty("line.separator")
					+ height_diametery
					+ System.getProperty("line.separator")
					+ Shape.color2String(tmp_shp.getColor())
					+ System.getProperty("line.separator")
					+ ZoneType.zoneType2String(getZoneByNumber(tmp_shp.getShapeNumber()).getZoneType())
					+ System.getProperty("line.separator"));
		}
		return res_buf.toString();
	}

	/**
	 * Loads zones and shapes information from a file.
	 * 
	 * @param path
	 *            File path to load information from.
	 */
	public void loadZonesFromFile(final String path) // THINK of XML =D
	{
		zones.clear();
		shape_controller.clearAllShapes();
		PManager.getDefault().drw_zns.clearTable();
		String data = readFromFile(path);
		String tmp_line = "";
		String shape_type = "";
		while (data.length() != 0)
		{
			tmp_line = data.substring(0, data.indexOf('\n'));
			data = data.substring(data.indexOf('\n') + 1);
			if (tmp_line.equals("Rectangle"))
				shape_type = "Rectangle";
			else if (tmp_line.equals("Oval"))
				shape_type = "Oval";

			final int zone_number = Integer.parseInt(data.substring(0, data.indexOf('\n')));
			data = data.substring(data.indexOf('\n') + 1);
			final int x = Integer.parseInt(data.substring(0, data.indexOf('\n')));
			data = data.substring(data.indexOf('\n') + 1);
			final int y = Integer.parseInt(data.substring(0, data.indexOf('\n')));
			data = data.substring(data.indexOf('\n') + 1);
			final int w = Integer.parseInt(data.substring(0, data.indexOf('\n')));
			data = data.substring(data.indexOf('\n') + 1);
			final int h = Integer.parseInt(data.substring(0, data.indexOf('\n')));
			data = data.substring(data.indexOf('\n') + 1);
			final int red = Integer.parseInt(data.substring(0, data.indexOf('\n')));
			data = data.substring(data.indexOf('\n') + 1);
			final int green = Integer.parseInt(data.substring(0, data.indexOf('\n')));
			data = data.substring(data.indexOf('\n') + 1);
			final int blue = Integer.parseInt(data.substring(0, data.indexOf('\n')));
			final RGB c = new RGB(red, green, blue);
			data = data.substring(data.indexOf('\n') + 1);
			final String zt = data.substring(0, data.indexOf('\n'));
			final ZoneType ztype = ZoneType.string2ZoneType(zt);

			data = data.substring(data.indexOf('\n') + 1);
			Shape tmp_shp = null;
			if (shape_type.equals("Rectangle"))
			{
				tmp_shp = new RectangleShape(x, y, w, h, c);
				tmp_shp.setShapeNumber(zone_number);
			}
			else if (shape_type.equals("Oval"))
			{
				tmp_shp = new OvalShape(w, h, x, y, c);
				tmp_shp.setShapeNumber(zone_number);
			}
			shape_controller.addShape(tmp_shp);
			addZone(zone_number, ztype);

		}
	}

	/**
	 * Gets the zone instance given the zone number.
	 * 
	 * @param zone_number
	 *            Number of zone to return
	 * @return Zone instance having the zone number given
	 */
	public Zone getZoneByNumber(final int zone_number)
	{
		for (final Zone z : zones)
			if (z.getZoneNumber() == zone_number)
				return z;
		return null;
	}

	/**
	 * Changes the zone type of the zone specified by "zonenumber", and updates
	 * the zone's information in the GUI table.
	 * 
	 * @param zonenumber
	 *            zone number of the zone to edit.
	 * @param zonetype
	 *            the new type to change the zone's type to.
	 */
	public void editZone(final int zonenumber, final ZoneType zonetype)
	{
		final Zone z = getZoneByNumber(zonenumber);
		z.setZoneType(zonetype);
		PManager.getDefault().drw_zns.editZoneDataInTable(
				zonenumber,
				Shape.color2String(shape_controller.getShapeByNumber(zonenumber)
						.getColor()),
				ZoneType.zoneType2String(zonetype));
	}

	/**
	 * Adds a new zone to the collection.
	 * 
	 * @param zone_number
	 *            new zone's number
	 * @param type
	 *            new zone's type
	 */
	public void addZone(final int zone_number, final ZoneType type)
	{
		Zone tmp = new Zone(zone_number, type);
		zones.add(tmp);
		PManager.getDefault().drw_zns.addZoneToTable(
				Integer.toString(zone_number),
				Shape.color2String(shape_controller.getShapeByNumber(zone_number)
						.getColor()),
				ZoneType.zoneType2String(type));
		tmp = null;
	}

	/**
	 * Deletes a zone from the collection.
	 * 
	 * @param zonenumber
	 *            number of the zone to delete
	 */
	public void deleteZone(final int zonenumber)
	{
		zones.remove(getZoneByNumber(zonenumber));

	}
}
