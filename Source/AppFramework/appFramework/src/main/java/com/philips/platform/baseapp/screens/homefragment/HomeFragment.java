/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.homefragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkBaseFragment;

/**
 * This is the home fragment the main landing page of the application , once onboarding is completed.
 * All the fragments are added on top of this , handleBack event from all other fragemnts ends up  landing here
 */

public class HomeFragment extends AppFrameworkBaseFragment {
    public static final String TAG =  HomeFragment.class.getSimpleName();
    public HomeFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("testmicroapp","Current state::"+((AppFrameworkApplication)getActivity().getApplicationContext()).getTargetFlowManager().getCurrentState().toString());
        ((AppFrameworkBaseActivity)getActivity()).updateActionBarIcon(false);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_HomeScreen_Title);
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
