package com.philips.cl.di.digitalcare.locatephilips;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * GpsHelper class helps to track the GPS co-ordinates.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 8 May 2015
 */
public class GpsHelper {

	private Context context = null;;
	// flag for GPS Status
	private boolean isGPSEnabled = false;
	// flag for network status
	private boolean isNetworkEnabled = false;
	private LocationManager locationManager = null;;
	private Location location = null;
	private double mLatitude = 0.0;
	private double mLongitude = 0.0;

	public GpsHelper(Context context) {
		this.context = context;

		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		getMyLocation();
	}

	private void getMyLocation() {
		List<String> providers = locationManager.getProviders(true);

		Location l = null;
		for (int i = 0; i < providers.size(); i++) {
			l = locationManager.getLastKnownLocation(providers.get(i));
			if (l != null)
				break;
		}
		if (l != null) {
			mLatitude = l.getLatitude();
			mLongitude = l.getLongitude();
		}
	}

	public boolean isGPSenabled() {
		isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// getting network status
		isNetworkEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		return (isGPSEnabled || isNetworkEnabled);
	}

	/**
	 * Function to get latitude
	 */
	public double getLatitude() {
		return mLatitude;
	}

	/**
	 * Function to get longitude
	 */
	public double getLongitude() {
		return mLongitude;
	}
}
