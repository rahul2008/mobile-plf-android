/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.termsandconditions;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 24/07/17.
 */

public class WebViewState extends BaseState {

    public WebViewState() {
        super(AppStates.WEB_VIEW_STATE);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        Intent intent=new Intent(fragmentLauncher.getFragmentActivity(),WebViewActivity.class);
        WebViewEnum termsAndPrivacyEnum=((WebViewStateData)getUiStateData()).getWebViewEnum();
        intent.putExtra(WebViewActivity.STATE,termsAndPrivacyEnum);
        fragmentLauncher.getFragmentActivity().startActivity(intent);
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
