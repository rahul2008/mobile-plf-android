package com.philips.dhpclient;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/21/2016.
 */
public class DhpSubscriptionServiceClientTest extends InstrumentationTestCase{

    DhpSubscriptionServiceClient mDhpSubscriptionServiceClient;
    DhpSubscriptionServiceClient.DhpTermsAndConditionsResponse mDhpTermsAndConditionsResponse;
    DhpSubscriptionServiceClient.DhpTermsAndConditionsResponse mDhpTermsAndConditionsResponse1;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl","dhpApplicationName","signingKey","signingSecret");
    mDhpSubscriptionServiceClient = new DhpSubscriptionServiceClient(dhpApiClientConfiguration);
        mDhpTermsAndConditionsResponse = new DhpSubscriptionServiceClient.DhpTermsAndConditionsResponse("responseCode","acceptedTermsVersion");
        mDhpTermsAndConditionsResponse1 = new DhpSubscriptionServiceClient.DhpTermsAndConditionsResponse("responseCode","acceptedTermsVersion");
    }

    @Test
    public void testDhpSubscriptionServiceClient(){
        mDhpSubscriptionServiceClient.subscribe("dhpUserId","accessToken");
        mDhpSubscriptionServiceClient.closeAccount("dhpUserId",true,"accessToken");
        assertNotNull(mDhpSubscriptionServiceClient);

    }
    @Test
    public void testDhpTermsAndConditonsResponse(){
        assertNotNull(mDhpTermsAndConditionsResponse);
        assertTrue(mDhpTermsAndConditionsResponse.equals(mDhpTermsAndConditionsResponse));
        assertTrue(mDhpTermsAndConditionsResponse.equals(mDhpTermsAndConditionsResponse1));
        assertFalse(mDhpTermsAndConditionsResponse.equals(mDhpSubscriptionServiceClient));
        assertFalse(mDhpTermsAndConditionsResponse.equals(null));
        assertNotNull(mDhpTermsAndConditionsResponse.hashCode());
        assertNotNull(mDhpTermsAndConditionsResponse.toString());
    }
}