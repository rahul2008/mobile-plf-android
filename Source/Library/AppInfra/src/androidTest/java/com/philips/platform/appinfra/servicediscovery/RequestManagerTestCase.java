package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;

import com.philips.cdp.prxclient.RequestManager;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;

/**
 * Created by 310238655 on 8/11/2016.
 */
public class RequestManagerTestCase extends MockitoTestCase {

    private Context context;
    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    ServiceDiscoveryManager mServiceDiscoveryManager = null;
    //    RequestManager mRequestManager = null;
    RequestItemManager mRequestItemManager = null;
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
        mRequestItemManager = new RequestItemManager(context, mAppInfra);
        assertNotNull(mServiceDiscoveryInterface);
        assertNotNull(mServiceDiscoveryManager);
        assertNotNull(mRequestItemManager);
    }

    public void testRequestManager() {

        RequestItemManager mRequestManagerTest = new RequestItemManager(context, mAppInfra);
        assertNotSame(mRequestItemManager, mRequestManagerTest);
    }

    public void testexecute() {
        mRequestItemManager.execute("https://acc.philips.com/api/v1/discovery/b2c/77000?locale=en_US&tags=apps%2b%2benv%2bstage&country=IN");


    }

    public void testexecuteNegetivePath() {
        mRequestItemManager.execute("https://acc");


    }
}
