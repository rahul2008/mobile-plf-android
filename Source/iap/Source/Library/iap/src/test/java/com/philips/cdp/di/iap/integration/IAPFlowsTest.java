package com.philips.cdp.di.iap.integration;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class IAPFlowsTest {
    @Test
    public void testIAPFlows() {
        assertEquals(0, IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW);
        assertEquals(1, IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW);
        assertEquals(2, IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW);
        assertEquals(3, IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW);
        assertEquals(4, IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW);
    }
}