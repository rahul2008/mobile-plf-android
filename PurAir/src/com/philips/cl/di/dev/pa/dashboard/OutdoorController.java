package com.philips.cl.di.dev.pa.dashboard;

import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.fragment.StartFlowDialogFragment;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorLocationDatabase;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LocationUtils;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;

//TODO : Remove this class for next version.
public class OutdoorController implements ServerResponseListener, AMapLocationListener {

	private LocationManagerProxy mAMapLocationManager;
	private double latitude;
	private double longitude;
	private Location location;
	
	private static OutdoorController smInstance;
	private Activity mActivity;
	private static boolean lastKnownLocationFound = false;
	private boolean providerEnabled = false;
	
	private CurrentCityAreaIdReceivedListener areaIdReceivedListener;

	private OutdoorController() {
		lastKnownLocationFound = false;
		setLocationProvider();
	}
	
	public void setLocationProvider() {
		mAMapLocationManager = LocationManagerProxy.getInstance(PurAirApplication.getAppContext());
		
		List<String> providers = mAMapLocationManager.getAllProviders();
		ALog.i(ALog.TEMP, "Providers " + providers +": " + providerEnabled);
		for (String provider : providers) {
			if(mAMapLocationManager.isProviderEnabled(provider)) {
				ALog.i(ALog.TEMP, "Enabled provider " + provider + " : " + providerEnabled);
				providerEnabled = true;
				mAMapLocationManager.setGpsEnable(true);
				mAMapLocationManager.requestLocationUpdates(provider, 2000, 10, this);
				Location loc = mAMapLocationManager.getLastKnownLocation(provider);
				if(loc != null) { 
					location = loc;
					startGetAreaIDTask(loc.getLongitude(), loc.getLatitude());
				}
			}
		}
	}

	public static OutdoorController getInstance() {
		if(smInstance == null) {
			smInstance = new OutdoorController();
		}
		return smInstance;
	}

	@Override
	public void receiveServerResponse(int responseCode, String data, String areaID) {
		ALog.i(ALog.DASHBOARD, "OutdoorController data received " + data + " responseCode " + responseCode + " areaID " + areaID);
		if (!areaID.isEmpty() && areaID.equals("from_lat_long") && data != null && !data.isEmpty()) {
			processAreaID(data);
		} 
	}
	
	private void processAreaID(String data) {
		String areaId = parseAreaID(data);
		if (areaId.isEmpty()) {
			lastKnownLocationFound = false;
			done=false;
			ALog.i(ALog.OUTDOOR_LOCATION, "Current location task failed");
			return;
		}
		
		if (!LocationUtils.getCurrentLocationAreaId().isEmpty() && !areaId.isEmpty() 
				&& LocationUtils.getCurrentLocationAreaId().equals(areaId)) {
			ALog.i(ALog.OUTDOOR_LOCATION, "Current location same");
			return;
		}
		ALog.i(ALog.OUTDOOR_LOCATION, "Current location new");
		LocationUtils.saveCurrentLocationAreaId(areaId);
		OutdoorManager.getInstance().addCurrentCityAreaIDToUsersList(areaId);
		areaIdReceived();//Listen to outdoor location fragment
		
		addMyLocationToMap(areaId);
		//For download outdoor AQI and weather detail, resetting lastUpdatedTime to zero
		OutdoorManager.getInstance().resetUpdatedTime();
		OutdoorManager.getInstance().startCitiesTask();
	}

	public void addMyLocationToMap(String areaId) {
		OutdoorLocationDatabase database =  new OutdoorLocationDatabase();
		database.open();
		Cursor c = database.getDataCurrentLoacation(areaId);
		if (c != null && c.getCount() == 1) {
			c.moveToFirst();
			String city = c.getString(c.getColumnIndex(AppConstants.KEY_CITY));
			String cityCN = c.getString(c.getColumnIndex(AppConstants.KEY_CITY_CN));
			String cityTW = c.getString(c.getColumnIndex(AppConstants.KEY_CITY_TW));
			float longitude = c.getFloat(c.getColumnIndex(AppConstants.KEY_LONGITUDE));
			float latitude = c.getFloat(c.getColumnIndex(AppConstants.KEY_LATITUDE));

			OutdoorCityInfo info = new OutdoorCityInfo(city, cityCN, cityTW, longitude, latitude, areaId,  999/*TODO : Remove this*/);
			OutdoorManager.getInstance().addCityDataToMap(info, null, null, areaId);
		}
		database.close();
	}

