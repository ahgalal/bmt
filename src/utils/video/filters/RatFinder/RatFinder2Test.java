/**
 * 
 */
package utils.video.filters.RatFinder;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.video.filters.Link;

/**
 * @author Creative
 *
 */
public class RatFinder2Test
{
	int width=10;
	RatFinder2 rf2;
	int[] img;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		
		img=new int[100];
		
		img[3+width*1]=255;
		img[4+width*1]=255;
		
		img[2+width*2]=255;
		img[3+width*2]=255;
		img[4+width*2]=255;
		img[5+width*2]=255;
		
		img[3+width*3]=255;
		img[4+width*3]=255;
		
		Link lin,lout;
		lin=new Link(new Point(10,10));
		lout=new Link(new Point(10,10));
		rf2 = new RatFinder2("a", lin, lout);
		
		lin.setData(img);
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * Test method for {@link utils.video.filters.RatFinder.RatFinder2#contourEdge(int[], int, int, int, int)}.
	 */
	@Test
	public final void testContourEdge()
	{
		rf2.contourEdge(img, 2, 2, width, width);
		fail("Not yet implemented"); // TODO
	}

}
