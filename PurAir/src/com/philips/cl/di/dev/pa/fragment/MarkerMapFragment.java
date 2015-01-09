package com.philips.cl.di.dev.pa.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.OutdoorDetailsActivity;
import com.philips.cl.di.dev.pa.dashboard.AllOutdoorDataListener;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.outdoorlocations.AddOutdoorLocationHelper;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorDataProvider;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.gaode.map.MapUtils;

/**
 * 
 * MarkerMapFragment class will be showing AQI details of the qualified cities.
 * Author : Ritesh.jha@philips.com Date : 4 Aug 2014
 * 
 */
public class MarkerMapFragment extends BaseFragment implements
		OnMarkerClickListener, OnMapLoadedListener, AllOutdoorDataListener {

	private AMap aMap;
	private MapView mapView;
	private UiSettings mUiSettings;
	private List<String> mCitiesListAll = null;
	private LatLngBounds bounds = null;
	private Builder builder = null;
	private OutdoorCity mOutdoorCity = null;
	private boolean isMapLoaded = false;
	private boolean isAllAqiReceived = false;
	private View mView = null;
	private Bitmap mBitMap = null;
	private LayoutInflater mInflater = null;
	private RelativeLayout mParentLayout = null;
	private ArrayList<Marker> mArrayListMarker = null;
	private TextView textView = null;
	private Canvas mCanvas = null;
	
	private static final String TAG = "MarkerMapFragment";

	@Override
	public void onMapLoaded() {
		isMapLoaded = true;
		fillMapWithMarker();
	}
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case 0:
				if(isMapLoaded && isAllAqiReceived){
					mHandler.sendEmptyMessageDelayed(1, 500);
				}
				else{
					mHandler.sendEmptyMessageDelayed(0, 500);
				}
				break;
			case 1:
				isMapLoaded = false;
				isAllAqiReceived = false;
				fillMapWithMarker();
				break;
			}
		};
	};
	
	private void fillMapWithMarker(){
		OutdoorCity outdoorCity = OutdoorManager.getInstance().getCityData(OutdoorDetailsActivity.getSelectedCityCode());
		
		addMarker();

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
		OutdoorManager.getInstance().setAllOutdoorDataListener(this);
		View view;
		/* We inflate the xml which gives us a view */
		view = inflater.inflate(R.layout.marker_activity, container, false);
		mapView = (MapView) view.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		mParentLayout = (RelativeLayout)view.findViewById(R.id.mapParent);
		builder = new LatLngBounds.Builder();
		mInflater = (LayoutInflater) 
				getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = mInflater.inflate(R.layout.circle_lyt, null);
		textView = (TextView) mView.findViewById(R.id.circle_txt); 
		init();
		mArrayListMarker = new ArrayList<Marker>();	
		return view;
	}

	private void addMarker() {
		String selectedCityCode = OutdoorDetailsActivity.getSelectedCityCode();
		OutdoorCity selectedOutdoorCity = OutdoorManager.getInstance().getCityData(selectedCityCode);
		float selectedLatitude = 50f;
		float selectedLongitude = 50f;
		
		//Adding below condition because UnitTest case is crashing.
		if(selectedOutdoorCity != null && selectedOutdoorCity.getOutdoorCityInfo() != null){
			selectedLatitude = selectedOutdoorCity.getOutdoorCityInfo().getLatitude();
			selectedLongitude = selectedOutdoorCity.getOutdoorCityInfo().getLongitude();
		}
		
		/*
		 * logic to find nearBy cities.
		 */
		float latitudePlus = selectedLatitude + 4;
		float latitudeMinus = selectedLatitude - 4;
		float longitudePlus = selectedLongitude + 4;
		float longitudeMinus = selectedLongitude - 4;
		
		mCitiesListAll = OutdoorManager.getInstance().getNearbyCitiesList(latitudePlus, 
				latitudeMinus, longitudePlus, longitudeMinus);
		if (selectedOutdoorCity != null) {
			OutdoorCityInfo outdoorCityInfo = selectedOutdoorCity.getOutdoorCityInfo();
			if (outdoorCityInfo != null && outdoorCityInfo.getDataProvider() == OutdoorDataProvider.US_EMBASSY.ordinal()) {
				mCitiesListAll.add(selectedCityCode);
			}
		}
		
		for (int i = 0; i < (mCitiesListAll.size()); i++) {
			OutdoorCity outdoorCity = OutdoorManager.getInstance().getCityData(mCitiesListAll.get(i));
			addMarkerToMap(outdoorCity);
		}
		addMarkerToMap(mOutdoorCity);
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
		 || outdoorCity.getOutdoorAQI() == null )
			return;
		float latitude = outdoorCity.getOutdoorCityInfo().getLatitude();
		float longitude = outdoorCity.getOutdoorCityInfo().getLongitude();
		String cityName = outdoorCity.getOutdoorCityInfo().getCityName();
		boolean iconOval = false;
		String cityCode = AddOutdoorLocationHelper.getCityKeyWithRespectDataProvider(outdoorCity.getOutdoorCityInfo());
		
		LatLng latLng = new LatLng(latitude, longitude);
		builder.include(latLng);

		int aqiValue = 0;
		if (outdoorCity.getOutdoorAQI() != null)
			aqiValue = outdoorCity.getOutdoorAQI().getAQI();

		String selectedCityAreaId = OutdoorDetailsActivity.getSelectedCityCode();
		if (selectedCityAreaId != null && selectedCityAreaId.equalsIgnoreCase(cityCode)) {
			mOutdoorCity = outdoorCity;
			iconOval = true;
		}

		if(mBitMap != null){
			mBitMap.recycle();
			mBitMap = null;
		}
		
		mBitMap = writeTextOnDrawable(
				MapUtils.getMapUtilsInstace().getAqiPointerImageResId(aqiValue,
						iconOval), aqiValue);
				
		mArrayListMarker.add(aMap.addMarker(new MarkerOptions()
				.anchor(0.5f, 0.5f)
				.position(latLng)
				.title(cityName)
				.snippet(cityName + " : " + latitude + " , " + longitude)
				.draggable(false)
				.icon(BitmapDescriptorFactory.fromBitmap(mBitMap))));
	}

	private Bitmap writeTextOnDrawable(int drawableId, int text) {
		Bitmap bm = BitmapFactory.decodeResource(
				getActivity().getResources(), drawableId)
				.copy(Bitmap.Config.ARGB_8888, true);
		if(mCanvas!=null){
			mCanvas = null;
		}
		mCanvas = new Canvas(bm);
		textView.setText(String.valueOf(text));
		mView.measure(mCanvas.getWidth(), mCanvas.getHeight());
		mView.layout(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
		mView.draw(mCanvas);
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
		OutdoorManager.getInstance().removeAllOutdoorDataListener(this);
		mView = null;
		mInflater = null;
		
		if(mHandler != null){
			mHandler.removeMessages(0);
			mHandler.removeMessages(1);
			mHandler = null;
		}
		
		if(aMap!=null){		
			aMap.stopAnimation();
			aMap.clear();
			aMap = null;
		}
		if(mapView != null){
			mapView.setBackgroundColor(Color.BLACK);
			mapView.removeAllViewsInLayout();
			mapView.onDestroy();
			mapView = null;
		}
		
		if(mParentLayout != null){
			mParentLayout.removeAllViews();
			mParentLayout = null;
		}
		
		if(mArrayListMarker!=null && mArrayListMarker.size()>0){
			mArrayListMarker.clear();
			mArrayListMarker=null;
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

	@Override
	public void updateUIOnAllDataReceived() {
		isAllAqiReceived = true;
		mHandler.sendEmptyMessageDelayed(0, 500);
	}
}
