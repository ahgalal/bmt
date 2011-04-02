package control;

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

import model.Zone;
import model.Zone.ZoneType;

import org.eclipse.swt.graphics.RGB;

import utils.PManager;

/**
 * Manages zones.
 * 
 * @author Creative
 */
public class ZonesController
{
	private static ZonesController default_controller;
	ShapeController shape_controller;
	private byte[] zone_map;
	private final ArrayList<Zone> zones;
	private final PManager pm;

	private int width = 640, height = 480;

	public int getNumberOfZones()
	{
		return zones.size();
	}

	private void clearAllZones()
	{
		zones.clear();
	}

	public void setWidthandHeight(final int width, final int height)
	{
		this.width = width;
		this.height = height;
		zone_map = new byte[width * height];
	}

	public ZonesController()
	{
		zones = new ArrayList<Zone>();
		pm = PManager.getDefault();
	}

	/**
	 * Updates the zone map array according to the shapes setup on the GfxPanel
	 * Note: zone map is an array of bytes, we can imagine it as a two dim.
	 * array (width*height) , where each byte contains the number of the zone
	 * existing at that point (x,y)
	 */
	public void updateZoneMap()
	{
		RectangleShape tmp_rect;
		OvalShape tmp_oval;
		int tmp_zone_number;
		zone_map = new byte[width * height];
		initializeZoneMap(-1);
		for (int i = 0; i < zones.size(); i++)
		{
			tmp_zone_number = zones.get(i).getZoneNumber();
			final Shape tmp_shp = shape_controller.getShapeByNumber(tmp_zone_number);

			if (tmp_shp instanceof RectangleShape)
			{
				tmp_rect = (RectangleShape) tmp_shp;
				for (int x = tmp_rect.getX(); x < tmp_rect.getX() + tmp_rect.getWidth(); x++)
				{
					if (x > -1 & x < width)
						for (int y = tmp_rect.getY(); y < tmp_rect.getY()
								+ tmp_rect.getHeight(); y++)
						{
							if (y > -1 & y < height)
								zone_map[x + (height - y) * width] = (byte) tmp_zone_number;
						}
				}
			} else if (tmp_shp instanceof OvalShape)
			{
				tmp_oval = (OvalShape) tmp_shp;
				final int rx = tmp_oval.getWidth() / 2, ry = tmp_oval.getHeight() / 2, x_ov = tmp_oval.getX()
						+ rx, y_ov = tmp_oval.getY() + ry;
				float x_final, y_final;

				for (int x = tmp_oval.getX(); x < tmp_oval.getX() + rx * 2; x++)
				{
					if (x > -1 & x < width)
						for (int y = tmp_oval.getY(); y < tmp_oval.getY() + ry * 2; y++)
						{
							if (y > -1 & y < height)
							{
								x_final = x - x_ov;
								y_final = y - y_ov;
								if ((x_final * x_final)
										/ (rx * rx)
										+ (y_final * y_final)
										/ (ry * ry) < 1)
									zone_map[x + (height - y) * width] = (byte) tmp_zone_number;
							}
						}
				}
			}
		}
	}

	/**
	 * Fills the zone map with a given number.
	 * 
	 * @param null_zone_number
	 *            Number to fill the zone map array with
	 */
	private void initializeZoneMap(final int null_zone_number)
	{
		for (int i = 0; i < zone_map.length; i++)
			zone_map[i] = (byte) null_zone_number;
	}

	public int getZone(final int x, final int y)
	{
		return zone_map[x + y * width];
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

	public static ZonesController getDefault()
	{
		if (default_controller == null)
			default_controller = new ZonesController();

		return default_controller;
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
			} else if (tmp_shp instanceof OvalShape)
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
		clearAllZones();
		shape_controller.clearAllShapes();
		pm.drw_zns.clearTable();
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
			} else if (shape_type.equals("Oval"))
			{
				tmp_shp = new OvalShape(w, h, x, y, c);
				tmp_shp.setShapeNumber(zone_number);
			}
			shape_controller.addShape(tmp_shp);
			addZone(zone_number, ztype);

		}
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
		pm.drw_zns.editZoneDataInTable(
				zonenumber,
				Shape.color2String(shape_controller.getShapeByNumber(zonenumber)
						.getColor()),
				ZoneType.zoneType2String(zonetype));
	}

	/**
	 * Updates the zone's information in the GUI table.
	 * 
	 * @param zonenumber
	 *            zone number of the zone to update its information in GUI
	 *            table.
	 */
	public void updateZoneDataInGUI(final int zonenumber)
	{
		final Zone z = getZoneByNumber(zonenumber);
		pm.drw_zns.editZoneDataInTable(
				zonenumber,
				Shape.color2String(shape_controller.getShapeByNumber(zonenumber)
						.getColor()),
				ZoneType.zoneType2String(z.getZoneType()));
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
		pm.drw_zns.addZoneToTable(
				Integer.toString(zone_number),
				Shape.color2String(shape_controller.getShapeByNumber(zone_number)
						.getColor()),
				ZoneType.zoneType2String(type));
		updateZoneMap();
		tmp = null;
	}

	/**
	 * Adds all zones in the collectoin to the GUI table.
	 */
	public void addAllZonesToGUI()
	{
		int zonenumber;
		for (final Zone z : zones)
		{
			if (z != null)
			{
				zonenumber = z.getZoneNumber();
				pm.drw_zns.addZoneToTable(
						Integer.toString(zonenumber),
						Shape.color2String(shape_controller.getShapeByNumber(zonenumber)
								.getColor()),
						ZoneType.zoneType2String(z.getZoneType()));
			}
		}
	}

	public void deleteZone(final int zonenumber)
	{
		zones.remove(getZoneByNumber(zonenumber));
		updateZoneMap();
		pm.drw_zns.clearTable();
		addAllZonesToGUI();
	}

	/**
	 * Selects the zone in the GUI table.
	 * 
	 * @param zone_number
	 *            number of the zone to select in the GUI table.
	 */
	public void selectZoneInGUI(final int zone_number)
	{
		pm.drw_zns.selectZoneInTable(zone_number);
	}

	/**
	 * Initialize the ShapeController instance.
	 */
	public void init()
	{
		shape_controller = ShapeController.getDefault();
	}

}
