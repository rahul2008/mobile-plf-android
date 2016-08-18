/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.response.config.AppConfigResponse;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.platform.appinfra.config.ConfigInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * This class will read the inapp configuration file to get hostport and propostion
 */
public class VerticalAppConfig {
    private String mHostPort;
    private String mProposition;
     private ConfigInterface mConfigInterface;


    public VerticalAppConfig(final Context context, IAPDependencies iapDependencies) {
        loadConfigurationFromAsset(context,iapDependencies);
    }

    void loadConfigurationFromAsset(Context context, IAPDependencies iapDependencies) {
        mConfigInterface = iapDependencies.getAppInfra().getConfigInterface();
        ConfigInterface.ConfigError configError = new ConfigInterface.ConfigError();
        mHostPort = (String) mConfigInterface.getPropertyForKey("IAP", "hostport", configError);
        mProposition = (String) mConfigInterface.getPropertyForKey("IAP", "propositionid", configError);
        if (configError.getErrorCode() != null) {
            IAPLog.e(IAPLog.LOG, "VerticalAppConfig ==loadConfigurationFromAsset " + configError.getErrorCode().toString());
        }
    }

    public InputStream readJsonInputStream(final Context context) throws IOException {
        final AssetManager assetManager = context.getAssets();
        return assetManager.open("PhilipsInAppPurchaseConfiguration.json");
    }

    public String getHostPort() {
        return mHostPort;
    }

    public String getProposition() {
        return mProposition;
    }
}
