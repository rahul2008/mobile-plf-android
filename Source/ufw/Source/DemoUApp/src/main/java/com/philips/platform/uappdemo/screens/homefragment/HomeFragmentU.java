/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.uappdemo.screens.homefragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.uappdemo.screens.base.UappBaseActivity;
import com.philips.platform.uappdemo.screens.base.UAppBaseFragment;
import com.philips.platform.uappdemolibrary.R;

/**
 * This is the home fragment the main landing page of the application , once onboarding is completed.
 * All the fragments are added on top of this , handleBack event from all other fragemnts ends up  landing here
 */

public class HomeFragmentU extends UAppBaseFragment {
    public static final String TAG =  HomeFragmentU.class.getSimpleName();
    public HomeFragmentU() {
    }

    @Override
    public void onResume() {
        super.onResume();
        ((UappBaseActivity)getActivity()).updateActionBarIcon(false);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.app_name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.af_home_fragment, container, false);
        setDateToView();
        return rootView;
    }
    

    private void setDateToView() {
        Bundle bundle = getArguments();
    }
}