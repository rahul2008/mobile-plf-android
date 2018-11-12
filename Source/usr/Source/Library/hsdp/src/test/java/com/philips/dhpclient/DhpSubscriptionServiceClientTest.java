
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by 310243576 on 8/21/2016.
 */
public class DhpSubscriptionServiceClientTest {

    private DhpSubscriptionServiceClient mDhpSubscriptionServiceClient;
    private DhpSubscriptionServiceClient.DhpTermsAndConditionsResponse mDhpTermsAndConditionsResponse;
    private DhpSubscriptionServiceClient.DhpTermsAndConditionsResponse mDhpTermsAndConditionsResponse1;

    @Before
    public void setUp() throws Exception {
        DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl", "dhpApplicationName", "signingKey", "signingSecret");
        mDhpSubscriptionServiceClient = new DhpSubscriptionServiceClient(dhpApiClientConfiguration);
        mDhpTermsAndConditionsResponse = new DhpSubscriptionServiceClient.DhpTermsAndConditionsResponse("responseCode", "acceptedTermsVersion");
        mDhpTermsAndConditionsResponse1 = new DhpSubscriptionServiceClient.DhpTermsAndConditionsResponse("responseCode", "acceptedTermsVersion");
    }

    @Test
    public void testDhpSubscriptionServiceClient() {
        mDhpSubscriptionServiceClient.subscribe("dhpUserId", "accessToken");
        mDhpSubscriptionServiceClient.closeAccount("dhpUserId", true, "accessToken");
        assertNotNull(mDhpSubscriptionServiceClient);
    }

    @Test
    public void testDhpTermsAndConditonsResponse() {
        assertNotNull(mDhpTermsAndConditionsResponse);
        assertEquals(mDhpTermsAndConditionsResponse, mDhpTermsAndConditionsResponse);
        assertEquals(mDhpTermsAndConditionsResponse, mDhpTermsAndConditionsResponse1);
        assertNotEquals(mDhpTermsAndConditionsResponse, mDhpSubscriptionServiceClient);
        assertNotNull(mDhpTermsAndConditionsResponse.toString());
    }

    @Test
    public void testGetLastAcceptedTermsAndConditions() throws Exception {
        Method method = null;
        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("200", "\\.sample");

        method = DhpSubscriptionServiceClient.class.getDeclaredMethod("getLastAcceptedTermsAndConditions", Map.class);
        method.setAccessible(true);
        method.invoke(mDhpSubscriptionServiceClient, responseMap);
    }
}
