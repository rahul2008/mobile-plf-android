package com.philips.cl.di.dev.pa.dashboard;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.philips.cl.di.dev.pa.PurAirApplication;

public class GPSLocation implements LocationListener {
	
	private Location location; 
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private LocationManager locationManager;
	
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
	
	public boolean isLocationEnabled() {
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) 
				|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			return true;
		}
		return false;
	}
	
	public void requestGPSLocation() {
		if (isLocationEnabled()) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER,
					MIN_TIME_BW_UPDATES,
					MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		}
	}
	
	public Location getGPSLocation() {
		if (location == null && isLocationEnabled()) {
			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		if (location != null) {
			OutdoorController.getInstance().setCurrentLocation(location);
		}
		return location;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
		if (location != null) {
			OutdoorController.getInstance().setCurrentLocation(location);
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// NOP
	}

	@Override
	public void onProviderEnabled(String provider) {
		// NOP
	}

	@Override
	public void onProviderDisabled(String provider) {
		// NOP
	}
	
	public static void reset() {
		instance = null;
	}

}
