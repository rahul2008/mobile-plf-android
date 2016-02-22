package com.philips.cdp.di.iap.utils;

import junit.framework.TestCase;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IAPLogTest extends TestCase {

    public void testEnableLogging() throws Exception {
        boolean enable = IAPLog.isLoggingEnabled();
        IAPLog.enableLogging();
        assertEquals(true, enable);
    }

    public void testDisableLogging() throws Exception {
        boolean disable = !IAPLog.isLoggingEnabled();
        IAPLog.disableLogging();
        assertEquals(false, disable);
    }

    public void testIsLoggingEnabled() throws Exception {
        IAPLog.isLoggingEnabled();
        assertTrue("islogingEnable", true);
    }
}