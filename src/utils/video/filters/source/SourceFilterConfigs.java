package utils.video.filters.source;

import utils.video.FrameIntArray;
import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;

public class SourceFilterConfigs extends FilterConfigs
{
	public FrameIntArray fia;
	public SourceFilterConfigs(String name, CommonFilterConfigs commonConfigs,FrameIntArray fia)
	{
		super(name, commonConfigs);
		this.fia=fia;
	}

	@Override
	public void mergeConfigs(FilterConfigs configs)
	{

	}

}
