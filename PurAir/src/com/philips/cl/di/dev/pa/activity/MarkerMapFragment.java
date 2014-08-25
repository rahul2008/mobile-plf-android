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
import android.os.Handler;
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
import com.amap.api.maps2d.model.LatLngBounds.Builder;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.util.ALog;

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
//	private List<String> mCitiesList = null;
	private static List<String> mCitiesListAll = null;
	private LatLngBounds bounds = null;
	private Builder builder = null;

	private static final String TAG = "MapMarkerFragment";

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
			ALog.d(TAG, "IllegalStateException");
		}
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsNew, 10));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		/* We inflate the xml which gives us a view */
		view = inflater.inflate(R.layout.marker_activity, container, false);
		mapView = (MapView) view.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
//		mCitiesList = OutdoorManager.getInstance().getUsersCitiesList();
		builder = new LatLngBounds.Builder();

		init();

//		populatingDashboardData();
		// testing
		mHandler.sendEmptyMessageDelayed(0, 100);
		return view;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
//			dbOperations();
			addMarker();
		}
	};

	private void addMarker() {
		String selectedCityCode = OutdoorDetailsActivity.getSelectedCityCode();
		OutdoorCity selectedOutdoorCity = OutdoorManager.getInstance().getCityDataAll(selectedCityCode);
		float selectedLatitude = selectedOutdoorCity.getOutdoorCityInfo().getLatitude();
		float selectedLongitude = selectedOutdoorCity.getOutdoorCityInfo().getLongitude();
		
		float latitudePlus = selectedLatitude + 4;
		float latitudeMinus = selectedLatitude - 4;
		float longitudePlus = selectedLongitude + 4;
		float longitudeMinus = selectedLongitude - 4;
		
//		mCitiesListAll = OutdoorManager.getInstance().getAllMatchingCitiesList(latitudePlus, latitudeMinus, longitudePlus, longitudeMinus);
		
		mCitiesListAll = OutdoorManager.getInstance().getAllCitiesList();
		ALog.i("testing", "mCitiesListAll : " + mCitiesListAll.size());
		for (int i = 0; i < (mCitiesListAll.size())/4; i++) {
			OutdoorCity outdoorCity = OutdoorManager.getInstance()
					.getCityDataAll(mCitiesListAll.get(i));
			addMarkerToMap(outdoorCity);
		}
	};

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
		if (outdoorCity == null || outdoorCity.getOutdoorCityInfo() == null
		/* || outdoorCity.getOutdoorAQI() == null */)
			return;
		float latitude = outdoorCity.getOutdoorCityInfo().getLatitude();
		float longitude = outdoorCity.getOutdoorCityInfo().getLongitude();
		String cityName = outdoorCity.getOutdoorCityInfo().getCityName();
		boolean iconOval = false;
		String cityCode = outdoorCity.getOutdoorCityInfo().getAreaID();

		// if(outdoorCity == null || outdoorCity.getOutdoorAQI() == null){
		// return;
		// }

		LatLng latLng = new LatLng(latitude, longitude);
		builder.include(latLng);

		int aqiValue = 101;
		if (outdoorCity.getOutdoorAQI() != null)
			aqiValue = outdoorCity.getOutdoorAQI().getAQI();

		if (OutdoorDetailsActivity.getSelectedCityCode() != null
				&& OutdoorDetailsActivity.getSelectedCityCode()
						.equalsIgnoreCase(cityCode)) {
			iconOval = true;
		}

		aMap.addMarker(new MarkerOptions()
				.anchor(0.5f, 0.5f)
				.position(latLng)
				.title(cityName)
				.snippet(cityName + " : " + latitude + " , " + longitude)
				.draggable(false)
				.icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(
						MarkerMapFragment.getAqiPointerImageResId(aqiValue,
								iconOval), aqiValue))));
	}

	static Bitmap writeTextOnDrawable(int drawableId, int text) {
		Bitmap bm = BitmapFactory.decodeResource(
				PurAirApplication.getAppContext().getResources(), drawableId)
				.copy(Bitmap.Config.ARGB_8888, true);
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTypeface(tf);
		paint.setTextAlign(Align.CENTER);

		/*
		 * Putting this logic here because we have fixed circle resources. We
		 * need to manipulate the font size only to fit in the circle.
		 */
		if (text > 99) {
			paint.setTextSize(16);
		} else {
			paint.setTextSize(18);
		}

		Rect textRect = new Rect();
		String newText = String.valueOf(text);
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
		if (mHandler != null) {
			mHandler = null;
		}
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

	static int getAqiPointerImageResId(int p2, boolean iconOval) {

		if (!iconOval) {
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
		} else {
			if (p2 >= 0 && p2 <= 50) {
				return R.drawable.map_oval_6;
			} else if (p2 > 50 && p2 <= 100) {
				return R.drawable.map_oval_5;
			} else if (p2 > 100 && p2 <= 150) {
				return R.drawable.map_oval_4;
			} else if (p2 > 150 && p2 <= 200) {
				return R.drawable.map_oval_3;
			} else if (p2 > 200 && p2 <= 300) {
				return R.drawable.map_oval_2;
			} else if (p2 > 300) {
				return R.drawable.map_oval_1;
			}
		}

		return R.drawable.map_circle_6;
	}
}
