package com.philips.platform.appinfra.servicediscovery.model;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

/**
 * Created by 310238655 on 8/16/2016.
 */
public class ServiceDiscoveryServiceTest extends MockitoTestCase {

    private Context context;
    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    ServiceDiscoveryManager mServiceDiscoveryManager = null;
    ServiceDiscoveryService mServiceDiscoveryService = null;
    AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        mServiceDiscoveryManager = new ServiceDiscoveryManager(mAppInfra);
        mServiceDiscoveryService = new ServiceDiscoveryService();
        assertNotNull(mServiceDiscoveryInterface);
        assertNotNull(mServiceDiscoveryManager);
        assertNotNull(mServiceDiscoveryService);
    }

    public void testInit(){
        mServiceDiscoveryService.init("TestLocal","TestConfig");
        assertSame("TestLocal", mServiceDiscoveryService.locale);
        assertSame("TestConfig", mServiceDiscoveryService.configUrl);
    }

    public void testgetLocale(){
        mServiceDiscoveryService.init("TestLocal","TestConfig");
       assertNotNull(mServiceDiscoveryService.getLocale());

    }
    public void testgetConfigUrls(){
        mServiceDiscoveryService.init("TestLocal","TestConfig");
        assertNotNull(mServiceDiscoveryService.getConfigUrls());
    }


}
