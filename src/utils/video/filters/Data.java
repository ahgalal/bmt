package utils.video.filters;

public abstract class Data
{
	public Object data;
	private final String name;

	public Data(final String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public abstract Object getData();
}
