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
package filters.ratfinder;

import static org.junit.Assert.fail;

import java.awt.Point;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import filters.Link;


/**
 * @author Creative
 */
public class RatFinder2Test {
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	int[]		img;

	RatFinder2	rf2;

	int			width	= 10;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		img = new int[100];

		img[3 + width * 1] = 255;
		img[4 + width * 1] = 255;

		img[2 + width * 2] = 255;
		img[3 + width * 2] = 255;
		img[4 + width * 2] = 255;
		img[5 + width * 2] = 255;

		img[3 + width * 3] = 255;
		img[4 + width * 3] = 255;

		Link lin, lout;
		lin = new Link(new Point(10, 10));
		lout = new Link(new Point(10, 10));
		rf2 = new RatFinder2("a", lin, lout);

		lin.setData(img);

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link utils.video.filters.ratFinder.RatFinder2#contourEdge(int[], int, int, int, int)}
	 * .
	 */
	@Test
	public final void testContourEdge() {
		rf2.contourEdge(img, 2, 2, width, width);
		fail("Not yet implemented"); // TODO
	}

}
