package com.philips.cdp.prodreg.prxrequest;

import android.test.InstrumentationTestCase;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.registration.configuration.Configuration;

import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductSummaryRequestTest extends InstrumentationTestCase {

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
    public void testGetRequestUrl() throws Exception {
        productSummaryRequest = new ProductSummaryRequest(mCtn) {
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
        final String requestUrl = productSummaryRequest.getRequestUrl();
        assertEquals(requestUrl, "https://www.philips.com/prx/registration/B2C/en_GB/CONSUMER/products/null.summary");
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