	private String parseAreaID(String data) {
		String[] areaIDResponse = data.split(",");
		String newAreaID = "";
		if (areaIDResponse != null && areaIDResponse.length > 0) {
			String[] areaIDSplit = areaIDResponse[0].split(":");
			if (areaIDSplit != null && areaIDSplit.length > 1) {
				newAreaID = areaIDSplit[1];
			}
		}
		return newAreaID;
	}

	public boolean isPhilipsSetupWifiSelected() {
		String ssid = EWSWifiManager.getSsidOfConnectedNetwork();
		if (ssid != null && ssid.contains(EWSWifiManager.DEVICE_SSID)) {
			return true;
		} 
		return false;
	}
	
	public Location getCurrentLocation() {
		return location;
	}
	
	public void setCurrentLocation(Location location) {
		this.location = location;
	}

	@Override
	public void onLocationChanged(Location location) {
		ALog.i(ALog.OUTDOOR_LOCATION, "onLocationChanged Android.Location " + location);
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
	
	private boolean done = false;

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		location = aLocation;
		
		if(location!=null && location.getLatitude()>0 && location.getLongitude()>0 && !LocationUtils.getCurrentLocationAreaId().isEmpty() && (GPSLocation.getInstance().isLocationEnabled()))
			showLocationServiceTurnedOnDialog();
		
		if(aLocation != null && aLocation.getLatitude() > 0 && aLocation.getLongitude() > 0 && !done) {
			latitude = aLocation.getLatitude();
			longitude = aLocation.getLongitude();
			startGetAreaIDTask(longitude, latitude);
			done = true;
		}
	}
	
	public void startGetAreaIDTask(double longitude, double latitude) {
		/**If purifier in demo mode, skip download data*/
		if (isPhilipsSetupWifiSelected() || (longitude <= 0 && latitude <= 0) || lastKnownLocationFound) {
			ALog.i(ALog.OUTDOOR_LOCATION, "Current location task all ready started");
			return;
		}
		ALog.i(ALog.OUTDOOR_LOCATION, "Current location task started");
		lastKnownLocationFound = true;
		LocationUtils.saveCurrentLocationLatLon(String.valueOf(latitude), String.valueOf(longitude));

		TaskGetHttp citiesList = new TaskGetHttp("http://data.fuwu.weather.com.cn/getareaid/findId?lat=" + latitude + "&lon=" + longitude, "from_lat_long", PurAirApplication.getAppContext(), this);
		citiesList.start();
	}
	
	public static void reset() {
		smInstance = null;
	}
	
	public void setActivity(Activity activity){
		mActivity= activity;
	}
	
	public void showLocationServiceTurnedOnDialog() {
		try {
			if(mActivity==null || !(mActivity instanceof MainActivity))return;
			
			if(!Utils.getGPSDisabledDialogShownValue() || Utils.getGPSEnabledDialogShownValue())return;
			
			Bundle mBundle= new Bundle();
			StartFlowDialogFragment mDialog = new StartFlowDialogFragment();
			mBundle.putInt(StartFlowDialogFragment.DIALOG_NUMBER, StartFlowDialogFragment.LOCATION_SERVICES_TURNED_ON);
			mDialog.setArguments(mBundle);
			mDialog.show(((MainActivity) mActivity).getSupportFragmentManager(), "start_flow_dialog");
			Utils.setGPSEnabledDialogShownValue(true);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}	
	}
	
	public boolean isProviderEnabled() {
		return providerEnabled;
	}
	
	//Add listener and listen when current city areaId received
	public void setCurrentCityAreaIdReceivedListener(CurrentCityAreaIdReceivedListener areaIdReceivedListener) {
		this.areaIdReceivedListener = areaIdReceivedListener;
	}
	
	public void removeCurrentCityAreaIdReceivedListener() {
		areaIdReceivedListener = null;
	}
	
	private void areaIdReceived() {
		if (areaIdReceivedListener != null) {
			areaIdReceivedListener.areaIdReceived();
		}
	}
	
	public interface CurrentCityAreaIdReceivedListener {
		void areaIdReceived();
	}
}