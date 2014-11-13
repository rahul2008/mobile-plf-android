package com.philips.cl.di.dev.pa.util;

import java.util.ArrayList;

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
}
