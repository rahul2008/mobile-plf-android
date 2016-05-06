package com.philips.cdp.di.iap.session;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.Locale;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class NetworkConstantsTest extends TestCase {

    @Test
    public void testPRXConstant() {
        assertEquals("B2C", NetworkConstants.PRX_SECTOR_CODE);
        assertEquals("CONSUMER", NetworkConstants.PRX_CATALOG_CODE);
        assertEquals("EXTRA_ANIMATIONTYPE", NetworkConstants.EXTRA_ANIMATIONTYPE);
        assertEquals("IS_ONLINE", NetworkConstants.IS_ONLINE);
        assertEquals(Locale.US, NetworkConstants.STORE_LOCALE);
        assertEquals("", NetworkConstants.EMPTY_RESPONSE);
    }
}