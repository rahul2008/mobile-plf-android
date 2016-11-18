/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class NetworkConstantsTest {

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