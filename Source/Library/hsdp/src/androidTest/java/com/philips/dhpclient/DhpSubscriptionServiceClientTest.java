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
    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl","dhpApplicationName","signingKey","signingSecret");
    mDhpSubscriptionServiceClient = new DhpSubscriptionServiceClient(dhpApiClientConfiguration);
    }

    @Test
    public void testDhpSubscriptionServiceClient(){
        mDhpSubscriptionServiceClient.subscribe("dhpUserId","accessToken");
        mDhpSubscriptionServiceClient.closeAccount("dhpUserId",true,"accessToken");
        assertNotNull(mDhpSubscriptionServiceClient);

    }
}