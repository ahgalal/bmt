/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

/**
 * 
 */
package utils.video.input;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import sun.awt.image.IntegerInterleavedRaster;
import utils.Utils;
import utils.video.FrameIntArray;

import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;

/**
 * @author Creative
 */
public class JavaCVFileModule extends VidInputter<AGVidLibConfigs> {
	
	//private int[][] imgBuffer = new int[10][];
	//private int consumedAmount,writtenAmount;
	private final int BUF_SIZE=10;
	private final int BACK_BUF_SIZE=BUF_SIZE+5;
	private ArrayBlockingQueue<FrameIntArray> blockingQueue= new ArrayBlockingQueue<FrameIntArray>(BUF_SIZE);
	
	private class RunnableBufferFiller implements Runnable{

		@Override
		public void run() {
			final Point dims = new Point();
			dims.x=grabber.getImageWidth();
			dims.y=grabber.getImageHeight();
			BufferedImage bufferedImage = new BufferedImage(dims.x, dims.y,BufferedImage.TYPE_INT_RGB);
			IntegerInterleavedRaster integerInterleavedRaster = (IntegerInterleavedRaster)bufferedImage.getRaster();
			int[] im = integerInterleavedRaster.getDataStorage();
			int[] intBuffer = null;
			int frames=0;
			FrameIntArray[] backBuffer=new FrameIntArray[BACK_BUF_SIZE];
			// initialize buffer
			for(int i=0;i<backBuffer.length;i++){
				FrameIntArray frameIntArray= new FrameIntArray();
				frameIntArray.setFrameData(new int[im.length]);
				backBuffer[i]=frameIntArray;
			}
			
			while (!stopStream) {

				long t1=(long) (System.nanoTime()/1E6);
				IplImage iplImage = null;
				try {
					if (status == SourceStatus.END_OF_STREAM)
						continue;
					iplImage = grabber.grab();

				} catch (Exception e1) {
					status = SourceStatus.END_OF_STREAM;
					continue;
				}
				
				JavaCVUtils.updateImageData(im,iplImage,intBuffer);
				
				FrameIntArray frameIntArray = getNextFrameIntArray(backBuffer);
				
				System.arraycopy(im, 0, frameIntArray.getFrameData(), 0,
						im.length);
				frames++;
				//System.out.println("frames: "+frames);
				
				Utils.sleep(7);
				putFrameIntoBuffer(frameIntArray);
				long t2=(long) (System.nanoTime()/1E6);
				//System.out.println(t2-t1 + " " + blockingQueue.size());
				
			}
		}

		private void putFrameIntoBuffer(FrameIntArray frameIntArray) {
			try {
				blockingQueue.put(frameIntArray);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private FrameIntArray getNextFrameIntArray(FrameIntArray[] backBuffer) {
			int found=0;
			FrameIntArray frameIntArray = null;
			for(int i=0;i<backBuffer.length;i++)
				if(blockingQueue.contains(backBuffer[i])){
					continue;
				}else{
					if(found<2)
						found++;
					else{
						frameIntArray=backBuffer[i];
						break;
					}
				}
			return frameIntArray;
		}

	}
	
	private int frameRate = 0;
	private long streamLengthTime=0;
	private long streamPositionTime=0;
	private class RunnableJavaCV implements Runnable {
		@Override
		public void run() {
			
			int frameInterval=0;
			final Point dims = new Point();
			dims.x=grabber.getImageWidth();
			dims.y=grabber.getImageHeight();
			
			frameRate=(int) grabber.getFrameRate();
			streamLengthTime=grabber.getLengthInTime();
			frameInterval=1000/frameRate;
			System.out.println("frameRate= "+frameRate + " stream length: "+streamLengthTime);

			Utils.sleep(100);
			int updateStreamTimeFlag=0;
			int delayTime=0;
			int prevLoopTime=0;
			long lastFrameTimeStamp=0;
			int[] im = new int[dims.x*dims.y];
			fia.setFrameData(new int[im.length]);
			long t3 = 0;
			while (!stopStream) {
				synchronized (this) {
					while (paused) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							// set lastFrameTimeStamp to 0 after resume
							lastFrameTimeStamp=0;
						}
					}
				}
				long t1=System.nanoTime();
				
				
				if(lastFrameTimeStamp==0){
					lastFrameTimeStamp=getCurrentTimeMilli();
					im = grabFrameFromBuffer();
				}else{
					long currentTimeStamp=getCurrentTimeMilli();
					long timeDiff = currentTimeStamp-lastFrameTimeStamp;
					int framesSkipped=Math.round(timeDiff/frameInterval);
					//System.out.println("time diff: "+timeDiff+" skipped frames: "+ framesSkipped);
					lastFrameTimeStamp = currentTimeStamp;
					for(int i=0;i<framesSkipped;i++)
						im = grabFrameFromBuffer();
				}
				if(im!=null){
					System.arraycopy(im, 0, fia.getFrameData(), 0, im.length);
					//fia.setFrameData(im);
				}

				updateStatus();

				if(updateStreamTimeFlag%25==0){
					streamPositionTime=grabber.getTimestamp();
					updateStreamTimeFlag=0;
				}
				updateStreamTimeFlag++;
				
				long t2=System.nanoTime();
/*				long frameDelta;
				if(prevLoopTime==0)
					frameDelta= (long) ((t2-t1)/1E6);
				else
					frameDelta= prevLoopTime;*/
				long sleepTime=0;//=(long) (frameInterval-frameDelta);
/*				if(sleepTime<0){
					delayTime+=sleepTime*-1;
					//System.out.println("delay time: "+delayTime);
					sleepTime=1;
				}else{
					if(delayTime>30 && sleepTime-9>0){
						sleepTime=sleepTime-9;
						delayTime-=9;
					}
				}*/
				sleepTime=frameInterval; // TODO: fix this to get real time video playback
				//LockSupport.parkNanos((long) (sleepTime*1000000));
				
				Utils.sleep((int) sleepTime);
				//sleep(sleepTime);
				t3=System.nanoTime();
				prevLoopTime=(int) ((t3-t1)/1E6);
				//System.out.println("sleep time: "+ sleepTime+" actual time: "+ (t3-t2_2));
				//System.out.println(/*"loop time: "+*/prevLoopTime /*+ " sleep time: "+sleepTime + " delay time: "+ delayTime + " frame delta: " + frameDelta*/);
				//System.out.println(frameDelta + "\t"+delayTime + "\t" + sleepTime);
			}
		}

