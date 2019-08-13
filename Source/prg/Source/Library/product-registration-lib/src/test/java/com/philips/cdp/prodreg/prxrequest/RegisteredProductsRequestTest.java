/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.prodreg.prxrequest;

import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.request.RequestType;

import junit.framework.TestCase;

import org.junit.Test;

public class RegisteredProductsRequestTest extends TestCase {
    private RegisteredProductsRequest registeredProductsRequest;
    String mCtn = "HC5410/83", mSerialNumber = "1344";
    PrxConstants.Sector sector;
    PrxConstants.Catalog catalog;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sector = PrxConstants.Sector.B2C;
        catalog = PrxConstants.Catalog.CONSUMER;
        registeredProductsRequest = new RegisteredProductsRequest(mCtn, mSerialNumber,sector,catalog, true);
    }

//    @Test
//    public void testGetServerInfo() throws Exception {
//        registeredProductsRequest = new RegisteredProductsRequest() {
//            @Override
//            protected String getRegistrationEnvironment() {
//                return Configuration.PRODUCTION.name();
//            }
//        };
//        final String serverInfo = registeredProductsRequest.getServerInfo();
//        assertEquals(serverInfo, "https://www.philips.com/prx/registration.registeredProducts");
//    }

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