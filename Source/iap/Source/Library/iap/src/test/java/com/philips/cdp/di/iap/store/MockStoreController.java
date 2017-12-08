/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.session.MockIAPHurlStack;
import com.philips.cdp.di.iap.session.MockSynchronizedNetwork;
import com.philips.cdp.di.iap.session.SynchronizedNetwork;

import org.mockito.Mock;

public class MockStoreController extends StoreController {
    private final static String LOCALE = "en_US";
    private final static String SITE_ID = "US_TUSCANY";
    private SynchronizedNetwork mSynchronizedNetwork;



    public MockStoreController(final Context context, final StoreConfiguration storeConfig) {
        super(context, storeConfig);
    }

    @Override
    String getLocale() {
        return LOCALE;
    }

    @Override
    String getSiteID() {
        return SITE_ID;
    }

    @Override
    SynchronizedNetwork getSynchronizedNetwork() {
        if (mSynchronizedNetwork == null) {
            mSynchronizedNetwork = new MockSynchronizedNetwork((new MockIAPHurlStack(null)
                    .getHurlStack()));
        }
        return mSynchronizedNetwork;
    }
}
