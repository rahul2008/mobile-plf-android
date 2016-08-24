package com.philips.dhpclient.request;

import android.test.InstrumentationTestCase;

import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class DhpStoreTermsAndConditionsRequestTest extends InstrumentationTestCase{
    DhpStoreTermsAndConditionsRequest mDhpStoreTermsAndConditionsRequest;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mDhpStoreTermsAndConditionsRequest = new DhpStoreTermsAndConditionsRequest("applicationName","documentId","documentVersion","countryCode","consentCode");
    }
    public void testDhpStoreTermsAndContionsRequest(){

        assertNotNull(mDhpStoreTermsAndConditionsRequest);

    }
}