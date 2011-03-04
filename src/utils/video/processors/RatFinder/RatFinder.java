package utils.video.processors.RatFinder;

import java.awt.Point;

import utils.video.processors.VideoFilter;

public class RatFinder extends VideoFilter {

	private int tmp_max;
	private RatFinderConfigs ratfinder_configs;
	private void drawMarkerOnImg(int[] binary_image)
	{
		int[] buf_data=binary_image;

		try {
			final int mark_length=10;
			int j=(ratfinder_configs.ref_center_point.x + (ratfinder_configs.ref_center_point.y-mark_length)*ratfinder_configs.common_configs.width);
			int i=(ratfinder_configs.ref_center_point.x-mark_length + ratfinder_configs.ref_center_point.y*ratfinder_configs.common_configs.width);
			int len=binary_image.length;
			for(int c=0;c<mark_length*2;c++)//i<(pos_x+mark_length + pos_y*width)*3
			{
				if(i<0)
					i=ratfinder_configs.ref_center_point.y*ratfinder_configs.common_configs.width;
				if(j<0)
					j=ratfinder_configs.ref_center_point.x;
				if(i<len & j<len){ 
					buf_data[i]= 0x00FF0000;	//red
					buf_data[j]= 0x00FF0000;	//red
				}
				j+=ratfinder_configs.common_configs.width;
				i+=1;

			}
		} catch (Exception e) {
			System.err.print("Error ya 3am el 7ag, fel index!");
			e.printStackTrace();
		}
	}

	public RatFinder(String name) {
		super();
		ratfinder_configs=new RatFinderConfigs(0, null,null);
		this.name=name;
		configs=ratfinder_configs;
	}



	public Point getCentroid(int[] binary_image)
	{
		tmp_max=ratfinder_configs.max_thresh;
		int[] hori_sum=new int[ratfinder_configs.common_configs.height];
		int[] vert_sum=new int[ratfinder_configs.common_configs.width];
		for(int y=0;y<ratfinder_configs.common_configs.height;y++) // Horizontal Sum
		{
			for(int x=0;x<ratfinder_configs.common_configs.width;x++)
				hori_sum[y]+=binary_image[y*ratfinder_configs.common_configs.width + x]&0xff;
			if(hori_sum[y]>tmp_max)
			{
				ratfinder_configs.ref_center_point.y=y;
				tmp_max=hori_sum[y];
			}
		}

		tmp_max = ratfinder_configs.max_thresh;
		for(int x=0;x<ratfinder_configs.common_configs.width;x++) // Vertical Sum
		{
			for(int y=0;y<ratfinder_configs.common_configs.height;y++)
				vert_sum[x]+=binary_image[y*ratfinder_configs.common_configs.width + x]&0xff;
			if(vert_sum[x]>tmp_max)
			{
				ratfinder_configs.ref_center_point.x=x;
				tmp_max = vert_sum[x];
			}
		}

		//System.out.print(Integer.toString(center_point.x) + " " + Integer.toString(center_point.y) + "\n");
		//System.out.print("V: " + Integer.toString(vert_sum[start_point.x])+"         H :" + Integer.toString(vert_sum[start_point.y])+"\n");

		return ratfinder_configs.ref_center_point;
	}

	/* (non-Javadoc)
	 * @see utils.video.processors.VideoUtility#process(int[])
	 */
	@Override
	public int[] process(int[] imageData) {
		if(configs.enabled)
		{
			getCentroid(imageData);
			drawMarkerOnImg(imageData);
		}
		return imageData;
	}

	@Override
	public boolean initialize() {
		return true;
	}

}
