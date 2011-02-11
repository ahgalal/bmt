package utils.video.input;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import javax.media.Buffer;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;

public class JMFCamFrame {
	private Buffer buffer = null;
	private BufferedImage displayable_bufimg;
	private BufferedImage tmp_bufimg;
	public static byte[] intRGB2ByteRGB(int[] dImg_1DimInt) { //4ms

		byte[] res = new byte[dImg_1DimInt.length*3];
		for(int i=0;i<res.length;i+=3)
		{
			res[i]   = (byte) ((byte) dImg_1DimInt[i/3]&(255)); //B
			res[i+1] = (byte) ((dImg_1DimInt[i/3]>>8)&(255)); //G
			res[i+2] = (byte) ((byte)(dImg_1DimInt[i/3]>>16)&(255)); //R

		}

		return res;
	}
	public Buffer getBuffer()
	{
		return buffer;
	}
	
	public byte[] convertYUV2RGB(byte[] yuv_data1)
	{
		byte[] yuv_data = (byte[]) buffer.getData();
		byte[] rgb_data=new byte[width*height*3];
		
		int rgb_row=height-1;
		int rgb_ptr=width*rgb_row*3;
		
		for(int i=0;i<yuv_data.length;i+=4)
		{
			int y1  = yuv_data[i+0] & 0xff;
			int u = yuv_data[i+1]& 0xff;
			int y2  = yuv_data[i+2]& 0xff;
			int v = yuv_data[i+3]& 0xff;
			
			//System.out.print("y1:" + y1 +"  u1:" + u +"  y2:" + y2 +"  v:" + v + "\n");

			int b1,r1,g1,r2,g2,b2;
			
			b1 =  (int) ((y1 + 1.722*(u - 128)) );
			g1 =  (int) ((y1 - 0.714*(v - 128) - 0.344*(u - 128)) );
			r1 =  (int) ((y1 + 1.402*(v - 128)));

			b2 =  (int) ((y2 + 1.722*(u - 128)) );
			g2 =  (int) ((y2 - 0.714*(v - 128) - 0.344*(u - 128)));
			r2 =  (int) ((y2 + 1.402*(v - 128)));
			
			if(b1>255) b1=255;
			if(g1>255) g1=255;
			if(r1>255) r1=255;
			if(b2>255) b2=255;
			if(g2>255) g2=255;
			if(r2>255) r2=255;
			
			if(b1<0) b1=0;
			if(g1<0) g1=0;
			if(r1<0) r1=0;
			if(b2<0) b2=0;
			if(g2<0) g2=0;
			if(r2<0) r2=0;
			
			rgb_data[rgb_ptr]= (byte) b1;
			rgb_data[rgb_ptr+1]= (byte) g1;
			rgb_data[rgb_ptr+2]= (byte) r1;
			rgb_data[rgb_ptr+3]= (byte) b2;
			rgb_data[rgb_ptr+4]= (byte) g2;
			rgb_data[rgb_ptr+5]= (byte) r2;
			
			rgb_ptr+=6;
			if(rgb_ptr%(width*3)==0)
			{
				rgb_row--;
				rgb_ptr=rgb_row*width*3;
			}

		}
		return rgb_data;
	}
	
	
/*	public void drawMarkerOnDisplayableBufferedImg(int pos_x,int pos_y)
	{
		byte[] buf_data=(byte[]) buffer.getData();

		try {
			int mark_length=10;
			int j=(pos_x + (pos_y-mark_length)*width)*3;
			int i=(pos_x-mark_length + pos_y*width)*3;
			for(int c=0;c<mark_length*2;c++)//i<(pos_x+mark_length + pos_y*width)*3
			{
				if(i<0)
					i=0;
				if(j<0)
					j=0;
				if(i<0)
					i=pos_y*width*3;
				if(j<0)
					j=pos_x*3;
				buf_data[i]=buf_data[i+1]=0;
				buf_data[i+2]=(byte) 255;

				buf_data[j]=buf_data[j+1]=0;
				buf_data[j+2]=(byte) 255;
				j+=width*3;
				i+=3;
			}
			//buffer.setData(buf_data);
		} catch (Exception e) {
			System.err.print("Error ya 3am el 7ag, fel index !");
		}
	}*/


	public BufferedImage getBufImg(int ImageType)
	{
		updateDiplayableBufImg();
		tmp_bufimg = new BufferedImage(width, height, ImageType);
		tmp_bufimg.getGraphics().drawImage(displayable_bufimg, 0, 0, width, height, 0, height, width, 0, null);
		return tmp_bufimg;
	}

	private void updateDiplayableBufImg() {
		BufferToImage b2i = new BufferToImage((VideoFormat) buffer.getFormat());
		Image tmp_img = b2i.createImage(buffer);
		displayable_bufimg.getGraphics().drawImage(tmp_img, 0, 0, null);
	}

	public void updateBufferData(byte[] newdata)
	{
		buffer.setData(newdata);
	}

	public int[] getRGBIntArray()
	{
		return byteRGB2IntRGB((byte[]) buffer.getData());
	}

	public int[][] get2DIntArray()
	{
		return intArray2_2DIntArray(getRGBIntArray());
	}

	private int[][] intArray2_2DIntArray(int[] arr)
	{
		int[][] res=new int[width][height];
		for(int i=0;i<arr.length;i++)
		{
			res[i%width][i/width] = (byte)arr[i];
		}
		return res;
	}

	private int[] byteRGB2IntRGB(byte[] barr)
	{
		int[] iarr = new int[barr.length/3];
		int r,g,b;
		for(int i =0;i<barr.length;i+=3)
		{
			r=barr[i+2] & 255;
			g=barr[i+1] & 255;
			b=barr[i] & 255;
			iarr[i/3]= b|(g<<8)|(r<<16);
		}
		return iarr;
	}


