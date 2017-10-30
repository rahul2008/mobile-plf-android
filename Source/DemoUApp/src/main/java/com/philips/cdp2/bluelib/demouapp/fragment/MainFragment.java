/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.bluelib.demouapp.R;
import com.philips.cdp2.bluelib.demouapp.fragment.associate.AssociatedDevicesFragment;
import com.philips.cdp2.bluelib.demouapp.fragment.connect.ConnectDevicesFragment;

public class MainFragment extends Fragment {
    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 1;
    private static final int NUMBER_OF_PAGES = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.bll_fragment_main, container, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = rootview.findViewById(R.id.bll_container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = rootview.findViewById(R.id.bll_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Acquire Bluetooth permission
        acquirePermission();

        return rootview;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AssociatedDevicesFragment.newInstance();
                case 1:
                    return ConnectDevicesFragment.newInstance();
            }
            throw new IllegalStateException("No fragment defined for position: " + position);
        }

        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.bll_association_title);
                case 1:
                    return getString(R.string.bll_connect_title);
            }
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION_REQUEST_CODE: {
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //TODO: finish();
                }
            }
        }
    }

    private void acquirePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_LOCATION_REQUEST_CODE);
        }
    }
}
