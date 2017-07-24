package com.philips.cl.di.dev.pa.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import android.graphics.Point;

import com.philips.cl.di.dev.pa.util.DashboardUtil;

public class DashboardUtilTest extends TestCase {
	
	public void testGetCircularBoundarySixPoint() {
		Point imageCenter = new Point();
		imageCenter.x = 50;
		imageCenter.y = 50;
		ArrayList<Point> points = DashboardUtil.getCircularBoundary(imageCenter, 50, 6);
		assertEquals(6, points.size());
	}
	
	public void testGetCircularBoundaryFourPoint() {
		Point imageCenter = new Point();
		imageCenter.x = 50;
		imageCenter.y = 50;
		ArrayList<Point> points = DashboardUtil.getCircularBoundary(imageCenter, 50, 4);
		assertEquals(4, points.size());
	}
	
	public void testGetCircularBoundarySixPointNoCenter() {
		Point imageCenter = new Point();
		ArrayList<Point> points = DashboardUtil.getCircularBoundary(imageCenter, 50, 6);
		assertEquals(6, points.size());
	}
	
	public void testGetRundomNumber() {
		int min = 100;
		int max = 500;
		
		int randomNumber1 = DashboardUtil.getRundomNumber(min, max);
		int randomNumber2 = DashboardUtil.getRundomNumber(min, max);
		
		assertFalse(randomNumber1 == randomNumber2);
	}
	
	public void testGetCurrentTime24HrFormat() {
		String time = DashboardUtil.getCurrentTime24HrFormat();
		assertEquals(5, time.length());
	}
	
	public void testIsNoPurifierMode() {
		boolean noPurifierMode = DashboardUtil.isNoPurifierMode(null);
		assertFalse(noPurifierMode);
	}
	
}
