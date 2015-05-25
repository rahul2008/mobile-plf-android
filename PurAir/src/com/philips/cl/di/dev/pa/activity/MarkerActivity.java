package com.philips.cl.di.dev.pa.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;

import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.LatLngBounds.Builder;
import com.amap.api.maps2d.model.Marker;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.dashboard.IndoorDashboardUtils;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorDetailFragment;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.outdoorlocations.AddOutdoorLocationHelper;
import com.philips.cl.di.dev.pa.outdoorlocations.NearbyCitiesData.LocalityInfo;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorDataProvider;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.OutdoorDetailsListener;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.gaode.map.MapActivity;

/**
 * 
 * MarkerActivity class will be showing AQI details of the qualified cities.
 * Author : Ritesh.jha@philips.com Date : 4 Aug 2014
 * 
 */
public class MarkerActivity extends MapActivity implements OnMapLoadedListener, OutdoorDetailsListener {
	private LatLngBounds bounds = null;
	private Builder builder = null;
	private List<String> mCitiesListAll = null;
	private OutdoorCity mOutdoorCity = null;
	private ArrayList<Marker> mArrayListMarker = null;
	private Thread mThread = null;
	private PopulateMarkerThread mRunnable = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mArrayListMarker = new ArrayList<Marker>();
		builder = new LatLngBounds.Builder();
		mCitiesListAll = OutdoorManager.getInstance().getCMACities();
		setMarkerAnchorFirstParam(0.5f);
		setMarkerAnchorSecondParam(0.5f);
		setMarkerDraggable(true);
		
