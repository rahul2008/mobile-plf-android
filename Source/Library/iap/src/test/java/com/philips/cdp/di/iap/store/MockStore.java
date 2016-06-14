/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.core.StoreSpec;

import org.mockito.Mockito;

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
        when(mUser.getJanRainEmail()).thenReturn(NetworkURLConstants.JANRAIN_EMAIL);
        when(mUser.getJanRainID()).thenReturn(NetworkURLConstants.JANRAIN_ID);
    }

    public StoreSpec getStore() {
        return new HybrisStore(mContext) {
            @Override
            protected StoreConfiguration getStoreConfig(final Context context) {
                return getStoreConfiguration(this);
            }

            @Override
            IAPUser initIAPUser(final Context context) {
                return mUser;
            }
        };
    }

    private StoreConfiguration getStoreConfiguration(HybrisStore hybrisStore) {
        return new StoreConfiguration(mContext, hybrisStore) {
            @Override
            VerticalAppConfig getVerticalAppConfig(final Context context) {
                return new MockVerticalAppConfig(mContext);
            }

            @Override
            WebStoreConfig getWebStoreConfig(final Context context) {
                return new MockWebStoreConfig(mContext, this);
            }
        };
    }
}
