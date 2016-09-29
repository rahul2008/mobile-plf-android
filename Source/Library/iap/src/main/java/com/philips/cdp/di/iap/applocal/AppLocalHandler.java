/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;

import com.philips.cdp.di.iap.core.IAPExposedAPI;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.session.IAPListener;

public class AppLocalHandler extends IAPInterface implements IAPExposedAPI {

    private Context mContext;

    public AppLocalHandler(Context context) {
        mContext = context;
    }

    @Override
    public void getProductCartCount(final IAPListener iapListener) {
        iapListener.onGetCartCount(0);
    }

    @Override
    public void getCompleteProductList(IAPListener iapListener) {
    }
}