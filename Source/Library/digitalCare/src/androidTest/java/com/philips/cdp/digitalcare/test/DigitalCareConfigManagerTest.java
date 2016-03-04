/*
package com.philips.cdp.digitalcare.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.activity.DigitalCareActivity;

public class DigitalCareConfigManagerTest extends
        ActivityInstrumentationTestCase2<DigitalCareActivity> {

    private DigitalCareConfigManager mConfigManager = null;

    public DigitalCareConfigManagerTest() {
        super(DigitalCareActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mConfigManager = DigitalCareConfigManager.getInstance();
    }

    @SmallTest
    public void testIsConfigurationManagerIsMocked() {
        boolean validate = false;
        String received = mConfigManager.getClass().getSimpleName();
        if (received.equalsIgnoreCase("DigitalCareConfigManager_Proxy"))
            validate = true;
        assertTrue(validate);
    }

}
*/
