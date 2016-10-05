package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.session.MockIAPHurlStack;
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
    private SynchronizedNetwork mSynchronizedNetwork;

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
    void setPILocalManager() {
        mLocaleManager = mPILLocalManager;
    }

    @Override
    void initLocaleMatcher() {
        mSiteID = SITE_ID;
        mStoreConfig.getProposition();
        mStoreConfig.getRawConfigUrl();
        mStoreConfig.generateStoreUrls();
//        fetchConfiguration();
    }

/*    @Override
    void fetchConfiguration() {
        mSiteID = SITE_ID;
        mStoreConfig.getProposition();
        mStoreConfig.getRawConfigUrl();
        mStoreConfig.generateStoreUrls();
    }*/

    @Override
    void refresh(final String language, final String countryCode) {
        initLocaleMatcher();
    }

    @Override
    SynchronizedNetwork getSynchronizedNetwork() {
        if(mSynchronizedNetwork == null) {
            mSynchronizedNetwork = new MockSynchronizedNetwork((new MockIAPHurlStack(null)
                    .getHurlStack()));;
        }
        return mSynchronizedNetwork;
    }
}
