/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.mecHandler;

import com.philips.cdp.di.mec.integration.MECListener;

public interface MECExposedAPI {
    void getProductCartCount(MECListener iapListener);

    void getCompleteProductList(MECListener iapListener);

    void isCartVisible(MECListener iapListener);
}
