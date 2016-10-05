/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.net.URL;

/**
 * This class will read the inapp configuration file to get hostport and propostion
 */
public class VerticalAppConfig {
    private String mHostPort;
    private String mProposition;
    private IAPSettings mIAPSettings;

    public VerticalAppConfig(IAPSettings iapSettings) {
        //  loadConfigurationFromAsset(iapDependencie;s);
        mIAPSettings = iapSettings;
        initServiceDiscovery(iapSettings);
    }

    void loadConfigurationFromAsset(IAPDependencies iapDependencies) {
        AppConfigurationInterface mConfigInterface = iapDependencies.getAppInfra().getConfigInterface();
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();

        mHostPort = (String) mConfigInterface.getPropertyForKey("hostport", "IAP", configError);
        mProposition = (String) mConfigInterface.getPropertyForKey("propositionid", "IAP", configError);

        if (configError.getErrorCode() != null) {
            IAPLog.e(IAPLog.LOG, "VerticalAppConfig ==loadConfigurationFromAsset " + configError.getErrorCode().toString());
        }
    }

    private void initServiceDiscovery(final IAPSettings iapSettings) {
        AppInfraInterface appInfra = RegistrationHelper.getInstance().getAppInfraInstance();
        final ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();

        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("userreg.janrain.api", new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        IAPLog.d("IsStoreAvailable", s);
                    }

                    @Override
                    public void onSuccess(URL url) {
                        if (url.toString().isEmpty()) {
                            iapSettings.setUseLocalData(true);
                        } else {
                            iapSettings.setUseLocalData(false);
                            fetchBaseUrl(serviceDiscoveryInterface);
                        }
                    }
                });
    }

    public String getHostPort() {
        return mHostPort;
    }

    public String getProposition() {
        return mProposition;
    }

    private void fetchBaseUrl(ServiceDiscoveryInterface serviceDiscoveryInterface) {
        serviceDiscoveryInterface.getServiceUrlWithCountryPreference("iap.get.baseurl", new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        IAPLog.d("Baseurl onError", s);
                    }

                    @Override
                    public void onSuccess(URL url) {
                        //Assign base url
                        IAPLog.d("Baseurl onSuccess URL = ", url.toString());
                        mHostPort = "https://acc.occ.shop.philips.com/pilcommercewebservices/v2/";
                        mProposition = mIAPSettings.getProposition();
                    }
                });
    }
}
