package utils.video.processors.rearingdetection;

import utils.video.processors.VideoFilter;

public class RearingDetector extends VideoFilter{
	private RearingConfigs rearing_configs;



	public RearingFilterData rearing_data;
	public boolean rearing_now;
	public Integer normal_rat_area;
	public boolean is_rearing;
	public int current_rat_area;


	public RearingDetector(String name) 
	{
		rearing_configs = new RearingConfigs(0, 0, 0, null,null);
		rearing_data=new RearingFilterData();
		special_data = rearing_data;
		configs=rearing_configs;
		((RearingFilterData)special_data).setRearing(false);

		this.configs= rearing_configs;
		this.name = name;
	}

	public void rearinNow(boolean rearing) {

		if(rearing)
		{
			rearing_now=true;
			rearing_configs.rearing_thresh = (normal_rat_area + current_rat_area)/2;
			System.out.print("Rearing threshold: "+rearing_configs.rearing_thresh + "\n");
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

	@Override
	public int[] process(int[] imageData) {
		if(configs.enabled)
		{
			if(imageData!=null)
			{
				int white_area=0;
				for(int x=rearing_configs.ref_center_point.x-(rearing_configs.margin_x/2);x<rearing_configs.ref_center_point.x+(rearing_configs.margin_x/2);x++)
					for(int y=rearing_configs.ref_center_point.y-(rearing_configs.margin_y/2);y<rearing_configs.ref_center_point.y+(rearing_configs.margin_y/2);y++)
						if(x<rearing_configs.common_configs.width & x>=0 & y<rearing_configs.common_configs.height & y>=0)
							if(imageData[x+y*rearing_configs.common_configs.width]==0x00FFFFFF)
								white_area++;

				current_rat_area=white_area;
				if(current_rat_area<rearing_configs.rearing_thresh)
					is_rearing=true;
				else
					is_rearing=false;
			}
		}
		rearing_data.setRearing(is_rearing);
		return imageData;
	}

	@Override
	public boolean initialize() {
		return true;
	}


}
