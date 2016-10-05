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

    private final HybrisStore mStore;
    private final VerticalAppConfig mVerticalAppConfig;
    private final WebStoreConfig mWebStoreConfig;

    public StoreConfiguration(Context context, HybrisStore store, IAPSettings iapSettings) {
        mStore = store;
        mVerticalAppConfig = getVerticalAppConfig(iapSettings);
        mWebStoreConfig = getWebStoreConfig(context);
    }

    VerticalAppConfig getVerticalAppConfig(final IAPSettings iapSettings) {
        return new VerticalAppConfig(iapSettings);
    }

    WebStoreConfig getWebStoreConfig(final Context context) {
        return new WebStoreConfig(context, this);
    }

    void initConfig(final String language, String countryCode, RequestListener listener) {
        mWebStoreConfig.initConfig(language, countryCode, listener);
    }

    public String getHostPort() {
        return mVerticalAppConfig.getHostPort();
    }

    public String getProposition() {
        return mVerticalAppConfig.getProposition();
    }

    public String getSite() {
        return mWebStoreConfig.getSiteID();
    }

    public String getLocale() {
        return mWebStoreConfig.getLocale();
    }

    public String getRawConfigUrl() {
        final StringBuilder builder = new StringBuilder(getHostPort());
        builder.append(SUFFIX_CONFIGURATION).append(HybrisStore.SEPERATOR);
        builder.append(mWebStoreConfig.getLocale()).append(HybrisStore.SEPERATOR);
        builder.append(getProposition());
        return builder.toString();
    }

    public void generateStoreUrls() {
        mStore.generateStoreUrls();
    }
}