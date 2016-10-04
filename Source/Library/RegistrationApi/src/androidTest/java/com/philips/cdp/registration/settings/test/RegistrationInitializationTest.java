/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.settings.test;

import android.test.InstrumentationTestCase;

/**
 * Created by 310202337 on 2/15/2016.
 */
public class RegistrationInitializationTest extends InstrumentationTestCase {

/*    public static final int TIME = 1000*5;

    public RegistrationInitializationTest() {
       // super(RegistrationActivity.class);

    }

    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        RLog.init(getInstrumentation().getTargetContext());
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



    }*/

}
