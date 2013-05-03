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
import utils.StatusManager.StatusSeverity;

/**
 * Collection of zones, with the facilities to add, remove and get zones in an
 * easy way.
 * 
 * @author Creative
 */
public class ZonesCollection {

	private final ShapeController	shapeController;
	private final ArrayList<Zone>	zones;
private ZonesModule zonesModule;
	/**
	 * Initializes the Collection.
	 * @param shapeController2 
	 */
	public ZonesCollection(ZonesModule zonesModule, ShapeController shapeController) {
		zones = new ArrayList<Zone>();
		this.zonesModule=zonesModule;
		this.shapeController = shapeController;
	}

	/**
	 * Adds a new zone to the collection.
	 * 
	 * @param zoneNumber
	 *            new zone's number
	 * @param type
	 *            new zone's type
	 */
	public void addZone(final int zoneNumber, final ZoneType type) {
		// TODO: add check for previous existance
		Zone tmp = new Zone(zoneNumber, type);
		zones.add(tmp);
		tmp = null;
	}

	/**
	 * Deletes a zone from the collection.
	 * 
	 * @param zonenumber
	 *            number of the zone to delete
	 */
	public void deleteZone(final int zonenumber) {
		zones.remove(getZoneByNumber(zonenumber));

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
	public void editZone(final int zonenumber, final ZoneType zonetype) {
		final Zone z = getZoneByNumber(zonenumber);
		z.setZoneType(zonetype);
	}

	/**
	 * Gets all zones stored in the collection.
	 * 
	 * @return array of zones
	 */
	public Zone[] getAllZones() {
		final Zone[] tmpZnzArray = new Zone[getNumberOfZones()];
		zones.toArray(tmpZnzArray);
		return tmpZnzArray;
	}

	/**
	 * Gets the number of zones stored in the collection.
	 * 
	 * @return integer representing the number of zones
	 */
	public int getNumberOfZones() {
		return zones.size();
	}

	/**
	 * Gets the zone instance given the zone number.
	 * 
	 * @param zoneNumber
	 *            Number of zone to return
	 * @return Zone instance having the zone number given
	 */
	public Zone getZoneByNumber(final int zoneNumber) {
		for (final Zone z : zones)
			if (z.getZoneNumber() == zoneNumber)
				return z;
		return null;
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
		shapeController.clearAllShapes();
		String data = readFromFile(path);
		String tmpLine = "";
		String shapeType = "";
		try {
			while (data.length() != 0) {
				tmpLine = data.substring(0, data.indexOf('\n'));
				data = data.substring(data.indexOf('\n') + 1);
				if (tmpLine.equals("Rectangle"))
					shapeType = "Rectangle";
				else if (tmpLine.equals("Oval"))
					shapeType = "Oval";

				final int zoneNumber = Integer.parseInt(data.substring(0,
						data.indexOf('\n')));
				data = data.substring(data.indexOf('\n') + 1);
				final int x = Integer
						.parseInt(data.substring(0, data.indexOf('\n')));
				data = data.substring(data.indexOf('\n') + 1);
				final int y = Integer
						.parseInt(data.substring(0, data.indexOf('\n')));
				data = data.substring(data.indexOf('\n') + 1);
				final int w = Integer
						.parseInt(data.substring(0, data.indexOf('\n')));
				data = data.substring(data.indexOf('\n') + 1);
				final int h = Integer
						.parseInt(data.substring(0, data.indexOf('\n')));
				data = data.substring(data.indexOf('\n') + 1);
				final int red = Integer.parseInt(data.substring(0,
						data.indexOf('\n')));
				data = data.substring(data.indexOf('\n') + 1);
				final int green = Integer.parseInt(data.substring(0,
						data.indexOf('\n')));
				data = data.substring(data.indexOf('\n') + 1);
				final int blue = Integer.parseInt(data.substring(0,
						data.indexOf('\n')));
				final RGB c = new RGB(red, green, blue);
				data = data.substring(data.indexOf('\n') + 1);
				final String zt = data.substring(0, data.indexOf('\n'));
				final ZoneType ztype = ZoneType.string2ZoneType(zt);

				data = data.substring(data.indexOf('\n') + 1);
				Shape tmpShp = null;
				if (shapeType.equals("Rectangle")) {
					tmpShp = new RectangleShape(x, y, w, h, c);
					tmpShp.setShapeNumber(zoneNumber);
				} else if (shapeType.equals("Oval")) {
					tmpShp = new OvalShape(w, h, x, y, c);
					tmpShp.setShapeNumber(zoneNumber);
				}
				shapeController.addShape(tmpShp);
				zonesModule.addZone(zoneNumber, ztype);
			}
		} catch (Exception e) {
			PManager.getDefault().getStatusMgr().setStatus("Zones file may be corrupt: "+ path, StatusSeverity.ERROR);
		}
	}

	/**
	 * Converts the zones & shapes information of the current zones to a String.
	 * to be saved to a file for later loading.
	 * 
	 * @return a String containing all zones & shapes information
	 */
	private String prepareShapesZonesDescription() {
		// String res = "";
		final StringBuffer resBuf = new StringBuffer();
		RectangleShape tmpRect = null;
		OvalShape tmpOval = null;
		Shape tmpShp = null;
		String widthDiameterX = "", heightDiameterY = "";
		for (int i = 0; i < shapeController.getNumberOfShapes(); i++) {
			tmpShp = shapeController.getShapeByIndex(i);
			if (tmpShp instanceof RectangleShape) {
				tmpRect = (RectangleShape) tmpShp;
				// res += "Rectangle" + System.getProperty("line.separator");
				resBuf.append("Rectangle"
						+ System.getProperty("line.separator"));
				widthDiameterX = String.valueOf(tmpRect.getWidth());
				heightDiameterY = String.valueOf(tmpRect.getHeight());
			} else if (tmpShp instanceof OvalShape) {
				tmpOval = (OvalShape) tmpShp;
				// res += "Oval" + System.getProperty("line.separator");
				resBuf.append("Oval" + System.getProperty("line.separator"));
				widthDiameterX = String.valueOf(tmpOval.getWidth());
				heightDiameterY = String.valueOf(tmpOval.getHeight());
			}
			resBuf.append(tmpShp.getShapeNumber()
					+ System.getProperty("line.separator")
					+ tmpShp.getX()
					+ System.getProperty("line.separator")
					+ tmpShp.getY()
					+ System.getProperty("line.separator")
					+ widthDiameterX
					+ System.getProperty("line.separator")
					+ heightDiameterY
					+ System.getProperty("line.separator")
					+ Shape.color2String(tmpShp.getColor())
					+ System.getProperty("line.separator")
					+ ZoneType.zoneType2String(getZoneByNumber(
							tmpShp.getShapeNumber()).getZoneType())
					+ System.getProperty("line.separator"));
		}
		return resBuf.toString();
	}

	/**
	 * Reads file contents and puts them in a String.
	 * 
	 * @param path
	 *            file path to rread data from
	 * @return String containing the contents of the file
	 */
	@SuppressWarnings("deprecation")
	private String readFromFile(final String path) {
		// String res = "";
		final StringBuffer resBuf = new StringBuffer();
		final File file = new File(path);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			while (dis.available() != 0)
				// res += dis.readLine() + "\n";
				resBuf.append(dis.readLine() + "\n");
			fis.close();
			bis.close();
			dis.close();

		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return resBuf.toString();
	}

	/**
	 * Saves zones & shapes information to a file, to be loaded later.
	 * 
	 * @param filePath
	 *            File path to save the information to.
	 */
	public void saveZonesToFile(final String filePath) {
		write2file(filePath, prepareShapesZonesDescription());
	}

	/**
	 * Writes a string to a file.
	 * 
	 * @param path
	 *            file path to write the string to
	 * @param data
	 *            String data to write to the file
	 */
	private void write2file(final String path, final String data) {
		FileOutputStream out; // declare a file output object
		PrintStream p; // declare a print stream object
		try { // Create a new file output stream // connected to "myfile.txt"
			out = new FileOutputStream(path); // Connect print stream to the
			// output stream
			p = new PrintStream(out);
			p.print(data);
			p.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
