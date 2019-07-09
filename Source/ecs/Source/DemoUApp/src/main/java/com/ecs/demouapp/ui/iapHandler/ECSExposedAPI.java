/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.iapHandler;


import com.ecs.demouapp.ui.integration.ECSListener;

public interface ECSExposedAPI {
    void getProductCartCount(ECSListener iapListener);

    void getCompleteProductList(ECSListener iapListener);

    void isCartVisible(ECSListener iapListener);
}
