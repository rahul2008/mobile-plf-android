/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.config.AppConfigResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

class VerticalAppConfig {
    String mHostPort;
    String mPropositionID;

    VerticalAppConfig(final Context context) {
        loadConfigurationFromAsset(context);
    }

    void loadConfigurationFromAsset(Context context) {
        InputStream fromAsset = null;
        Reader reader = null;
        try {
            fromAsset = readJSONInputStream(context);
            reader = new BufferedReader(new InputStreamReader(fromAsset));
            AppConfigResponse configuration = new Gson().fromJson(reader, AppConfigResponse.class);
            mHostPort = configuration.getHostport();
            mPropositionID = configuration.getPropositionid();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fromAsset != null) {
                try {
                    fromAsset.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public InputStream readJSONInputStream(final Context context) throws IOException {
        return context.getResources().getAssets().open("PhilipsInAppPurchaseConfiguration.json");
    }

    public String getHostPort() {
        return mHostPort;
    }

    public String getPropositionID() {
        return mPropositionID;
    }
}
