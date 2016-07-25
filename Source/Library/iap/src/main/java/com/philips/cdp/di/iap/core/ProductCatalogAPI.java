package com.philips.cdp.di.iap.core;

import android.content.Context;

import com.philips.cdp.di.iap.session.IAPHandlerListener;
import com.philips.cdp.di.iap.session.IAPHandlerProductListListener;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface ProductCatalogAPI {
    boolean getProductCatalog(int currentPage, int pageSize, IAPHandlerProductListListener listener);

    void getProductCategorizedProduct(ArrayList<String> productList);

    void getCompleteProductList(final Context mContext, final IAPHandlerProductListListener iapListener, final int currentPage, final int pageSize);

    void getCatalogCount(IAPHandlerListener listener);
}
