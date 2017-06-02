package com.philips.platform.appinfra;

import android.content.Context;

/**
 * Created by philips on 3/13/17.
 */

public class AppInfraTest extends MockitoTestCase {



    AppInfra mAppInfra;
    private Context context;
    private final String AppInfraID = "ail:"; // Please refer http://devportal.spssvr1.htce.nl.philips.com/assets/

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
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
        assertEquals(AppInfraID,mAppInfra.getComponentId()); // ail = AppInfra Language
    }
}
