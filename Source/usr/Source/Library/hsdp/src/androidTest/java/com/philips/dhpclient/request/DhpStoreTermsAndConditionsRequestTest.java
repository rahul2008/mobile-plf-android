
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient.request;

import android.support.multidex.MultiDex;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class DhpStoreTermsAndConditionsRequestTest {
    private DhpStoreTermsAndConditionsRequest mDhpStoreTermsAndConditionsRequest;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        mDhpStoreTermsAndConditionsRequest = new DhpStoreTermsAndConditionsRequest("applicationName","documentId","documentVersion","countryCode","consentCode");
    }

    @Test
    public void testDhpStoreTermsAndContionsRequest(){
        assertNotNull(mDhpStoreTermsAndConditionsRequest);
    }
}

