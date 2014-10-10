package com.philips.cl.di.dev.pa.dashboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
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
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.fragment.StartFlowDialogFragment;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorLocationDatabase;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.LocationUtils;
import com.philips.cl.di.dev.pa.util.OutdoorDetailsListener;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;

public class OutdoorController implements ServerResponseListener, AMapLocationListener {

	private static String BASE_URL ;

	public static final String BASE_URL_AQI = "http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php";
	public static final String BASE_URL_HOURLY_FORECAST = "http://data.fuwu.weather.com.cn/getareaid/areaid?id=";
	
	public static final String AIR = "air";
	public static final String AIR_HISTORY = "air_his";
	public static final String OBSERVE = "observe";
	public static final String FORECAST_4_DAYS = "forecast4d";
	
	private List<OutdoorEventListener> outdoorEventListeners;
	private OutdoorDetailsListener outdoorDetailsListener ;

	private LocationManagerProxy mAMapLocationManager;
	private double latitude;
	private double longitude;
	private Location location;
	
	private static OutdoorController smInstance;
	private Activity mActivity;
	private static boolean lastKnownLocationFound = false;
	private boolean providerEnabled = false;
	
	private CurrentCityAreaIdReceivedListener areaIdReceivedListener;

	private CMAHelper cmaHelper;
	
