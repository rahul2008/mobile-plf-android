/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.tabs;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.MyaPager;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.MyaBaseFragment;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;

public class MyaTabFragment extends MyaBaseFragment {

    private View view;
    private String TAB_BUNDLE = "tab_bundle";
    private String TAB_POSITION = "tab_position";
    private ViewPager viewPager;
    private MyaLaunchInput myaLaunchInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        if (view == null) {
            view = inflater.inflate(R.layout.mya_tab_fragment, container, false);
            UIDHelper.injectCalligraphyFonts();
            TabLayout tabLayout = view.findViewById(R.id.tab_layout);
            viewPager = view.findViewById(R.id.pager);
            MyaPager adapter;
            int tabPosition=0;
            addTags(tabPosition);
            myaLaunchInput = MyaHelper.getInstance().getMyaLaunchInput();
            if (savedInstanceState != null) {
                Bundle arguments = savedInstanceState.getBundle(TAB_BUNDLE);
                this.setArguments(arguments);
                tabPosition = savedInstanceState.getInt(TAB_POSITION);
            }
            addTabs(tabLayout);
            adapter = new MyaPager(this.getChildFragmentManager(), tabLayout.getTabCount(), this);
            if (myaLaunchInput != null && myaLaunchInput.getMyaTabConfig() != null) {
                if (myaLaunchInput.getMyaTabConfig().getFragment() != null)
                    adapter.setTabConfiguredFragment(myaLaunchInput.getMyaTabConfig().getFragment());
                else {
                    MyaHelper.getInstance().getMyaLogger().log(LoggingInterface.LogLevel.DEBUG, " mya ", " Input(MYATabConfig) to configure Proposition specific Tab not provided ");
                }
            }
            viewPager.setAdapter(adapter);
            tabLayout.addOnTabSelectedListener(getTabListener(viewPager));
            viewPager.setCurrentItem(tabPosition, true);
            tabLayout.setScrollPosition(tabPosition, 0, true);
            applyFontTab(tabLayout);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(TAB_BUNDLE, getArguments());
        outState.putInt(TAB_POSITION, viewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    private void addTabs(TabLayout tabLayout) {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.MYA_Profile)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.MYA_Settings)));
        if (myaLaunchInput != null && myaLaunchInput.getMyaTabConfig() != null && myaLaunchInput.getMyaTabConfig().getTabName() != null && myaLaunchInput.getMyaTabConfig().getFragment() != null)
            tabLayout.addTab(tabLayout.newTab().setText(myaLaunchInput.getMyaTabConfig().getTabName()));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


    }

    private void applyFontTab(TabLayout tabLayout) {
        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            Label tv = (Label) getActivity().getLayoutInflater().inflate(R.layout.mya_tab_label, null);
            if (i == viewPager.getCurrentItem()) tv.setSelected(true);
            TabLayout.Tab tabAt = tabLayout.getTabAt(i);
            tv.setText(tabAt != null ? tabAt.getText() : "");
            try {
                if (tabAt != null) {
                    tabAt.setCustomView(tv);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private TabLayout.OnTabSelectedListener getTabListener(final ViewPager viewPager) {
        return new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                addTags(tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
            }
        };
    }

    private void addTags(int position) {
        switch (position) {
            case 0:
                MyaHelper.getInstance().getAppTaggingInterface().trackPageWithInfo("MYA_01_01_profile_page",null,null);
                break;
            case 1:
                MyaHelper.getInstance().getAppTaggingInterface().trackPageWithInfo("MYA_01_06_settings_page",null,null);
                break;
            default:
                break;
        }
    }

    @Override
    public int getActionbarTitleResId() {
        return R.string.MYA_My_account;
    }

    @Override
    public String getActionbarTitle(Context context) {
        return context.getString(R.string.MYA_My_account);
    }

    @Override
    public boolean getBackButtonState() {
        return false;
    }
}
