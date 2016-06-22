package com.philips.cdp.prodreg.util;

import com.philips.cdp.prodreg.MockitoTestCase;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegUtilTest extends MockitoTestCase {

    private ProdRegUtil prodRegUtil;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        prodRegUtil = new ProdRegUtil();
    }

    public void testReturnTrueForValidDate() throws Exception {
        assertTrue(ProdRegUtil.isValidDate("2016-03-22"));
    }

    public void testReturnFalseForInValidDate() throws Exception {
        assertFalse(ProdRegUtil.isValidDate("1998-03-22"));
        assertFalse(ProdRegUtil.isValidDate("2098-05-22"));
    }

    public void testIsFutureDate() {
        String date = "2098-05-22";
        assertTrue(ProdRegUtil.isFutureDate(date));
        String date1 = "2016-05-16";
        assertFalse(ProdRegUtil.isFutureDate(date1));
    }
}