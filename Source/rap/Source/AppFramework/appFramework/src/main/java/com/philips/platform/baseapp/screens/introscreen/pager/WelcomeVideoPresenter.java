/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.introscreen.pager;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.Constants;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

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

        ArrayList<String> serviceIDList = new ArrayList<>();
        serviceIDList.add(Constants.SERVICE_DISCOVERY_SPLASH_VIDEO);
        serviceDiscoveryInterface.getServicesWithLanguagePreference(serviceIDList, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                view.setVideoDataSource(urlMap.get(Constants.SERVICE_DISCOVERY_SPLASH_VIDEO).getConfigUrls() + COMPRESSED_VIDEO_EXTENSION);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                view.onFetchError();
            }
        },null);
    }
}
