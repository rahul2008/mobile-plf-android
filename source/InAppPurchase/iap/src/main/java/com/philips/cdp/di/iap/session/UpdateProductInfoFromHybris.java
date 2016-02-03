package com.philips.cdp.di.iap.session;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface UpdateProductInfoFromHybris extends RequestListener{
    void updateProductInfo(ProductSummary summary, CartInfo cartInfo, String hasCartItems);
}
