package com.philips.cl.di.dev.pa.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.GPSLocation;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;

public class DashboardUtil {
	
	public static final long SAVE_FREESPACE_BYTE = 5 * 1024 * 1024;
	
	public enum Detail {
		INDOOR, OUTDOOR
	}
	
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
	
	public static boolean isNoPurifierMode(Bundle bundle) {
		boolean purifierMode = false;
		GPSLocation.getInstance().requestGPSLocation();
		if (bundle != null) {
			if(Utils.getUserGPSPermission() && !GPSLocation.getInstance().isLocationEnabled()) {
				//TODO : Show pop-up inviting the user to enable GPS
			} else {
				//Add user location to dashboard
				Location location = GPSLocation.getInstance().getGPSLocation();
				ALog.i(ALog.OUTDOOR_LOCATION, "My location " + location);
				if(location != null) {
					OutdoorController.getInstance().startGetAreaIDTask(location.getLongitude(), location.getLatitude());
				}
			}
			purifierMode = bundle.getBoolean(AppConstants.NO_PURIFIER_FLOW, false);
		} else {
			purifierMode = false;
		}
		return purifierMode;
	}
	
	public static int getIndoorPageCount() {
		int countIndoor = 0;
		//For demo mode
		if (PurAirApplication.isDemoModeEnable()) {
			countIndoor = 1;
		} else if (DiscoveryManager.getInstance().getStoreDevices().size() > 0) {
			countIndoor = DiscoveryManager.getInstance().getStoreDevices().size() ;

			AirPurifier purifier = DiscoveryManager.getInstance().getStoreDevices().get(0);
			if(purifier != null) {
				AirPurifierManager.getInstance().setCurrentPurifier(purifier);
			}
		}
		return countIndoor;
	}
	
	public static int getOutdoorPageCount() {
    	int count = 0;
		OutdoorManager.getInstance().processDataBaseInfo();
		List<String> myCityList = OutdoorManager.getInstance().getUsersCitiesList() ;
		if( myCityList != null ) {
			count = myCityList.size() ;
		}
		
		return count;
    }
	
	public static void startCurrentCityAreaIdTask() {
		String lat = LocationUtils.getCurrentLocationLat();
		String lon = LocationUtils.getCurrentLocationLon();
		if (!lat.isEmpty() && !lon.isEmpty()) {
			try {
				OutdoorController.getInstance().startGetAreaIDTask(Double.parseDouble(lon), Double.parseDouble(lat));
			} catch (NumberFormatException e) {
				ALog.e(ALog.ERROR, "OutdoorLocationFragment$showCurrentCityVisibility: " + "Error: " + e.getMessage());
			}
		}
	}
	
	

	 public static boolean isHaveSDCard() {
	        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	    }
	 
	 public static long freeSpaceOnSd_BYTE() {
	        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	        long sdFreeKB = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
	        return (long) sdFreeKB;
	    }
	    
	    public static Bitmap captureView(View paramView)
	    {
	    	
	    	Bitmap localBitmap = Bitmap.createBitmap(paramView.getWidth(),paramView.getHeight(),Bitmap.Config.ARGB_8888);
	    	Canvas canvas = new Canvas(localBitmap);
	    	canvas.drawColor(Color.WHITE);
	    	paramView.draw(canvas);
	    	return localBitmap;
	      
	    }
	
}
