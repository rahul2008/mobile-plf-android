package com.philips.cdp.model;

import junit.framework.TestCase;

import org.mockito.Mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegResponseTest extends TestCase {

    ProdRegResponse prodRegResponse;
    @Mock
    ProdRegData data;

    @Override
    public void setUp() throws Exception {
        prodRegResponse = new ProdRegResponse();
    }

    public void testGetData() throws Exception {
        prodRegResponse.setData(data);
        assertEquals(data, prodRegResponse.getData());

    }
}