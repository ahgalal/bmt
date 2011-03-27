package utils.video.filters.screendrawer;

import java.awt.Graphics;

import control.ShapeController;

import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;
import utils.video.input.VidInputter;

public class ScreenDrawerConfigs extends FilterConfigs
{
	public ScreenDrawerConfigs(
			final String filt_name,
			final Graphics refGfxMainScreen,
			final Graphics refGfxSecScreen,
			final VidInputter vIn,
			final CommonFilterConfigs common_configs,
			final boolean enable_sec_screen,
			final ShapeController shp_controller)
	{
		super(filt_name, common_configs);
		ref_gfx_main_screen = refGfxMainScreen;
		ref_gfx_sec_screen = refGfxSecScreen;
		this.enable_sec_screen = enable_sec_screen;
		this.shape_controller=shp_controller;
	}

	public Graphics ref_gfx_main_screen, ref_gfx_sec_screen;
	public boolean enable_sec_screen;
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

}
