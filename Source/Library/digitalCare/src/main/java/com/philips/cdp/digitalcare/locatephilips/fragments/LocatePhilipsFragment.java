package com.philips.cdp.digitalcare.locatephilips.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.support.annotation.Nullable;
import android.view.Gravity;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.philips.cdp.digitalcare.ConsumerProductInfo;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.contactus.fragments.ContactUsFragment;
import com.philips.cdp.digitalcare.customview.GpsAlertView;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.locatephilips.CustomGeoAdapter;
import com.philips.cdp.digitalcare.locatephilips.GoToContactUsListener;
import com.philips.cdp.digitalcare.locatephilips.LocateNearCustomDialog;
import com.philips.cdp.digitalcare.locatephilips.MapDirections;
import com.philips.cdp.digitalcare.locatephilips.MapDirections.MapDirectionResponse;
import com.philips.cdp.digitalcare.locatephilips.fragments.GoogleMapFragment.onMapReadyListener;
import com.philips.cdp.digitalcare.locatephilips.models.AtosAddressModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosLocationModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosResponseModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosResultsModel;
import com.philips.cdp.digitalcare.locatephilips.parser.AtosParsingCallback;
import com.philips.cdp.digitalcare.locatephilips.parser.AtosResponseParser;
import com.philips.cdp.digitalcare.request.RequestData;
import com.philips.cdp.digitalcare.request.ResponseCallback;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.cdp.digitalcare.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * LocateNearYouFragment will help to locate PHILIPS SERVICE CENTERS on the
 * screen. This class will invoke ATOS server and getting store details in
 * JSON/XML format.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 8 May 2015
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */
@SuppressLint({"SetJavaScriptEnabled", "DefaultLocale"})
public class LocatePhilipsFragment extends DigitalCareBaseFragment implements
        OnItemClickListener, onMapReadyListener, OnMarkerClickListener,
        ResponseCallback, GpsStatus.Listener, OnMapClickListener {
    private static final String ATOS_URL_PORT = "https://www.philips.com/search/search?q=%s&subcategory=%s&country=%s&type=servicers&sid=cp-dlr&output=json";
    private static final String TAG = LocatePhilipsFragment.class
            .getSimpleName();
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static View mView = null;
    private static HashMap<String, AtosResultsModel> mHashMapResults = null;
    protected SharedPreferences mSharedpreferences;
    AlertDialog.Builder mdialogBuilder = null;
    AlertDialog malertDialog = null;
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
    private GpsAlertView gpsAlertView = null;
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
    private ImageView mArabicSearchIcon = null;
    private ImageView mMarkerIcon = null;
    private ImageView mArabicMarkerIcon = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private Button mButtonCall = null;
    private Button mButtonDirection = null;
    private CustomGeoAdapter adapter = null;
    private int mLocateLayoutMargin = 0;
    private int mLocateSearchLayoutMargin = 0;
    private ProgressDialog mDialog = null;
    private FrameLayout.LayoutParams mLocateLayoutParentParams = null;
    private FrameLayout.LayoutParams mLocateSearchLayoutParentParams = null;
    private ProgressBar mLocateNearProgressBar;
    private boolean isContactUsScreenLaunched = false;
    private Utils mUtils = null;
    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
          /*  DigiCareLogger.i(TAG, "LocationListener Changed..");*/
            updateWithNewLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
           /* DigiCareLogger.i(TAG, "Location Listener Disabled");*/
            updateWithNewLocation(null);
        }

        @Override
        public void onProviderEnabled(String provider) {
          /*  DigiCareLogger.i(TAG, "Location Listener Enabled");*/
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                    DigiCareLogger.w(TAG, "Status Changed: Out of Service");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    DigiCareLogger
                            .w(TAG, "Status Changed: Temporarily Unavailable");
                    break;
                case LocationProvider.AVAILABLE:
                    DigiCareLogger.w(TAG, "Status Changed: Available");
                    break;

                default:
                   /* DigiCareLogger.i(TAG, "Default in onStatusChanged");*/
                    break;
            }
        }
    };
    private GoToContactUsListener mGoToContactUsListener = new GoToContactUsListener() {
        @Override
        public void goToContactUsSelected() {
            isContactUsScreenLaunched = true;
            showFragment(new ContactUsFragment());
        }
    };
    private boolean isDialogShown;
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

                if (!isEulaAccepted()) {
                    showAlert(getActivity().getResources().getString(R.string.locate_philips_popup_legal));
                    setEulaPreference();
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.i(TAG, "Launching the Find Philips near you Screen");

        try {
            if (Build.VERSION.SDK_INT >= 11)
                getActivity().getWindow().setFlags(16777216, 16777216);
        } catch (Exception e) {
        }
        if (isConnectionAvailable())
            requestATOSResponseData();

        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        try {
            mView = inflater.inflate(R.layout.consumercare_fragment_locate_philips, container, false);
        } catch (InflateException e) {
        }

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);
        checkGooglePlayServices();
        initGoogleMapv2();
        createBitmap();
        mUtils = new Utils();
        try {
            AnalyticsTracker.trackPage(
                    AnalyticsConstants.PAGE_FIND_PHILIPS_NEAR_YOU,
                    getPreviousName());
        } catch (Exception e) {

        }
        gpsAlertView = GpsAlertView.getInstance();
    }

    private String formAtosURL() {
        ConsumerProductInfo consumerProductInfo = DigitalCareConfigManager
                .getInstance().getConsumerProductInfo();
        Locale locale = DigitalCareConfigManager
                .getInstance().getLocale();
        if (consumerProductInfo == null || locale == null) {
            getActivity().finish();
            return null;
        }
        String atosUrl = getAtosUrl(consumerProductInfo.getCtn(),
                consumerProductInfo.getSubCategory(), DigitalCareConfigManager
                        .getInstance().getLocale().getCountry().toLowerCase());

        DigiCareLogger.i(TAG, "ATOS URL : " + atosUrl);
        return atosUrl;


    }

    protected String getAtosUrl(String ctn, String subcategory, String country) {
        return String.format(ATOS_URL_PORT, ctn, subcategory, country);
    }

    protected void requestATOSResponseData() {
        DigiCareLogger.d(TAG, "CDLS Request Thread is started");
        if (!getActivity().isFinishing())
            startProgressDialog();
        DigiCareLogger.d(TAG, "ATOS URL : " + formAtosURL());
        RequestData requestData = new RequestData();
        requestData.setRequestUrl(formAtosURL());
        requestData.setResponseCallback(this);
        requestData.execute();
    }

    protected void startProgressDialog() {
        if (mDialog == null)
            mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage(getResources().getString(R.string.loading));
        mDialog.setCancelable(true);
        if (!(getActivity().isFinishing())) {
            try {
                mDialog.show();
            } catch (Exception e) {
                DigiCareLogger.e(TAG, "Window Leakage handled : " + e);
            }
        }
    }

    protected void closeProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            try {
                mDialog.dismiss();
                mDialog.cancel();
                mDialog = null;
            } catch (RuntimeException ex) {
                DigiCareLogger.e(TAG, "Dialog Window Leak is handled");
            }
        }
    }

    private void addBoundaryToCurrentPosition(double lat, double lang) {

        MarkerOptions mMarkerOptions = new MarkerOptions();
        mMarkerOptions.position(new LatLng(lat, lang));
        mMarkerOptions.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.consumercare_marker_current));
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
        DigiCareLogger.i(TAG, "ATOS Response : " + response);
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
            if (resultModelSet.size() <= 0 && !isContactUsScreenLaunched) {
                showCustomAlert();
                isContactUsScreenLaunched = false;
                return;
            } else {
                addMarkers(resultModelSet);
            }
        } else {
            showCustomAlert();
        }
    }

    private void showCustomAlert() {
        LocateNearCustomDialog locateNearCustomDialog = new LocateNearCustomDialog(getActivity(),
                getActivity().getSupportFragmentManager(), mGoToContactUsListener);
        if (!isDialogShown) {
            locateNearCustomDialog.show();
            isDialogShown = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("dialog_key", isDialogShown);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            isDialogShown = savedInstanceState.getBoolean("dialog_key");
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
            mMap = ((SupportMapFragment) this.getFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
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

    protected boolean isEulaAccepted() {
        mSharedpreferences = getActivity().getSharedPreferences(DigitalCareConstants.DIGITALCARE_FRAGMENT_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedpreferences.edit();

        boolean mBoolean = mSharedpreferences.getBoolean("acceptEula", false);
        editor.commit();
        return mBoolean;
    }

    protected void setEulaPreference() {
        mSharedpreferences = getActivity().getSharedPreferences(DigitalCareConstants.DIGITALCARE_FRAGMENT_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedpreferences.edit();
        editor.putBoolean("acceptEula", true);
        editor.commit();
    }

    /*
     Android Marshmallow: Android M : Permission has to be requested at runtime.
     */
    private void requestPermissionAndroidM() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            int hasPermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (isProviderAvailable() && (provider != null)) {
            DigiCareLogger.v(TAG, "Provider is [" + provider + "]");
            locateCurrentPosition();
        }
    }

    private void initView() {

        requestPermissionAndroidM();
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
        mArabicSearchIcon = (ImageView) getActivity().findViewById(R.id.arabic_search_icon);
        mMarkerIcon = (ImageView) getActivity().findViewById(R.id.marker_icon);
        mArabicMarkerIcon = (ImageView) getActivity().findViewById(R.id.arabic_marker_icon);
        mLocationDetailScroll = (ScrollView) getActivity().findViewById(
                R.id.locationDetailScroll);
        mButtonCall = (Button) getActivity().findViewById(R.id.call);
        mButtonCall.setTransformationMethod(null);
        mButtonDirection = (Button) getActivity().findViewById(
                R.id.getdirection);
        mButtonDirection.setTransformationMethod(null);
        mButtonCall.setOnClickListener(this);
        mSearchIcon.setOnClickListener(this);
        mArabicSearchIcon.setOnClickListener(this);
        mMarkerIcon.setOnClickListener(this);
        mArabicMarkerIcon.setOnClickListener(this);
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
        mArabicMarkerIcon.setVisibility(View.GONE);
        mLocateNearProgressBar = (ProgressBar) getActivity().findViewById(
                R.id.locate_near_progress);
        mLocateNearProgressBar.setVisibility(View.GONE);
        Configuration config = getResources().getConfiguration();
        setViewParams(config);
        float density = getResources().getDisplayMetrics().density;
        setButtonParams(density);
    }

    private void addMarkers(final ArrayList<AtosResultsModel> resultModelSet) {
        int resultsetSize = resultModelSet.size();
        mHashMapResults = new HashMap<String, AtosResultsModel>(resultsetSize);
        if (mMap != null) {
            mMap.setOnMarkerClickListener(this);

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
    }

    private void createBitmap() {
        mBitmapMarker = BitmapFactory.decodeResource(
                getActivity().getResources(), R.drawable.consumercare_marker_shadow).copy(
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

        return provider != null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    if (isProviderAvailable() && (provider != null)) {
                       /* DigiCareLogger.i(TAG, "Provider is [" + provider + "]");*/
                        getCurrentLocation();
                    }
                } else {
                    // Permission Denied
                    DigiCareLogger.e(TAG, "LocateNearYou -> permissions not granted" + permissions.toString());
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
                    .d(TAG,
                            "MAP is null Failed to update GoogleMap.MAP_TYPE_NORMAL Maptype");
        }
    }

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
                        DigiCareLogger.e(TAG,
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
            DigiCareLogger.v(TAG, where);
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
        /*DigiCareLogger.i(TAG, where);*/
    }

    @Override
    public void onGpsStatusChanged(int event) {
        switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
                DigiCareLogger.d(TAG, "GPS_EVENT_STARTED");
                break;

            case GpsStatus.GPS_EVENT_STOPPED:
                DigiCareLogger.d(TAG, "GPS_EVENT_STOPPED");
                break;

            case GpsStatus.GPS_EVENT_FIRST_FIX:
                DigiCareLogger.d(TAG, "GPS_EVENT_FIRST_FIX");
                break;

            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                break;

            default:
                DigiCareLogger.d(TAG, "default method on onGPSStatusChanged");
                break;
        }
    }

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
        if (mSearchBox != null)
            mSearchBox.setText(null);
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
    public void onDestroyView() {
        closeProgressDialog();
        super.onDestroyView();
    }

    @Override
    public void setViewParams(Configuration config) {

        if (mLocateLayoutParentParams != null) {

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
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);


        setViewParams(config);
    }

    @Override
    public String getActionbarTitle() {
        String title = getResources().getString(R.string.find_philips_near_you);
        DigiCareLogger.i(TAG, "Title : " + title);
        return title;
    }

    @Override
    public void onClick(View v) {

        if ((v.getId() == R.id.search_icon) || (v.getId() == R.id.arabic_search_icon)) {
            hideKeyboard();
            final String constrain = mSearchBox.getText().toString().trim();

            if (mResultModelSet != null) {
                adapter = new CustomGeoAdapter(getActivity(), mResultModelSet);
                adapter.getFilter().filter(constrain,
                        new Filter.FilterListener() {
                            public void onFilterComplete(int count) {

                                /*
                                It been instructed to combine the tags, if necessary.
                                 */
                                Map<String, Object> contextData = new HashMap<String, Object>();
                                contextData.put(AnalyticsConstants.ACTION_KEY_LOCATE_PHILIPS_SEARCH_TERM, constrain);
                                contextData.put(AnalyticsConstants.ACTION_KEY_LOCATE_PHILIPS_SEARCH_RESULTS,
                                        String.valueOf(count));
                                AnalyticsTracker
                                        .trackAction(
                                                AnalyticsConstants.ACTION_SEND_DATA,
                                                contextData);
                                if (count == 0) {
                                    showAlert(getResources().getString(
                                            R.string.no_data_available));
                                }

                                mListView.setAdapter(adapter);
                                mListView.setVisibility(View.VISIBLE);
                                mLinearLayout.setVisibility(View.GONE);
                                if (mSearchIcon.getVisibility() == View.VISIBLE)
                                    mMarkerIcon.setVisibility(View.VISIBLE);

                                if (mArabicSearchIcon.getVisibility() == View.VISIBLE)
                                    mArabicMarkerIcon.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                showAlert(getResources().getString(
                        R.string.no_data_available));
            }
        } else if (v.getId() == R.id.getdirection) {
            AnalyticsTracker
                    .trackAction(
                            AnalyticsConstants.ACTION_SEND_DATA,
                            AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL,
                            AnalyticsConstants.ACTION_VALUE_LOCATE_PHILIPS_SEND_GET_DIRECTIONS);

            if (isConnectionAvailable()) {
                // check sourcelat and sourcelng
                if (mSourceLat == 0 && mSourceLng == 0) {

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                        int hasPermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    } else {
                        gpsAlertView.showAlert(this, -1, R.string.gps_disabled,
                                android.R.string.yes, android.R.string.no);
                    }
                } else {
                    gpsAlertView.removeAlert();
                    trackToMe(new LatLng(mSourceLat, mSourceLng), new LatLng(
                            mDestinationLat, mDestinationLng));
                }

                mLinearLayout.setVisibility(View.GONE);
            }

        } else if ((v.getId() == R.id.marker_icon) || (v.getId() == R.id.arabic_marker_icon)) {
            mListView.setVisibility(View.GONE);
            if (mSearchIcon.getVisibility() == View.VISIBLE)
                mMarkerIcon.setVisibility(View.GONE);

            if (mArabicSearchIcon.getVisibility() == View.VISIBLE)
                mArabicMarkerIcon.setVisibility(View.GONE);

            mSearchBox.setText(null);
        } else if (v.getId() == R.id.call) {
            mLinearLayout.setVisibility(View.GONE);
            AnalyticsTracker
                    .trackAction(
                            AnalyticsConstants.ACTION_SEND_DATA,
                            AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL,
                            AnalyticsConstants.ACTION_VALUE_LOCATE_PHILIPS_CALL_LOCATION);
            if (mAtosResponse != null && mPhoneNumber != null && !mAtosResponse.getSuccess()) {
                DigiCareLogger.e(TAG, mAtosResponse.getCdlsErrorModel()
                        .getErrorMessage());
            } else if (mUtils.isSimAvailable(getActivity())) {
                callPhilips();
            } else if (!mUtils.isSimAvailable(getActivity())) {
                DigiCareLogger.v(TAG, "Check the SIM");
                showAlert(getActivity().getString(R.string.check_sim));
            }
        }
    }

    private void callPhilips() {
        Intent myintent = new Intent(Intent.ACTION_DIAL);
        myintent.setData(Uri.parse("tel:" + mPhoneNumber));
        myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        DigiCareLogger.i(TAG, "Contact Number : " + mPhoneNumber);
        startActivity(myintent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        mLocationDetailScroll.fullScroll(ScrollView.FOCUS_UP);
        AtosResultsModel resultModel = (AtosResultsModel) adapter
                .getItem(position);
        showServiceCentreDetails(resultModel);
    }

    private void showServiceCentreDetails(AtosResultsModel resultModel) {

        String addressForTag = resultModel.getAddressModel().getAddress1();
        if (addressForTag.isEmpty() || addressForTag == null) {
            addressForTag = resultModel.getAddressModel().getAddress2();
        }
        addressForTag = addressForTag.replaceAll(",", " ");

        AnalyticsTracker.trackAction(
                AnalyticsConstants.ACTION_SEND_DATA,
                AnalyticsConstants.ACTION_KEY_LOCATE_PHILIPS_LOCATION_VIEW,
                resultModel.getAddressModel().getPhone() + '|'
                        + addressForTag);

        AtosLocationModel mGeoData = null;
        AtosAddressModel mAddressModel = null;
        try {
            mAddressModel = resultModel.getAddressModel();
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

        if (mPhoneNumber != null && mPhoneNumber != "")
            mButtonCall.setText(getResources().getString(R.string.call_number) + " "
                    + mPhoneNumber);
        else
            mButtonCall.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.GONE);
        mLinearLayout.setVisibility(View.VISIBLE);
        if (mSearchIcon.getVisibility() == View.VISIBLE)
            mMarkerIcon.setVisibility(View.GONE);

        if (mArabicSearchIcon.getVisibility() == View.VISIBLE)
            mArabicMarkerIcon.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);

        // checking gps enabled or disbled
        final LocationManager manager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (!getActivity().isFinishing())
                gpsAlertView.showAlert(this, -1, R.string.gps_disabled,
                        android.R.string.yes, android.R.string.no);
        } else {
            gpsAlertView.removeAlert();
        }
        setSearchIcon();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void setSearchIcon() {

        if ((mSearchBox != null) && (mArabicSearchIcon != null) && (mSearchBox != null)) {


            if (getActivity().getResources().getConfiguration().locale.getLanguage().toString().contains("ar")) {
                mSearchIcon.setVisibility(View.GONE);
                mArabicSearchIcon.setVisibility(View.VISIBLE);
                mSearchBox.setGravity(Gravity.RIGHT);
            } else {
                mSearchIcon.setVisibility(View.VISIBLE);
                mArabicSearchIcon.setVisibility(View.GONE);
                mSearchBox.setGravity(Gravity.LEFT);
            }

            hideKeyboard();
        }
    }

    @Override
    public void onMapReady() {
        mMap = mMapFragment.getMap();
        if (mMap != null) {
            initView();
        }
        DigiCareLogger.v(TAG, "onMAP Ready Callback : " + mMap);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        AtosResultsModel resultModel = mHashMapResults.get(marker.getId());
        try {
            resultModel.getAddressModel().toString();
            showServiceCentreDetails(resultModel);
        } catch (NullPointerException exception) {
            DigiCareLogger
                    .d(TAG, "We don't show direction to current location");
        }

        return true;
    }

    @Override
    public void onMapClick(LatLng arg0) {

        if (mListView.getVisibility() == View.VISIBLE)
            mListView.setVisibility(View.GONE);

        if (mSearchIcon.getVisibility() == View.VISIBLE)
            mMarkerIcon.setVisibility(View.GONE);

        if (mArabicSearchIcon.getVisibility() == View.VISIBLE)
            mArabicMarkerIcon.setVisibility(View.GONE);

        if (mLinearLayout.getVisibility() == View.VISIBLE)
            mLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_FIND_PHILIPS_NEAR_YOU;
    }

    private void setButtonParams(float density) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));

        params.topMargin = (int) getActivity().getResources().getDimension(R.dimen.marginTopButton);

        mButtonCall.setLayoutParams(params);
        mButtonDirection.setLayoutParams(params);
    }
}