package com.philips.cdp.registration.configuration;

import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class RegistrationConfigurationTest extends InstrumentationTestCase {
    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        try {
            AppInfraSingleton.setInstance(new AppInfra.Builder().build(getInstrumentation().getContext()));
        } catch (Exception e) {

        }
        RegistrationHelper.getInstance().setAppInfraInstance(AppInfraSingleton.getInstance());
        RLog.initForTesting(getInstrumentation().getContext());
    }

    @Test
    public void testConfiguration() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        Object obj = RegistrationHelper.getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." +
                        Configuration.STAGING.getValue(), "UR", configError);
        assertEquals(null,obj);

        RegistrationConfiguration.getInstance().setRegistrationClientId(Configuration.STAGING, "8kaxdrpvkwyr7pnp987amu4aqb4wmnte");
        assertEquals(null, RegistrationConfiguration.getInstance().getRegistrationClientId(Configuration.STAGING));

        RegistrationConfiguration.getInstance().setMicrositeId("77000");
        assertEquals(null, RegistrationConfiguration.getInstance().getMicrositeId());

        RegistrationConfiguration.getInstance().setCampainId("abc");
        assertEquals(null, RegistrationConfiguration.getInstance().getCampaignId());

        RegistrationConfiguration.getInstance().setRegistrationEnvironment(Configuration.STAGING);
        assertEquals(null, RegistrationConfiguration.getInstance().getRegistrationEnvironment());

        RegistrationConfiguration.getInstance().setEmailVerificationRequired(true);
        RegistrationConfiguration.getInstance().isEmailVerificationRequired();
        assertTrue(true);

        RegistrationConfiguration.getInstance().setTermsAndConditionsAcceptanceRequired(true);
        RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired();
        assertTrue(true);

        HashMap<String, String> ageMap = new HashMap<>();
        ageMap.put("NL", "16");
        ageMap.put("GB", "16");
        ageMap.put("default", "16");
        RegistrationConfiguration.getInstance().setMinAgeLimit(ageMap);
        assertEquals(0,RegistrationConfiguration.getInstance().getMinAgeLimitByCountry("NL"));


        //Configure Signin Providers
        HashMap<String, ArrayList<String>> providers = new HashMap<String, ArrayList<String>>();
        ArrayList<String> values1 = new ArrayList<String>();
        ArrayList<String> values2 = new ArrayList<String>();
        ArrayList<String> values3 = new ArrayList<String>();
        values1.add("facebook");
        values1.add("googleplus");
        values1.add("sinaweibo");
        values1.add("qq");

        values2.add("facebook");
        values2.add("googleplus");
        values2.add("sinaweibo");
        values2.add("qq");

        values3.add("facebook");
        values3.add("googleplus");
        values3.add("sinaweibo");
        values3.add("qq");

        providers.put("NL", values1);
        providers.put("US", values2);
        providers.put("default", values3);
        RegistrationConfiguration.getInstance().setProviders(providers);

        assertEquals(null,RegistrationConfiguration.getInstance().getProvidersForCountry("hh"));

        HSDPInfo hsdpInfo;
        hsdpInfo = new HSDPInfo();
        hsdpInfo.setApplicationName("uGrow");
        hsdpInfo.setSharedId("e95f5e71-c3c0-4b52-8b12-ec297d8ae960");
        hsdpInfo.setSecreteId("e33a4d97-6ada-491f-84e4-a2f7006625e2");
        hsdpInfo.setBaseURL("https://user-registration-assembly-staging.eu-west.philips-healthsuite.com");
        RegistrationConfiguration.getInstance().setHSDPInfo(Configuration.EVALUATION,hsdpInfo);

        assertEquals("uGrow",hsdpInfo.getApplicationName());
        assertEquals("e95f5e71-c3c0-4b52-8b12-ec297d8ae960",hsdpInfo.getSharedId());
        assertEquals("e33a4d97-6ada-491f-84e4-a2f7006625e2",hsdpInfo.getSecreteId());
        assertEquals("https://user-registration-assembly-staging.eu-west.philips-healthsuite.com",hsdpInfo.getBaseURL());
        assertEquals(null,RegistrationConfiguration.getInstance().getHSDPInfo(Configuration.EVALUATION));


    }

    @Test
    public void testRegistrationConfiguration() {

        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
        assertEquals(RegistrationFunction.Registration, RegistrationConfiguration.getInstance().getPrioritisedFunction());
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.SignIn);
        assertEquals(RegistrationFunction.SignIn, RegistrationConfiguration.getInstance().getPrioritisedFunction());

        HSDPInfo hsdpInfo = RegistrationConfiguration.getInstance().getHSDPInfo(Configuration.EVALUATION);
        assertEquals(null,hsdpInfo);

    }
}