/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.content.Context;

import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.net.URL;

public class IAPSettings extends UappSettings {
    private boolean mUseLocalData;
    private String mProposition;
    private String mHostPort;

    public IAPSettings(Context applicationContext) {
        super(applicationContext);
        mHostPort = "https://www.occ.shop.philips.com/";
//        initServiceDiscovery();
    }

    public boolean isUseLocalData() {
        return mUseLocalData;
    }

    public void setUseLocalData(boolean isLocalData) {
        mUseLocalData = isLocalData;
    }

    public String getProposition() {
        return mProposition;
    }

    public void setProposition(String proposition) {
        mProposition = proposition;
    }

    public String getHostPort() {
        return mHostPort;
    }

    protected void initServiceDiscovery() {
        AppInfraInterface appInfra = RegistrationHelper.getInstance().getAppInfraInstance();
        final ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();
        fetchBaseUrl(serviceDiscoveryInterface);
    }

    private void fetchBaseUrl(ServiceDiscoveryInterface serviceDiscoveryInterface) {
        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("iap.baseurl", new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        IAPLog.d("Baseurl onError", s);
                        //Throw Runtime exception so that vertical can handle
                    }

                    @Override
                    public void onSuccess(URL url) {
                        if (url.toString().isEmpty()) {
                            setUseLocalData(true);
                        } else {
                            IAPLog.d("Baseurl onSuccess URL = ", url.toString());
                            setUseLocalData(false);
                            String urlPort = "https://acc.occ.shop.philips.com/en_US"; // has to be removed once the xls is uploaded add url.toString()
                            mHostPort = urlPort.substring(0, urlPort.length() - 5);
                        }
                    }
                });
    }
}
