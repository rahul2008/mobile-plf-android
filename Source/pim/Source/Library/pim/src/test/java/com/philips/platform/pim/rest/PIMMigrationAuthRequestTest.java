package com.philips.platform.pim.rest;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

@RunWith(PowerMockRunner.class)
public class PIMMigrationAuthRequestTest extends TestCase {

    private PIMMigrationAuthRequest pimMigrationAuthRequest;

    private final String idMigrationAuthReq = "https://stg.api.eu-west-1.philips.com/consumerIdentityService/identityAssertions/";

    @Before
    public void setUp() throws Exception {
        super.setUp();

        pimMigrationAuthRequest = new PIMMigrationAuthRequest(idMigrationAuthReq);
    }

    @Test
    public void testGetUrl() {
        String url = pimMigrationAuthRequest.getUrl();
        assertEquals(url, idMigrationAuthReq);
    }

    @Test
    public void testGetHeader() {
        Map<String, String> header = pimMigrationAuthRequest.getHeader();
        int size = header.size();
        assertEquals(size, 1);
    }

    @Test
    public void testGetBody() {
        String body = pimMigrationAuthRequest.getBody();
        assertNull(body);
    }

    @Test
    public void getGetMethodType() {
        int methodType = pimMigrationAuthRequest.getMethodType();
        assertEquals(methodType, PIMRequest.Method.GET);
    }

    @After
    public void tearDown() throws Exception {
    }
}