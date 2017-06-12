
package com.philips.dhpclient.request;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class DhpStoreTermsAndConditionsRequestTest extends InstrumentationTestCase{
    DhpStoreTermsAndConditionsRequest mDhpStoreTermsAndConditionsRequest;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        mDhpStoreTermsAndConditionsRequest = new DhpStoreTermsAndConditionsRequest("applicationName","documentId","documentVersion","countryCode","consentCode");
    }
    public void testDhpStoreTermsAndContionsRequest(){

        assertNotNull(mDhpStoreTermsAndConditionsRequest);

    }
}

