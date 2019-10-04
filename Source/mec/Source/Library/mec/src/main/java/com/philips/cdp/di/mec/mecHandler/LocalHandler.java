/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.mecHandler;

import com.philips.cdp.di.mec.integration.MECInterface;
import com.philips.cdp.di.mec.integration.MECListener;

public class LocalHandler extends MECInterface implements MECExposedAPI {

    @Override
    public void getProductCartCount(final MECListener mecListener) {
        mecListener.onGetCartCount(-1);
    }

    @Override
    public void getCompleteProductList(MECListener iapListener) {
    }
}