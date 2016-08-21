package com.philips.cdp.di.iap.core;

import com.philips.cdp.di.iap.session.IAPHandlerProductListListener;
import com.philips.cdp.di.iap.session.IAPListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface IAPExposedAPI {
    void getProductCartCount(IAPListener iapListener);

    void getCompleteProductList(IAPHandlerProductListListener iapHandlerListener);
}
