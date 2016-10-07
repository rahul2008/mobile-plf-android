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
        initServiceDiscovery();
    }

    public boolean isUseLocalData() {
        return mUseLocalData;
    }

    public void setUseLocalData(boolean mUseLocalData) {
        this.mUseLocalData = mUseLocalData;
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

    private void initServiceDiscovery() {
        AppInfraInterface appInfra = RegistrationHelper.getInstance().getAppInfraInstance();
        final ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();

        serviceDiscoveryInterface.getServiceUrlWithLanguagePreference("appinfra.testing.service", new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {

                    @Override
                    public void onSuccess(URL url) {
                        if (url.toString().isEmpty()) {
                            setUseLocalData(true);
                        } else {
                            setUseLocalData(false);
                            fetchBaseUrl(serviceDiscoveryInterface);
                        }
                    }

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        IAPLog.d("IsStoreAvailable", s);
                        //Notify
                    }
                });
    }

    private void fetchBaseUrl(ServiceDiscoveryInterface serviceDiscoveryInterface) {
        serviceDiscoveryInterface.getServiceUrlWithLanguagePreference("appinfra.testing.service", new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        IAPLog.d("Baseurl onError", s);
                        //Notify
                    }

                    @Override
                    public void onSuccess(URL url) {
                        IAPLog.d("Baseurl onSuccess URL = ", url.toString());
                        mHostPort = "https://acc.occ.shop.philips.com/pilcommercewebservices/v2/";
                    }
                });
    }
}
