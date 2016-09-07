package com.philips.dhpclient;

import android.test.InstrumentationTestCase;

import com.philips.dhpclient.response.DhpResponse;
import com.philips.dhpclient.response.DhpResponseVerifier;

import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class DhpApiClientTest extends InstrumentationTestCase{
    DhpApiClient mDhpApiClient;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl","dhpApplicationName","signingKey","signingSecret");
                mDhpApiClient = new DhpApiClient(dhpApiClientConfiguration);
    }

    public void testDhpApiClient(){
        assertNotNull(mDhpApiClient);
//        mDhpApiClient.UTCDatetimeAsString();
        DhpResponseVerifier dhpResponseVerifier = new DhpResponseVerifier() {
            @Override
            public void verify(DhpResponse dhpResponse) {

            }
        };
        mDhpApiClient.setResponseVerifier(dhpResponseVerifier);
        assertNotNull(mDhpApiClient);


    }
}