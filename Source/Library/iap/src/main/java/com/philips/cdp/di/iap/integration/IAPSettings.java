/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.content.Context;

import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.net.URL;

public class IAPSettings extends UappSettings {
    private boolean mUseLocalData;
    private String mProposition;
    private String mHostPort;

    public IAPSettings(Context applicationContext) {
        super(applicationContext);
        loadCofigParams();
//        initServiceDiscovery();
    }

    private void loadCofigParams() {
        AppConfigurationInterface mConfigInterface = RegistrationHelper.getInstance().getAppInfraInstance().getConfigInterface();
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();

        mHostPort = (String) mConfigInterface.getPropertyForKey("hostport", "IAP", configError);
        mProposition = (String) mConfigInterface.getPropertyForKey("propositionid", "IAP", configError);

        if (configError.getErrorCode() != null) {
            IAPLog.e(IAPLog.LOG, "VerticalAppConfig ==loadConfigurationFromAsset " + configError.getErrorCode().toString());
        }
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
        serviceDiscoveryInterface.getServiceUrlWithLanguagePreference("iap.baseurl", new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        setUseLocalData(true);
//                        throw new RuntimeException("Cannot fetch base url");
                    }

                    @Override
                    public void onSuccess(URL url) {
                        if (url.toString().isEmpty()) {
                            setUseLocalData(true);
                        } else {
                            setUseLocalData(false);
                            String urlPort = url.toString();//"https://acc.occ.shop.philips.com/en_US";"https://www.occ.shop.philips.com/en_US"
                            mHostPort = urlPort.substring(0, urlPort.length() - 5);
                        }
                    }
                });
    }
}
