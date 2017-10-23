/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.SplashScreenActivity;
import com.philips.platform.catalogapp.databinding.FragmentSplashScreenSelectBinding;

public class SplashScreenSelectFragment extends BaseFragment {

    private FragmentSplashScreenSelectBinding fragmentSplashScreenSelectBinding;

    @Override
    public int getPageTitle() {
        return R.string.page_title_splash_screen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentSplashScreenSelectBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash_screen_select, container, false);
        fragmentSplashScreenSelectBinding.setFragment(this);
        return fragmentSplashScreenSelectBinding.getRoot();
    }

    public void showSplashScreen() {
        Intent launchSplash = new Intent(getContext(), SplashScreenActivity.class);
        startActivity(launchSplash);
    }
}