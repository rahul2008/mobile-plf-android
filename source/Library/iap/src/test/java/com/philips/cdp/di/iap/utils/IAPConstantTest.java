package com.philips.cdp.di.iap.utils;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IAPConstantTest extends TestCase {

    @Test
    public void testConstant() {
        assertEquals("Value is Same", true, IAPConstant.TEST_MODE);
        assertEquals("Value is Same", "EMPTY_CART_FRGMENT_REPLACED", IAPConstant.EMPTY_CART_FRGMENT_REPLACED);
        assertFalse(IAPConstant.BUTTON_STATE_CHANGED);
        assertEquals("Value is Same", 0, IAPConstant.IAP_SUCCESS);
        assertEquals("Value is Same", -1, IAPConstant.IAP_ERROR);
    }
}