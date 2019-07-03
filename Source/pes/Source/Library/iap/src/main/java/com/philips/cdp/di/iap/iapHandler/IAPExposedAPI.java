/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.iapHandler;

import com.philips.cdp.di.iap.integration.IAPListener;

public interface IAPExposedAPI {
    void getProductCartCount(IAPListener iapListener);

    void getCompleteProductList(IAPListener iapListener);

    void isCartVisible(IAPListener iapListener);
}
