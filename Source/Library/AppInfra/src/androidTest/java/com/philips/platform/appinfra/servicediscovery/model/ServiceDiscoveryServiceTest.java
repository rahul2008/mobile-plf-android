package com.philips.platform.appinfra.servicediscovery.model;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

/**
 * Created by 310238655 on 8/16/2016.
 */
public class ServiceDiscoveryServiceTest extends AppInfraInstrumentation {

    private Context context;
    private ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    private ServiceDiscoveryManager mServiceDiscoveryManager = null;
    private ServiceDiscoveryService mServiceDiscoveyService = null;
    private AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
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
