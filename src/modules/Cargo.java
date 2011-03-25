package modules;

public class Cargo
{
	private String arr_data[];
	private String arr_tags[];

	public void setTags(final String[] tags)
	{
		arr_tags = tags;
	}

	public void setData(final String[] data)
	{
		arr_data = data;
	}

	public Cargo(final String[] tags)
	{
		arr_data = new String[tags.length];
		arr_tags = tags;
	}

	public String getDataByIndex(final int index)
	{
		return arr_data[index];
	}

	public String getDataByTag(final String tag)
	{
		final int index = getIndexByTag(tag);
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

	public void setDataByIndex(final int index, final String data)
	{
		arr_data[index] = data;
	}

	public void setDataByTag(final String tag, final String data)
	{

		arr_data[getIndexByTag(tag)] = data;
	}

	private int getIndexByTag(final String tag)
	{
		for (int i = 0; i < arr_tags.length; i++)
		{
			if (arr_tags[i].equals(tag))
				return i;
		}
		return -1;
	}

}
