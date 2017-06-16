package com.philips.platform.appinfra;

import android.content.Context;

/**
 * AppInfra Test class.
 */

public class AppInfraTest extends MockitoTestCase {



    AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = getInstrumentation().getContext();
        assertNotNull(context);

        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
    }

    public void testComponentVersion(){
        assertNotNull(mAppInfra.getVersion());
        assertEquals(BuildConfig.VERSION_NAME,mAppInfra.getVersion()); // BuildConfig is dynamically created version file
    }

    public void testComponentID(){
        assertNotNull(mAppInfra.getComponentId());
        String appInfraID = "ail:";
        assertEquals(appInfraID,mAppInfra.getComponentId()); // ail = AppInfra Language
    }
}
