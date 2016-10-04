package com.philips.cdp.registration.configuration;

import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class RegistrationConfigurationTest extends InstrumentationTestCase {

    RegistrationConfiguration registrationConfiguration;
    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        if( RegistrationHelper.getInstance().getAppInfraInstance()==null){
            RegistrationHelper.getInstance().setAppInfraInstance(new AppInfra.Builder().
                    build(getInstrumentation().getContext()));
        }
        registrationConfiguration = RegistrationConfiguration.getInstance();

        RLog.initForTesting(getInstrumentation().getContext());
    }

    @Test
    public void testConfiguration() {

        assertEquals(null, RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.STAGING));

        assertEquals(null, RegistrationConfiguration.getInstance().getMicrositeId());

        assertEquals(null, RegistrationConfiguration.getInstance().getCampaignId());

        assertEquals(null, RegistrationConfiguration.getInstance().getRegistrationEnvironment());

        RegistrationConfiguration.getInstance().isEmailVerificationRequired();
        assertTrue(true);

        RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired();
        assertTrue(true);

        assertEquals(0,RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("NL"));



        assertEquals(null,RegistrationConfiguration.getInstance().getProvidersForCountry("hh"));

        HSDPInfo hsdpInfo;
        hsdpInfo = new HSDPInfo();
        hsdpInfo.setApplicationName("uGrow");
        hsdpInfo.setSharedId("e95f5e71-c3c0-4b52-8b12-ec297d8ae960");
        hsdpInfo.setSecreteId("e33a4d97-6ada-491f-84e4-a2f7006625e2");
        hsdpInfo.setBaseURL("https://user-registration-assembly-staging.eu-west.philips-healthsuite.com");

        assertEquals("uGrow",hsdpInfo.getApplicationName());
        assertEquals("e95f5e71-c3c0-4b52-8b12-ec297d8ae960",hsdpInfo.getSharedId());
        assertEquals("e33a4d97-6ada-491f-84e4-a2f7006625e2",hsdpInfo.getSecreteId());
        assertEquals("https://user-registration-assembly-staging.eu-west.philips-healthsuite.com",hsdpInfo.getBaseURL());
        assertEquals(null,RegistrationConfiguration.getInstance().getHSDPInfo());


    }

    @Test
    public void testRegistrationConfiguration() {

        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
        assertEquals(RegistrationFunction.Registration, RegistrationConfiguration.getInstance().getPrioritisedFunction());
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.SignIn);
        assertEquals(RegistrationFunction.SignIn, RegistrationConfiguration.getInstance().getPrioritisedFunction());
    }

    @Test
    public void testBuildException(){
        Method method = null;
        String s= "";
        HSDPInfo hsdpInfo = new HSDPInfo();
        try {
            method = RegistrationConfiguration.class.getDeclaredMethod("buildException", HSDPInfo.class);
            method.setAccessible(true);
            method.invoke(registrationConfiguration, hsdpInfo);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}