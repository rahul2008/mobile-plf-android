package com.philips.cl.di.digitalcare.locatephilips;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.contactus.CdlsRequestTask;
import com.philips.cl.di.digitalcare.contactus.CdlsResponseCallback;

/**
 * LocateNearYouFragment will help to locate PHILIPS SERVICE CENTERS on the
 * screen. This class will invoke ATOS server and getting store details in
 * JSON/XML format.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 8 May 2015
 */
@SuppressLint("SetJavaScriptEnabled")
public class LocatePhilipsFragment extends DigitalCareBaseFragment {
	final LatLng BANGALORE = new LatLng(12.920102, 77.571412);
	private GoogleMap mMap = null;
	private TextView txtOutput = null;
	private Marker markerMe = null;
	private LinearLayout mConactUsParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private AtosResponseParser mCdlsResponseParser = null;
	private AtosResponseModel mCdlsParsedResponse = null;
	private String mCdlsResponseStr = null;
	private View mView = null;
	private ProgressDialog mPostProgress = null;
	private Configuration config = null;
	private ArrayList<LatLng> traceOfMe = null;
	// CDLS related
	private CdlsRequestTask mCdlsRequestTask = null;
	// private static final String CDLS_BASE_URL_PREFIX =
	// "http://www.philips.com/prx/cdls/B2C/";
	// private static final String CDLS_BASE_URL_POSTFIX =
	// ".querytype.(fallback)";

	// private static final String CDLS_BASE_URL_PREFIX =
	// "http://www.philips.com/search/search?q=FC5830/81&lat=12.920102&lng=77.571412&subcategory=BAGLESS_VACUUM_CLEANERS_SU&country=in&type=servicers&sid=cp-dlr&output=json";

	private static final String CDLS_BASE_URL_PREFIX = "http://www.philips.com/search/search?q=FC5830/81&&subcategory=BAGLESS_VACUUM_CLEANERS_SU&country=in&type=servicers&sid=cp-dlr&output=json";

	private static final String TAG = LocatePhilipsFragment.class
			.getSimpleName();

	/** GPS */
	private LocationManager locationMgr;
	private String provider;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mCdlsRequestTask = new CdlsRequestTask(getActivity(), formCdlsURL(),
				mCdlsResponseCallback);
		if (!(mCdlsRequestTask.getStatus() == AsyncTask.Status.RUNNING || mCdlsRequestTask
				.getStatus() == AsyncTask.Status.FINISHED)) {
			mCdlsRequestTask.execute();
		}
		View view = inflater.inflate(R.layout.fragment_locate_philips,
				container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		initMap();
		checkGooglePlayServices();
		drawPolyline();
		Log.i("testing", "initLocationProvider : " + initLocationProvider());
		if (initLocationProvider()) {
			whereAmI();
		} else {
			txtOutput.setText("RITESH");
		}
	}

