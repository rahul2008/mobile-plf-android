package com.philips.cdp.handler;

import com.philips.cdp.MockitoTestCase;

import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductTest extends MockitoTestCase {

    Product product;

    public void testUser() throws Exception {
        super.setUp();
        product = new Product();
    }

    @Test
    public void testProductMetadata() {

    }
}