/*
 * Copyright (c) 2016 Philips. All rights reserved.
 *
 */


package com.philips.cdp.digitalcare.locatephilips.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import com.philips.cdp.digitalcare.contactus.fragments.ContactUsFragment;
import com.philips.cdp.digitalcare.customview.GpsAlertView;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.locatephilips.CustomGeoAdapter;
import com.philips.cdp.digitalcare.locatephilips.GoToContactUsListener;
import com.philips.cdp.digitalcare.locatephilips.LocateNearCustomDialog;
import com.philips.cdp.digitalcare.locatephilips.MapDirections;
import com.philips.cdp.digitalcare.locatephilips.MapDirections.MapDirectionResponse;
import com.philips.cdp.digitalcare.locatephilips.models.AtosAddressModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosLocationModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosResponseModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosResultsModel;
import com.philips.cdp.digitalcare.locatephilips.parser.AtosParsingCallback;
import com.philips.cdp.digitalcare.locatephilips.parser.AtosResponseParser;
import com.philips.cdp.digitalcare.request.RequestData;
import com.philips.cdp.digitalcare.request.ResponseCallback;
import com.philips.cdp.digitalcare.util.CustomSearchView;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.cdp.digitalcare.util.Utils;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.shamanland.fonticon.FontIconDrawable;
import com.shamanland.fonticon.FontIconTypefaceHolder;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This Class responsible for showing the locale Specfic customer support/service center location
 * with the location tracker MAP usin the GoogleMap Feature from the GooglePlayServices library.
 */
