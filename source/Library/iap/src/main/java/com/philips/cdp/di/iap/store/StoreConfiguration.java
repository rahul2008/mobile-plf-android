/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.session.RequestListener;

/*
 * This class having responsible for Store configuration
 */
public class StoreConfiguration {
    private static final String SUFFIX_INAPPCONFIG = "inAppConfig";

    private HybrisStore mStore;
    private String hostport;
    private String site;
    private VerticalAppConfig mVerticalAppConfig;
    private WebStoreConfig mWebStoreConfig;

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

    public String getPropositionID() {
        return mVerticalAppConfig.getPropositionID();
    }

    VerticalAppConfig getVerticalAppConfig(final Context context) {
        return new VerticalAppConfig(context);
    }

    WebStoreConfig getWebStoreConfig(final Context context) {
        return new WebStoreConfig(context, this);
    }

    public String getRawConfigUrl() {
        StringBuilder builder = new StringBuilder(HybrisStore.HTTPS);
        builder.append(getHostPort()).append(HybrisStore.SEPERATOR);
        builder.append(HybrisStore.WEB_ROOT).append(HybrisStore.SEPERATOR);
        builder.append(HybrisStore.V2).append(HybrisStore.SEPERATOR);
        builder.append(SUFFIX_INAPPCONFIG).append(HybrisStore.SEPERATOR);
        builder.append(mWebStoreConfig.getLocale()).append(HybrisStore.SEPERATOR);
        builder.append(getPropositionID());

        return builder.toString();
    }

    public void generateStoreUrls() {
        mStore.generateStoreUrls();
    }

    public String getLocale() {
        return mWebStoreConfig.getLocale();
    }
}