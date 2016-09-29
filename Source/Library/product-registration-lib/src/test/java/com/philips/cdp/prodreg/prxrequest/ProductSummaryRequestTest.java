package com.philips.cdp.prodreg.prxrequest;

import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.registration.configuration.Configuration;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductSummaryRequestTest extends TestCase {

    private ProductSummaryRequest productSummaryRequest;
    private String mCtn = "HC5410/83";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        productSummaryRequest = new ProductSummaryRequest(mCtn);
    }

    @Test
    public void testGetServerInfo() throws Exception {
        productSummaryRequest = new ProductSummaryRequest(mCtn) {
            @Override
            protected String getRegistrationEnvironment() {
                return Configuration.PRODUCTION.name();
            }
        };
        final String serverInfo = productSummaryRequest.getServerInfo();
        assertEquals(serverInfo, "https://www.philips.com/prx/product/");
    }

    @Test
    public void testGetRequestType() throws Exception {
        int mIntType = productSummaryRequest.getRequestType();
        assertEquals(RequestType.GET.getValue(), mIntType);
    }

    @Test
    public void testGetRequestTimeOut() throws Exception {
        assertEquals(productSummaryRequest.getRequestTimeOut(), 30000);
    }
}