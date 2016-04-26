package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.session.IAPHurlStack;
import com.philips.cdp.di.iap.session.MockSynchronizedNetwork;
import com.philips.cdp.di.iap.session.SynchronizedNetwork;
import com.philips.cdp.localematch.PILLocaleManager;

import org.mockito.Mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MockWebStoreConfig extends WebStoreConfig {
    private final static String LOCALE = "en_US";
    private final static String SITE_ID = "US_TUSCANY";

    @Mock
    PILLocaleManager mPILLocalManager;
    public MockWebStoreConfig(final Context context, final StoreConfiguration storeConfig) {
        super(context, storeConfig);
    }

    @Override
    String getLocale() {
        return LOCALE;
    }

    @Override
    void setPILLocalMangaer() {
        mLocaleManager = mPILLocalManager;
    }

    @Override
    void initLocaleMatcher() {
        requestHybrisConfig();
    }

    @Override
    void requestHybrisConfig() {
        mSiteID = SITE_ID;
        mStoreConfig.getPropositionID();
        mStoreConfig.getRawConfigUrl();
        mStoreConfig.generateStoreUrls();
    }

    @Override
    void refresh(final String language, final String countryCode) {
        initLocaleMatcher();
    }

    @Override
    SynchronizedNetwork getSynchronizedNetwork() {
        return new MockSynchronizedNetwork((new IAPHurlStack(null).getHurlStack()));
    }
}
