package com.philips.cdp.registration.settings.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationDynamicConfiguration;
import com.philips.cdp.registration.configuration.RegistrationStaticConfiguration;
import com.philips.cdp.registration.settings.RegistrationEnvironmentConstants;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

import java.util.Locale;

/**
 * Created by 310202337 on 2/15/2016.
 */
public class RegistrationInitializationTest extends InstrumentationTestCase {

    public static final int TIME = 1000*5;

    public RegistrationInitializationTest() {
       // super(RegistrationActivity.class);

    }

    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        context = getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("LOCALEMATCH_PREFERENCE", 0);
        String inputLocale = "en" + "_" + "US";
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("input_locale", inputLocale);
        boolean committed = editor.commit();
        String CONFIGURATION_JSON_PATH = "registration/configuration/configuration_for_test_cases.json";

        RegistrationStaticConfiguration.getInstance().parseConfigurationJson(getInstrumentation().getTargetContext(), CONFIGURATION_JSON_PATH);

    }


    public void testEvalConfigurationInitialization(){


        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(Configuration.EVALUATION);
        UserRegistrationInitializer.getInstance().initializeEnvironment(getInstrumentation().getTargetContext(), Locale.getDefault());

        addNetworkDelay();

        assertEquals(Configuration.EVALUATION.getValue(), RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment());
        assertNotNull(UserRegistrationInitializer.getInstance().getRegistrationSettings());



    }

    protected void addNetworkDelay() {
        try {
            Thread.sleep(TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testProdConfigurationInitialization(){

        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(Configuration.PRODUCTION);
        UserRegistrationInitializer.getInstance().initializeEnvironment(getInstrumentation().getTargetContext(), Locale.getDefault());

        addNetworkDelay();

        assertEquals(Configuration.PRODUCTION.getValue(), RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment());
        assertNotNull(UserRegistrationInitializer.getInstance().getRegistrationSettings());



    }

    public void testStagConfigurationInitialization(){


        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(Configuration.STAGING);
        UserRegistrationInitializer.getInstance().initializeEnvironment(getInstrumentation().getTargetContext(), Locale.getDefault());
        addNetworkDelay();

        assertEquals(Configuration.STAGING.getValue(), RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment());
        assertNotNull(UserRegistrationInitializer.getInstance().getRegistrationSettings());


    }

    public void testTestConfigurationInitialization(){


        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(Configuration.TESTING);
        UserRegistrationInitializer.getInstance().initializeEnvironment(getInstrumentation().getTargetContext(), Locale.getDefault());
        addNetworkDelay();

        assertEquals(Configuration.TESTING.getValue(), RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment());
        assertNotNull(UserRegistrationInitializer.getInstance().getRegistrationSettings());



    }

    public void testDevTestConfigurationInitialization(){


        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(Configuration.DEVELOPMENT);
        UserRegistrationInitializer.getInstance().initializeEnvironment(getInstrumentation().getTargetContext(), Locale.getDefault());
        addNetworkDelay();

        assertEquals(Configuration.DEVELOPMENT.getValue(), RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment());
        assertNotNull(UserRegistrationInitializer.getInstance().getRegistrationSettings());



    }

}
