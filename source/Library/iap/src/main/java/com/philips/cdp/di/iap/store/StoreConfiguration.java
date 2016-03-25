/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class StoreConfiguration {
    private String hostport;
    private String site;
    private int mTheme;

    public StoreConfiguration(Context context) {
        loadConfigurationFromAsset(context);
    }

    public String getHostPort() {
        return hostport;
    }

    public String getSite() {
        return site;
    }

    public int getTheme() {
        return mTheme;
    }

    private void loadConfigurationFromAsset(Context context) {
        InputStream fromAsset = null;
        Reader reader = null;
        try {
            fromAsset = context.getResources().getAssets().open("PhilipsInAppPurchaseConfiguration.json");
            reader = new BufferedReader(new InputStreamReader(fromAsset));
            StoreConfiguration configuration = new Gson().fromJson(reader, StoreConfiguration.class);
            hostport = configuration.hostport;
            site = configuration.site;
            mTheme = configuration.mTheme;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fromAsset != null) {
                try {
                    fromAsset.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}