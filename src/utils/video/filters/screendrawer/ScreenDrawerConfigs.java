package utils.video.filters.screendrawer;

import java.awt.Graphics;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;
import control.ShapeController;

/**
 * Configurations for the ScreenDrawer filter.
 * 
 * @author Creative
 */
public class ScreenDrawerConfigs extends FilterConfigs
{
	/**
	 * Initializes the configurations.
	 * 
	 * @param filt_name
	 *            name of the filter this configurations will be applied to
	 * @param refGfxMainScreen
	 *            reference to a Graphics object that the filter will draw on
	 *            the main data stream
	 * @param refGfxSecScreen
	 *            reference to a Graphics object that the filter will draw on
	 *            the secondary data stream
	 * @param common_configs
	 *            CommonConfigurations used by all filters
	 * @param enable_sec_screen
	 *            enable drawing of the secondary stream
	 * @param shp_controller
	 *            instance of ShapeController that will draw its shapes on the
	 *            main stream
	 */
	public ScreenDrawerConfigs(
			final String filt_name,
			final Graphics refGfxMainScreen,
			final Graphics refGfxSecScreen,
			final CommonFilterConfigs common_configs,
			final boolean enable_sec_screen,
			final ShapeController shp_controller)
	{
		super(filt_name, common_configs);
		ref_gfx_main_screen = refGfxMainScreen;
		ref_gfx_sec_screen = refGfxSecScreen;
		this.enable_sec_screen = enable_sec_screen;
		this.shape_controller = shp_controller;
	}

	/**
	 * ref_gfx_main_screen reference to a Graphics object that the filter will
	 * draw on the main data stream. ref_gfx_sec_screen reference to a Graphics
	 * object that the filter will draw on the secondary data stream.
	 */
	public Graphics ref_gfx_main_screen, ref_gfx_sec_screen;
	/**
	 * enable drawing of the secondary stream.
	 */
	public boolean enable_sec_screen;
	/**
	 * instance of ShapeController that will draw its shapes on the main stream.
	 */
	public ShapeController shape_controller;

	@Override
	public void mergeConfigs(final FilterConfigs configs)
	{
		final ScreenDrawerConfigs tmp_scn_drwr_cfgs = (ScreenDrawerConfigs) configs;
		if (tmp_scn_drwr_cfgs.common_configs != null)
			common_configs = tmp_scn_drwr_cfgs.common_configs;
		if (tmp_scn_drwr_cfgs.ref_gfx_main_screen != null)
			ref_gfx_main_screen = tmp_scn_drwr_cfgs.ref_gfx_main_screen;
		if (tmp_scn_drwr_cfgs.ref_gfx_sec_screen != null)
			ref_gfx_sec_screen = tmp_scn_drwr_cfgs.ref_gfx_sec_screen;
		this.enable_sec_screen = tmp_scn_drwr_cfgs.enable_sec_screen;
		if (tmp_scn_drwr_cfgs.shape_controller != null)
			shape_controller = tmp_scn_drwr_cfgs.shape_controller;
	}

	@Override
	public boolean validate()
	{
		if (common_configs == null || shape_controller == null)
		{
			PManager.log.print(
					"Configs are not completely configured!",
					this,
					StatusSeverity.ERROR);
			return false;
		}
		return true;
	}

}
