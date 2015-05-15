package com.philips.cl.di.digitalcare.locatephilips;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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
	private GoogleMap mMap = null;
	private Marker markerMe = null;
	private AtosResponseParser mCdlsResponseParser = null;
	private AtosResponseModel mCdlsParsedResponse = null;
	private ProgressDialog mPostProgress = null;
	private ArrayList<LatLng> traceOfMe = null;
	// CDLS related
	private CdlsRequestTask mCdlsRequestTask = null;
	private Thread mThread = null;
	private MarkerRunnable mRunnable = null;
	private Bitmap mBitmapMarker = null;
	// private static final String CDLS_BASE_URL_PREFIX =
	// "http://www.philips.com/prx/cdls/B2C/";
	// private static final String CDLS_BASE_URL_POSTFIX =
	// ".querytype.(fallback)";

	// private static final String CDLS_BASE_URL_PREFIX =
	// "http://www.philips.com/search/search?q=FC5830/81&lat=12.920102&lng=77.571412&subcategory=BAGLESS_VACUUM_CLEANERS_SU&country=in&type=servicers&sid=cp-dlr&output=json";

	private static final String CDLS_BASE_URL_PREFIX = "http://www.philips.com/search/search?q=FC5830/81&&subcategory=BAGLESS_VACUUM_CLEANERS_SU&country=in&type=servicers&sid=cp-dlr&output=json";

	private static final String TAG = "testing";/*
												 * LocatePhilipsFragment.class
												 * .getSimpleName();
												 */

	/** GPS */
	private LocationManager locationMgr;
	private String provider;
	private static View mView = null;

	// variables related to search
	private LinearLayout linearLayout;
	private ListView listview;
	private TextView txtAddress, txtCityState, txtPhone;
	private String[] titles;
	private String[] descriptions;

	private CustomGeoAdapter adapter;
	private Handler mHandler = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mCdlsRequestTask = new CdlsRequestTask(getActivity(), formCdlsURL(),
				mCdlsResponseCallback);
		if (!(mCdlsRequestTask.getStatus() == AsyncTask.Status.RUNNING || mCdlsRequestTask
				.getStatus() == AsyncTask.Status.FINISHED)) {
			mCdlsRequestTask.execute();
		}
		mHandler = new Handler();
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null)
				parent.removeView(mView);
		}
		try {
			mView = inflater.inflate(R.layout.fragment_locate_philips,
					container, false);
		} catch (InflateException e) {
		}

		linearLayout = (LinearLayout) mView.findViewById(R.id.showlayout);
		listview = (ListView) mView.findViewById(R.id.placelistview);
		txtAddress = (TextView) mView.findViewById(R.id.place_address);
		txtCityState = (TextView) mView.findViewById(R.id.place_city_state);
		txtPhone = (TextView) mView.findViewById(R.id.place_phone);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		// initMap();
		checkGooglePlayServices();
		Log.i("testing", "initLocationProvider : " + initLocationProvider());
		if (initLocationProvider()) {
			whereAmI();
		} else {

		}
		createBitmap();

		// listview

		// adapter = new CustomGeoAdapter(getActivity(), mListItems);
		//
		// listview.setAdapter(adapter);

		// SwingBottomInAnimationAdapter swingBottomInAdapter = new
		// SwingBottomInAnimationAdapter(
		// adapter);
		// swingBottomInAdapter.setAbsListView(listview);
		// listview.setAdapter(swingBottomInAdapter);

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
			if (response != null && isAdded()) {
				mCdlsResponseParser = AtosResponseParser
						.getParserControllInstance(getActivity());
				mCdlsResponseParser.processCdlsResponse(response);
				mCdlsParsedResponse = mCdlsResponseParser.getCdlsBean();
				if (mCdlsParsedResponse != null) {
					if (mCdlsParsedResponse.getSuccess()) {
						// mRunnable = new MarkerRunnable(mCdlsParsedResponse);
						// mThread = new Thread(mRunnable);
						// mThread.start();
						ArrayList<ResultsModel> resultModelSet = mCdlsParsedResponse
								.getResultsModel();
						if (resultModelSet.size() <= 0) {
							return;
						}
						addMarkers(resultModelSet);
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

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mHandler.postDelayed(mMapViewRunnable, 2000l);
	}

	private class MarkerRunnable implements Runnable {
		private AtosResponseModel atosResponseModel = null;

		public MarkerRunnable(AtosResponseModel atosResponseModel) {
			this.atosResponseModel = atosResponseModel;
		}

		@Override
		public void run() {
			ArrayList<ResultsModel> resultModelSet = atosResponseModel
					.getResultsModel();
			if (resultModelSet.size() <= 0) {
				return;
			}
			addMarkers(resultModelSet);
		}
	}

	private void addMarkers(final ArrayList<ResultsModel> resultModelSet) {

		// ResultsModel rs = resultModelSet.get(0);
		// AddressModel am = rs.getmAddressModel();

		// Toast.makeText(
		// getActivity(),
		// "City" + am.getCity() + "\nAddress" + am.getAddress1()
		// + "\nState" + am.getState() + "\nzip" + am.getZip()
		// + "\nphone" + am.getPhone(), Toast.LENGTH_SHORT).show();
		//
		// String data = "City" + am.getCity() + "\nAddress" + am.getAddress1()
		// + "\nState" + am.getState() + "\nzip" + am.getZip() + "\nphone"
		// + am.getPhone();

		for (int i = 0; i < resultModelSet.size(); i++) {
			ResultsModel resultModel = resultModelSet.get(i);
			LocationModel locationModel = resultModel.getLocationModel();

			MarkerOptions markerOpt = new MarkerOptions();
			double lat = Double.parseDouble(locationModel.getLatitude());
			double lng = Double.parseDouble(locationModel.getLongitude());

			markerOpt.position(new LatLng(lat, lng));
			markerOpt.title(resultModel.getTitle());
			markerOpt.snippet("Snippet : " + resultModel.getTitle());
			markerOpt.draggable(false);
			markerOpt.visible(true);
			markerOpt.anchor(0.5f, 0.5f);
			markerOpt.icon(BitmapDescriptorFactory.fromBitmap(mBitmapMarker));

			mMap.addMarker(markerOpt);

		}
		// zoomInOnClick();

		// adapter = new CustomGeoAdapter(getActivity(), mListItems);
		adapter = new CustomGeoAdapter(getActivity(), resultModelSet);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				AddressModel addressModel = resultModelSet.get(position)
						.getmAddressModel();

				txtAddress.setText(addressModel.getAddress1());
				txtCityState.setText(addressModel.getCity() + ","
						+ addressModel.getState());
				txtPhone.setText(addressModel.getPhone());

				listview.setVisibility(View.GONE);
				linearLayout.setVisibility(View.VISIBLE);
			}
		});

	}

	private void createBitmap() {
		mBitmapMarker = BitmapFactory.decodeResource(
				getActivity().getResources(), R.drawable.marker).copy(
				Bitmap.Config.ARGB_8888, true);
	}

	// private void initMap() {
	// if (mMap == null) {
	// mMap = ((MapFragment) getFragmentManager().findFragmentById(
	// R.id.map)).getMap();
	// }
	// // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	// MarkerOptions markerOpt = new MarkerOptions();
	// markerOpt.position(BANGALORE);
	// markerOpt.title("Ritesh Title");
	// markerOpt.snippet("Ritesh Snippet");
	// markerOpt.draggable(false);
	// markerOpt.visible(true);
	// markerOpt.anchor(0.5f, 0.5f);
	//
	// CircleOptions circleOptions = new CircleOptions().center(BANGALORE)
	// // set center
	// .radius(100)
	// // set radius in meters
	// .fillColor(Color.parseColor("#DAD7DE"))
	// // default
	// .strokeColor(Color.parseColor("#7B69F3")).strokeWidth(2);
	//
	// mMap.addCircle(circleOptions);
	// zoomInOnClick();
	// }

	/*
	 * public void zoomInOnClick() { // zoom in
	 * mMap.animateCamera(CameraUpdateFactory .newCameraPosition(new
	 * CameraPosition(BANGALORE, 13.5f, 30f, 112.5f))
	 * CameraUpdateFactory.zoomIn() ); mMap.setTrafficEnabled(true);
	 * 
	 * UiSettings settings = mMap.getUiSettings();
	 * settings.setAllGesturesEnabled(true); settings.setCompassEnabled(true);
	 * settings.setMyLocationButtonEnabled(true);
	 * settings.setRotateGesturesEnabled(true);
	 * settings.setScrollGesturesEnabled(true);
	 * settings.setTiltGesturesEnabled(true);
	 * settings.setZoomControlsEnabled(true);
	 * settings.setZoomGesturesEnabled(true); }
	 */

	// public void moveOnClick(View v) { // move camera
	// mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BANGALORE, 15));
	// }

	Runnable mMapViewRunnable = new Runnable() {

		@Override
		public void run() {

			if (mMap == null) {
				mHandler.postDelayed(mMapViewRunnable, 2000l);
			} else {
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
				mHandler.removeCallbacks(mMapViewRunnable);
			}
		}
	};

	public void zoomToOnClick(View v) {
		// zoom to level 10, animating with a duration of 3 seconds
		mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 3000, null);
	}

	private boolean initLocationProvider() {
		locationMgr = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

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

		if (locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
			return true;
		}

		if (locationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
			return true;
		}

		return false;
	}

	private void whereAmI() {
		// String provider = LocationManager.GPS_PROVIDER;

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
		Toast.makeText(getActivity(), where, Toast.LENGTH_SHORT).show();
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
		mHandler.removeCallbacks(mMapViewRunnable);
		mHandler = null;
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
