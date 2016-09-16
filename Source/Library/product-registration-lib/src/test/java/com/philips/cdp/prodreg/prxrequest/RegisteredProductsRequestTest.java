/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.prodreg.prxrequest;

import android.test.InstrumentationTestCase;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.registration.configuration.Configuration;

import org.junit.Test;

public class RegisteredProductsRequestTest extends InstrumentationTestCase {
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
    public void testGetRequestUrl() throws Exception {
        registeredProductsRequest = new RegisteredProductsRequest() {
            @Override
            protected String getRegistrationEnvironment() {
                return Configuration.PRODUCTION.name();
            }

            @Override
            public Sector getSector() {
                return Sector.B2C;
            }

            @Override
            public String getLocaleMatchResult() {
                return "en_GB";
            }

            @Override
            public Catalog getCatalog() {
                return Catalog.CONSUMER;
            }
        };
        final String requestUrl = registeredProductsRequest.getRequestUrl();
        assertEquals(requestUrl, "https://www.philips.com/prx/registration.registeredProducts");
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