package com.philips.cdp.model;

import junit.framework.TestCase;

import org.mockito.Mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegRegisteredDataResponseTest extends TestCase {

    ProdRegRegisteredDataResponse prodRegRegisteredDataResponse;
    @Mock
    ProdRegRegisteredResults[] results;


    @Override
    public void setUp() throws Exception {
        prodRegRegisteredDataResponse = new ProdRegRegisteredDataResponse();
    }

    public void testSetResults() throws Exception {
        prodRegRegisteredDataResponse.setResults(results);
    }

    public void testSetResult_count() throws Exception {
        prodRegRegisteredDataResponse.setResult_count("2");
    }

    public void testGetResults() throws Exception {
        prodRegRegisteredDataResponse.setResults(results);
        assertEquals(results, prodRegRegisteredDataResponse.getResults());
    }


    public void testGetResult_count() throws Exception {
        prodRegRegisteredDataResponse.setResult_count("2");
        assertEquals("2", prodRegRegisteredDataResponse.getResult_count());

    }


}