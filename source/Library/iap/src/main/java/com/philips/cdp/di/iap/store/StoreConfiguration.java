/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.session.RequestListener;

public class StoreConfiguration {
    private static final String SUFFIX_CONFIGURATION = "inAppConfig";
    private final IAPSettings mIAPSettings;

    private final HybrisStore mStore;
    private final StoreController mWebStoreConfig;

    public StoreConfiguration(Context context, HybrisStore store, IAPSettings iapSettings) {
        mStore = store;
        mIAPSettings = iapSettings;
        mWebStoreConfig = getWebStoreConfig(context);
    }

    StoreController getWebStoreConfig(final Context context) {
        return new StoreController(context, this);
    }

    void initConfig(final String language, String countryCode, RequestListener listener) {
        mWebStoreConfig.initConfig(language, countryCode, listener);
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

    public String getLocale() {
        return mWebStoreConfig.getLocale();
    }

    public String getRawConfigUrl() {
        final StringBuilder builder = new StringBuilder(getHostPort());
        builder.append(HybrisStore.WEBROOT).append(HybrisStore.SEPERATOR).append(HybrisStore.V2).append(HybrisStore.SEPERATOR);
        builder.append(SUFFIX_CONFIGURATION).append(HybrisStore.SEPERATOR);
        builder.append(mWebStoreConfig.getLocale()).append(HybrisStore.SEPERATOR);
        builder.append(getProposition());
        return builder.toString();
    }

    public void generateStoreUrls() {
        mStore.generateStoreUrls();
    }
}