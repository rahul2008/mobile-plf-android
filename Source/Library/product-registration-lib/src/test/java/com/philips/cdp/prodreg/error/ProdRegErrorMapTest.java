package com.philips.cdp.prodreg.error;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegErrorMapTest extends TestCase {

    ProdRegErrorMap prodRegErrorMap;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        prodRegErrorMap = new ProdRegErrorMap("title", "description");
    }

    @Test
    public void testMapping() {
        assertEquals(prodRegErrorMap.getDescription(), "description");
        assertEquals(prodRegErrorMap.getTitle(), "title");
    }
}