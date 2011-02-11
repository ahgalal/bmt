package utils.video.processors;

import utils.video.FrameIntArray;
import utils.video.ImageManipulator;

public class SubtractorFilter extends VideoFilter {
	private FrameIntArray bg,res;
	private int threshold;
	public SubtractorFilter(FrameIntArray bg_image,FrameIntArray substraction_res,int subtraction_thresh)
	{
		res=substraction_res;
		setBackGroundImage(bg_image);
		threshold=subtraction_thresh;
	}

	public void setBackGroundImage(FrameIntArray bg)
	{
		this.bg=bg;
	}

	public void setSubtractionThreshold(int threshold)
	{
		this.threshold=threshold;
	}

	public byte[] subtractGrayImages(byte[] img1,byte[] img2)
	{
		if(img1.length==img2.length)
		{
			byte[] res = new byte[img1.length];
			short tmp1 = 0,tmp2 = 0,tmp3;
			for(int i=0;i<img1.length;i++)
			{
				tmp1=img1[i];
				tmp2=img2[i];
				if(tmp1<0)
					tmp1=(short) (tmp1+256);
				if(tmp2<0)
					tmp2=(short) (tmp2+256);
				tmp3= (short) ( tmp1-tmp2);
				if(tmp3<0)
					tmp3*=-1;
				if(tmp3<threshold)
					res[i]=0;
				else
					res[i]=(byte) 0xFF;
			}
			return res;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see utils.video.processors.VideoFilter#process(int[])
	 */
	@Override
	public int[] process(int[] imageData) {
		if(enabled)
		{
			if(imageData.length==bg.frame_data.length)
			{
				imageData=ImageManipulator.rgbIntArray2GrayIntArray(imageData);
				int[] res = new int[imageData.length];
				int tmp = 0;
				for(int i=0;i<imageData.length;i++)
				{
					tmp= imageData[i]-bg.frame_data[i];

					if(tmp<0)
						tmp*=-1;

					if(tmp<threshold)
						res[i]=0;
					else
						res[i]= 0x00FFFFFF;
				}
				this.res.frame_data=res;
				return res;
			}
			return null;
		}
		return imageData;
	}

}
