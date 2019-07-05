/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.products;

import com.philips.cdp.di.iap.integration.IAPListener;

import java.util.ArrayList;

public interface ProductCatalogAPI {
    boolean getProductCatalog(int currentPage, int pageSize, IAPListener listener);

    void getCategorizedProductList(ArrayList<String> productList);

    void getCompleteProductList(IAPListener iapListener);

    void getCatalogCount(IAPListener listener);
}
