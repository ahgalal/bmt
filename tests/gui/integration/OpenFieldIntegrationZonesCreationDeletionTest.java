/**
 * 
 */
package gui.integration;

import gui.executionunit.ZonesExecUnitGroup;
import sys.utils.Files;

/**
 * Tests deleting/creating zones (rectangular and oval), saving zones to file
 * and loading them again, and checks correct tracking.
 * 
 * @author Creative
 */
public class OpenFieldIntegrationZonesCreationDeletionTest extends
		OpenFieldIntegrationTest {
	protected String	zonesFileEdited;

	@Override
	protected void afterSettingScale() throws Exception {
		zonesFileEdited = zonesFile + "_edited";

		ZonesExecUnitGroup.openZoneEditor();

		// replace Rectangle with a new Rectangle
		ZonesExecUnitGroup.deleteShape(311, 421);
		ZonesExecUnitGroup.createRectangleShape(250, 375, 366, 470,
				"Rectangle", "Central");

		// replace Rectangle with a new Oval
		ZonesExecUnitGroup.deleteShape(433, 424);
		ZonesExecUnitGroup.createRectangleShape(373, 375, 495, 472, "Circle",
				"Central");

		// replace Rectangle with a new Oval
		ZonesExecUnitGroup.deleteShape(570, 424);
		ZonesExecUnitGroup.createRectangleShape(503, 371, 629, 471, "Circle",
				"Central");

		// replace Rectangle with a new Oval
		ZonesExecUnitGroup.deleteShape(570, 300);
		ZonesExecUnitGroup.createRectangleShape(498, 247, 629, 361, "Circle",
				"Central");

		// save zones to file
		ZonesExecUnitGroup.saveZonesToFile(zonesFileEdited);

		// load zones from the saved file
		ZonesExecUnitGroup.basicLoadZones(zonesFileEdited);

		ZonesExecUnitGroup.hideZoneEditor();

		// do parent stuff
		super.afterSettingScale();
	}

	@Override
	public void setUp() {
		super.setUp();

		// add 4 entrances for the newly added central zones
		centralEntranceMax += 4;
		centralEntranceMin += 4;
		centralTimeMax += 4;
		centralTimeMin += 4;
	}

	@Override
	protected void tearDown() throws Exception {
		Files.deleteFile(zonesFileEdited);
		super.tearDown();
	}
}