	private OutdoorController() {
		BASE_URL = Utils.getCMA_BaseURL() ;
		outdoorEventListeners = new ArrayList<OutdoorEventListener>();
		setLocationProvider();
		cmaHelper = new CMAHelper(Utils.getCMA_AppID());
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
				if(loc != null && LocationUtils.getCurrentLocationAreaId().isEmpty() && !lastKnownLocationFound) { 
					lastKnownLocationFound = true;
					location = loc;
					OutdoorController.getInstance().startGetAreaIDTask(loc.getLongitude(), loc.getLatitude());
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

	public void setOutdoorDetailsListener(OutdoorDetailsListener outdoorDetailsListener) {
		this.outdoorDetailsListener = outdoorDetailsListener ;
	}

	public void setOutdoorEventListener(OutdoorEventListener outdoorEventListener) {
		outdoorEventListeners.add(outdoorEventListener);
	}

	public void removeOutdoorEventListener(OutdoorEventListener outdoorEventListener) {
		outdoorEventListeners.remove(outdoorEventListener);
	}

	@SuppressLint("SimpleDateFormat")
	public void startCityAQITask(String areaID) {
		//If purifier in demo mode, skip download data
//		if (PurAirApplication.isDemoModeEnable()) return;
		if (isPhilipsSetupWifiSelected()) return;

		TaskGetHttp citiesList = new TaskGetHttp(cmaHelper.getURL(BASE_URL_AQI, areaID, OutdoorController.AIR, Utils.getDate(System.currentTimeMillis())), areaID, PurAirApplication.getAppContext(), this);
		citiesList.start();
	}
	
	public void startAllCitiesAQITask() {
		if (isPhilipsSetupWifiSelected()) return;
		
		//Split requesting 194 cities data into 2 requests.
		String areaIds = OutdoorManager.getInstance().getAllCitiesList().subList(0, 97).toString().replace("[", "").replace("]", "").replace(", ", ",");
		String areaIds2 = OutdoorManager.getInstance().getAllCitiesList().subList(98, OutdoorManager.getInstance().getAllCitiesList().size()).toString().replace("[", "").replace("]", "").replace(", ", ",");
		
		TaskGetHttp citiesList = new TaskGetHttp(cmaHelper.getURL(BASE_URL_AQI, areaIds, OutdoorController.AIR, Utils.getDate(System.currentTimeMillis())), "all_cities", PurAirApplication.getAppContext(), this);
		citiesList.start();
		
		TaskGetHttp citiesList2 = new TaskGetHttp(cmaHelper.getURL(BASE_URL_AQI, areaIds2, OutdoorController.AIR, Utils.getDate(System.currentTimeMillis())), "all_cities", PurAirApplication.getAppContext(), this);
		citiesList2.start();
	}

	public void startCityAQIHistoryTask(String areaID) {
//		if (PurAirApplication.isDemoModeEnable()) return;
		if (isPhilipsSetupWifiSelected()) return;

		TaskGetHttp citiesList = new TaskGetHttp(cmaHelper.getURL(BASE_URL_AQI, areaID, OutdoorController.AIR_HISTORY, Utils.getDate((System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 30l))) + "," + Utils.getDate(System.currentTimeMillis())), areaID, PurAirApplication.getAppContext(), this);
		citiesList.start();
	}

	public void startCityWeatherTask(String areaID) {
		//If purifier in demo mode, skip download data
//		if (PurAirApplication.isDemoModeEnable()) return;
		if (isPhilipsSetupWifiSelected()) return;

		TaskGetHttp citiesList = new TaskGetHttp(cmaHelper.getURL(BASE_URL,areaID, OutdoorController.OBSERVE, Utils.getDate(System.currentTimeMillis())), areaID, PurAirApplication.getAppContext(), this);
		citiesList.start();
	}

	public void startCityFourDayForecastTask(String areaID) {
//		if (PurAirApplication.isDemoModeEnable()) return;
		if (isPhilipsSetupWifiSelected()) return;

		TaskGetHttp citiesList = new TaskGetHttp(cmaHelper.getURL(BASE_URL, areaID, OutdoorController.FORECAST_4_DAYS, Utils.getDate(System.currentTimeMillis())), areaID, PurAirApplication.getAppContext(), this);
		citiesList.start();
	}

	public void startCityOneDayForecastTask(String areaID) {
//		if (PurAirApplication.isDemoModeEnable()) return;
		if (isPhilipsSetupWifiSelected()) return;

		TaskGetHttp citiesList = new TaskGetHttp(BASE_URL_HOURLY_FORECAST + areaID + "&time=day", areaID, PurAirApplication.getAppContext(), this);
		citiesList.start();
	}

	private void notifyListeners(String data, String areaID) {
		if(outdoorEventListeners == null) return;

		OutdoorAQI outdoorAQIList = DataParser.parseLocationAQI(data, areaID);
		List<OutdoorWeather> outdoorWeatherList = DataParser.parseLocationWeather(data);

		if(areaID.equals("all_cities")) {
			List<OutdoorAQI> aqis = DataParser.parseAllLocationAQI(data);
			for(int index = 0; index < outdoorEventListeners.size(); index++) {
				outdoorEventListeners.get(index).allOutdoorAQIDataReceived(aqis);
			}
		} else if( outdoorAQIList == null && outdoorWeatherList == null ) {
			if( outdoorDetailsListener == null ) return ;
			List<Weatherdto>  weatherList = DataParser.getHourlyWeatherData(data) ;
			if( weatherList != null)
				outdoorDetailsListener.onHourlyWeatherForecastReceived(weatherList) ;
			List<ForecastWeatherDto> forecastWeatherDto = DataParser.parseFourDaysForecastData(data, areaID) ;
			if( forecastWeatherDto != null ) {
				outdoorDetailsListener.onWeatherForecastReceived(forecastWeatherDto) ;
			}
		} else {
			for(int index = 0; index < outdoorEventListeners.size(); index++) {
				if(outdoorAQIList != null) {
					outdoorEventListeners.get(index).outdoorAQIDataReceived(outdoorAQIList, areaID);
				}
				if(outdoorWeatherList != null && !outdoorWeatherList.isEmpty()) {
					Iterator<OutdoorWeather> iter = outdoorWeatherList.iterator();
					while(iter.hasNext()) {
						OutdoorWeather outdoorWeather = iter.next();
						outdoorEventListeners.get(index).outdoorWeatherDataReceived(outdoorWeather, areaID);
					}
				}
			}
		}
	}

	@Override
	public void receiveServerResponse(int responseCode, String data, String areaID) {
		ALog.i(ALog.DASHBOARD, "OutdoorController data received " + data + " responseCode " + responseCode + " areaID " + areaID);
		if(data != null && !areaID.isEmpty() && !areaID.equals("from_lat_long")) {
			notifyListeners(data, areaID);
		} else if (!areaID.isEmpty() && areaID.equals("from_lat_long") && data != null && !data.isEmpty()) {
			processAreaID(data);
		} 
	}
	
	private void processAreaID(String data) {
		String areaId = parseAreaID(data);
		if (areaId.isEmpty()) return;
		
		LocationUtils.saveCurrentLocationAreaId(areaId);
		OutdoorManager.getInstance().addAreaIDToUsersList(areaId);
		areaIdReceived();//Listen to outdoor location fragment
		
		addMyLocationToMap(areaId);
		
		OutdoorManager.getInstance().startCitiesTask();
		if(LocationUtils.getCurrentLocationAreaId().isEmpty()) done=false;
	}

	private void addMyLocationToMap(String areaId) {
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

			OutdoorCityInfo info = new OutdoorCityInfo(city, cityCN, cityTW, longitude, latitude, areaId);
			OutdoorManager.getInstance().addCityDataToMap(info, null, null, areaId);
		}
		database.updateOutdoorLocationShortListItem(areaId,	true);
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
//		ALog.i(ALog.OUTDOOR_LOCATION, "onLocationChanged aLocation " + aLocation + " exists current loc aid: " + LocationUtils.getCurrentLocationAreaId());
		location = aLocation;
		
		if(location!=null && location.getLatitude()>0 && location.getLongitude()>0 && !LocationUtils.getCurrentLocationAreaId().isEmpty() && GPSLocation.getInstance().isGPSEnabled())
			showLocationServiceTurnedOnDialog();
		
		if(aLocation != null && !done && LocationUtils.getCurrentLocationAreaId().isEmpty()) {
			latitude = aLocation.getLatitude();
			longitude = aLocation.getLongitude();
			startGetAreaIDTask(longitude, latitude);
			done = true;
		}
	}
	
	public void startGetAreaIDTask(double longitude, double latitude) {
		LocationUtils.saveCurrentLocationLatLon(String.valueOf(latitude), String.valueOf(longitude));
		//If purifier in demo mode, skip download data
		if (isPhilipsSetupWifiSelected()) return;

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
	
	public static void setDummyOutdoorControllerForTesting(OutdoorController controller) {
		smInstance = controller;
	}
	
}