package com.philips.cl.di.dev.pa.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;

import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.LatLngBounds.Builder;
import com.amap.api.maps2d.model.Marker;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.IndoorDashboardUtils;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.gaode.map.MapActivity;

/**
 * 
 * MarkerActivity class will be showing AQI details of the qualified cities.
 * Author : Ritesh.jha@philips.com Date : 4 Aug 2014
 * 
 */
public class MarkerActivity extends MapActivity implements
		OnMarkerClickListener, OnMapLoadedListener {
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
		mCitiesListAll = OutdoorManager.getInstance().getAllCitiesList();
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
			populateAllMarkers();
		}
	}
	
	private void populateAllMarkers() {
		for (int i = 0; i < mCitiesListAll.size(); i++) {
			OutdoorCity outdoorCity = OutdoorManager.getInstance()
					.getCityDataAll(mCitiesListAll.get(i));
			addMarkerToMap(outdoorCity);
		}
		addMarkerToMap(mOutdoorCity);
		addMarkerCurrentPurifer();	
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
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
		PurAirDevice currentPurifier = PurifierManager.getInstance()
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
		if (currentPurifier.getAirPortInfo() != null) {
			currentPurifierAQI = currentPurifier.getAirPortInfo()
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
		String cityCode = outdoorCity.getOutdoorCityInfo().getAreaID();
		int pm25 = outdoorCity.getOutdoorAQI().getPM25();
		int pm10 = outdoorCity.getOutdoorAQI().getPm10();
		int so2 = outdoorCity.getOutdoorAQI().getSo2();
		int no2 = outdoorCity.getOutdoorAQI().getNo2();
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

		if (OutdoorDetailsActivity.getSelectedCityCode().equalsIgnoreCase(
				cityCode)) {
			setMarkerIconOval(true);
			mOutdoorCity = outdoorCity;
		} else {
			setMarkerIconOval(false);
		}

		setMarkerSnippet("PM2.5: " + pm25 + ", PM10: " + pm10 + ", SO2: " + so2
				+ ", NO2: " + no2);
		setMarkerTitle(cityName);
		setMarkerPositionLatLng(latLng);

		mArrayListMarker.add(createMarker(DEFAULT_IMAGE_RESOURCE));
	}

	@Override
	public void onMapLoaded() {
		OutdoorCity outdoorCity = OutdoorManager.getInstance().getCityData(
				OutdoorDetailsActivity.getSelectedCityCode());

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
}