	/*
	 * Forming CDLS url. This url will be different for US and other countries.
	 */
	private String formCdlsURL() {
		return CDLS_BASE_URL_PREFIX/*
									 * + DigitalCareConfigManager.getLocale() +
									 * DigitalCareConfigManager
									 * .getCdlsPrimarySubCategory() +
									 * CDLS_BASE_URL_POSTFIX
									 */;
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

	private CdlsResponseCallback mCdlsResponseCallback = new CdlsResponseCallback() {

		@Override
		public void onCdlsResponseReceived(String response) {
			Log.i(TAG, "response : " + response);
			if (response != null/* && isAdded() */) {
				mCdlsResponseStr = response;
				mCdlsResponseParser = AtosResponseParser
						.getParserControllInstance(getActivity());
				mCdlsResponseParser.processCdlsResponse(response);
				mCdlsParsedResponse = mCdlsResponseParser.getCdlsBean();
				if (mCdlsParsedResponse != null) {
					if (mCdlsParsedResponse.getSuccess()) {
						// if (Utils.isEmpty(mCdlsParsedResponse.getPhone()
						// .getOpeningHoursSaturday())) {
						// mSecondRowText.setText(mCdlsParsedResponse
						// .getPhone().getOpeningHoursSunday());
						// } else {
						// mSecondRowText.setText(mCdlsParsedResponse
						// .getPhone().getOpeningHoursSaturday());
						// }
						// if (hasEmptyChatContent(mCdlsParsedResponse)) {
						// mChat.setBackgroundResource(R.drawable.selector_option_button_faded_bg);
						// mChat.setEnabled(false);
						// }
					}
				}
			}
		}
	};

	@Override
	public void onPause() {
		if (mPostProgress != null && mPostProgress.isShowing()) {
			mPostProgress.dismiss();
			mPostProgress = null;
		}
		super.onPause();
	}

	private void initView() {
		txtOutput = (TextView) getActivity().findViewById(R.id.txtOutput);
	}

	private void initMap() {
		if (mMap == null) {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			if (mMap != null) {
				// 設定地圖類型
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				// Marker1
				MarkerOptions markerOpt = new MarkerOptions();
				markerOpt.position(BANGALORE);
				markerOpt.title("台北101");
				markerOpt
						.snippet("於1999年動工，2004年12月31日完工啟用，樓高509.2公尺。");
				markerOpt.draggable(false);
				markerOpt.visible(true);
				markerOpt.anchor(0.5f, 0.5f);// 設為圖片中心

				for (int rad = 100; rad <= 500; rad = rad + 100) {

					CircleOptions circleOptions = new CircleOptions()
							.center(BANGALORE)
							// set center
							.radius(rad)
							// set radius in meters
							.fillColor(Color.parseColor("#DAD7DE"))
							// default
							.strokeColor(Color.parseColor("#7B69F3"))
							.strokeWidth(1);

					mMap.addCircle(circleOptions);
				}

				// markerOpt.icon(BitmapDescriptorFactory
				// .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)/*
				// * BitmapDescriptorFactory
				// * .
				// * fromResource
				// * (
				// * android
				// * .R.
				// * drawable
				// * .
				// * ic_menu_mylocation
				// * )
				// */);
				//
				// mMap.addMarker(markerOpt);
				//
				// //Marker2
				// MarkerOptions markerOpt2 = new MarkerOptions();
				// markerOpt2.position(TAIPEI_TRAIN_STATION);
				// markerOpt2.title("台北火車站");
				//
				// mMap.addMarker(markerOpt2);
				//
				// //Marker3
				// MarkerOptions markerOpt3 = new MarkerOptions();
				// markerOpt3.position(NATIONAL_TAIWAN_MUSEUM);
				// markerOpt3.title("國立台灣博物館");
				// markerOpt3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				//
				// mMap.addMarker(markerOpt3);

				zoomInOnClick();
			}
		}

	}

	/**
	 * 畫線
	 */
	private void drawPolyline() {
		PolylineOptions polylineOpt = new PolylineOptions();
		polylineOpt.add(new LatLng(25.033611, 121.565000));
		polylineOpt.add(new LatLng(25.032728, 121.565137));
		polylineOpt.add(new LatLng(25.033739, 121.527886));
		polylineOpt.add(new LatLng(25.038716, 121.517758));
		polylineOpt.add(new LatLng(25.045656, 121.519636));
		polylineOpt.add(new LatLng(25.046200, 121.517533));

		polylineOpt.color(Color.BLUE);

		Polyline polyline = mMap.addPolyline(polylineOpt);
		polyline.setWidth(10);
	}

	/**
	 * 按鈕:移動攝影機到墾丁
	 * 
	 * @param v
	 */
	public void moveOnClick(View v) {
		// move camera
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BANGALORE, 15));
	}

	/**
	 * 按鈕:放大地圖
	 * 
	 * @param v
	 */
	public void zoomInOnClick() {
		// zoom in
		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(new CameraPosition(BANGALORE, 13.5f, 30f,
						112.5f))/* CameraUpdateFactory.zoomIn() */);
		mMap.setTrafficEnabled(true);

		UiSettings settings = mMap.getUiSettings();
		settings.setAllGesturesEnabled(true);
		settings.setCompassEnabled(true);
		settings.setMyLocationButtonEnabled(true);
		settings.setRotateGesturesEnabled(true);
		settings.setScrollGesturesEnabled(true);
		settings.setTiltGesturesEnabled(true);
		settings.setZoomControlsEnabled(true);
		settings.setZoomGesturesEnabled(true);
	}

	/**
	 * 按鈕:縮小地圖
	 * 
	 * @param v
	 */
	public void zoomToOnClick(View v) {
		// zoom to level 10, animating with a duration of 3 seconds
		mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 3000, null);
	}

	/**
	 * 按鈕:攝影機移動到日月潭
	 * 
	 * @param v
	 */
	// public void animToOnClick(View v){
	// CameraPosition cameraPosition = new CameraPosition.Builder()
	// .target(ZINTUN) // Sets the center of the map to ZINTUN
	// .zoom(13) // Sets the zoom
	// .bearing(90) // Sets the orientation of the camera to east
	// .tilt(30) // Sets the tilt of the camera to 30 degrees
	// .build(); // Creates a CameraPosition from the builder
	// mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	// }

	/************************************************
	 * 
	 * GPS部份
	 * 
	 ***********************************************/
	/**
	 * GPS初始化，取得可用的位置提供器
	 * 
	 * @return
	 */
	private boolean initLocationProvider() {
		locationMgr = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

		// 1.選擇最佳提供器
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		provider = locationMgr.getBestProvider(criteria, true);

		if (provider != null) {
			return true;
		}

		// 2.選擇使用GPS提供器
		if (locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
			return true;
		}

		// 3.選擇使用網路提供器
		if (locationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
			return true;
		}

		return false;
	}

	private void whereAmI() {
		// String provider = LocationManager.GPS_PROVIDER;

		// 取得上次已知的位置
		Location location = locationMgr.getLastKnownLocation(provider);
		updateWithNewLocation(location);

		// GPS Listener
		locationMgr.addGpsStatusListener(gpsListener);

		// Location Listener
		long minTime = 5000;// ms
		float minDist = 5.0f;// meter
		locationMgr.requestLocationUpdates(provider, minTime, minDist,
				locationListener);
	}

	/**
	 * 顯示"我"在哪裡
	 * 
	 * @param lat
	 * @param lng
	 */
	private void showMarkerMe(double lat, double lng) {
		if (markerMe != null) {
			markerMe.remove();
		}

		MarkerOptions markerOpt = new MarkerOptions();
		markerOpt.position(new LatLng(lat, lng));
		markerOpt.title("我在這裡");
		markerMe = mMap.addMarker(markerOpt);

		Toast.makeText(getActivity(), "lat:" + lat + ",lng:" + lng,
				Toast.LENGTH_SHORT).show();
	}

	private void cameraFocusOnMe(double lat, double lng) {
		CameraPosition camPosition = new CameraPosition.Builder()
				.target(new LatLng(lat, lng)).zoom(16).build();

		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));
	}

	private void trackToMe(double lat, double lng) {
		if (traceOfMe == null) {
			traceOfMe = new ArrayList<LatLng>();
		}
		traceOfMe.add(new LatLng(lat, lng));

		PolylineOptions polylineOpt = new PolylineOptions();
		for (LatLng latlng : traceOfMe) {
			polylineOpt.add(latlng);
		}

		polylineOpt.color(Color.RED);

		Polyline line = mMap.addPolyline(polylineOpt);
		line.setWidth(10);
	}

	/**
	 * 更新並顯示新位置
	 * 
	 * @param location
	 */
	private void updateWithNewLocation(Location location) {
		String where = "";
		if (location != null) {
			// 經度
			double lng = location.getLongitude();
			// 緯度
			double lat = location.getLatitude();
			// 速度
			float speed = location.getSpeed();
			// 時間
			long time = location.getTime();
			String timeString = getTimeString(time);

			where = "經度: " + lng + "\n緯度: " + lat + "\n速度: " + speed
					+ "\n時間: " + timeString + "\nProvider: " + provider;

			// 標記"我"
			showMarkerMe(lat, lng);
			cameraFocusOnMe(lat, lng);
			trackToMe(lat, lng);

			// 移動攝影機跟著"我"
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(lat, lng)) // Sets the center of the map
													// to ZINTUN
					.zoom(13) // Sets the zoom
					.bearing(90) // Sets the orientation of the camera to east
					.tilt(30) // Sets the tilt of the camera to 30 degrees
					.build(); // Creates a CameraPosition from the builder
			mMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

			CameraPosition camPosition = new CameraPosition.Builder()
					.target(new LatLng(lat, lng)).zoom(16).build();

			mMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(camPosition));

		} else {
			where = "No location found.";
		}

		// 位置改變顯示
		txtOutput.setText(where);
	}

	GpsStatus.Listener gpsListener = new GpsStatus.Listener() {

		@Override
		public void onGpsStatusChanged(int event) {
			switch (event) {
			case GpsStatus.GPS_EVENT_STARTED:
				Log.d(TAG, "GPS_EVENT_STARTED");
				Toast.makeText(getActivity(), "GPS_EVENT_STARTED",
						Toast.LENGTH_SHORT).show();
				break;

			case GpsStatus.GPS_EVENT_STOPPED:
				Log.d(TAG, "GPS_EVENT_STOPPED");
				Toast.makeText(getActivity(), "GPS_EVENT_STOPPED",
						Toast.LENGTH_SHORT).show();
				break;

			case GpsStatus.GPS_EVENT_FIRST_FIX:
				Log.d(TAG, "GPS_EVENT_FIRST_FIX");
				Toast.makeText(getActivity(), "GPS_EVENT_FIRST_FIX",
						Toast.LENGTH_SHORT).show();
				break;

			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Log.d(TAG, "GPS_EVENT_SATELLITE_STATUS");
				break;
			}
		}
	};

	LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				Log.v(TAG, "Status Changed: Out of Service");
				Toast.makeText(getActivity(), "Status Changed: Out of Service",
						Toast.LENGTH_SHORT).show();
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Log.v(TAG, "Status Changed: Temporarily Unavailable");
				Toast.makeText(getActivity(),
						"Status Changed: Temporarily Unavailable",
						Toast.LENGTH_SHORT).show();
				break;
			case LocationProvider.AVAILABLE:
				Log.v(TAG, "Status Changed: Available");
				Toast.makeText(getActivity(), "Status Changed: Available",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}

	};

	private String getTimeString(long timeInMilliseconds) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(timeInMilliseconds);
	}

	private boolean checkGooglePlayServices() {
		int result = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity());
		switch (result) {
		case ConnectionResult.SUCCESS:
			Log.d(TAG, "SUCCESS");
			return true;

		case ConnectionResult.SERVICE_INVALID:
			Log.d(TAG, "SERVICE_INVALID");
			GooglePlayServicesUtil.getErrorDialog(
					ConnectionResult.SERVICE_INVALID, getActivity(), 0).show();
			break;

		case ConnectionResult.SERVICE_MISSING:
			Log.d(TAG, "SERVICE_MISSING");
			GooglePlayServicesUtil.getErrorDialog(
					ConnectionResult.SERVICE_MISSING, getActivity(), 0).show();
			break;

		case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			Log.d(TAG, "SERVICE_VERSION_UPDATE_REQUIRED");
			GooglePlayServicesUtil.getErrorDialog(
					ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,
					getActivity(), 0).show();
			break;

		case ConnectionResult.SERVICE_DISABLED:
			Log.d(TAG, "SERVICE_DISABLED");
			GooglePlayServicesUtil.getErrorDialog(
					ConnectionResult.SERVICE_DISABLED, getActivity(), 0).show();
			break;
		}

		return false;
	}

	@Override
	public void onStop() {
		super.onStop();
		locationMgr.removeUpdates(locationListener);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mMap != null) {
			mMap.clear();
			mMap = null;
		}
	}

	@Override
	public void setViewParams(Configuration config) {

	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.opt_find_philips_near_you);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
