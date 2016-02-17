package com.philips.cdp.registration.settings.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationStaticConfiguration;
import com.philips.cdp.registration.settings.RegistrationEnvironmentConstants;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

import java.util.Locale;

/**
 * Created by 310202337 on 2/15/2016.
 */
public class RegistrationInitializationTest extends InstrumentationTestCase {
    public RegistrationInitializationTest() {
       // super(RegistrationActivity.class);

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());


    }


    public void testEvalConfigurationInitialization(){

        String CONFIGURATION_JSON_PATH = "registration/configuration/configuration_for_test_cases.json";
        RegistrationStaticConfiguration.getInstance().parseConfigurationJson(getInstrumentation().getTargetContext(), CONFIGURATION_JSON_PATH);
        UserRegistrationInitializer.getInstance().initializeEnvironment(getInstrumentation().getTargetContext(), Locale.getDefault());
        assertEquals(Configuration.EVALUATION.getValue(), RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment());
        assertNotNull(UserRegistrationInitializer.getInstance().getRegistrationSettings());



    }

    public void testProdConfigurationInitialization(){
        String CONFIGURATION_JSON_PATH = "registration/configuration/configuration_for_test_cases_prod.json";
        RegistrationStaticConfiguration.getInstance().parseConfigurationJson(getInstrumentation().getTargetContext(), CONFIGURATION_JSON_PATH);
        UserRegistrationInitializer.getInstance().initializeEnvironment(getInstrumentation().getTargetContext(), Locale.getDefault());
        assertEquals(Configuration.PRODUCTION.getValue(), RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment());
        assertNotNull(UserRegistrationInitializer.getInstance().getRegistrationSettings());



    }

    public void testStagConfigurationInitialization(){

        String CONFIGURATION_JSON_PATH = "registration/configuration/configuration_for_test_cases_stag.json";
        RegistrationStaticConfiguration.getInstance().parseConfigurationJson(getInstrumentation().getTargetContext(), CONFIGURATION_JSON_PATH);
        UserRegistrationInitializer.getInstance().initializeEnvironment(getInstrumentation().getTargetContext(), Locale.getDefault());
        assertEquals(Configuration.DEVELOPMENT.getValue(), RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment());
        assertNotNull(UserRegistrationInitializer.getInstance().getRegistrationSettings());


    }

    public void testTestConfigurationInitialization(){

        String CONFIGURATION_JSON_PATH = "registration/configuration/configuration_for_test_cases_test.json";
        RegistrationStaticConfiguration.getInstance().parseConfigurationJson(getInstrumentation().getTargetContext(), CONFIGURATION_JSON_PATH);
        UserRegistrationInitializer.getInstance().initializeEnvironment(getInstrumentation().getTargetContext(), Locale.getDefault());
        assertEquals(Configuration.TESTING.getValue(), RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment());
        assertNotNull(UserRegistrationInitializer.getInstance().getRegistrationSettings());



    }

}
