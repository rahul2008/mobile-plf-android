package com.philips.platform.appinfra.config;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

/**
 * Created by 310238114 on 8/2/2016.
 */
public class ConfigTest extends MockitoTestCase {

    ConfigInterface mConfigInterface = null;

    private Context context;
    private AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        // mAppInfra = AppInfraSingleton.getInstance();
        mConfigInterface = mAppInfra.getConfigInterface();
        assertNotNull(mConfigInterface);

    }

    public void testGetPropertyForKey() throws Exception {
        ConfigInterface.ConfigError configError = new ConfigInterface.ConfigError();
        assertNull(mConfigInterface.getPropertyForKey("", "", configError));
        assertNull(mConfigInterface.getPropertyForKey(null, null, configError));
        mConfigInterface.getPropertyForKey("randomGroupKey", "value", configError);
      //  assertNotNull(mConfigInterface.getPropertyForKey("UR", "Testing", configError));
        //assertEquals(ConfigInterface.ConfigError.ConfigErrorEnum.GroupNotExists, configError.getErrorCode().toString());
    }


    public void testSetPropertyForKey() throws Exception {
        ConfigInterface.ConfigError configError = new ConfigInterface.ConfigError();
        assertFalse(mConfigInterface.setPropertyForKey("", "", "", configError));
        assertFalse(mConfigInterface.setPropertyForKey("null", "null", "null", configError));
        assertFalse(mConfigInterface.setPropertyForKey("MI", "", "test", configError));
      //  assertTrue(mConfigInterface.setPropertyForKey("UR", "Testing", "newValue", configError));
    }

}
