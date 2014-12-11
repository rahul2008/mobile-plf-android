package com.philips.cl.di.dev.pa.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.constant.AppConstants;

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
	
	public static void shareDashboardScreen(Context context, 
			File outputFile, String subject, String message, String pakage, String activity) {
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setClassName(pakage, activity);
		
		if (AppConstants.EMAIL_PAKAGE.equals(pakage)) {
			sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
			sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
		} else /*if (!AppConstants.WECHAT_PAKAGE.equals(pakage))*/{
			sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
		}
        
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputFile));
        sharingIntent.setType("image/*");
        context.startActivity(sharingIntent);  
	}
	
	public static HashMap<String, String> getSharingAppList(Context context) {
		PackageManager pm = context.getPackageManager();
	    Intent sendIntent = new Intent(Intent.ACTION_SEND);     
	    sendIntent.setType("image/*");

	    List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
	    HashMap<String, String> activityMap = new HashMap<String, String>();
	    for (int i = 0; i < resInfo.size(); i++) {
	        ResolveInfo resolveInfo = resInfo.get(i);
	        if (resolveInfo != null) {
	        	ActivityInfo activityInfo = resolveInfo.activityInfo;
	        	if (activityInfo != null) {
	        		String activity = activityInfo.name;
	        		String packageName = activityInfo.packageName;
	        		if(AppConstants.WECHAT_PAKAGE.equals(packageName) 
	        				|| AppConstants.WEIBO_PAKAGE.equals(packageName) 
		        			|| AppConstants.MMS_PAKAGE.equals(packageName) 
		        			|| AppConstants.EMAIL_PAKAGE.equals(packageName)
		        			|| AppConstants.FACEBOOK_PAKAGE.equals(packageName) 
		        			|| AppConstants.TWITTER_PAKAGE.equals(packageName)) {
	        			activityMap.put(packageName, activity);
	        		}
	        		
	        		if (AppConstants.WECHAT_PAKAGE.equals(packageName)) {
	        			activityMap.put(packageName, "com.tencent.mm.ui.tools.ShareImgUI");
	        		}
	        	}
	        }
	    }
	    return activityMap;
	}
	
}
