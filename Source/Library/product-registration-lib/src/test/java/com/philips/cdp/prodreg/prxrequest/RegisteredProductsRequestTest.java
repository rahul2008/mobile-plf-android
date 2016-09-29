/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.prodreg.prxrequest;

import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.registration.configuration.Configuration;

import junit.framework.TestCase;

import org.junit.Test;

public class RegisteredProductsRequestTest extends TestCase {
    private RegisteredProductsRequest registeredProductsRequest;
    private String mCtn = "HC5410/83";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        registeredProductsRequest = new RegisteredProductsRequest();
    }

    @Test
    public void testGetServerInfo() throws Exception {
        registeredProductsRequest = new RegisteredProductsRequest() {
            @Override
            protected String getRegistrationEnvironment() {
                return Configuration.PRODUCTION.name();
            }
        };
        final String serverInfo = registeredProductsRequest.getServerInfo();
        assertEquals(serverInfo, "https://www.philips.com/prx/registration.registeredProducts");
    }

    @Test
    public void testGetRequestType() throws Exception {
        int mIntType = registeredProductsRequest.getRequestType();
        assertEquals(RequestType.GET.getValue(), mIntType);
    }

    @Test
    public void testGetRequestTimeOut() throws Exception {
        assertEquals(registeredProductsRequest.getRequestTimeOut(), 30000);
    }
}