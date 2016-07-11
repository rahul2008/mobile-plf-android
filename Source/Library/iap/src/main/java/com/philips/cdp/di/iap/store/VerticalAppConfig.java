/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.config.AppConfigResponse;
import com.philips.cdp.di.iap.utils.IAPLog;

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

    public VerticalAppConfig(final Context context) {
        loadConfigurationFromAsset(context);
    }

    void loadConfigurationFromAsset(Context context) {
        InputStream fromAsset = null;
        Reader reader = null;
        try {
            fromAsset = readJsonInputStream(context);
            reader = new BufferedReader(new InputStreamReader(fromAsset));
            AppConfigResponse configuration = new Gson().fromJson(reader, AppConfigResponse.class);
            mHostPort = configuration.getHostport();
            mProposition = configuration.getPropositionid();
        } catch (IOException e) {
            IAPLog.e(IAPLog.LOG, e.getMessage());
        } finally {
            if (fromAsset != null) {
                try {
                    fromAsset.close();
                } catch (IOException e) {
                    IAPLog.e(IAPLog.LOG, e.getMessage());
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    IAPLog.e(IAPLog.LOG, e.getMessage());
                }
            }
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