		mRunnable = new PopulateMarkerThread();
		mThread = new Thread(mRunnable);
		mThread.start();
	}

	class PopulateMarkerThread implements Runnable{
		@Override
		public void run() {
			List<OutdoorAQI> neighborhoodCitiesAQI = OutdoorManager.getInstance().getNeighborhoodCitiesData();
			if (!neighborhoodCitiesAQI.isEmpty()) {
				populateAllNeighbourhoodMarkers(neighborhoodCitiesAQI);	
			}
			populateAllMarkers();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MetricsTracker.startCollectLifecycleData(this);
		MetricsTracker.trackPage(TrackPageConstants.OUTDOOR_MAP);
		OutdoorManager.getInstance().addOutdoorDetailsListener(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MetricsTracker.stopCollectLifecycleData();
		OutdoorManager.getInstance().removeOutdoorDetailsListener(this);
	}
	
	private void populateAllMarkers() {
		String selectedCityKey = OutdoorDetailFragment.getSelectedCityCode();
		String areaId = OutdoorManager.getInstance().getAreaIdFromCityName(selectedCityKey);
		
		for (int i = 0; i < mCitiesListAll.size(); i++) {
			OutdoorCity outdoorCity = OutdoorManager.getInstance().getCityData(mCitiesListAll.get(i));
			if (areaId.equalsIgnoreCase(outdoorCity.getOutdoorCityInfo().getAreaID())) {
				outdoorCity = OutdoorManager.getInstance().getCityData(selectedCityKey);
			}
			
			addMarkerToMap(outdoorCity);
		}
		addMarkerToMap(mOutdoorCity);
		addMarkerCurrentPurifer();	
	}
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mArrayListMarker != null && mArrayListMarker.size() > 0) {
			mArrayListMarker.clear();
			mArrayListMarker = null;
		}
		
		mThread = null;
		mRunnable = null;

		/*
		 * Since calling gc() is not recommended but by using this we are able
		 * to release 4~5 MB when come out from this activity.
		 */
		System.gc();
	}

	private void addMarkerCurrentPurifer() {
		AirPurifier currentPurifier = AirPurifierManager.getInstance()
				.getCurrentPurifier();
		if (currentPurifier == null || currentPurifier.getLatitude() == null
				|| currentPurifier.getLongitude() == null
				|| currentPurifier.getLatitude().isEmpty()
				|| currentPurifier.getLongitude().isEmpty()) {
			return;
		}

		Float latitude = Float.parseFloat(currentPurifier.getLatitude());
		Float longitude = Float.parseFloat(currentPurifier.getLongitude());
		String purifierName = currentPurifier.getName();

		int currentPurifierAQI = 0;
		if (currentPurifier.getAirPort().getAirPortInfo() != null) {
			currentPurifierAQI = currentPurifier.getAirPort().getAirPortInfo()
					.getIndoorAQI();
			setMarkerAqiValue(currentPurifierAQI);
		}
		LatLng latLng = new LatLng(latitude, longitude);
		String aqiTitle = getResources().getString(
				IndoorDashboardUtils.getAqiTitle(currentPurifierAQI));
		aqiTitle = aqiTitle.replace("\n", "");
		String snippet = getResources().getString(
				R.string.outdoor_aqi_map_purifier_title)
				+ " " + aqiTitle;

		setMarkerSnippet(snippet);
		setMarkerTitle(purifierName);
		setMarkerPositionLatLng(latLng);

		int imageRes = IndoorDashboardUtils
				.getAqiPointerMarker(currentPurifierAQI);
		mArrayListMarker.add(createMarker(imageRes));
	}

	private void addMarkerToMap(OutdoorCity outdoorCity) {
		if (outdoorCity == null || outdoorCity.getOutdoorAQI() == null
				|| outdoorCity.getOutdoorCityInfo() == null)
			return;

		int aqiValue = 0;
		if (outdoorCity.getOutdoorAQI() != null) {
			aqiValue = outdoorCity.getOutdoorAQI().getAQI();
			setMarkerAqiValue(aqiValue);
		}

		float latitude = outdoorCity.getOutdoorCityInfo().getLatitude();
		float longitude = outdoorCity.getOutdoorCityInfo().getLongitude();
		String cityCode = AddOutdoorLocationHelper.getCityKeyWithRespectDataProvider(outdoorCity.getOutdoorCityInfo());
//		String cityCode = outdoorCity.getOutdoorCityInfo().getAreaID();
		int pm25 = outdoorCity.getOutdoorAQI().getPM25();
		String pm10 = outdoorCity.getOutdoorAQI().getPm10();
		String no2 = outdoorCity.getOutdoorAQI().getNo2() ;
		String so2 = String.valueOf(outdoorCity.getOutdoorAQI().getSo2());
		
		String cityName = null;

		// added to support traditional and simplified Chinese in map
		if (LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains(
				"ZH-HANS")) {
			cityName = outdoorCity.getOutdoorCityInfo().getCityNameCN();
		} else if (LanguageUtils.getLanguageForLocale(Locale.getDefault())
				.contains("ZH-HANT")) {
			cityName = outdoorCity.getOutdoorCityInfo().getCityNameTW();
		} else {
			cityName = outdoorCity.getOutdoorCityInfo().getCityName();
		}

		LatLng latLng = new LatLng(latitude, longitude);
		builder.include(latLng);

		if (OutdoorDetailFragment.getSelectedCityCode().equalsIgnoreCase(cityCode)) {
			setMarkerIconOval(true);
			mOutdoorCity = outdoorCity;
			textView.setTextSize(16);
		} else {
			setMarkerIconOval(false);
			textView.setTextSize(10);
		}

		
		setMarkerSnippet("PM2.5: " + pm25 + ", PM10: " + pm10 + ", SO2: " + so2
				+ ", NO2: " + no2);
		
		//Replace first latter Capital and append US Embassy
		if( outdoorCity.getOutdoorCityInfo().getDataProvider() == OutdoorDataProvider.US_EMBASSY.ordinal()) {
			StringBuilder builder = new StringBuilder(AddOutdoorLocationHelper.getFirstWordCapitalInSentence(cityName)) ;				
			builder.append(" (").append(getString(R.string.us_embassy)).append(" )") ;				
			cityName = builder.toString() ;
		}
		
		setMarkerTitle(cityName);
		setMarkerPositionLatLng(latLng);

		mArrayListMarker.add(createMarker(DEFAULT_IMAGE_RESOURCE));
	}

	@Override
	public void onMapLoaded() {
		OutdoorCity outdoorCity = OutdoorManager.getInstance().getCityData(
				OutdoorDetailFragment.getSelectedCityCode());

		if (outdoorCity == null || outdoorCity.getOutdoorCityInfo() == null)
			return;

		LatLngBounds boundsNew = new LatLngBounds.Builder().include(
				new LatLng(outdoorCity.getOutdoorCityInfo().getLatitude(),
						outdoorCity.getOutdoorCityInfo().getLongitude()))
				.build();
		try {
			bounds = builder.build();
			aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
		} catch (IllegalStateException e) {
			ALog.d(ALog.MARKER_ACTIVITY, "IllegalStateException");
		}
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsNew, 10));
	}

	@Override
	public void onOneDayWeatherForecastReceived(List<Weatherdto> weatherList) {
	//NOP	
	}

	@Override
	public void onFourDayWeatherForecastReceived(List<ForecastWeatherDto> weatherList) {
		//NOP	
		
	}

	@Override
	public void onAQIHistoricalDataReceived(List<OutdoorAQI> outdoorAQIHistory,	String areaId) {
		// NOP
	}

	@Override
	public void onNearbyLocationsDataReceived(
			List<OutdoorAQI> nearbyLocationAQIs) {

		populateAllNeighbourhoodMarkers(nearbyLocationAQIs);
		addMarkerToMap(mOutdoorCity);
	}
	
	private void populateAllNeighbourhoodMarkers( List<OutdoorAQI> nearbyLocationAQIs) {

		if (nearbyLocationAQIs == null || nearbyLocationAQIs.isEmpty()) {
			return;
		}

		for (int i = 0; i < nearbyLocationAQIs.size(); i++) {
			addNeighbourhoodMarkerToMap(nearbyLocationAQIs.get(i));
		}

	}
	
	private void addNeighbourhoodMarkerToMap(OutdoorAQI outdooraqi) {
		if (outdooraqi == null) return;

		String parentId = OutdoorDetailFragment.getSelectedCityCode();
		LocalityInfo locallityinfo = OutdoorManager.getInstance().getLocalityInfoFromAreaId(parentId, outdooraqi.getAreaID());
		setMarkerAqiValue(outdooraqi.getAQI());
		
		float latitude = 0;
		float longitude = 0;
		
		
		try {
			latitude = Float.parseFloat(locallityinfo.getLatitude());
			longitude = Float.parseFloat(locallityinfo.getLongitude());
		} catch (Exception e) {
			
			ALog.d(ALog.MARKER_ACTIVITY, "IllegalStateException addNeighbourhoodMarkerToMap");
		}

		int pm25 = outdooraqi.getPM25();
		String pm10 = outdooraqi.getPm10();
		String so2 = outdooraqi.getSo2();
		String no2 = outdooraqi.getNo2();
		String cityName = OutdoorManager.getInstance().getLocalityNameFromAreaId(parentId, outdooraqi.getAreaID());

		LatLng latLng = new LatLng(latitude, longitude);
		builder.include(latLng);
		setMarkerIconOval(false);
		setMarkerSnippet("PM2.5: " + pm25 + ", PM10: " + pm10 + ", SO2: " + so2
				+ ", NO2: " + no2);

		setMarkerTitle(cityName);
		setMarkerPositionLatLng(latLng);
		mArrayListMarker.add(createMarker(DEFAULT_IMAGE_RESOURCE));
	}
	
}
