package utils.video.filters.source;

import utils.video.filters.FilterConfigs;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;

public class SourceFilter extends VideoFilter
{
	private SourceFilterConfigs source_configs;
	public SourceFilter(String name, FilterConfigs configs, Link linkIn, Link linkOut)
	{
		super(name, configs, linkIn, linkOut);
		source_configs=(SourceFilterConfigs)configs;
	}

	@Override
	public boolean initialize()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void process()
	{
		link_out.setData(source_configs.fia.frame_data);
	}

}
