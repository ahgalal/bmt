package utils.video.processors;

import java.awt.Point;

public class RatFinder extends VideoFilter {

	private int height;
	private int width;
	private int tmp_max;
	private Point center_point;
	private int max_thresh;

	private void drawMarkerOnImg(int[] binary_image)
	{
		int[] buf_data=binary_image;

		try {
			final int mark_length=10;
			int j=(center_point.x + (center_point.y-mark_length)*width);
			int i=(center_point.x-mark_length + center_point.y*width);
			int len=binary_image.length;
			for(int c=0;c<mark_length*2;c++)//i<(pos_x+mark_length + pos_y*width)*3
			{
				if(i<0)
					i=center_point.y*width;
				if(j<0)
					j=center_point.x;
				if(i<len & j<len){ 
					buf_data[i]= 0x00FF0000;	//red
					buf_data[j]= 0x00FF0000;	//red
				}
				j+=width;
				i+=1;

			}
		} catch (Exception e) {
			System.err.print("Error ya 3am el 7ag, fel index !");
			e.printStackTrace();
		}
	}

	public RatFinder(int height, int width, int maxThresh,Point center_point) {
		super();
		this.height = height;
		this.width = width;
		max_thresh = maxThresh;
		tmp_max=max_thresh;
		this.center_point=center_point;
	}



	public Point getCentroid(int[] binary_image)
	{
		int[] hori_sum=new int[height];
		int[] vert_sum=new int[width];
		for(int y=0;y<height;y++) // Horizontal Sum
		{
			for(int x=0;x<width;x++)
				hori_sum[y]+=binary_image[y*width + x]&0xff;
			if(hori_sum[y]>tmp_max)
			{
				center_point.y=y;
				tmp_max=hori_sum[y];
			}
		}

		tmp_max = max_thresh;
		for(int x=0;x<width;x++) // Vertical Sum
		{
			for(int y=0;y<height;y++)
				vert_sum[x]+=binary_image[y*width + x]&0xff;
			if(vert_sum[x]>tmp_max)
			{
				center_point.x=x;
				tmp_max = vert_sum[x];
			}
		}

		//System.out.print(Integer.toString(center_point.x) + " " + Integer.toString(center_point.y) + "\n");
		//System.out.print("V: " + Integer.toString(vert_sum[start_point.x])+"         H :" + Integer.toString(vert_sum[start_point.y])+"\n");

		return center_point;
	}

	/* (non-Javadoc)
	 * @see utils.video.processors.VideoUtility#process(int[])
	 */
	@Override
	public int[] process(int[] imageData) {
		if(enabled)
		{
			getCentroid(imageData);
			drawMarkerOnImg(imageData);
		}
		return imageData;
	}

}
