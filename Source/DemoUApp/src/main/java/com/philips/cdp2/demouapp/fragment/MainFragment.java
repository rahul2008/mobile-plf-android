/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.commlib.demouapp.R;

public class MainFragment extends Fragment {

    private static final int NUMBER_OF_PAGES = 2;

    private static final int PAGE_DISCOVERED_APPLIANCES = 0;
    private static final int PAGE_MISMATCHED_PIN_APPLIANCES = 1;

    private MismatchedPinAppliancesFragment mismatchedPinAppliancesFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.cml_fragment_main, container, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = (ViewPager) rootview.findViewById(R.id.cml_container);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int position) {
                if (position == PAGE_MISMATCHED_PIN_APPLIANCES) {
                    if (mismatchedPinAppliancesFragment != null) {
                        mismatchedPinAppliancesFragment.refresh();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Do nothing
            }
        });

        TabLayout tabLayout = (TabLayout) rootview.findViewById(R.id.cml_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return rootview;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case PAGE_DISCOVERED_APPLIANCES:
                    return DiscoveredAppliancesFragment.newInstance();
                case PAGE_MISMATCHED_PIN_APPLIANCES:
                    mismatchedPinAppliancesFragment = MismatchedPinAppliancesFragment.newInstance();
                    return mismatchedPinAppliancesFragment;
                default:
                    throw new IllegalStateException("No fragment defined for position: " + position);
            }
        }

        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case PAGE_DISCOVERED_APPLIANCES:
                    return getString(R.string.cml_tab_title_discovered_appliances);
                case PAGE_MISMATCHED_PIN_APPLIANCES:
                    return getString(R.string.cml_tab_title_mismatched_pin_appliances);
            }
            return null;
        }
    }
}
