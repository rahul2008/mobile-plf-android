/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.store;

import android.content.Context;

import com.ecs.demouapp.ui.integration.ECSSettings;
import com.ecs.demouapp.ui.session.RequestListener;


public class StoreConfiguration {
    private static final String SUFFIX_CONFIGURATION = "inAppConfig";
    private final ECSSettings mIAPSettings;

    private final HybrisStore mStore;
    private final StoreController mWebStoreConfig;

    public StoreConfiguration(Context context, HybrisStore store, ECSSettings iapSettings) {
        mStore = store;
        mIAPSettings = iapSettings;
        mWebStoreConfig = getWebStoreConfig(context);
    }

    StoreController getWebStoreConfig(final Context context) {
        return new StoreController(context, this);
    }

    void initConfig(RequestListener listener) {
        mWebStoreConfig.initConfig(listener);
    }

    public String getHostPort() {
        return mIAPSettings.getHostPort();
    }

    public String getProposition() {
        return mIAPSettings.getProposition();
    }

    public String getSite() {
        return mWebStoreConfig.getSiteID();
    }

    public String getCampaign() {
        return mWebStoreConfig.getCampaignID();
    }

    public String getLocale() {
        return mWebStoreConfig.getLocale();
    }

    public String getRawConfigUrl() {
        return getHostPort() + HybrisStore.WEBROOT + HybrisStore.SEPERATOR + HybrisStore.V2 + HybrisStore.SEPERATOR +
                SUFFIX_CONFIGURATION + HybrisStore.SEPERATOR +
                mWebStoreConfig.getLocale() + HybrisStore.SEPERATOR +
                getProposition();
    }

    public void generateStoreUrls() {
        mStore.generateStoreUrls();
    }
}