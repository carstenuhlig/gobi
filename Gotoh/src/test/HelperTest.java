package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class HelperTest {
	String one;
	int start;
	int end;

	@Before
	public void init() {
		one = "E -1  0  0  2 -3  2  6 -3  0 -4 -3  1 -2 -3 -1 -1 -1 -3 -2 -3  1 -3  5 -1 -5";
		start = 1;
	}

	@Test
	public void test() {
		double[] blah = util.StringHelper
				.processStringToDoubleMatrix(one, start);

		double[] blah2 = new double[] { -1, 0, 0, 2, -3, 2, 6, -3, 0, -4, -3, 1, -2,
				-3, -1, -1, -1, -3, -2, -3, 1, -3, 5, -1, -5 };

//		Assert.assertArrayEquals(blah2, blah);
		for (double d : blah)
			System.out.print(d + " ");
		Assert.assertArrayEquals(blah2, blah, 0.1);
	}
}
