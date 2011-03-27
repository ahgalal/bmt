package utils.video.filters;

import java.awt.Point;


public class Link
{
	//private Point dims;
	private int[] img_data;
	public Link(Point dims)//int width,int height)
	{
		//dims=img_dimentions;
		img_data = new int[dims.x*dims.y];
	}
	
	public int[] getData()
	{
		return img_data;
	}
	
	public void setData(int[] newdata)
	{
		img_data=newdata;
	}
}
