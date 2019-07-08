/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.iapHandler;


import com.ecs.demouapp.ui.integration.IAPListener;

public interface IAPExposedAPI {
    void getProductCartCount(IAPListener iapListener);

    void getCompleteProductList(IAPListener iapListener);

    void isCartVisible(IAPListener iapListener);
}
