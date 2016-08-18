package com.philips.cdp.di.iap.core;

import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.di.iap.session.IAPHandlerProductListListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface IAPExposedAPI {
   // void launchIAP(int landingView, String ctnNumber, IAPListener listener);

    void getProductCartCount(IAPListener iapListener);

    void getCompleteProductList(IAPHandlerProductListListener iapHandlerListener);

    //void launchCategorizedCatalog(ArrayList<String> pProductCTNs);

    //void getCatalogCountAndCallCatalog();

    void buyDirect(String ctn);
}
