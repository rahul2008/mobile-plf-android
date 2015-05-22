package com.philips.gaode.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

/**
 * 
 * MapActivity class is base class for GAODE MAP implementation. 
 * Author : Ritesh.jha@philips.com 
 * Date : 7 Oct 2014
 * 
 */
public class MapActivity extends Activity implements OnMarkerClickListener,
		OnMapLoadedListener, OnClickListener {
	protected RelativeLayout mParentLayout = null;
	protected AMap aMap;
	protected MapView mapView;
	protected ImageView mFinishActivity = null;
	protected RelativeLayout mAqiDrawer = null;
	protected TextView mAqiCity = null;
	protected TextView mAqiDetails = null;
	protected ImageView mAqiMarker = null;
	protected LayoutInflater inflater = null;
	protected View view = null;
	protected TextView textView = null;
	private boolean isAnimationDrawableOpen = false;
	private Canvas mCanvas = null;
	private float markerAnchorFirstParam = 0.0f;
	private float markerAnchorSecondParam = 0.0f;
	private LatLng markerLatLng = null;
	private String markerTitle = null;
	private String markerSnippet = null;
	private boolean markerDraggable = true;
	private boolean markerIconOval = false;
	private int markerAqiValue = 0;
	protected static final int DEFAULT_IMAGE_RESOURCE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.marker_activity);
		mParentLayout = (RelativeLayout) findViewById(R.id.mapParent);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		mFinishActivity = (ImageView) findViewById(R.id.gaodeMapFinish);
		mAqiDrawer = (RelativeLayout) findViewById(R.id.aqi_prompt_drawer);
		mAqiCity = (TextView) findViewById(R.id.aqiCity);
		mAqiDetails = (TextView) findViewById(R.id.aqiDetails);
		mAqiMarker = (ImageView) findViewById(R.id.aqiMarker);
		mFinishActivity.setVisibility(View.VISIBLE);
		mFinishActivity.setOnClickListener(this);
		mapView.setOnClickListener(this);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		init();

		view = inflater.inflate(R.layout.circle_lyt, null);
		textView = (TextView) view.findViewById(R.id.circle_txt);
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

		if (aMap != null) {
			aMap.stopAnimation();
			aMap.clear();
			aMap = null;
		}
		if (mapView != null) {
			mapView.setBackgroundColor(Color.BLACK);
			mapView.removeAllViewsInLayout();
			mapView.onDestroy();
			mapView = null;
		}

		if (mParentLayout != null) {
			mParentLayout.removeAllViews();
			mParentLayout = null;
		}
	}

	protected AMap getMarkerAMapView() {
		return aMap;
	}

	protected MapView getMarkerMapView() {
		return mapView;
	}

	protected UiSettings getMarkerUISettins() {
		return aMap != null ? aMap.getUiSettings() : null;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		mAqiCity.setText(marker.getTitle());
		mAqiDetails.setText(marker.getSnippet());
		mAqiMarker.setImageBitmap(marker.getIcons().get(0).getBitmap());
		showAqiDetails();
		return false;
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.gaodeMapFinish) {
			finish();
		}
	}

	private void hideAnimation() {
		Animation topDown = AnimationUtils.loadAnimation(this,
				R.anim.bottom_down_aqi_drawer);
		mAqiDrawer.startAnimation(topDown);
		isAnimationDrawableOpen = false;
		mAqiDrawer.setVisibility(View.GONE);
	}

	protected void showAqiDetails() {
		mAqiDrawer.setVisibility(View.VISIBLE);
		Animation bottomUp = AnimationUtils.loadAnimation(this,
				R.anim.bottom_up_aqi_drawer);
		mAqiDrawer.startAnimation(bottomUp);
		isAnimationDrawableOpen = true;
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

	protected Bitmap writeTextOnDrawable(int drawableId) {

		Bitmap bm = BitmapFactory.decodeResource(this.getResources(),
				drawableId).copy(Bitmap.Config.ARGB_8888, true);

		if (mCanvas != null) {
			mCanvas = null;
		}

		mCanvas = new Canvas(bm);
		textView.setText(String.valueOf(getMarkerAqiValue()));
		view.measure(mCanvas.getWidth(), mCanvas.getHeight());
		view.layout(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
		view.draw(mCanvas);
		return bm;
	}

	protected synchronized Marker createMarker(int imageResource) {
		Bitmap mBitMap = null;

		if (imageResource == DEFAULT_IMAGE_RESOURCE) {
			imageResource = MapUtils.getMapUtilsInstace()
					.getAqiPointerImageResId(getMarkerAqiValue(),
							isMarkerIconOval());
		}
		mBitMap = writeTextOnDrawable(imageResource);
		Marker marker = aMap.addMarker(new MarkerOptions()
				.anchor(getMarkerAnchorFirstParam(),
						getMarkerAnchorSecondParam())
				.position(getMarkerPositionLatLng()).title(getMarkerTitle())
				.snippet(getMarkerSnippet()).draggable(isMarkerDraggable())
				.icon(BitmapDescriptorFactory.fromBitmap(mBitMap)));

		return marker;
	}

	protected float getMarkerAnchorFirstParam() {
		return markerAnchorFirstParam;
	}

	protected void setMarkerAnchorFirstParam(float markerAnchorFirstParam) {
		this.markerAnchorFirstParam = markerAnchorFirstParam;
	}

	protected float getMarkerAnchorSecondParam() {
		return markerAnchorSecondParam;
	}

	protected void setMarkerAnchorSecondParam(float markerAnchorSecondParam) {
		this.markerAnchorSecondParam = markerAnchorSecondParam;
	}

	protected LatLng getMarkerPositionLatLng() {
		return markerLatLng;
	}

	protected void setMarkerPositionLatLng(LatLng markerLatLng) {
		this.markerLatLng = markerLatLng;
	}

	protected String getMarkerTitle() {
		return markerTitle;
	}

	protected void setMarkerTitle(String markerTitle) {
		this.markerTitle = markerTitle;
	}

	protected String getMarkerSnippet() {
		return markerSnippet;
	}

	protected void setMarkerSnippet(String markerSnippet) {
		this.markerSnippet = markerSnippet;
	}

	protected boolean isMarkerDraggable() {
		return markerDraggable;
	}

	protected void setMarkerDraggable(boolean markerDraggable) {
		this.markerDraggable = markerDraggable;
	}

	protected boolean isMarkerIconOval() {
		return markerIconOval;
	}

	protected void setMarkerIconOval(boolean iconOval) {
		this.markerIconOval = iconOval;
	}

	protected int getMarkerAqiValue() {
		return markerAqiValue;
	}

	protected void setMarkerAqiValue(int markerAqiValue) {
		this.markerAqiValue = markerAqiValue;
	}
}
