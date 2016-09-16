package com.philips.cdp.prodreg.prxrequest;

import android.test.InstrumentationTestCase;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.request.RequestType;
import com.philips.cdp.registration.configuration.Configuration;

import org.junit.Test;
import org.mockito.Mock;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class ProductMetadataRequestTest extends InstrumentationTestCase {

    ProductMetadataRequest productMetadataRequest;
    @Mock
    String mCtn = "HD8967/01";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        productMetadataRequest = new ProductMetadataRequest(mCtn);
    }

    public void testGetRequestType() throws Exception {
        int mIntType = productMetadataRequest.getRequestType();
        assertEquals(RequestType.GET.getValue(), mIntType);
    }

    public void testGetServerInfo() {
        productMetadataRequest = new ProductMetadataRequest(mCtn) {
            @Override
            protected String getRegistrationEnvironment() {
                return Configuration.PRODUCTION.name();
            }
        };
        final String serverInfo = productMetadataRequest.getServerInfo();
        assertEquals(serverInfo, "https://www.philips.com/prx/registration/");
    }

    public void testRequestUrl() {
        productMetadataRequest = new ProductMetadataRequest(mCtn) {
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
        final String requestUrl = productMetadataRequest.getRequestUrl();
        assertEquals(requestUrl, "https://www.philips.com/prx/registration/B2C/en_GB/CONSUMER/products/null.metadata");
    }

    @Test
    public void testGetRequestTimeOut() throws Exception {
        assertEquals(productMetadataRequest.getRequestTimeOut(), 30000);
    }
}