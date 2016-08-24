package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;

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
    RequestManager mRequestManager = null;
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
        mRequestManager = new RequestManager(context, mAppInfra);
        assertNotNull(mServiceDiscoveryInterface);
        assertNotNull(mServiceDiscoveryManager);
        assertNotNull(mRequestManager);
    }

    public void testRequestManager() {

        RequestManager mRequestManagerTest = new RequestManager(context, mAppInfra);
        assertNotSame(mRequestManager, mRequestManagerTest);
    }

    public void testexecute() {
        mRequestManager.execute("https://acc.philips.com/api/v1/discovery/b2c/77000?locale=en_US&tags=apps%2b%2benv%2bstage&country=IN", new ServiceDiscoveryInterface.OnRefreshListener() {
            @Override
            public void onSuccess() {

                assertNotNull(mRequestManager.mcountry);
                if(mRequestManager.mServiceDiscovery == null){
                    assertNull(mRequestManager.mServiceDiscovery);
                }else{
                    assertNotNull(mRequestManager.mServiceDiscovery);
                }

            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        });


    }

    public void testexecuteNegetivePath() {
        mRequestManager.execute("https://acc", new ServiceDiscoveryInterface.OnRefreshListener() {
            @Override
            public void onSuccess() {

                assertNotNull(mRequestManager.mcountry);
                if(mRequestManager.mServiceDiscovery == null){
                    assertNull(mRequestManager.mServiceDiscovery);
                }else{
                    assertNotNull(mRequestManager.mServiceDiscovery);
                }

            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                assertNotNull(error);
                assertNotNull(message);
            }
        });


    }
}
