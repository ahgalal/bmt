package utils.video.processors;

import java.awt.Point;

public class RearingDetector {


	private int height;
	private int width;
	private int margin_x,margin_y;
	private int current_rat_area;
	private boolean rearing_now;
	private int rearing_thresh=200;
	private Point center_point;

	public void setRearing_thresh(int rearingThresh) {
		rearing_thresh = rearingThresh;
	}

	private Integer normal_rat_area;



	public RearingDetector(int height, int width,Point center_point_ref,
			int margin_x,int margin_y) {
		this.height = height;
		this.width = width;
		center_point=center_point_ref;
		this.margin_x=margin_x;
		this.margin_y=margin_y;
	}



	public boolean isRearing(int[] binary_img_data)
	{	
		if(binary_img_data!=null)
		{
			int white_area=0;
			for(int x=center_point.x-(margin_x/2);x<center_point.x+(margin_x/2);x++)
				for(int y=center_point.y-(margin_y/2);y<center_point.y+(margin_y/2);y++)
					if(x<width & x>=0 & y<height & y>=0)
						if(binary_img_data[x+y*width]==0x00FFFFFF)
							white_area++;

			current_rat_area=white_area;
			if(current_rat_area<rearing_thresh)
				return true;
			else
				return false;
		}
		//PManager.getDefault().log.print("Rearing image is NULL!", this,StatusSeverity.ERROR);
		return false;
	}



	public void rearinNow(boolean rearing) {

		if(rearing)
		{
			rearing_now=true;
			rearing_thresh = (normal_rat_area + current_rat_area)/2;
			System.out.print("Rearing threshold: "+rearing_thresh + "\n");
		}
		else
		{
			rearing_now=false;
			Thread th_rearing = new Thread(new NormalRatAreaThread());
			th_rearing.start();
			System.out.print("Rearing Training Started" + "\n");
		}

	}

	private class NormalRatAreaThread implements Runnable
	{
		private static final long rat_area_training_time = 3;

		@Override
		public void run() {
			long timer_start=(System.currentTimeMillis()/1000);
			int tmp_rat_area_sum=0,num_samples=0;
			while(!rearing_now & (System.currentTimeMillis()/1000)-timer_start < rat_area_training_time)
			{
				num_samples++;
				tmp_rat_area_sum+=current_rat_area;

				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {}
			}
			rearing_now=false;
			normal_rat_area=tmp_rat_area_sum/num_samples;
			System.out.print("normal rat area: " + normal_rat_area + "\n" );
		}
	}
}
