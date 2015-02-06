package com.philips.cl.di.dev.pa.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Build;
import android.widget.ImageView;

public class DashboardUtil {
	
	public static ArrayList<Point> getCircularBoundary(Point imageCenter, int radius, int numOfPoints) {
		ArrayList<Point> points = new ArrayList<Point>();
		int angle;
		for (int i = 0; i < numOfPoints; i++) {
			angle = i * (360 / numOfPoints);
			Point boundaryPoint = new Point();
			boundaryPoint.x = (int) (imageCenter.x + radius	* Math.cos(Math.toRadians(angle)));
			boundaryPoint.y = (int) (imageCenter.y + radius	* Math.sin(Math.toRadians(angle)));
			points.add(boundaryPoint);
		}
		return points;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setAplha(ImageView ImageView, int alphaInt, float alphaFloat) {
		if (ImageView != null) {
			if (isAPILessThanHoneycomb()) {
				ImageView.setAlpha(alphaInt);
			} else {
				ImageView.setAlpha(alphaFloat);
			}
		}
	}
	
	public static boolean isAPILessThanHoneycomb() {
		return Build.VERSION.SDK_INT < 11 ? true : false;
	}
	
	public static int getRundomNumber(int min, int max) {
		int num = 450;
		Random random = new Random();
		num = random.nextInt(max - min) + 1 + min;
		return num;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentTime24HrFormat() {
		SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
		return formatTime.format(new Date());
	}
}
