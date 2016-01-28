package com.philips.cdp.di.iap.session;

import com.philips.cdp.di.iap.activity.ProductSummary;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface UpdateProductInfoFromHybris {
    void updateProductInfo(ProductSummary summary, CartInfo cartInfo);
}
