package com.philips.cdp.model;

import junit.framework.TestCase;

import org.mockito.Mock;

/**
 * Created by 310230979 on 3/24/2016.
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