package com.philips.cdp.digitalcare.locatephilips.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;

import com.google.android.gms.maps.SupportMapFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

/**
 * This class is the base class for the "Locate Philips Near Your" feature.
 * This class consumes the model class & sub classes comes  under "locatephilips" package class.
 *
 * @author naveen@philips.com
 * @since 22/May/2015
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */
@SuppressLint("NewApi")
public class GoogleMapFragment extends SupportMapFragment {

    private final String TAG = GoogleMapFragment.class.getSimpleName();

    public static GoogleMapFragment newInstance() {
        GoogleMapFragment mMapFragment = new GoogleMapFragment();
        return mMapFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup arg1,
                             Bundle arg2) {
        View mView = super.onCreateView(inflater, arg1, arg2);
        Fragment mFragment = getParentFragment();
        if (mFragment != null && mFragment instanceof onMapReadyListener) {
            ((onMapReadyListener) mFragment).onMapReady();
        }
        return mView;

    }

    public interface onMapReadyListener {
        void onMapReady();
    }
}
