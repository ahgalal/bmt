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
	public SourceFilter(String name, Link linkIn, Link linkOut)
	{
		super(name, linkIn, linkOut);
	}

	private SourceFilterConfigs source_configs;



	@Override
	public boolean configure(FilterConfigs configs)
	{
		source_configs = (SourceFilterConfigs) configs;
		return super.configure(configs);
	}

	@Override
	public void process()
	{
		link_out.setData(source_configs.fia.frame_data);
	}

}
