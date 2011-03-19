package modules;

public class Cargo {
	private String arr_data[];
	private String arr_tags[];
	
	public void setTags(String[] tags)
	{
		arr_tags=tags;
	}
	
	public void setData(String[] data)
	{
		arr_data=data;
	}
	
	public Cargo(String[] tags)
	{
		arr_data=new String[tags.length];
		arr_tags=tags;
	}
	
	public String getDataByIndex(int index)
	{
		return arr_data[index];
	}
	
	public String getDataByTag(String tag)
	{
		int index=getIndexByTag(tag);
		return arr_data[index];
	}
	
	public String[] getData()
	{
		return arr_data;
	}
	
	public String[] getTags()
	{
		return arr_tags;
	}
	
	public void setDataByIndex(int index,String data)
	{
		arr_data[index]= data;
	}
	
	public void setDataByTag(String tag,String data)
	{
		
		arr_data[getIndexByTag(tag)]= data;
	}
	
	private int getIndexByTag(String tag)
	{
		for(int i=0;i<arr_tags.length;i++)
		{
			if(arr_tags[i].equals(tag))
				return i;
		}
		return -1;
	}
	
	
}
