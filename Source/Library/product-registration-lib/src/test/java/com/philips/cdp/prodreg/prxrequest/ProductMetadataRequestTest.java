package com.philips.cdp.prodreg.prxrequest;

import android.test.InstrumentationTestCase;

import com.philips.cdp.prxclient.request.RequestType;

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
}