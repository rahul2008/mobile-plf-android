package com.philips.platform.appinfra.servicediscovery.model;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

/**
 * ServiceDiscovery Service Test class.
 */
public class ServiceDiscoveryServiceTest extends AppInfraInstrumentation {

    private ServiceDiscoveryService mServiceDiscoveyService = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
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

    public void testInit() {
        mServiceDiscoveyService.init("TestLocal", "TestConfig");
        assertSame("TestLocal", mServiceDiscoveyService.getLocale());
        assertSame("TestConfig", mServiceDiscoveyService.getConfigUrls());
    }

    public void testgetLocale() {
        mServiceDiscoveyService.init("TestLocal", "TestConfig");
        assertNotNull(mServiceDiscoveyService.getLocale());

    }

    public void testgetConfigUrls() {
        mServiceDiscoveyService.init("TestLocal", "TestConfig");
        assertNotNull(mServiceDiscoveyService.getConfigUrls());
    }
}
