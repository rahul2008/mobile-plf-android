package com.philips.cdp.di.iap.integration;

/**
 * Created by philips on 4/26/19.
 */

public interface IAPOrderFlowCompletion {

    default void didPlaceOrder() {

    }

    default void didCancelOrder() {

    }
    boolean shouldPopToProductList();
}
