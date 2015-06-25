package com.philips.cl.di.digitalcare.locatephilips;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.philips.cl.di.digitalcare.util.DigiCareLogger;

/**
 * 
 * @author naveen@philips.com
 * @since 22/May/2015
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
		DigiCareLogger.d(TAG, "GoogleMap Fragment : " + mFragment);
		return mView;

	}

	public static interface onMapReadyListener {
		void onMapReady();
	}
}
