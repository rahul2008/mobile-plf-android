package com.philips.cdp.registration.configuration;

import android.support.annotation.NonNull;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class RegistrationConfigurationTest extends InstrumentationTestCase {
    RegistrationConfiguration mRegistrationConfiguration;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mRegistrationConfiguration = mRegistrationConfiguration.getInstance();
    }
    @Test
    public void testRegistrationConfiguration(){

//        AppInfraSingleton.setInstance( new AppInfra.Builder().build(getInstrumentation().getTargetContext()));
//        mRegistrationConfiguration.setRegistrationClientId(Configuration.STAGING,"STAGING");
//        assertEquals("STAGING",mRegistrationConfiguration.getRegistrationClientId(Configuration.STAGING));
//
//        mRegistrationConfiguration.setRegistrationClientId(Configuration.EVALUATION,"EVALUATION");
//        assertEquals("EVALUATION",mRegistrationConfiguration.getRegistrationClientId(Configuration.EVALUATION));
//
//        mRegistrationConfiguration.setRegistrationClientId(Configuration.DEVELOPMENT,"DEVELOPMENT");
//        assertEquals("DEVELOPMENT",mRegistrationConfiguration.getRegistrationClientId(Configuration.DEVELOPMENT));
//
//        mRegistrationConfiguration.setRegistrationClientId(Configuration.TESTING,"TESTING");
//        assertEquals("TESTING",mRegistrationConfiguration.getRegistrationClientId(Configuration.TESTING));
//
//        mRegistrationConfiguration.setRegistrationClientId(Configuration.PRODUCTION,"PRODUCTION");
//        assertEquals("PRODUCTION",mRegistrationConfiguration.getRegistrationClientId(Configuration.PRODUCTION));

        mRegistrationConfiguration.setPrioritisedFunction(RegistrationFunction.Registration);
        assertEquals(RegistrationFunction.Registration,mRegistrationConfiguration.getPrioritisedFunction());
        mRegistrationConfiguration.setPrioritisedFunction(RegistrationFunction.SignIn);
        assertEquals(RegistrationFunction.SignIn,mRegistrationConfiguration.getPrioritisedFunction());

        assertNotNull(mRegistrationConfiguration);


    }
}