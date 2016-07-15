package com.philips.platform.appinfra.appidentity;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

/**
 * Created by 310238114 on 6/22/2016.
 */
public class AppIdentityTest extends MockitoTestCase {

    AppIdentityInterface mAppIdentityManager=null;
    // Context context = Mockito.mock(Context.class);

    private Context context;
    AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra =  new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mAppIdentityManager = mAppInfra.getAppIdentity();
        assertNotNull(mAppIdentityManager);
    }
    public void testHappyPath()throws Exception {
        assertNotNull(mAppIdentityManager.getLocalizedAppName());
        assertNotNull(mAppIdentityManager.getAppName());
        assertNotNull(mAppIdentityManager.getAppState());
        assertNotNull(mAppIdentityManager.getAppVersion());
        assertNotNull(mAppIdentityManager.getMicrositeId());
        assertNotNull(mAppIdentityManager.getSector());
    }
}
