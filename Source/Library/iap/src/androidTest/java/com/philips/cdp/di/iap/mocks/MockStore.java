/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.mocks;

import android.content.Context;

import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.store.HybrisStore;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.StoreListener;

import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockStore {

    private Context mContext;
    private IAPUser mUser;

    public MockStore(Context context, IAPUser user) {
        mContext = context;
        mUser = user;
        if (context == null) {
            mContext = Mockito.mock(Context.class);
        }
        if (user == null) {
            mUser = Mockito.mock(IAPUser.class);
        }
        when(mUser.getJanRainEmail()).thenReturn("JANRAIN_EMAIL");
        when(mUser.getJanRainID()).thenReturn("JANRAIN_ID");
    }

    public StoreListener getStore(IAPSettings iapSettings) {
        return mock(HybrisStore.class);
    }


//    public StoreListener getStore(IAPSettings pIAPSettings) {
//        HybrisStore hybrisStore = new HybrisStore(mContext, pIAPSettings) {
//            protected StoreConfiguration getStoreConfig(final Context context, final IAPSettings mIapSettings) {
//                return getStoreConfiguration(this, mIapSettings);
//            }
//
//            IAPUser createUser(final Context context) {
//                return mUser;
//            }
//
//            @Override
//            public String getCountry() {
//                return "US";
//            }
//        };
//        hybrisStore.createNewUser(mContext);
//        return hybrisStore;
//    }

//    private StoreConfiguration getStoreConfiguration(HybrisStore hybrisStore, IAPSettings pIapSettings) {
//        return new StoreConfiguration(mContext, hybrisStore, pIapSettings) {
//
//            StoreController getWebStoreConfig(final Context context) {
//                return new MockStoreController(mContext, this);
//            }
//        };
//    }
}