		private long getCurrentTimeMilli() {
			return (long) (System.nanoTime()/1E6);
		}

		private int[] grabFrameFromBuffer() {
			int[] im = null;
			try {
				FrameIntArray fia = blockingQueue.poll(1000, TimeUnit.MILLISECONDS);
				if(fia!=null)
					im=fia.getFrameData();
				else
					System.out.println("CAN'T GET NEW FRAME");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return im;
		}
	}
	
	private void sleep(long sleepTime){
		opencv_highgui.cvWaitKey((int) sleepTime);
		
/*		long t1=System.nanoTime();
		long sleepTimeNano=(long) (sleepTime*1E6);
		int diff=0;
		while(diff<sleepTimeNano){
			diff=(int) (System.nanoTime()-t1);
			if(diff<sleepTimeNano)
				Utils.sleep(1);
		}*/
	}
	
	private boolean			stopStream;

	private Thread			thUpdateImage;
	private Thread			thFillBuffer;

	private OpenCVFrameGrabber	grabber;

	@Override
	public int getStreamPosition() {
		return (int) streamPositionTime/1000;
	}

	@Override
	public int getStreamLength() {
		return (int) streamLengthTime/1000;
	}
	/**
	 * 
	 */
	public JavaCVFileModule() {/*
		Thread threadDead=new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					long t1=System.currentTimeMillis();
					try {
						Thread.sleep(Long.MAX_VALUE);
					} catch (InterruptedException e) {
					}
					long t2=System.currentTimeMillis();
				}
			}
		});
		threadDead.start();
	*/}

	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#displayMoreSettings()
	 */
	@Override
	public int displayMoreSettings() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#getName()
	 */
	@Override
	public String getName() {
		return "JavaCVFile";
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#getNumCams()
	 */
	@Override
	public int getNumCams() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#getStatus()
	 */
	@Override
	public SourceStatus getStatus() {
		return status;
	}

	@Override
	public boolean initialize(final FrameIntArray frameData,
			final AGVidLibConfigs configs) {
		this.configs = configs;
		fia = frameData;
		grabber=new OpenCVFrameGrabber(configs.getVideoFilePath());
		try {
			grabber.start();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#setFormat(java.lang.String)
	 */
	@Override
	public void setFormat(final String format) {
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#startStream()
	 */
	@Override
	public boolean startStream() {
		stopStream = false;
		
		thFillBuffer = new Thread(new RunnableBufferFiller(),"JavaCVBufferFiller");
		thFillBuffer.start();
		
		Utils.sleep(100);
		
		thUpdateImage = new Thread(new RunnableJavaCV(),"JavaCVFile");
		thUpdateImage.start();
		
		
		
		return true;
	}
	@Override
	public utils.video.input.VidInputter.SourceType getType() {
		return SourceType.FILE;
	}
	/*
	 * (non-Javadoc)
	 * @see utils.video.input.VidInputter#stopModule()
	 */
	@Override
	public void stopModule() {
		stopStream = true;
		Utils.sleep(200);
		try {
			grabber.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void resumeStream() {
		paused=false;
		thUpdateImage.interrupt();		
	}

}
