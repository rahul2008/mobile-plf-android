/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.aboutscreen.AboutScreenFragment;
import com.philips.platform.modularui.statecontroller.UIState;

public class AboutScreenState  extends UIState {
    AppFrameworkApplication appFrameworkApplication;


    public AboutScreenState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        ((AppFrameworkBaseActivity)context).showFragment( new AboutScreenFragment(), AboutScreenFragment.TAG);
    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBackTillHomeFragment();
    }
}

