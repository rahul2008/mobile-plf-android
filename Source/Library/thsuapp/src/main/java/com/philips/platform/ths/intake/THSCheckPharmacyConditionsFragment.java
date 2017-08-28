package com.philips.platform.ths.intake;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.pharmacy.THSPharmacyListFragment;
import com.philips.platform.ths.pharmacy.THSSearchPharmacyFragment;
import com.philips.platform.ths.utility.THSManager;

public class THSCheckPharmacyConditionsFragment extends THSBaseFragment {

    private int REQUEST_LOCATION = 1001;
    private LocationManager mLocationManager = null;
    private String provider = null;
    protected Location updatedLocation = null;

    public void displaySearchPharmacy() {
        if (checkGooglePlayServices()) {
            checkPermission();
        }
    }

    /**
     * Ask users permission to fetch location
     */
    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            getLocation();
        }
    }

    /**
     * This methos checks is the proivider is enabled to fetch the location
     */
    public void getLocation() {
        if (isProviderAvailable() && (provider != null)) {
            fetchCurrentLocation();
        }
    }

    /**
     * Calls the location fetch method of the location manager
     */
    private void fetchCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(provider);
        long minTime = 5000;// ms
        float minDist = 5.0f;// meter
        mLocationManager.requestLocationUpdates(provider, minTime, minDist, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.v("Location", "Lon : " + location.getLongitude() + "::::Lat : " + location.getLatitude());
            updatedLocation = location;
            mLocationManager = null;
            callPharmacyListFragment(location);


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    /**
     * checks is provider is available
     *
     * @return
     */
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
            return true;
        }

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            return true;
        }

        return provider != null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                showPharmacySearch();
                showToast("Permission denied : Going to search");
            }
        }
    }


    /**
     * Location services code below. Checking if google play services is installed/latest/supported version?
     *
     * @return
     */
    private boolean checkGooglePlayServices() {
        int result = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        switch (result) {
            case ConnectionResult.SUCCESS:
                return true;

            case ConnectionResult.SERVICE_INVALID:
                GooglePlayServicesUtil.getErrorDialog(
                        ConnectionResult.SERVICE_INVALID, getActivity(), 0).show();
                break;

            case ConnectionResult.SERVICE_MISSING:
                GooglePlayServicesUtil.getErrorDialog(
                        ConnectionResult.SERVICE_MISSING, getActivity(), 0).show();
                break;

            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                GooglePlayServicesUtil.getErrorDialog(
                        ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,
                        getActivity(), 0).show();
                break;

            case ConnectionResult.SERVICE_DISABLED:
                GooglePlayServicesUtil.getErrorDialog(
                        ConnectionResult.SERVICE_DISABLED, getActivity(), 0).show();
                break;
        }
        return false;
    }

    private void callPharmacyListFragment(Location location) {

        THSPharmacyListFragment thsPharmacyListFragment = new THSPharmacyListFragment();
        thsPharmacyListFragment.setConsumerAndAddress(THSManager.getInstance().getPTHConsumer(), null);
        thsPharmacyListFragment.setLocation(location);
        thsPharmacyListFragment.setFragmentLauncher(getFragmentLauncher());
        addFragment(thsPharmacyListFragment, THSPharmacyListFragment.TAG, null);
    }

    private void showPharmacySearch() {

        THSSearchPharmacyFragment thsSearchPharmacyFragment = new THSSearchPharmacyFragment();
        thsSearchPharmacyFragment.setFragmentLauncher(getFragmentLauncher());
        addFragment(thsSearchPharmacyFragment, THSSearchPharmacyFragment.TAG, null);
    }

}
