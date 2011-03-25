package utils.video.processors;

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
