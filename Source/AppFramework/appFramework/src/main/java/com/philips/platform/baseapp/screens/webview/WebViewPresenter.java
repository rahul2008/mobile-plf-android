/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.webview;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;

import java.net.URL;


/**
 * Created by philips on 25/07/17.
 */

public class WebViewPresenter implements WebViewContract.Action {

    private WebViewContract.View viewListener;

    private Context context;


    public WebViewPresenter(WebViewContract.View viewListener, Context context) {
        this.viewListener = viewListener;
        this.context = context;
    }


    @Override
    public void loadUrl(String serviceId) {
        getAppInfra().getServiceDiscovery().getServiceUrlWithCountryPreference(serviceId, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                viewListener.onUrlLoadSuccess(url.toString());
            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                viewListener.onUrlLoadError(s);
            }
        });
    }

    protected AppInfraInterface getAppInfra() {
        return ((AppFrameworkApplication) context.getApplicationContext()).getAppInfra();
    }
}
