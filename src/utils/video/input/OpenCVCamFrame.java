package utils.video.input;


public class OpenCVCamFrame{
	//private Buffer buffer = null;
	private int width;
	private int height;
	//private BufferedImage displayable_bufimg,tmp_bufimg;

/*	public CamFrame(VideoFormat format , byte[] data) {

		buffer = new Buffer();
		buffer.setData(data);
		buffer.setFormat(format);
		width = format.getSize().width;
		height = format.getSize().height;
		displayable_bufimg = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	}*/

	public static byte[] intRGB2ByteRGB(int[] dImg_1DimInt) { //4ms

		byte[] res = new byte[dImg_1DimInt.length*3];
		for(int i=0;i<res.length;i+=3)
		{
			res[i]=(byte) ((byte) dImg_1DimInt[i/3]&(255)); //B
			res[i+1]= (byte) ((dImg_1DimInt[i/3]>>8)&(255)); //G
			res[i+2]=(byte) ((byte)(dImg_1DimInt[i/3]>>16)&(255)); //R

		}

		return res;
	}
/*	public Buffer getBuffer()
	{
		return buffer;
	}*/

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
	}
*/

/*	public BufferedImage getBufImg(int ImageType)
	{
		updateDiplayableBufImg();
		tmp_bufimg = new BufferedImage(width, height, ImageType);
		tmp_bufimg.getGraphics().drawImage(displayable_bufimg, 0, 0, width, height, 0, height, width, 0, null);
		return tmp_bufimg;
	}*/

/*	private void updateDiplayableBufImg() {
		BufferToImage b2i = new BufferToImage((VideoFormat) buffer.getFormat());
		Image tmp_img = b2i.createImage(buffer);
		displayable_bufimg.getGraphics().drawImage(tmp_img, 0, 0, null);
	}
*/
/*	public void updateBufferData(byte[] newdata)
	{
		buffer.setData(newdata);
	}

	public int[] getRGBIntArray()
	{
		return byteRGB2IntRGB((byte[]) buffer.getData());
	}*/

/*	public int[][] get2DIntArray()
	{
		return intArray2_2DIntArray(getRGBIntArray());
	}*/

	@SuppressWarnings("unused")
	private int[][] intArray2_2DIntArray(int[] arr)
	{
		int[][] res=new int[width][height];
		for(int i=0;i<arr.length;i++)
		{
			res[i%width][i/width] = (byte)arr[i];
		}
		return res;
	}

	@SuppressWarnings("unused")
	private int[] byteRGB2IntRGB(byte[] barr)
	{
		int[] iarr = new int[barr.length/3];
		int r,g,b;
		for(int i =0;i<barr.length;i+=3)
		{
			r=barr[i+2];
			g=barr[i+1];
			b=barr[i];
			iarr[i/3]= b|(g<<8)|(r<<16);
		}
		return iarr;
	}


/*	public byte[] getRGBByteArray()
	{
		return (byte[]) buffer.getData();
	}

	public byte[] getGrayByteArray()
	{
		return rgbByteArray_2_GrayByteArray((byte[]) buffer.getData());
	}*/

	@SuppressWarnings("unused")
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

/*	private byte rgb2GrayValue(int r,int g,int b)
	{
		byte gray=(byte) (0.2989*r + 0.5870*g+ 0.1140*b);
		if (gray<0);
		//System.out.print(Integer.toString(r)+ " " +Integer.toString(g)+" "+Integer.toString(b)+"\n");
		return gray;
	}
*/

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/*
	private byte[][] d_img_2_dim_gray;
	private int[] d_img_1_dim_int;
	private byte[] d_img_1_dim_byte;
	private byte[] d_img_1_dim_gray;
	private BufferedImage p_rgb_img_buf;
	private int width;
	private int height;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}



	public CamFrame(BufferedImage rgbImgBuf) {
		constructImage(rgbImgBuf);

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
			if(d_img_1_dim_gray!=null)
			{
				d_img_2_dim_gray=singleDim2DoubleDim(d_img_1_dim_gray, width, height);
				return d_img_2_dim_gray;
			}
			else
			{
				d_img_1_dim_gray = rgbInt2GrayByteArray(d_img_1_dim_int);
				d_img_2_dim_gray=singleDim2DoubleDim(d_img_1_dim_gray, width, height);
				return d_img_2_dim_gray;
			}
	}

	public int[] getImg_1_dim_int() {
		return d_img_1_dim_int;
	}

	public byte[] getD_img_1_dim_byte() {
		return d_img_1_dim_byte;
	}

	public byte[] getImg_1_dim_gray() {
		if (d_img_1_dim_gray!=null) 
			return d_img_1_dim_gray;
		else
		{
			d_img_1_dim_gray=rgbInt2GrayByteArray(d_img_1_dim_int);
			return d_img_1_dim_gray;
		}
	}

		public void setImage(int[] img_1Dim,int width,int height) {
		d_img_1_dim = img_1Dim;
		this.width=width;
		this.height=height;
	}

	//Manipulation Methods:
	private byte[] rgbInt2GrayByteArray(int[] rgb_image)
	{
		//int r = 0,g = 0,b = 0,k=0;
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

	private int[][] singleDim2DoubleDim(int[] imgin,int width,int height)
	{
		int[][] res=new int[width][height];
		for(int i=0;i<imgin.length;i++)
		{
			res[i%width][i/width] = (int)imgin[i];
		}
		return res;
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
		d_img_1_dim_gray=null;
		d_img_2_dim_gray=null;
		d_img_1_dim_byte=null;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		d_img_1_dim_int=p_rgb_img_buf.getRGB(0, 0, width, height, null, 0, width);
		//d_img_1_dim_int = (int[]) pg.getPixels();
		d_img_1_dim_byte = intRGB2ByteRGB(d_img_1_dim_int);

	}

	public static byte[] intRGB2ByteRGB(int[] dImg_1DimInt) {
		byte[] res = new byte[dImg_1DimInt.length*3];
		byte a;
		for(int i=0;i<res.length;i+=3)
		{
			a=(byte) ((byte)(dImg_1DimInt[i/3]>>24)&(255));
			res[i]=(byte) ((byte) dImg_1DimInt[i/3]&(255)); //B
			res[i+1]= (byte) ((dImg_1DimInt[i/3]>>8)&(255)); //G
			res[i+2]=(byte) ((byte)(dImg_1DimInt[i/3]>>16)&(255)); //R

		}



		return res;
	}


	 */
}
