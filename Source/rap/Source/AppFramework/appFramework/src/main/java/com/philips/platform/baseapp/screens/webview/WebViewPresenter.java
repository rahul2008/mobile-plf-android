/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.webview;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.baseapp.base.AppFrameworkApplication;

import java.util.ArrayList;
import java.util.Map;

public class WebViewPresenter implements WebViewContract.Action {

    private WebViewContract.View viewListener;

    private Context context;


    public WebViewPresenter(WebViewContract.View viewListener, Context context) {
        this.viewListener = viewListener;
        this.context = context;
    }


    @Override
    public void loadUrl(String serviceId) {
        ArrayList<String> serviceIdList = new ArrayList<>();
        serviceIdList.add(serviceId);
        getAppInfra().getServiceDiscovery().getServicesWithCountryPreference(serviceIdList, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                viewListener.onUrlLoadSuccess(urlMap.toString());
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                viewListener.onUrlLoadError(message);
            }
        },null );
    }

    protected AppInfraInterface getAppInfra() {
        return ((AppFrameworkApplication) context.getApplicationContext()).getAppInfra();
    }
}
