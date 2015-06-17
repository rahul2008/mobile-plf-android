package com.philips.cl.di.digitalcare.locatephilips;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.os.Build;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.philips.cl.di.digitalcare.ConsumerProductInfo;
import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.RequestData;
import com.philips.cl.di.digitalcare.ResponseCallback;
import com.philips.cl.di.digitalcare.SupportHomeFragment;
import com.philips.cl.di.digitalcare.locatephilips.GoogleMapFragment.onMapReadyListener;
import com.philips.cl.di.digitalcare.locatephilips.MapDirections.MapDirectionResponse;
import com.philips.cl.di.digitalcare.util.DigiCareLogger;
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
		OnItemClickListener, onMapReadyListener, OnMarkerClickListener,
		ResponseCallback, GpsStatus.Listener, OnMapClickListener {
	private GoogleMap mMap = null;
	private GoogleMapFragment mMapFragment = null;
	private Marker mCurrentPosition = null;
	private AtosResponseModel mAtosResponse = null;
	private ProgressDialog mProgressDialog = null;
	private ArrayList<LatLng> traceOfMe = null;
	private Bitmap mBitmapMarker = null;
	private Polyline mPolyline = null;
	private MapDirectionResponse mGetDirectionResponse = null;
	private double mSourceLat = 0;
	private double mSourceLng = 0;
	private double mDestinationLat = 0;
	private double mDestinationLng = 0;
	private String mPhoneNumber = null;
	private LocationManager mLocationManager = null;
	private String provider = null;
	private LinearLayout mLinearLayout;
	private ListView mListView;
	private TextView mShowTxtAddress = null;
	private TextView mShowTxtTitle = null;

	private ScrollView mLocationDetailScroll;

	private ArrayList<AtosResultsModel> mResultModelSet = null;

	private RelativeLayout mLocateLayout = null;
	private RelativeLayout mLocateSearchLayout = null;

	private EditText mSearchBox = null;
	private ImageView mSearchIcon = null;
	private ImageView mMarkerIcon = null;
	private Button mButtonCall = null;
	private Button mButtonDirection = null;

	private CustomGeoAdapter adapter = null;
	private int mLocateLayoutMargin = 0;
	private int mLocateSearchLayoutMargin = 0;

	private ProgressDialog mDialog = null;

	private FrameLayout.LayoutParams mLocateLayoutParentParams = null;
	private FrameLayout.LayoutParams mLocateSearchLayoutParentParams = null;

	private static final String ATOS_URL_PORT = "http://www.philips.com/search/search?q=%s&subcategory=%s&country=%s&type=servicers&sid=cp-dlr&output=json";
	private static final String TAG = LocatePhilipsFragment.class
			.getSimpleName();
	private static View mView = null;
	private static HashMap<String, AtosResultsModel> mHashMapResults = null;

	private ProgressBar mLocateNearProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			if (Build.VERSION.SDK_INT >= 11)
				getActivity().getWindow().setFlags(16777216, 16777216);
		} catch (Exception e) {
		}
		if (isConnectionAvailable())
			requestATOSResponseData();
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

	private String formAtosURL() {
		ConsumerProductInfo consumerProductInfo = DigitalCareConfigManager
				.getInstance(getActivity().getApplicationContext())
				.getConsumerProductInfo();
		return getAtosUrl(consumerProductInfo.getCtn(),
				consumerProductInfo.getSubCategory(), DigitalCareConfigManager
						.getInstance(getActivity().getBaseContext())
						.getLocale().getCountry().toLowerCase());
	}

	protected String getAtosUrl(String ctn, String subcategory, String country) {
		return String.format(ATOS_URL_PORT, ctn, subcategory, country);
	}

	protected void requestATOSResponseData() {
		DigiCareLogger.d(TAG, "CDLS Request Thread is started");
		startProgressDialog();
		DigiCareLogger.d(TAG, "ATOS URL : " + formAtosURL());
		new RequestData(formAtosURL(), this).execute();
	}

	protected void startProgressDialog() {
		if (mDialog == null)
			mDialog = new ProgressDialog(getActivity());
		mDialog.setMessage(getResources().getString(R.string.loading));
		mDialog.setCancelable(false);
		if (!(getActivity().isFinishing())) {
			mDialog.show();
		}
	}

	protected void closeProgressDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog.cancel();
			mDialog = null;
		}
	}

	private void addBoundaryToCurrentPosition(double lat, double lang) {

		MarkerOptions mMarkerOptions = new MarkerOptions();
		mMarkerOptions.position(new LatLng(lat, lang));
		mMarkerOptions.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.marker_current));
		mMarkerOptions.anchor(0.5f, 0.5f);

		CircleOptions mOptions = new CircleOptions()
				.center(new LatLng(lat, lang)).radius(10000)
				.strokeColor(0x110000FF).strokeWidth(1).fillColor(0x110000FF);
		mMap.addCircle(mOptions);
		if (mCurrentPosition != null)
			mCurrentPosition.remove();
		mCurrentPosition = mMap.addMarker(mMarkerOptions);
	}

	@Override
	public void onResponseReceived(String response) {
		DigiCareLogger.i(TAG, "Response : " + response);
		closeProgressDialog();
		if (response != null && isAdded()) {
			AtosResponseParser atosResponseParser = new AtosResponseParser(
					mParsingCompletedCallback);
			atosResponseParser.parseAtosResponse(response);
		}
	}

	private void validateAtosResponse(AtosResponseModel atosResponse) {
		mAtosResponse = atosResponse;
		if (mAtosResponse.getSuccess()
				|| mAtosResponse.getCdlsErrorModel() != null) {
			ArrayList<AtosResultsModel> resultModelSet = mAtosResponse
					.getResultsModel();
			if (resultModelSet.size() <= 0) {
				showAlert("Geo Data not found");
				return;
			}
			addMarkers(resultModelSet);
		} else {
			showAlert("Geo Data not found");
		}
	}

	@Override
	public void onPause() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
		super.onPause();
	}

	@SuppressLint("NewApi")
	private void initGoogleMapv2() {

		try {
			DigiCareLogger.v(TAG, "Initializing Google Map");
			mMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			if (mMap != null) {
				initView();

			}
		} catch (NullPointerException e) {
			DigiCareLogger
					.v(TAG,
							"Failed to get GoogleMap so so enabling Google v2 Map Compatibility Enabled");
			mMapFragment = GoogleMapFragment.newInstance();
			getChildFragmentManager().beginTransaction()
					.replace(R.id.map, mMapFragment).commit();
			mMap = mMapFragment.getMap();

		}

	}

	private void initView() {
		if (isProviderAvailable() && (provider != null)) {
			DigiCareLogger.i(TAG, "Provider is [" + provider + "]");
			locateCurrentPosition();
		}
		mLinearLayout = (LinearLayout) getActivity().findViewById(
				R.id.showlayout);
		mListView = (ListView) getActivity().findViewById(R.id.placelistview);
		mShowTxtTitle = (TextView) getActivity().findViewById(
				R.id.show_place_title);
		mShowTxtAddress = (TextView) getActivity().findViewById(
				R.id.show_place_address);
		mLocateLayout = (RelativeLayout) getActivity().findViewById(
				R.id.locate_layout);
		mLocateSearchLayout = (RelativeLayout) getActivity().findViewById(
				R.id.locate_search_layout);
		mSearchBox = (EditText) getActivity().findViewById(R.id.search_box);
		mSearchIcon = (ImageView) getActivity().findViewById(R.id.search_icon);
		mMarkerIcon = (ImageView) getActivity().findViewById(R.id.marker_icon);
		mLocationDetailScroll = (ScrollView) getActivity().findViewById(
				R.id.locationDetailScroll);
		mButtonCall = (Button) getActivity().findViewById(R.id.call);
		mButtonCall.setTransformationMethod(null);
		mButtonDirection = (Button) getActivity().findViewById(
				R.id.getdirection);
		mButtonDirection.setTransformationMethod(null);
		mButtonCall.setOnClickListener(this);
		mSearchIcon.setOnClickListener(this);
		mMarkerIcon.setOnClickListener(this);
		mListView.setTextFilterEnabled(true);
		mListView.setOnItemClickListener(this);
		mButtonDirection.setOnClickListener(this);
		mMap.setOnMapClickListener(this);
		mLocateLayoutMargin = (int) getActivity().getResources().getDimension(
				R.dimen.locate_layout_margin);
		mLocateSearchLayoutMargin = (int) getActivity().getResources()
				.getDimension(R.dimen.locate_search_layout_margin);
		mLocateLayoutParentParams = (FrameLayout.LayoutParams) mLocateLayout
				.getLayoutParams();
		mLocateSearchLayoutParentParams = (FrameLayout.LayoutParams) mLocateSearchLayout
				.getLayoutParams();
		mListView.setVisibility(View.GONE);
		mLinearLayout.setVisibility(View.GONE);
		mMarkerIcon.setVisibility(View.GONE);
		mLocateNearProgressBar = (ProgressBar) getActivity().findViewById(
				R.id.locate_near_progress);
		mLocateNearProgressBar.setVisibility(View.GONE);
		Configuration config = getResources().getConfiguration();
		setViewParams(config);
	}

	private void addMarkers(final ArrayList<AtosResultsModel> resultModelSet) {
		int resultsetSize = resultModelSet.size();
		mHashMapResults = new HashMap<String, AtosResultsModel>(resultsetSize);
		mMap.setOnMarkerClickListener((OnMarkerClickListener) this);

		for (int i = 0; i < resultsetSize; i++) {
			AtosResultsModel resultModel = resultModelSet.get(i);
			AtosLocationModel locationModel = resultModel.getLocationModel();
			// AtosAddressModel addressModel = resultModel.getmAddressModel();
			double lat = Double.parseDouble(locationModel.getLatitude());
			double lng = Double.parseDouble(locationModel.getLongitude());
			LatLng latLng = new LatLng(lat, lng);

			MarkerOptions markerOpt = new MarkerOptions();
			markerOpt.position(latLng);
			markerOpt.draggable(false);
			markerOpt.visible(true);
			markerOpt.anchor(0.5f, 0.5f);
			markerOpt.icon(BitmapDescriptorFactory.fromBitmap(mBitmapMarker));

			Marker marker = mMap.addMarker(markerOpt);
			mHashMapResults.put(marker.getId(), resultModel);
		}
		mResultModelSet = resultModelSet;
	}

	private void createBitmap() {
		mBitmapMarker = BitmapFactory.decodeResource(
				getActivity().getResources(), R.drawable.marker_shadow).copy(
				Bitmap.Config.ARGB_8888, true);
	}

	/**
	 * Move my position button at the bottom of map
	 */

	public void zoomToOnClick(View v) {
		mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 3000, null);
	}

	private boolean isProviderAvailable() {
		mLocationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		provider = mLocationManager.getBestProvider(criteria, true);
		if (mLocationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
			DigiCareLogger.v(TAG, "Network provider is enabled");
			return true;
		}

		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
			DigiCareLogger.v(TAG, "GPS provider is enabled");
			return true;
		}

		if (provider != null) {
			return true;
		}
		return false;
	}

	private void locateCurrentPosition() {
		Location location = mLocationManager.getLastKnownLocation(provider);
		updateWithNewLocation(location);
		mLocationManager.addGpsStatusListener(this);
		long minTime = 5000;// ms
		float minDist = 5.0f;// meter
		mLocationManager.requestLocationUpdates(provider, minTime, minDist,
				mLocationListener);
	}

	private void setMapType(double lat, double lng) {
		if (mMap != null) {
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			addBoundaryToCurrentPosition(lat, lng);
		} else {
			DigiCareLogger
					.i(TAG,
							"MAP is null Failed to update GoogleMap.MAP_TYPE_NORMAL Maptype");
		}
	}

	/*
	 * private void cameraFocusOnMe(double lat, double lng) { CameraPosition
	 * camPosition = new CameraPosition.Builder() .target(new LatLng(lat,
	 * lng)).zoom(21).build(); if (mMap != null && camPosition != null)
	 * mMap.animateCamera(CameraUpdateFactory .newCameraPosition(camPosition));
	 * }
	 */

	private void trackToMe(final LatLng currentLocation,
			final LatLng markerPosition) {
		if (traceOfMe != null && !traceOfMe.isEmpty()) {
			traceOfMe.clear();
			traceOfMe = null;
		}
		if (traceOfMe == null) {
			mLocateNearProgressBar.setVisibility(View.VISIBLE);
			mLocateNearProgressBar.setClickable(false);
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
					if (mMap != null) {
						mPolyline = mMap.addPolyline(polylineOpt);
						mLocateNearProgressBar.setVisibility(View.GONE);
					} else {
						DigiCareLogger.i(TAG,
								"MAP is null, So unable to polyline");
					}
					if (mPolyline != null)
						mPolyline.setWidth(12);
				}
			};

			new MapDirections(mGetDirectionResponse, currentLocation,
					markerPosition);
		}

	}

	private void updateWithNewLocation(Location location) {
		String where = "";
		if (location != null && provider != null) {
			double lng = location.getLongitude();
			double lat = location.getLatitude();
			float speed = location.getSpeed();

			where = "latitude : " + lat + "\n longitude : " + lng
					+ "\n speed: " + speed + "\nProvider: " + provider;
			DigiCareLogger.i(TAG, where);
			setMapType(lat, lng);
			mSourceLat = lat;
			mSourceLng = lng;

			CameraPosition camPosition = new CameraPosition.Builder()
					.target(new LatLng(lat, lng)).zoom(10f).build();

			if (mMap != null)
				mMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(camPosition));
		} else {
			where = "No location found.";
		}
		DigiCareLogger.i(TAG, where);
	}

	@Override
	public void onGpsStatusChanged(int event) {
		switch (event) {
		case GpsStatus.GPS_EVENT_STARTED:
			DigiCareLogger.v(TAG, "GPS_EVENT_STARTED");
			break;

		case GpsStatus.GPS_EVENT_STOPPED:
			DigiCareLogger.v(TAG, "GPS_EVENT_STOPPED");
			break;

		case GpsStatus.GPS_EVENT_FIRST_FIX:
			DigiCareLogger.v(TAG, "GPS_EVENT_FIRST_FIX");
			break;

		case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
			break;
		}
	}

	private LocationListener mLocationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			DigiCareLogger.v(TAG, "LocationListener Changed..");
			updateWithNewLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			DigiCareLogger.v(TAG, "Location Listener Disabled");
			updateWithNewLocation(null);
		}

		@Override
		public void onProviderEnabled(String provider) {
			DigiCareLogger.v(TAG, "Location Listener Enabled");
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				DigiCareLogger.v(TAG, "Status Changed: Out of Service");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				DigiCareLogger
						.v(TAG, "Status Changed: Temporarily Unavailable");
				break;
			case LocationProvider.AVAILABLE:
				DigiCareLogger.v(TAG, "Status Changed: Available");
				break;
			}
		}
	};

	private boolean checkGooglePlayServices() {
		int result = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity());
		switch (result) {
		case ConnectionResult.SUCCESS:
			DigiCareLogger.d(TAG, "SUCCESS");
			return true;

		case ConnectionResult.SERVICE_INVALID:
			DigiCareLogger.d(TAG, "SERVICE_INVALID");
			GooglePlayServicesUtil.getErrorDialog(
					ConnectionResult.SERVICE_INVALID, getActivity(), 0).show();
			break;

		case ConnectionResult.SERVICE_MISSING:
			DigiCareLogger.d(TAG, "SERVICE_MISSING");
			GooglePlayServicesUtil.getErrorDialog(
					ConnectionResult.SERVICE_MISSING, getActivity(), 0).show();
			break;

		case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			DigiCareLogger.d(TAG, "SERVICE_VERSION_UPDATE_REQUIRED");
			GooglePlayServicesUtil.getErrorDialog(
					ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,
					getActivity(), 0).show();
			break;

		case ConnectionResult.SERVICE_DISABLED:
			DigiCareLogger.d(TAG, "SERVICE_DISABLED");
			GooglePlayServicesUtil.getErrorDialog(
					ConnectionResult.SERVICE_DISABLED, getActivity(), 0).show();
			break;
		}
		return false;
	}

	@Override
	public void onStop() {
		super.onStop();

		mLocationManager = null;
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

		if (mHashMapResults != null && mHashMapResults.size() <= 0) {
			mHashMapResults.clear();
			mHashMapResults = null;
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
		return getResources().getString(R.string.find_philips_near_you);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.search_icon) {
			hideKeyboard();
			String constrain = mSearchBox.getText().toString().trim();

			if (mResultModelSet != null) {
				adapter = new CustomGeoAdapter(getActivity(), mResultModelSet);
				adapter.getFilter().filter(constrain,
						new Filter.FilterListener() {
							public void onFilterComplete(int count) {
								mListView.setAdapter(adapter);
								mListView.setVisibility(View.VISIBLE);
								mLinearLayout.setVisibility(View.GONE);
								mMarkerIcon.setVisibility(View.VISIBLE);
							}
						});
			}

		} else if (v.getId() == R.id.getdirection) {

			if (isConnectionAvailable()) {
				trackToMe(new LatLng(mSourceLat, mSourceLng), new LatLng(
						mDestinationLat, mDestinationLng));
				mLinearLayout.setVisibility(View.GONE);
			}

		} else if (v.getId() == R.id.marker_icon) {
			mListView.setVisibility(View.GONE);
			mMarkerIcon.setVisibility(View.GONE);
			mSearchBox.setText(null);
		} else if (v.getId() == R.id.call) {
			mLinearLayout.setVisibility(View.GONE);
			if (mPhoneNumber != null && !mAtosResponse.getSuccess()) {
				DigiCareLogger.i(TAG, mAtosResponse.getCdlsErrorModel()
						.getErrorMessage());
			} else if (Utils.isSimAvailable(getActivity())) {
				callPhilips();
			} else if (!Utils.isSimAvailable(getActivity())) {
				DigiCareLogger.i(TAG, "Check the SIM");
				showAlert("Check the SIM");
			}
		}
	}

	private void callPhilips() {
		Intent myintent = new Intent(Intent.ACTION_CALL);
		myintent.setData(Uri.parse("tel:" + mPhoneNumber));
		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		DigiCareLogger.d(TAG, "Contact Number : " + mPhoneNumber);
		startActivity(myintent);
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		mLocationDetailScroll.fullScroll(ScrollView.FOCUS_UP);
		AtosResultsModel resultModel = (AtosResultsModel) adapter
				.getItem(position);
		showServiceCentreDetails(resultModel);
	}

	private void showServiceCentreDetails(AtosResultsModel resultModel) {

		AtosLocationModel mGeoData = null;
		AtosAddressModel mAddressModel = null;
		try {
			mAddressModel = resultModel.getmAddressModel();
			mGeoData = resultModel.getLocationModel();
		} catch (NullPointerException e) {
			DigiCareLogger.d(TAG, " " + e);
			return;
		}

		mDestinationLat = Double.parseDouble(mGeoData.getLatitude());
		mDestinationLng = Double.parseDouble(mGeoData.getLongitude());
		mShowTxtTitle.setText(resultModel.getTitle());
		mShowTxtAddress.setText(mAddressModel.getAddress1() + "\n"
				+ mAddressModel.getCityState() + "\n" + mAddressModel.getUrl());
		ArrayList<String> phoneNumbers = mAddressModel.getPhoneList();
		mPhoneNumber = phoneNumbers.get(0);
		mButtonCall.setText(getResources().getString(R.string.call) + " "
				+ mPhoneNumber);
		mListView.setVisibility(View.GONE);
		mLinearLayout.setVisibility(View.VISIBLE);
		mMarkerIcon.setVisibility(View.GONE);
	}

	AlertDialog.Builder mdialogBuilder = null;
	AlertDialog malertDialog = null;

	@Override
	public void onResume() {
		super.onResume();
		if (!isConnectionAvailable()) {
			if (mdialogBuilder == null) {
				mdialogBuilder = new AlertDialog.Builder(getActivity());

				mdialogBuilder.setTitle("Alert");
				mdialogBuilder.setMessage("No Network");

				mdialogBuilder.setPositiveButton(R.string.enableNetwork,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								startActivityForResult(
										new Intent(
												android.provider.Settings.ACTION_SETTINGS),
										0);
							}
						});

				mdialogBuilder.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								backstackFragment();
								SupportHomeFragmentisInLayout();
							}
						}).setIcon(android.R.drawable.ic_dialog_alert);

				malertDialog = mdialogBuilder.create();
				malertDialog.show();
			}

		} else {

			if (malertDialog != null) {
				malertDialog.dismiss();
				backstackFragment();
				SupportHomeFragmentisInLayout();
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
		DigiCareLogger.v(TAG, "onMAP Ready Callback : " + mMap);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		AtosResultsModel resultModel = mHashMapResults.get(marker.getId());
		showServiceCentreDetails(resultModel);
		return true;
	}

	@Override
	public void onMapClick(LatLng arg0) {

		if (mListView.getVisibility() == View.VISIBLE)
			mListView.setVisibility(View.GONE);

		if (mMarkerIcon.getVisibility() == View.VISIBLE)
			mMarkerIcon.setVisibility(View.GONE);

		if (mLinearLayout.getVisibility() == View.VISIBLE)
			mLinearLayout.setVisibility(View.GONE);
	}

	private AtosParsingCallback mParsingCompletedCallback = new AtosParsingCallback() {
		@Override
		public void onAtosParsingComplete(final AtosResponseModel response) {
			if (response != null) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						validateAtosResponse(response);
					}
				});
			}
		}
	};
}