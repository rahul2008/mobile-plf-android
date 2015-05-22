package com.philips.cl.di.digitalcare.locatephilips;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.SupportHomeFragment;
import com.philips.cl.di.digitalcare.contactus.CdlsRequestTask;
import com.philips.cl.di.digitalcare.contactus.CdlsResponseCallback;
import com.philips.cl.di.digitalcare.locatephilips.GoogleMapFragment.onMapReadyListener;
import com.philips.cl.di.digitalcare.locatephilips.MapDirections.MapDirectionResponse;
import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.digitalcare.util.Utils;

/**
 * LocateNearYouFragment will help to locate PHILIPS SERVICE CENTERS on the
 * screen. This class will invoke ATOS server and getting store details in
 * JSON/XML format.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 8 May 2015
 */
@SuppressLint({ "SetJavaScriptEnabled", "DefaultLocale" })
public class LocatePhilipsFragment extends DigitalCareBaseFragment implements
		OnItemClickListener, onMapReadyListener {
	private static GoogleMap mMap = null;
	private GoogleMapFragment mMapFragment = null;
	private Marker markerMe = null;
	private AtosResponseParser mCdlsResponseParser = null;
	private AtosResponseModel mCdlsParsedResponse = null;
	private ProgressDialog mPostProgress = null;
	private ArrayList<LatLng> traceOfMe = null;
	private CdlsRequestTask mCdlsRequestTask = null;
	private Thread mThread = null;
	private MarkerRunnable mRunnable = null;
	private Bitmap mBitmapMarker = null;
	private Polyline mPolyline = null;
	private MapDirectionResponse mGetDirectionResponse = null;
	private double mSourceLat = 0;
	private double mSourceLng = 0;
	private double mDestinationLat = 0;
	private double mDestinationLng = 0;
	private String mPhoneNumber = null;
	private LocationManager locationMgr = null;
	private String provider = null;
	private LinearLayout mLinearLayout;
	private ListView mListView;
	private TextView mShowTxtAddress = null;
	private TextView mShowTxtTitle = null;

	// private ImageView mImgListRightArrow = null;
	private ArrayList<AtosResultsModel> mResultModelSet = null;

	private RelativeLayout mLocateLayout = null;
	private RelativeLayout mLocateSearchLayout = null;

	private EditText mSearchBox = null;
	private ImageView mSearchIcon = null;
	private ImageView mMarkerIcon = null;
	private Button mButtonCall = null;
	private Button mButtonDirection = null;

	private CustomGeoAdapter adapter = null;
	private Handler mHandler = null;
	private int mLocateLayoutMargin = 0;
	private int mLocateSearchLayoutMargin = 0;

	private AlertDialog.Builder mdialogBuilder = null;
	private AlertDialog malertDialog = null;

	private FrameLayout.LayoutParams mLocateLayoutParentParams = null;
	private FrameLayout.LayoutParams mLocateSearchLayoutParentParams = null;

	// "http://www.philips.com/search/search?q=FC5830/81&subcategory=BAGLESS_VACUUM_CLEANERS_SU&country=in&type=servicers&sid=cp-dlr&output=json";
	private static final String ATOS_BASE_URL_PREFIX = "http://www.philips.com/search/search?q=";
	private static final String ATOS_BASE_URL_SUBCATEGORY = "&subcategory=";
	private static final String ATOS_BASE_URL_COUNTRY = "&country=";
	private static final String ATOS_BASE_URL_POSTFIX = "&type=servicers&sid=cp-dlr&output=json";

	private static final String TAG = LocatePhilipsFragment.class
			.getSimpleName();
	private static View mView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mCdlsRequestTask = new CdlsRequestTask(getActivity(), formAtosURL(),
				mCdlsResponseCallback);
		if (!(mCdlsRequestTask.getStatus() == AsyncTask.Status.RUNNING || mCdlsRequestTask
				.getStatus() == AsyncTask.Status.FINISHED)) {
			mCdlsRequestTask.execute();
		}
		mHandler = new Handler();
		try {
			mView = inflater.inflate(R.layout.fragment_locate_philips,
					container, false);
		} catch (InflateException e) {
		}

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		checkGooglePlayServices();
		initGoogleMapv2();
		createBitmap();
	}

	/*
	 * Forming ATOS url to hit cloud and get JSON response. This url will be
	 * different for countries. So making this URL dynamic.
	 * 
	 * Combination of CTN and Subcategory is mandatory otherwise ATOS server
	 * will fallback to all server centres.
	 */
	private String formAtosURL() {
		/*
		 * TODO : Since airfryer app has 10 locales and ServiceCentres are not
		 * supporting for all those 10 locales. So fixing country only "in" as
		 * of now.
		 */
		return ATOS_BASE_URL_PREFIX + DigitalCareConfigManager.getCTN()
				+ ATOS_BASE_URL_SUBCATEGORY
				+ DigitalCareConfigManager.getSubCategory()
				+ ATOS_BASE_URL_COUNTRY + /*
										 * DigitalCareConfigManager.getCountry().
										 * toLowerCase()
										 */"in" + ATOS_BASE_URL_POSTFIX;
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

						ArrayList<AtosResultsModel> resultModelSet = mCdlsParsedResponse
								.getResultsModel();
						if (resultModelSet.size() <= 0) {
							return;
						}

						// mBuilder = new LatLngBounds.Builder();
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

	@SuppressLint("NewApi")
	private void initGoogleMapv2() {

		Log.v(TAG, "Initializing Google Maps");

		try {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			if (mMap != null)
				initView();
		} catch (NullPointerException e) {
			Log.v(TAG, "Googlev2 Map Compatibility Enabled");
			mMapFragment = GoogleMapFragment.newInstance();
			getChildFragmentManager().beginTransaction()
					.replace(R.id.map, mMapFragment).commit();
			mMap = mMapFragment.getMap();
		}

	}

	private void initView() {
		if (initLocationProvider()) {
			whereAmI();
			Log.v(TAG, "WhereAmI Method");
		}
		Log.d(TAG, "initView is initialized");
		mHandler.postDelayed(mMapViewRunnable, 1000l);
		mLinearLayout = (LinearLayout) getActivity().findViewById(
				R.id.showlayout);
		mListView = (ListView) getActivity().findViewById(R.id.placelistview);
		mShowTxtTitle = (TextView) getActivity().findViewById(
				R.id.show_place_title);
		mShowTxtAddress = (TextView) getActivity().findViewById(
				R.id.show_place_address);

		// mImgListRightArrow = (ImageView) getActivity().findViewById(
		// R.id.imgListRightArrow);
		mLocateLayout = (RelativeLayout) getActivity().findViewById(
				R.id.locate_layout);
		mLocateSearchLayout = (RelativeLayout) getActivity().findViewById(
				R.id.locate_search_layout);
		mSearchBox = (EditText) getActivity().findViewById(R.id.search_box);
		mSearchIcon = (ImageView) getActivity().findViewById(R.id.search_icon);
		mMarkerIcon = (ImageView) getActivity().findViewById(R.id.marker_icon);

		mButtonCall = (Button) getActivity().findViewById(R.id.call);
		mButtonDirection = (Button) getActivity().findViewById(
				R.id.getdirection);

		mButtonCall.setOnClickListener(this);
		mSearchIcon.setOnClickListener(this);
		mMarkerIcon.setOnClickListener(this);
		mListView.setTextFilterEnabled(true);
		mListView.setOnItemClickListener(this);
		mButtonDirection.setOnClickListener(this);

		mLocateLayoutMargin = (int) getActivity().getResources().getDimension(
				R.dimen.locate_layout_margin);
		mLocateSearchLayoutMargin = (int) getActivity().getResources()
				.getDimension(R.dimen.locate_search_layout_margin);

		mLocateLayoutParentParams = (FrameLayout.LayoutParams) mLocateLayout
				.getLayoutParams();

		mLocateSearchLayoutParentParams = (FrameLayout.LayoutParams) mLocateSearchLayout
				.getLayoutParams();

		Configuration config = getResources().getConfiguration();
		setViewParams(config);
	}

	private class MarkerRunnable implements Runnable {
		private AtosResponseModel atosResponseModel = null;

		public MarkerRunnable(AtosResponseModel atosResponseModel) {
			this.atosResponseModel = atosResponseModel;
		}

		@Override
		public void run() {
			ArrayList<AtosResultsModel> resultModelSet = atosResponseModel
					.getResultsModel();
			if (resultModelSet.size() <= 0) {
				return;
			}
			addMarkers(resultModelSet);
		}
	}

	private void addMarkers(final ArrayList<AtosResultsModel> resultModelSet) {

		for (int i = 0; i < resultModelSet.size(); i++) {
			AtosResultsModel resultModel = resultModelSet.get(i);
			AtosLocationModel locationModel = resultModel.getLocationModel();
			AtosAddressModel addressModel = resultModel.getmAddressModel();
			double lat = Double.parseDouble(locationModel.getLatitude());
			double lng = Double.parseDouble(locationModel.getLongitude());
			LatLng latLng = new LatLng(lat, lng);

			MarkerOptions markerOpt = new MarkerOptions();
			markerOpt.position(latLng);
			markerOpt.title(resultModel.getTitle());
			markerOpt.snippet(addressModel.getCityState());
			markerOpt.draggable(false);
			markerOpt.visible(true);
			markerOpt.anchor(0.5f, 0.5f);
			markerOpt.icon(BitmapDescriptorFactory.fromBitmap(mBitmapMarker));

			mMap.addMarker(markerOpt);
		}
		// zoomInOnClick();

		mResultModelSet = resultModelSet;
	}

	private void createBitmap() {
		mBitmapMarker = BitmapFactory.decodeResource(
				getActivity().getResources(), R.drawable.marker_shadow).copy(
				Bitmap.Config.ARGB_8888, true);
	}

	// public void moveOnClick(View v) { // move camera
	// mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BANGALORE, 15));
	// }

	private Runnable mMapViewRunnable = new Runnable() {

		@Override
		public void run() {

			if (mMap == null) {
				mHandler.postDelayed(mMapViewRunnable, 1000l);
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
				mMap.setMyLocationEnabled(true);
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

				resetMyButtonPosition();
			}
		}
	};

	/**
	 * Move my position button at the bottom of map
	 */
	private void resetMyButtonPosition() {
		View mapView = null;
		View btnMyLocation = null;
		MapFragment mMapFragment = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map));
		if (mMapFragment != null)
			mapView = ((MapFragment) mMapFragment).getView();

		if (mapView != null) {
			btnMyLocation = ((View) mapView.findViewById(1).getParent())
					.findViewById(2);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					80, 80); // size of button in dp
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
					RelativeLayout.TRUE);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL,
					RelativeLayout.TRUE);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
					RelativeLayout.TRUE);
			params.setMargins(20, 0, 0, 40);
			btnMyLocation.setLayoutParams(params);
		}
	}

	public void zoomToOnClick(View v) {
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
		Location location = locationMgr.getLastKnownLocation(provider);
		updateWithNewLocation(location);
		// GPS Listener
		locationMgr.addGpsStatusListener(mGpsListener);
		// Location Listener
		long minTime = 5000;// ms
		float minDist = 5.0f;// meter
		locationMgr.requestLocationUpdates(provider, minTime, minDist,
				mLocationListener);
	}

	private void showMarkerMe(double lat, double lng) {
		if (markerMe != null) {
			markerMe.remove();
		}

		MarkerOptions markerOpt = new MarkerOptions();
		markerOpt.position(new LatLng(lat, lng));
		markerOpt.title("testing");
		markerOpt.snippet("testing");
		// markerMe = mMap.addMarker(markerOpt);
		mMap.setMyLocationEnabled(true);
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		resetMyButtonPosition();
	}

	private void cameraFocusOnMe(double lat, double lng) {
		CameraPosition camPosition = new CameraPosition.Builder()
				.target(new LatLng(lat, lng)).zoom(2).build();

		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));
	}

	private void trackToMe(final LatLng currentLocation,
			final LatLng markerPosition) {
		if (traceOfMe != null && !traceOfMe.isEmpty()) {
			traceOfMe.clear();
			traceOfMe = null;
		}
		if (traceOfMe == null) {
			mGetDirectionResponse = new MapDirectionResponse() {
				@Override
				public void onReceived(ArrayList<LatLng> arrayList) {
					if (arrayList == null) {
						return;
					}
					traceOfMe = arrayList;
					PolylineOptions polylineOpt = new PolylineOptions();
					for (LatLng latlng : traceOfMe) {
						polylineOpt.add(latlng);
					}

					polylineOpt.color(Color.BLUE);

					if (mPolyline != null) {
						mPolyline.remove();
						mPolyline = null;
					}
					mPolyline = mMap.addPolyline(polylineOpt);
					mPolyline.setWidth(12);
				}
			};
			new MapDirections(mGetDirectionResponse, currentLocation,
					markerPosition);
		}

		// PolylineOptions polylineOpt = new PolylineOptions();
		// for (LatLng latlng : traceOfMe) {
		// polylineOpt.add(latlng);
		// }
		//
		// polylineOpt.color(Color.BLUE);
		//
		// if (mPolyline != null) {
		// mPolyline.remove();
		// mPolyline = null;
		// }
		// /* Polyline */mPolyline = mMap.addPolyline(polylineOpt);
		// mPolyline.setWidth(8);
	}

	private void updateWithNewLocation(Location location) {
		String where = "";
		if (location != null) {
			double lng = location.getLongitude();
			double lat = location.getLatitude();
			float speed = location.getSpeed();

			where = "latitude : " + lat + "\n longitude : " + lng
					+ "\n speed: " + speed + "\nProvider: " + provider;
			DLog.i(TAG, where);
			showMarkerMe(lat, lng);
			cameraFocusOnMe(lat, lng);
			mSourceLat = lat;
			mSourceLng = lng;

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(lat, lng)).zoom(2) // Sets the zoom
					.bearing(90) // Sets the orientation of the camera to east
					.tilt(30) // Sets the tilt of the camera to 30 degrees
					.build(); // Creates a CameraPosition from the builder
			mMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

			CameraPosition camPosition = new CameraPosition.Builder()
					.target(new LatLng(lat, lng)).zoom(6).build();

			mMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(camPosition));
		} else {
			where = "No location found.";
		}
		DLog.i(TAG, where);
	}

	private GpsStatus.Listener mGpsListener = new GpsStatus.Listener() {

		@Override
		public void onGpsStatusChanged(int event) {
			switch (event) {
			case GpsStatus.GPS_EVENT_STARTED:
				DLog.d(TAG, "GPS_EVENT_STARTED");
				break;

			case GpsStatus.GPS_EVENT_STOPPED:
				DLog.d(TAG, "GPS_EVENT_STOPPED");
				break;

			case GpsStatus.GPS_EVENT_FIRST_FIX:
				DLog.d(TAG, "GPS_EVENT_FIRST_FIX");
				break;

			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				DLog.d(TAG, "GPS_EVENT_SATELLITE_STATUS");
				break;
			}
		}
	};

	private LocationListener mLocationListener = new LocationListener() {

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
				DLog.v(TAG, "Status Changed: Out of Service");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				DLog.v(TAG, "Status Changed: Temporarily Unavailable");
				break;
			case LocationProvider.AVAILABLE:
				DLog.v(TAG, "Status Changed: Available");
				break;
			}
		}
	};

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

		if (mHandler != null) {
			mHandler.removeCallbacks(mMapViewRunnable);
		}

		locationMgr = null;
		mHandler = null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mMap != null) {
			mMap.clear();
			mMap = null;
		}
		if (mResultModelSet != null) {
			mResultModelSet.clear();
			mResultModelSet = null;
		}
		mLocationListener = null;
		mGpsListener = null;

		if (mdialogBuilder != null) {
			mdialogBuilder = null;
		}
		if (malertDialog != null) {
			malertDialog = null;
		}
	}

	@Override
	public void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mLocateLayoutParentParams.leftMargin = mLocateLayoutParentParams.rightMargin = mLocateLayoutMargin;
			mLocateSearchLayoutParentParams.leftMargin = mLocateSearchLayoutParentParams.rightMargin = mLocateSearchLayoutMargin;
		} else {
			mLocateLayoutParentParams.leftMargin = mLocateLayoutParentParams.rightMargin = mLocateLayoutMargin
					+ mLeftRightMarginLand / 2;
			mLocateSearchLayoutParentParams.leftMargin = mLocateSearchLayoutParentParams.rightMargin = mLocateLayoutMargin
					+ mLeftRightMarginLand / 2;
		}
		mLocateLayout.setLayoutParams(mLocateLayoutParentParams);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);

		setViewParams(config);
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.opt_find_philips_near_you);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.search_icon) {
			hideKeyboard();
			String constrain = mSearchBox.getText().toString();

			// if (constrain.length() > 1) {
			// new UITask().execute(constrain);
			// } else {
			// }
			new UITask().execute(constrain);

		} else if (v.getId() == R.id.getdirection) {
			trackToMe(new LatLng(mSourceLat, mSourceLng), new LatLng(
					mDestinationLat, mDestinationLng));
			mLinearLayout.setVisibility(View.GONE);
		} else if (v.getId() == R.id.marker_icon) {
			mListView.setVisibility(View.GONE);
			mSearchBox.setText(null);
		} else if (v.getId() == R.id.call) {
			mLinearLayout.setVisibility(View.GONE);
			if (mPhoneNumber != null && !mCdlsParsedResponse.getSuccess()) {
				DLog.i(TAG, mCdlsParsedResponse.getCdlsErrorModel()
						.getErrorMessage());
			} else if (Utils.isSimAvailable(getActivity())) {
				callPhilips();
			} else if (!Utils.isSimAvailable(getActivity())) {
				DLog.i(TAG, "Check the SIM");
				Toast.makeText(getActivity(), "Check the SIM",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void callPhilips() {
		Intent myintent = new Intent(Intent.ACTION_CALL);
		myintent.setData(Uri.parse("tel:" + mPhoneNumber));
		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent);
	};

	private class UITask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			if (mResultModelSet != null) {
				adapter = new CustomGeoAdapter(getActivity(), mResultModelSet);
			}
			return params[0];

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (adapter != null) {

				adapter.getFilter().filter(result);
				mListView.setAdapter(adapter);

				mListView.setVisibility(View.VISIBLE);
				mLinearLayout.setVisibility(View.GONE);
				mMarkerIcon.setVisibility(View.VISIBLE);
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		AtosResultsModel resultModel = (AtosResultsModel) adapter
				.getItem(position);

		AtosAddressModel addressModel = resultModel.getmAddressModel();
		AtosLocationModel locationModel = resultModel.getLocationModel();

		mDestinationLat = Double.parseDouble(locationModel.getLatitude());
		mDestinationLng = Double.parseDouble(locationModel.getLongitude());

		// mPhoneNumber = addressModel.getPhone();

		mShowTxtTitle.setText(resultModel.getTitle());
		mShowTxtAddress.setText(addressModel.getAddress1() + "\n"
				+ addressModel.getCityState() + "\n" + addressModel.getUrl());

		// mImgListRightArrow.setVisibility(View.GONE);

		// String phoneNumbers[] = addressModel.getPhone().split(",");

		ArrayList<String> phoneNumbers = addressModel.getPhoneList();

		mButtonCall.setText(getResources().getString(R.string.call) + " "
				+ phoneNumbers.get(0));

		mListView.setVisibility(View.GONE);
		mLinearLayout.setVisibility(View.VISIBLE);
		mMarkerIcon.setVisibility(View.GONE);
		// linearLayout.setVisibility(View.GONE);// ritesh testing
	}

	@Override
	public void onResume() {
		super.onResume();

		// Checking Network
		if (!Utils.isNetworkConnected(getActivity())) {

			mdialogBuilder = new AlertDialog.Builder(getActivity())
					.setTitle("Alert")
					.setMessage("No Network")
					.setPositiveButton(R.string.enableNetwork,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// launch setting Activity
									startActivityForResult(
											new Intent(
													android.provider.Settings.ACTION_SETTINGS),
											0);
									backstackFragment();
									SupportHomeFragmentisInLayout();

								}
							})
					.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									backstackFragment();
									SupportHomeFragmentisInLayout();
								}
							}).setIcon(android.R.drawable.ic_dialog_alert);

			malertDialog = mdialogBuilder.create();
			malertDialog.show();
		} else {
			if (malertDialog != null) {
				malertDialog.dismiss();
				// backstackFragment();
				SupportHomeFragmentisInLayout();
			} else {
				// do nothiing
			}
		}

	}

	private void SupportHomeFragmentisInLayout() {
		final SupportHomeFragment supporthomeFragment = new SupportHomeFragment();

		if (!supporthomeFragment.isInLayout()) {
			showFragment(supporthomeFragment);
		}
	}

	@Override
	public void onMapReady() {
		mMap = mMapFragment.getMap();
		initView();
		Log.v(TAG, "onMAP Ready Callback : " + mMap);
	}

}
