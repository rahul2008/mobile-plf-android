package com.philips.platform.pim.rest;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class PIMRequestTest extends TestCase {

    private PIMRequest pimRequest;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetBody() {
        //pimRequest = new PIMRequest(PIMRequest.Method.GET,)
    }
}