package utils.video.filters.RatFinder;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import utils.video.filters.FilterConfigs;
import utils.video.filters.Link;

public class RatFinder2 extends RatFinder
{
	public class ContigousArea
	{
		public Point center;

		public ContigousArea()
		{
			center = new Point();
		}
	}

	int border_color=Color.GREEN.getRGB();
	int botash_found=0;
	protected ArrayList<Point> coveredGrid;
	public RatFinder2(String name, Link linkIn, Link linkOut)
	{
		super(name, linkIn, linkOut);
		coveredGrid = new ArrayList<Point>();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see utils.video.filters.RatFinder.RatFinder#updateCentroid(int[])
	 */
	@Override
	protected void updateCentroid(int[] binaryImage)
	{
		System.arraycopy(binaryImage, 0, out_data, 0, binaryImage.length);
		//walking on y, step=5
		y_loop:
			for(int y= 0; y< ratfinder_configs.common_configs.height;y+=5)
			{
				//walking on x, step=1
				x_loop:
					for(int x= 0; x< ratfinder_configs.common_configs.width;x++)
					{
						// if white ...
						if(out_data[x + y*ratfinder_configs.common_configs.width] == 0xFFFFFF)
						{
							contourEdge(out_data, x, y, ratfinder_configs.common_configs.width, ratfinder_configs.common_configs.height);
							botash_found++;
						}
						else if(out_data[x + y*ratfinder_configs.common_configs.width] == border_color)
						{
							continue y_loop;
						}
					}
			}
		System.out.print(botash_found + "\n");
	}

	protected int contourEdge(int[] img,int x_start,int y_start,int width,int height)
	{
		int area;
		int pivot_x = 0,pivot_y = 0;
		int pivot_x_old=x_start,pivot_y_old=y_start;

		int start_i = -1;


		for(int j=7;j>=0;j--) //initial pivot
		{
			int x1,y1,x2,y2;
			x1 = (int) (Math.cos(0.785 * j)*1.5);
			y1 = (int) (Math.sin(0.785 * j)*-1.5);
			x2 = (int) (Math.cos(0.785 * (j-1)%8)*1.5);
			y2 = (int) (Math.sin(0.785 * (j-1)%8)*-1.5);

			if(img[x_start+x2+width*(y2+y_start)]==0xFFFFFF &&
					img[x_start+x1+width*(y1+y_start)]==0)
			{
				pivot_x=x_start+x2;
				pivot_y=y_start+y2;
				//img[x_start+x2+width*(y2+y_start)]= border_color;
			}
		}

		int x=0,y=0;
		while(pivot_x!=x_start || pivot_y!=y_start)
		{
			/*
			 * Selecting the start angle "i"
			 */
			if(pivot_x-pivot_x_old== 1)
			{
				if(pivot_y - pivot_y_old ==-1)
					start_i=4;
				else if(pivot_y - pivot_y_old ==0)
					start_i=3;
				else if(pivot_y - pivot_y_old ==1)
					start_i=2;
			}
			else if(pivot_x-pivot_x_old== 0)
			{
				if(pivot_y - pivot_y_old ==-1)
					start_i=5;
				else if(pivot_y - pivot_y_old ==1)
					start_i=1;
			}
			else if(pivot_x-pivot_x_old== -1)
			{
				if(pivot_y - pivot_y_old ==-1)
					start_i=3;
				else if(pivot_y - pivot_y_old ==0)
					start_i=7;
				else if(pivot_y - pivot_y_old ==1)
					start_i=0;
			}
			////////////////////////////////////////////////////


			int trials=0;
			for(int i=(start_i)%8; i!=(start_i+1)%8;i=(i-1+8)%8)
			{
				trials++;

				// trapped on left side
				if(pivot_x==0) 
				{
					// trapped on origin 
					if(pivot_y==0) 
					{
						// go to the right pixel
						if(img[pivot_x+1]== 0xFFFFFF) 
						{
							x=1;
							y=0;
						}
						// go to the bottom pixel
						else if(img[pivot_x+1*width]== 0xFFFFFF) 
						{
							x=0;
							y=+1;
						}
						// go to the right-bottom pixel
						else if(img[pivot_x+1 + 1*width]== 0xFFFFFF) 
						{
							x=+1;
							y=+1;
						}
						pivot_x_old=pivot_x;
						pivot_y_old=pivot_y;
						img[pivot_x+x+width*(y+pivot_y)] = border_color; // border
						pivot_x+=x;
						pivot_y+=y;
						break;
					}// end trapped on origin
					//trapped on bottom-left
					else if(pivot_y==height-1)
					{
						// go to the right pixel
						if(img[pivot_x+1 + pivot_y*width]== 0xFFFFFF) 
						{
							x=1;
							y=0;
						}
						// go to the upper pixel
						else if(img[pivot_x + (pivot_y-1)*width]== 0xFFFFFF) 
						{
							x=0;
							y=-1;
						}
						// go to the upper-right pixel
						else if(img[pivot_x+1 + (pivot_y+1)*width]== 0xFFFFFF) 
						{
							x=+1;
							y=-1;
						}
						pivot_x_old=pivot_x;
						pivot_y_old=pivot_y;
						img[pivot_x+x+width*(y+pivot_y)] = border_color; // border
						pivot_x+=x;
						pivot_y+=y;
						break;
					}//end trapped on bottom-left
					// trapped on the left side (general place)
					else
					{
						// go to the bottom pixel
						if(img[pivot_x + (pivot_y+1)*width]== 0xFFFFFF) 
						{
							x=0;
							y=+1;
						}
						// go to the upper pixel
						else if(img[pivot_x + (pivot_y-1)*width]== 0xFFFFFF) 
						{
							x=0;
							y=-1;
						}
						pivot_x_old=pivot_x;
						pivot_y_old=pivot_y;
						img[pivot_x+x+width*(y+pivot_y)] = border_color; // border
						pivot_x+=x;
						pivot_y+=y;
						break;
					}// end trapped on the left side
				}// end trapped on the left side

				// trapped on right side
				else if(pivot_x==width -1) 
				{
					// trapped on top-right 
					if(pivot_y==0) 
					{
						// go to the left pixel
						if(img[pivot_x-1]== 0xFFFFFF) 
						{
							x=-1;
							y=0;
						}
						// go to the bottom pixel
						else if(img[pivot_x+1*width]== 0xFFFFFF) 
						{
							x=0;
							y=+1;
						}
						// go to the left-bottom pixel
						else if(img[pivot_x-1 + 1*width]== 0xFFFFFF) 
						{
							x=-1;
							y=+1;
						}
						pivot_x_old=pivot_x;
						pivot_y_old=pivot_y;
						img[pivot_x+x+width*(y+pivot_y)] = border_color; // border
						pivot_x+=x;
						pivot_y+=y;
						break;
					}// end trapped on top-right
					//trapped on bottom-right
					else if(pivot_y==height-1)
					{
						// go to the left pixel
						if(img[pivot_x-1 + pivot_y*width]== 0xFFFFFF) 
						{
							x=-1;
							y=0;
						}
						// go to the upper pixel
						else if(img[pivot_x + (pivot_y-1)*width]== 0xFFFFFF) 
						{
							x=0;
							y=-1;
						}
						// go to the upper-left pixel
						else if(img[pivot_x-1 + (pivot_y+1)*width]== 0xFFFFFF) 
						{
							x=-1;
							y=-1;
						}
						pivot_x_old=pivot_x;
						pivot_y_old=pivot_y;
						img[pivot_x+x+width*(y+pivot_y)] = border_color; // border
						pivot_x+=x;
						pivot_y+=y;
						break;
					}//end trapped on bottom-right
					// trapped on the right side (general place)
					else
					{
						// go to the bottom pixel
						if(img[pivot_x + (pivot_y+1)*width]== 0xFFFFFF) 
						{
							x=0;
							y=+1;
						}
						// go to the upper pixel
						else if(img[pivot_x + (pivot_y-1)*width]== 0xFFFFFF) 
						{
							x=0;
							y=-1;
						}
						pivot_x_old=pivot_x;
						pivot_y_old=pivot_y;
						img[pivot_x+x+width*(y+pivot_y)] = border_color; // border
						pivot_x+=x;
						pivot_y+=y;
						break;
					}// end trapped on the right side
				}// end trapped on the right side
				// trapped on a general place on the x-axis
				else 
				{
					// trapped on top general point
					if(pivot_y==0) 
					{
						// go to the left pixel
						if(img[pivot_x-1]== 0xFFFFFF) 
						{
							x=-1;
							y=0;
						}
						// go to the right pixel
						else if(img[pivot_x+1]== 0xFFFFFF) 
						{
							x=0;
							y=+1;
						}
						// go to the left-bottom pixel
						else if(img[pivot_x-1 + 1*width]== 0xFFFFFF) 
						{
							x=-1;
							y=+1;
						}
						// go to the right-bottom pixel
						else if(img[pivot_x+1 + 1*width]== 0xFFFFFF) 
						{
							x=+1;
							y=+1;
						}
						pivot_x_old=pivot_x;
						pivot_y_old=pivot_y;
						img[pivot_x+x+width*(y+pivot_y)] = border_color; // border
						pivot_x+=x;
						pivot_y+=y;
						break;
					}// end trapped on top general point
					// trapped on bottom general point
					else if(pivot_y==height-1) 
					{
						// go to the left pixel
						if(img[pivot_x-1 + pivot_y*width]== 0xFFFFFF) 
						{
							x=-1;
							y=0;
						}
						// go to the right pixel
						else if(img[pivot_x+1 + pivot_y*width]== 0xFFFFFF) 
						{
							x=+1;
							y=0;
						}
						// go to the left-top pixel
						else if(img[pivot_x-1 + (pivot_y-1)*width]== 0xFFFFFF) 
						{
							x=-1;
							y=-1;
						}
						// go to the right-top pixel
						else if(img[pivot_x+1 + (pivot_y-1)*width]== 0xFFFFFF) 
						{
							x=+1;
							y=-1;
						}
						// go to the top pixel
						else if(img[pivot_x + (pivot_y-1)*width]== 0xFFFFFF) 
						{
							x=0;
							y=-1;
						}
						pivot_x_old=pivot_x;
						pivot_y_old=pivot_y;
						img[pivot_x+x+width*(y+pivot_y)] = border_color; // border
						pivot_x+=x;
						pivot_y+=y;
						break;
					}// end trapped on bottom general point
					// not trapped
					else if(img[pivot_x+x+width*(y+pivot_y)]==0xFFFFFF ||
							(pivot_x+x == x_start && pivot_y+y == y_start))
					{
						x = (int) (Math.cos(0.785 * i)*1.5);
						y = (int) (Math.sin(0.785 * i)*-1.5);
						pivot_x_old=pivot_x;
						pivot_y_old=pivot_y;
						img[pivot_x+x+width*(y+pivot_y)] = border_color; // border
						pivot_x+=x;
						pivot_y+=y;
						break;
					}// end not trapped
				}// end trapped on x general point

			}
			if (trials == 7)
			{
				break;
			}
		}
		boolean abc=false;
		if(abc)
		{
			writeImageToFile(img, width, height);
		}


		return 0;
	}

	public void writeImageToFile(int[] img,int width,int height)
	{

		File f = new File("C:\\img");
		try
		{
			OutputStreamWriter osr = new OutputStreamWriter(new FileOutputStream(f));
			for(int ii=0;ii<width;ii++)
			{
				for(int iii=0;iii<height;iii++)
				{
					try
					{
						osr.write(img[ii + iii*width]);
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try
				{
					osr.write("\n");
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/* (non-Javadoc)
	 * @see utils.video.filters.RatFinder.RatFinder#specialConfiguration(utils.video.filters.FilterConfigs)
	 */
	@Override
	protected void specialConfiguration(FilterConfigs configs)
	{
		// TODO Auto-generated method stub
	}







}
