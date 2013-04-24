/**
 * 
 */
package filters.zonesdrawer;

import modules.zones.ShapeCollection;
import modules.zones.ShapeController;
import utils.PManager;
import utils.StatusManager.StatusSeverity;
import filters.CommonFilterConfigs;
import filters.FilterConfigs;

/**
 * @author Creative
 */
public class ZonesDrawerConfigs extends FilterConfigs {

	/**
	 * instance of a ShapeCollection that will draw its shapes on the main
	 * stream.
	 */
	private ShapeCollection	shapeController;

	public ZonesDrawerConfigs(final String name,
			final CommonFilterConfigs commonConfigs,
			final ShapeCollection shpController) {
		super(name, ZonesDrawerFilter.ID, commonConfigs);
		this.setShapeController(shpController);
	}

	public ShapeCollection getShapeController() {
		return shapeController;
	}

	/*
	 * (non-Javadoc)
	 * @see filters.FilterConfigs#mergeConfigs(filters.FilterConfigs)
	 */
	@Override
	public void mergeConfigs(final FilterConfigs configs) {
		super.mergeConfigs(configs);
		final ZonesDrawerConfigs tmpScnDrwrCfgs = (ZonesDrawerConfigs) configs;
		if (tmpScnDrwrCfgs.getShapeController() != null)
			setShapeController(tmpScnDrwrCfgs.getShapeController());
	}

	/*
	 * (non-Javadoc)
	 * @see filters.FilterConfigs#newInstance(java.lang.String,
	 * filters.CommonFilterConfigs)
	 */
	@Override
	public FilterConfigs newInstance(final String filterName,
			final CommonFilterConfigs commonConfigs) {
		return new ZonesDrawerConfigs(filterName, commonConfigs,
				ShapeController.getDefault());
	}

	public void setShapeController(final ShapeCollection shapeController) {
		this.shapeController = shapeController;
	}

	/*
	 * (non-Javadoc)
	 * @see filters.FilterConfigs#validate()
	 */
	@Override
	public boolean validate() {
		if ((getCommonConfigs() == null) || (getShapeController() == null)) {
			PManager.log.print("Configs are not completely configured!", this,
					StatusSeverity.ERROR);
			return false;
		}
		return true;
	}

}
