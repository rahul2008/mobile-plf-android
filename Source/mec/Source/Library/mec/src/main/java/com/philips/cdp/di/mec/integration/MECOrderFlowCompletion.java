package com.philips.cdp.di.mec.integration;

/**
 * Created by philips on 4/26/19.
 */

public interface MECOrderFlowCompletion {

    default void didPlaceOrder() {

    }

    default void didCancelOrder() {

    }
    boolean shouldPopToProductList();
}
