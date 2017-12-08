/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentAboutScreenSelectBinding;

public class AboutScreenSelectFragment extends BaseFragment {

    private FragmentAboutScreenSelectBinding fragmentAboutScreenSelectBinding;

    @Override
    public int getPageTitle() {
        return R.string.page_title_about_screen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAboutScreenSelectBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_screen_select, container, false);
        fragmentAboutScreenSelectBinding.setFragment(this);
        return fragmentAboutScreenSelectBinding.getRoot();
    }

    public void showAboutScreen() {
        showFragment(new AboutScreenFragment());
    }
}