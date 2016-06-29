package com.philips.platform.appinfra.appidentity;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.appinfra.securestorage.SecureStorageError;

/**
 * Created by 310238114 on 6/22/2016.
 */
public class AppIdentityTest extends MockitoTestCase {

    AppIdentityManager mAppIdentityManager=null;
    // Context context = Mockito.mock(Context.class);

    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        AppInfra mAppInfra = new AppInfra(context);
        assertNotNull(mAppInfra);
        mAppIdentityManager = new AppIdentityManager(mAppInfra);
        assertNotNull(mAppIdentityManager);
    }
    public void testHappyPath()throws Exception {
        assertNotNull(mAppIdentityManager.getAppLocalizedNAme());
        assertNotNull(mAppIdentityManager.getAppName());
        assertNotNull(mAppIdentityManager.getAppState());
        assertNotNull(mAppIdentityManager.getAppVersion());
        assertNotNull(mAppIdentityManager.getMicrositeId());
        assertNotNull(mAppIdentityManager.getSector());
    }
}
