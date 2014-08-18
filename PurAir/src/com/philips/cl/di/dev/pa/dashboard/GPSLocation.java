package com.philips.cl.di.dev.pa.dashboard;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.util.ALog;

public class GPSLocation implements LocationListener {
	
	private Location location; 
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private LocationManager locationManager;
	private boolean stopGpsUpdate;
	
	private static GPSLocation instance;
	
	public synchronized static GPSLocation getInstance() {
		if (instance == null) {
			instance = new GPSLocation();
		}
		return instance;
	}
	
	private GPSLocation() {
		locationManager = (LocationManager) 
				PurAirApplication.getAppContext().getSystemService(Context.LOCATION_SERVICE);
		requestGPSLocation();
	}
	
	private boolean isGPSEnabled() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	private void requestGPSLocation() {
		if (isGPSEnabled()) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER,
					MIN_TIME_BW_UPDATES,
					MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		}
	}
	
	public Location getGPSLocation() {
		if (location == null && isGPSEnabled()) {
			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		return location;
	}
	
//	public void removeGPSUpdate() {
//		locationManager.removeUpdates(this);
//	}
	
	public void stopGPSListener() {
		stopGpsUpdate = true;
		ALog.i(ALog.TEMP, "Stop GPS : " + stopGpsUpdate);
	}

	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
		ALog.i(ALog.TEMP, "GPSLocation$onLocationChanged: " + location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}
	
	public static void reset() {
		instance = null;
	}

}
