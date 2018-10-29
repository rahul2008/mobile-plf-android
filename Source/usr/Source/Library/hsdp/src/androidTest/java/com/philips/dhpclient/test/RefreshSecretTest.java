/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */


package com.philips.dhpclient.test;

import android.support.multidex.MultiDex;

import com.philips.dhpclient.DhpApiClientConfiguration;
import com.philips.dhpclient.DhpAuthenticationManagementClient;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;


/**
 * Created by 310190722 on 9/8/2015.
 */

public class RefreshSecretTest {

    private DhpAuthenticationManagementClient authenticationManagementClient;

    private final DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration(
            "http://ugrow-userregistration15.cloud.pcftest.com",
            "uGrowApp",
            "f129afcc-55f4-11e5-885d-feff819cdc9f",
            "f129b5a8-55f4-11e5-885d-feff819cdc9f");

    @Before
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        authenticationManagementClient = new DhpAuthenticationManagementClient(dhpApiClientConfiguration);
    }


    @Test
    public void testRefreshSecretCreateSignature() {
        assertEquals("YPCh1N0aEs3r4+2uKoNTqBeT/aw=",
                authenticationManagementClient.createRefreshSignature("aa6c3f0dd953bcf11053e00e686af2e0d9b1d05b", "2016-03-28 07:20:31", "3kr6baw3tqbuyg58"));
    }
}

