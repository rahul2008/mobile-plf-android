package com.philips.cl.di.dev.pa.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.LatLngBounds.Builder;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.IndoorDashboardUtils;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.fragment.MarkerMapFragment;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.view.FontTextView;

/**
 * 
 * MarkerActivity class will be showing AQI details of the qualified cities.
 * Author : Ritesh.jha@philips.com Date : 4 Aug 2014
 * 
 */
public class MarkerActivity extends Activity implements OnMarkerClickListener,
		OnMapLoadedListener, OnClickListener {
	private AMap aMap;
	private MapView mapView;
	private ImageView mFinishActivity = null;
	private LatLngBounds bounds = null;
	private Builder builder = null;
	private RelativeLayout mAqiDrawer = null;
	private FontTextView mAqiCity = null;
	private FontTextView mAqiDetails = null;
	private ImageView mAqiMarker = null;
	private List<String> mCitiesListAll = null;
	private OutdoorCity mOutdoorCity = null;
	private boolean isAnimationDrawableOpen = false;
	private ArrayList<Marker> mArrayListMarker = null;
	private RelativeLayout mParentLayout = null;
	private LayoutInflater inflater = null;
	private View view = null;
	private Bitmap mBitMap = null;
	private FontTextView textView = null;
	private Canvas mCanvas = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.marker_activity);
		mArrayListMarker = new ArrayList<Marker>();
		mParentLayout = (RelativeLayout)findViewById(R.id.mapParent);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		mFinishActivity = (ImageView) findViewById(R.id.gaodeMapFinish);
		mAqiDrawer = (RelativeLayout) findViewById(R.id.aqi_prompt_drawer);
		mAqiCity = (FontTextView) findViewById(R.id.aqiCity);
		mAqiDetails = (FontTextView) findViewById(R.id.aqiDetails);
		mAqiMarker = (ImageView) findViewById(R.id.aqiMarker);
		mFinishActivity.setVisibility(View.VISIBLE);
		mFinishActivity.setOnClickListener(this);
		builder = new LatLngBounds.Builder();
		mapView.setOnClickListener(this);
		
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		view = inflater.inflate(R.layout.circle_lyt, null);
		textView = (FontTextView) view.findViewById(R.id.circle_txt); 
		
		init();

		mCitiesListAll = OutdoorManager.getInstance().getAllCitiesList();
		
		for (int i = 0; i < mCitiesListAll.size(); i++) {
			OutdoorCity outdoorCity = OutdoorManager.getInstance()
					.getCityDataAll(mCitiesListAll.get(i));
			addMarkerToMap(outdoorCity);
		}
		addMarkerToMap(mOutdoorCity);
		addMarkerCurrentPurifer();
	}

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(LatLng latLng) {
					if (isAnimationDrawableOpen) {
						hideAnimation();
					}
				}
			});
			setUpMap();
		}
	}

	private void setUpMap() {
		aMap.setOnMapLoadedListener(this);
		aMap.setOnMarkerClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();		
		
		view = null;
		inflater = null;
		
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
		
		/*
		 *  Since calling gc() is not recommended but by using this
		 *  we are able to release 4~5 MB when come out from this activity. 
		 */
		System.gc();
	}

	private void addMarkerCurrentPurifer(){
		PurAirDevice currentPurifier = PurifierManager.getInstance().getCurrentPurifier();
		if(currentPurifier == null || currentPurifier.getLatitude() == null || currentPurifier.getLongitude() == null ||
				currentPurifier.getLatitude().isEmpty() || currentPurifier.getLongitude().isEmpty()){
			return;
		}
		
		Float latitude = Float.parseFloat(currentPurifier.getLatitude());
		Float longitude = Float.parseFloat(currentPurifier.getLongitude());
		String purifierName = currentPurifier.getName();
		
		int currentPurifierAQI = 0;
		if(currentPurifier.getAirPortInfo()!=null){
			currentPurifierAQI = currentPurifier.getAirPortInfo().getIndoorAQI();
		}
		LatLng latLng = new LatLng(latitude, longitude);
		String aqiTitle=getResources().getString(IndoorDashboardUtils.getAqiTitle(currentPurifierAQI));
		aqiTitle=aqiTitle.replace("\n", "");
		String snippet = getResources().getString(R.string.outdoor_aqi_map_purifier_title) + " "+ aqiTitle;
		
		mArrayListMarker.add(aMap.addMarker(new MarkerOptions()
				.anchor(0.5f, 0.5f)
				.position(latLng)
				.title(purifierName)
				.snippet(snippet)
				.draggable(true)
				.icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(
						IndoorDashboardUtils.getAqiPointerMarker(currentPurifierAQI), currentPurifierAQI)))));
	}
	
	
	private void addMarkerToMap(OutdoorCity outdoorCity) {
		if (outdoorCity == null || outdoorCity.getOutdoorAQI() == null
				|| outdoorCity.getOutdoorCityInfo() == null)
			return;

		int aqiValue = 0;
		if (outdoorCity.getOutdoorAQI() != null)
			aqiValue = outdoorCity.getOutdoorAQI().getAQI();

		float latitude = outdoorCity.getOutdoorCityInfo().getLatitude();
		float longitude = outdoorCity.getOutdoorCityInfo().getLongitude();
		String cityCode = outdoorCity.getOutdoorCityInfo().getAreaID();
		int pm25 = outdoorCity.getOutdoorAQI().getPM25();
		int pm10 = outdoorCity.getOutdoorAQI().getPm10();
		int so2 = outdoorCity.getOutdoorAQI().getSo2();
		int no2 = outdoorCity.getOutdoorAQI().getNo2();
		String cityName = null;
		boolean iconOval = false;

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
			iconOval = true;
			mOutdoorCity = outdoorCity;
		}

		if(mBitMap != null){
			mBitMap.recycle();
			mBitMap = null;
		}			
		
		mBitMap = writeTextOnDrawable(MarkerMapFragment
				.getAqiPointerImageResId(aqiValue, iconOval),
				aqiValue);
				
		mArrayListMarker.add(aMap.addMarker(new MarkerOptions()
				.anchor(0.5f, 0.5f)
				.position(latLng)
				.title(cityName)
				.snippet(
						"PM2.5: " + pm25 + ", PM10: " + pm10 + ", SO2: " + so2
								+ ", NO2: " + no2)
				.draggable(true)
				.icon(BitmapDescriptorFactory.fromBitmap(mBitMap))));
	}
	
	private Bitmap writeTextOnDrawable(int drawableId, int text) {
		
		Bitmap bm = BitmapFactory.decodeResource(
				this.getResources(), drawableId)
				.copy(Bitmap.Config.ARGB_8888, true);
		
		if(mCanvas!=null){
			mCanvas = null;
		}
		
		mCanvas = new Canvas(bm);
		textView.setText(String.valueOf(text));
		view.measure(mCanvas.getWidth(), mCanvas.getHeight());
		view.layout(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
		view.draw(mCanvas);
		return bm;
	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		mAqiCity.setText(marker.getTitle());
		mAqiDetails.setText(marker.getSnippet());
		mAqiMarker.setImageBitmap(marker.getIcons().get(0).getBitmap());
		showAqiDetails();
		return false;
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

	public void render(Marker marker, View view) {
		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			SpannableString titleText = new SpannableString(title);
			titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
					titleText.length(), 0);
			titleUi.setTextSize(20);
			titleUi.setText(titleText);

		} else {
			titleUi.setText("");
		}
		String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		if (snippet != null) {
			SpannableString snippetText = new SpannableString(snippet);
			snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
					snippetText.length(), 0);
			snippetUi.setTextSize(15);
			snippetUi.setText(snippetText);
		} else {
			snippetUi.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.gaodeMapFinish) {
			finish();
		}
	}

	private void hideAnimation() {
		Animation topDown = AnimationUtils.loadAnimation(MarkerActivity.this,
				R.anim.bottom_down_aqi_drawer);
		mAqiDrawer.startAnimation(topDown);
		isAnimationDrawableOpen = false;
		mAqiDrawer.setVisibility(View.GONE);
	}

	private void showAqiDetails() {
		mAqiDrawer.setVisibility(View.VISIBLE);
		Animation bottomUp = AnimationUtils.loadAnimation(MarkerActivity.this, R.anim.bottom_up_aqi_drawer);
		mAqiDrawer.startAnimation(bottomUp);
		isAnimationDrawableOpen = true;
	}
}
