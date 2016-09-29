/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import com.philips.cdp.di.iap.session.IAPListener;

public interface IAPExposedAPI {
    void getProductCartCount(IAPListener iapListener);

    void getCompleteProductList(IAPListener iapListener);
}
