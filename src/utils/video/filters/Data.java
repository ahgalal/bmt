package utils.video.filters;

/**
 * Data Object, used by Modules to handle Filters/other Modules data.
 * 
 * @author Creative
 */
public abstract class Data
{
	private final String name;

	/**
	 * Initializes the name of the data object.
	 * 
	 * @param name
	 */
	public Data(final String name)
	{
		this.name = name;
	}

	/**
	 * Gets the name of the data object.
	 * 
	 * @return String containing the name of the data object
	 */
	public String getName()
	{
		return name;
	}

}
