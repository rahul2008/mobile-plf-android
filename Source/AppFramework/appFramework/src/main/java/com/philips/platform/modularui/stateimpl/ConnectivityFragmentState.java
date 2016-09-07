package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.connectivity.ConnectivityFragment;
import com.philips.platform.modularui.statecontroller.UIState;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConnectivityFragmentState extends UIState{
    public ConnectivityFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        ((AppFrameworkBaseActivity)context).showFragment( new ConnectivityFragment(), ConnectivityFragment.TAG);
    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).finishActivity();
    }
}
