/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

/**
 * This class will read the inapp configuration file to get hostport and propostion
 */
public class VerticalAppConfig {
    private String mHostPort;
    private String mProposition;


    public VerticalAppConfig(IAPDependencies iapDependencies) {
        loadConfigurationFromAsset(iapDependencies);
    }

    void loadConfigurationFromAsset(IAPDependencies iapDependencies) {
        AppConfigurationInterface mConfigInterface = iapDependencies.getAppInfra().getConfigInterface();
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
        try {
            mHostPort = (String) mConfigInterface.getPropertyForKey("hostport", "IAP", configError);
            mProposition = (String) mConfigInterface.getPropertyForKey("propositionid", "IAP", configError);
        } catch (AppConfigurationInterface.InvalidArgumentException e) {
            e.printStackTrace();
        }

        if (configError.getErrorCode() != null) {
            IAPLog.e(IAPLog.LOG, "VerticalAppConfig ==loadConfigurationFromAsset " + configError.getErrorCode().toString());
        }
    }

    public String getHostPort() {
        return mHostPort;
    }

    public String getProposition() {
        return mProposition;
    }
}
