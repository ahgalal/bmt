package utils.video.processors;

public abstract class Data {
	public Object data;
	private String name;
	
	public Data(String name)
	{
		this.name=name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public abstract Object getData();
}