	public byte[] getRGBByteArray()
	{
		return (byte[]) buffer.getData();
	}

	public byte[] getGrayByteArray()
	{
		return rgbByteArray_2_GrayByteArray((byte[]) buffer.getData());
	}

	private byte[] rgbByteArray_2_GrayByteArray(byte[] rgb_byte_arr)
	{
		byte[] gray_arr = new byte [rgb_byte_arr.length/3];
		int r,g,b;
		for(int i=0;i<rgb_byte_arr.length;i+=3)
		{
			r=rgb_byte_arr[i+2]&0xff;
			g=rgb_byte_arr[i+1]&0xff;
			b=rgb_byte_arr[i]&0xff;
			gray_arr[i/3] = (byte) (0.2989*r + 0.5870*g+ 0.1140*b);
		}
		return gray_arr;
	}

	private byte rgb2GrayValue(int r,int g,int b)
	{
		byte gray=(byte) (0.2989*r + 0.5870*g+ 0.1140*b);
		if (gray<0);
		//System.out.print(Integer.toString(r)+ " " +Integer.toString(g)+" "+Integer.toString(b)+"\n");
		return gray;
	}


	private byte[][] d_img_2_dim_gray;
	private int[] d_img_1_dim_int;
	private byte[] d_img_1_dim_byte_RGB;
	private byte[] d_img_1_dim_byte_gray;
	private BufferedImage p_rgb_img_buf;
	private int width;
	private int height;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}



	public JMFCamFrame(byte[] data,int w,int h) {
		width=w;
		height=h;
		buffer = new Buffer();
		displayable_bufimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		updateBufferData(data);
		updateDiplayableBufImg();
		constructImage(displayable_bufimg);

	}

	public BufferedImage getRgb_img_buf() {
		return p_rgb_img_buf;
	}

	public void setRgb_img_buf(BufferedImage rgbImgBuf) {
		constructImage( rgbImgBuf);
	}

	public byte[][] getImg_2_dim_gray() {
		if(d_img_2_dim_gray!=null)
			return d_img_2_dim_gray;
		else
			if(d_img_1_dim_byte_gray!=null)
			{
				d_img_2_dim_gray=singleDim2DoubleDim(d_img_1_dim_byte_gray, width, height);
				return d_img_2_dim_gray;
			}
			else
			{
				d_img_1_dim_byte_gray = rgbInt2GrayByteArray(d_img_1_dim_int);
				d_img_2_dim_gray=singleDim2DoubleDim(d_img_1_dim_byte_gray, width, height);
				return d_img_2_dim_gray;
			}
	}

	public int[] getImg_1_dim_int() {
		return d_img_1_dim_int;
	}

	public byte[] getD_img_1_dim_byte() {
		return d_img_1_dim_byte_RGB;
	}

	public byte[] getImg_1_dim_gray() {
		if (d_img_1_dim_byte_gray!=null) 
			return d_img_1_dim_byte_gray;
		else
		{
			d_img_1_dim_byte_gray=rgbInt2GrayByteArray(d_img_1_dim_int);
			return d_img_1_dim_byte_gray;
		}
	}

	public void setImage(int[] img_1Dim,int width,int height) {
		d_img_1_dim_int = img_1Dim;
		this.width=width;
		this.height=height;
	}

	//Manipulation Methods:
	private byte[] rgbInt2GrayByteArray(int[] rgb_image)
	{
		int r = 0,g = 0,b = 0,k=0;
		byte[] gray_array = new byte[rgb_image.length];
		for(int i=0;i<rgb_image.length;i++)
		{
			if(i%3==0) //Blue
				b=rgb_image[i];
			else if((i+2)%3 == 0) //Green
				g=rgb_image[i];
			else if((i+1)%3 == 0) //Red
				r=rgb_image[i];

			if(i>0 && (i+1)%3 == 0)
			{
				gray_array[k]= rgb2GrayValue(r, g, b);
				k++;
			}

			gray_array[i] = rgb2GrayValue((byte)(rgb_image[i]>>16),(byte)(rgb_image[i]>>8),(byte)(rgb_image[i]));

		}
		return gray_array;
	}

	private byte rgb2GrayValue(byte r,byte g,byte b)
	{
		return (byte) ((r+g+b)/3);
	}

	private byte[][] singleDim2DoubleDim(byte[] imgin,int width,int height)
	{
		byte[][] res=new byte[width][height];
		for(int i=0;i<imgin.length;i++)
		{
			res[i%width][i/width] = (byte)imgin[i];
		}
		return res;
	}

	public void clearFrameData()
	{
		d_img_1_dim_int = null;
		d_img_1_dim_byte_gray=null;
		d_img_2_dim_gray=null;
		d_img_1_dim_byte_RGB=null;
		p_rgb_img_buf=null;
	}

	public Graphics getGraphicsObject()
	{
		return p_rgb_img_buf.getGraphics();
	}

	private void constructImage(BufferedImage rgbImgBuf)
	{
		p_rgb_img_buf = rgbImgBuf;
		width=rgbImgBuf.getWidth();
		height=rgbImgBuf.getHeight();


		PixelGrabber pg = new PixelGrabber(rgbImgBuf, 0, 0, rgbImgBuf.getWidth(), rgbImgBuf.getHeight(), false);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		d_img_1_dim_int=p_rgb_img_buf.getRGB(0, 0, width, height, null, 0, width);
		//d_img_1_dim_int = (int[]) pg.getPixels();
		d_img_1_dim_byte_RGB = intRGB2ByteRGB(d_img_1_dim_int);

	}


}
