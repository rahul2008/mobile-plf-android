package com.philips.cdp.prodreg.prxrequest;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.prxclient.RequestType;

import org.mockito.Mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductMetadataRequestTest extends InstrumentationTestCase {

    ProductMetadataRequest productMetadataRequest;
    Context context;
    @Mock
    String mCtn = "HD8967/01";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        productMetadataRequest = new ProductMetadataRequest(mCtn);
        context = getInstrumentation().getContext();
    }

    public void testGetServerInfo() throws Exception {
        String mServerInfo = productMetadataRequest.getServerInfo();
        assertEquals("https://dev.philips.co.uk/prx/registration/", mServerInfo);
    }

    public void testGetRequestType() throws Exception {
        int mIntType = productMetadataRequest.getRequestType();
        assertEquals(RequestType.GET.getValue(), mIntType);
    }
}