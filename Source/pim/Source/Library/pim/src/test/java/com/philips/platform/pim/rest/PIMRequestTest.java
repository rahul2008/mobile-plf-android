package com.philips.platform.pim.rest;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

public class PIMRequestTest extends TestCase {

    private PIMRequest pimRequest;

    @Mock
    private Response.Listener<String> mockResponseListener;
    @Mock
    private Response.ErrorListener mockErrorListener;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetBody() throws AuthFailureError {
        Map<String, String> header = new HashMap<>();
        pimRequest = new PIMRequest(PIMRequest.Method.GET, "url", "body", mockResponseListener, mockErrorListener, header);
        byte[] body = pimRequest.getBody();
        assertNotNull(body);
    }
}