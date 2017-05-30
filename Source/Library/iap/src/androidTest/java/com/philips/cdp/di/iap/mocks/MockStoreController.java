/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.mocks;

import android.content.Context;

import com.philips.cdp.di.iap.session.SynchronizedNetwork;
import com.philips.cdp.di.iap.store.StoreConfiguration;
import com.philips.cdp.di.iap.store.StoreController;

import org.mockito.Mock;

public class MockStoreController extends StoreController {
    private final static String LOCALE = "en_US";
    private final static String SITE_ID = "US_TUSCANY";
    private SynchronizedNetwork mSynchronizedNetwork;

    public MockStoreController(final Context context, final StoreConfiguration storeConfig) {
        super(context, storeConfig);
    }

    String getLocale() {
        return LOCALE;
    }



    String getSiteID() {
        return SITE_ID;
    }

    SynchronizedNetwork getSynchronizedNetwork() {
        if (mSynchronizedNetwork == null) {
            mSynchronizedNetwork = new MockSynchronizedNetwork((new MockIAPHurlStack(null)
                    .getHurlStack()));
        }
        return mSynchronizedNetwork;
    }
}
