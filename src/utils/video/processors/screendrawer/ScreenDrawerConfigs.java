package utils.video.processors.screendrawer;

import java.awt.Graphics;

import utils.video.FrameIntArray;
import utils.video.input.VidInputter;
import utils.video.processors.CommonConfigs;
import utils.video.processors.FilterConfigs;

public class ScreenDrawerConfigs extends FilterConfigs {

	public ScreenDrawerConfigs(Graphics refGfxMainScreen,
			Graphics refGfxSecScreen, VidInputter vIn, FrameIntArray refFia,CommonConfigs common_configs,boolean enable_sec_screen) {
		super();
		ref_gfx_main_screen = refGfxMainScreen;
		ref_gfx_sec_screen = refGfxSecScreen;
		v_in = vIn;
		ref_fia = refFia;
		this.common_configs=common_configs;
		this.enable_sec_screen=enable_sec_screen;
	}

	public Graphics ref_gfx_main_screen, ref_gfx_sec_screen;
	public VidInputter v_in;
	public FrameIntArray ref_fia;
	public boolean enable_sec_screen;
	
	@Override
	public void mergeConfigs(FilterConfigs configs) {
		ScreenDrawerConfigs tmp_scn_drwr_cfgs = (ScreenDrawerConfigs)configs;
		if(tmp_scn_drwr_cfgs.ref_fia!=null)
			ref_fia=tmp_scn_drwr_cfgs.ref_fia;
		if(tmp_scn_drwr_cfgs.common_configs!=null)
			common_configs=tmp_scn_drwr_cfgs.common_configs;
		if(tmp_scn_drwr_cfgs.ref_gfx_main_screen!=null)
			ref_gfx_main_screen=tmp_scn_drwr_cfgs.ref_gfx_main_screen;
		if(tmp_scn_drwr_cfgs.ref_gfx_sec_screen!=null)
			ref_gfx_sec_screen=tmp_scn_drwr_cfgs.ref_gfx_sec_screen;
		if(tmp_scn_drwr_cfgs.v_in!=null)
			v_in=tmp_scn_drwr_cfgs.v_in;
		this.enable_sec_screen=tmp_scn_drwr_cfgs.enable_sec_screen;
	}

}
