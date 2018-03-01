package com.philips.platform.ths.intake;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.cost.THSCostSummaryFragment;
import com.philips.platform.ths.insurance.THSInsuranceConfirmationFragment;
import com.philips.platform.ths.pharmacy.THSPharmacyAndShippingFragment;
import com.philips.platform.ths.pharmacy.THSPharmacyListFragment;
import com.philips.platform.ths.pharmacy.THSSearchPharmacyFragment;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;
import static com.philips.platform.ths.providerslist.THSProvidersListFragment.DIALOG_TAG;

public class THSCheckPharmacyConditionsFragment extends THSBaseFragment implements THSCheckPharmacyConditonsView, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static String TAG = THSCheckPharmacyConditionsFragment.class.getSimpleName();

    private int REQUEST_LOCATION = 1001;
    private THSCheckPharmacyConditionsPresenter thscheckPharmacyConditionsPresenter;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final long TIMEOUT = 1000 * 15;
    static final long serialVersionUID = 46L;
    private Handler timeoutHandler;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    Intent gpsSettingsIntent;
    private AlertDialogFragment alertDialogFragment;
    private RelativeLayout relativeLayout;
    private boolean isPharmacyCheckRequired = false;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setMaxWaitTime(TIMEOUT);
    }

    public void setPharmacyCheckRequired(boolean isPharmacyCheckRequired){
        this.isPharmacyCheckRequired = isPharmacyCheckRequired;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_conditions_fragment_layout,container,false);
        relativeLayout = view.findViewById(R.id.ths_conditions_fragment_container);
        createCustomProgressBar(relativeLayout,BIG);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            getActivity().finish();
        }
        timeoutHandler =  new Handler();
        createLocationRequest();
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        thscheckPharmacyConditionsPresenter = new THSCheckPharmacyConditionsPresenter(this);
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int status = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            apiAvailability.getErrorDialog(getActivity(), status, 0).show();
            return false;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        AmwellLog.d(TAG, "onStart fired ..............");
        getLocationUpdate();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        AmwellLog.d(TAG, "onStop fired ..............");
        disconnectGoogleApiClient();
        AmwellLog.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
        super.onStop();
    }

    public void disconnectGoogleApiClient() {
        hideProgressBar();
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                stopLocationUpdates();
                mGoogleApiClient.disconnect();
            }
        }
    }


    protected void stopLocationUpdates() {
        hideProgressBar();
        FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        AmwellLog.d(TAG, "Location update stopped .......................");
    }

    private void checkIfPharmacyRequired() {
        if(isPharmacyCheckRequired) {
            boolean isPharmacyRequired = THSManager.getInstance().getPthVisitContext().isCanPrescribe();
            if (isPharmacyRequired) {
                thscheckPharmacyConditionsPresenter.fetchConsumerPreferredPharmacy();
            } else {  // go to insurance or cost detail
                Consumer consumer = THSManager.getInstance().getPTHConsumer(getContext()).getConsumer();
                getActivity().getSupportFragmentManager().popBackStack();
                if (consumer.getSubscription() != null && consumer.getSubscription().getHealthPlan() != null) {
                    final THSCostSummaryFragment fragment = new THSCostSummaryFragment();
                    addFragment(fragment, THSCostSummaryFragment.TAG, null, true);
                } else {
                    final THSInsuranceConfirmationFragment fragment = new THSInsuranceConfirmationFragment();
                    addFragment(fragment, THSInsuranceConfirmationFragment.TAG, null, true);
                }
            }
        }else {
            displayPharmacy();
        }
    }

    public void displayPharmacy() {
        checkPermission();
    }

    /**
     * Ask users permission to fetch location
     */
    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            startLocationUpdates();
        }
    }

    private void callPharmacyListFragment(Location location) {
        hideProgressBar();
        if (isFragmentAttached()) {
            getActivity().getSupportFragmentManager().popBackStack();
            THSPharmacyListFragment thsPharmacyListFragment = new THSPharmacyListFragment();
            thsPharmacyListFragment.setConsumerAndAddress(THSManager.getInstance().getPTHConsumer(getContext()), null);
            thsPharmacyListFragment.setLocation(location);
            addFragment(thsPharmacyListFragment, THSPharmacyListFragment.TAG, null, true);
        }
    }

    /**
     *  new thread added here to handle the crash "java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState"
     *  The crash was only in android 6 (Marshmallow) devices. Only way to handle the crash was to spawn a new thread and then post the runnable
     *  on UI thread.
     */
    private void showPharmacySearch() {
        hideProgressBar();
        if (isFragmentAttached()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().getSupportFragmentManager().popBackStack();
                            THSSearchPharmacyFragment thsSearchPharmacyFragment = new THSSearchPharmacyFragment();
                            addFragment(thsSearchPharmacyFragment, THSSearchPharmacyFragment.TAG, null, true);
                        }
                    });
                }
            }).start();

        }
    }

    public void displayPharmacyAndShippingPreferenceFragment(Pharmacy pharmacy, Address address) {
        hideProgressBar();
        if (isFragmentAttached()) {
            getActivity().getSupportFragmentManager().popBackStack();
            THSPharmacyAndShippingFragment thsPharmacyAndShippingFragment = new THSPharmacyAndShippingFragment();
            thsPharmacyAndShippingFragment.setPharmacyAndAddress(address, pharmacy);
            addFragment(thsPharmacyAndShippingFragment, THSPharmacyAndShippingFragment.TAG, null, true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        timeoutHandler.removeCallbacks(timeoutHandlerRunnable);
        FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        AmwellLog.d(TAG, "Firing onLocationChanged...... ::: Lat: " + location.getLatitude() + "Log:::: " + location.getLongitude());
        mCurrentLocation = location;
        callPharmacyListFragment(location);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        AmwellLog.d(TAG, "onConnected - isConnected ........: " + mGoogleApiClient.isConnected());
        checkIfPharmacyRequired();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (checkIfGPSProviderAvailable()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            timeoutHandler.postDelayed(timeoutHandlerRunnable , TIMEOUT);

        } else {
            showNoGpsDialog();
        }


        // mGoogleApiClient, mLocationRequest, this);
        AmwellLog.d(TAG, "Location update started ..............: ");
    }

    private void showNoGpsDialog() {

        alertDialogFragment = new AlertDialogFragment.Builder(UIDHelper.getPopupThemedContext(getContext())).setDialogType(DialogConstants.TYPE_ALERT).setTitle(R.string.ths_gps_not_enabled_message_title)
                .setMessage(R.string.ths_gps_not_enabled_message).
                        setPositiveButton(R.string.ths_ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialogFragment.dismiss();
                                launchLocationSettings();
                            }
                        }).setNegativeButton(R.string.ths_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialogFragment.dismiss();
                                showPharmacySearch();
                            }
                        }).setCancelable(false).create();
        alertDialogFragment.show(getActivity().getSupportFragmentManager(), DIALOG_TAG);
    }

    private void launchLocationSettings() {
        gpsSettingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsSettingsIntent);
    }

    private boolean checkIfGPSProviderAvailable() {

        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(
                Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        AmwellLog.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationUpdate();
            } else {
                showPharmacySearch();
                showError(getString(R.string.ths_permission_denied_message));
            }
        }
    }

    private void getLocationUpdate() {
        if (mGoogleApiClient.isConnected()) {
            checkIfPharmacyRequired();
            AmwellLog.d(TAG, "Location update resumed .....................");
        }
    }

    protected final Runnable timeoutHandlerRunnable = new Runnable() {
        @Override
        public void run() {
            showPharmacySearch();
        }
    };


}
