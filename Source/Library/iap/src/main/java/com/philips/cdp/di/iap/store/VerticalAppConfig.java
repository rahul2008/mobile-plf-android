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
            mHostPort = (String) mConfigInterface.getPropertyForKey("IAP", "hostport", configError);
            mProposition = (String) mConfigInterface.getPropertyForKey("IAP", "propositionid", configError);
        } catch (AppConfigurationInterface.InvalidArgumentException e) {
            e.printStackTrace();
        }

        if (configError.getErrorCode() != null) {
            IAPLog.e(IAPLog.LOG, "VerticalAppConfig ==loadConfigurationFromAsset " + configError.getErrorCode().toString());
        }
    }

//    public InputStream readJsonInputStream(final Context context) throws IOException {
//        final AssetManager assetManager = context.getAssets();
//        return assetManager.open("PhilipsInAppPurchaseConfiguration.json");
//    }

    public String getHostPort() {
        return mHostPort;
    }

    public String getProposition() {
        return mProposition;
    }
}
