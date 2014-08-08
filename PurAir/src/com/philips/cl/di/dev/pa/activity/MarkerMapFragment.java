package com.philips.cl.di.dev.pa.activity;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;

/**
 * 
 * MarkerActivity class will be showing AQI details of the qualified cities.
 * Author : Ritesh.jha@philips.com Date : 4 Aug 2014
 * 
 */
public class MarkerMapFragment extends Fragment implements
		OnMarkerClickListener, OnMapLoadedListener {

	private AMap aMap;
	private MapView mapView;
	private UiSettings mUiSettings;
	private static List<String> mCitiesList = null;

	@Override
	public void onMapLoaded() {
		OutdoorCity outdoorCity = OutdoorManager.getInstance().getCityData(
				OutdoorDetailsActivity.getSelectedCityCode());

		if (outdoorCity == null || outdoorCity.getOutdoorCityInfo() == null) return;
		
		LatLngBounds bounds = new LatLngBounds.Builder().include(
				new LatLng(outdoorCity.getOutdoorCityInfo().getLatitude(),
						outdoorCity.getOutdoorCityInfo().getLongitude()))
				.build();
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		/* We inflate the xml which gives us a view */
		view = inflater.inflate(R.layout.marker_activity, container, false);
		mapView = (MapView) view.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		mCitiesList = OutdoorManager.getInstance().getCitiesList();

		init();

		for (int i = 0; i < mCitiesList.size(); i++) {
			OutdoorCity outdoorCity = OutdoorManager.getInstance().getCityData(
					mCitiesList.get(i));
			addMarkerToMap(outdoorCity);
		}

		return view;
	}

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			mUiSettings = aMap.getUiSettings();
			mUiSettings.setZoomControlsEnabled(false);
			mUiSettings.setCompassEnabled(false);
			mUiSettings.setMyLocationButtonEnabled(false);
			mUiSettings.setScaleControlsEnabled(false);
			mUiSettings.setAllGesturesEnabled(false);
			aMap.setOnMapLoadedListener(this);
			aMap.setOnMarkerClickListener(this);
		}
	}

	private void addMarkerToMap(OutdoorCity outdoorCity) {
		if (outdoorCity == null || outdoorCity.getOutdoorCityInfo() == null || outdoorCity.getOutdoorAQI() == null) return;
		float latitude = outdoorCity.getOutdoorCityInfo().getLatitude();
		float longitude = outdoorCity.getOutdoorCityInfo().getLongitude();
		String cityName = outdoorCity.getOutdoorCityInfo().getCityName();

		if(outdoorCity == null || outdoorCity.getOutdoorAQI() == null){
			return;
		}
		
		int aqiValue = outdoorCity.getOutdoorAQI().getAQI();

		aMap.addMarker(new MarkerOptions()
				.anchor(0.5f, 0.5f)
				.position(new LatLng(latitude, longitude))
				.title(cityName)
				.snippet(cityName + " : " + latitude + " , " + longitude)
				.draggable(false)
				.icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(
						getAqiPointerImageResId(aqiValue), aqiValue))));
	}

	static Bitmap writeTextOnDrawable(int drawableId, Integer text) {
		Bitmap bm = BitmapFactory.decodeResource(PurAirApplication.getAppContext().getResources(), drawableId)
				.copy(Bitmap.Config.ARGB_8888, true);
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(20);
		paint.setTypeface(tf);
		paint.setTextAlign(Align.CENTER);

		Rect textRect = new Rect();
		String newText = text.toString();
		paint.getTextBounds(newText, 0, newText.length(), textRect);

		Canvas canvas = new Canvas(bm);
		// Calculate the positions
		int xPos = (canvas.getWidth() / 2) - 2; // -2 is for regulating the x
												// position offset
		// baseline to the center.
		int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint
				.ascent()) / 2));
		canvas.drawText(newText, xPos, yPos, paint);
		return bm;
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return false;
	}

	static int getAqiPointerImageResId(int p2) {

		if (p2 >= 0 && p2 <= 50) {
			return R.drawable.map_circle_6;
		} else if (p2 > 50 && p2 <= 100) {
			return R.drawable.map_circle_5;
		} else if (p2 > 100 && p2 <= 150) {
			return R.drawable.map_circle_4;
		} else if (p2 > 150 && p2 <= 200) {
			return R.drawable.map_circle_3;
		} else if (p2 > 200 && p2 <= 300) {
			return R.drawable.map_circle_2;
		} else if (p2 > 300) {
			return R.drawable.map_circle_1;
		}

		return R.drawable.map_circle_6;
	}
}
