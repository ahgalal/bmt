package utils.video.filters.source;

import utils.video.filters.FilterConfigs;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;

/**
 * Takes the data coming from the input device and puts it on its output link,
 * to be used by other filters.
 * 
 * @author Creative
 */
public class SourceFilter extends VideoFilter
{
	private final SourceFilterConfigs source_configs;

	/**
	 * Initializes the filter.
	 * 
	 * @param name
	 *            filter's name
	 * @param configs
	 *            common configurations
	 * @param linkOut
	 *            output link that will distribute the data on other filters
	 */
	public SourceFilter(final String name, final FilterConfigs configs, final Link linkOut)
	{
		super(name, configs, null, linkOut);
		source_configs = (SourceFilterConfigs) configs;
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
