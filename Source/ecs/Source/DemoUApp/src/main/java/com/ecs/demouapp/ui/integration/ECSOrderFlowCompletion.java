package com.ecs.demouapp.ui.integration;

/**
 * Created by philips on 4/26/19.
 */

public interface ECSOrderFlowCompletion {

    default void didPlaceOrder() {

    }

    default void didCancelOrder() {

    }
    boolean shouldPopToProductList();
}
