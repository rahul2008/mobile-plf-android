/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appinfra.servicediscovery.model;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * ServiceDiscovery Service Test class.
 */
public class ServiceDiscoveryServiceTest {

    private ServiceDiscoveryService mServiceDiscoveyService = null;

    @Before
    public void setUp() throws Exception {
        Context context = getInstrumentation().getContext();
        assertNotNull(context);
        AppInfra mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        ServiceDiscoveryInterface mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        ServiceDiscoveryManager mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
        mServiceDiscoveyService = new ServiceDiscoveryService();
        assertNotNull(mServiceDiscoveryInterface);
        assertNotNull(mServiceDiscoveryManager);
        assertNotNull(mServiceDiscoveyService);
    }

    @Test
    public void testInit() {
        mServiceDiscoveyService.init("TestLocal", "TestConfig");
        assertSame("TestLocal", mServiceDiscoveyService.getLocale());
        assertSame("TestConfig", mServiceDiscoveyService.getConfigUrls());
    }

    @Test
    public void testgetLocale() {
        mServiceDiscoveyService.init("TestLocal", "TestConfig");
        assertNotNull(mServiceDiscoveyService.getLocale());

    }

    @Test
    public void testgetConfigUrls() {
        mServiceDiscoveyService.init("TestLocal", "TestConfig");
        assertNotNull(mServiceDiscoveyService.getConfigUrls());
    }
}