@SuppressLint({"SetJavaScriptEnabled", "DefaultLocale"})
public class LocatePhilipsFragment extends DigitalCareBaseFragment implements
        OnItemClickListener, OnMarkerClickListener,
        ResponseCallback, OnMapClickListener, OnMapReadyCallback,TextWatcher,CustomSearchView.OnHideListView {
    private static final String TAG = LocatePhilipsFragment.class
            .getSimpleName();
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static View mView = null;
    private static HashMap<String, AtosResultsModel> mHashMapResults = null;
    protected SharedPreferences mSharedpreferences = null;
    private GoogleMap mMap = null;
    private Marker mCurrentPosition = null;
    private AtosResponseModel mAtosResponse = null;
    private ArrayList<LatLng> traceOfMe = null;
    private Bitmap mBitmapMarker = null;
    private Polyline mPolyline = null;
    private double mSourceLat = 0;
    private double mSourceLng = 0;
    private double mDestinationLat = 0;
    private double mDestinationLng = 0;
    private String mPhoneNumber = null;
    private LocationManager mLocationManager = null;
    private GpsAlertView gpsAlertView = null;
    private String provider = null;
    private LinearLayout mLinearLayout = null;
    private ListView mListView = null;
    private TextView mShowTxtAddress = null;
    private TextView mShowTxtTitle = null;
    private ScrollView mLocationDetailScroll = null;
    private static ArrayList<AtosResultsModel> mResultModelSet = null;
    private CustomSearchView mSearchBox = null;
    private ImageView mArabicSearchIcon = null;
    private TextView mArabicMarkerIcon = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private Button mButtonCall = null;
    private Button mButtonDirection = null;
    private CustomGeoAdapter mAdapter = null;
    private ProgressDialog mDialog = null;
    private ProgressBar mLocateNearProgressBar = null;
    private boolean isContactUsScreenLaunched = false;
    private Utils mUtils = null;
    private static ArrayList<AtosResultsModel> mResultModelsetDataHold;

    private LocationListener locationListener = new LocationListener() {

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
                    break;
            }
        }
    };
    private GoToContactUsListener fragmentNavigationListener = new GoToContactUsListener() {
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
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.i(TAG, "Launching the Find Philips near you Screen");

        try {
            getActivity().getWindow().setFlags(16777216, 16777216);
        } catch (Exception e) {
        }
        if (isConnectionAvailable())
            requestATOSResponseData();

        try {
            mView = inflater.inflate(R.layout.consumercare_fragment_locate_philips, container, false);
        } catch (InflateException e) {
        }
        mSearchBox = mView.findViewById(R.id.search_box);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionBarMenuIcon = getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = getActivity().findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);
        checkGooglePlayServices();
        initGoogleMapv2();
        createBitmap();
        mUtils = new Utils();
        try {

            DigitalCareConfigManager.getInstance().getTaggingInterface().trackPageWithInfo
                    (AnalyticsConstants.PAGE_FIND_PHILIPS_NEAR_YOU,
                            getPreviousName(), getPreviousName());
        } catch (Exception e) {

        }
        gpsAlertView = GpsAlertView.getInstance();
    }

    private String formAtosURL() {
        ConsumerProductInfo consumerProductInfo = DigitalCareConfigManager
                .getInstance().getConsumerProductInfo();
        if (isConsProdInfoAvailable(consumerProductInfo)) return null;
        String atosUrl = getAtosUrl();
        DigiCareLogger.d(TAG, "ATOS URL : " + atosUrl);
        return atosUrl;


    }

    private boolean isConsProdInfoAvailable(ConsumerProductInfo consumerProductInfo) {
        if (consumerProductInfo == null) {
            getActivity().finish();
            return true;
        }
        return false;
    }

    protected String getAtosUrl() {

        HashMap<String, String> hm = new HashMap<>();
        hm.put(DigitalCareConstants.KEY_PRODUCT_SUBCATEGORY, DigitalCareConfigManager.getInstance().getConsumerProductInfo().getSubCategory());
        hm.put(DigitalCareConstants.KEY_LATITUDE, "" + mSourceLat);
        hm.put(DigitalCareConstants.KEY_LONGITUDE, "" + mSourceLng);

        DigitalCareConfigManager.getInstance().getAPPInfraInstance().getServiceDiscovery().getServiceUrlWithCountryPreference(DigitalCareConstants.SERVICE_ID_CC_ATOS, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                DigiCareLogger.v(TAG, "Response from Service Discovery : Service ID : 'cc.atos' - " + url);
                DigitalCareConfigManager.getInstance().setAtosUrl(url.toString());
            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                DigiCareLogger.v(TAG, "Error Response from Service Discovery :" + s);
                DigitalCareConfigManager.getInstance().getTaggingInterface().trackActionWithInfo(AnalyticsConstants.ACTION_SET_ERROR, AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR, s);
            }
        }, hm);

        return DigitalCareConfigManager.getInstance().getAtosUrl();
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

    private Handler handler = new Handler();
    @Override
    public void onResponseReceived(String response) {
        DigiCareLogger.i(TAG, "ATOS Response : " + response);
        closeProgressDialog();

        if (response != null && isAdded()) {
            AtosResponseParser atosResponseParser = new AtosResponseParser(
                    mParsingCompletedCallback);
            atosResponseParser.parseAtosResponse(response);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mMap!=null){
                        initView();
                    }
                }
            },0);
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
            }else{
                mResultModelsetDataHold = resultModelSet;
                mAdapter = new CustomGeoAdapter(getActivity(), mResultModelsetDataHold);
            }
        } else {
            showCustomAlert();
        }
    }

    private void showCustomAlert() {
        LocateNearCustomDialog locateNearCustomDialog = new LocateNearCustomDialog(getActivity(),
                getActivity().getSupportFragmentManager(), fragmentNavigationListener);
        if (!isDialogShown) {
            locateNearCustomDialog.show();
            isDialogShown = true;
        }
    }

    @SuppressLint("NewApi")
    private void initGoogleMapv2() {

        try {
            DigiCareLogger.v(TAG, "Initializing Google Map");
            ((SupportMapFragment) this.getFragmentManager()
                    .findFragmentById(R.id.map)).getMapAsync(this);
        } catch (NullPointerException e) {
            DigiCareLogger
                    .v(TAG,
                            "Failed to get GoogleMap so so enabling Google v2 Map Compatibility " +
                                    "Enabled");
            GoogleMapFragment mMapFragment = GoogleMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mMapFragment).commit();
            mMapFragment.getMapAsync(this);
        }

    }

    /*
     Android Marshmallow: Android M : Permission has to be requested at runtime.
     */
    private void requestPermissionAndroidM() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            int hasPermission = getActivity().checkSelfPermission(Manifest.permission.
                    ACCESS_COARSE_LOCATION);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
            } else {
                getCurrentLocation();
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

        if(Utils.isEulaAccepted(getActivity())){
            requestPermissionAndroidM();
        }
        mLinearLayout = getActivity().findViewById(
                R.id.showlayout);
        mListView = getActivity().findViewById(R.id.placelistview);
        mShowTxtTitle = getActivity().findViewById(
                R.id.show_place_title);
        mShowTxtAddress = getActivity().findViewById(
                R.id.show_place_address);
        mSearchBox.setOnClickListener(this);
        mSearchBox.addTextChangedListener(this);
        mSearchBox.setOnHideListListener(this);
        mArabicSearchIcon = getActivity().findViewById(R.id.arabic_search_icon);
        TextView mMarkerIcon = getActivity().findViewById(R.id.marker_icon);
        TextView mCloseIcon  = getActivity().findViewById(R.id.close_icon);
        mArabicMarkerIcon = getActivity().findViewById(R.id.arabic_marker_icon);
        mLocationDetailScroll = getActivity().findViewById(
                R.id.locationDetailScroll);
        mButtonCall = getActivity().findViewById(R.id.call);
        mButtonDirection = getActivity().findViewById(
                R.id.getdirection);
        mButtonCall.setOnClickListener(this);
        mArabicSearchIcon.setOnClickListener(this);
        mMarkerIcon.setOnClickListener(this);
        mCloseIcon.setOnClickListener(this);
        mArabicMarkerIcon.setOnClickListener(this);
        mListView.setTextFilterEnabled(true);
        mListView.setOnItemClickListener(this);
        mButtonDirection.setOnClickListener(this);
        mMap.setOnMapClickListener(this);
        mListView.setVisibility(View.GONE);
        mLinearLayout.setVisibility(View.GONE);
        mMarkerIcon.setVisibility(View.GONE);
        mArabicMarkerIcon.setVisibility(View.GONE);
        mLocateNearProgressBar = getActivity().findViewById(
                R.id.locate_near_progress);
        mLocateNearProgressBar.setVisibility(View.GONE);
        Configuration config = getResources().getConfiguration();
        setViewParams(config);
        float density = getResources().getDisplayMetrics().density;
        setButtonParams(density);
        if(mResultModelsetDataHold!=null){
            addMarkers(mResultModelsetDataHold);
        }
    }

    private void addMarkers(final ArrayList<AtosResultsModel> resultModelSet) {
        int resultsetSize = resultModelSet.size();
        mHashMapResults = new HashMap<>(resultsetSize);
        if (mMap != null) {
            mMap.setOnMarkerClickListener(this);

            for (int i = 0; i < resultsetSize; i++) {
                AtosResultsModel resultModel = resultModelSet.get(i);
                AtosLocationModel locationModel = resultModel.getLocationModel();
                AtosAddressModel addressModel = resultModel.getAddressModel();
                addressModel.setCurrentLat(mSourceLat);
                addressModel.setCurrentLng(mSourceLng);
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

        FontIconDrawable icon = new FontIconDrawable();
        FontIconTypefaceHolder.init(getActivity().getAssets(),"fonts/iconfont.ttf");
        TypedArray typedArray = getActivity().getTheme().obtainStyledAttributes(new int[]{R.attr.uidButtonPrimaryFocusBackgroundColor});
        int color = typedArray.getColor(0, Color.BLACK);
        typedArray.recycle();
        icon.setTextColor(color);
        int markerIconSize = getActivity().getResources().getDimensionPixelSize(R.dimen.locate_philips_marker_size);
        icon.setTextSize(markerIconSize);
        icon.setText(getActivity().getString(R.string.dls_location));

        mBitmapMarker = drawableToBitmap(icon);

    }


    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);


        return bitmap;
    }

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (isProviderAvailable() && (provider != null)) {
                        getCurrentLocation();
                    }
                } else {
                    DigiCareLogger.e(TAG, "LocateNearYou -> permissions not granted" +
                            permissions.toString());
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void locateCurrentPosition() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(provider);
        updateWithNewLocation(location);
        long minTime = 5000;// ms
        float minDist = 5.0f;// meter
        mLocationManager.requestLocationUpdates(provider, minTime, minDist,locationListener);
    }

    private void setMapType(double lat, double lng) {
        if (mMap != null) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            addBoundaryToCurrentPosition(lat, lng);
        } else {
            DigiCareLogger.d(TAG,"MAP is null Failed to update GoogleMap.MAP_TYPE_NORMAL Maptype");
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
            MapDirectionResponse mGetDirectionResponse = new MapDirectionResponse() {
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
        String where;
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
            DigiCareLogger.v(TAG, where);
        }
    }

    private void checkGooglePlayServices() {

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getActivity());
        switch (result) {
            case ConnectionResult.SUCCESS:
                DigiCareLogger.d(TAG, "SUCCESS");
                break;

            case ConnectionResult.SERVICE_INVALID:
                DigiCareLogger.d(TAG, "SERVICE_INVALID");
                googleAPI.getErrorDialog(getActivity(),
                        ConnectionResult.SERVICE_INVALID, 0).show();
                break;

            case ConnectionResult.SERVICE_MISSING:
                DigiCareLogger.d(TAG, "SERVICE_MISSING");
                googleAPI.getErrorDialog(
                        getActivity(), ConnectionResult.SERVICE_MISSING, 0).show();
                break;

            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                DigiCareLogger.d(TAG, "SERVICE_VERSION_UPDATE_REQUIRED");
                googleAPI.getErrorDialog(
                        getActivity(), ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,
                        0).show();
                break;

            case ConnectionResult.SERVICE_DISABLED:
                DigiCareLogger.d(TAG, "SERVICE_DISABLED");
                googleAPI.getErrorDialog(
                        getActivity(), ConnectionResult.SERVICE_DISABLED, 0).show();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSearchBox != null)
            mSearchBox.setText(null);
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
        locationListener = null;

        if (mHashMapResults != null && mHashMapResults.size() <= 0) {
            mHashMapResults.clear();
            mHashMapResults = null;
        }
        mLocationManager = null;
    }

    @Override
    public void onDestroyView() {
        closeProgressDialog();
        super.onDestroyView();
    }

    @Override
    public void setViewParams(Configuration config) {
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
        if (v.getId() == R.id.call) {
            mLinearLayout.setVisibility(View.GONE);
            DigitalCareConfigManager.getInstance().getTaggingInterface().trackActionWithInfo
                    (AnalyticsConstants.ACTION_SEND_DATA,
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
        }else if ((v.getId() == R.id.marker_icon) || (v.getId() == R.id.arabic_marker_icon)) {
            mListView.setVisibility(View.GONE);
            removeListData();
            removeArabicSearchIcon();
            mSearchBox.setText(null);
        }else if (v.getId() == R.id.getdirection) {
            DigitalCareConfigManager.getInstance().getTaggingInterface().trackActionWithInfo
                    (AnalyticsConstants.ACTION_SEND_DATA,
                            AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL,
                            AnalyticsConstants.ACTION_VALUE_LOCATE_PHILIPS_SEND_GET_DIRECTIONS);

            if (isConnectionAvailable()) {
                if (mSourceLat == 0 && mSourceLng == 0) {

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                        int hasPermission = getActivity().checkSelfPermission(Manifest.permission.
                                ACCESS_COARSE_LOCATION);
                        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.
                                            ACCESS_COARSE_LOCATION},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    } else {
                        gpsAlertView.showAlert(this, -1,
                                R.string.gps_disabled,
                                android.R.string.yes, android.R.string.no);
                    }
                } else {
                    gpsAlertView.removeAlert();
                    trackToMe(new LatLng(mSourceLat, mSourceLng), new LatLng(
                            mDestinationLat, mDestinationLng));
                }

                mLinearLayout.setVisibility(View.GONE);
            }

        }else if((v.getId() == R.id.close_icon)){
            hideListData();
        }
    }

    private void showListView() {

        final String constrain = mSearchBox.getText().toString().trim();

        if (mResultModelSet != null && mAdapter.getFilter()!=null) {
            mAdapter.getFilter().filter(constrain,
                    new Filter.FilterListener() {
                        public void onFilterComplete(int count) {
                            Map<String, String> contextData = new HashMap<>();
                            contextData.put(AnalyticsConstants.
                                    ACTION_KEY_LOCATE_PHILIPS_SEARCH_TERM, constrain);
                            contextData.put(AnalyticsConstants.
                                            ACTION_KEY_LOCATE_PHILIPS_SEARCH_RESULTS,
                                    String.valueOf(count));
                            DigitalCareConfigManager.getInstance().getTaggingInterface().
                                    trackActionWithInfo(AnalyticsConstants.ACTION_SEND_DATA,
                                            contextData);
                            mListView.setAdapter(mAdapter);
                            mListView.setVisibility(View.VISIBLE);
                            mLinearLayout.setVisibility(View.GONE);
                            if (mArabicSearchIcon.getVisibility() == View.VISIBLE)
                                mArabicMarkerIcon.setVisibility(View.VISIBLE);
                        }
                    });
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
        AtosResultsModel resultModel = (AtosResultsModel) mAdapter
                .getItem(position);
        showServiceCentreDetails(resultModel);
    }

    private void showServiceCentreDetails(AtosResultsModel resultModel) {

        String addressForTag = resultModel.getAddressModel().getAddress1();
        if (addressForTag.isEmpty()) {
            addressForTag = resultModel.getAddressModel().getAddress2();
        }
        addressForTag = addressForTag.replaceAll(",", " ");

        DigitalCareConfigManager.getInstance().getTaggingInterface().trackActionWithInfo
                (AnalyticsConstants.ACTION_SEND_DATA,
                        AnalyticsConstants.ACTION_KEY_LOCATE_PHILIPS_LOCATION_VIEW,
                        resultModel.getAddressModel().getPhone() + '|'
                                + addressForTag);

        AtosLocationModel mGeoData;
        AtosAddressModel atosAddressModel;

        try {
            atosAddressModel = resultModel.getAddressModel();
            mGeoData = resultModel.getLocationModel();
        } catch (NullPointerException e) {
            DigiCareLogger.d(TAG, " " + e);
            return;
        }

        mDestinationLat = Double.parseDouble(mGeoData.getLatitude());
        mDestinationLng = Double.parseDouble(mGeoData.getLongitude());
        mShowTxtTitle.setText(resultModel.getTitle());
        mShowTxtAddress.setText(atosAddressModel.getAddress1() + "\n"
                + atosAddressModel.getCityState() + "\n" + atosAddressModel.getUrl());
        ArrayList<String> phoneNumbers = atosAddressModel.getPhoneList();
        mPhoneNumber = phoneNumbers.get(0);
        if (mPhoneNumber != null && !mPhoneNumber.equals(""))
            mButtonCall.setText(getResources().getString(R.string.call_number) + " "
                    + mPhoneNumber);
        else
            mButtonCall.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.GONE);
        mLinearLayout.setVisibility(View.VISIBLE);
        removeArabicSearchIcon();
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
            getCurrentLocation();
            gpsAlertView.removeAlert();
        }
        setSearchIcon();
        if(mResultModelsetDataHold!=null)
            addMarkers(mResultModelsetDataHold);
        mSearchBox.setGravity(Gravity.CENTER_VERTICAL);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void setSearchIcon() {

        if ((mSearchBox != null) && (mArabicSearchIcon != null)) {

            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = getActivity().getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = getActivity().getResources().getConfiguration().locale;
            }

            if (locale.
                    getLanguage().contains("ar")) {
                //mSearchIcon.setVisibility(View.GONE);
                mArabicSearchIcon.setVisibility(View.VISIBLE);
                mSearchBox.setGravity(Gravity.RIGHT);
            } else {
                //mSearchIcon.setVisibility(View.VISIBLE);
                mArabicSearchIcon.setVisibility(View.GONE);
                mSearchBox.setGravity(Gravity.LEFT);
            }
            hideKeyboard();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        hideListData();
    }

    public void hideListData() {
        removeArabicSearchIcon();
        removeListLayout();
        removeListData();
    }

    @Override
    public void hideListView() {
        hideListData();
    }

    private void removeListLayout() {
        if (mLinearLayout.getVisibility() == View.VISIBLE)
            mLinearLayout.setVisibility(View.GONE);
    }

    private void removeArabicSearchIcon() {
        if (mArabicSearchIcon.getVisibility() == View.VISIBLE)
            mArabicMarkerIcon.setVisibility(View.GONE);
    }

    private void removeListData() {
        if (mListView.getVisibility() == View.VISIBLE)
            mListView.setVisibility(View.GONE);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.toString().length() == 0){
            hideListData();
            return;
        }
        showListView();

    }
}