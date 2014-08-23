package com.philips.cl.di.dev.pa.util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;

/**
 * LocationTracker : This class provides Current Location Details.
 * 
 * Author : ritesh.jha@philips.com
 * 
 * Date : 21 Aug 2014
 */
public class LocationTracker implements AMapLocationListener {
	private LocationManagerProxy mAMapLocationManager;
	private Context mContext = null;

	public LocationTracker(Context context) {
		mContext = context;
		Log.i("testing", " LocationSourceActivity constructor ");
		init();
	}

	private void init() {
		if (mAMapLocationManager == null) {
			Log.i("testing", " LocationSourceActivity activate ");
			mAMapLocationManager = LocationManagerProxy.getInstance(mContext);
			mAMapLocationManager.setGpsEnable(true);
			mAMapLocationManager.requestLocationUpdates(
					LocationManagerProxy.GPS_PROVIDER, 2000, 10, this);
		}
	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		Log.i("testing", " LocationSourceActivity onLocationChanged aLocation  " + aLocation);
		if (aLocation != null) {
			Log.i("testing",
					" aLocation.getLatitude() : " + aLocation.getLatitude());
			Log.i("testing",
					" aLocation.getLongitude() : " + aLocation.getLongitude());
		}
	}

	public void deactivate() {
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.i("testing", " LocationSourceActivity onLocationChanged location ");
		if (location != null) {
			Log.i("testing",
					" location.getLatitude() : " + location.getLatitude());
			Log.i("testing",
					" location.getLongitude() : " + location.getLongitude());
		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
