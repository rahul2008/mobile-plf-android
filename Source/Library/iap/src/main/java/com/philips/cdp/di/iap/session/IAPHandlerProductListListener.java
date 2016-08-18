/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import java.util.ArrayList;

public interface IAPHandlerProductListListener /*extends UappListener*/{

    /**
     * Product list from Hybris request
     *
     * @param productList
     */
    void onSuccess(final ArrayList<String> productList);

    /**
     * Error occurred during the request
     *
     * @param errorCode
     */
    void onFailure(final int errorCode);

}
