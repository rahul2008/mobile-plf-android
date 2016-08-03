package com.philips.cdp.prodreg.error;

import android.test.InstrumentationTestCase;

import com.philips.cdp.prodreg.MockitoTestCase;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegErrorMapTest extends MockitoTestCase {

    ProdRegErrorMap prodRegErrorMap;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        prodRegErrorMap = new ProdRegErrorMap("title", "description");
    }

    public void testMapping() {
        assertEquals(prodRegErrorMap.getDescription(), "description");
        assertEquals(prodRegErrorMap.getTitle(), "title");
    }
}