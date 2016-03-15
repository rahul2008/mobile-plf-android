package com.philips.cdp.backend;

import android.test.InstrumentationTestCase;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegRequestInfoTest extends InstrumentationTestCase {

    ProdRegRequestInfo prodRegRequestInfo;

    public void testGetSerialNumber() throws Exception {
        prodRegRequestInfo.setSerialNumber("1344");
        assertEquals("1344", prodRegRequestInfo.getSerialNumber());
    }

    public void testGetPurchaseDate() throws Exception {
        prodRegRequestInfo.setPurchaseDate("28-02-2016");
        assertEquals("28-02-2016", prodRegRequestInfo.getPurchaseDate());
    }

    public void testGetLocale() throws Exception {
        prodRegRequestInfo.setLocale("en_GB");
        assertEquals("en_GB", prodRegRequestInfo.getLocale());
    }


}