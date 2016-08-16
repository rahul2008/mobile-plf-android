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
    ServiceDiscoveyService mServiceDiscoveyService = null;
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
        mServiceDiscoveyService = new ServiceDiscoveyService();
        assertNotNull(mServiceDiscoveryInterface);
        assertNotNull(mServiceDiscoveryManager);
        assertNotNull(mServiceDiscoveyService);
    }

    public void testInit(){
        mServiceDiscoveyService.init("TestLocal","TestConfig");
        assertSame("TestLocal",mServiceDiscoveyService.locale);
        assertSame("TestConfig",mServiceDiscoveyService.configUrl);
    }

    public void testgetLocale(){
        mServiceDiscoveyService.init("TestLocal","TestConfig");
       assertNotNull(mServiceDiscoveyService.getLocale());

    }
    public void testgetConfigUrls(){
        mServiceDiscoveyService.init("TestLocal","TestConfig");
        assertNotNull(mServiceDiscoveyService.getConfigUrls());
    }


}
