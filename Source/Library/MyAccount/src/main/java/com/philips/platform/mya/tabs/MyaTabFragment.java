/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.tabs;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.csw.ConsentBundleConfig;
import com.philips.platform.mya.MyaPager;
import com.philips.platform.mya.R;
import com.philips.platform.mya.util.mvp.MyaBaseFragment;

public class MyaTabFragment extends MyaBaseFragment {

    private View view;
    private ConsentBundleConfig config;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            config = new ConsentBundleConfig(getArguments());
            view = inflater.inflate(R.layout.mya_tab_fragment, container, false);
            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
            ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
            addTabs(tabLayout);
            MyaPager adapter = new MyaPager(this.getChildFragmentManager(), tabLayout.getTabCount(), config);
            viewPager.setAdapter(adapter);
            tabLayout.addOnTabSelectedListener(getTabListener(viewPager));
        }
        return view;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            config = new ConsentBundleConfig(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(config.toBundle());
    }

    private void addTabs(TabLayout tabLayout) {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.mya_profile)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.mya_settings)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private TabLayout.OnTabSelectedListener getTabListener(final ViewPager viewPager) {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        };
    }

    @Override
    public int getActionbarTitleResId() {
        return R.string.MYA_My_account;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.MYA_My_account);
    }

    @Override
    public boolean getBackButtonState() {
        return false;
    }
}
