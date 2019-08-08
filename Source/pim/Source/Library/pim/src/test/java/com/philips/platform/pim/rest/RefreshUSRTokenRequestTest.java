package com.philips.platform.pim.rest;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

@RunWith(PowerMockRunner.class)
public class RefreshUSRTokenRequestTest extends TestCase {

    private RefreshUSRTokenRequest refreshUSRTokenRequest;

    private final String refreshTokenUrl = "refreshUrl"; //TODO:Shashi, Update url later;
    private String body = "body";

    @Before
    public void setUp() throws Exception {
        super.setUp();

        refreshUSRTokenRequest = new RefreshUSRTokenRequest(refreshTokenUrl, body);
    }

    @Test
    public void testGetUrl() {
        String url = refreshUSRTokenRequest.getUrl();
        assertEquals(url, refreshTokenUrl);
    }

    @Test
    public void testGetHeader() {
        Map<String, String> header = refreshUSRTokenRequest.getHeader();
        int size = header.size();
        assertEquals(size, 2);
    }

    @Test
    public void testGetBody() {
        String body = refreshUSRTokenRequest.getBody();
        assertNotNull(body);
    }

    @Test
    public void testGetMethodType() {
        int methodType = refreshUSRTokenRequest.getMethodType();
        assertEquals(methodType, PIMRequest.Method.POST);
    }

    @After
    public void tearDown() throws Exception {
    }
}