/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.session.RequestListener;

/**
 * This class having responsible for Store configuration
 */
public class StoreConfiguration {
    private static final String SUFFIX_INAPPCONFIG = "inAppConfig";

    private final HybrisStore mStore;
    private final VerticalAppConfig mVerticalAppConfig;
    private final WebStoreConfig mWebStoreConfig;

    public StoreConfiguration(Context context, HybrisStore store) {
        mStore = store;
        mVerticalAppConfig = getVerticalAppConfig(context);
        mWebStoreConfig = getWebStoreConfig(context);
    }

    void initConfig(final String language, String countryCode, RequestListener listener) {
        mWebStoreConfig.initConfig(language, countryCode, listener);
    }

    public String getHostPort() {
        return mVerticalAppConfig.getHostPort();
    }

    public String getSite() {
        return mWebStoreConfig.getSiteID();
    }

    public String getProposition() {
        return mVerticalAppConfig.getProposition();
    }

    VerticalAppConfig getVerticalAppConfig(final Context context) {
        return new VerticalAppConfig(context);
    }

    WebStoreConfig getWebStoreConfig(final Context context) {
        return new WebStoreConfig(context, this);
    }

    public String getRawConfigUrl() {
       final StringBuilder builder = new StringBuilder(HybrisStore.HTTPS);
        builder.append(getHostPort()).append(HybrisStore.SEPERATOR);
        builder.append(HybrisStore.WEB_ROOT).append(HybrisStore.SEPERATOR);
        builder.append(HybrisStore.V2).append(HybrisStore.SEPERATOR);
        builder.append(SUFFIX_INAPPCONFIG).append(HybrisStore.SEPERATOR);
        builder.append(mWebStoreConfig.getLocale()).append(HybrisStore.SEPERATOR);
        builder.append(getProposition());

        return builder.toString();
    }

    public void generateStoreUrls() {
        mStore.generateStoreUrls();
    }

    public String getLocale() {
        return mWebStoreConfig.getLocale();
    }
}