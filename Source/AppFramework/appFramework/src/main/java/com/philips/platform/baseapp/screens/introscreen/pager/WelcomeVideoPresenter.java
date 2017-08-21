/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.introscreen.pager;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.Constants;

import java.net.URL;

public class WelcomeVideoPresenter implements WelcomeVideoFragmentContract.Presenter {

    public static final String COMPRESSED_VIDEO_EXTENSION = "_iPad_640x480_1000K";
    private WelcomeVideoFragmentContract.View view;

    private Context context;

    public WelcomeVideoPresenter(WelcomeVideoFragmentContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }
    @Override
    public void fetchVideoDataSource() {
        AppInfraInterface appInfra = ((AppFrameworkApplication) context.getApplicationContext()).getAppInfra();
        ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();

        serviceDiscoveryInterface.getServiceUrlWithLanguagePreference(Constants.SERVICE_DISCOVERY_SPLASH_VIDEO,
                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onSuccess(URL url) {
                        view.setVideoDataSource(url.toString()+ COMPRESSED_VIDEO_EXTENSION);
                    }

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        view.onFetchError();
                    }
                });
    }
}
